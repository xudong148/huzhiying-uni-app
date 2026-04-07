package com.huzhiying.server.service;

import com.huzhiying.domain.enums.DomainEnums.DispatchMode;
import com.huzhiying.domain.enums.DomainEnums.PaymentStatus;
import com.huzhiying.domain.enums.DomainEnums.QuotationStatus;
import com.huzhiying.domain.enums.DomainEnums.ServiceOrderStatus;
import com.huzhiying.domain.model.DomainModels.Address;
import com.huzhiying.domain.model.DomainModels.AuthToken;
import com.huzhiying.domain.model.DomainModels.DispatchTask;
import com.huzhiying.domain.model.DomainModels.MasterProfile;
import com.huzhiying.domain.model.DomainModels.MemberLevel;
import com.huzhiying.domain.model.DomainModels.MessageItem;
import com.huzhiying.domain.model.DomainModels.MessageSession;
import com.huzhiying.domain.model.DomainModels.ProductOrder;
import com.huzhiying.domain.model.DomainModels.Quotation;
import com.huzhiying.domain.model.DomainModels.QuotationItem;
import com.huzhiying.domain.model.DomainModels.SearchDocument;
import com.huzhiying.domain.model.DomainModels.ServiceOrder;
import com.huzhiying.domain.model.DomainModels.WalletAccount;
import com.huzhiying.domain.model.DomainModels.WalletTransaction;
import com.huzhiying.domain.model.DomainModels.WorkStepRecord;
import com.huzhiying.server.repository.DemoStateRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PlatformFacadeService {

    private final DemoStateRepository repository;
    private final PricingEngineService pricingEngineService;
    private final OrderWorkflowService orderWorkflowService;

    public PlatformFacadeService(DemoStateRepository repository, PricingEngineService pricingEngineService,
                                 OrderWorkflowService orderWorkflowService) {
        this.repository = repository;
        this.pricingEngineService = pricingEngineService;
        this.orderWorkflowService = orderWorkflowService;
    }

    public AuthToken login(String role) {
        return new AuthToken("mock-token-" + role, "mock-refresh-" + role, role);
    }

    public List<?> categories() {
        return repository.categories();
    }

    public List<?> services() {
        return repository.categories().stream().flatMap(item -> item.services().stream()).toList();
    }

    public List<?> products() {
        return repository.products();
    }

    public List<SearchDocument> search(String keyword) {
        return repository.searchDocuments().stream()
                .filter(item -> keyword == null || keyword.isBlank() || item.title().contains(keyword) || item.summary().contains(keyword))
                .toList();
    }

    public Map<String, Object> currentUser() {
        return Map.of(
                "profile", repository.users().get(0),
                "banners", repository.banners(),
                "notices", repository.notices()
        );
    }

    public List<Address> addresses() {
        return repository.addresses();
    }

    public List<?> coupons() {
        return repository.coupons();
    }

    public MemberLevel currentMember() {
        return repository.memberLevel();
    }

    public List<ServiceOrder> serviceOrders() {
        return new ArrayList<>(repository.serviceOrders().values());
    }

    public ServiceOrder serviceOrder(String id) {
        return repository.serviceOrders().get(id);
    }

    public ServiceOrder createServiceOrder(String title, String appointment, boolean emergency, boolean night) {
        Address address = repository.addresses().get(0);
        BigDecimal amount = pricingEngineService.estimate(BigDecimal.valueOf(58), BigDecimal.valueOf(30), emergency, night);
        ServiceOrder order = new ServiceOrder("SO" + System.currentTimeMillis(), title, ServiceOrderStatus.PENDING_DISPATCH,
                PaymentStatus.PARTIAL_PAID, "周女士", "待接单", appointment, address, amount, DispatchMode.ROB,
                List.of(new WorkStepRecord("created", "订单创建", "用户完成预付款并提交工单", true, LocalDateTime.now())),
                null, "30 分钟");
        repository.serviceOrders().put(order.id(), order);
        return order;
    }

    public ServiceOrder updateServiceOrderStatus(String id, ServiceOrderStatus status) {
        ServiceOrder updated = orderWorkflowService.advance(serviceOrder(id), status);
        repository.serviceOrders().put(id, updated);
        return updated;
    }

    public Quotation createQuotation(String orderId, String remark) {
        ServiceOrder order = serviceOrder(orderId);
        Quotation quotation = new Quotation("QT-" + UUID.randomUUID().toString().substring(0, 8), orderId,
                List.of(new QuotationItem("增项配件", BigDecimal.valueOf(88))),
                BigDecimal.valueOf(88), QuotationStatus.PENDING_CONFIRM, remark);
        ServiceOrder next = new ServiceOrder(order.id(), order.title(), ServiceOrderStatus.WAITING_SUPPLEMENT_PAYMENT,
                PaymentStatus.PARTIAL_PAID, order.userName(), order.masterName(), order.appointment(),
                order.address(), order.amount(), order.dispatchMode(), order.timeline(), quotation, order.eta());
        repository.serviceOrders().put(orderId, next);
        return quotation;
    }

    public ServiceOrder confirmQuotation(String quotationId) {
        ServiceOrder order = repository.serviceOrders().values().stream()
                .filter(item -> item.quotation() != null && quotationId.equals(item.quotation().id()))
                .findFirst()
                .orElseThrow();
        ServiceOrder updated = orderWorkflowService.confirmQuotation(order);
        repository.serviceOrders().put(order.id(), updated);
        return updated;
    }

    public List<ProductOrder> productOrders() {
        return new ArrayList<>(repository.productOrders().values());
    }

    public ProductOrder productOrder(String id) {
        return repository.productOrders().get(id);
    }

    public List<DispatchTask> dispatchTasks() {
        return new ArrayList<>(repository.dispatchTasks().values());
    }

    public DispatchTask claimTask(String id, String masterName) {
        DispatchTask task = repository.dispatchTasks().get(id);
        DispatchTask updated = new DispatchTask(task.id(), task.orderId(), task.title(), task.income(),
                task.distance(), task.area(), task.mode(), masterName, task.tags());
        repository.dispatchTasks().put(id, updated);
        return updated;
    }

    public MasterProfile applyMaster(String realName, String mobile, String skills, String area) {
        MasterProfile profile = new MasterProfile(System.currentTimeMillis(), realName + "(" + mobile + ")",
                skills, area, BigDecimal.valueOf(3000), 100, false);
        repository.masters().add(profile);
        return profile;
    }

    public Map<String, Object> masterDashboard() {
        return Map.of(
                "dispatchCount", repository.dispatchTasks().size(),
                "wallet", repository.walletAccount(),
                "orders", repository.dispatchTasks().values()
        );
    }

    public WalletAccount wallet() {
        return repository.walletAccount();
    }

    public List<WalletTransaction> walletTransactions() {
        return repository.walletTransactions();
    }

    public List<MessageSession> messageSessions() {
        return repository.messageSessions();
    }

    public List<MessageItem> messageItems(String sessionId) {
        return repository.messageItems().stream().filter(item -> sessionId.equals(item.sessionId())).toList();
    }

    public List<?> notices() {
        return repository.notices();
    }

    public Map<String, Object> adminDashboard() {
        BigDecimal gmv = repository.serviceOrders().values().stream().map(ServiceOrder::amount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .add(repository.productOrders().values().stream().map(ProductOrder::amount)
                        .reduce(BigDecimal.ZERO, BigDecimal::add));
        return Map.of(
                "gmv", gmv,
                "serviceOrders", repository.serviceOrders().size(),
                "productOrders", repository.productOrders().size(),
                "onlineMasters", repository.masters().stream().filter(MasterProfile::online).count(),
                "warning", repository.arbitrations().size()
        );
    }

    public List<MasterProfile> masters() {
        return repository.masters().stream()
                .sorted(Comparator.comparing(MasterProfile::creditScore).reversed())
                .toList();
    }

    public List<?> arbitrations() {
        return repository.arbitrations();
    }

    public List<?> pricingRules() {
        return repository.categories().stream()
                .flatMap(item -> item.services().stream())
                .map(service -> Map.of(
                        "category", service.name(),
                        "basePrice", service.basePrice(),
                        "doorPrice", service.doorPrice(),
                        "nightCoefficient", "夜间 +30%"
                ))
                .toList();
    }

    public Map<String, Object> createWechatPrepay(String orderId) {
        return Map.of(
                "orderId", orderId,
                "appId", "demo-wx-appid",
                "timeStamp", String.valueOf(System.currentTimeMillis() / 1000),
                "nonceStr", UUID.randomUUID().toString().replace("-", ""),
                "packageValue", "prepay_id=demo",
                "signType", "RSA",
                "paySign", "demo-sign"
        );
    }
}
