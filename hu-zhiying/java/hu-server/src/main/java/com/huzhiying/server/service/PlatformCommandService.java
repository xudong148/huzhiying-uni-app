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
import com.huzhiying.server.dto.SupportDtos;
import com.huzhiying.server.persistence.PersistenceEntities.AddressEntity;
import com.huzhiying.server.persistence.PersistenceEntities.DispatchTaskEntity;
import com.huzhiying.server.persistence.PersistenceEntities.MasterProfileEntity;
import com.huzhiying.server.persistence.PersistenceEntities.MediaFileEntity;
import com.huzhiying.server.persistence.PersistenceEntities.MessageItemEntity;
import com.huzhiying.server.persistence.PersistenceEntities.MessageSessionEntity;
import com.huzhiying.server.persistence.PersistenceEntities.OrderTrackPointEntity;
import com.huzhiying.server.persistence.PersistenceEntities.ProductEntity;
import com.huzhiying.server.persistence.PersistenceEntities.ProductOrderEntity;
import com.huzhiying.server.persistence.PersistenceEntities.QuotationEntity;
import com.huzhiying.server.persistence.PersistenceEntities.QuotationItemEntity;
import com.huzhiying.server.persistence.PersistenceEntities.ServiceItemEntity;
import com.huzhiying.server.persistence.PersistenceEntities.ServiceOrderEntity;
import com.huzhiying.server.persistence.PersistenceEntities.UserEntity;
import com.huzhiying.server.persistence.PersistenceEntities.WalletAccountEntity;
import com.huzhiying.server.repository.PlatformRepository;
import com.huzhiying.server.websocket.WebSocketEventGateway;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class PlatformCommandService {

    private static final DateTimeFormatter MESSAGE_TIME = DateTimeFormatter.ofPattern("MM-dd HH:mm");

    private final PlatformRepository platformRepository;
    private final PlatformAssembler assembler;
    private final PlatformDomainSupport domainSupport;
    private final PricingEngineService pricingEngineService;
    private final OrderWorkflowService orderWorkflowService;
    private final DispatchLockService dispatchLockService;
    private final WebSocketEventGateway webSocketEventGateway;
    private final FileStorageService fileStorageService;

    public PlatformCommandService(PlatformRepository platformRepository, PlatformAssembler assembler,
                                  PlatformDomainSupport domainSupport, PricingEngineService pricingEngineService,
                                  OrderWorkflowService orderWorkflowService, DispatchLockService dispatchLockService,
                                  WebSocketEventGateway webSocketEventGateway, FileStorageService fileStorageService) {
        this.platformRepository = platformRepository;
        this.assembler = assembler;
        this.domainSupport = domainSupport;
        this.pricingEngineService = pricingEngineService;
        this.orderWorkflowService = orderWorkflowService;
        this.dispatchLockService = dispatchLockService;
        this.webSocketEventGateway = webSocketEventGateway;
        this.fileStorageService = fileStorageService;
    }

    public AuthToken login(String role) {
        RoleCode roleCode = domainSupport.parseRole(role);
        UserEntity user = switch (roleCode) {
            case MASTER -> domainSupport.findUserEntity(PlatformDomainSupport.DEFAULT_MASTER_USER_ID);
            case ADMIN -> platformRepository.findFirstUserByRole(RoleCode.ADMIN).orElse(domainSupport.findUserEntity(PlatformDomainSupport.DEFAULT_USER_ID));
            default -> domainSupport.findUserEntity(PlatformDomainSupport.DEFAULT_USER_ID);
        };
        String normalizedRole = roleCode.name().toLowerCase();
        return new AuthToken("token-" + user.id + "-" + normalizedRole, "refresh-" + user.id + "-" + UUID.randomUUID().toString().replace("-", ""), normalizedRole);
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
        entity.cityName = detailAddress != null && detailAddress.contains("杭州") ? "杭州" : "上海";
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
        if (entity.userId == null || !entity.userId.equals(PlatformDomainSupport.DEFAULT_USER_ID)) {
            throw new IllegalStateException("当前地址不属于当前登录用户");
        }
        boolean wasDefault = Boolean.TRUE.equals(entity.isDefault);
        platformRepository.deleteAddress(id);
        if (wasDefault) {
            platformRepository.listAddressesByUserId(PlatformDomainSupport.DEFAULT_USER_ID).stream().findFirst().ifPresent(address -> {
                address.isDefault = true;
                platformRepository.saveAddress(address);
            });
        }
        return true;
    }

    public ServiceOrder createServiceOrder(Long serviceItemId, String title, String appointment, Long addressId,
                                           String description, boolean emergency, boolean nightService, List<Long> evidenceFileIds) {
        ServiceItemEntity serviceItem = serviceItemId == null ? domainSupport.findServiceItemByTitle(title).orElse(domainSupport.findServiceItemEntity(201L)) : domainSupport.findServiceItemEntity(serviceItemId);
        AddressEntity address = resolveAddress(addressId);
        BigDecimal amount = pricingEngineService.estimate(serviceItem.basePrice, serviceItem.doorPrice, emergency, nightService);
        ServiceOrderEntity entity = new ServiceOrderEntity();
        entity.id = "SO" + System.currentTimeMillis();
        entity.serviceItemId = serviceItem.id;
        entity.title = serviceItem.name;
        entity.status = ServiceOrderStatus.PENDING_PAYMENT;
        entity.paymentStatus = PaymentStatus.UNPAID;
        entity.userId = PlatformDomainSupport.DEFAULT_USER_ID;
        entity.addressId = address.id;
        entity.appointment = appointment;
        entity.amount = amount;
        entity.dispatchMode = DispatchMode.ROB;
        entity.etaText = "支付完成后开始派单";
        entity.descriptionText = description;
        entity.emergency = emergency;
        entity.nightService = nightService;
        entity.createdAt = LocalDateTime.now();
        entity.updatedAt = LocalDateTime.now();
        platformRepository.saveServiceOrder(entity);
        domainSupport.saveStep(entity.id, "created", "订单创建", "用户已提交工单，等待完成预付款", true);
        domainSupport.saveStep(entity.id, "payment_required", "待支付", "支付成功后系统才会开始派单", false);
        createTrackPoint(entity.id, "CREATED", "订单创建", "订单已创建，等待完成预付款", address.latitude, address.longitude);
        if (evidenceFileIds != null && !evidenceFileIds.isEmpty()) {
            markFilesAsBound(evidenceFileIds, "order_evidence", entity.id);
        }
        return domainSupport.buildServiceOrder(entity);
    }

    public ProductOrder createProductOrder(Long productId, Long skuId, Long addressId) {
        ProductEntity product = platformRepository.findProduct(productId).orElseThrow();
        AddressEntity address = resolveAddress(addressId);
        var sku = platformRepository.listSkusByProductIds(List.of(productId)).stream().filter(item -> skuId == null || item.id.equals(skuId)).findFirst().orElseThrow();
        ProductOrderEntity entity = new ProductOrderEntity();
        entity.id = "PO" + System.currentTimeMillis();
        entity.productId = product.id;
        entity.title = product.name + " / " + sku.name;
        entity.status = ProductOrderStatus.PENDING_PAYMENT;
        entity.paymentStatus = PaymentStatus.UNPAID;
        entity.userId = PlatformDomainSupport.DEFAULT_USER_ID;
        entity.addressId = address.id;
        entity.amount = sku.price == null ? product.price : sku.price;
        entity.createInstallOrder = Boolean.TRUE.equals(product.createInstallOrder);
        entity.createdAt = LocalDateTime.now();
        platformRepository.saveProductOrder(entity);
        return domainSupport.buildProductOrder(entity);
    }

    public ServiceOrder updateServiceOrderStatus(String id, ServiceOrderStatus status) {
        ServiceOrderEntity entity = domainSupport.findServiceOrderEntity(id);
        ServiceOrder updated = orderWorkflowService.advance(domainSupport.buildServiceOrder(entity), status);
        entity.status = updated.status();
        entity.paymentStatus = updated.paymentStatus();
        entity.updatedAt = LocalDateTime.now();
        platformRepository.saveServiceOrder(entity);
        String label = domainSupport.statusLabel(status);
        domainSupport.saveStep(entity.id, status.name().toLowerCase(), label, "订单状态已更新为" + label, true);
        createTrackPoint(entity.id, status.name(), label, "订单状态已更新", null, null);
        if (status == ServiceOrderStatus.COMPLETED) {
            domainSupport.createSettlementIfNeeded(entity);
        }
        webSocketEventGateway.publishOrderStatusChanged(entity.id, domainSupport.buildServiceOrder(entity));
        return domainSupport.buildServiceOrder(entity);
    }

    public Quotation createQuotation(String orderId, String remark) {
        ServiceOrderEntity order = domainSupport.findServiceOrderEntity(orderId);
        QuotationEntity quotation = platformRepository.findQuotationByOrderId(orderId).orElseGet(QuotationEntity::new);
        if (quotation.id == null) {
            quotation.id = "QT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            quotation.orderId = orderId;
        }
        quotation.totalAmount = BigDecimal.valueOf(88);
        quotation.status = QuotationStatus.PENDING_CONFIRM;
        quotation.remarkText = remark == null || remark.isBlank() ? "新增辅材与工时费用，待用户确认后继续施工。" : remark;
        quotation.createdAt = LocalDateTime.now();
        platformRepository.saveQuotation(quotation);
        platformRepository.deleteQuotationItems(quotation.id);
        QuotationItemEntity item = new QuotationItemEntity();
        item.quotationId = quotation.id;
        item.name = "新增配件";
        item.amount = BigDecimal.valueOf(88);
        platformRepository.saveQuotationItem(item);
        order.status = ServiceOrderStatus.WAITING_SUPPLEMENT_PAYMENT;
        order.updatedAt = LocalDateTime.now();
        platformRepository.saveServiceOrder(order);
        domainSupport.saveStep(orderId, "quotation", "增项报价", "师傅已提交增项报价，等待用户确认", true);
        createTrackPoint(orderId, "QUOTATION", "增项报价", "师傅已提交报价单", null, null);
        webSocketEventGateway.publishQuotationCreated(orderId, domainSupport.buildQuotation(orderId).orElseThrow());
        return domainSupport.buildQuotation(orderId).orElseThrow();
    }

    public ServiceOrder confirmQuotation(String quotationId) {
        QuotationEntity quotation = platformRepository.findQuotation(quotationId).orElseThrow();
        ServiceOrderEntity order = domainSupport.findServiceOrderEntity(quotation.orderId);
        if (quotation.status == QuotationStatus.CONFIRMED) {
            return domainSupport.buildServiceOrder(order);
        }
        ServiceOrder updated = orderWorkflowService.confirmQuotation(domainSupport.buildServiceOrder(order));
        quotation.status = QuotationStatus.CONFIRMED;
        platformRepository.saveQuotation(quotation);
        order.status = updated.status();
        order.paymentStatus = updated.paymentStatus();
        order.amount = updated.amount();
        order.updatedAt = LocalDateTime.now();
        platformRepository.saveServiceOrder(order);
        domainSupport.saveStep(order.id, "quotation_confirmed", "报价已确认", "用户已确认增项报价，等待完成补款", true);
        createTrackPoint(order.id, "QUOTATION_CONFIRMED", "报价已确认", "用户已确认报价，等待支付增项费用", null, null);
        webSocketEventGateway.publishOrderStatusChanged(order.id, domainSupport.buildServiceOrder(order));
        return domainSupport.buildServiceOrder(order);
    }

    public DispatchTask claimTask(String id, String masterName) {
        return assignTask(id, masterName, false);
    }

    public DispatchTask forceAssignTask(String id, String masterName) {
        return assignTask(id, masterName, true);
    }

    public MasterProfile applyMaster(String realName, String mobile, String skills, String area) {
        Long nextUserId = platformRepository.nextLongId("UserEntity", "id", 10000L);
        UserEntity user = new UserEntity();
        user.id = nextUserId;
        user.nickname = realName;
        user.mobile = mobile;
        user.roleCode = RoleCode.MASTER;
        user.avatar = "/static/user.png";
        user.levelName = "认证工程师";
        platformRepository.saveUser(user);
        MasterProfileEntity profile = new MasterProfileEntity();
        profile.id = platformRepository.nextLongId("MasterProfileEntity", "id", 0L);
        profile.userId = nextUserId;
        profile.realName = realName;
        profile.skillTags = skills;
        profile.serviceArea = area;
        profile.deposit = BigDecimal.valueOf(3000);
        profile.creditScore = 100;
        profile.online = false;
        profile.listening = true;
        profile.maxDistanceKm = 20;
        profile.privacyNumber = true;
        platformRepository.saveMasterProfile(profile);
        WalletAccountEntity wallet = new WalletAccountEntity();
        wallet.id = platformRepository.nextLongId("WalletAccountEntity", "id", 0L);
        wallet.masterUserId = nextUserId;
        wallet.availableAmount = BigDecimal.ZERO;
        wallet.frozenAmount = BigDecimal.valueOf(3000);
        wallet.todayIncome = BigDecimal.ZERO;
        platformRepository.saveWalletAccount(wallet);
        return assembler.toMasterProfile(profile, user);
    }

    public Map<String, Object> saveMasterSettings(boolean listening, String maxDistance, boolean privacyNumber) {
        MasterProfileEntity entity = platformRepository.findMasterProfileByUserId(PlatformDomainSupport.DEFAULT_MASTER_USER_ID).orElseThrow();
        entity.listening = listening;
        entity.maxDistanceKm = parseDistance(maxDistance, entity.maxDistanceKm == null ? 20 : entity.maxDistanceKm);
        entity.privacyNumber = privacyNumber;
        platformRepository.saveMasterProfile(entity);
        return Map.of("listening", Boolean.TRUE.equals(entity.listening), "maxDistance", entity.maxDistanceKm + "km", "privacyNumber", Boolean.TRUE.equals(entity.privacyNumber));
    }

    public MessageItem sendMessage(String sessionId, String senderCode, String messageType, String content) {
        MessageItemEntity entity = new MessageItemEntity();
        entity.sessionId = sessionId;
        entity.senderCode = senderCode;
        entity.messageType = messageType == null || messageType.isBlank() ? "text" : messageType;
        entity.contentText = content;
        entity.messageTime = LocalDateTime.now().format(MESSAGE_TIME);
        MessageItem message = assembler.toMessageItem(platformRepository.saveMessageItem(entity));
        MessageSessionEntity session = platformRepository.findMessageSession(sessionId).orElse(null);
        webSocketEventGateway.publishChatMessage(Map.of("sessionId", sessionId, "orderId", session == null ? "" : session.orderId, "message", message));
        return message;
    }

    public Map<String, Object> refundOrder(String orderId) {
        Optional<ServiceOrderEntity> serviceOrder = platformRepository.findServiceOrder(orderId);
        if (serviceOrder.isPresent()) {
            ServiceOrderEntity entity = serviceOrder.get();
            entity.paymentStatus = PaymentStatus.REFUNDING;
            entity.status = ServiceOrderStatus.REFUNDING;
            entity.updatedAt = LocalDateTime.now();
            platformRepository.saveServiceOrder(entity);
            domainSupport.saveStep(entity.id, "refunding", "退款处理中", "用户已发起退款申请", true);
            createTrackPoint(entity.id, "REFUNDING", "退款处理中", "用户已提交售后退款申请", null, null);
            webSocketEventGateway.publishOrderStatusChanged(entity.id, domainSupport.buildServiceOrder(entity));
            return Map.of("orderId", orderId, "status", "ACCEPTED");
        }
        ProductOrderEntity productOrder = platformRepository.findProductOrder(orderId).orElseThrow();
        productOrder.status = ProductOrderStatus.REFUNDING;
        productOrder.paymentStatus = PaymentStatus.REFUNDING;
        platformRepository.saveProductOrder(productOrder);
        return Map.of("orderId", orderId, "status", "ACCEPTED");
    }

    public ServiceOrder cancelOrder(String orderId, String reason) {
        ServiceOrderEntity entity = domainSupport.findServiceOrderEntity(orderId);
        if (entity.status == ServiceOrderStatus.COMPLETED) {
            throw new IllegalStateException("已完工订单不能取消");
        }
        entity.status = ServiceOrderStatus.CANCELLED;
        entity.updatedAt = LocalDateTime.now();
        platformRepository.saveServiceOrder(entity);
        String finalReason = reason == null || reason.isBlank() ? "用户主动取消订单" : reason;
        domainSupport.saveStep(orderId, "cancelled", "订单取消", finalReason, true);
        createTrackPoint(orderId, "CANCELLED", "订单取消", finalReason, null, null);
        webSocketEventGateway.publishOrderStatusChanged(orderId, domainSupport.buildServiceOrder(entity));
        return domainSupport.buildServiceOrder(entity);
    }

    public Map<String, Object> urgeOrder(String orderId, String remark) {
        ServiceOrderEntity entity = domainSupport.findServiceOrderEntity(orderId);
        String finalRemark = remark == null || remark.isBlank() ? "用户发起催单" : remark;
        domainSupport.saveStep(orderId, "urge", "催单提醒", finalRemark, true);
        createTrackPoint(orderId, "URGE", "催单提醒", finalRemark, null, null);
        webSocketEventGateway.publishOrderStatusChanged(orderId, domainSupport.buildServiceOrder(entity));
        return Map.of("orderId", orderId, "status", entity.status.name(), "remark", finalRemark);
    }

    public Map<String, Object> masterCheckIn(String orderId, Double latitude, Double longitude, Double accuracy) {
        ServiceOrderEntity order = domainSupport.findServiceOrderEntity(orderId);
        AddressEntity address = resolveAddress(order.addressId);
        double distanceMeters = distanceMeters(latitude, longitude, address.latitude, address.longitude);
        boolean verified = latitude != null && longitude != null && distanceMeters <= 3000D;
        if (order.status != ServiceOrderStatus.ARRIVED) {
            updateServiceOrderStatus(orderId, ServiceOrderStatus.ARRIVED);
        }
        String description = verified ? "签到成功，距服务地址约 " + BigDecimal.valueOf(distanceMeters).setScale(0, RoundingMode.HALF_UP) + " 米" : "已记录到场签到，定位精度 " + (accuracy == null ? "未知" : accuracy + " 米");
        createTrackPoint(orderId, "CHECK_IN", "到场签到", description, latitude, longitude);
        return Map.of("orderId", orderId, "verified", verified, "distanceMeters", BigDecimal.valueOf(distanceMeters).setScale(0, RoundingMode.HALF_UP), "status", domainSupport.statusLabel(ServiceOrderStatus.ARRIVED));
    }

    public List<SupportDtos.MediaFilePayload> attachOrderMedia(String orderId, String stage, List<Long> fileIds, String note) {
        domainSupport.findServiceOrderEntity(orderId);
        if (fileIds == null || fileIds.isEmpty()) {
            throw new IllegalArgumentException("请先上传文件");
        }
        List<SupportDtos.MediaFilePayload> payloads = markFilesAsBound(fileIds, stage, orderId);
        String stageLabel = switch (stage) {
            case "before_work_media" -> "施工前拍照";
            case "after_work_media" -> "完工拍照";
            default -> "订单资料";
        };
        String description = note == null || note.isBlank() ? stageLabel + "已上传" : note;
        domainSupport.saveStep(orderId, stage, stageLabel, description, true);
        createTrackPoint(orderId, stage.toUpperCase(), stageLabel, description, null, null);
        return payloads;
    }

    public Map<String, Object> handleWechatCallback(String orderId) {
        platformRepository.findServiceOrder(orderId).ifPresent(order -> {
            if (order.status == ServiceOrderStatus.PENDING_PAYMENT && order.paymentStatus == PaymentStatus.UNPAID) {
                order.paymentStatus = PaymentStatus.PARTIAL_PAID;
                order.status = ServiceOrderStatus.PENDING_DISPATCH;
                order.etaText = order.emergency ? "15 分钟" : "30 分钟";
                order.updatedAt = LocalDateTime.now();
                platformRepository.saveServiceOrder(order);
                domainSupport.saveStep(order.id, "payment_completed", "预付款完成", "预付款已到账，订单进入派单队列", true);
                domainSupport.saveStep(order.id, "dispatch", "等待派单", "平台正在匹配可服务的师傅", false);
                createTrackPoint(order.id, "PAYMENT", "预付款完成", "微信支付回调成功，平台开始派单", null, null);
                createDispatchTaskIfNeeded(order);
                webSocketEventGateway.publishOrderStatusChanged(order.id, domainSupport.buildServiceOrder(order));
            } else if (order.status == ServiceOrderStatus.WAITING_SUPPLEMENT_PAYMENT && order.paymentStatus != PaymentStatus.PAID) {
                order.paymentStatus = PaymentStatus.PAID;
                order.status = ServiceOrderStatus.IN_SERVICE;
                order.updatedAt = LocalDateTime.now();
                platformRepository.saveServiceOrder(order);
                domainSupport.saveStep(order.id, "supplement_paid", "补款完成", "用户已完成增项补款，订单继续施工", true);
                createTrackPoint(order.id, "SUPPLEMENT_PAID", "补款完成", "微信支付回调成功，师傅可继续施工", null, null);
                webSocketEventGateway.publishOrderStatusChanged(order.id, domainSupport.buildServiceOrder(order));
            }
        });
        platformRepository.findProductOrder(orderId).ifPresent(order -> {
            order.paymentStatus = PaymentStatus.PAID;
            if (order.status == ProductOrderStatus.PENDING_PAYMENT) {
                order.status = ProductOrderStatus.PENDING_SHIPMENT;
            }
            if (Boolean.TRUE.equals(order.createInstallOrder) && (order.installServiceOrderId == null || order.installServiceOrderId.isBlank())) {
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
            DispatchTaskEntity task = platformRepository.findDispatchTask(id).orElseThrow();
            MasterProfileEntity master = domainSupport.resolveMaster(masterName);
            if (!forceAssign && task.currentMasterUserId != null && !task.currentMasterUserId.equals(master.userId)) {
                throw new IllegalStateException("当前任务已被其他师傅领取");
            }
            task.currentMasterUserId = master.userId;
            task.taskStatus = forceAssign ? "FORCE_ASSIGNED" : "CLAIMED";
            platformRepository.saveDispatchTask(task);
            ServiceOrderEntity order = domainSupport.findServiceOrderEntity(task.orderId);
            order.masterUserId = master.userId;
            if (order.status == ServiceOrderStatus.PENDING_DISPATCH) {
                order.status = ServiceOrderStatus.PENDING_ACCEPT;
            }
            order.updatedAt = LocalDateTime.now();
            platformRepository.saveServiceOrder(order);
            String actionLabel = forceAssign ? "强派完成" : "师傅接单";
            String actionDesc = forceAssign ? master.realName + " 已被平台强制派单" : master.realName + " 已接单并准备出发";
            domainSupport.saveStep(order.id, forceAssign ? "assigned" : "accepted", actionLabel, actionDesc, true);
            createTrackPoint(order.id, forceAssign ? "FORCE_ASSIGN" : "CLAIM", actionLabel, actionDesc, null, null);
            webSocketEventGateway.publishDispatchUpdate(Map.of("taskId", task.id, "orderId", task.orderId, "status", task.taskStatus, "masterName", master.realName));
            webSocketEventGateway.publishOrderStatusChanged(order.id, domainSupport.buildServiceOrder(order));
            return domainSupport.buildDispatchTask(task);
        } finally {
            dispatchLockService.release(id);
        }
    }

    private int parseDistance(String value, int fallback) {
        if (value == null || value.isBlank()) {
            return fallback;
        }
        String digits = value.replaceAll("[^0-9]", "");
        return digits.isBlank() ? fallback : Integer.parseInt(digits);
    }

    private AddressEntity resolveAddress(Long addressId) {
        return addressId == null ? platformRepository.listAddressesByUserId(PlatformDomainSupport.DEFAULT_USER_ID).stream().findFirst().orElseThrow() : platformRepository.findAddress(addressId).orElseThrow();
    }

    private List<SupportDtos.MediaFilePayload> markFilesAsBound(List<Long> fileIds, String bizType, String bizId) {
        return fileIds.stream().map(fileId -> {
            MediaFileEntity entity = fileStorageService.findFileEntity(fileId);
            entity.bizType = bizType;
            entity.bizId = bizId;
            return fileStorageService.toPayload(platformRepository.saveMediaFile(entity));
        }).toList();
    }

    private void createTrackPoint(String orderId, String type, String label, String description, Double latitude, Double longitude) {
        AddressEntity address = platformRepository.findServiceOrder(orderId).flatMap(order -> platformRepository.findAddress(order.addressId)).orElse(null);
        OrderTrackPointEntity point = new OrderTrackPointEntity();
        point.orderId = orderId;
        point.pointType = type;
        point.labelText = label;
        point.descriptionText = description;
        point.latitude = latitude != null ? latitude : address == null ? 31.2253 : address.latitude;
        point.longitude = longitude != null ? longitude : address == null ? 121.5443 : address.longitude;
        point.createdAt = LocalDateTime.now();
        platformRepository.saveTrackPoint(point);
    }

    private double distanceMeters(Double latitude1, Double longitude1, Double latitude2, Double longitude2) {
        if (latitude1 == null || longitude1 == null || latitude2 == null || longitude2 == null) {
            return 0D;
        }
        double earthRadius = 6371000D;
        double dLat = Math.toRadians(latitude2 - latitude1);
        double dLng = Math.toRadians(longitude2 - longitude1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(latitude1)) * Math.cos(Math.toRadians(latitude2)) * Math.sin(dLng / 2) * Math.sin(dLng / 2);
        return earthRadius * (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)));
    }

    private void createDispatchTaskIfNeeded(ServiceOrderEntity order) {
        if (platformRepository.findDispatchTaskByOrderId(order.id).isPresent()) {
            return;
        }
        AddressEntity address = resolveAddress(order.addressId);
        ServiceItemEntity serviceItem = domainSupport.findServiceItemEntity(order.serviceItemId);
        DispatchTaskEntity task = new DispatchTaskEntity();
        task.id = "DISP-" + System.currentTimeMillis();
        task.orderId = order.id;
        task.title = order.title;
        task.income = (order.amount == null ? BigDecimal.ZERO : order.amount).add(BigDecimal.valueOf(80));
        task.distanceText = Boolean.TRUE.equals(order.emergency) ? "2.1km" : "3.8km";
        task.areaText = address.districtName;
        task.dispatchMode = DispatchMode.ROB;
        task.taskStatus = "PENDING";
        task.tagsText = serviceItem.tagsText;
        task.createdAt = LocalDateTime.now();
        platformRepository.saveDispatchTask(task);
        webSocketEventGateway.publishDispatchUpdate(Map.of("taskId", task.id, "orderId", order.id, "status", "PENDING", "title", order.title));
    }

    private String createInstallServiceOrder(ProductOrderEntity productOrder) {
        ServiceItemEntity installService = platformRepository.findServiceItem(301L).orElse(domainSupport.findServiceItemEntity(201L));
        AddressEntity address = resolveAddress(productOrder.addressId);
        ServiceOrderEntity entity = new ServiceOrderEntity();
        entity.id = "SO" + System.currentTimeMillis();
        entity.serviceItemId = installService.id;
        entity.title = installService.name;
        entity.status = ServiceOrderStatus.PENDING_DISPATCH;
        entity.paymentStatus = PaymentStatus.PAID;
        entity.userId = productOrder.userId;
        entity.addressId = address.id;
        entity.appointment = "待用户确认上门时间";
        entity.amount = installService.basePrice == null ? BigDecimal.ZERO : installService.basePrice;
        entity.dispatchMode = DispatchMode.ROB;
        entity.etaText = "待派单";
        entity.descriptionText = productOrder.title + " 已支付，自动生成安装工单";
        entity.emergency = false;
        entity.nightService = false;
        entity.createdAt = LocalDateTime.now();
        entity.updatedAt = LocalDateTime.now();
        platformRepository.saveServiceOrder(entity);
        domainSupport.saveStep(entity.id, "created", "安装工单创建", "商品支付完成后自动生成安装服务单", true);
        domainSupport.saveStep(entity.id, "dispatch", "等待派单", "平台正在为安装工单匹配附近师傅", false);
        createTrackPoint(entity.id, "INSTALL_ORDER_CREATED", "安装工单创建", "商品订单已转换为安装服务单", address.latitude, address.longitude);
        DispatchTaskEntity task = new DispatchTaskEntity();
        task.id = "DISP-" + System.currentTimeMillis();
        task.orderId = entity.id;
        task.title = entity.title;
        task.income = entity.amount.add(BigDecimal.valueOf(60));
        task.distanceText = "3.5km";
        task.areaText = address.districtName;
        task.dispatchMode = DispatchMode.ROB;
        task.taskStatus = "PENDING";
        task.tagsText = installService.tagsText;
        task.createdAt = LocalDateTime.now();
        platformRepository.saveDispatchTask(task);
        webSocketEventGateway.publishDispatchUpdate(Map.of("taskId", task.id, "orderId", entity.id, "status", task.taskStatus, "title", entity.title));
        return entity.id;
    }
}
