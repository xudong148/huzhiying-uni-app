package com.huzhiying.server.service;

import com.huzhiying.domain.model.DomainModels.Address;
import com.huzhiying.domain.model.DomainModels.ArbitrationCase;
import com.huzhiying.domain.model.DomainModels.Banner;
import com.huzhiying.domain.model.DomainModels.Coupon;
import com.huzhiying.domain.model.DomainModels.DispatchTask;
import com.huzhiying.domain.model.DomainModels.MasterProfile;
import com.huzhiying.domain.model.DomainModels.MemberLevel;
import com.huzhiying.domain.model.DomainModels.MessageItem;
import com.huzhiying.domain.model.DomainModels.MessageSession;
import com.huzhiying.domain.model.DomainModels.Notice;
import com.huzhiying.domain.model.DomainModels.Product;
import com.huzhiying.domain.model.DomainModels.ProductOrder;
import com.huzhiying.domain.model.DomainModels.Quotation;
import com.huzhiying.domain.model.DomainModels.QuotationItem;
import com.huzhiying.domain.model.DomainModels.SearchDocument;
import com.huzhiying.domain.model.DomainModels.ServiceCategory;
import com.huzhiying.domain.model.DomainModels.ServiceItem;
import com.huzhiying.domain.model.DomainModels.ServiceOrder;
import com.huzhiying.domain.model.DomainModels.Sku;
import com.huzhiying.domain.model.DomainModels.User;
import com.huzhiying.domain.model.DomainModels.WalletAccount;
import com.huzhiying.domain.model.DomainModels.WalletTransaction;
import com.huzhiying.domain.model.DomainModels.WorkStepRecord;
import com.huzhiying.server.persistence.PersistenceEntities.AddressEntity;
import com.huzhiying.server.persistence.PersistenceEntities.ArbitrationCaseEntity;
import com.huzhiying.server.persistence.PersistenceEntities.BannerEntity;
import com.huzhiying.server.persistence.PersistenceEntities.CouponEntity;
import com.huzhiying.server.persistence.PersistenceEntities.DispatchTaskEntity;
import com.huzhiying.server.persistence.PersistenceEntities.MasterProfileEntity;
import com.huzhiying.server.persistence.PersistenceEntities.MemberLevelEntity;
import com.huzhiying.server.persistence.PersistenceEntities.MessageItemEntity;
import com.huzhiying.server.persistence.PersistenceEntities.MessageSessionEntity;
import com.huzhiying.server.persistence.PersistenceEntities.NoticeEntity;
import com.huzhiying.server.persistence.PersistenceEntities.ProductEntity;
import com.huzhiying.server.persistence.PersistenceEntities.ProductOrderEntity;
import com.huzhiying.server.persistence.PersistenceEntities.QuotationEntity;
import com.huzhiying.server.persistence.PersistenceEntities.QuotationItemEntity;
import com.huzhiying.server.persistence.PersistenceEntities.ServiceCategoryEntity;
import com.huzhiying.server.persistence.PersistenceEntities.ServiceItemEntity;
import com.huzhiying.server.persistence.PersistenceEntities.ServiceOrderEntity;
import com.huzhiying.server.persistence.PersistenceEntities.SkuEntity;
import com.huzhiying.server.persistence.PersistenceEntities.UserEntity;
import com.huzhiying.server.persistence.PersistenceEntities.WalletAccountEntity;
import com.huzhiying.server.persistence.PersistenceEntities.WalletTransactionEntity;
import com.huzhiying.server.persistence.PersistenceEntities.WorkStepRecordEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PlatformAssembler {

    public List<String> splitList(String raw) {
        if (raw == null || raw.isBlank()) {
            return List.of();
        }
        return Arrays.stream(raw.split("\\|"))
                .map(String::trim)
                .filter(item -> !item.isEmpty())
                .toList();
    }

    public String joinList(List<String> values) {
        if (values == null || values.isEmpty()) {
            return "";
        }
        return values.stream()
                .map(String::trim)
                .filter(item -> !item.isEmpty())
                .collect(Collectors.joining("|"));
    }

    public User toUser(UserEntity entity) {
        return new User(entity.id, entity.nickname, entity.mobile, entity.roleCode, entity.avatar, entity.levelName);
    }

    public Address toAddress(AddressEntity entity) {
        return new Address(entity.id, entity.tagName, entity.contactName, entity.mobile, entity.detailAddress,
                entity.latitude, entity.longitude, Boolean.TRUE.equals(entity.isDefault));
    }

    public MasterProfile toMasterProfile(MasterProfileEntity entity, UserEntity userEntity) {
        String displayName = entity.realName != null && !entity.realName.isBlank() ? entity.realName : userEntity.nickname;
        return new MasterProfile(entity.userId, displayName, entity.skillTags, entity.serviceArea,
                entity.deposit, entity.creditScore, Boolean.TRUE.equals(entity.online));
    }

    public ServiceItem toServiceItem(ServiceItemEntity entity) {
        return new ServiceItem(entity.id, entity.categoryId, entity.name, entity.subtitle, entity.basePrice,
                entity.doorPrice, splitList(entity.guaranteesText), splitList(entity.tagsText));
    }

    public ServiceCategory toServiceCategory(ServiceCategoryEntity entity, List<ServiceItem> services) {
        return new ServiceCategory(entity.id, entity.name, entity.icon, services);
    }

    public Sku toSku(SkuEntity entity) {
        return new Sku(entity.id, entity.productId, entity.name, entity.price, entity.stock);
    }

    public Product toProduct(ProductEntity entity, List<Sku> skus) {
        return new Product(entity.id, entity.name, entity.descriptionText, entity.price, skus,
                Boolean.TRUE.equals(entity.createInstallOrder));
    }

    public SearchDocument toSearchDocument(String id, String type, String title, String summary, BigDecimal price, String icon) {
        return new SearchDocument(id, type, title, summary, price, icon);
    }

    public WorkStepRecord toWorkStepRecord(WorkStepRecordEntity entity) {
        return new WorkStepRecord(entity.stepKey, entity.labelText, entity.descriptionText,
                Boolean.TRUE.equals(entity.done), entity.stepTime);
    }

    public QuotationItem toQuotationItem(QuotationItemEntity entity) {
        return new QuotationItem(entity.name, entity.amount);
    }

    public Quotation toQuotation(QuotationEntity entity, List<QuotationItem> items) {
        return new Quotation(entity.id, entity.orderId, items, entity.totalAmount, entity.status, entity.remarkText);
    }

    public ServiceOrder toServiceOrder(ServiceOrderEntity entity, Address address, UserEntity userEntity,
                                       UserEntity masterEntity, List<WorkStepRecord> timeline, Quotation quotation) {
        return new ServiceOrder(entity.id, entity.title, entity.status, entity.paymentStatus,
                userEntity == null ? "" : userEntity.nickname,
                masterEntity == null ? "待接单" : masterEntity.nickname,
                entity.appointment, address, entity.amount, entity.dispatchMode, timeline, quotation, entity.etaText);
    }

    public ProductOrder toProductOrder(ProductOrderEntity entity, Address address, UserEntity userEntity) {
        return new ProductOrder(entity.id, entity.title, entity.status, entity.paymentStatus,
                userEntity == null ? "" : userEntity.nickname, address, entity.amount,
                Boolean.TRUE.equals(entity.createInstallOrder), entity.installServiceOrderId);
    }

    public DispatchTask toDispatchTask(DispatchTaskEntity entity, UserEntity masterEntity) {
        return new DispatchTask(entity.id, entity.orderId, entity.title, entity.income, entity.distanceText,
                entity.areaText, entity.dispatchMode, masterEntity == null ? "待接单" : masterEntity.nickname,
                splitList(entity.tagsText));
    }

    public Coupon toCoupon(CouponEntity entity) {
        return new Coupon(entity.id, entity.title, entity.amount, entity.thresholdText, entity.expireAt);
    }

    public MemberLevel toMemberLevel(MemberLevelEntity entity) {
        return new MemberLevel(entity.name, entity.benefitText, entity.pointsRequired);
    }

    public WalletAccount toWalletAccount(WalletAccountEntity entity) {
        return new WalletAccount(entity.availableAmount, entity.frozenAmount, entity.todayIncome);
    }

    public WalletTransaction toWalletTransaction(WalletTransactionEntity entity) {
        return new WalletTransaction(entity.id, entity.title, entity.amount, entity.transactionTime);
    }

    public ArbitrationCase toArbitration(ArbitrationCaseEntity entity) {
        return new ArbitrationCase(entity.id, entity.orderId, entity.reasonText, entity.statusText);
    }

    public MessageSession toMessageSession(MessageSessionEntity entity, UserEntity participant) {
        return new MessageSession(entity.id, entity.orderId, entity.title, participant == null ? "" : participant.nickname);
    }

    public MessageItem toMessageItem(MessageItemEntity entity) {
        return new MessageItem(entity.id, entity.sessionId, entity.senderCode, entity.messageType, entity.contentText, entity.messageTime);
    }

    public Banner toBanner(BannerEntity entity) {
        return new Banner(entity.id, entity.title, entity.subtitle, entity.image);
    }

    public Notice toNotice(NoticeEntity entity) {
        return new Notice(entity.id, entity.title, entity.levelCode);
    }

    public Map<Long, List<Sku>> groupSkus(List<SkuEntity> entities) {
        return entities.stream().map(this::toSku).collect(Collectors.groupingBy(Sku::productId));
    }

    public Map<Long, List<ServiceItem>> groupServiceItems(List<ServiceItemEntity> entities) {
        return entities.stream().map(this::toServiceItem).collect(Collectors.groupingBy(ServiceItem::categoryId));
    }
}
