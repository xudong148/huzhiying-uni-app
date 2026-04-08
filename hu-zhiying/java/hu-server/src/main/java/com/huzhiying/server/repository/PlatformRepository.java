package com.huzhiying.server.repository;

import com.huzhiying.domain.enums.DomainEnums.RoleCode;
import com.huzhiying.server.persistence.PersistenceEntities.AddressEntity;
import com.huzhiying.server.persistence.PersistenceEntities.ArbitrationCaseEntity;
import com.huzhiying.server.persistence.PersistenceEntities.BannerEntity;
import com.huzhiying.server.persistence.PersistenceEntities.CouponEntity;
import com.huzhiying.server.persistence.PersistenceEntities.DispatchTaskEntity;
import com.huzhiying.server.persistence.PersistenceEntities.MasterProfileEntity;
import com.huzhiying.server.persistence.PersistenceEntities.MemberLevelEntity;
import com.huzhiying.server.persistence.PersistenceEntities.MediaFileEntity;
import com.huzhiying.server.persistence.PersistenceEntities.MessageItemEntity;
import com.huzhiying.server.persistence.PersistenceEntities.MessageSessionEntity;
import com.huzhiying.server.persistence.PersistenceEntities.NoticeEntity;
import com.huzhiying.server.persistence.PersistenceEntities.OrderTrackPointEntity;
import com.huzhiying.server.persistence.PersistenceEntities.ProductEntity;
import com.huzhiying.server.persistence.PersistenceEntities.ProductOrderEntity;
import com.huzhiying.server.persistence.PersistenceEntities.QuotationEntity;
import com.huzhiying.server.persistence.PersistenceEntities.QuotationItemEntity;
import com.huzhiying.server.persistence.PersistenceEntities.CommentEntity;
import com.huzhiying.server.persistence.PersistenceEntities.ServiceCategoryEntity;
import com.huzhiying.server.persistence.PersistenceEntities.ServiceItemEntity;
import com.huzhiying.server.persistence.PersistenceEntities.ServiceOrderEntity;
import com.huzhiying.server.persistence.PersistenceEntities.SkuEntity;
import com.huzhiying.server.persistence.PersistenceEntities.UserEntity;
import com.huzhiying.server.persistence.PersistenceEntities.WalletAccountEntity;
import com.huzhiying.server.persistence.PersistenceEntities.WalletTransactionEntity;
import com.huzhiying.server.persistence.PersistenceEntities.WorkStepRecordEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PlatformRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public <T> List<T> listAll(String entityName, String orderClause, Class<T> entityClass) {
        return entityManager.createQuery(
                        "select e from " + entityName + " e order by " + orderClause,
                        entityClass
                )
                .getResultList();
    }

    public <T> Optional<T> findById(Class<T> entityClass, Object id) {
        return Optional.ofNullable(entityManager.find(entityClass, id));
    }

    public <T> T saveEntity(T entity) {
        return entityManager.merge(entity);
    }

    public <T> void deleteEntity(Class<T> entityClass, Object id) {
        findById(entityClass, id).ifPresent(entityManager::remove);
    }

    public Long nextLongId(String entityName, String fieldName, long initialValue) {
        Long value = entityManager.createQuery(
                        "select coalesce(max(e." + fieldName + "), :initialValue) from " + entityName + " e",
                        Long.class
                )
                .setParameter("initialValue", initialValue)
                .getSingleResult();
        return value + 1;
    }

    public List<UserEntity> listUsers() {
        return entityManager.createQuery("select u from UserEntity u order by u.id", UserEntity.class).getResultList();
    }

    public Optional<UserEntity> findUser(Long id) {
        return Optional.ofNullable(entityManager.find(UserEntity.class, id));
    }

    public Optional<UserEntity> findFirstUserByRole(RoleCode roleCode) {
        TypedQuery<UserEntity> query = entityManager.createQuery(
                "select u from UserEntity u where u.roleCode = :role order by u.id",
                UserEntity.class
        );
        query.setParameter("role", roleCode);
        query.setMaxResults(1);
        return query.getResultList().stream().findFirst();
    }

    public Optional<UserEntity> findUserByNickname(String nickname) {
        TypedQuery<UserEntity> query = entityManager.createQuery(
                "select u from UserEntity u where u.nickname = :nickname",
                UserEntity.class
        );
        query.setParameter("nickname", nickname);
        query.setMaxResults(1);
        return query.getResultList().stream().findFirst();
    }

    public UserEntity saveUser(UserEntity entity) {
        return entityManager.merge(entity);
    }

    public List<AddressEntity> listAddressesByUserId(Long userId) {
        return entityManager.createQuery(
                        "select a from AddressEntity a where a.userId = :userId order by a.isDefault desc, a.id asc",
                        AddressEntity.class
                )
                .setParameter("userId", userId)
                .getResultList();
    }

    public Optional<AddressEntity> findAddress(Long id) {
        return Optional.ofNullable(entityManager.find(AddressEntity.class, id));
    }

    public AddressEntity saveAddress(AddressEntity entity) {
        return entityManager.merge(entity);
    }

    public void deleteAddress(Long id) {
        deleteEntity(AddressEntity.class, id);
    }

    public List<MasterProfileEntity> listMasterProfiles() {
        return entityManager.createQuery("select m from MasterProfileEntity m order by m.creditScore desc", MasterProfileEntity.class)
                .getResultList();
    }

    public Optional<MasterProfileEntity> findMasterProfileByUserId(Long userId) {
        TypedQuery<MasterProfileEntity> query = entityManager.createQuery(
                "select m from MasterProfileEntity m where m.userId = :userId",
                MasterProfileEntity.class
        );
        query.setParameter("userId", userId);
        query.setMaxResults(1);
        return query.getResultList().stream().findFirst();
    }

    public Optional<MasterProfileEntity> findMasterProfileByName(String realName) {
        TypedQuery<MasterProfileEntity> query = entityManager.createQuery(
                "select m from MasterProfileEntity m where m.realName = :realName",
                MasterProfileEntity.class
        );
        query.setParameter("realName", realName);
        query.setMaxResults(1);
        return query.getResultList().stream().findFirst();
    }

    public MasterProfileEntity saveMasterProfile(MasterProfileEntity entity) {
        return entityManager.merge(entity);
    }

    public List<ServiceCategoryEntity> listServiceCategories() {
        return entityManager.createQuery("select c from ServiceCategoryEntity c order by c.sortOrder asc, c.id asc", ServiceCategoryEntity.class)
                .getResultList();
    }

    public List<ServiceItemEntity> listServiceItems() {
        return entityManager.createQuery("select s from ServiceItemEntity s order by s.id asc", ServiceItemEntity.class).getResultList();
    }

    public List<ServiceItemEntity> listServiceItemsByCategoryIds(List<Long> categoryIds) {
        if (categoryIds.isEmpty()) {
            return List.of();
        }
        return entityManager.createQuery(
                        "select s from ServiceItemEntity s where s.categoryId in :categoryIds order by s.id asc",
                        ServiceItemEntity.class
                )
                .setParameter("categoryIds", categoryIds)
                .getResultList();
    }

    public Optional<ServiceItemEntity> findServiceItem(Long id) {
        return Optional.ofNullable(entityManager.find(ServiceItemEntity.class, id));
    }

    public List<CommentEntity> listCommentsByServiceItemId(Long serviceItemId) {
        return entityManager.createQuery(
                        "select c from CommentEntity c where c.serviceItemId = :serviceItemId order by c.createdAt desc, c.id desc",
                        CommentEntity.class
                )
                .setParameter("serviceItemId", serviceItemId)
                .getResultList();
    }

    public List<ProductEntity> listProducts() {
        return entityManager.createQuery("select p from ProductEntity p order by p.id asc", ProductEntity.class).getResultList();
    }

    public Optional<ProductEntity> findProduct(Long id) {
        return Optional.ofNullable(entityManager.find(ProductEntity.class, id));
    }

    public List<SkuEntity> listSkusByProductIds(List<Long> productIds) {
        if (productIds.isEmpty()) {
            return List.of();
        }
        return entityManager.createQuery(
                        "select s from SkuEntity s where s.productId in :productIds order by s.id asc",
                        SkuEntity.class
                )
                .setParameter("productIds", productIds)
                .getResultList();
    }

    public List<ServiceOrderEntity> listServiceOrders() {
        return entityManager.createQuery("select s from ServiceOrderEntity s order by s.createdAt desc", ServiceOrderEntity.class)
                .getResultList();
    }

    public Optional<ServiceOrderEntity> findServiceOrder(String id) {
        return Optional.ofNullable(entityManager.find(ServiceOrderEntity.class, id));
    }

    public ServiceOrderEntity saveServiceOrder(ServiceOrderEntity entity) {
        return entityManager.merge(entity);
    }

    public List<OrderTrackPointEntity> listTrackPoints(String orderId) {
        return entityManager.createQuery(
                        "select t from OrderTrackPointEntity t where t.orderId = :orderId order by t.createdAt asc, t.id asc",
                        OrderTrackPointEntity.class
                )
                .setParameter("orderId", orderId)
                .getResultList();
    }

    public OrderTrackPointEntity saveTrackPoint(OrderTrackPointEntity entity) {
        return entityManager.merge(entity);
    }

    public List<ProductOrderEntity> listProductOrders() {
        return entityManager.createQuery("select p from ProductOrderEntity p order by p.createdAt desc", ProductOrderEntity.class)
                .getResultList();
    }

    public Optional<ProductOrderEntity> findProductOrder(String id) {
        return Optional.ofNullable(entityManager.find(ProductOrderEntity.class, id));
    }

    public ProductOrderEntity saveProductOrder(ProductOrderEntity entity) {
        return entityManager.merge(entity);
    }

    public List<DispatchTaskEntity> listDispatchTasks() {
        return entityManager.createQuery("select d from DispatchTaskEntity d order by d.createdAt desc", DispatchTaskEntity.class)
                .getResultList();
    }

    public Optional<DispatchTaskEntity> findDispatchTask(String id) {
        return Optional.ofNullable(entityManager.find(DispatchTaskEntity.class, id));
    }

    public Optional<DispatchTaskEntity> findDispatchTaskByOrderId(String orderId) {
        TypedQuery<DispatchTaskEntity> query = entityManager.createQuery(
                "select d from DispatchTaskEntity d where d.orderId = :orderId order by d.createdAt desc",
                DispatchTaskEntity.class
        );
        query.setParameter("orderId", orderId);
        query.setMaxResults(1);
        return query.getResultList().stream().findFirst();
    }

    public DispatchTaskEntity saveDispatchTask(DispatchTaskEntity entity) {
        return entityManager.merge(entity);
    }

    public List<WorkStepRecordEntity> listWorkSteps(String orderId) {
        return entityManager.createQuery(
                        "select w from WorkStepRecordEntity w where w.orderId = :orderId order by w.stepTime asc, w.id asc",
                        WorkStepRecordEntity.class
                )
                .setParameter("orderId", orderId)
                .getResultList();
    }

    public WorkStepRecordEntity saveWorkStep(WorkStepRecordEntity entity) {
        return entityManager.merge(entity);
    }

    public void deleteWorkSteps(String orderId) {
        entityManager.createQuery("delete from WorkStepRecordEntity w where w.orderId = :orderId")
                .setParameter("orderId", orderId)
                .executeUpdate();
    }

    public Optional<QuotationEntity> findQuotation(String id) {
        return Optional.ofNullable(entityManager.find(QuotationEntity.class, id));
    }

    public Optional<QuotationEntity> findQuotationByOrderId(String orderId) {
        TypedQuery<QuotationEntity> query = entityManager.createQuery(
                "select q from QuotationEntity q where q.orderId = :orderId",
                QuotationEntity.class
        );
        query.setParameter("orderId", orderId);
        query.setMaxResults(1);
        return query.getResultList().stream().findFirst();
    }

    public QuotationEntity saveQuotation(QuotationEntity entity) {
        return entityManager.merge(entity);
    }

    public List<QuotationItemEntity> listQuotationItems(String quotationId) {
        return entityManager.createQuery(
                        "select q from QuotationItemEntity q where q.quotationId = :quotationId order by q.id asc",
                        QuotationItemEntity.class
                )
                .setParameter("quotationId", quotationId)
                .getResultList();
    }

    public QuotationItemEntity saveQuotationItem(QuotationItemEntity entity) {
        return entityManager.merge(entity);
    }

    public void deleteQuotationItems(String quotationId) {
        entityManager.createQuery("delete from QuotationItemEntity q where q.quotationId = :quotationId")
                .setParameter("quotationId", quotationId)
                .executeUpdate();
    }

    public Optional<WalletAccountEntity> findWalletAccountByMasterUserId(Long masterUserId) {
        TypedQuery<WalletAccountEntity> query = entityManager.createQuery(
                "select w from WalletAccountEntity w where w.masterUserId = :masterUserId",
                WalletAccountEntity.class
        );
        query.setParameter("masterUserId", masterUserId);
        query.setMaxResults(1);
        return query.getResultList().stream().findFirst();
    }

    public WalletAccountEntity saveWalletAccount(WalletAccountEntity entity) {
        return entityManager.merge(entity);
    }

    public List<WalletTransactionEntity> listWalletTransactions(Long walletAccountId) {
        return entityManager.createQuery(
                        "select w from WalletTransactionEntity w where w.walletAccountId = :walletAccountId order by w.id desc",
                        WalletTransactionEntity.class
                )
                .setParameter("walletAccountId", walletAccountId)
                .getResultList();
    }

    public WalletTransactionEntity saveWalletTransaction(WalletTransactionEntity entity) {
        return entityManager.merge(entity);
    }

    public List<MessageSessionEntity> listMessageSessions() {
        return entityManager.createQuery("select m from MessageSessionEntity m order by m.id asc", MessageSessionEntity.class)
                .getResultList();
    }

    public Optional<MessageSessionEntity> findMessageSession(String id) {
        return Optional.ofNullable(entityManager.find(MessageSessionEntity.class, id));
    }

    public List<MessageItemEntity> listMessageItems(String sessionId) {
        return entityManager.createQuery(
                        "select m from MessageItemEntity m where m.sessionId = :sessionId order by m.id asc",
                        MessageItemEntity.class
                )
                .setParameter("sessionId", sessionId)
                .getResultList();
    }

    public MessageItemEntity saveMessageItem(MessageItemEntity entity) {
        return entityManager.merge(entity);
    }

    public Optional<MediaFileEntity> findMediaFile(Long id) {
        return Optional.ofNullable(entityManager.find(MediaFileEntity.class, id));
    }

    public List<MediaFileEntity> listMediaFilesByBiz(String bizType, String bizId) {
        return entityManager.createQuery(
                        "select m from MediaFileEntity m where m.bizType = :bizType and m.bizId = :bizId order by m.createdAt asc, m.id asc",
                        MediaFileEntity.class
                )
                .setParameter("bizType", bizType)
                .setParameter("bizId", bizId)
                .getResultList();
    }

    public MediaFileEntity saveMediaFile(MediaFileEntity entity) {
        return entityManager.merge(entity);
    }

    public List<BannerEntity> listBanners() {
        return entityManager.createQuery("select b from BannerEntity b order by b.id asc", BannerEntity.class).getResultList();
    }

    public List<NoticeEntity> listNotices() {
        return entityManager.createQuery("select n from NoticeEntity n order by n.id asc", NoticeEntity.class).getResultList();
    }

    public List<ArbitrationCaseEntity> listArbitrations() {
        return entityManager.createQuery("select a from ArbitrationCaseEntity a order by a.id asc", ArbitrationCaseEntity.class)
                .getResultList();
    }

    public List<CouponEntity> listCoupons() {
        return entityManager.createQuery("select c from CouponEntity c order by c.id asc", CouponEntity.class).getResultList();
    }

    public Optional<MemberLevelEntity> findTopMemberLevel() {
        TypedQuery<MemberLevelEntity> query = entityManager.createQuery(
                "select m from MemberLevelEntity m order by m.pointsRequired desc",
                MemberLevelEntity.class
        );
        query.setMaxResults(1);
        return query.getResultList().stream().findFirst();
    }
}
