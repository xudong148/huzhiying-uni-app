package com.huzhiying.server.service;

import com.huzhiying.domain.enums.DomainEnums.DispatchMode;
import com.huzhiying.domain.enums.DomainEnums.PaymentStatus;
import com.huzhiying.domain.enums.DomainEnums.QuotationStatus;
import com.huzhiying.domain.enums.DomainEnums.ServiceOrderStatus;
import com.huzhiying.domain.model.DomainModels.Address;
import com.huzhiying.domain.model.DomainModels.Quotation;
import com.huzhiying.domain.model.DomainModels.QuotationItem;
import com.huzhiying.domain.model.DomainModels.ServiceOrder;
import com.huzhiying.domain.model.DomainModels.WorkStepRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

class OrderWorkflowServiceTest {

    private final OrderWorkflowService orderWorkflowService = new OrderWorkflowService();

    @Test
    void shouldAdvanceOrderStatus() {
        ServiceOrder order = mockOrder(ServiceOrderStatus.PENDING_ACCEPT, null);
        ServiceOrder updated = orderWorkflowService.advance(order, ServiceOrderStatus.ON_THE_WAY);
        Assertions.assertEquals(ServiceOrderStatus.ON_THE_WAY, updated.status());
    }

    @Test
    void shouldConfirmQuotation() {
        Quotation quotation = new Quotation("Q1", "SO1", List.of(new QuotationItem("配件", BigDecimal.TEN)),
                BigDecimal.TEN, QuotationStatus.PENDING_CONFIRM, "test");
        ServiceOrder order = mockOrder(ServiceOrderStatus.WAITING_SUPPLEMENT_PAYMENT, quotation);
        ServiceOrder updated = orderWorkflowService.confirmQuotation(order);
        Assertions.assertEquals(ServiceOrderStatus.WAITING_SUPPLEMENT_PAYMENT, updated.status());
        Assertions.assertEquals(PaymentStatus.PARTIAL_PAID, updated.paymentStatus());
        Assertions.assertEquals(QuotationStatus.CONFIRMED, updated.quotation().status());
    }

    private ServiceOrder mockOrder(ServiceOrderStatus status, Quotation quotation) {
        return new ServiceOrder(
                "SO1",
                "测试工单",
                status,
                PaymentStatus.PARTIAL_PAID,
                "订单用户",
                "服务技师",
                "今天 14:00-16:00",
                new Address(1L, "家", "联系人", "13900001234", "浦东新区", 31.0, 121.0, true),
                BigDecimal.valueOf(88),
                DispatchMode.ROB,
                List.of(new WorkStepRecord("created", "订单创建", "创建", true, LocalDateTime.now())),
                quotation,
                "26 分钟"
        );
    }
}
