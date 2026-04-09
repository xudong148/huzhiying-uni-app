package com.huzhiying.server.service;

import com.huzhiying.domain.enums.DomainEnums.NotificationTaskStatus;
import com.huzhiying.domain.enums.DomainEnums.PaymentRecordStatus;
import com.huzhiying.domain.enums.DomainEnums.PaymentStatus;
import com.huzhiying.domain.enums.DomainEnums.ProductOrderStatus;
import com.huzhiying.domain.enums.DomainEnums.RefundRequestStatus;
import com.huzhiying.domain.enums.DomainEnums.RoleCode;
import com.huzhiying.domain.enums.DomainEnums.SettlementBillStatus;
import com.huzhiying.domain.enums.DomainEnums.ServiceOrderStatus;
import com.huzhiying.domain.enums.DomainEnums.WalletLedgerDirection;
import com.huzhiying.domain.enums.DomainEnums.WalletLedgerStatus;
import com.huzhiying.domain.model.DomainModels.Address;
import com.huzhiying.domain.model.DomainModels.DispatchTask;
import com.huzhiying.domain.model.DomainModels.ProductOrder;
import com.huzhiying.domain.model.DomainModels.Quotation;
import com.huzhiying.domain.model.DomainModels.QuotationItem;
import com.huzhiying.domain.model.DomainModels.ServiceOrder;
import com.huzhiying.server.persistence.PersistenceEntities.AuditLogEntity;
import com.huzhiying.server.persistence.PersistenceEntities.DispatchTaskEntity;
import com.huzhiying.server.persistence.PersistenceEntities.MasterProfileEntity;
import com.huzhiying.server.persistence.PersistenceEntities.NotificationTaskEntity;
import com.huzhiying.server.persistence.PersistenceEntities.PaymentRecordEntity;
import com.huzhiying.server.persistence.PersistenceEntities.ProductOrderEntity;
import com.huzhiying.server.persistence.PersistenceEntities.RefundRequestEntity;
import com.huzhiying.server.persistence.PersistenceEntities.ServiceItemEntity;
import com.huzhiying.server.persistence.PersistenceEntities.ServiceOrderEntity;
import com.huzhiying.server.persistence.PersistenceEntities.SettlementBillEntity;
import com.huzhiying.server.persistence.PersistenceEntities.UserEntity;
import com.huzhiying.server.persistence.PersistenceEntities.WalletAccountEntity;
import com.huzhiying.server.persistence.PersistenceEntities.WalletLedgerEntity;
import com.huzhiying.server.persistence.PersistenceEntities.WalletTransactionEntity;
import com.huzhiying.server.persistence.PersistenceEntities.WorkStepRecordEntity;
import com.huzhiying.server.repository.PlatformRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class PlatformDomainSupport {

    public static final long DEFAULT_USER_ID = 10001L;
    public static final long DEFAULT_MASTER_USER_ID = 20001L;

    private static final String ORDER_TYPE_SERVICE = "SERVICE";
    private static final String ORDER_TYPE_PRODUCT = "PRODUCT";
    private static final String CHANNEL_WECHAT = "WECHAT";
    private static final String CHANNEL_PLATFORM = "PLATFORM";

    private final PlatformRepository platformRepository;
    private final PlatformAssembler assembler;

    public PlatformDomainSupport(PlatformRepository platformRepository, PlatformAssembler assembler) {
        this.platformRepository = platformRepository;
        this.assembler = assembler;
    }

    public UserEntity findUserEntity(Long id) {
        return platformRepository.findUser(id).orElseThrow();
    }

    public ServiceItemEntity findServiceItemEntity(Long id) {
        return platformRepository.findServiceItem(id).orElseThrow();
    }

    public Optional<ServiceItemEntity> findServiceItemByTitle(String title) {
        return platformRepository.listServiceItems().stream()
                .filter(item -> item.name.equals(title))
                .findFirst();
    }

    public ServiceOrderEntity findServiceOrderEntity(String id) {
        return platformRepository.findServiceOrder(id).orElseThrow();
    }

    public MasterProfileEntity resolveMaster(String masterName) {
        if (masterName != null && !masterName.isBlank()) {
            return platformRepository.findMasterProfileByName(masterName)
                    .orElseGet(() -> platformRepository.findMasterProfileByUserId(DEFAULT_MASTER_USER_ID).orElseThrow());
        }
        return platformRepository.findMasterProfileByUserId(DEFAULT_MASTER_USER_ID).orElseThrow();
    }

    public ServiceOrder buildServiceOrder(ServiceOrderEntity entity) {
        Address address = platformRepository.findAddress(entity.addressId).map(assembler::toAddress).orElse(null);
        UserEntity userEntity = platformRepository.findUser(entity.userId).orElse(null);
        UserEntity masterEntity = entity.masterUserId == null ? null : platformRepository.findUser(entity.masterUserId).orElse(null);
        List<com.huzhiying.domain.model.DomainModels.WorkStepRecord> timeline = platformRepository.listWorkSteps(entity.id).stream()
                .map(assembler::toWorkStepRecord)
                .toList();
        Quotation quotation = buildQuotation(entity.id).orElse(null);
        return assembler.toServiceOrder(entity, address, userEntity, masterEntity, timeline, quotation);
    }

    public Optional<Quotation> buildQuotation(String orderId) {
        return platformRepository.findQuotationByOrderId(orderId).map(entity -> {
            List<QuotationItem> items = platformRepository.listQuotationItems(entity.id).stream()
                    .map(assembler::toQuotationItem)
                    .toList();
            return assembler.toQuotation(entity, items);
        });
    }

    public ProductOrder buildProductOrder(ProductOrderEntity entity) {
        Address address = platformRepository.findAddress(entity.addressId).map(assembler::toAddress).orElse(null);
        UserEntity userEntity = platformRepository.findUser(entity.userId).orElse(null);
        return assembler.toProductOrder(entity, address, userEntity);
    }

    public DispatchTask buildDispatchTask(DispatchTaskEntity entity) {
        UserEntity masterEntity = entity.currentMasterUserId == null ? null : platformRepository.findUser(entity.currentMasterUserId).orElse(null);
        return assembler.toDispatchTask(entity, masterEntity);
    }

    @Transactional
    public void saveStep(String orderId, String key, String label, String description, boolean done) {
        WorkStepRecordEntity entity = new WorkStepRecordEntity();
        entity.orderId = orderId;
        entity.stepKey = key;
        entity.labelText = label;
        entity.descriptionText = description;
        entity.done = done;
        entity.stepTime = LocalDateTime.now();
        platformRepository.saveWorkStep(entity);
    }

    @Transactional
    public PaymentRecordEntity recordPayment(String orderId, String orderType, BigDecimal amount, Long userId,
                                             Long masterId, String paymentStage, String remark) {
        PaymentRecordEntity paymentRecord = preparePaymentRecord(
                orderId,
                orderType,
                amount,
                userId,
                masterId,
                paymentStage,
                CHANNEL_WECHAT,
                remark
        );
        return recordPayment(paymentRecord, paymentRecord.externalTransactionNo, remark);
    }

    @Transactional
    public PaymentRecordEntity preparePaymentRecord(String orderId, String orderType, BigDecimal amount, Long userId,
                                                    Long masterId, String paymentStage, String channel, String remark) {
        PaymentRecordEntity successRecord = platformRepository.listPaymentRecordsByOrderId(orderId).stream()
                .filter(item -> paymentStage.equals(item.paymentStage))
                .filter(item -> item.status == PaymentRecordStatus.SUCCESS)
                .findFirst()
                .orElse(null);
        if (successRecord != null) {
            return successRecord;
        }

        PaymentRecordEntity pendingRecord = platformRepository.listPaymentRecordsByOrderId(orderId).stream()
                .filter(item -> paymentStage.equals(item.paymentStage))
                .filter(item -> item.status == PaymentRecordStatus.PENDING)
                .findFirst()
                .orElse(null);
        if (pendingRecord != null) {
            pendingRecord.channel = channel == null || channel.isBlank() ? pendingRecord.channel : channel;
            pendingRecord.amount = safeAmount(amount);
            pendingRecord.updatedAt = LocalDateTime.now();
            pendingRecord.remarkText = remark;
            return platformRepository.savePaymentRecord(pendingRecord);
        }

        LocalDateTime now = LocalDateTime.now();
        PaymentRecordEntity entity = new PaymentRecordEntity();
        entity.bizNo = "PAY-" + System.currentTimeMillis();
        entity.orderId = orderId;
        entity.orderType = orderType;
        entity.status = PaymentRecordStatus.PENDING;
        entity.channel = channel == null || channel.isBlank() ? CHANNEL_WECHAT : channel;
        entity.amount = safeAmount(amount);
        entity.userId = userId;
        entity.masterId = masterId;
        entity.traceId = nextTraceId("pay");
        entity.paymentStage = paymentStage;
        entity.remarkText = remark;
        entity.createdAt = now;
        entity.updatedAt = now;
        return platformRepository.savePaymentRecord(entity);
    }

    @Transactional
    public PaymentRecordEntity recordPayment(PaymentRecordEntity entity, String externalTransactionNo, String remark) {
        if (entity.status == PaymentRecordStatus.SUCCESS) {
            return entity;
        }
        LocalDateTime now = LocalDateTime.now();
        entity.status = PaymentRecordStatus.SUCCESS;
        entity.externalTransactionNo = externalTransactionNo == null || externalTransactionNo.isBlank()
                ? entity.externalTransactionNo
                : externalTransactionNo;
        entity.updatedAt = now;
        entity.remarkText = remark;
        PaymentRecordEntity saved = platformRepository.savePaymentRecord(entity);
        saveAuditLog(saved.orderType, saved.orderId, "PAYMENT_CALLBACK_CONFIRMED", saved.status.name(), "SYSTEM",
                null, saved.userId, saved.masterId, saved.traceId, remark);
        enqueueNotification(saved.orderType, saved.orderId, "USER", saved.userId, CHANNEL_PLATFORM,
                "payment-success", "{\"stage\":\"" + saved.paymentStage + "\"}", saved.traceId);
        return saved;
    }

    @Transactional
    public PaymentRecordEntity markPaymentFailed(PaymentRecordEntity entity, String remark) {
        if (entity.status == PaymentRecordStatus.SUCCESS) {
            return entity;
        }
        entity.status = PaymentRecordStatus.FAILED;
        entity.updatedAt = LocalDateTime.now();
        entity.remarkText = remark;
        PaymentRecordEntity saved = platformRepository.savePaymentRecord(entity);
        saveAuditLog(saved.orderType, saved.orderId, "PAYMENT_PREPAY_FAILED", saved.status.name(), "SYSTEM",
                null, saved.userId, saved.masterId, saved.traceId, remark);
        return saved;
    }

    @Transactional
    public RefundRequestEntity createRefundRequestIfNeeded(String orderId, String orderType, BigDecimal amount,
                                                           Long userId, Long masterId, String reason, String source) {
        RefundRequestEntity existing = platformRepository.findLatestRefundRequestByOrderId(orderId)
                .filter(item -> item.status == RefundRequestStatus.PENDING_REVIEW
                        || item.status == RefundRequestStatus.APPROVED
                        || item.status == RefundRequestStatus.COMPLETED)
                .orElse(null);
        if (existing != null) {
            return existing;
        }

        LocalDateTime now = LocalDateTime.now();
        String traceId = nextTraceId("refund");
        RefundRequestEntity entity = new RefundRequestEntity();
        entity.bizNo = "RFD-" + System.currentTimeMillis();
        entity.orderId = orderId;
        entity.orderType = orderType;
        entity.paymentRecordId = platformRepository.findLatestPaymentRecordByOrderId(orderId).map(item -> item.id).orElse(null);
        entity.status = RefundRequestStatus.PENDING_REVIEW;
        entity.channel = normalizeRefundSource(source);
        entity.amount = safeAmount(amount);
        entity.userId = userId;
        entity.masterId = masterId;
        entity.traceId = traceId;
        entity.reasonText = reason;
        entity.createdAt = now;
        entity.updatedAt = now;
        RefundRequestEntity saved = platformRepository.saveRefundRequest(entity);
        platformRepository.listPaymentRecordsByOrderId(orderId).forEach(item -> {
            if (item.status == PaymentRecordStatus.SUCCESS) {
                item.status = PaymentRecordStatus.REFUNDING;
                item.updatedAt = now;
                platformRepository.savePaymentRecord(item);
            }
        });
        saveAuditLog(orderType, orderId, "REFUND_REQUEST_CREATED", entity.status.name(), "USER",
                userId, userId, masterId, traceId, reason);
        enqueueNotification(orderType, orderId, "ADMIN", null, CHANNEL_PLATFORM,
                "refund-review-required", "{\"orderId\":\"" + orderId + "\"}", traceId);
        return saved;
    }

    @Transactional
    public void createSettlementIfNeeded(ServiceOrderEntity entity) {
        if (entity.masterUserId == null) {
            return;
        }
        if (platformRepository.findSettlementBillByOrderId(entity.id).isPresent()) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        String title = settlementTitle(entity.id, entity.title);
        WalletAccountEntity account = platformRepository.findWalletAccountByMasterUserId(entity.masterUserId).orElseThrow();

        SettlementBillEntity bill = new SettlementBillEntity();
        bill.bizNo = "STL-" + System.currentTimeMillis();
        bill.orderId = entity.id;
        bill.orderType = ORDER_TYPE_SERVICE;
        bill.status = SettlementBillStatus.PENDING_REVIEW;
        bill.channel = CHANNEL_PLATFORM;
        bill.amount = safeAmount(entity.amount);
        bill.userId = entity.userId;
        bill.masterId = entity.masterUserId;
        bill.traceId = nextTraceId("settlement");
        bill.walletAccountId = account.id;
        bill.remarkText = title;
        bill.createdAt = now;
        bill.updatedAt = now;
        SettlementBillEntity savedBill = platformRepository.saveSettlementBill(bill);

        WalletLedgerEntity ledger = new WalletLedgerEntity();
        ledger.bizNo = "LEDGER-" + System.currentTimeMillis();
        ledger.walletAccountId = account.id;
        ledger.settlementBillId = savedBill.id;
        ledger.orderId = entity.id;
        ledger.orderType = ORDER_TYPE_SERVICE;
        ledger.status = WalletLedgerStatus.PENDING_REVIEW;
        ledger.directionCode = WalletLedgerDirection.IN;
        ledger.channel = CHANNEL_PLATFORM;
        ledger.amount = safeAmount(entity.amount);
        ledger.userId = entity.userId;
        ledger.masterId = entity.masterUserId;
        ledger.traceId = bill.traceId;
        ledger.title = title;
        ledger.createdAt = now;
        ledger.updatedAt = now;
        platformRepository.saveWalletLedger(ledger);

        WalletTransactionEntity transactionEntity = findWalletTransactionByTitle(account.id, title)
                .orElseGet(WalletTransactionEntity::new);
        transactionEntity.walletAccountId = account.id;
        transactionEntity.title = title;
        transactionEntity.amount = safeAmount(entity.amount);
        transactionEntity.transactionTime = "鍒氬垰";
        transactionEntity.statusText = "待结算";
        platformRepository.saveWalletTransaction(transactionEntity);

        saveAuditLog(ORDER_TYPE_SERVICE, entity.id, "SETTLEMENT_CREATED", bill.status.name(), "SYSTEM",
                null, entity.userId, entity.masterUserId, bill.traceId, title);
        enqueueNotification(ORDER_TYPE_SERVICE, entity.id, "MASTER", entity.masterUserId, CHANNEL_PLATFORM,
                "settlement-created", "{\"amount\":\"" + safeAmount(entity.amount) + "\"}", bill.traceId);
    }

    @Transactional
    public SettlementBillEntity approveSettlement(SettlementBillEntity bill, Long operatorId, String remark) {
        if (bill.status == SettlementBillStatus.APPROVED) {
            return bill;
        }

        LocalDateTime now = LocalDateTime.now();
        WalletAccountEntity account = resolveWalletAccount(bill.walletAccountId, bill.masterId);
        account.availableAmount = safeAmount(account.availableAmount).add(safeAmount(bill.amount));
        account.todayIncome = safeAmount(account.todayIncome).add(safeAmount(bill.amount));
        platformRepository.saveWalletAccount(account);

        bill.status = SettlementBillStatus.APPROVED;
        bill.operatorId = operatorId;
        bill.updatedAt = now;
        bill.approvedAt = now;
        bill.settledAt = now;
        SettlementBillEntity savedBill = platformRepository.saveSettlementBill(bill);

        platformRepository.findWalletLedgerBySettlementBillId(savedBill.id).ifPresent(ledger -> {
            ledger.status = WalletLedgerStatus.POSTED;
            ledger.operatorId = operatorId;
            ledger.updatedAt = now;
            platformRepository.saveWalletLedger(ledger);
        });

        String title = savedBill.remarkText == null || savedBill.remarkText.isBlank()
                ? settlementTitle(savedBill.orderId, savedBill.orderId)
                : savedBill.remarkText;
        WalletTransactionEntity transactionEntity = findWalletTransactionByTitle(account.id, title)
                .orElseGet(WalletTransactionEntity::new);
        transactionEntity.walletAccountId = account.id;
        transactionEntity.title = title;
        transactionEntity.amount = safeAmount(savedBill.amount);
        transactionEntity.transactionTime = "鍒氬垰";
        transactionEntity.statusText = "已结算";
        platformRepository.saveWalletTransaction(transactionEntity);

        saveAuditLog(savedBill.orderType, savedBill.orderId, "SETTLEMENT_APPROVED", savedBill.status.name(), "ADMIN",
                operatorId, savedBill.userId, savedBill.masterId, nextTraceId("settlement-approve"), remark);
        enqueueNotification(savedBill.orderType, savedBill.orderId, "MASTER", savedBill.masterId, CHANNEL_PLATFORM,
                "settlement-approved", "{\"billNo\":\"SETTLE-" + savedBill.id + "\"}", nextTraceId("notify"));
        return savedBill;
    }

    @Transactional
    public RefundRequestEntity completeRefund(RefundRequestEntity refundRequest, Long operatorId, String remark) {
        LocalDateTime now = LocalDateTime.now();
        if (refundRequest.status != RefundRequestStatus.COMPLETED) {
            refundRequest.status = RefundRequestStatus.COMPLETED;
            refundRequest.operatorId = operatorId;
            refundRequest.updatedAt = now;
            refundRequest.approvedAt = refundRequest.approvedAt == null ? now : refundRequest.approvedAt;
            refundRequest.completedAt = now;
            refundRequest = platformRepository.saveRefundRequest(refundRequest);
        }

        platformRepository.listPaymentRecordsByOrderId(refundRequest.orderId).forEach(item -> {
            if (item.status == PaymentRecordStatus.SUCCESS || item.status == PaymentRecordStatus.REFUNDING) {
                item.status = PaymentRecordStatus.REFUNDED;
                item.updatedAt = now;
                platformRepository.savePaymentRecord(item);
            }
        });

        reverseSettlementIfNeeded(refundRequest, operatorId, remark);
        saveAuditLog(refundRequest.orderType, refundRequest.orderId, "REFUND_APPROVED", refundRequest.status.name(), "ADMIN",
                operatorId, refundRequest.userId, refundRequest.masterId, nextTraceId("refund-approve"), remark);
        enqueueNotification(refundRequest.orderType, refundRequest.orderId, "USER", refundRequest.userId, CHANNEL_PLATFORM,
                "refund-completed", "{\"orderId\":\"" + refundRequest.orderId + "\"}", nextTraceId("notify"));
        return refundRequest;
    }

    @Transactional
    public AuditLogEntity saveAuditLog(String bizType, String bizId, String actionCode, String statusCode,
                                       String operatorRole, Long operatorId, Long userId, Long masterId,
                                       String traceId, String detailText) {
        AuditLogEntity entity = new AuditLogEntity();
        entity.bizNo = "AUD-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 6);
        entity.bizType = bizType;
        entity.bizId = bizId;
        entity.actionCode = actionCode;
        entity.statusCode = statusCode;
        entity.operatorRole = operatorRole;
        entity.operatorId = operatorId;
        entity.userId = userId;
        entity.masterId = masterId;
        entity.traceId = traceId;
        entity.detailText = detailText;
        entity.createdAt = LocalDateTime.now();
        return platformRepository.saveAuditLog(entity);
    }

    @Transactional
    public NotificationTaskEntity enqueueNotification(String bizType, String bizId, String targetRole,
                                                      Long targetUserId, String channel, String templateCode,
                                                      String payloadText, String traceId) {
        NotificationTaskEntity entity = new NotificationTaskEntity();
        entity.bizNo = "NTF-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 6);
        entity.bizType = bizType;
        entity.bizId = bizId;
        entity.targetRole = targetRole;
        entity.targetUserId = targetUserId;
        entity.channel = channel;
        entity.templateCode = templateCode;
        entity.status = NotificationTaskStatus.PENDING;
        entity.payloadText = payloadText;
        entity.traceId = traceId;
        entity.createdAt = LocalDateTime.now();
        entity.updatedAt = entity.createdAt;
        return platformRepository.saveNotificationTask(entity);
    }

    public RoleCode parseRole(String role) {
        if (role == null || role.isBlank()) {
            return RoleCode.USER;
        }
        return switch (role.toLowerCase()) {
            case "master" -> RoleCode.MASTER;
            case "admin" -> RoleCode.ADMIN;
            case "customer_service" -> RoleCode.CUSTOMER_SERVICE;
            default -> RoleCode.USER;
        };
    }

    public String statusLabel(ServiceOrderStatus status) {
        return switch (status) {
            case PENDING_PAYMENT -> "寰呮敮浠?";
            case PENDING_DISPATCH -> "寰呮淳鍗?";
            case PENDING_ACCEPT -> "寰呮帴鍗?";
            case ON_THE_WAY -> "鍑哄彂涓?";
            case ARRIVED -> "宸插埌鍦?";
            case WAITING_SUPPLEMENT_PAYMENT -> "寰呰ˉ娆?";
            case IN_SERVICE -> "鏂藉伐涓?";
            case COMPLETED -> "宸插畬鎴?";
            case CANCELLED -> "宸插彇娑?";
            case REFUNDING -> "閫€娆句腑";
            case AFTER_SALES -> "鍞悗涓?";
        };
    }

    public String productStatusLabel(ProductOrderStatus status) {
        return switch (status) {
            case PENDING_PAYMENT -> "寰呮敮浠?";
            case PAID -> "宸叉敮浠?";
            case PENDING_SHIPMENT -> "寰呭彂璐?";
            case SHIPPED -> "宸插彂璐?";
            case COMPLETED -> "宸插畬鎴?";
            case REFUNDING -> "閫€娆句腑";
        };
    }

    public String paymentStatusLabel(PaymentStatus status) {
        return switch (status) {
            case UNPAID -> "鏈敮浠?";
            case PARTIAL_PAID -> "宸查浠?";
            case PAID -> "宸叉敮浠?";
            case REFUNDING -> "閫€娆句腑";
            case REFUNDED -> "宸查€€娆?";
        };
    }

    public String mapTaskStatus(String taskStatus) {
        if (taskStatus == null) {
            return "寰呮帴鍗?";
        }
        return switch (taskStatus) {
            case "CLAIMED" -> "宸叉姠鍗?";
            case "FORCE_ASSIGNED" -> "宸插己娲?";
            case "ASSIGNED" -> "寰呬笂闂?";
            case "PENDING" -> "鎶㈠崟涓?";
            default -> "鎶㈠崟涓?";
        };
    }

    private String normalizeRefundSource(String source) {
        return source == null || source.isBlank() ? "uni-app" : source.trim();
    }

    private void reverseSettlementIfNeeded(RefundRequestEntity refundRequest, Long operatorId, String remark) {
        SettlementBillEntity bill = platformRepository.findSettlementBillByOrderId(refundRequest.orderId).orElse(null);
        if (bill == null) {
            return;
        }
        if (platformRepository.findWalletLedgerByRefundRequestId(refundRequest.id).isPresent()) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        WalletAccountEntity account = resolveWalletAccount(bill.walletAccountId, bill.masterId);
        if (bill.status == SettlementBillStatus.APPROVED) {
            account.availableAmount = safeAmount(account.availableAmount).subtract(safeAmount(refundRequest.amount));
            account.todayIncome = safeAmount(account.todayIncome).subtract(safeAmount(refundRequest.amount));
            platformRepository.saveWalletAccount(account);
        }

        WalletLedgerEntity ledger = new WalletLedgerEntity();
        ledger.bizNo = "LEDGER-" + System.currentTimeMillis() + "-REV";
        ledger.walletAccountId = account.id;
        ledger.refundRequestId = refundRequest.id;
        ledger.orderId = refundRequest.orderId;
        ledger.orderType = refundRequest.orderType;
        ledger.status = WalletLedgerStatus.REVERSED;
        ledger.directionCode = WalletLedgerDirection.OUT;
        ledger.channel = CHANNEL_PLATFORM;
        ledger.amount = safeAmount(refundRequest.amount);
        ledger.userId = refundRequest.userId;
        ledger.masterId = refundRequest.masterId;
        ledger.operatorId = operatorId;
        ledger.traceId = nextTraceId("refund-reverse");
        ledger.title = refundTitle(refundRequest.orderId);
        ledger.createdAt = now;
        ledger.updatedAt = now;
        platformRepository.saveWalletLedger(ledger);

        WalletTransactionEntity transactionEntity = new WalletTransactionEntity();
        transactionEntity.walletAccountId = account.id;
        transactionEntity.title = refundTitle(refundRequest.orderId);
        transactionEntity.amount = safeAmount(refundRequest.amount).negate();
        transactionEntity.transactionTime = "鍒氬垰";
        transactionEntity.statusText = "已冲减";
        platformRepository.saveWalletTransaction(transactionEntity);

        bill.status = SettlementBillStatus.REVERSED;
        bill.operatorId = operatorId;
        bill.updatedAt = now;
        bill.remarkText = remark == null || remark.isBlank() ? bill.remarkText : remark;
        platformRepository.saveSettlementBill(bill);
    }

    private WalletAccountEntity resolveWalletAccount(Long walletAccountId, Long masterId) {
        if (walletAccountId != null) {
            return platformRepository.findWalletAccount(walletAccountId).orElseThrow();
        }
        return platformRepository.findWalletAccountByMasterUserId(masterId).orElseThrow();
    }

    private Optional<WalletTransactionEntity> findWalletTransactionByTitle(Long walletAccountId, String title) {
        return platformRepository.listWalletTransactions(walletAccountId).stream()
                .filter(item -> title.equals(item.title))
                .findFirst();
    }

    private BigDecimal safeAmount(BigDecimal amount) {
        return amount == null ? BigDecimal.ZERO : amount;
    }

    private String settlementTitle(String orderId, String title) {
        return orderId + " " + (title == null ? orderId : title) + " 缁撶畻";
    }

    private String refundTitle(String orderId) {
        return orderId + " 閫€娆惧啿鍑?";
    }

    private String nextTraceId(String prefix) {
        return prefix + "-" + UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }
}
