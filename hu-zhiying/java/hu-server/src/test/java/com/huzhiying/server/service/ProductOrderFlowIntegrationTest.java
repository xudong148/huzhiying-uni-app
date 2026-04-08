package com.huzhiying.server.service;

import com.huzhiying.domain.enums.DomainEnums.PaymentStatus;
import com.huzhiying.domain.enums.DomainEnums.ProductOrderStatus;
import com.huzhiying.domain.enums.DomainEnums.ServiceOrderStatus;
import com.huzhiying.domain.model.DomainModels.ProductOrder;
import com.huzhiying.domain.model.DomainModels.ServiceOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
class ProductOrderFlowIntegrationTest {

    @Autowired
    private PlatformCommandService commandService;

    @Autowired
    private PlatformQueryService queryService;

    @Test
    void paymentCallbackShouldCreateInstallServiceOrder() {
        ProductOrder created = commandService.createProductOrder(1001L, 1L, 1L);
        assertEquals(ProductOrderStatus.PENDING_PAYMENT, created.status());
        assertEquals(PaymentStatus.UNPAID, created.paymentStatus());

        commandService.handleWechatCallback(created.id());

        ProductOrder paid = (ProductOrder) queryService.productOrder(created.id());
        assertEquals(ProductOrderStatus.PENDING_SHIPMENT, paid.status());
        assertEquals(PaymentStatus.PAID, paid.paymentStatus());
        assertNotNull(paid.installServiceOrderId());
        assertFalse(paid.installServiceOrderId().isBlank());

        ServiceOrder installOrder = queryService.serviceOrder(paid.installServiceOrderId());
        assertEquals(ServiceOrderStatus.PENDING_DISPATCH, installOrder.status());
        assertEquals(PaymentStatus.PAID, installOrder.paymentStatus());
    }
}
