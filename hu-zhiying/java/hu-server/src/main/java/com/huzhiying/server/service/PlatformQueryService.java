package com.huzhiying.server.service;

import com.huzhiying.domain.model.DomainModels.MemberLevel;
import com.huzhiying.domain.model.DomainModels.SearchDocument;
import com.huzhiying.domain.model.DomainModels.ServiceCategory;
import com.huzhiying.domain.model.DomainModels.ServiceItem;
import com.huzhiying.domain.model.DomainModels.ServiceOrder;
import com.huzhiying.server.persistence.PersistenceEntities.MasterProfileEntity;
import com.huzhiying.server.persistence.PersistenceEntities.ServiceItemEntity;
import com.huzhiying.server.repository.PlatformRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class PlatformQueryService {

    private final PlatformRepository platformRepository;
    private final PlatformAssembler assembler;
    private final PlatformDomainSupport domainSupport;
    private final boolean wechatPayEnabled;
    private final String wechatAppId;

    public PlatformQueryService(PlatformRepository platformRepository,
                                PlatformAssembler assembler,
                                PlatformDomainSupport domainSupport,
                                @Value("${hzy.wechat.pay-enabled:false}") boolean wechatPayEnabled,
                                @Value("${hzy.wechat.app-id:}") String wechatAppId) {
        this.platformRepository = platformRepository;
        this.assembler = assembler;
        this.domainSupport = domainSupport;
        this.wechatPayEnabled = wechatPayEnabled;
        this.wechatAppId = wechatAppId;
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
        payload.put("comments", List.of(
                Map.of(
                        "id", 1,
                        "user", "赵女士",
                        "score", 5,
                        "content", "响应很快，到场前会电话确认，增项报价也说得很清楚。",
                        "images", List.of("https://picsum.photos/360/240?random=214"),
                        "date", "2026-04-06"
                ),
                Map.of(
                        "id", 2,
                        "user", "吴先生",
                        "score", 4,
                        "content", "收费透明，现场解决了冷媒不足的问题。",
                        "images", List.of(),
                        "date", "2026-04-02"
                )
        ));
        return payload;
    }

    public List<?> products() {
        Map<Long, List<com.huzhiying.domain.model.DomainModels.Sku>> groupedSkus =
                assembler.groupSkus(platformRepository.listSkusByProductIds(platformRepository.listProducts().stream().map(product -> product.id).toList()));
        return platformRepository.listProducts().stream()
                .map(product -> assembler.toProduct(product, groupedSkus.getOrDefault(product.id, List.of())))
                .toList();
    }

    public Map<String, Object> productDetail(Long productId) {
        var productEntity = platformRepository.findProduct(productId).orElseThrow();
        var skus = assembler.groupSkus(platformRepository.listSkusByProductIds(List.of(productId)))
                .getOrDefault(productId, List.of());

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("id", productEntity.id);
        payload.put("title", productEntity.name);
        payload.put("subtitle", productEntity.descriptionText);
        payload.put("price", productEntity.price);
        payload.put("images", productEntity.imageUrl == null || productEntity.imageUrl.isBlank()
                ? List.of("https://picsum.photos/960/720?random=" + productId)
                : List.of(productEntity.imageUrl));
        payload.put("createInstallOrder", Boolean.TRUE.equals(productEntity.createInstallOrder));
        payload.put("deliveryDesc", Boolean.TRUE.equals(productEntity.createInstallOrder)
                ? "支付成功后自动生成安装工单，平台同步派单"
                : "现货商品发货，默认 1-2 天内送达");
        payload.put("highlights", Boolean.TRUE.equals(productEntity.createInstallOrder)
                ? List.of("正品保障", "包安装调试", "电子质保单")
                : List.of("现货速发", "平台售后", "支持开票"));
        payload.put("skus", skus.stream().map(item -> Map.of(
                "id", item.id(),
                "name", item.name(),
                "price", item.price(),
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
        return documents.stream().filter(item -> item.title().contains(query) || item.summary().contains(query)).toList();
    }

    public Map<String, Object> currentUser() {
        return Map.of(
                "profile", assembler.toUser(domainSupport.findUserEntity(PlatformDomainSupport.DEFAULT_USER_ID)),
                "banners", platformRepository.listBanners().stream().map(assembler::toBanner).toList(),
                "notices", platformRepository.listNotices().stream().map(assembler::toNotice).toList()
        );
    }

    public List<?> addresses() {
        return platformRepository.listAddressesByUserId(PlatformDomainSupport.DEFAULT_USER_ID).stream().map(assembler::toAddress).toList();
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
        return platformRepository.listServiceOrders().stream().map(domainSupport::buildServiceOrder).toList();
    }

    public ServiceOrder serviceOrder(String id) {
        return domainSupport.buildServiceOrder(domainSupport.findServiceOrderEntity(id));
    }

    public List<?> productOrders() {
        return platformRepository.listProductOrders().stream().map(domainSupport::buildProductOrder).toList();
    }

    public Object productOrder(String id) {
        return domainSupport.buildProductOrder(platformRepository.findProductOrder(id).orElseThrow());
    }

    public List<?> dispatchTasks() {
        return platformRepository.listDispatchTasks().stream().map(domainSupport::buildDispatchTask).toList();
    }

    public Map<String, Object> masterDashboard() {
        Long masterUserId = PlatformDomainSupport.DEFAULT_MASTER_USER_ID;
        return Map.of(
                "dispatchCount", platformRepository.listDispatchTasks().size(),
                "wallet", wallet(),
                "orders", platformRepository.listServiceOrders().stream()
                        .filter(order -> masterUserId.equals(order.masterUserId))
                        .map(domainSupport::buildServiceOrder)
                        .toList()
        );
    }

    public Object wallet() {
        return assembler.toWalletAccount(platformRepository.findWalletAccountByMasterUserId(PlatformDomainSupport.DEFAULT_MASTER_USER_ID).orElseThrow());
    }

    public List<?> walletTransactions() {
        Long accountId = platformRepository.findWalletAccountByMasterUserId(PlatformDomainSupport.DEFAULT_MASTER_USER_ID).orElseThrow().id;
        return platformRepository.listWalletTransactions(accountId).stream().map(assembler::toWalletTransaction).toList();
    }

    public Map<String, Object> masterSettings() {
        MasterProfileEntity profileEntity = platformRepository.findMasterProfileByUserId(PlatformDomainSupport.DEFAULT_MASTER_USER_ID).orElseThrow();
        return Map.of(
                "listening", Boolean.TRUE.equals(profileEntity.listening),
                "maxDistance", profileEntity.maxDistanceKm + "km",
                "privacyNumber", Boolean.TRUE.equals(profileEntity.privacyNumber)
        );
    }

    public List<?> messageSessions() {
        return platformRepository.listMessageSessions().stream().map(session ->
                assembler.toMessageSession(session, platformRepository.findUser(session.participantUserId).orElse(null))
        ).toList();
    }

    public List<?> messageItems(String sessionId) {
        return platformRepository.listMessageItems(sessionId).stream().map(assembler::toMessageItem).toList();
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
        return Map.of(
                "gmv", gmv.toPlainString(),
                "serviceOrders", platformRepository.listServiceOrders().size(),
                "productOrders", platformRepository.listProductOrders().size(),
                "onlineMasters", onlineMasters,
                "warning", platformRepository.listArbitrations().size()
        );
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
                        "type", task.title.contains("安装") ? "安装" : "维修",
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
        platformRepository.listWalletTransactions(accountId).forEach(item -> rows.add(Map.of(
                "billNo", "SETTLE-" + item.id,
                "type", "师傅结算",
                "amount", item.amount,
                "status", item.amount.signum() >= 0 ? "待结算" : "已冻结"
        )));
        platformRepository.listServiceOrders().stream()
                .filter(order -> order.paymentStatus == com.huzhiying.domain.enums.DomainEnums.PaymentStatus.REFUNDING
                        || order.status == com.huzhiying.domain.enums.DomainEnums.ServiceOrderStatus.REFUNDING)
                .forEach(order -> rows.add(Map.of(
                        "billNo", "REFUND-" + order.id,
                        "type", "退款单",
                        "amount", order.amount,
                        "status", "处理中"
                )));
        return rows;
    }

    public Map<String, Object> createWechatPrepay(String orderId) {
        return Map.of(
                "orderId", orderId,
                "appId", wechatAppId == null || wechatAppId.isBlank() ? "demo-wx-appid" : wechatAppId,
                "timeStamp", String.valueOf(System.currentTimeMillis() / 1000),
                "nonceStr", java.util.UUID.randomUUID().toString().replace("-", ""),
                "packageValue", "prepay_id=demo",
                "signType", "RSA",
                "paySign", "demo-sign",
                "sandbox", !wechatPayEnabled
        );
    }
}
