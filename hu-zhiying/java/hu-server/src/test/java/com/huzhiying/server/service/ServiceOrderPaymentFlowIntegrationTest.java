package com.huzhiying.server.service;

import com.huzhiying.domain.enums.DomainEnums.PaymentStatus;
import com.huzhiying.domain.enums.DomainEnums.ServiceOrderStatus;
import com.huzhiying.domain.model.DomainModels.Quotation;
import com.huzhiying.domain.model.DomainModels.ServiceOrder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class ServiceOrderPaymentFlowIntegrationTest {

    @Autowired
    private PlatformCommandService commandService;

    @Autowired
    private PlatformQueryService queryService;

    @Test
    void serviceOrderShouldWaitForPaymentBeforeDispatch() {
        int dispatchCountBefore = ((List<?>) queryService.dispatchTasks()).size();

        ServiceOrder created = commandService.createServiceOrder(
                201L,
                "空调上门维修",
                "今天 18:00-20:00",
                1L,
                "客厅空调制冷效果差",
                false,
                false,
                List.of()
        );

        assertEquals(ServiceOrderStatus.PENDING_PAYMENT, created.status());
        assertEquals(PaymentStatus.UNPAID, created.paymentStatus());
        assertEquals(dispatchCountBefore, ((List<?>) queryService.dispatchTasks()).size());

        commandService.handleWechatCallback(created.id());

        ServiceOrder paid = queryService.serviceOrder(created.id());
        assertEquals(ServiceOrderStatus.PENDING_DISPATCH, paid.status());
        assertEquals(PaymentStatus.PARTIAL_PAID, paid.paymentStatus());
        assertEquals(dispatchCountBefore + 1, ((List<?>) queryService.dispatchTasks()).size());
    }

    @Test
    void supplementPaymentShouldResumeServiceOnlyAfterCallback() {
        ServiceOrder created = commandService.createServiceOrder(
                201L,
                "空调上门维修",
                "明天 09:00-11:00",
                1L,
                "需要检测并更换配件",
                true,
                false,
                List.of()
        );

        commandService.handleWechatCallback(created.id());
        commandService.createQuotation(created.id(), "新增配件与工时费用");

        ServiceOrder waitingPay = queryService.serviceOrder(created.id());
        Quotation quotation = waitingPay.quotation();

        ServiceOrder confirmed = commandService.confirmQuotation(quotation.id());
        assertEquals(ServiceOrderStatus.WAITING_SUPPLEMENT_PAYMENT, confirmed.status());
        assertEquals(PaymentStatus.PARTIAL_PAID, confirmed.paymentStatus());
        assertEquals("CONFIRMED", confirmed.quotation().status().name());

        commandService.handleWechatCallback(created.id());

        ServiceOrder resumed = queryService.serviceOrder(created.id());
        assertEquals(ServiceOrderStatus.IN_SERVICE, resumed.status());
        assertEquals(PaymentStatus.PAID, resumed.paymentStatus());
        assertTrue(resumed.timeline().stream().anyMatch(item -> "补款完成".equals(item.label())));
    }
}
