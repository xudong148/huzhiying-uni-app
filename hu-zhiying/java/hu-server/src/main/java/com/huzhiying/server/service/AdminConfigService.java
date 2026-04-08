package com.huzhiying.server.service;

import com.huzhiying.server.dto.AdminConfigDtos;
import com.huzhiying.server.persistence.PersistenceEntities;
import com.huzhiying.server.repository.PlatformRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 后台配置类资源的统一 CRUD 服务。
 */
@Service
@Transactional
public class AdminConfigService {

    private final PlatformRepository platformRepository;

    public AdminConfigService(PlatformRepository platformRepository) {
        this.platformRepository = platformRepository;
    }

    public List<AdminConfigDtos.BannerPayload> listBanners() {
        return platformRepository.listAll("BannerEntity", "e.sortOrder asc, e.id asc", PersistenceEntities.BannerEntity.class)
                .stream()
                .map(this::toBanner)
                .toList();
    }

    public AdminConfigDtos.BannerPayload getBanner(Long id) {
        return toBanner(platformRepository.findById(PersistenceEntities.BannerEntity.class, id).orElseThrow());
    }

    public AdminConfigDtos.BannerPayload saveBanner(Long id, AdminConfigDtos.BannerPayload payload) {
        PersistenceEntities.BannerEntity entity = id == null
                ? new PersistenceEntities.BannerEntity()
                : platformRepository.findById(PersistenceEntities.BannerEntity.class, id).orElse(new PersistenceEntities.BannerEntity());
        if (entity.id == null) {
            entity.id = nextId("BannerEntity", 0L);
        }
        entity.title = payload.title();
        entity.subtitle = payload.subtitle();
        entity.image = payload.image();
        entity.link = payload.link();
        entity.sortOrder = payload.sortOrder() == null ? 0 : payload.sortOrder();
        entity.enabled = enabled(payload.enabled());
        return toBanner(platformRepository.saveEntity(entity));
    }

    public void deleteBanner(Long id) {
        platformRepository.deleteEntity(PersistenceEntities.BannerEntity.class, id);
    }

    public List<AdminConfigDtos.NoticePayload> listNotices() {
        return platformRepository.listAll("NoticeEntity", "e.id asc", PersistenceEntities.NoticeEntity.class)
                .stream()
                .map(this::toNotice)
                .toList();
    }

    public AdminConfigDtos.NoticePayload getNotice(Long id) {
        return toNotice(platformRepository.findById(PersistenceEntities.NoticeEntity.class, id).orElseThrow());
    }

    public AdminConfigDtos.NoticePayload saveNotice(Long id, AdminConfigDtos.NoticePayload payload) {
        PersistenceEntities.NoticeEntity entity = id == null
                ? new PersistenceEntities.NoticeEntity()
                : platformRepository.findById(PersistenceEntities.NoticeEntity.class, id).orElse(new PersistenceEntities.NoticeEntity());
        if (entity.id == null) {
            entity.id = nextId("NoticeEntity", 0L);
        }
        entity.title = payload.title();
        entity.contentText = payload.content();
        entity.levelCode = payload.levelCode();
        entity.enabled = enabled(payload.enabled());
        return toNotice(platformRepository.saveEntity(entity));
    }

    public void deleteNotice(Long id) {
        platformRepository.deleteEntity(PersistenceEntities.NoticeEntity.class, id);
    }

    public List<AdminConfigDtos.AgreementPayload> listAgreements() {
        return platformRepository.listAll("AgreementEntity", "e.id asc", PersistenceEntities.AgreementEntity.class)
                .stream()
                .map(this::toAgreement)
                .toList();
    }

    public AdminConfigDtos.AgreementPayload getAgreement(Long id) {
        return toAgreement(platformRepository.findById(PersistenceEntities.AgreementEntity.class, id).orElseThrow());
    }

    public AdminConfigDtos.AgreementPayload saveAgreement(Long id, AdminConfigDtos.AgreementPayload payload) {
        PersistenceEntities.AgreementEntity entity = id == null
                ? new PersistenceEntities.AgreementEntity()
                : platformRepository.findById(PersistenceEntities.AgreementEntity.class, id).orElse(new PersistenceEntities.AgreementEntity());
        if (entity.id == null) {
            entity.id = nextId("AgreementEntity", 0L);
        }
        entity.title = payload.title();
        entity.version = payload.version();
        entity.contentText = payload.content();
        entity.enabled = enabled(payload.enabled());
        return toAgreement(platformRepository.saveEntity(entity));
    }

    public void deleteAgreement(Long id) {
        platformRepository.deleteEntity(PersistenceEntities.AgreementEntity.class, id);
    }

    public List<AdminConfigDtos.CouponPayload> listCoupons() {
        return platformRepository.listAll("CouponEntity", "e.id asc", PersistenceEntities.CouponEntity.class)
                .stream()
                .map(this::toCoupon)
                .toList();
    }

    public AdminConfigDtos.CouponPayload getCoupon(Long id) {
        return toCoupon(platformRepository.findById(PersistenceEntities.CouponEntity.class, id).orElseThrow());
    }

    public AdminConfigDtos.CouponPayload saveCoupon(Long id, AdminConfigDtos.CouponPayload payload) {
        PersistenceEntities.CouponEntity entity = id == null
                ? new PersistenceEntities.CouponEntity()
                : platformRepository.findById(PersistenceEntities.CouponEntity.class, id).orElse(new PersistenceEntities.CouponEntity());
        if (entity.id == null) {
            entity.id = nextId("CouponEntity", 0L);
        }
        entity.title = payload.title();
        entity.amount = payload.amount();
        entity.thresholdText = payload.thresholdText();
        entity.expireAt = payload.expireAt();
        entity.enabled = enabled(payload.enabled());
        return toCoupon(platformRepository.saveEntity(entity));
    }

    public void deleteCoupon(Long id) {
        platformRepository.deleteEntity(PersistenceEntities.CouponEntity.class, id);
    }

    public List<AdminConfigDtos.MemberLevelPayload> listMemberLevels() {
        return platformRepository.listAll("MemberLevelEntity", "e.pointsRequired asc, e.id asc", PersistenceEntities.MemberLevelEntity.class)
                .stream()
                .map(this::toMemberLevel)
                .toList();
    }

    public AdminConfigDtos.MemberLevelPayload getMemberLevel(Long id) {
        return toMemberLevel(platformRepository.findById(PersistenceEntities.MemberLevelEntity.class, id).orElseThrow());
    }

    public AdminConfigDtos.MemberLevelPayload saveMemberLevel(Long id, AdminConfigDtos.MemberLevelPayload payload) {
        PersistenceEntities.MemberLevelEntity entity = id == null
                ? new PersistenceEntities.MemberLevelEntity()
                : platformRepository.findById(PersistenceEntities.MemberLevelEntity.class, id).orElse(new PersistenceEntities.MemberLevelEntity());
        if (entity.id == null) {
            entity.id = nextId("MemberLevelEntity", 0L);
        }
        entity.name = payload.name();
        entity.benefitText = payload.benefitText();
        entity.pointsRequired = payload.pointsRequired();
        entity.enabled = enabled(payload.enabled());
        return toMemberLevel(platformRepository.saveEntity(entity));
    }

    public void deleteMemberLevel(Long id) {
        platformRepository.deleteEntity(PersistenceEntities.MemberLevelEntity.class, id);
    }

    public List<AdminConfigDtos.RolePayload> listRoles() {
        return platformRepository.listAll("RoleEntity", "e.id asc", PersistenceEntities.RoleEntity.class)
                .stream()
                .map(this::toRole)
                .toList();
    }

    public AdminConfigDtos.RolePayload getRole(Long id) {
        return toRole(platformRepository.findById(PersistenceEntities.RoleEntity.class, id).orElseThrow());
    }

    public AdminConfigDtos.RolePayload saveRole(Long id, AdminConfigDtos.RolePayload payload) {
        PersistenceEntities.RoleEntity entity = id == null
                ? new PersistenceEntities.RoleEntity()
                : platformRepository.findById(PersistenceEntities.RoleEntity.class, id).orElse(new PersistenceEntities.RoleEntity());
        if (entity.id == null) {
            entity.id = nextId("RoleEntity", 0L);
        }
        entity.code = payload.code();
        entity.name = payload.name();
        entity.descriptionText = payload.description();
        entity.enabled = enabled(payload.enabled());
        return toRole(platformRepository.saveEntity(entity));
    }

    public void deleteRole(Long id) {
        platformRepository.deleteEntity(PersistenceEntities.RoleEntity.class, id);
    }

    public List<AdminConfigDtos.MenuPayload> listMenus() {
        return platformRepository.listAll("MenuEntity", "e.sortOrder asc, e.id asc", PersistenceEntities.MenuEntity.class)
                .stream()
                .map(this::toMenu)
                .toList();
    }

    public AdminConfigDtos.MenuPayload getMenu(Long id) {
        return toMenu(platformRepository.findById(PersistenceEntities.MenuEntity.class, id).orElseThrow());
    }

    public AdminConfigDtos.MenuPayload saveMenu(Long id, AdminConfigDtos.MenuPayload payload) {
        PersistenceEntities.MenuEntity entity = id == null
                ? new PersistenceEntities.MenuEntity()
                : platformRepository.findById(PersistenceEntities.MenuEntity.class, id).orElse(new PersistenceEntities.MenuEntity());
        if (entity.id == null) {
            entity.id = nextId("MenuEntity", 0L);
        }
        entity.name = payload.name();
        entity.path = payload.path();
        entity.icon = payload.icon();
        entity.sortOrder = payload.sortOrder() == null ? 0 : payload.sortOrder();
        entity.enabled = enabled(payload.enabled());
        return toMenu(platformRepository.saveEntity(entity));
    }

    public void deleteMenu(Long id) {
        platformRepository.deleteEntity(PersistenceEntities.MenuEntity.class, id);
    }

    public List<AdminConfigDtos.PermissionPayload> listPermissions() {
        return platformRepository.listAll("PermissionEntity", "e.id asc", PersistenceEntities.PermissionEntity.class)
                .stream()
                .map(this::toPermission)
                .toList();
    }

    public AdminConfigDtos.PermissionPayload getPermission(Long id) {
        return toPermission(platformRepository.findById(PersistenceEntities.PermissionEntity.class, id).orElseThrow());
    }

    public AdminConfigDtos.PermissionPayload savePermission(Long id, AdminConfigDtos.PermissionPayload payload) {
        PersistenceEntities.PermissionEntity entity = id == null
                ? new PersistenceEntities.PermissionEntity()
                : platformRepository.findById(PersistenceEntities.PermissionEntity.class, id).orElse(new PersistenceEntities.PermissionEntity());
        if (entity.id == null) {
            entity.id = nextId("PermissionEntity", 0L);
        }
        entity.code = payload.code();
        entity.name = payload.name();
        entity.descriptionText = payload.description();
        entity.enabled = enabled(payload.enabled());
        return toPermission(platformRepository.saveEntity(entity));
    }

    public void deletePermission(Long id) {
        platformRepository.deleteEntity(PersistenceEntities.PermissionEntity.class, id);
    }

    public List<AdminConfigDtos.ServiceCategoryPayload> listCategories() {
        return platformRepository.listAll("ServiceCategoryEntity", "e.sortOrder asc, e.id asc", PersistenceEntities.ServiceCategoryEntity.class)
                .stream()
                .map(this::toCategory)
                .toList();
    }

    public AdminConfigDtos.ServiceCategoryPayload getCategory(Long id) {
        return toCategory(platformRepository.findById(PersistenceEntities.ServiceCategoryEntity.class, id).orElseThrow());
    }

    public AdminConfigDtos.ServiceCategoryPayload saveCategory(Long id, AdminConfigDtos.ServiceCategoryPayload payload) {
        PersistenceEntities.ServiceCategoryEntity entity = id == null
                ? new PersistenceEntities.ServiceCategoryEntity()
                : platformRepository.findById(PersistenceEntities.ServiceCategoryEntity.class, id).orElse(new PersistenceEntities.ServiceCategoryEntity());
        if (entity.id == null) {
            entity.id = nextId("ServiceCategoryEntity", 0L);
        }
        entity.name = payload.name();
        entity.icon = payload.icon();
        entity.sortOrder = payload.sortOrder() == null ? 0 : payload.sortOrder();
        entity.enabled = enabled(payload.enabled());
        return toCategory(platformRepository.saveEntity(entity));
    }

    public void deleteCategory(Long id) {
        platformRepository.deleteEntity(PersistenceEntities.ServiceCategoryEntity.class, id);
    }

    public List<AdminConfigDtos.ServiceItemPayload> listServiceItems() {
        return platformRepository.listAll("ServiceItemEntity", "e.categoryId asc, e.id asc", PersistenceEntities.ServiceItemEntity.class)
                .stream()
                .map(this::toServiceItem)
                .toList();
    }

    public AdminConfigDtos.ServiceItemPayload getServiceItem(Long id) {
        return toServiceItem(platformRepository.findById(PersistenceEntities.ServiceItemEntity.class, id).orElseThrow());
    }

    public AdminConfigDtos.ServiceItemPayload saveServiceItem(Long id, AdminConfigDtos.ServiceItemPayload payload) {
        PersistenceEntities.ServiceItemEntity entity = id == null
                ? new PersistenceEntities.ServiceItemEntity()
                : platformRepository.findById(PersistenceEntities.ServiceItemEntity.class, id).orElse(new PersistenceEntities.ServiceItemEntity());
        if (entity.id == null) {
            entity.id = nextId("ServiceItemEntity", 200L);
        }
        entity.categoryId = payload.categoryId();
        entity.name = payload.name();
        entity.subtitle = payload.subtitle();
        entity.basePrice = payload.basePrice();
        entity.doorPrice = payload.doorPrice();
        entity.guidePrice = payload.guidePrice();
        entity.warrantyText = payload.warrantyText();
        entity.guaranteesText = payload.guaranteesText();
        entity.tagsText = payload.tagsText();
        entity.imageUrls = payload.imageUrls();
        entity.processSteps = payload.processSteps();
        entity.enabled = enabled(payload.enabled());
        return toServiceItem(platformRepository.saveEntity(entity));
    }

    public void deleteServiceItem(Long id) {
        platformRepository.deleteEntity(PersistenceEntities.ServiceItemEntity.class, id);
    }

    public List<AdminConfigDtos.ProductPayload> listProducts() {
        return platformRepository.listAll("ProductEntity", "e.id asc", PersistenceEntities.ProductEntity.class)
                .stream()
                .map(this::toProduct)
                .toList();
    }

    public AdminConfigDtos.ProductPayload getProduct(Long id) {
        return toProduct(platformRepository.findById(PersistenceEntities.ProductEntity.class, id).orElseThrow());
    }

    public AdminConfigDtos.ProductPayload saveProduct(Long id, AdminConfigDtos.ProductPayload payload) {
        PersistenceEntities.ProductEntity entity = id == null
                ? new PersistenceEntities.ProductEntity()
                : platformRepository.findById(PersistenceEntities.ProductEntity.class, id).orElse(new PersistenceEntities.ProductEntity());
        if (entity.id == null) {
            entity.id = nextId("ProductEntity", 1000L);
        }
        entity.name = payload.name();
        entity.descriptionText = payload.descriptionText();
        entity.price = payload.price();
        entity.createInstallOrder = payload.createInstallOrder();
        entity.imageUrl = payload.imageUrl();
        entity.enabled = enabled(payload.enabled());
        return toProduct(platformRepository.saveEntity(entity));
    }

    public void deleteProduct(Long id) {
        platformRepository.deleteEntity(PersistenceEntities.ProductEntity.class, id);
    }

    public List<AdminConfigDtos.SkuPayload> listSkus() {
        return platformRepository.listAll("SkuEntity", "e.productId asc, e.id asc", PersistenceEntities.SkuEntity.class)
                .stream()
                .map(this::toSku)
                .toList();
    }

    public AdminConfigDtos.SkuPayload getSku(Long id) {
        return toSku(platformRepository.findById(PersistenceEntities.SkuEntity.class, id).orElseThrow());
    }

    public AdminConfigDtos.SkuPayload saveSku(Long id, AdminConfigDtos.SkuPayload payload) {
        PersistenceEntities.SkuEntity entity = id == null
                ? new PersistenceEntities.SkuEntity()
                : platformRepository.findById(PersistenceEntities.SkuEntity.class, id).orElse(new PersistenceEntities.SkuEntity());
        if (entity.id == null) {
            entity.id = nextId("SkuEntity", 0L);
        }
        entity.productId = payload.productId();
        entity.name = payload.name();
        entity.price = payload.price();
        entity.stock = payload.stock();
        entity.enabled = enabled(payload.enabled());
        return toSku(platformRepository.saveEntity(entity));
    }

    public void deleteSku(Long id) {
        platformRepository.deleteEntity(PersistenceEntities.SkuEntity.class, id);
    }

    public List<AdminConfigDtos.PricingRulePayload> listPricingRules() {
        return platformRepository.listAll("PricingRuleEntity", "e.id asc", PersistenceEntities.PricingRuleEntity.class)
                .stream()
                .map(this::toPricingRule)
                .toList();
    }

    public AdminConfigDtos.PricingRulePayload getPricingRule(Long id) {
        return toPricingRule(platformRepository.findById(PersistenceEntities.PricingRuleEntity.class, id).orElseThrow());
    }

    public AdminConfigDtos.PricingRulePayload savePricingRule(Long id, AdminConfigDtos.PricingRulePayload payload) {
        PersistenceEntities.PricingRuleEntity entity = id == null
                ? new PersistenceEntities.PricingRuleEntity()
                : platformRepository.findById(PersistenceEntities.PricingRuleEntity.class, id).orElse(new PersistenceEntities.PricingRuleEntity());
        if (entity.id == null) {
            entity.id = nextId("PricingRuleEntity", 0L);
        }
        entity.categoryId = payload.categoryId();
        entity.labelText = payload.label();
        entity.basePrice = payload.basePrice();
        entity.coefficient = payload.coefficient();
        entity.guidePrice = payload.guidePrice();
        entity.enabled = enabled(payload.enabled());
        return toPricingRule(platformRepository.saveEntity(entity));
    }

    public void deletePricingRule(Long id) {
        platformRepository.deleteEntity(PersistenceEntities.PricingRuleEntity.class, id);
    }

    public List<AdminConfigDtos.DispatchZonePayload> listDispatchZones() {
        return platformRepository.listAll("DispatchZoneEntity", "e.sortOrder asc, e.id asc", PersistenceEntities.DispatchZoneEntity.class)
                .stream()
                .map(this::toDispatchZone)
                .toList();
    }

    public AdminConfigDtos.DispatchZonePayload getDispatchZone(Long id) {
        return toDispatchZone(platformRepository.findById(PersistenceEntities.DispatchZoneEntity.class, id).orElseThrow());
    }

    public AdminConfigDtos.DispatchZonePayload saveDispatchZone(Long id, AdminConfigDtos.DispatchZonePayload payload) {
        PersistenceEntities.DispatchZoneEntity entity = id == null
                ? new PersistenceEntities.DispatchZoneEntity()
                : platformRepository.findById(PersistenceEntities.DispatchZoneEntity.class, id).orElse(new PersistenceEntities.DispatchZoneEntity());
        if (entity.id == null) {
            entity.id = nextId("DispatchZoneEntity", 0L);
        }
        entity.cityName = payload.cityName();
        entity.districtName = payload.districtName();
        entity.sortOrder = payload.sortOrder() == null ? 0 : payload.sortOrder();
        entity.enabled = enabled(payload.enabled());
        return toDispatchZone(platformRepository.saveEntity(entity));
    }

    public void deleteDispatchZone(Long id) {
        platformRepository.deleteEntity(PersistenceEntities.DispatchZoneEntity.class, id);
    }

    private long nextId(String entityName, long initialValue) {
        return platformRepository.nextLongId(entityName, "id", initialValue);
    }

    private boolean enabled(Boolean value) {
        return value == null || value;
    }

    private AdminConfigDtos.BannerPayload toBanner(PersistenceEntities.BannerEntity entity) {
        return new AdminConfigDtos.BannerPayload(
                entity.id,
                entity.title,
                entity.subtitle,
                entity.image,
                entity.link,
                entity.sortOrder,
                enabled(entity.enabled)
        );
    }

    private AdminConfigDtos.NoticePayload toNotice(PersistenceEntities.NoticeEntity entity) {
        return new AdminConfigDtos.NoticePayload(
                entity.id,
                entity.title,
                entity.contentText,
                entity.levelCode,
                enabled(entity.enabled)
        );
    }

    private AdminConfigDtos.AgreementPayload toAgreement(PersistenceEntities.AgreementEntity entity) {
        return new AdminConfigDtos.AgreementPayload(
                entity.id,
                entity.title,
                entity.version,
                entity.contentText,
                enabled(entity.enabled)
        );
    }

    private AdminConfigDtos.CouponPayload toCoupon(PersistenceEntities.CouponEntity entity) {
        return new AdminConfigDtos.CouponPayload(
                entity.id,
                entity.title,
                entity.amount,
                entity.thresholdText,
                entity.expireAt,
                enabled(entity.enabled)
        );
    }

    private AdminConfigDtos.MemberLevelPayload toMemberLevel(PersistenceEntities.MemberLevelEntity entity) {
        return new AdminConfigDtos.MemberLevelPayload(
                entity.id,
                entity.name,
                entity.benefitText,
                entity.pointsRequired,
                enabled(entity.enabled)
        );
    }

    private AdminConfigDtos.RolePayload toRole(PersistenceEntities.RoleEntity entity) {
        return new AdminConfigDtos.RolePayload(
                entity.id,
                entity.code,
                entity.name,
                entity.descriptionText,
                enabled(entity.enabled)
        );
    }

    private AdminConfigDtos.MenuPayload toMenu(PersistenceEntities.MenuEntity entity) {
        return new AdminConfigDtos.MenuPayload(
                entity.id,
                entity.name,
                entity.path,
                entity.icon,
                entity.sortOrder,
                enabled(entity.enabled)
        );
    }

    private AdminConfigDtos.PermissionPayload toPermission(PersistenceEntities.PermissionEntity entity) {
        return new AdminConfigDtos.PermissionPayload(
                entity.id,
                entity.code,
                entity.name,
                entity.descriptionText,
                enabled(entity.enabled)
        );
    }

    private AdminConfigDtos.ServiceCategoryPayload toCategory(PersistenceEntities.ServiceCategoryEntity entity) {
        return new AdminConfigDtos.ServiceCategoryPayload(
                entity.id,
                entity.name,
                entity.icon,
                entity.sortOrder,
                enabled(entity.enabled)
        );
    }

    private AdminConfigDtos.ServiceItemPayload toServiceItem(PersistenceEntities.ServiceItemEntity entity) {
        return new AdminConfigDtos.ServiceItemPayload(
                entity.id,
                entity.categoryId,
                entity.name,
                entity.subtitle,
                entity.basePrice,
                entity.doorPrice,
                entity.guidePrice,
                entity.warrantyText,
                entity.guaranteesText,
                entity.tagsText,
                entity.imageUrls,
                entity.processSteps,
                enabled(entity.enabled)
        );
    }

    private AdminConfigDtos.ProductPayload toProduct(PersistenceEntities.ProductEntity entity) {
        return new AdminConfigDtos.ProductPayload(
                entity.id,
                entity.name,
                entity.descriptionText,
                entity.price,
                entity.createInstallOrder,
                entity.imageUrl,
                enabled(entity.enabled)
        );
    }

    private AdminConfigDtos.SkuPayload toSku(PersistenceEntities.SkuEntity entity) {
        return new AdminConfigDtos.SkuPayload(
                entity.id,
                entity.productId,
                entity.name,
                entity.price,
                entity.stock,
                enabled(entity.enabled)
        );
    }

    private AdminConfigDtos.PricingRulePayload toPricingRule(PersistenceEntities.PricingRuleEntity entity) {
        return new AdminConfigDtos.PricingRulePayload(
                entity.id,
                entity.categoryId,
                entity.labelText,
                entity.basePrice,
                entity.coefficient,
                entity.guidePrice,
                enabled(entity.enabled)
        );
    }

    private AdminConfigDtos.DispatchZonePayload toDispatchZone(PersistenceEntities.DispatchZoneEntity entity) {
        return new AdminConfigDtos.DispatchZonePayload(
                entity.id,
                entity.cityName,
                entity.districtName,
                entity.sortOrder,
                enabled(entity.enabled)
        );
    }
}
