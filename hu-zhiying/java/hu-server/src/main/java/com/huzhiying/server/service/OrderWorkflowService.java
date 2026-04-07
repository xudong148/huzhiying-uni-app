package com.huzhiying.server.service;

import com.huzhiying.domain.enums.DomainEnums.PaymentStatus;
import com.huzhiying.domain.enums.DomainEnums.QuotationStatus;
import com.huzhiying.domain.enums.DomainEnums.ServiceOrderStatus;
import com.huzhiying.domain.model.DomainModels.Quotation;
import com.huzhiying.domain.model.DomainModels.ServiceOrder;
import com.huzhiying.domain.model.DomainModels.WorkStepRecord;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class OrderWorkflowService {

    private static final Map<ServiceOrderStatus, Set<ServiceOrderStatus>> TRANSITIONS = Map.of(
            ServiceOrderStatus.PENDING_ACCEPT, Set.of(ServiceOrderStatus.ON_THE_WAY),
            ServiceOrderStatus.ON_THE_WAY, Set.of(ServiceOrderStatus.ARRIVED),
            ServiceOrderStatus.ARRIVED, Set.of(ServiceOrderStatus.WAITING_SUPPLEMENT_PAYMENT, ServiceOrderStatus.IN_SERVICE),
            ServiceOrderStatus.WAITING_SUPPLEMENT_PAYMENT, Set.of(ServiceOrderStatus.IN_SERVICE),
            ServiceOrderStatus.IN_SERVICE, Set.of(ServiceOrderStatus.COMPLETED),
            ServiceOrderStatus.COMPLETED, Set.of(ServiceOrderStatus.AFTER_SALES)
    );

    public ServiceOrder advance(ServiceOrder order, ServiceOrderStatus target) {
        Set<ServiceOrderStatus> allowedTargets = TRANSITIONS.getOrDefault(order.status(), Set.of());
        if (!allowedTargets.contains(target)) {
            throw new IllegalArgumentException("非法状态流转: " + order.status() + " -> " + target);
        }

        List<WorkStepRecord> nextTimeline = new ArrayList<>(order.timeline());
        nextTimeline.add(new WorkStepRecord(target.name().toLowerCase(), target.name(),
                "系统完成状态流转到 " + target, true, LocalDateTime.now()));

        return new ServiceOrder(order.id(), order.title(), target, order.paymentStatus(), order.userName(),
                order.masterName(), order.appointment(), order.address(), order.amount(), order.dispatchMode(),
                nextTimeline, order.quotation(), order.eta());
    }

    public ServiceOrder confirmQuotation(ServiceOrder order) {
        Quotation quotation = order.quotation();
        Quotation confirmed = new Quotation(quotation.id(), quotation.orderId(), quotation.items(),
                quotation.totalAmount(), QuotationStatus.CONFIRMED, quotation.remark());
        return new ServiceOrder(order.id(), order.title(), ServiceOrderStatus.IN_SERVICE, PaymentStatus.PAID,
                order.userName(), order.masterName(), order.appointment(), order.address(),
                order.amount().add(quotation.totalAmount()), order.dispatchMode(), order.timeline(),
                confirmed, order.eta());
    }
}
