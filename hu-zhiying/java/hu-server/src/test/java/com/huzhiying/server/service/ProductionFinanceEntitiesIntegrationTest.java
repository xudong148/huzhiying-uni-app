package com.huzhiying.server.service;

import com.huzhiying.domain.enums.DomainEnums.PaymentRecordStatus;
import com.huzhiying.domain.enums.DomainEnums.ServiceOrderStatus;
import com.huzhiying.domain.enums.DomainEnums.SettlementBillStatus;
import com.huzhiying.domain.enums.DomainEnums.WalletLedgerStatus;
import com.huzhiying.domain.model.DomainModels.ServiceOrder;
import com.huzhiying.server.dto.AdminBusinessDtos;
import com.huzhiying.server.repository.PlatformRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class ProductionFinanceEntitiesIntegrationTest {

    @Autowired
    private PlatformCommandService commandService;

    @Autowired
    private PlatformRepository platformRepository;

    @Autowired
    private AdminBusinessService adminBusinessService;

    @Test
    void wechatCallbackShouldPersistExplicitPaymentRecordAndAuditTrail() {
        ServiceOrder created = commandService.createServiceOrder(
                201L,
                "绌鸿皟涓婇棬缁翠慨",
                "2026-04-09 18:00-20:00",
                1L,
                "production finance record test",
                false,
                false,
                List.of()
        );

        commandService.handleWechatCallback(created.id());

        var paymentRecords = platformRepository.listPaymentRecordsByOrderId(created.id());
        assertEquals(1, paymentRecords.size());
        assertEquals(PaymentRecordStatus.SUCCESS, paymentRecords.get(0).status);
        assertEquals("INITIAL", paymentRecords.get(0).paymentStage);
        assertTrue(platformRepository.listAuditLogsByBiz("SERVICE", created.id()).stream()
                .anyMatch(item -> "PAYMENT_CALLBACK_CONFIRMED".equals(item.actionCode)));
        assertTrue(platformRepository.listNotificationTasksByBiz("SERVICE", created.id()).stream()
                .anyMatch(item -> "payment-success".equals(item.templateCode)));
    }

    @Test
    void refundRequestShouldPersistExplicitRefundEntity() {
        ServiceOrder created = commandService.createServiceOrder(
                201L,
                "绌鸿皟涓婇棬缁翠慨",
                "2026-04-10 09:00-11:00",
                1L,
                "refund entity test",
                false,
                false,
                List.of()
        );
        commandService.handleWechatCallback(created.id());

        commandService.refundOrder(created.id(), "test-refund-request");

        var refundRequest = platformRepository.findLatestRefundRequestByOrderId(created.id()).orElseThrow();
        assertEquals("SERVICE", refundRequest.orderType);
        assertEquals("test-refund-request", refundRequest.reasonText);
        assertNotNull(refundRequest.traceId);
        assertTrue(platformRepository.listAuditLogsByBiz("SERVICE", created.id()).stream()
                .anyMatch(item -> "REFUND_REQUEST_CREATED".equals(item.actionCode)));
    }

    @Test
    void settlementApprovalShouldDriveSettlementBillWalletLedgerAndWalletBalance() {
        BigDecimal availableBefore = platformRepository.findWalletAccountByMasterUserId(PlatformDomainSupport.DEFAULT_MASTER_USER_ID)
                .orElseThrow()
                .availableAmount;

        ServiceOrder created = commandService.createServiceOrder(
                201L,
                "绌鸿皟涓婇棬缁翠慨",
                "2026-04-10 14:00-16:00",
                1L,
                "settlement bill test",
                false,
                false,
                List.of()
        );
        commandService.handleWechatCallback(created.id());

        String dispatchTaskId = platformRepository.findDispatchTaskByOrderId(created.id()).orElseThrow().id;
        commandService.forceAssignTask(dispatchTaskId, null);
        commandService.updateServiceOrderStatus(created.id(), ServiceOrderStatus.ON_THE_WAY);
        commandService.updateServiceOrderStatus(created.id(), ServiceOrderStatus.ARRIVED);
        commandService.updateServiceOrderStatus(created.id(), ServiceOrderStatus.IN_SERVICE);
        commandService.updateServiceOrderStatus(created.id(), ServiceOrderStatus.COMPLETED);

        var settlementBill = platformRepository.findSettlementBillByOrderId(created.id()).orElseThrow();
        assertEquals(SettlementBillStatus.PENDING_REVIEW, settlementBill.status);

        var pendingLedger = platformRepository.findWalletLedgerBySettlementBillId(settlementBill.id).orElseThrow();
        assertEquals(WalletLedgerStatus.PENDING_REVIEW, pendingLedger.status);

        AdminBusinessDtos.FinanceDetail detail = adminBusinessService.approveFinance(
                "SETTLE-" + settlementBill.id,
                new AdminBusinessDtos.FinanceApproveRequest("approve-settlement")
        );

        var approvedBill = platformRepository.findSettlementBill(settlementBill.id).orElseThrow();
        var postedLedger = platformRepository.findWalletLedgerBySettlementBillId(settlementBill.id).orElseThrow();
        BigDecimal availableAfter = platformRepository.findWalletAccountByMasterUserId(PlatformDomainSupport.DEFAULT_MASTER_USER_ID)
                .orElseThrow()
                .availableAmount;

        assertEquals("SETTLE-" + settlementBill.id, detail.billNo());
        assertEquals(SettlementBillStatus.APPROVED, approvedBill.status);
        assertEquals(WalletLedgerStatus.POSTED, postedLedger.status);
        assertEquals(availableBefore.add(created.amount()), availableAfter);
        assertTrue(platformRepository.listAuditLogsByBiz("SERVICE", created.id()).stream()
                .anyMatch(item -> "SETTLEMENT_APPROVED".equals(item.actionCode)));
    }
}
