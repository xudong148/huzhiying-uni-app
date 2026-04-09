package com.huzhiying.server.persistence;

import com.huzhiying.domain.enums.DomainEnums.DispatchMode;
import com.huzhiying.domain.enums.DomainEnums.NotificationTaskStatus;
import com.huzhiying.domain.enums.DomainEnums.PaymentStatus;
import com.huzhiying.domain.enums.DomainEnums.PaymentRecordStatus;
import com.huzhiying.domain.enums.DomainEnums.ProductOrderStatus;
import com.huzhiying.domain.enums.DomainEnums.QuotationStatus;
import com.huzhiying.domain.enums.DomainEnums.RefundRequestStatus;
import com.huzhiying.domain.enums.DomainEnums.RoleCode;
import com.huzhiying.domain.enums.DomainEnums.SettlementBillStatus;
import com.huzhiying.domain.enums.DomainEnums.ServiceOrderStatus;
import com.huzhiying.domain.enums.DomainEnums.WalletLedgerDirection;
import com.huzhiying.domain.enums.DomainEnums.WalletLedgerStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class PersistenceEntities {

    private PersistenceEntities() {}

    @Entity(name = "UserEntity")
    @Table(name = "users")
    public static class UserEntity {
        @Id
        public Long id;
        public String username;
        @Column(name = "password_hash")
        public String passwordHash;
        public String nickname;
        public String mobile;
        @Enumerated(EnumType.STRING)
        @Column(name = "role_code")
        public RoleCode roleCode;
        public String avatar;
        @Column(name = "level_name")
        public String levelName;
        public Boolean enabled;

        public UserEntity() {}
    }

    @Entity(name = "AddressEntity")
    @Table(name = "addresses")
    public static class AddressEntity {
        @Id
        public Long id;
        @Column(name = "user_id")
        public Long userId;
        @Column(name = "tag_name")
        public String tagName;
        @Column(name = "contact_name")
        public String contactName;
        public String mobile;
        @Column(name = "detail_address")
        public String detailAddress;
        @Column(name = "city_name")
        public String cityName;
        @Column(name = "district_name")
        public String districtName;
        public Double latitude;
        public Double longitude;
        @Column(name = "is_default")
        public Boolean isDefault;

        public AddressEntity() {}
    }

    @Entity(name = "MasterProfileEntity")
    @Table(name = "master_profiles")
    public static class MasterProfileEntity {
        @Id
        public Long id;
        @Column(name = "user_id")
        public Long userId;
        @Column(name = "real_name")
        public String realName;
        @Column(name = "skill_tags")
        public String skillTags;
        @Column(name = "service_area")
        public String serviceArea;
        public BigDecimal deposit;
        @Column(name = "credit_score")
        public Integer creditScore;
        public Boolean online;
        public Boolean listening;
        @Column(name = "max_distance_km")
        public Integer maxDistanceKm;
        @Column(name = "privacy_number")
        public Boolean privacyNumber;
        public Boolean enabled;

        public MasterProfileEntity() {}
    }

    @Entity(name = "ServiceCategoryEntity")
    @Table(name = "service_categories")
    public static class ServiceCategoryEntity {
        @Id
        public Long id;
        public String name;
        public String icon;
        @Column(name = "sort_order")
        public Integer sortOrder;
        public Boolean enabled;

        public ServiceCategoryEntity() {}
    }

    @Entity(name = "ServiceItemEntity")
    @Table(name = "service_items")
    public static class ServiceItemEntity {
        @Id
        public Long id;
        @Column(name = "category_id")
        public Long categoryId;
        public String name;
        public String subtitle;
        @Column(name = "base_price")
        public BigDecimal basePrice;
        @Column(name = "door_price")
        public BigDecimal doorPrice;
        @Column(name = "guide_price")
        public String guidePrice;
        @Column(name = "warranty_text")
        public String warrantyText;
        @Lob
        @Column(name = "guarantees_text")
        public String guaranteesText;
        @Lob
        @Column(name = "tags_text")
        public String tagsText;
        @Lob
        @Column(name = "image_urls")
        public String imageUrls;
        @Lob
        @Column(name = "process_steps")
        public String processSteps;
        public Boolean enabled;

        public ServiceItemEntity() {}
    }

    @Entity(name = "ProductEntity")
    @Table(name = "products")
    public static class ProductEntity {
        @Id
        public Long id;
        public String name;
        @Column(name = "description_text")
        public String descriptionText;
        public BigDecimal price;
        @Column(name = "tag_price")
        public BigDecimal tagPrice;
        @Column(name = "discount_price")
        public BigDecimal discountPrice;
        @Column(name = "create_install_order")
        public Boolean createInstallOrder;
        @Column(name = "image_url")
        public String imageUrl;
        public Boolean enabled;

        public ProductEntity() {}
    }

    @Entity(name = "SkuEntity")
    @Table(name = "skus")
    public static class SkuEntity {
        @Id
        public Long id;
        @Column(name = "product_id")
        public Long productId;
        public String name;
        public BigDecimal price;
        @Column(name = "tag_price")
        public BigDecimal tagPrice;
        @Column(name = "discount_price")
        public BigDecimal discountPrice;
        public Integer stock;
        public Boolean enabled;

        public SkuEntity() {}
    }

    @Entity(name = "ServiceOrderEntity")
    @Table(name = "service_orders")
    public static class ServiceOrderEntity {
        @Id
        public String id;
        @Column(name = "service_item_id")
        public Long serviceItemId;
        public String title;
        @Enumerated(EnumType.STRING)
        public ServiceOrderStatus status;
        @Enumerated(EnumType.STRING)
        @Column(name = "payment_status")
        public PaymentStatus paymentStatus;
        @Column(name = "user_id")
        public Long userId;
        @Column(name = "address_id")
        public Long addressId;
        @Column(name = "master_user_id")
        public Long masterUserId;
        public String appointment;
        public BigDecimal amount;
        @Enumerated(EnumType.STRING)
        @Column(name = "dispatch_mode")
        public DispatchMode dispatchMode;
        @Column(name = "eta_text")
        public String etaText;
        @Lob
        @Column(name = "description_text")
        public String descriptionText;
        public Boolean emergency;
        @Column(name = "night_service")
        public Boolean nightService;
        @Column(name = "created_at")
        public LocalDateTime createdAt;
        @Column(name = "updated_at")
        public LocalDateTime updatedAt;

        public ServiceOrderEntity() {}
    }

    @Entity(name = "ProductOrderEntity")
    @Table(name = "product_orders")
    public static class ProductOrderEntity {
        @Id
        public String id;
        @Column(name = "product_id")
        public Long productId;
        public String title;
        @Enumerated(EnumType.STRING)
        public ProductOrderStatus status;
        @Enumerated(EnumType.STRING)
        @Column(name = "payment_status")
        public PaymentStatus paymentStatus;
        @Column(name = "user_id")
        public Long userId;
        @Column(name = "address_id")
        public Long addressId;
        public BigDecimal amount;
        @Column(name = "create_install_order")
        public Boolean createInstallOrder;
        @Column(name = "install_service_order_id")
        public String installServiceOrderId;
        @Column(name = "created_at")
        public LocalDateTime createdAt;

        public ProductOrderEntity() {}
    }

    @Entity(name = "DispatchTaskEntity")
    @Table(name = "dispatch_tasks")
    public static class DispatchTaskEntity {
        @Id
        public String id;
        @Column(name = "order_id")
        public String orderId;
        public String title;
        public BigDecimal income;
        @Column(name = "distance_text")
        public String distanceText;
        @Column(name = "area_text")
        public String areaText;
        @Enumerated(EnumType.STRING)
        @Column(name = "dispatch_mode")
        public DispatchMode dispatchMode;
        @Column(name = "current_master_user_id")
        public Long currentMasterUserId;
        @Column(name = "task_status")
        public String taskStatus;
        @Lob
        @Column(name = "tags_text")
        public String tagsText;
        @Column(name = "created_at")
        public LocalDateTime createdAt;

        public DispatchTaskEntity() {}
    }

    @Entity(name = "WorkStepRecordEntity")
    @Table(name = "work_step_records")
    public static class WorkStepRecordEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        public Long id;
        @Column(name = "order_id")
        public String orderId;
        @Column(name = "step_key")
        public String stepKey;
        @Column(name = "label_text")
        public String labelText;
        @Column(name = "description_text")
        public String descriptionText;
        public Boolean done;
        @Column(name = "step_time")
        public LocalDateTime stepTime;

        public WorkStepRecordEntity() {}
    }

    @Entity(name = "QuotationEntity")
    @Table(name = "quotations")
    public static class QuotationEntity {
        @Id
        public String id;
        @Column(name = "order_id")
        public String orderId;
        @Column(name = "total_amount")
        public BigDecimal totalAmount;
        @Enumerated(EnumType.STRING)
        public QuotationStatus status;
        @Column(name = "remark_text")
        public String remarkText;
        @Column(name = "created_at")
        public LocalDateTime createdAt;

        public QuotationEntity() {}
    }

    @Entity(name = "QuotationItemEntity")
    @Table(name = "quotation_items")
    public static class QuotationItemEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        public Long id;
        @Column(name = "quotation_id")
        public String quotationId;
        public String name;
        public BigDecimal amount;

        public QuotationItemEntity() {}
    }

    @Entity(name = "WalletAccountEntity")
    @Table(name = "wallet_accounts")
    public static class WalletAccountEntity {
        @Id
        public Long id;
        @Column(name = "master_user_id")
        public Long masterUserId;
        @Column(name = "available_amount")
        public BigDecimal availableAmount;
        @Column(name = "frozen_amount")
        public BigDecimal frozenAmount;
        @Column(name = "today_income")
        public BigDecimal todayIncome;

        public WalletAccountEntity() {}
    }

    @Entity(name = "WalletTransactionEntity")
    @Table(name = "wallet_transactions")
    public static class WalletTransactionEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        public Long id;
        @Column(name = "wallet_account_id")
        public Long walletAccountId;
        public String title;
        public BigDecimal amount;
        @Column(name = "transaction_time")
        public String transactionTime;
        @Column(name = "status_text")
        public String statusText;

        public WalletTransactionEntity() {}
    }

    @Entity(name = "PaymentRecordEntity")
    @Table(name = "payment_records")
    public static class PaymentRecordEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        public Long id;
        @Column(name = "biz_no")
        public String bizNo;
        @Column(name = "order_id")
        public String orderId;
        @Column(name = "order_type")
        public String orderType;
        @Enumerated(EnumType.STRING)
        public PaymentRecordStatus status;
        public String channel;
        public BigDecimal amount;
        @Column(name = "user_id")
        public Long userId;
        @Column(name = "master_id")
        public Long masterId;
        @Column(name = "operator_id")
        public Long operatorId;
        @Column(name = "trace_id")
        public String traceId;
        @Column(name = "external_transaction_no")
        public String externalTransactionNo;
        @Column(name = "payment_stage")
        public String paymentStage;
        @Column(name = "remark_text")
        public String remarkText;
        @Column(name = "created_at")
        public LocalDateTime createdAt;
        @Column(name = "updated_at")
        public LocalDateTime updatedAt;

        public PaymentRecordEntity() {}
    }

    @Entity(name = "RefundRequestEntity")
    @Table(name = "refund_requests")
    public static class RefundRequestEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        public Long id;
        @Column(name = "biz_no")
        public String bizNo;
        @Column(name = "order_id")
        public String orderId;
        @Column(name = "order_type")
        public String orderType;
        @Column(name = "payment_record_id")
        public Long paymentRecordId;
        @Enumerated(EnumType.STRING)
        public RefundRequestStatus status;
        public String channel;
        public BigDecimal amount;
        @Column(name = "user_id")
        public Long userId;
        @Column(name = "master_id")
        public Long masterId;
        @Column(name = "operator_id")
        public Long operatorId;
        @Column(name = "trace_id")
        public String traceId;
        @Column(name = "reason_text")
        public String reasonText;
        @Column(name = "created_at")
        public LocalDateTime createdAt;
        @Column(name = "updated_at")
        public LocalDateTime updatedAt;
        @Column(name = "approved_at")
        public LocalDateTime approvedAt;
        @Column(name = "completed_at")
        public LocalDateTime completedAt;

        public RefundRequestEntity() {}
    }

    @Entity(name = "SettlementBillEntity")
    @Table(name = "settlement_bills")
    public static class SettlementBillEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        public Long id;
        @Column(name = "biz_no")
        public String bizNo;
        @Column(name = "order_id")
        public String orderId;
        @Column(name = "order_type")
        public String orderType;
        @Enumerated(EnumType.STRING)
        public SettlementBillStatus status;
        public String channel;
        public BigDecimal amount;
        @Column(name = "user_id")
        public Long userId;
        @Column(name = "master_id")
        public Long masterId;
        @Column(name = "operator_id")
        public Long operatorId;
        @Column(name = "trace_id")
        public String traceId;
        @Column(name = "wallet_account_id")
        public Long walletAccountId;
        @Column(name = "remark_text")
        public String remarkText;
        @Column(name = "created_at")
        public LocalDateTime createdAt;
        @Column(name = "updated_at")
        public LocalDateTime updatedAt;
        @Column(name = "approved_at")
        public LocalDateTime approvedAt;
        @Column(name = "settled_at")
        public LocalDateTime settledAt;

        public SettlementBillEntity() {}
    }

    @Entity(name = "WalletLedgerEntity")
    @Table(name = "wallet_ledgers")
    public static class WalletLedgerEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        public Long id;
        @Column(name = "biz_no")
        public String bizNo;
        @Column(name = "wallet_account_id")
        public Long walletAccountId;
        @Column(name = "settlement_bill_id")
        public Long settlementBillId;
        @Column(name = "refund_request_id")
        public Long refundRequestId;
        @Column(name = "order_id")
        public String orderId;
        @Column(name = "order_type")
        public String orderType;
        @Enumerated(EnumType.STRING)
        public WalletLedgerStatus status;
        @Enumerated(EnumType.STRING)
        @Column(name = "direction_code")
        public WalletLedgerDirection directionCode;
        public String channel;
        public BigDecimal amount;
        @Column(name = "user_id")
        public Long userId;
        @Column(name = "master_id")
        public Long masterId;
        @Column(name = "operator_id")
        public Long operatorId;
        @Column(name = "trace_id")
        public String traceId;
        public String title;
        @Column(name = "created_at")
        public LocalDateTime createdAt;
        @Column(name = "updated_at")
        public LocalDateTime updatedAt;

        public WalletLedgerEntity() {}
    }

    @Entity(name = "AuditLogEntity")
    @Table(name = "audit_logs")
    public static class AuditLogEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        public Long id;
        @Column(name = "biz_no")
        public String bizNo;
        @Column(name = "biz_type")
        public String bizType;
        @Column(name = "biz_id")
        public String bizId;
        @Column(name = "action_code")
        public String actionCode;
        @Column(name = "status_code")
        public String statusCode;
        @Column(name = "operator_role")
        public String operatorRole;
        @Column(name = "operator_id")
        public Long operatorId;
        @Column(name = "user_id")
        public Long userId;
        @Column(name = "master_id")
        public Long masterId;
        @Column(name = "trace_id")
        public String traceId;
        @Lob
        @Column(name = "detail_text")
        public String detailText;
        @Column(name = "created_at")
        public LocalDateTime createdAt;

        public AuditLogEntity() {}
    }

    @Entity(name = "NotificationTaskEntity")
    @Table(name = "notification_tasks")
    public static class NotificationTaskEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        public Long id;
        @Column(name = "biz_no")
        public String bizNo;
        @Column(name = "biz_type")
        public String bizType;
        @Column(name = "biz_id")
        public String bizId;
        @Column(name = "target_role")
        public String targetRole;
        @Column(name = "target_user_id")
        public Long targetUserId;
        public String channel;
        @Column(name = "template_code")
        public String templateCode;
        @Enumerated(EnumType.STRING)
        public NotificationTaskStatus status;
        @Lob
        @Column(name = "payload_text")
        public String payloadText;
        @Column(name = "trace_id")
        public String traceId;
        @Column(name = "next_retry_at")
        public LocalDateTime nextRetryAt;
        @Column(name = "sent_at")
        public LocalDateTime sentAt;
        @Column(name = "created_at")
        public LocalDateTime createdAt;
        @Column(name = "updated_at")
        public LocalDateTime updatedAt;

        public NotificationTaskEntity() {}
    }

    @Entity(name = "MessageSessionEntity")
    @Table(name = "message_sessions")
    public static class MessageSessionEntity {
        @Id
        public String id;
        @Column(name = "order_id")
        public String orderId;
        public String title;
        @Column(name = "participant_user_id")
        public Long participantUserId;

        public MessageSessionEntity() {}
    }

    @Entity(name = "MessageItemEntity")
    @Table(name = "message_items")
    public static class MessageItemEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        public Long id;
        @Column(name = "session_id")
        public String sessionId;
        @Column(name = "sender_code")
        public String senderCode;
        @Column(name = "message_type")
        public String messageType;
        @Column(name = "content_text")
        public String contentText;
        @Column(name = "message_time")
        public String messageTime;

        public MessageItemEntity() {}
    }

    @Entity(name = "MessageSessionReadEntity")
    @Table(name = "message_session_reads")
    public static class MessageSessionReadEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        public Long id;
        @Column(name = "session_id")
        public String sessionId;
        @Column(name = "reader_code")
        public String readerCode;
        @Column(name = "last_read_message_id")
        public Long lastReadMessageId;
        @Column(name = "read_at")
        public LocalDateTime readAt;

        public MessageSessionReadEntity() {}
    }

    @Entity(name = "BannerEntity")
    @Table(name = "banners")
    public static class BannerEntity {
        @Id
        public Long id;
        public String title;
        public String subtitle;
        public String image;
        public String link;
        @Column(name = "sort_order")
        public Integer sortOrder;
        public Boolean enabled;

        public BannerEntity() {}
    }

    @Entity(name = "NoticeEntity")
    @Table(name = "notices")
    public static class NoticeEntity {
        @Id
        public Long id;
        public String title;
        @Column(name = "content_text")
        public String contentText;
        @Column(name = "level_code")
        public String levelCode;
        public Boolean enabled;

        public NoticeEntity() {}
    }

    @Entity(name = "EcosystemCardEntity")
    @Table(name = "ecosystem_cards")
    public static class EcosystemCardEntity {
        @Id
        public Long id;
        public String name;
        @Column(name = "description_text")
        public String descriptionText;
        public String icon;
        public String color;
        public String link;
        @Column(name = "sort_order")
        public Integer sortOrder;
        public Boolean enabled;

        public EcosystemCardEntity() {}
    }

    @Entity(name = "AcademyCategoryEntity")
    @Table(name = "academy_categories")
    public static class AcademyCategoryEntity {
        @Id
        public Long id;
        public String name;
        @Column(name = "description_text")
        public String descriptionText;
        public String icon;
        @Column(name = "sort_order")
        public Integer sortOrder;
        public Boolean enabled;

        public AcademyCategoryEntity() {}
    }

    @Entity(name = "AcademyArticleEntity")
    @Table(name = "academy_articles")
    public static class AcademyArticleEntity {
        @Id
        public Long id;
        @Column(name = "category_id")
        public Long categoryId;
        public String title;
        @Column(name = "summary_text")
        public String summaryText;
        @Column(name = "cover_image")
        public String coverImage;
        @Lob
        @Column(name = "content_text")
        public String contentText;
        @Column(name = "author_name")
        public String authorName;
        @Column(name = "sort_order")
        public Integer sortOrder;
        public Boolean published;
        public Boolean enabled;
        @Column(name = "created_at")
        public LocalDateTime createdAt;
        @Column(name = "updated_at")
        public LocalDateTime updatedAt;

        public AcademyArticleEntity() {}
    }

    @Entity(name = "CommunityPostEntity")
    @Table(name = "community_posts")
    public static class CommunityPostEntity {
        @Id
        public Long id;
        @Column(name = "user_id")
        public Long userId;
        @Column(name = "city_name")
        public String cityName;
        public String title;
        @Lob
        @Column(name = "content_text")
        public String contentText;
        @Lob
        @Column(name = "images_text")
        public String imagesText;
        @Column(name = "like_count")
        public Integer likeCount;
        @Column(name = "comment_count")
        public Integer commentCount;
        @Column(name = "status_code")
        public String statusCode;
        @Column(name = "created_at")
        public LocalDateTime createdAt;
        @Column(name = "updated_at")
        public LocalDateTime updatedAt;

        public CommunityPostEntity() {}
    }

    @Entity(name = "CommunityCommentEntity")
    @Table(name = "community_comments")
    public static class CommunityCommentEntity {
        @Id
        public Long id;
        @Column(name = "post_id")
        public Long postId;
        @Column(name = "user_id")
        public Long userId;
        @Column(name = "content_text")
        public String contentText;
        @Column(name = "created_at")
        public LocalDateTime createdAt;

        public CommunityCommentEntity() {}
    }

    @Entity(name = "CommunityReportEntity")
    @Table(name = "community_reports")
    public static class CommunityReportEntity {
        @Id
        public Long id;
        @Column(name = "post_id")
        public Long postId;
        @Column(name = "reporter_user_id")
        public Long reporterUserId;
        @Column(name = "reason_text")
        public String reasonText;
        @Column(name = "status_code")
        public String statusCode;
        @Column(name = "created_at")
        public LocalDateTime createdAt;
        @Column(name = "handled_at")
        public LocalDateTime handledAt;

        public CommunityReportEntity() {}
    }

    @Entity(name = "ArbitrationCaseEntity")
    @Table(name = "arbitration_cases")
    public static class ArbitrationCaseEntity {
        @Id
        public String id;
        @Column(name = "order_id")
        public String orderId;
        @Column(name = "reason_text")
        public String reasonText;
        @Column(name = "status_text")
        public String statusText;
        @Column(name = "result_text")
        public String resultText;

        public ArbitrationCaseEntity() {}
    }

    @Entity(name = "CouponEntity")
    @Table(name = "coupons")
    public static class CouponEntity {
        @Id
        public Long id;
        public String title;
        public BigDecimal amount;
        @Column(name = "threshold_text")
        public String thresholdText;
        @Column(name = "expire_at")
        public String expireAt;
        public Boolean enabled;

        public CouponEntity() {}
    }

    @Entity(name = "MemberLevelEntity")
    @Table(name = "member_levels")
    public static class MemberLevelEntity {
        @Id
        public Long id;
        public String name;
        @Column(name = "benefit_text")
        public String benefitText;
        @Column(name = "points_required")
        public Integer pointsRequired;
        public Boolean enabled;

        public MemberLevelEntity() {}
    }

    @Entity(name = "AgreementEntity")
    @Table(name = "agreements")
    public static class AgreementEntity {
        @Id
        public Long id;
        public String title;
        public String version;
        @Lob
        @Column(name = "content_text")
        public String contentText;
        public Boolean enabled;

        public AgreementEntity() {}
    }

    @Entity(name = "RoleEntity")
    @Table(name = "roles")
    public static class RoleEntity {
        @Id
        public Long id;
        public String code;
        public String name;
        @Column(name = "description_text")
        public String descriptionText;
        public Boolean enabled;

        public RoleEntity() {}
    }

    @Entity(name = "MenuEntity")
    @Table(name = "menus")
    public static class MenuEntity {
        @Id
        public Long id;
        public String name;
        public String path;
        public String icon;
        @Column(name = "sort_order")
        public Integer sortOrder;
        public Boolean enabled;

        public MenuEntity() {}
    }

    @Entity(name = "PermissionEntity")
    @Table(name = "permissions")
    public static class PermissionEntity {
        @Id
        public Long id;
        public String code;
        public String name;
        @Column(name = "description_text")
        public String descriptionText;
        public Boolean enabled;

        public PermissionEntity() {}
    }

    @Entity(name = "UserRoleBindingEntity")
    @Table(name = "user_roles")
    public static class UserRoleBindingEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        public Long id;
        @Column(name = "user_id")
        public Long userId;
        @Column(name = "role_id")
        public Long roleId;

        public UserRoleBindingEntity() {}
    }

    @Entity(name = "RoleMenuBindingEntity")
    @Table(name = "role_menus")
    public static class RoleMenuBindingEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        public Long id;
        @Column(name = "role_id")
        public Long roleId;
        @Column(name = "menu_id")
        public Long menuId;

        public RoleMenuBindingEntity() {}
    }

    @Entity(name = "RolePermissionBindingEntity")
    @Table(name = "role_permissions")
    public static class RolePermissionBindingEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        public Long id;
        @Column(name = "role_id")
        public Long roleId;
        @Column(name = "permission_id")
        public Long permissionId;

        public RolePermissionBindingEntity() {}
    }

    @Entity(name = "AuthSessionEntity")
    @Table(name = "auth_sessions")
    public static class AuthSessionEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        public Long id;
        @Column(name = "user_id")
        public Long userId;
        @Column(name = "role_code")
        public String roleCode;
        @Column(name = "access_token_hash")
        public String accessTokenHash;
        @Column(name = "access_token_preview")
        public String accessTokenPreview;
        @Column(name = "refresh_token_hash")
        public String refreshTokenHash;
        @Column(name = "client_type")
        public String clientType;
        @Column(name = "access_expires_at")
        public LocalDateTime accessExpiresAt;
        @Column(name = "refresh_expires_at")
        public LocalDateTime refreshExpiresAt;
        @Column(name = "created_at")
        public LocalDateTime createdAt;
        @Column(name = "updated_at")
        public LocalDateTime updatedAt;
        @Column(name = "last_seen_at")
        public LocalDateTime lastSeenAt;

        public AuthSessionEntity() {}
    }

    @Entity(name = "SmsCodeEntity")
    @Table(name = "auth_sms_codes")
    public static class SmsCodeEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        public Long id;
        public String mobile;
        public String purpose;
        public String code;
        @Column(name = "expires_at")
        public LocalDateTime expiresAt;
        @Column(name = "consumed_at")
        public LocalDateTime consumedAt;
        @Column(name = "created_at")
        public LocalDateTime createdAt;

        public SmsCodeEntity() {}
    }

    @Entity(name = "PricingRuleEntity")
    @Table(name = "pricing_rules")
    public static class PricingRuleEntity {
        @Id
        public Long id;
        @Column(name = "category_id")
        public Long categoryId;
        @Column(name = "label_text")
        public String labelText;
        @Column(name = "base_price")
        public BigDecimal basePrice;
        public String coefficient;
        @Column(name = "guide_price")
        public String guidePrice;
        public Boolean enabled;

        public PricingRuleEntity() {}
    }

    @Entity(name = "DispatchZoneEntity")
    @Table(name = "dispatch_zones")
    public static class DispatchZoneEntity {
        @Id
        public Long id;
        @Column(name = "city_name")
        public String cityName;
        @Column(name = "district_name")
        public String districtName;
        @Column(name = "sort_order")
        public Integer sortOrder;
        public Boolean enabled;

        public DispatchZoneEntity() {}
    }

    @Entity(name = "MediaFileEntity")
    @Table(name = "media_files")
    public static class MediaFileEntity {
        @Id
        public Long id;
        @Column(name = "biz_type")
        public String bizType;
        @Column(name = "biz_id")
        public String bizId;
        @Column(name = "original_name")
        public String originalName;
        @Column(name = "content_type")
        public String contentType;
        @Column(name = "size_bytes")
        public Long sizeBytes;
        @Column(name = "storage_path")
        public String storagePath;
        @Column(name = "access_url")
        public String accessUrl;
        @Column(name = "created_at")
        public LocalDateTime createdAt;

        public MediaFileEntity() {}
    }

    @Entity(name = "CommentEntity")
    @Table(name = "comments")
    public static class CommentEntity {
        @Id
        public Long id;
        @Column(name = "service_item_id")
        public Long serviceItemId;
        @Column(name = "user_name")
        public String userName;
        public Integer score;
        @Lob
        @Column(name = "content_text")
        public String contentText;
        @Lob
        @Column(name = "images_text")
        public String imagesText;
        @Lob
        @Column(name = "tags_text")
        public String tagsText;
        @Column(name = "created_at")
        public LocalDateTime createdAt;

        public CommentEntity() {}
    }

    @Entity(name = "OrderTrackPointEntity")
    @Table(name = "order_track_points")
    public static class OrderTrackPointEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        public Long id;
        @Column(name = "order_id")
        public String orderId;
        @Column(name = "point_type")
        public String pointType;
        @Column(name = "label_text")
        public String labelText;
        @Column(name = "description_text")
        public String descriptionText;
        public Double latitude;
        public Double longitude;
        @Column(name = "created_at")
        public LocalDateTime createdAt;

        public OrderTrackPointEntity() {}
    }
}
