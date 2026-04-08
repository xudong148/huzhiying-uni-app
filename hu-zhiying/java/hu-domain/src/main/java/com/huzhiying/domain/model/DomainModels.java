package com.huzhiying.domain.model;

import com.huzhiying.domain.enums.DomainEnums.DispatchMode;
import com.huzhiying.domain.enums.DomainEnums.PaymentStatus;
import com.huzhiying.domain.enums.DomainEnums.ProductOrderStatus;
import com.huzhiying.domain.enums.DomainEnums.QuotationStatus;
import com.huzhiying.domain.enums.DomainEnums.RoleCode;
import com.huzhiying.domain.enums.DomainEnums.ServiceOrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface DomainModels {

    record User(Long id, String nickname, String mobile, RoleCode role, String avatar, String level) {}

    record Role(Long id, String code, String name) {}

    record MasterProfile(Long userId, String realName, String skillTags, String serviceArea, BigDecimal deposit,
                         Integer creditScore, boolean online) {}

    record Address(Long id, String tag, String name, String mobile, String detail, Double latitude, Double longitude,
                   boolean isDefault) {}

    record ServiceCategory(Long id, String name, String icon, List<ServiceItem> services) {}

    record ServiceItem(Long id, Long categoryId, String name, String subtitle, BigDecimal basePrice,
                       BigDecimal doorPrice, List<String> guarantees, List<String> tags) {}

    record Product(Long id, String name, String description, BigDecimal price, BigDecimal tagPrice,
                   BigDecimal discountPrice, List<Sku> skus, boolean createInstallOrder) {}

    record Sku(Long id, Long productId, String name, BigDecimal price, BigDecimal tagPrice,
               BigDecimal discountPrice, Integer stock) {}

    record SearchDocument(String id, String type, String title, String summary, BigDecimal price, String icon) {}

    record WorkStepRecord(String key, String label, String desc, boolean done, LocalDateTime time) {}

    record QuotationItem(String name, BigDecimal amount) {}

    record Quotation(String id, String orderId, List<QuotationItem> items, BigDecimal totalAmount,
                     QuotationStatus status, String remark) {}

    record ServiceOrder(String id, String title, ServiceOrderStatus status, PaymentStatus paymentStatus,
                        String userName, String masterName, String appointment, Address address, BigDecimal amount,
                        DispatchMode dispatchMode, List<WorkStepRecord> timeline, Quotation quotation, String eta) {}

    record ProductOrder(String id, String title, ProductOrderStatus status, PaymentStatus paymentStatus,
                        String userName, Address address, BigDecimal amount, boolean createInstallOrder,
                        String installServiceOrderId) {}

    record DispatchTask(String id, String orderId, String title, BigDecimal income, String distance, String area,
                        DispatchMode mode, String currentMaster, List<String> tags) {}

    record Coupon(Long id, String title, BigDecimal amount, String threshold, String expireAt) {}

    record MemberLevel(String name, String benefit, Integer pointsRequired) {}

    record WalletAccount(BigDecimal available, BigDecimal frozen, BigDecimal todayIncome) {}

    record WalletTransaction(Long id, String title, BigDecimal amount, String time) {}

    record ArbitrationCase(String id, String orderId, String reason, String status) {}

    record MessageSession(String id, String orderId, String title, String participant) {}

    record MessageItem(Long id, String sessionId, String sender, String type, String content, String time) {}

    record Banner(Long id, String title, String subtitle, String image) {}

    record Notice(Long id, String title, String level) {}

    record AuthToken(String token, String refreshToken, String role) {}
}
