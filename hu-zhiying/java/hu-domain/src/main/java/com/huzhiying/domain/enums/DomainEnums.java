package com.huzhiying.domain.enums;

public interface DomainEnums {

    enum RoleCode {
        USER,
        MASTER,
        CUSTOMER_SERVICE,
        ADMIN
    }

    enum ServiceOrderStatus {
        PENDING_PAYMENT,
        PENDING_DISPATCH,
        PENDING_ACCEPT,
        ON_THE_WAY,
        ARRIVED,
        WAITING_SUPPLEMENT_PAYMENT,
        IN_SERVICE,
        COMPLETED,
        AFTER_SALES
    }

    enum ProductOrderStatus {
        PENDING_PAYMENT,
        PAID,
        PENDING_SHIPMENT,
        SHIPPED,
        COMPLETED,
        REFUNDING
    }

    enum PaymentStatus {
        UNPAID,
        PARTIAL_PAID,
        PAID,
        REFUNDING,
        REFUNDED
    }

    enum QuotationStatus {
        PENDING_CONFIRM,
        CONFIRMED,
        REJECTED
    }

    enum DispatchMode {
        ROB,
        FORCE_ASSIGN
    }
}
