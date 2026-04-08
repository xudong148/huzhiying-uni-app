package com.huzhiying.server.service;

import com.huzhiying.server.dto.AdminConfigDtos;
import com.huzhiying.server.exception.ConfigConflictException;
import com.huzhiying.server.exception.ConfigNotFoundException;
import com.huzhiying.server.exception.ConfigValidationException;
import com.huzhiying.server.persistence.PersistenceEntities;
import com.huzhiying.server.repository.PlatformRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class AdminConfigService {

    private final PlatformRepository platformRepository;

    public AdminConfigService(PlatformRepository platformRepository) {
        this.platformRepository = platformRepository;
    }

    private long nextId(String entityName, long initialValue) {
        return platformRepository.nextLongId(entityName, "id", initialValue);
    }

    private boolean enabled(Boolean value) {
        return value == null || value;
    }

    private boolean booleanValue(Boolean value, boolean defaultValue) {
        return value == null ? defaultValue : value;
    }

    private <T> T requireEntity(Class<T> entityClass, Long id, String label) {
        return platformRepository.findById(entityClass, id)
                .orElseThrow(() -> new ConfigNotFoundException(label + "不存在"));
    }

    private Long requireConfigId(Class<?> entityClass, Long id, String label) {
        if (id == null) {
            throw new ConfigValidationException(label + "不能为空");
        }
        platformRepository.findById(entityClass, id)
                .orElseThrow(() -> new ConfigValidationException(label + "不存在"));
        return id;
    }

    private void ensureUnique(String entityName, Map<String, ?> filters, Long excludeId, String message) {
        if (platformRepository.existsByFields(entityName, filters, excludeId)) {
            throw new ConfigConflictException(message);
        }
    }

    private String trimRequired(String value, String label) {
        if (value == null || value.trim().isEmpty()) {
            throw new ConfigValidationException(label + "不能为空");
        }
        return value.trim();
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private BigDecimal requireNonNegative(BigDecimal value, String label) {
        if (value == null) {
            throw new ConfigValidationException(label + "不能为空");
        }
        if (value.signum() < 0) {
            throw new ConfigValidationException(label + "不能小于 0");
        }
        return value;
    }

    private Integer requireNonNegative(Integer value, String label) {
        if (value == null) {
            throw new ConfigValidationException(label + "不能为空");
        }
        if (value < 0) {
            throw new ConfigValidationException(label + "不能小于 0");
        }
        return value;
    }

    private MerchandisePrice normalizeMerchandisePrice(BigDecimal price, BigDecimal tagPrice, BigDecimal discountPrice, String label) {
        BigDecimal effectivePrice = requireNonNegative(price, label + "价格");
        BigDecimal normalizedTagPrice = tagPrice == null ? effectivePrice : requireNonNegative(tagPrice, label + "标签价");
        BigDecimal normalizedDiscountPrice = discountPrice == null ? effectivePrice : requireNonNegative(discountPrice, label + "折扣价");
        if (normalizedDiscountPrice.compareTo(normalizedTagPrice) > 0) {
            throw new ConfigValidationException(label + "折扣价不能大于标签价");
        }
        if (effectivePrice.compareTo(normalizedDiscountPrice) > 0) {
            throw new ConfigValidationException(label + "价格不能大于折扣价");
        }
        return new MerchandisePrice(effectivePrice, normalizedTagPrice, normalizedDiscountPrice);
    }

    private String normalizeCouponDate(String value) {
        String dateText = trimRequired(value, "优惠券过期时间");
        try {
            return LocalDate.parse(dateText).toString();
        } catch (DateTimeParseException exception) {
            throw new ConfigValidationException("优惠券过期时间必须是 yyyy-MM-dd");
        }
    }

    private String normalizeNoticeLevel(String value) {
        String normalized = trimRequired(value, "公告级别");
        if (!List.of("warning", "promo", "system").contains(normalized)) {
            throw new ConfigValidationException("公告级别仅支持 warning、promo、system");
        }
        return normalized;
    }

    public List<AdminConfigDtos.BannerPayload> listBanners() {
        return platformRepository.listAll("BannerEntity", "e.sortOrder asc, e.id asc", PersistenceEntities.BannerEntity.class)
                .stream()
                .map(this::toBanner)
                .toList();
    }

    public AdminConfigDtos.BannerPayload getBanner(Long id) {
        return toBanner(requireEntity(PersistenceEntities.BannerEntity.class, id, "Banner"));
    }

    public AdminConfigDtos.BannerPayload saveBanner(Long id, AdminConfigDtos.BannerPayload payload) {
        PersistenceEntities.BannerEntity entity = id == null ? new PersistenceEntities.BannerEntity()
                : requireEntity(PersistenceEntities.BannerEntity.class, id, "Banner");
        if (entity.id == null) {
            entity.id = nextId("BannerEntity", 0L);
        }
        entity.title = trimRequired(payload.title(), "Banner 标题");
        entity.subtitle = trimToNull(payload.subtitle());
        entity.image = trimRequired(payload.image(), "Banner 图片");
        entity.link = trimRequired(payload.link(), "Banner 跳转链接");
        entity.sortOrder = requireNonNegative(payload.sortOrder(), "Banner 排序");
        entity.enabled = enabled(payload.enabled());
        return toBanner(platformRepository.saveEntity(entity));
    }

    public void deleteBanner(Long id) {
        requireEntity(PersistenceEntities.BannerEntity.class, id, "Banner");
        platformRepository.deleteEntity(PersistenceEntities.BannerEntity.class, id);
    }

    public List<AdminConfigDtos.NoticePayload> listNotices() {
        return platformRepository.listAll("NoticeEntity", "e.id asc", PersistenceEntities.NoticeEntity.class)
                .stream()
                .map(this::toNotice)
                .toList();
    }

    public AdminConfigDtos.NoticePayload getNotice(Long id) {
        return toNotice(requireEntity(PersistenceEntities.NoticeEntity.class, id, "公告"));
    }

    public AdminConfigDtos.NoticePayload saveNotice(Long id, AdminConfigDtos.NoticePayload payload) {
        PersistenceEntities.NoticeEntity entity = id == null ? new PersistenceEntities.NoticeEntity()
                : requireEntity(PersistenceEntities.NoticeEntity.class, id, "公告");
        if (entity.id == null) {
            entity.id = nextId("NoticeEntity", 0L);
        }
        entity.title = trimRequired(payload.title(), "公告标题");
        entity.contentText = trimRequired(payload.content(), "公告内容");
        entity.levelCode = normalizeNoticeLevel(payload.levelCode());
        entity.enabled = enabled(payload.enabled());
        return toNotice(platformRepository.saveEntity(entity));
    }

    public void deleteNotice(Long id) {
        requireEntity(PersistenceEntities.NoticeEntity.class, id, "公告");
        platformRepository.deleteEntity(PersistenceEntities.NoticeEntity.class, id);
    }

    public List<AdminConfigDtos.AgreementPayload> listAgreements() {
        return platformRepository.listAll("AgreementEntity", "e.id asc", PersistenceEntities.AgreementEntity.class)
                .stream()
                .map(this::toAgreement)
                .toList();
    }

    public AdminConfigDtos.AgreementPayload getAgreement(Long id) {
        return toAgreement(requireEntity(PersistenceEntities.AgreementEntity.class, id, "协议"));
    }

    public AdminConfigDtos.AgreementPayload saveAgreement(Long id, AdminConfigDtos.AgreementPayload payload) {
        PersistenceEntities.AgreementEntity entity = id == null ? new PersistenceEntities.AgreementEntity()
                : requireEntity(PersistenceEntities.AgreementEntity.class, id, "协议");
        if (entity.id == null) {
            entity.id = nextId("AgreementEntity", 0L);
        }
        String title = trimRequired(payload.title(), "协议标题");
        String version = trimRequired(payload.version(), "协议版本");
        ensureUnique("AgreementEntity", Map.of("title", title, "version", version), id, "同标题同版本的协议已存在");
        entity.title = title;
        entity.version = version;
        entity.contentText = trimRequired(payload.content(), "协议正文");
        entity.enabled = enabled(payload.enabled());
        return toAgreement(platformRepository.saveEntity(entity));
    }

    public void deleteAgreement(Long id) {
        requireEntity(PersistenceEntities.AgreementEntity.class, id, "协议");
        platformRepository.deleteEntity(PersistenceEntities.AgreementEntity.class, id);
    }

    public List<AdminConfigDtos.CouponPayload> listCoupons() {
        return platformRepository.listAll("CouponEntity", "e.id asc", PersistenceEntities.CouponEntity.class)
                .stream()
                .map(this::toCoupon)
                .toList();
    }

    public AdminConfigDtos.CouponPayload getCoupon(Long id) {
        return toCoupon(requireEntity(PersistenceEntities.CouponEntity.class, id, "优惠券"));
    }

    public AdminConfigDtos.CouponPayload saveCoupon(Long id, AdminConfigDtos.CouponPayload payload) {
        PersistenceEntities.CouponEntity entity = id == null ? new PersistenceEntities.CouponEntity()
                : requireEntity(PersistenceEntities.CouponEntity.class, id, "优惠券");
        if (entity.id == null) {
            entity.id = nextId("CouponEntity", 0L);
        }
        entity.title = trimRequired(payload.title(), "优惠券标题");
        entity.amount = requireNonNegative(payload.amount(), "优惠券面额");
        entity.thresholdText = trimRequired(payload.thresholdText(), "优惠券门槛说明");
        entity.expireAt = normalizeCouponDate(payload.expireAt());
        entity.enabled = enabled(payload.enabled());
        return toCoupon(platformRepository.saveEntity(entity));
    }

    public void deleteCoupon(Long id) {
        requireEntity(PersistenceEntities.CouponEntity.class, id, "优惠券");
        platformRepository.deleteEntity(PersistenceEntities.CouponEntity.class, id);
    }

    public List<AdminConfigDtos.MemberLevelPayload> listMemberLevels() {
        return platformRepository.listAll("MemberLevelEntity", "e.pointsRequired asc, e.id asc", PersistenceEntities.MemberLevelEntity.class)
                .stream()
                .map(this::toMemberLevel)
                .toList();
    }

    public AdminConfigDtos.MemberLevelPayload getMemberLevel(Long id) {
        return toMemberLevel(requireEntity(PersistenceEntities.MemberLevelEntity.class, id, "会员等级"));
    }

    public AdminConfigDtos.MemberLevelPayload saveMemberLevel(Long id, AdminConfigDtos.MemberLevelPayload payload) {
        PersistenceEntities.MemberLevelEntity entity = id == null ? new PersistenceEntities.MemberLevelEntity()
                : requireEntity(PersistenceEntities.MemberLevelEntity.class, id, "会员等级");
        if (entity.id == null) {
            entity.id = nextId("MemberLevelEntity", 0L);
        }
        Integer pointsRequired = requireNonNegative(payload.pointsRequired(), "会员积分门槛");
        ensureUnique("MemberLevelEntity", Map.of("pointsRequired", pointsRequired), id, "相同积分门槛的会员等级已存在");
        entity.name = trimRequired(payload.name(), "会员等级名称");
        entity.benefitText = trimRequired(payload.benefitText(), "会员权益说明");
        entity.pointsRequired = pointsRequired;
        entity.enabled = enabled(payload.enabled());
        return toMemberLevel(platformRepository.saveEntity(entity));
    }

    public void deleteMemberLevel(Long id) {
        requireEntity(PersistenceEntities.MemberLevelEntity.class, id, "会员等级");
        platformRepository.deleteEntity(PersistenceEntities.MemberLevelEntity.class, id);
    }

    public List<AdminConfigDtos.RolePayload> listRoles() {
        return platformRepository.listAll("RoleEntity", "e.id asc", PersistenceEntities.RoleEntity.class)
                .stream()
                .map(this::toRole)
                .toList();
    }

    public AdminConfigDtos.RolePayload getRole(Long id) {
        return toRole(requireEntity(PersistenceEntities.RoleEntity.class, id, "角色"));
    }

    public AdminConfigDtos.RolePayload saveRole(Long id, AdminConfigDtos.RolePayload payload) {
        PersistenceEntities.RoleEntity entity = id == null ? new PersistenceEntities.RoleEntity()
                : requireEntity(PersistenceEntities.RoleEntity.class, id, "角色");
        if (entity.id == null) {
            entity.id = nextId("RoleEntity", 0L);
        }
        String code = trimRequired(payload.code(), "角色编码");
        ensureUnique("RoleEntity", Map.of("code", code), id, "角色编码已存在");
        entity.code = code;
        entity.name = trimRequired(payload.name(), "角色名称");
        entity.descriptionText = trimToNull(payload.description());
        entity.enabled = enabled(payload.enabled());
        return toRole(platformRepository.saveEntity(entity));
    }

    public void deleteRole(Long id) {
        requireEntity(PersistenceEntities.RoleEntity.class, id, "角色");
        if (!platformRepository.listUserRolesByRoleId(id).isEmpty()) {
            throw new ConfigConflictException("角色已绑定后台账号，不能删除");
        }
        platformRepository.deleteEntity(PersistenceEntities.RoleEntity.class, id);
    }

    public AdminConfigDtos.RoleGrantPayload getRoleGrant(Long roleId) {
        requireEntity(PersistenceEntities.RoleEntity.class, roleId, "角色");
        return new AdminConfigDtos.RoleGrantPayload(
                roleId,
                platformRepository.listRoleMenuBindings(roleId).stream().map(binding -> binding.menuId).toList(),
                platformRepository.listRolePermissionBindings(roleId).stream().map(binding -> binding.permissionId).toList()
        );
    }

    public AdminConfigDtos.RoleGrantPayload saveRoleGrant(Long roleId, AdminConfigDtos.RoleGrantPayload payload) {
        requireEntity(PersistenceEntities.RoleEntity.class, roleId, "角色");
        List<Long> menuIds = payload.menuIds() == null ? List.of() : payload.menuIds().stream().distinct().toList();
        List<Long> permissionIds = payload.permissionIds() == null ? List.of() : payload.permissionIds().stream().distinct().toList();
        menuIds.forEach(menuId -> requireConfigId(PersistenceEntities.MenuEntity.class, menuId, "菜单"));
        permissionIds.forEach(permissionId -> requireConfigId(PersistenceEntities.PermissionEntity.class, permissionId, "权限点"));

        platformRepository.deleteRoleMenuBindings(roleId);
        platformRepository.deleteRolePermissionBindings(roleId);
        menuIds.forEach(menuId -> {
            PersistenceEntities.RoleMenuBindingEntity binding = new PersistenceEntities.RoleMenuBindingEntity();
            binding.roleId = roleId;
            binding.menuId = menuId;
            platformRepository.saveRoleMenuBinding(binding);
        });
        permissionIds.forEach(permissionId -> {
            PersistenceEntities.RolePermissionBindingEntity binding = new PersistenceEntities.RolePermissionBindingEntity();
            binding.roleId = roleId;
            binding.permissionId = permissionId;
            platformRepository.saveRolePermissionBinding(binding);
        });
        return getRoleGrant(roleId);
    }

    public List<AdminConfigDtos.MenuPayload> listMenus() {
        return platformRepository.listAll("MenuEntity", "e.sortOrder asc, e.id asc", PersistenceEntities.MenuEntity.class)
                .stream()
                .map(this::toMenu)
                .toList();
    }

    public AdminConfigDtos.MenuPayload getMenu(Long id) {
        return toMenu(requireEntity(PersistenceEntities.MenuEntity.class, id, "菜单"));
    }

    public AdminConfigDtos.MenuPayload saveMenu(Long id, AdminConfigDtos.MenuPayload payload) {
        PersistenceEntities.MenuEntity entity = id == null ? new PersistenceEntities.MenuEntity()
                : requireEntity(PersistenceEntities.MenuEntity.class, id, "菜单");
        if (entity.id == null) {
            entity.id = nextId("MenuEntity", 0L);
        }
        String path = trimRequired(payload.path(), "菜单路径");
        ensureUnique("MenuEntity", Map.of("path", path), id, "菜单路径已存在");
        entity.name = trimRequired(payload.name(), "菜单名称");
        entity.path = path;
        entity.icon = trimToNull(payload.icon());
        entity.sortOrder = requireNonNegative(payload.sortOrder(), "菜单排序");
        entity.enabled = enabled(payload.enabled());
        return toMenu(platformRepository.saveEntity(entity));
    }

    public void deleteMenu(Long id) {
        requireEntity(PersistenceEntities.MenuEntity.class, id, "菜单");
        if (!platformRepository.listMenuBindingsByMenuId(id).isEmpty()) {
            throw new ConfigConflictException("菜单已绑定角色，不能删除");
        }
        platformRepository.deleteEntity(PersistenceEntities.MenuEntity.class, id);
    }

    public List<AdminConfigDtos.PermissionPayload> listPermissions() {
        return platformRepository.listAll("PermissionEntity", "e.id asc", PersistenceEntities.PermissionEntity.class)
                .stream()
                .map(this::toPermission)
                .toList();
    }

    public AdminConfigDtos.PermissionPayload getPermission(Long id) {
        return toPermission(requireEntity(PersistenceEntities.PermissionEntity.class, id, "权限点"));
    }

    public AdminConfigDtos.PermissionPayload savePermission(Long id, AdminConfigDtos.PermissionPayload payload) {
        PersistenceEntities.PermissionEntity entity = id == null ? new PersistenceEntities.PermissionEntity()
                : requireEntity(PersistenceEntities.PermissionEntity.class, id, "权限点");
        if (entity.id == null) {
            entity.id = nextId("PermissionEntity", 0L);
        }
        String code = trimRequired(payload.code(), "权限编码");
        ensureUnique("PermissionEntity", Map.of("code", code), id, "权限编码已存在");
        entity.code = code;
        entity.name = trimRequired(payload.name(), "权限名称");
        entity.descriptionText = trimToNull(payload.description());
        entity.enabled = enabled(payload.enabled());
        return toPermission(platformRepository.saveEntity(entity));
    }

    public void deletePermission(Long id) {
        requireEntity(PersistenceEntities.PermissionEntity.class, id, "权限点");
        if (!platformRepository.listPermissionBindingsByPermissionId(id).isEmpty()) {
            throw new ConfigConflictException("权限点已绑定角色，不能删除");
        }
        platformRepository.deleteEntity(PersistenceEntities.PermissionEntity.class, id);
    }

    public List<AdminConfigDtos.ServiceCategoryPayload> listCategories() {
        return platformRepository.listAll("ServiceCategoryEntity", "e.sortOrder asc, e.id asc", PersistenceEntities.ServiceCategoryEntity.class)
                .stream()
                .map(this::toCategory)
                .toList();
    }

    public AdminConfigDtos.ServiceCategoryPayload getCategory(Long id) {
        return toCategory(requireEntity(PersistenceEntities.ServiceCategoryEntity.class, id, "服务类目"));
    }

    public AdminConfigDtos.ServiceCategoryPayload saveCategory(Long id, AdminConfigDtos.ServiceCategoryPayload payload) {
        PersistenceEntities.ServiceCategoryEntity entity = id == null ? new PersistenceEntities.ServiceCategoryEntity()
                : requireEntity(PersistenceEntities.ServiceCategoryEntity.class, id, "服务类目");
        if (entity.id == null) {
            entity.id = nextId("ServiceCategoryEntity", 0L);
        }
        String name = trimRequired(payload.name(), "服务类目名称");
        ensureUnique("ServiceCategoryEntity", Map.of("name", name), id, "服务类目名称已存在");
        entity.name = name;
        entity.icon = trimToNull(payload.icon());
        entity.sortOrder = requireNonNegative(payload.sortOrder(), "服务类目排序");
        entity.enabled = enabled(payload.enabled());
        return toCategory(platformRepository.saveEntity(entity));
    }

    public void deleteCategory(Long id) {
        requireEntity(PersistenceEntities.ServiceCategoryEntity.class, id, "服务类目");
        if (platformRepository.countByField("ServiceItemEntity", "categoryId", id) > 0) {
            throw new ConfigConflictException("服务类目仍被服务项引用，不能删除");
        }
        if (platformRepository.countByField("PricingRuleEntity", "categoryId", id) > 0) {
            throw new ConfigConflictException("服务类目仍被定价规则引用，不能删除");
        }
        platformRepository.deleteEntity(PersistenceEntities.ServiceCategoryEntity.class, id);
    }

    public List<AdminConfigDtos.ServiceItemPayload> listServiceItems() {
        return platformRepository.listAll("ServiceItemEntity", "e.categoryId asc, e.id asc", PersistenceEntities.ServiceItemEntity.class)
                .stream()
                .map(this::toServiceItem)
                .toList();
    }

    public AdminConfigDtos.ServiceItemPayload getServiceItem(Long id) {
        return toServiceItem(requireEntity(PersistenceEntities.ServiceItemEntity.class, id, "服务项"));
    }

    public AdminConfigDtos.ServiceItemPayload saveServiceItem(Long id, AdminConfigDtos.ServiceItemPayload payload) {
        PersistenceEntities.ServiceItemEntity entity = id == null ? new PersistenceEntities.ServiceItemEntity()
                : requireEntity(PersistenceEntities.ServiceItemEntity.class, id, "服务项");
        if (entity.id == null) {
            entity.id = nextId("ServiceItemEntity", 200L);
        }
        entity.categoryId = requireConfigId(PersistenceEntities.ServiceCategoryEntity.class, payload.categoryId(), "服务类目");
        entity.name = trimRequired(payload.name(), "服务项名称");
        entity.subtitle = trimToNull(payload.subtitle());
        entity.basePrice = requireNonNegative(payload.basePrice(), "服务项基础价");
        entity.doorPrice = requireNonNegative(payload.doorPrice(), "服务项上门费");
        entity.guidePrice = trimToNull(payload.guidePrice());
        entity.warrantyText = trimToNull(payload.warrantyText());
        entity.guaranteesText = trimToNull(payload.guaranteesText());
        entity.tagsText = trimToNull(payload.tagsText());
        entity.imageUrls = trimToNull(payload.imageUrls());
        entity.processSteps = trimToNull(payload.processSteps());
        entity.enabled = enabled(payload.enabled());
        return toServiceItem(platformRepository.saveEntity(entity));
    }

    public void deleteServiceItem(Long id) {
        requireEntity(PersistenceEntities.ServiceItemEntity.class, id, "服务项");
        if (platformRepository.countByField("ServiceOrderEntity", "serviceItemId", id) > 0) {
            throw new ConfigConflictException("服务项已被服务订单引用，不能删除");
        }
        platformRepository.deleteEntity(PersistenceEntities.ServiceItemEntity.class, id);
    }

    public List<AdminConfigDtos.ProductPayload> listProducts() {
        return platformRepository.listAll("ProductEntity", "e.id asc", PersistenceEntities.ProductEntity.class)
                .stream()
                .map(this::toProduct)
                .toList();
    }

    public AdminConfigDtos.ProductPayload getProduct(Long id) {
        return toProduct(requireEntity(PersistenceEntities.ProductEntity.class, id, "商品"));
    }

    public AdminConfigDtos.ProductPayload saveProduct(Long id, AdminConfigDtos.ProductPayload payload) {
        PersistenceEntities.ProductEntity entity = id == null ? new PersistenceEntities.ProductEntity()
                : requireEntity(PersistenceEntities.ProductEntity.class, id, "商品");
        if (entity.id == null) {
            entity.id = nextId("ProductEntity", 1000L);
        }
        MerchandisePrice price = normalizeMerchandisePrice(payload.price(), payload.tagPrice(), payload.discountPrice(), "商品");
        entity.name = trimRequired(payload.name(), "商品名称");
        entity.descriptionText = trimToNull(payload.descriptionText());
        entity.price = price.effectivePrice();
        entity.tagPrice = price.tagPrice();
        entity.discountPrice = price.discountPrice();
        entity.createInstallOrder = booleanValue(payload.createInstallOrder(), false);
        entity.imageUrl = trimToNull(payload.imageUrl());
        entity.enabled = enabled(payload.enabled());
        return toProduct(platformRepository.saveEntity(entity));
    }

    public void deleteProduct(Long id) {
        requireEntity(PersistenceEntities.ProductEntity.class, id, "商品");
        if (platformRepository.countByField("SkuEntity", "productId", id) > 0) {
            throw new ConfigConflictException("商品仍被 SKU 引用，不能删除");
        }
        if (platformRepository.countByField("ProductOrderEntity", "productId", id) > 0) {
            throw new ConfigConflictException("商品已被商品订单引用，不能删除");
        }
        platformRepository.deleteEntity(PersistenceEntities.ProductEntity.class, id);
    }

    public List<AdminConfigDtos.SkuPayload> listSkus() {
        return platformRepository.listAll("SkuEntity", "e.productId asc, e.id asc", PersistenceEntities.SkuEntity.class)
                .stream()
                .map(this::toSku)
                .toList();
    }

    public AdminConfigDtos.SkuPayload getSku(Long id) {
        return toSku(requireEntity(PersistenceEntities.SkuEntity.class, id, "SKU"));
    }

    public AdminConfigDtos.SkuPayload saveSku(Long id, AdminConfigDtos.SkuPayload payload) {
        PersistenceEntities.SkuEntity entity = id == null ? new PersistenceEntities.SkuEntity()
                : requireEntity(PersistenceEntities.SkuEntity.class, id, "SKU");
        if (entity.id == null) {
            entity.id = nextId("SkuEntity", 0L);
        }
        MerchandisePrice price = normalizeMerchandisePrice(payload.price(), payload.tagPrice(), payload.discountPrice(), "SKU");
        entity.productId = requireConfigId(PersistenceEntities.ProductEntity.class, payload.productId(), "商品");
        entity.name = trimRequired(payload.name(), "SKU 名称");
        entity.price = price.effectivePrice();
        entity.tagPrice = price.tagPrice();
        entity.discountPrice = price.discountPrice();
        entity.stock = requireNonNegative(payload.stock(), "SKU 库存");
        entity.enabled = enabled(payload.enabled());
        return toSku(platformRepository.saveEntity(entity));
    }

    public void deleteSku(Long id) {
        requireEntity(PersistenceEntities.SkuEntity.class, id, "SKU");
        platformRepository.deleteEntity(PersistenceEntities.SkuEntity.class, id);
    }

    public List<AdminConfigDtos.PricingRulePayload> listPricingRules() {
        return platformRepository.listAll("PricingRuleEntity", "e.categoryId asc, e.id asc", PersistenceEntities.PricingRuleEntity.class)
                .stream()
                .map(this::toPricingRule)
                .toList();
    }

    public AdminConfigDtos.PricingRulePayload getPricingRule(Long id) {
        return toPricingRule(requireEntity(PersistenceEntities.PricingRuleEntity.class, id, "定价规则"));
    }

    public AdminConfigDtos.PricingRulePayload savePricingRule(Long id, AdminConfigDtos.PricingRulePayload payload) {
        PersistenceEntities.PricingRuleEntity entity = id == null ? new PersistenceEntities.PricingRuleEntity()
                : requireEntity(PersistenceEntities.PricingRuleEntity.class, id, "定价规则");
        if (entity.id == null) {
            entity.id = nextId("PricingRuleEntity", 0L);
        }
        Long categoryId = requireConfigId(PersistenceEntities.ServiceCategoryEntity.class, payload.categoryId(), "服务类目");
        String label = trimRequired(payload.label(), "定价规则名称");
        ensureUnique("PricingRuleEntity", Map.of("categoryId", categoryId, "labelText", label), id, "同类目下的定价规则名称已存在");
        entity.categoryId = categoryId;
        entity.labelText = label;
        entity.basePrice = requireNonNegative(payload.basePrice(), "定价规则基础价");
        entity.coefficient = trimToNull(payload.coefficient());
        entity.guidePrice = trimToNull(payload.guidePrice());
        entity.enabled = enabled(payload.enabled());
        return toPricingRule(platformRepository.saveEntity(entity));
    }

    public void deletePricingRule(Long id) {
        requireEntity(PersistenceEntities.PricingRuleEntity.class, id, "定价规则");
        platformRepository.deleteEntity(PersistenceEntities.PricingRuleEntity.class, id);
    }

    public List<AdminConfigDtos.DispatchZonePayload> listDispatchZones() {
        return platformRepository.listAll("DispatchZoneEntity", "e.sortOrder asc, e.id asc", PersistenceEntities.DispatchZoneEntity.class)
                .stream()
                .map(this::toDispatchZone)
                .toList();
    }

    public AdminConfigDtos.DispatchZonePayload getDispatchZone(Long id) {
        return toDispatchZone(requireEntity(PersistenceEntities.DispatchZoneEntity.class, id, "服务区域"));
    }

    public AdminConfigDtos.DispatchZonePayload saveDispatchZone(Long id, AdminConfigDtos.DispatchZonePayload payload) {
        PersistenceEntities.DispatchZoneEntity entity = id == null ? new PersistenceEntities.DispatchZoneEntity()
                : requireEntity(PersistenceEntities.DispatchZoneEntity.class, id, "服务区域");
        if (entity.id == null) {
            entity.id = nextId("DispatchZoneEntity", 0L);
        }
        String cityName = trimRequired(payload.cityName(), "服务区域城市名称");
        String districtName = trimRequired(payload.districtName(), "服务区域区县名称");
        boolean nextEnabled = enabled(payload.enabled());
        ensureUnique("DispatchZoneEntity", Map.of("cityName", cityName, "districtName", districtName), id, "同城市同区县的服务区域已存在");
        if (entity.id != null && enabled(entity.enabled) && !nextEnabled && platformRepository.countEnabledDispatchZonesExcluding(entity.id) == 0) {
            throw new ConfigConflictException("至少需要保留一个启用中的服务区域");
        }
        entity.cityName = cityName;
        entity.districtName = districtName;
        entity.sortOrder = requireNonNegative(payload.sortOrder(), "服务区域排序");
        entity.enabled = nextEnabled;
        return toDispatchZone(platformRepository.saveEntity(entity));
    }

    public void deleteDispatchZone(Long id) {
        PersistenceEntities.DispatchZoneEntity entity = requireEntity(PersistenceEntities.DispatchZoneEntity.class, id, "服务区域");
        if (enabled(entity.enabled) && platformRepository.countEnabledDispatchZonesExcluding(id) == 0) {
            throw new ConfigConflictException("至少需要保留一个启用中的服务区域");
        }
        platformRepository.deleteEntity(PersistenceEntities.DispatchZoneEntity.class, id);
    }

    private AdminConfigDtos.BannerPayload toBanner(PersistenceEntities.BannerEntity entity) {
        return new AdminConfigDtos.BannerPayload(entity.id, entity.title, entity.subtitle, entity.image, entity.link, entity.sortOrder, enabled(entity.enabled));
    }

    private AdminConfigDtos.NoticePayload toNotice(PersistenceEntities.NoticeEntity entity) {
        return new AdminConfigDtos.NoticePayload(entity.id, entity.title, entity.contentText, entity.levelCode, enabled(entity.enabled));
    }

    private AdminConfigDtos.AgreementPayload toAgreement(PersistenceEntities.AgreementEntity entity) {
        return new AdminConfigDtos.AgreementPayload(entity.id, entity.title, entity.version, entity.contentText, enabled(entity.enabled));
    }

    private AdminConfigDtos.CouponPayload toCoupon(PersistenceEntities.CouponEntity entity) {
        return new AdminConfigDtos.CouponPayload(entity.id, entity.title, entity.amount, entity.thresholdText, entity.expireAt, enabled(entity.enabled));
    }

    private AdminConfigDtos.MemberLevelPayload toMemberLevel(PersistenceEntities.MemberLevelEntity entity) {
        return new AdminConfigDtos.MemberLevelPayload(entity.id, entity.name, entity.benefitText, entity.pointsRequired, enabled(entity.enabled));
    }

    private AdminConfigDtos.RolePayload toRole(PersistenceEntities.RoleEntity entity) {
        return new AdminConfigDtos.RolePayload(entity.id, entity.code, entity.name, entity.descriptionText, enabled(entity.enabled));
    }

    private AdminConfigDtos.MenuPayload toMenu(PersistenceEntities.MenuEntity entity) {
        return new AdminConfigDtos.MenuPayload(entity.id, entity.name, entity.path, entity.icon, entity.sortOrder, enabled(entity.enabled));
    }

    private AdminConfigDtos.PermissionPayload toPermission(PersistenceEntities.PermissionEntity entity) {
        return new AdminConfigDtos.PermissionPayload(entity.id, entity.code, entity.name, entity.descriptionText, enabled(entity.enabled));
    }

    private AdminConfigDtos.ServiceCategoryPayload toCategory(PersistenceEntities.ServiceCategoryEntity entity) {
        return new AdminConfigDtos.ServiceCategoryPayload(entity.id, entity.name, entity.icon, entity.sortOrder, enabled(entity.enabled));
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
                entity.tagPrice,
                entity.discountPrice,
                booleanValue(entity.createInstallOrder, false),
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
                entity.tagPrice,
                entity.discountPrice,
                entity.stock,
                enabled(entity.enabled)
        );
    }

    private AdminConfigDtos.PricingRulePayload toPricingRule(PersistenceEntities.PricingRuleEntity entity) {
        return new AdminConfigDtos.PricingRulePayload(entity.id, entity.categoryId, entity.labelText, entity.basePrice, entity.coefficient, entity.guidePrice, enabled(entity.enabled));
    }

    private AdminConfigDtos.DispatchZonePayload toDispatchZone(PersistenceEntities.DispatchZoneEntity entity) {
        return new AdminConfigDtos.DispatchZonePayload(entity.id, entity.cityName, entity.districtName, entity.sortOrder, enabled(entity.enabled));
    }

    private record MerchandisePrice(BigDecimal effectivePrice, BigDecimal tagPrice, BigDecimal discountPrice) {}
}
