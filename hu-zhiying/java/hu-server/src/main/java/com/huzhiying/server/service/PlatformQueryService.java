package com.huzhiying.server.service;

import com.huzhiying.domain.enums.DomainEnums;
import com.huzhiying.server.dto.AdminOverviewDtos;
import com.huzhiying.domain.model.DomainModels.MemberLevel;
import com.huzhiying.domain.model.DomainModels.SearchDocument;
import com.huzhiying.domain.model.DomainModels.ServiceCategory;
import com.huzhiying.domain.model.DomainModels.ServiceItem;
import com.huzhiying.domain.model.DomainModels.ServiceOrder;
import com.huzhiying.server.dto.ContentDtos;
import com.huzhiying.server.dto.MapDtos;
import com.huzhiying.server.dto.SupportDtos;
import com.huzhiying.server.persistence.PersistenceEntities.AcademyArticleEntity;
import com.huzhiying.server.persistence.PersistenceEntities.AcademyCategoryEntity;
import com.huzhiying.server.persistence.PersistenceEntities.AuditLogEntity;
import com.huzhiying.server.persistence.PersistenceEntities.CommentEntity;
import com.huzhiying.server.persistence.PersistenceEntities.CommunityCommentEntity;
import com.huzhiying.server.persistence.PersistenceEntities.CommunityPostEntity;
import com.huzhiying.server.persistence.PersistenceEntities.EcosystemCardEntity;
import com.huzhiying.server.persistence.PersistenceEntities.MasterProfileEntity;
import com.huzhiying.server.persistence.PersistenceEntities.MediaFileEntity;
import com.huzhiying.server.persistence.PersistenceEntities.MessageItemEntity;
import com.huzhiying.server.persistence.PersistenceEntities.MessageSessionEntity;
import com.huzhiying.server.persistence.PersistenceEntities.MessageSessionReadEntity;
import com.huzhiying.server.persistence.PersistenceEntities.NotificationTaskEntity;
import com.huzhiying.server.persistence.PersistenceEntities.OrderTrackPointEntity;
import com.huzhiying.server.persistence.PersistenceEntities.ProductEntity;
import com.huzhiying.server.persistence.PersistenceEntities.ProductOrderEntity;
import com.huzhiying.server.persistence.PersistenceEntities.RefundRequestEntity;
import com.huzhiying.server.persistence.PersistenceEntities.ServiceItemEntity;
import com.huzhiying.server.persistence.PersistenceEntities.ServiceOrderEntity;
import com.huzhiying.server.repository.PlatformRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class PlatformQueryService {

    private static final DateTimeFormatter COMMENT_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final PlatformRepository platformRepository;
    private final PlatformAssembler assembler;
    private final PlatformDomainSupport domainSupport;
    private final MapService mapService;
    private final AuthSessionService authSessionService;
    private final NotificationDispatchService notificationDispatchService;
    private final boolean wechatPayEnabled;
    private final String wechatAppId;
    private final String wechatMerchantId;

    public PlatformQueryService(PlatformRepository platformRepository,
                                PlatformAssembler assembler,
                                PlatformDomainSupport domainSupport,
                                MapService mapService,
                                AuthSessionService authSessionService,
                                NotificationDispatchService notificationDispatchService,
                                @Value("${hzy.wechat.pay-enabled:false}") boolean wechatPayEnabled,
                                @Value("${hzy.wechat.app-id:}") String wechatAppId,
                                @Value("${hzy.wechat.mch-id:}") String wechatMerchantId) {
        this.platformRepository = platformRepository;
        this.assembler = assembler;
        this.domainSupport = domainSupport;
        this.mapService = mapService;
        this.authSessionService = authSessionService;
        this.notificationDispatchService = notificationDispatchService;
        this.wechatPayEnabled = wechatPayEnabled;
        this.wechatAppId = wechatAppId;
        this.wechatMerchantId = wechatMerchantId;
    }

    public SupportDtos.HomePayload homeData() {
        List<ServiceCategory> categoryTree = categories();
        List<SearchDocument> documents = search("");
        List<SupportDtos.HomeCategoryNavPayload> categoryNav = categoryTree.stream()
                .map(category -> new SupportDtos.HomeCategoryNavPayload(
                        category.id(),
                        category.name(),
                        category.icon(),
                        category.services().stream().limit(4).map(ServiceItem::name).toList()
                ))
                .toList();

        List<SupportDtos.RecommendationPayload> recommendations = documents.stream()
                .limit(8)
                .map(document -> new SupportDtos.RecommendationPayload(
                        extractDocumentNumericId(document.id()),
                        document.type(),
                        document.title(),
                        "product".equals(document.type()) ? "商城推荐" : "上门服务",
                        document.price(),
                        "1.2k",
                        document.icon()
                ))
                .toList();

        List<String> hotKeywords = documents.stream()
                .limit(6)
                .map(SearchDocument::title)
                .toList();

        return new SupportDtos.HomePayload(
                "上海",
                mapService.serviceCities(),
                hotKeywords,
                platformRepository.listBanners().stream().map(assembler::toBanner).toList(),
                platformRepository.listNotices().stream().map(assembler::toNotice).toList(),
                categoryNav,
                new SupportDtos.SvipCardPayload(
                        "SVIP 金管家",
                        "专属客服、优先派单、年度保养与会员礼包",
                        List.of("专属客服", "优先派单", "年度保养", "上门优惠")
                ),
                List.of(
                        new SupportDtos.EcosystemCardPayload(1L, "小应学堂", "师傅培训与故障案例库", "/static/icons/school.svg", "#2B5CFF"),
                        new SupportDtos.EcosystemCardPayload(2L, "小应商城", "五金耗材与安装配件", "/static/icons/mall.svg", "#FF7D00"),
                        new SupportDtos.EcosystemCardPayload(3L, "同城圈子", "邻里互助与本地口碑内容", "/static/icons/community.svg", "#00B578")
                ),
                recommendations
        );
    }

    public List<ServiceCategory> categories() {
        List<ServiceCategory> categories = new ArrayList<>();
        Map<Long, List<ServiceItem>> groupedItems = assembler.groupServiceItems(platformRepository.listServiceItems());
        platformRepository.listServiceCategories().forEach(category ->
                categories.add(assembler.toServiceCategory(category, groupedItems.getOrDefault(category.id, List.of())))
        );
        return categories;
    }

    public List<ServiceItem> services() {
        return platformRepository.listServiceItems().stream().map(assembler::toServiceItem).toList();
    }

    public Map<String, Object> serviceDetail(Long serviceItemId) {
        ServiceItemEntity entity = domainSupport.findServiceItemEntity(serviceItemId);
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("id", entity.id);
        payload.put("title", entity.name);
        payload.put("subtitle", entity.subtitle);
        payload.put("basePrice", entity.basePrice);
        payload.put("doorPrice", entity.doorPrice);
        payload.put("guidePrice", entity.guidePrice);
        payload.put("warranty", entity.warrantyText);
        payload.put("images", assembler.splitList(entity.imageUrls));
        payload.put("guarantees", assembler.splitList(entity.guaranteesText));
        payload.put("process", assembler.splitList(entity.processSteps));
        payload.put("comments", serviceComments(serviceItemId));
        return payload;
    }

    public List<SupportDtos.CommentPayload> serviceComments(Long serviceItemId) {
        return platformRepository.listCommentsByServiceItemId(serviceItemId).stream()
                .map(this::toCommentPayload)
                .toList();
    }

    public List<?> products() {
        Map<Long, List<com.huzhiying.domain.model.DomainModels.Sku>> groupedSkus =
                assembler.groupSkus(platformRepository.listSkusByProductIds(platformRepository.listProducts().stream().map(product -> product.id).toList()));
        return platformRepository.listProducts().stream()
                .map(product -> assembler.toProduct(product, groupedSkus.getOrDefault(product.id, List.of())))
                .toList();
    }

    public Map<String, Object> productDetail(Long productId) {
        ProductEntity productEntity = platformRepository.findProduct(productId).orElseThrow();
        var skus = assembler.groupSkus(platformRepository.listSkusByProductIds(List.of(productId)))
                .getOrDefault(productId, List.of());

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("id", productEntity.id);
        payload.put("title", productEntity.name);
        payload.put("subtitle", productEntity.descriptionText);
        payload.put("price", productEntity.price);
        payload.put("tagPrice", productEntity.tagPrice);
        payload.put("discountPrice", productEntity.discountPrice);
        payload.put("images", productEntity.imageUrl == null || productEntity.imageUrl.isBlank()
                ? List.of("/seed-media/product-card.svg")
                : List.of(productEntity.imageUrl));
        payload.put("createInstallOrder", Boolean.TRUE.equals(productEntity.createInstallOrder));
        payload.put("deliveryDesc", Boolean.TRUE.equals(productEntity.createInstallOrder)
                ? "支付成功后自动生成安装工单，平台同步派单"
                : "现货商品发货，默认 1-2 天内送达");
        payload.put("highlights", Boolean.TRUE.equals(productEntity.createInstallOrder)
                ? List.of("正品保障", "包安装调试", "电子质保卡")
                : List.of("现货速发", "平台售后", "支持开票"));
        payload.put("skus", skus.stream().map(item -> Map.of(
                "id", item.id(),
                "name", item.name(),
                "price", item.price(),
                "tagPrice", item.tagPrice(),
                "discountPrice", item.discountPrice(),
                "stock", item.stock()
        )).toList());
        return payload;
    }

    public List<SearchDocument> search(String keyword) {
        String query = keyword == null ? "" : keyword.trim();
        List<SearchDocument> documents = new ArrayList<>();
        platformRepository.listServiceItems().forEach(item -> documents.add(
                assembler.toSearchDocument("service-" + item.id, "service", item.name, item.subtitle, item.basePrice, "/static/icons/screwdriver.svg")
        ));
        platformRepository.listProducts().forEach(item -> documents.add(
                assembler.toSearchDocument("product-" + item.id, "product", item.name, item.descriptionText, item.price, "/static/icons/mall.svg")
        ));
        if (query.isBlank()) {
            return documents;
        }
        return documents.stream()
                .filter(item -> item.title().contains(query) || item.summary().contains(query))
                .toList();
    }

    public Map<String, Object> currentUser() {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("profile", assembler.toUser(authSessionService.currentUser(DomainEnums.RoleCode.USER)));
        payload.put("banners", platformRepository.listBanners().stream().map(assembler::toBanner).toList());
        payload.put("notices", platformRepository.listNotices().stream().map(assembler::toNotice).toList());
        return payload;
    }

    public List<?> addresses() {
        return platformRepository.listAddressesByUserId(authSessionService.currentUserId(DomainEnums.RoleCode.USER)).stream().map(assembler::toAddress).toList();
    }

    public List<?> coupons() {
        return platformRepository.listCoupons().stream().map(assembler::toCoupon).toList();
    }

    public MemberLevel currentMember() {
        return platformRepository.findTopMemberLevel()
                .map(assembler::toMemberLevel)
                .orElse(new MemberLevel("普通会员", "基础服务权益", 0));
    }

    public List<ServiceOrder> serviceOrders() {
        Long currentUserId = authSessionService.currentUserId(DomainEnums.RoleCode.USER);
        return platformRepository.listServiceOrders().stream()
                .filter(order -> currentUserId.equals(order.userId))
                .map(domainSupport::buildServiceOrder)
                .toList();
    }

    public ServiceOrder serviceOrder(String id) {
        return domainSupport.buildServiceOrder(domainSupport.findServiceOrderEntity(id));
    }

    public SupportDtos.OrderDetailPayload serviceOrderDetail(String id) {
        ServiceOrderEntity order = domainSupport.findServiceOrderEntity(id);
        return buildServiceOrderDetailPayload(order);
    }

    public SupportDtos.AfterSalesPayload afterSalesDetail(String orderId) {
        RefundRequestEntity refundRequest = platformRepository.findLatestRefundRequestByOrderId(orderId)
                .orElseThrow(() -> new IllegalStateException("当前订单暂无售后申请"));
        List<MediaFileEntity> evidenceFiles = platformRepository.listMediaFilesByBiz("refund_evidence", refundRequest.bizNo);
        return new SupportDtos.AfterSalesPayload(
                refundRequest.bizNo,
                refundRequest.orderId,
                "SERVICE".equalsIgnoreCase(refundRequest.orderType) ? "service" : "product",
                refundRequest.status == null ? "" : refundRequest.status.name(),
                refundStatusText(refundRequest),
                refundRequest.reasonText == null ? "" : refundRequest.reasonText,
                refundRequest.channel == null ? "" : refundRequest.channel,
                refundRequest.amount,
                refundRequest.createdAt,
                refundRequest.approvedAt,
                refundRequest.completedAt,
                buildAfterSalesReviewRemark(refundRequest),
                evidenceFiles.stream().map(this::toMediaPayload).toList(),
                buildAfterSalesTimeline(refundRequest),
                refundRequest.status != DomainEnums.RefundRequestStatus.COMPLETED
        );
    }

    public SupportDtos.OrderTrackingPayload orderTracking(String orderId) {
        ServiceOrderEntity entity = domainSupport.findServiceOrderEntity(orderId);
        var paymentRecord = platformRepository.findLatestPaymentRecordByOrderId(orderId).orElse(null);
        List<SupportDtos.OrderTrackPointPayload> points = platformRepository.listTrackPoints(orderId).stream()
                .map(this::toTrackPointPayload)
                .toList();
        return new SupportDtos.OrderTrackingPayload(
                entity.id,
                paymentRecord == null ? "" : paymentRecord.channel,
                paymentRecord == null ? "" : paymentRecord.bizNo,
                domainSupport.statusLabel(entity.status),
                entity.appointment,
                platformRepository.findAddress(entity.addressId).map(address -> address.detailAddress).orElse(""),
                entity.etaText,
                buildDistanceText(orderId),
                points,
                collectOrderMedia(orderId)
        );
    }

    public List<?> productOrders() {
        Long currentUserId = authSessionService.currentUserId(DomainEnums.RoleCode.USER);
        return platformRepository.listProductOrders().stream()
                .filter(order -> currentUserId.equals(order.userId))
                .map(domainSupport::buildProductOrder)
                .toList();
    }

    public Object productOrder(String id) {
        return buildProductOrderDetailPayload(platformRepository.findProductOrder(id).orElseThrow());
    }

    public List<?> dispatchTasks() {
        return platformRepository.listDispatchTasks().stream().map(domainSupport::buildDispatchTask).toList();
    }

    public Map<String, Object> masterDashboard() {
        Long masterUserId = authSessionService.currentMasterUserId();
        List<ServiceOrder> orders = platformRepository.listServiceOrders().stream()
                .filter(order -> masterUserId.equals(order.masterUserId))
                .map(domainSupport::buildServiceOrder)
                .toList();

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("dispatchCount", platformRepository.listDispatchTasks().size());
        payload.put("wallet", wallet());
        payload.put("orders", orders);
        payload.put("currentOrder", orders.isEmpty() ? null : orders.get(0));
        payload.put("settings", masterSettings());
        payload.put("offlineQueueSummary", Map.of("pendingActions", 0, "pendingUploads", 0));
        return payload;
    }

    public Object wallet() {
        return assembler.toWalletAccount(platformRepository.findWalletAccountByMasterUserId(authSessionService.currentMasterUserId()).orElseThrow());
    }

    public List<?> walletTransactions() {
        Long accountId = platformRepository.findWalletAccountByMasterUserId(authSessionService.currentMasterUserId()).orElseThrow().id;
        return platformRepository.listWalletTransactions(accountId).stream().map(assembler::toWalletTransaction).toList();
    }

    public Map<String, Object> masterSettings() {
        MasterProfileEntity profileEntity = authSessionService.currentMasterProfile();
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("listening", Boolean.TRUE.equals(profileEntity.listening));
        payload.put("maxDistance", profileEntity.maxDistanceKm + "km");
        payload.put("privacyNumber", Boolean.TRUE.equals(profileEntity.privacyNumber));
        payload.put("enabled", profileEntity.enabled == null || Boolean.TRUE.equals(profileEntity.enabled));
        payload.put("online", Boolean.TRUE.equals(profileEntity.online));
        return payload;
    }

    public List<?> messageSessions() {
        AuthSessionService.SessionIdentity identity = authSessionService.currentIdentity(DomainEnums.RoleCode.USER);
        materializePendingInboxNotifications(identity);
        return platformRepository.listMessageSessions().stream()
                .filter(session -> canAccessMessageSession(session, identity))
                .sorted(Comparator
                        .comparing((MessageSessionEntity session) -> !"system".equals(messageSessionType(session)))
                        .thenComparing(Comparator.comparingLong((MessageSessionEntity session) -> latestMessageId(session.id)).reversed()))
                .map(session -> buildMessageSessionPayload(session, identity))
                .toList();
    }

    public List<?> messageItems(String sessionId) {
        MessageSessionEntity session = requireAccessibleMessageSession(sessionId, authSessionService.currentIdentity(DomainEnums.RoleCode.USER));
        return platformRepository.listMessageItems(session.id).stream().map(assembler::toMessageItem).toList();
    }

    public List<?> notifications() {
        AuthSessionService.SessionIdentity identity = authSessionService.currentIdentity(DomainEnums.RoleCode.USER);
        materializePendingInboxNotifications(identity);
        return platformRepository.listMessageSessions().stream()
                .filter(this::isSystemSession)
                .filter(session -> canAccessMessageSession(session, identity))
                .map(session -> buildMessageSessionPayload(session, identity))
                .toList();
    }

    public List<?> notices() {
        return platformRepository.listNotices().stream().map(assembler::toNotice).toList();
    }

    public Map<String, Object> adminDashboard() {
        BigDecimal gmv = platformRepository.listServiceOrders().stream()
                .map(item -> item.amount == null ? BigDecimal.ZERO : item.amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .add(platformRepository.listProductOrders().stream()
                        .map(item -> item.amount == null ? BigDecimal.ZERO : item.amount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add));
        long onlineMasters = platformRepository.listMasterProfiles().stream().filter(item -> Boolean.TRUE.equals(item.online)).count();

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("gmv", gmv.toPlainString());
        payload.put("serviceOrders", platformRepository.listServiceOrders().size());
        payload.put("productOrders", platformRepository.listProductOrders().size());
        payload.put("onlineMasters", onlineMasters);
        payload.put("warning", platformRepository.listArbitrations().size());
        return payload;
    }

    public List<?> masters() {
        return platformRepository.listMasterProfiles().stream()
                .sorted(java.util.Comparator.comparing((MasterProfileEntity item) -> item.creditScore).reversed())
                .map(item -> assembler.toMasterProfile(item, platformRepository.findUser(item.userId).orElseThrow()))
                .toList();
    }

    public List<?> arbitrations() {
        return platformRepository.listArbitrations().stream().map(assembler::toArbitration).toList();
    }

    public List<?> pricingRules() {
        Map<Long, String> categoryNameMap = platformRepository.listServiceCategories().stream()
                .collect(LinkedHashMap::new, (map, item) -> map.put(item.id, item.name), Map::putAll);
        return platformRepository.listServiceItems().stream()
                .map(item -> Map.of(
                        "category", categoryNameMap.getOrDefault(item.categoryId, item.name),
                        "basePrice", item.basePrice,
                        "guidePrice", item.guidePrice,
                        "coefficient", "夜间 +30%"
                ))
                .toList();
    }

    public List<?> adminDispatchRows() {
        return platformRepository.listDispatchTasks().stream()
                .map(task -> Map.of(
                        "taskId", task.id,
                        "id", task.orderId,
                        "type", task.orderId != null && task.orderId.startsWith("PO") ? "安装" : "维修",
                        "area", task.areaText,
                        "status", domainSupport.mapTaskStatus(task.taskStatus),
                        "master", task.currentMasterUserId == null
                                ? "待接单"
                                : platformRepository.findUser(task.currentMasterUserId).map(user -> user.nickname).orElse("待接单"),
                        "amount", task.income
                ))
                .toList();
    }

    public List<?> adminOrders() {
        List<Map<String, Object>> rows = new ArrayList<>();
        platformRepository.listServiceOrders().forEach(order -> rows.add(Map.of(
                "id", order.id,
                "category", order.title,
                "status", domainSupport.statusLabel(order.status),
                "user", platformRepository.findUser(order.userId).map(user -> user.nickname).orElse("用户"),
                "amount", order.amount
        )));
        platformRepository.listProductOrders().forEach(order -> rows.add(Map.of(
                "id", order.id,
                "category", order.title,
                "status", domainSupport.productStatusLabel(order.status),
                "user", platformRepository.findUser(order.userId).map(user -> user.nickname).orElse("用户"),
                "amount", order.amount
        )));
        return rows;
    }

    public List<?> financeRows() {
        Long accountId = platformRepository.findWalletAccountByMasterUserId(PlatformDomainSupport.DEFAULT_MASTER_USER_ID).orElseThrow().id;
        List<Map<String, Object>> rows = new ArrayList<>();
        platformRepository.listSettlementBills().forEach(item -> rows.add(Map.of(
                "billNo", "SETTLE-" + item.id,
                "type", "甯堝倕缁撶畻",
                "amount", item.amount,
                "status", switch (item.status) {
                    case PENDING_REVIEW -> "待结算";
                    case APPROVED -> "已结算";
                    case REVERSED -> "已冲减";
                }
        )));
        platformRepository.listRefundRequests().forEach(item -> rows.add(Map.of(
                "billNo", "REFUND-" + item.orderId,
                "type", "閫€娆惧崟",
                "amount", item.amount,
                "status", switch (item.status) {
                    case PENDING_REVIEW -> "待审核";
                    case APPROVED -> "待退款";
                    case REJECTED -> "已拒绝";
                    case COMPLETED -> "已退款";
                }
        )));
        if (!rows.isEmpty()) {
            return rows;
        }
        platformRepository.listWalletTransactions(accountId).forEach(item -> rows.add(Map.of(
                "billNo", "SETTLE-" + item.id,
                "type", "师傅结算",
                "amount", item.amount,
                "status", item.statusText == null || item.statusText.isBlank() ? "待结算" : item.statusText
        )));
        platformRepository.listServiceOrders().stream()
                .filter(order -> order.paymentStatus == DomainEnums.PaymentStatus.REFUNDING
                        || order.status == DomainEnums.ServiceOrderStatus.REFUNDING)
                .forEach(order -> rows.add(Map.of(
                        "billNo", "REFUND-" + order.id,
                        "type", "退款单",
                        "amount", order.amount,
                        "status", "处理中"
                )));
        return rows;
    }

    public java.util.List<AdminOverviewDtos.FinanceRow> adminFinanceRows() {
        Long accountId = platformRepository.findWalletAccountByMasterUserId(PlatformDomainSupport.DEFAULT_MASTER_USER_ID)
                .orElseThrow()
                .id;
        List<AdminOverviewDtos.FinanceRow> rows = new ArrayList<>();
        platformRepository.listSettlementBills().forEach(item -> rows.add(new AdminOverviewDtos.FinanceRow(
                "SETTLE-" + item.id,
                "甯堝倕缁撶畻",
                switch (item.status) {
                    case PENDING_REVIEW -> "待结算";
                    case APPROVED -> "已结算";
                    case REVERSED -> "已冲减";
                },
                item.amount,
                item.orderId,
                item.remarkText == null || item.remarkText.isBlank() ? item.orderId : item.remarkText,
                adminResolveUserName(item.masterId),
                item.updatedAt == null ? "" : item.updatedAt.toString(),
                item.bizNo == null ? "" : item.bizNo
        )));
        platformRepository.listRefundRequests().forEach(item -> rows.add(new AdminOverviewDtos.FinanceRow(
                "REFUND-" + item.orderId,
                "SERVICE".equalsIgnoreCase(item.orderType) ? "鏈嶅姟閫€娆?" : "鍟嗗搧閫€娆?",
                switch (item.status) {
                    case PENDING_REVIEW -> "待审核";
                    case APPROVED -> "待退款";
                    case REJECTED -> "已拒绝";
                    case COMPLETED -> "已退款";
                },
                item.amount,
                item.orderId,
                adminResolveFinanceTitle(item.orderId),
                adminResolveUserName(item.masterId),
                item.updatedAt == null ? "" : item.updatedAt.toString(),
                item.bizNo == null ? "" : item.bizNo
        )));
        if (!rows.isEmpty()) {
            return rows;
        }

        platformRepository.listWalletTransactions(accountId).forEach(item -> rows.add(new AdminOverviewDtos.FinanceRow(
                "SETTLE-" + item.id,
                "甯堝倕缁撶畻",
                item.statusText == null || item.statusText.isBlank() ? "待结算" : item.statusText,
                item.amount,
                adminExtractOrderId(item.title),
                item.title,
                adminResolveWalletMasterName(accountId),
                item.transactionTime == null ? "" : item.transactionTime,
                ""
        )));
        platformRepository.listServiceOrders().stream()
                .filter(order -> order.paymentStatus == DomainEnums.PaymentStatus.REFUNDING
                        || order.status == DomainEnums.ServiceOrderStatus.REFUNDING)
                .forEach(order -> rows.add(new AdminOverviewDtos.FinanceRow(
                        "REFUND-" + order.id,
                        "閫€娆惧崟",
                        "处理中",
                        order.amount,
                        order.id,
                        order.title,
                        adminResolveUserName(order.masterUserId),
                        order.updatedAt == null ? "" : order.updatedAt.toString(),
                        ""
                )));
        return rows;
    }

    public java.util.List<AdminOverviewDtos.NotificationTaskItem> notificationTasks() {
        return platformRepository.listNotificationTasks().stream()
                .map(this::toNotificationTaskItem)
                .toList();
    }

    private String adminResolveUserName(Long userId) {
        if (userId == null) {
            return "";
        }
        return platformRepository.findUser(userId)
                .map(user -> user.nickname)
                .orElse("");
    }

    private String adminResolveFinanceTitle(String orderId) {
        if (orderId == null || orderId.isBlank()) {
            return "";
        }
        if (orderId.startsWith("SO")) {
            return platformRepository.findServiceOrder(orderId)
                    .map(order -> order.title)
                    .orElse(orderId);
        }
        if (orderId.startsWith("PO")) {
            return platformRepository.findProductOrder(orderId)
                    .map(order -> order.title)
                    .orElse(orderId);
        }
        return orderId;
    }

    private String adminResolveWalletMasterName(Long walletAccountId) {
        if (walletAccountId == null) {
            return "";
        }
        return platformRepository.findWalletAccount(walletAccountId)
                .flatMap(account -> platformRepository.findUser(account.masterUserId))
                .map(user -> user.nickname)
                .orElse("");
    }

    private String adminExtractOrderId(String title) {
        if (title == null || title.isBlank()) {
            return "";
        }
        for (String token : title.split("\\s+")) {
            if (token.startsWith("SO") || token.startsWith("PO")) {
                return token.trim();
            }
        }
        return "";
    }

    private AdminOverviewDtos.NotificationTaskItem toNotificationTaskItem(NotificationTaskEntity entity) {
        return new AdminOverviewDtos.NotificationTaskItem(
                entity.id,
                entity.bizNo,
                entity.bizType,
                entity.bizId,
                entity.targetRole,
                entity.targetUserId,
                entity.channel,
                entity.templateCode,
                entity.status == null ? "" : entity.status.name(),
                entity.traceId,
                entity.createdAt == null ? "" : entity.createdAt.toString(),
                entity.updatedAt == null ? "" : entity.updatedAt.toString(),
                entity.sentAt == null ? "" : entity.sentAt.toString()
        );
    }

    public SupportDtos.WechatPrepayPayload createWechatPrepay(String orderId) {
        if (orderId == null || orderId.isBlank()) {
            throw new IllegalArgumentException("缺少订单号，无法创建预支付单。");
        }

        if (orderId.startsWith("SO")) {
            ServiceOrderEntity order = domainSupport.findServiceOrderEntity(orderId);
            if (order.status != DomainEnums.ServiceOrderStatus.PENDING_PAYMENT
                    && order.status != DomainEnums.ServiceOrderStatus.WAITING_SUPPLEMENT_PAYMENT) {
                throw new IllegalStateException("当前服务订单不处于待支付状态，无需重复发起支付。");
            }
        } else if (orderId.startsWith("PO")) {
            var order = platformRepository.findProductOrder(orderId).orElseThrow(() -> new IllegalArgumentException("商品订单不存在。"));
            if (order.status != DomainEnums.ProductOrderStatus.PENDING_PAYMENT) {
                throw new IllegalStateException("当前商品订单不处于待支付状态，无需重复发起支付。");
            }
        } else {
            throw new IllegalArgumentException("不支持的订单号格式。");
        }

        if (!wechatPayEnabled || wechatAppId == null || wechatAppId.isBlank() || wechatMerchantId == null || wechatMerchantId.isBlank()) {
            throw new IllegalStateException("当前环境未配置微信支付商户参数，无法创建预支付单。");
        }

        throw new IllegalStateException("当前版本尚未接入真实微信支付下单服务，不能返回伪造支付参数。");
    }

    private SupportDtos.OrderDetailPayload buildServiceOrderDetailPayload(ServiceOrderEntity order) {
        var user = platformRepository.findUser(order.userId).orElse(null);
        var master = order.masterUserId == null ? null : platformRepository.findUser(order.masterUserId).orElse(null);
        String address = platformRepository.findAddress(order.addressId).map(item -> item.detailAddress).orElse("");

        boolean canPay = order.status == DomainEnums.ServiceOrderStatus.PENDING_PAYMENT
                || order.status == DomainEnums.ServiceOrderStatus.WAITING_SUPPLEMENT_PAYMENT;
        boolean canUrge = order.status == DomainEnums.ServiceOrderStatus.PENDING_DISPATCH
                || order.status == DomainEnums.ServiceOrderStatus.PENDING_ACCEPT
                || order.status == DomainEnums.ServiceOrderStatus.ON_THE_WAY;
        boolean canCancel = order.status != DomainEnums.ServiceOrderStatus.COMPLETED
                && order.status != DomainEnums.ServiceOrderStatus.CANCELLED;
        boolean canRefund = order.paymentStatus == DomainEnums.PaymentStatus.PARTIAL_PAID
                || order.paymentStatus == DomainEnums.PaymentStatus.PAID
                || order.paymentStatus == DomainEnums.PaymentStatus.REFUNDING;

        return new SupportDtos.OrderDetailPayload(
                order.id,
                "service",
                order.title,
                order.status.name(),
                order.paymentStatus.name(),
                statusText(order.status),
                user == null ? "" : user.nickname,
                master == null ? "待接单" : master.nickname,
                address,
                order.appointment,
                order.etaText,
                order.amount,
                "",
                master == null ? "" : maskMobile(master.mobile),
                canPay,
                canUrge,
                canCancel,
                canRefund,
                buildTimelinePayload(order.id),
                collectOrderMedia(order.id),
                buildQuotationPayload(order.id),
                buildAfterSalesSummary(order.id),
                buildMessageSummary(order.id)
        );
    }

    private SupportDtos.OrderDetailPayload buildProductOrderDetailPayload(ProductOrderEntity order) {
        var user = platformRepository.findUser(order.userId).orElse(null);
        String address = platformRepository.findAddress(order.addressId).map(item -> item.detailAddress).orElse("");
        boolean canPay = order.status == DomainEnums.ProductOrderStatus.PENDING_PAYMENT;
        boolean canRefund = order.paymentStatus == DomainEnums.PaymentStatus.PAID
                || order.paymentStatus == DomainEnums.PaymentStatus.REFUNDING
                || order.paymentStatus == DomainEnums.PaymentStatus.REFUNDED;

        return new SupportDtos.OrderDetailPayload(
                order.id,
                "product",
                order.title,
                order.status == null ? "" : order.status.name(),
                order.paymentStatus == null ? "" : order.paymentStatus.name(),
                productStatusText(order),
                user == null ? "" : user.nickname,
                "",
                address,
                Boolean.TRUE.equals(order.createInstallOrder) ? "支付完成后同步安排安装时间" : "预计 1-2 天内送达",
                "",
                order.amount,
                order.installServiceOrderId,
                "",
                canPay,
                false,
                false,
                canRefund,
                List.of(),
                List.of(),
                null,
                buildAfterSalesSummary(order.id),
                null
        );
    }

    private SupportDtos.QuotationPayload buildQuotationPayload(String orderId) {
        var quotation = platformRepository.findQuotationByOrderId(orderId).orElse(null);
        if (quotation == null) {
            return null;
        }
        return new SupportDtos.QuotationPayload(
                quotation.id,
                quotation.status == null ? "" : quotation.status.name(),
                quotation.remarkText,
                quotation.totalAmount,
                platformRepository.listQuotationItems(quotation.id).stream()
                        .map(item -> new SupportDtos.QuotationItemPayload(item.name, item.amount))
                        .toList()
        );
    }

    private List<SupportDtos.TimelineItemPayload> buildTimelinePayload(String orderId) {
        return platformRepository.listWorkSteps(orderId).stream()
                .map(item -> new SupportDtos.TimelineItemPayload(
                        item.stepKey,
                        item.labelText,
                        item.descriptionText,
                        Boolean.TRUE.equals(item.done),
                        item.stepTime
                ))
                .toList();
    }

    private SupportDtos.MessageSummaryPayload buildMessageSummary(String orderId) {
        MessageSessionEntity session = platformRepository.listMessageSessions().stream()
                .filter(item -> orderId.equals(item.orderId))
                .findFirst()
                .orElse(null);
        if (session == null) {
            return null;
        }

        List<MessageItemEntity> items = platformRepository.listMessageItems(session.id);
        MessageItemEntity latest = items.isEmpty() ? null : items.get(items.size() - 1);
        String participant = resolveSessionParticipant(session, authSessionService.currentIdentity(DomainEnums.RoleCode.USER).roleCode());
        return new SupportDtos.MessageSummaryPayload(session.id, session.title, participant, items.size(), summarizeMessage(latest));
    }

    private SupportDtos.AfterSalesSummaryPayload buildAfterSalesSummary(String orderId) {
        RefundRequestEntity refundRequest = platformRepository.findLatestRefundRequestByOrderId(orderId).orElse(null);
        if (refundRequest == null) {
            return null;
        }
        boolean active = refundRequest.status == DomainEnums.RefundRequestStatus.PENDING_REVIEW
                || refundRequest.status == DomainEnums.RefundRequestStatus.APPROVED;
        return new SupportDtos.AfterSalesSummaryPayload(
                active,
                refundRequest.bizNo,
                refundRequest.status == null ? "" : refundRequest.status.name(),
                refundStatusText(refundRequest)
        );
    }

    private List<SupportDtos.TimelineItemPayload> buildAfterSalesTimeline(RefundRequestEntity refundRequest) {
        List<SupportDtos.TimelineItemPayload> timeline = new ArrayList<>();
        Set<String> recordedKeys = new HashSet<>();

        timeline.add(new SupportDtos.TimelineItemPayload(
                "refund_requested",
                "售后申请已提交",
                refundRequest.reasonText == null || refundRequest.reasonText.isBlank() ? "平台已收到你的售后申请" : refundRequest.reasonText,
                true,
                refundRequest.createdAt
        ));
        recordedKeys.add("refund_requested");

        for (AuditLogEntity auditLog : platformRepository.listAuditLogsByBiz(refundRequest.orderType, refundRequest.orderId)) {
            String key = switch (auditLog.actionCode) {
                case "REFUND_REQUEST_APPROVED" -> "refund_approved";
                case "REFUND_CALLBACK_FAILED" -> "refund_rejected";
                case "REFUND_APPROVED" -> "refund_completed";
                default -> "";
            };
            if (key.isBlank() || !recordedKeys.add(key)) {
                continue;
            }
            timeline.add(new SupportDtos.TimelineItemPayload(
                    key,
                    switch (key) {
                        case "refund_approved" -> "审核通过";
                        case "refund_rejected" -> "审核驳回";
                        case "refund_completed" -> "退款完成";
                        default -> "售后更新";
                    },
                    auditLog.detailText == null || auditLog.detailText.isBlank() ? "平台已更新售后进度" : auditLog.detailText,
                    true,
                    auditLog.createdAt
            ));
        }

        for (OrderTrackPointEntity trackPoint : platformRepository.listTrackPoints(refundRequest.orderId)) {
            if (!"REFUNDING".equalsIgnoreCase(trackPoint.pointType)
                    && !"REFUND_COMPLETED".equalsIgnoreCase(trackPoint.pointType)) {
                continue;
            }
            String key = "REFUND_COMPLETED".equalsIgnoreCase(trackPoint.pointType) ? "refund_completed" : "refund_tracking";
            if (!recordedKeys.add(key)) {
                continue;
            }
            timeline.add(new SupportDtos.TimelineItemPayload(
                    key,
                    trackPoint.labelText == null || trackPoint.labelText.isBlank() ? "售后处理中" : trackPoint.labelText,
                    trackPoint.descriptionText == null || trackPoint.descriptionText.isBlank() ? "平台正在处理当前售后申请" : trackPoint.descriptionText,
                    true,
                    trackPoint.createdAt
            ));
        }

        if (refundRequest.approvedAt != null && recordedKeys.add("refund_approved")) {
            timeline.add(new SupportDtos.TimelineItemPayload(
                    "refund_approved",
                    "审核通过",
                    "平台已审核通过当前退款申请，正在处理退款。",
                    true,
                    refundRequest.approvedAt
            ));
        }

        if (refundRequest.completedAt != null && recordedKeys.add("refund_completed")) {
            timeline.add(new SupportDtos.TimelineItemPayload(
                    "refund_completed",
                    "退款完成",
                    "平台已完成退款处理，请留意原支付渠道到账。",
                    true,
                    refundRequest.completedAt
            ));
        }

        return timeline.stream()
                .sorted(Comparator.comparing(SupportDtos.TimelineItemPayload::time, Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();
    }

    private String buildAfterSalesReviewRemark(RefundRequestEntity refundRequest) {
        List<AuditLogEntity> auditLogs = platformRepository.listAuditLogsByBiz(refundRequest.orderType, refundRequest.orderId);
        for (int index = auditLogs.size() - 1; index >= 0; index--) {
            AuditLogEntity auditLog = auditLogs.get(index);
            if ("REFUND_REQUEST_APPROVED".equals(auditLog.actionCode)
                    || "REFUND_CALLBACK_FAILED".equals(auditLog.actionCode)
                    || "REFUND_APPROVED".equals(auditLog.actionCode)) {
                return auditLog.detailText == null ? "" : auditLog.detailText;
            }
        }
        return refundRequest.status == DomainEnums.RefundRequestStatus.REJECTED
                ? (refundRequest.reasonText == null ? "" : refundRequest.reasonText)
                : "";
    }

    private void materializePendingInboxNotifications(AuthSessionService.SessionIdentity identity) {
        if (identity == null) {
            return;
        }
        if (identity.roleCode() != DomainEnums.RoleCode.USER && identity.roleCode() != DomainEnums.RoleCode.MASTER) {
            return;
        }
        notificationDispatchService.dispatchPendingTasksToInbox(identity.roleCode(), identity.userId(), 100);
    }

    private List<SupportDtos.MediaFilePayload> collectOrderMedia(String orderId) {
        List<MediaFileEntity> entities = new ArrayList<>();
        entities.addAll(platformRepository.listMediaFilesByBiz("order_evidence", orderId));
        entities.addAll(platformRepository.listMediaFilesByBiz("before_work_media", orderId));
        entities.addAll(platformRepository.listMediaFilesByBiz("after_work_media", orderId));
        return entities.stream().map(this::toMediaPayload).toList();
    }

    private SupportDtos.MessageSessionPayload buildMessageSessionPayload(MessageSessionEntity session, AuthSessionService.SessionIdentity identity) {
        List<MessageItemEntity> items = platformRepository.listMessageItems(session.id);
        MessageItemEntity latest = items.isEmpty() ? null : items.get(items.size() - 1);
        String readerCode = messageReaderCode(identity.roleCode());
        MessageSessionReadEntity readState = platformRepository.findMessageSessionRead(session.id, readerCode).orElse(null);
        long lastReadMessageId = readState == null || readState.lastReadMessageId == null ? 0L : readState.lastReadMessageId;
        int unreadCount = (int) items.stream()
                .filter(item -> item.id != null && item.id > lastReadMessageId)
                .filter(item -> !readerCode.equals(normalizeMessageSender(item.senderCode)))
                .count();

        return new SupportDtos.MessageSessionPayload(
                session.id,
                messageSessionType(session),
                session.orderId,
                session.title,
                resolveSessionParticipant(session, identity.roleCode()),
                summarizeMessage(latest),
                latest == null ? "" : latest.messageTime,
                unreadCount,
                items.size()
        );
    }

    private SupportDtos.MediaFilePayload toMediaPayload(MediaFileEntity entity) {
        String url = entity.accessUrl == null || entity.accessUrl.isBlank() ? entity.storagePath : entity.accessUrl;
        return new SupportDtos.MediaFilePayload(
                entity.id,
                entity.bizType,
                entity.bizId,
                entity.originalName,
                url,
                entity.contentType,
                entity.sizeBytes,
                entity.createdAt
        );
    }

    private MessageSessionEntity requireAccessibleMessageSession(String sessionId, AuthSessionService.SessionIdentity identity) {
        MessageSessionEntity session = platformRepository.findMessageSession(sessionId).orElseThrow();
        if (!canAccessMessageSession(session, identity)) {
            throw new IllegalStateException("当前会话不可访问");
        }
        return session;
    }

    private boolean canAccessMessageSession(MessageSessionEntity session, AuthSessionService.SessionIdentity identity) {
        if (isSystemSession(session)) {
            return identity.userId().equals(session.participantUserId)
                    && systemSessionRole(session.id) == identity.roleCode();
        }
        return identity.roleCode() == DomainEnums.RoleCode.MASTER
                ? platformRepository.findServiceOrder(session.orderId)
                        .map(order -> identity.userId().equals(order.masterUserId))
                        .orElse(false)
                : identity.userId().equals(session.participantUserId);
    }

    private String resolveSessionParticipant(MessageSessionEntity session, DomainEnums.RoleCode roleCode) {
        if (isSystemSession(session)) {
            return "系统通知";
        }
        if (roleCode == DomainEnums.RoleCode.MASTER) {
            return platformRepository.findUser(session.participantUserId)
                    .map(user -> user.nickname)
                    .orElse("用户");
        }
        return platformRepository.findServiceOrder(session.orderId)
                .flatMap(order -> Optional.ofNullable(order.masterUserId))
                .flatMap(platformRepository::findUser)
                .map(user -> user.nickname)
                .orElse("平台客服");
    }

    private String summarizeMessage(MessageItemEntity item) {
        if (item == null) {
            return "";
        }
        String type = item.messageType == null ? "text" : item.messageType.trim().toLowerCase();
        return switch (type) {
            case "image" -> "[图片]";
            case "audio" -> "[语音]";
            default -> item.contentText == null ? "" : item.contentText;
        };
    }

    private long latestMessageId(String sessionId) {
        List<MessageItemEntity> items = platformRepository.listMessageItems(sessionId);
        if (items.isEmpty()) {
            return 0L;
        }
        MessageItemEntity latest = items.get(items.size() - 1);
        return latest.id == null ? 0L : latest.id;
    }

    private String messageReaderCode(DomainEnums.RoleCode roleCode) {
        return roleCode == DomainEnums.RoleCode.MASTER ? "master" : "user";
    }

    private String normalizeMessageSender(String senderCode) {
        if (senderCode == null || senderCode.isBlank()) {
            return "system";
        }
        return senderCode.trim().toLowerCase();
    }

    private boolean isSystemSession(MessageSessionEntity session) {
        return session.orderId == null || session.orderId.isBlank();
    }

    private String messageSessionType(MessageSessionEntity session) {
        return isSystemSession(session) ? "system" : "order";
    }

    private DomainEnums.RoleCode systemSessionRole(String sessionId) {
        if (sessionId == null || sessionId.isBlank()) {
            return DomainEnums.RoleCode.USER;
        }
        String[] parts = sessionId.split("-");
        if (parts.length < 3) {
            return DomainEnums.RoleCode.USER;
        }
        return "MASTER".equalsIgnoreCase(parts[1]) ? DomainEnums.RoleCode.MASTER : DomainEnums.RoleCode.USER;
    }

    private String buildDistanceText(String orderId) {
        return mapService.eta(new MapDtos.EtaRequest(orderId, null, null)).distance();
    }

    private String statusText(DomainEnums.ServiceOrderStatus status) {
        return switch (status) {
            case PENDING_PAYMENT -> "待支付预付款，支付后开始派单";
            case PENDING_DISPATCH -> "平台正在匹配可服务师傅";
            case PENDING_ACCEPT -> "等待师傅确认上门";
            case ON_THE_WAY -> "师傅已出发，正在赶来";
            case ARRIVED -> "师傅已到场，等待开始施工";
            case WAITING_SUPPLEMENT_PAYMENT -> "师傅已提交增项报价，等待补款";
            case IN_SERVICE -> "订单正在施工中";
            case COMPLETED -> "服务已完成，欢迎评价";
            case REFUNDING -> "退款处理中";
            case CANCELLED -> "订单已取消";
            case AFTER_SALES -> "售后处理中";
        };
    }

    private String productStatusText(ProductOrderEntity order) {
        if (order.status == null) {
            return "商品订单处理中";
        }
        return switch (order.status) {
            case PENDING_PAYMENT -> "等待完成支付确认";
            case PAID -> "支付成功，等待仓库备货";
            case PENDING_SHIPMENT -> "仓库已打包，等待出库";
            case SHIPPED -> "商品已发货，可关注物流进度";
            case COMPLETED -> "商品已完成签收";
            case REFUNDING -> "售后处理中";
        };
    }

    private String refundStatusText(RefundRequestEntity refundRequest) {
        if (refundRequest.status == null) {
            return "售后处理中";
        }
        return switch (refundRequest.status) {
            case PENDING_REVIEW -> "等待平台审核";
            case APPROVED -> "审核通过，退款处理中";
            case REJECTED -> "审核驳回";
            case COMPLETED -> "退款完成";
        };
    }

    private long extractDocumentNumericId(String id) {
        String digits = id == null ? "" : id.replaceAll("\\D", "");
        if (digits.isBlank()) {
            return 0L;
        }
        return Long.parseLong(digits);
    }

    private String maskMobile(String mobile) {
        if (mobile == null || mobile.length() < 7) {
            return "";
        }
        return mobile.substring(0, 3) + "****" + mobile.substring(mobile.length() - 4);
    }

    private SupportDtos.CommentPayload toCommentPayload(CommentEntity entity) {
        return new SupportDtos.CommentPayload(
                entity.id,
                entity.userName,
                entity.score,
                entity.contentText,
                assembler.splitList(entity.imagesText),
                assembler.splitList(entity.tagsText),
                entity.createdAt == null ? "" : entity.createdAt.format(COMMENT_DATE_FORMATTER)
        );
    }

    private SupportDtos.OrderTrackPointPayload toTrackPointPayload(OrderTrackPointEntity entity) {
        return new SupportDtos.OrderTrackPointPayload(
                entity.id,
                entity.pointType,
                entity.labelText,
                entity.descriptionText,
                entity.latitude,
                entity.longitude,
                entity.createdAt
        );
    }
}
