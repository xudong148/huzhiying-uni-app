package com.huzhiying.server.service;

import com.huzhiying.domain.enums.DomainEnums.DispatchMode;
import com.huzhiying.domain.enums.DomainEnums.PaymentStatus;
import com.huzhiying.domain.enums.DomainEnums.ProductOrderStatus;
import com.huzhiying.domain.enums.DomainEnums.QuotationStatus;
import com.huzhiying.domain.enums.DomainEnums.RoleCode;
import com.huzhiying.domain.enums.DomainEnums.ServiceOrderStatus;
import com.huzhiying.domain.model.DomainModels.Address;
import com.huzhiying.domain.model.DomainModels.AuthToken;
import com.huzhiying.domain.model.DomainModels.DispatchTask;
import com.huzhiying.domain.model.DomainModels.MasterProfile;
import com.huzhiying.domain.model.DomainModels.MessageItem;
import com.huzhiying.domain.model.DomainModels.ProductOrder;
import com.huzhiying.domain.model.DomainModels.Quotation;
import com.huzhiying.domain.model.DomainModels.ServiceOrder;
import com.huzhiying.domain.model.DomainModels.User;
import com.huzhiying.server.persistence.PersistenceEntities.ProductEntity;
import com.huzhiying.server.persistence.PersistenceEntities.AddressEntity;
import com.huzhiying.server.persistence.PersistenceEntities.DispatchTaskEntity;
import com.huzhiying.server.persistence.PersistenceEntities.MasterProfileEntity;
import com.huzhiying.server.persistence.PersistenceEntities.MessageItemEntity;
import com.huzhiying.server.persistence.PersistenceEntities.ProductOrderEntity;
import com.huzhiying.server.persistence.PersistenceEntities.QuotationEntity;
import com.huzhiying.server.persistence.PersistenceEntities.QuotationItemEntity;
import com.huzhiying.server.persistence.PersistenceEntities.ServiceItemEntity;
import com.huzhiying.server.persistence.PersistenceEntities.ServiceOrderEntity;
import com.huzhiying.server.persistence.PersistenceEntities.UserEntity;
import com.huzhiying.server.persistence.PersistenceEntities.WalletAccountEntity;
import com.huzhiying.server.persistence.PersistenceEntities.MessageSessionEntity;
import com.huzhiying.server.repository.PlatformRepository;
import com.huzhiying.server.websocket.WebSocketEventGateway;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class PlatformCommandService {

    private final PlatformRepository platformRepository;
    private final PlatformAssembler assembler;
    private final PlatformDomainSupport domainSupport;
    private final PricingEngineService pricingEngineService;
    private final OrderWorkflowService orderWorkflowService;
    private final DispatchLockService dispatchLockService;
    private final WebSocketEventGateway webSocketEventGateway;

    public PlatformCommandService(PlatformRepository platformRepository,
                                  PlatformAssembler assembler,
                                  PlatformDomainSupport domainSupport,
                                  PricingEngineService pricingEngineService,
                                  OrderWorkflowService orderWorkflowService,
                                  DispatchLockService dispatchLockService,
                                  WebSocketEventGateway webSocketEventGateway) {
        this.platformRepository = platformRepository;
        this.assembler = assembler;
        this.domainSupport = domainSupport;
        this.pricingEngineService = pricingEngineService;
        this.orderWorkflowService = orderWorkflowService;
        this.dispatchLockService = dispatchLockService;
        this.webSocketEventGateway = webSocketEventGateway;
    }

    public AuthToken login(String role) {
        RoleCode roleCode = domainSupport.parseRole(role);
        UserEntity user = switch (roleCode) {
            case MASTER -> domainSupport.findUserEntity(PlatformDomainSupport.DEFAULT_MASTER_USER_ID);
            case ADMIN -> platformRepository.findFirstUserByRole(RoleCode.ADMIN)
                    .orElse(domainSupport.findUserEntity(PlatformDomainSupport.DEFAULT_USER_ID));
            default -> domainSupport.findUserEntity(PlatformDomainSupport.DEFAULT_USER_ID);
        };
        String normalizedRole = roleCode.name().toLowerCase();
        return new AuthToken(
                "token-" + user.id + "-" + normalizedRole,
                "refresh-" + user.id + "-" + UUID.randomUUID().toString().replace("-", ""),
                normalizedRole
        );
    }

    public User saveProfile(String nickname, String mobile) {
        UserEntity entity = domainSupport.findUserEntity(PlatformDomainSupport.DEFAULT_USER_ID);
        entity.nickname = nickname == null || nickname.isBlank() ? entity.nickname : nickname;
        entity.mobile = mobile == null || mobile.isBlank() ? entity.mobile : mobile;
        return assembler.toUser(platformRepository.saveUser(entity));
    }

    public Address saveAddress(Long id, String tag, String name, String mobile, String detailAddress, boolean isDefault) {
        AddressEntity entity = id == null ? new AddressEntity() : platformRepository.findAddress(id).orElse(new AddressEntity());
        if (entity.id == null) {
            entity.id = platformRepository.nextLongId("AddressEntity", "id", 0L);
            entity.userId = PlatformDomainSupport.DEFAULT_USER_ID;
        }
        entity.tagName = tag;
        entity.contactName = name;
        entity.mobile = mobile;
        entity.detailAddress = detailAddress;
        entity.cityName = detailAddress != null && detailAddress.contains("上海") ? "上海" : "上海";
        entity.districtName = detailAddress != null && detailAddress.contains("徐汇") ? "徐汇区" : "浦东新区";
        entity.latitude = entity.latitude == null ? 31.2253 : entity.latitude;
        entity.longitude = entity.longitude == null ? 121.5443 : entity.longitude;
        entity.isDefault = isDefault;

        if (Boolean.TRUE.equals(entity.isDefault)) {
            platformRepository.listAddressesByUserId(PlatformDomainSupport.DEFAULT_USER_ID).forEach(address -> {
                if (!address.id.equals(entity.id) && Boolean.TRUE.equals(address.isDefault)) {
                    address.isDefault = false;
                    platformRepository.saveAddress(address);
                }
            });
        }

        return assembler.toAddress(platformRepository.saveAddress(entity));
    }

    public boolean deleteAddress(Long id) {
        AddressEntity entity = platformRepository.findAddress(id).orElseThrow();
        if (!PlatformDomainSupport.DEFAULT_USER_ID.equals(entity.userId)) {
            throw new IllegalStateException("当前地址不属于当前登录用户");
        }
        boolean wasDefault = Boolean.TRUE.equals(entity.isDefault);
        platformRepository.deleteAddress(id);
        if (wasDefault) {
            platformRepository.listAddressesByUserId(PlatformDomainSupport.DEFAULT_USER_ID).stream()
                    .findFirst()
                    .ifPresent(address -> {
                        address.isDefault = true;
                        platformRepository.saveAddress(address);
                    });
        }
        return true;
    }

    public ServiceOrder createServiceOrder(Long serviceItemId, String title, String appointment, Long addressId,
                                           String description, boolean emergency, boolean nightService) {
        ServiceItemEntity serviceItem = serviceItemId == null
                ? domainSupport.findServiceItemByTitle(title).orElse(domainSupport.findServiceItemEntity(201L))
                : domainSupport.findServiceItemEntity(serviceItemId);
        AddressEntity address = resolveAddress(addressId);
        BigDecimal amount = pricingEngineService.estimate(serviceItem.basePrice, serviceItem.doorPrice, emergency, nightService);

        ServiceOrderEntity orderEntity = new ServiceOrderEntity();
        orderEntity.id = "SO" + System.currentTimeMillis();
        orderEntity.serviceItemId = serviceItem.id;
        orderEntity.title = serviceItem.name;
        orderEntity.status = ServiceOrderStatus.PENDING_DISPATCH;
        orderEntity.paymentStatus = PaymentStatus.PARTIAL_PAID;
        orderEntity.userId = PlatformDomainSupport.DEFAULT_USER_ID;
        orderEntity.addressId = address.id;
        orderEntity.appointment = appointment;
        orderEntity.amount = amount;
        orderEntity.dispatchMode = DispatchMode.ROB;
        orderEntity.etaText = emergency ? "15 分钟" : "30 分钟";
        orderEntity.descriptionText = description;
        orderEntity.emergency = emergency;
        orderEntity.nightService = nightService;
        orderEntity.createdAt = LocalDateTime.now();
        orderEntity.updatedAt = LocalDateTime.now();
        platformRepository.saveServiceOrder(orderEntity);

        domainSupport.saveStep(orderEntity.id, "created", "订单创建", "用户已提交工单并完成预付款", true);
        domainSupport.saveStep(orderEntity.id, "dispatch", "待派单", "系统正在匹配可服务的师傅", false);

        DispatchTaskEntity taskEntity = new DispatchTaskEntity();
        taskEntity.id = "DISP-" + System.currentTimeMillis();
        taskEntity.orderId = orderEntity.id;
        taskEntity.title = orderEntity.title;
        taskEntity.income = amount.add(BigDecimal.valueOf(80));
        taskEntity.distanceText = emergency ? "2.1km" : "3.8km";
        taskEntity.areaText = address.districtName;
        taskEntity.dispatchMode = DispatchMode.ROB;
        taskEntity.taskStatus = "PENDING";
        taskEntity.tagsText = serviceItem.tagsText;
        taskEntity.createdAt = LocalDateTime.now();
        platformRepository.saveDispatchTask(taskEntity);

        webSocketEventGateway.publishDispatchUpdate(Map.of(
                "taskId", taskEntity.id,
                "orderId", orderEntity.id,
                "status", "PENDING",
                "title", orderEntity.title
        ));

        return domainSupport.buildServiceOrder(orderEntity);
    }

    public ProductOrder createProductOrder(Long productId, Long skuId, Long addressId) {
        ProductEntity productEntity = platformRepository.findProduct(productId).orElseThrow();
        AddressEntity addressEntity = resolveAddress(addressId);
        var skuEntity = platformRepository.listSkusByProductIds(java.util.List.of(productId)).stream()
                .filter(item -> skuId == null || item.id.equals(skuId))
                .findFirst()
                .orElseThrow();

        ProductOrderEntity orderEntity = new ProductOrderEntity();
        orderEntity.id = "PO" + System.currentTimeMillis();
        orderEntity.productId = productEntity.id;
        orderEntity.title = productEntity.name + " · " + skuEntity.name;
        orderEntity.status = ProductOrderStatus.PENDING_PAYMENT;
        orderEntity.paymentStatus = PaymentStatus.UNPAID;
        orderEntity.userId = PlatformDomainSupport.DEFAULT_USER_ID;
        orderEntity.addressId = addressEntity.id;
        orderEntity.amount = skuEntity.price == null ? productEntity.price : skuEntity.price;
        orderEntity.createInstallOrder = Boolean.TRUE.equals(productEntity.createInstallOrder);
        orderEntity.createdAt = LocalDateTime.now();
        platformRepository.saveProductOrder(orderEntity);
        return domainSupport.buildProductOrder(orderEntity);
    }

    public ServiceOrder updateServiceOrderStatus(String id, ServiceOrderStatus status) {
        ServiceOrderEntity entity = domainSupport.findServiceOrderEntity(id);
        ServiceOrder updated = orderWorkflowService.advance(domainSupport.buildServiceOrder(entity), status);
        entity.status = updated.status();
        entity.paymentStatus = updated.paymentStatus();
        entity.updatedAt = LocalDateTime.now();
        platformRepository.saveServiceOrder(entity);
        domainSupport.saveStep(entity.id, status.name().toLowerCase(), domainSupport.statusLabel(status),
                "订单状态已更新为 " + domainSupport.statusLabel(status), true);
        if (status == ServiceOrderStatus.COMPLETED) {
            domainSupport.createSettlementIfNeeded(entity);
        }
        webSocketEventGateway.publishOrderStatusChanged(entity.id, domainSupport.buildServiceOrder(entity));
        return domainSupport.buildServiceOrder(entity);
    }

    public Quotation createQuotation(String orderId, String remark) {
        ServiceOrderEntity orderEntity = domainSupport.findServiceOrderEntity(orderId);
        QuotationEntity quotationEntity = platformRepository.findQuotationByOrderId(orderId).orElseGet(QuotationEntity::new);
        if (quotationEntity.id == null) {
            quotationEntity.id = "QT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            quotationEntity.orderId = orderId;
        }
        quotationEntity.totalAmount = BigDecimal.valueOf(88);
        quotationEntity.status = QuotationStatus.PENDING_CONFIRM;
        quotationEntity.remarkText = remark == null || remark.isBlank()
                ? "新增辅材与工时费用，待用户确认后继续施工。"
                : remark;
        quotationEntity.createdAt = LocalDateTime.now();
        platformRepository.saveQuotation(quotationEntity);

        platformRepository.deleteQuotationItems(quotationEntity.id);
        QuotationItemEntity itemEntity = new QuotationItemEntity();
        itemEntity.quotationId = quotationEntity.id;
        itemEntity.name = "增项配件";
        itemEntity.amount = BigDecimal.valueOf(88);
        platformRepository.saveQuotationItem(itemEntity);

        orderEntity.status = ServiceOrderStatus.WAITING_SUPPLEMENT_PAYMENT;
        orderEntity.updatedAt = LocalDateTime.now();
        platformRepository.saveServiceOrder(orderEntity);
        domainSupport.saveStep(orderId, "quotation", "增项报价", "师傅已提交增项报价，等待用户确认", true);

        webSocketEventGateway.publishQuotationCreated(orderId, domainSupport.buildQuotation(orderId).orElseThrow());

        return domainSupport.buildQuotation(orderId).orElseThrow();
    }

    public ServiceOrder confirmQuotation(String quotationId) {
        QuotationEntity quotationEntity = platformRepository.findQuotation(quotationId).orElseThrow();
        ServiceOrderEntity orderEntity = domainSupport.findServiceOrderEntity(quotationEntity.orderId);
        ServiceOrder updated = orderWorkflowService.confirmQuotation(domainSupport.buildServiceOrder(orderEntity));

        quotationEntity.status = QuotationStatus.CONFIRMED;
        platformRepository.saveQuotation(quotationEntity);

        orderEntity.status = updated.status();
        orderEntity.paymentStatus = updated.paymentStatus();
        orderEntity.amount = updated.amount();
        orderEntity.updatedAt = LocalDateTime.now();
        platformRepository.saveServiceOrder(orderEntity);
        domainSupport.saveStep(orderEntity.id, "supplement_paid", "补款完成", "用户已确认增项并完成补款", true);
        webSocketEventGateway.publishOrderStatusChanged(orderEntity.id, domainSupport.buildServiceOrder(orderEntity));

        return domainSupport.buildServiceOrder(orderEntity);
    }

    public DispatchTask claimTask(String id, String masterName) {
        return assignTask(id, masterName, false);
    }

    public DispatchTask forceAssignTask(String id, String masterName) {
        return assignTask(id, masterName, true);
    }

    public MasterProfile applyMaster(String realName, String mobile, String skills, String area) {
        Long nextUserId = platformRepository.nextLongId("UserEntity", "id", 10000L);
        UserEntity userEntity = new UserEntity();
        userEntity.id = nextUserId;
        userEntity.nickname = realName;
        userEntity.mobile = mobile;
        userEntity.roleCode = RoleCode.MASTER;
        userEntity.avatar = "/static/user.png";
        userEntity.levelName = "认证工程师";
        platformRepository.saveUser(userEntity);

        MasterProfileEntity masterProfileEntity = new MasterProfileEntity();
        masterProfileEntity.id = platformRepository.nextLongId("MasterProfileEntity", "id", 0L);
        masterProfileEntity.userId = nextUserId;
        masterProfileEntity.realName = realName;
        masterProfileEntity.skillTags = skills;
        masterProfileEntity.serviceArea = area;
        masterProfileEntity.deposit = BigDecimal.valueOf(3000);
        masterProfileEntity.creditScore = 100;
        masterProfileEntity.online = false;
        masterProfileEntity.listening = true;
        masterProfileEntity.maxDistanceKm = 20;
        masterProfileEntity.privacyNumber = true;
        platformRepository.saveMasterProfile(masterProfileEntity);

        WalletAccountEntity walletAccountEntity = new WalletAccountEntity();
        walletAccountEntity.id = platformRepository.nextLongId("WalletAccountEntity", "id", 0L);
        walletAccountEntity.masterUserId = nextUserId;
        walletAccountEntity.availableAmount = BigDecimal.ZERO;
        walletAccountEntity.frozenAmount = BigDecimal.valueOf(3000);
        walletAccountEntity.todayIncome = BigDecimal.ZERO;
        platformRepository.saveWalletAccount(walletAccountEntity);

        return assembler.toMasterProfile(masterProfileEntity, userEntity);
    }

    public Map<String, Object> saveMasterSettings(boolean listening, String maxDistance, boolean privacyNumber) {
        MasterProfileEntity entity = platformRepository.findMasterProfileByUserId(PlatformDomainSupport.DEFAULT_MASTER_USER_ID).orElseThrow();
        entity.listening = listening;
        entity.maxDistanceKm = parseDistance(maxDistance, entity.maxDistanceKm == null ? 20 : entity.maxDistanceKm);
        entity.privacyNumber = privacyNumber;
        platformRepository.saveMasterProfile(entity);
        return Map.of(
                "listening", Boolean.TRUE.equals(entity.listening),
                "maxDistance", entity.maxDistanceKm + "km",
                "privacyNumber", Boolean.TRUE.equals(entity.privacyNumber)
        );
    }

    public MessageItem sendMessage(String sessionId, String senderCode, String content) {
        MessageItemEntity entity = new MessageItemEntity();
        entity.sessionId = sessionId;
        entity.senderCode = senderCode;
        entity.messageType = "text";
        entity.contentText = content;
        entity.messageTime = "刚刚";
        MessageItem messageItem = assembler.toMessageItem(platformRepository.saveMessageItem(entity));
        MessageSessionEntity sessionEntity = platformRepository.findMessageSession(sessionId).orElse(null);
        webSocketEventGateway.publishChatMessage(Map.of(
                "sessionId", sessionId,
                "orderId", sessionEntity == null ? "" : sessionEntity.orderId,
                "message", messageItem
        ));
        return messageItem;
    }

    public Map<String, Object> refundOrder(String orderId) {
        Optional<ServiceOrderEntity> serviceOrder = platformRepository.findServiceOrder(orderId);
        if (serviceOrder.isPresent()) {
            ServiceOrderEntity entity = serviceOrder.get();
            entity.paymentStatus = PaymentStatus.REFUNDING;
            entity.status = ServiceOrderStatus.REFUNDING;
            entity.updatedAt = LocalDateTime.now();
            platformRepository.saveServiceOrder(entity);
            domainSupport.saveStep(entity.id, "refunding", "退款中", "用户已发起退款申请", true);
            webSocketEventGateway.publishOrderStatusChanged(entity.id, domainSupport.buildServiceOrder(entity));
            return Map.of("orderId", orderId, "status", "ACCEPTED");
        }

        ProductOrderEntity productOrderEntity = platformRepository.findProductOrder(orderId).orElseThrow();
        productOrderEntity.status = com.huzhiying.domain.enums.DomainEnums.ProductOrderStatus.REFUNDING;
        productOrderEntity.paymentStatus = PaymentStatus.REFUNDING;
        platformRepository.saveProductOrder(productOrderEntity);
        return Map.of("orderId", orderId, "status", "ACCEPTED");
    }

    public Map<String, Object> handleWechatCallback(String orderId) {
        platformRepository.findServiceOrder(orderId).ifPresent(order -> {
            order.paymentStatus = PaymentStatus.PAID;
            order.updatedAt = LocalDateTime.now();
            platformRepository.saveServiceOrder(order);
            webSocketEventGateway.publishOrderStatusChanged(order.id, domainSupport.buildServiceOrder(order));
        });
        platformRepository.findProductOrder(orderId).ifPresent(order -> {
            order.paymentStatus = PaymentStatus.PAID;
            if (order.status == ProductOrderStatus.PENDING_PAYMENT) {
                order.status = ProductOrderStatus.PENDING_SHIPMENT;
            }
            if (Boolean.TRUE.equals(order.createInstallOrder)
                    && (order.installServiceOrderId == null || order.installServiceOrderId.isBlank())) {
                order.installServiceOrderId = createInstallServiceOrder(order);
            }
            platformRepository.saveProductOrder(order);
        });
        return Map.of("orderId", orderId, "status", "SUCCESS");
    }

    private DispatchTask assignTask(String id, String masterName, boolean forceAssign) {
        if (!dispatchLockService.tryLock(id)) {
            throw new IllegalStateException("当前派单任务正在被其他师傅处理");
        }

        try {
            DispatchTaskEntity taskEntity = platformRepository.findDispatchTask(id).orElseThrow();
            MasterProfileEntity masterProfile = domainSupport.resolveMaster(masterName);
            if (!forceAssign && taskEntity.currentMasterUserId != null && !taskEntity.currentMasterUserId.equals(masterProfile.userId)) {
                throw new IllegalStateException("当前任务已被其他师傅领取");
            }

            taskEntity.currentMasterUserId = masterProfile.userId;
            taskEntity.taskStatus = forceAssign ? "FORCE_ASSIGNED" : "CLAIMED";
            platformRepository.saveDispatchTask(taskEntity);

            ServiceOrderEntity orderEntity = domainSupport.findServiceOrderEntity(taskEntity.orderId);
            orderEntity.masterUserId = masterProfile.userId;
            if (orderEntity.status == ServiceOrderStatus.PENDING_DISPATCH) {
                orderEntity.status = ServiceOrderStatus.PENDING_ACCEPT;
            }
            orderEntity.updatedAt = LocalDateTime.now();
            platformRepository.saveServiceOrder(orderEntity);

            String actionLabel = forceAssign ? "强派完成" : "师傅接单";
            String actionDesc = forceAssign
                    ? masterProfile.realName + " 已被平台强制派单"
                    : masterProfile.realName + " 已接单并准备出发";
            domainSupport.saveStep(orderEntity.id, forceAssign ? "assigned" : "accepted", actionLabel, actionDesc, true);
            webSocketEventGateway.publishDispatchUpdate(Map.of(
                    "taskId", taskEntity.id,
                    "orderId", taskEntity.orderId,
                    "status", taskEntity.taskStatus,
                    "masterName", masterProfile.realName
            ));
            webSocketEventGateway.publishOrderStatusChanged(orderEntity.id, domainSupport.buildServiceOrder(orderEntity));
            return domainSupport.buildDispatchTask(taskEntity);
        } finally {
            dispatchLockService.release(id);
        }
    }

    private int parseDistance(String value, int fallback) {
        if (value == null || value.isBlank()) {
            return fallback;
        }
        String digits = value.replaceAll("[^0-9]", "");
        if (digits.isBlank()) {
            return fallback;
        }
        return Integer.parseInt(digits);
    }

    private AddressEntity resolveAddress(Long addressId) {
        return addressId == null
                ? platformRepository.listAddressesByUserId(PlatformDomainSupport.DEFAULT_USER_ID).stream().findFirst().orElseThrow()
                : platformRepository.findAddress(addressId).orElseThrow();
    }

    private String createInstallServiceOrder(ProductOrderEntity productOrderEntity) {
        ServiceItemEntity installService = platformRepository.findServiceItem(301L)
                .orElse(domainSupport.findServiceItemEntity(201L));
        AddressEntity addressEntity = resolveAddress(productOrderEntity.addressId);

        ServiceOrderEntity orderEntity = new ServiceOrderEntity();
        orderEntity.id = "SO" + System.currentTimeMillis();
        orderEntity.serviceItemId = installService.id;
        orderEntity.title = installService.name;
        orderEntity.status = ServiceOrderStatus.PENDING_DISPATCH;
        orderEntity.paymentStatus = PaymentStatus.PAID;
        orderEntity.userId = productOrderEntity.userId;
        orderEntity.addressId = addressEntity.id;
        orderEntity.appointment = "待用户确认上门时间";
        orderEntity.amount = installService.basePrice == null ? BigDecimal.ZERO : installService.basePrice;
        orderEntity.dispatchMode = DispatchMode.ROB;
        orderEntity.etaText = "待派单";
        orderEntity.descriptionText = productOrderEntity.title + " 已支付，自动生成安装工单";
        orderEntity.emergency = false;
        orderEntity.nightService = false;
        orderEntity.createdAt = LocalDateTime.now();
        orderEntity.updatedAt = LocalDateTime.now();
        platformRepository.saveServiceOrder(orderEntity);

        domainSupport.saveStep(orderEntity.id, "created", "安装工单创建", "商品支付完成后自动生成安装服务单", true);
        domainSupport.saveStep(orderEntity.id, "dispatch", "等待派单", "平台正在为安装工单匹配附近师傅", false);

        DispatchTaskEntity taskEntity = new DispatchTaskEntity();
        taskEntity.id = "DISP-" + System.currentTimeMillis();
        taskEntity.orderId = orderEntity.id;
        taskEntity.title = orderEntity.title;
        taskEntity.income = orderEntity.amount.add(BigDecimal.valueOf(60));
        taskEntity.distanceText = "3.5km";
        taskEntity.areaText = addressEntity.districtName;
        taskEntity.dispatchMode = DispatchMode.ROB;
        taskEntity.taskStatus = "PENDING";
        taskEntity.tagsText = installService.tagsText;
        taskEntity.createdAt = LocalDateTime.now();
        platformRepository.saveDispatchTask(taskEntity);

        webSocketEventGateway.publishDispatchUpdate(Map.of(
                "taskId", taskEntity.id,
                "orderId", orderEntity.id,
                "status", taskEntity.taskStatus,
                "title", orderEntity.title
        ));
        return orderEntity.id;
    }
}
