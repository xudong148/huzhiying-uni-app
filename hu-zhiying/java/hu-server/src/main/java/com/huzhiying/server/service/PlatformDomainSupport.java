package com.huzhiying.server.service;

import com.huzhiying.domain.enums.DomainEnums.PaymentStatus;
import com.huzhiying.domain.enums.DomainEnums.ProductOrderStatus;
import com.huzhiying.domain.enums.DomainEnums.RoleCode;
import com.huzhiying.domain.enums.DomainEnums.ServiceOrderStatus;
import com.huzhiying.domain.model.DomainModels.Address;
import com.huzhiying.domain.model.DomainModels.DispatchTask;
import com.huzhiying.domain.model.DomainModels.ProductOrder;
import com.huzhiying.domain.model.DomainModels.Quotation;
import com.huzhiying.domain.model.DomainModels.QuotationItem;
import com.huzhiying.domain.model.DomainModels.ServiceOrder;
import com.huzhiying.server.persistence.PersistenceEntities.DispatchTaskEntity;
import com.huzhiying.server.persistence.PersistenceEntities.MasterProfileEntity;
import com.huzhiying.server.persistence.PersistenceEntities.ProductOrderEntity;
import com.huzhiying.server.persistence.PersistenceEntities.ServiceItemEntity;
import com.huzhiying.server.persistence.PersistenceEntities.ServiceOrderEntity;
import com.huzhiying.server.persistence.PersistenceEntities.UserEntity;
import com.huzhiying.server.persistence.PersistenceEntities.WalletAccountEntity;
import com.huzhiying.server.persistence.PersistenceEntities.WalletTransactionEntity;
import com.huzhiying.server.persistence.PersistenceEntities.WorkStepRecordEntity;
import com.huzhiying.server.repository.PlatformRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class PlatformDomainSupport {

    public static final long DEFAULT_USER_ID = 10001L;
    public static final long DEFAULT_MASTER_USER_ID = 20001L;

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
    public void createSettlementIfNeeded(ServiceOrderEntity entity) {
        if (entity.masterUserId == null) {
            return;
        }
        WalletAccountEntity account = platformRepository.findWalletAccountByMasterUserId(entity.masterUserId).orElseThrow();
        String title = entity.id + " " + entity.title + " 结算";
        boolean alreadySettled = platformRepository.listWalletTransactions(account.id).stream()
                .anyMatch(item -> title.equals(item.title));
        if (alreadySettled) {
            return;
        }

        BigDecimal settlementAmount = entity.amount == null ? BigDecimal.ZERO : entity.amount;
        account.availableAmount = account.availableAmount.add(settlementAmount);
        account.todayIncome = account.todayIncome.add(settlementAmount);
        platformRepository.saveWalletAccount(account);

        WalletTransactionEntity transactionEntity = new WalletTransactionEntity();
        transactionEntity.walletAccountId = account.id;
        transactionEntity.title = title;
        transactionEntity.amount = settlementAmount;
        transactionEntity.transactionTime = "刚刚";
        platformRepository.saveWalletTransaction(transactionEntity);
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
            case PENDING_PAYMENT -> "待支付";
            case PENDING_DISPATCH -> "待派单";
            case PENDING_ACCEPT -> "待接单";
            case ON_THE_WAY -> "出发中";
            case ARRIVED -> "已到场";
            case WAITING_SUPPLEMENT_PAYMENT -> "待补款";
            case IN_SERVICE -> "施工中";
            case COMPLETED -> "已完成";
            case CANCELLED -> "已取消";
            case REFUNDING -> "退款中";
            case AFTER_SALES -> "售后中";
        };
    }

    public String productStatusLabel(ProductOrderStatus status) {
        return switch (status) {
            case PENDING_PAYMENT -> "待支付";
            case PAID -> "已支付";
            case PENDING_SHIPMENT -> "待发货";
            case SHIPPED -> "已发货";
            case COMPLETED -> "已完成";
            case REFUNDING -> "退款中";
        };
    }

    public String mapTaskStatus(String taskStatus) {
        if (taskStatus == null) {
            return "待接单";
        }
        return switch (taskStatus) {
            case "CLAIMED" -> "已抢单";
            case "FORCE_ASSIGNED" -> "已强派";
            case "ASSIGNED" -> "待上门";
            default -> "抢单中";
        };
    }
}
