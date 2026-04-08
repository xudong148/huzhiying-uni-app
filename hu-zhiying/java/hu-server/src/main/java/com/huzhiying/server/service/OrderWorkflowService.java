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
            ServiceOrderStatus.PENDING_DISPATCH, Set.of(ServiceOrderStatus.PENDING_ACCEPT, ServiceOrderStatus.CANCELLED),
            ServiceOrderStatus.PENDING_ACCEPT, Set.of(ServiceOrderStatus.ON_THE_WAY, ServiceOrderStatus.CANCELLED),
            ServiceOrderStatus.ON_THE_WAY, Set.of(ServiceOrderStatus.ARRIVED, ServiceOrderStatus.CANCELLED),
            ServiceOrderStatus.ARRIVED, Set.of(ServiceOrderStatus.IN_SERVICE, ServiceOrderStatus.WAITING_SUPPLEMENT_PAYMENT, ServiceOrderStatus.CANCELLED),
            ServiceOrderStatus.WAITING_SUPPLEMENT_PAYMENT, Set.of(ServiceOrderStatus.IN_SERVICE, ServiceOrderStatus.REFUNDING),
            ServiceOrderStatus.IN_SERVICE, Set.of(ServiceOrderStatus.COMPLETED, ServiceOrderStatus.REFUNDING),
            ServiceOrderStatus.REFUNDING, Set.of(ServiceOrderStatus.AFTER_SALES),
            ServiceOrderStatus.COMPLETED, Set.of(ServiceOrderStatus.AFTER_SALES)
    );

    public ServiceOrder advance(ServiceOrder order, ServiceOrderStatus target) {
        Set<ServiceOrderStatus> allowedTargets = TRANSITIONS.getOrDefault(order.status(), Set.of());
        if (!allowedTargets.contains(target)) {
            throw new IllegalArgumentException("非法状态流转: " + order.status() + " -> " + target);
        }

        List<WorkStepRecord> nextTimeline = new ArrayList<>(order.timeline());
        nextTimeline.add(new WorkStepRecord(
                target.name().toLowerCase(),
                statusLabel(target),
                "系统已将订单状态更新为 " + statusLabel(target),
                true,
                LocalDateTime.now()
        ));

        return new ServiceOrder(order.id(), order.title(), target, order.paymentStatus(), order.userName(),
                order.masterName(), order.appointment(), order.address(), order.amount(), order.dispatchMode(),
                nextTimeline, order.quotation(), order.eta());
    }

    public ServiceOrder confirmQuotation(ServiceOrder order) {
        Quotation quotation = order.quotation();
        Quotation confirmed = new Quotation(
                quotation.id(),
                quotation.orderId(),
                quotation.items(),
                quotation.totalAmount(),
                QuotationStatus.CONFIRMED,
                quotation.remark()
        );
        return new ServiceOrder(order.id(), order.title(), ServiceOrderStatus.WAITING_SUPPLEMENT_PAYMENT, PaymentStatus.PARTIAL_PAID,
                order.userName(), order.masterName(), order.appointment(), order.address(),
                order.amount().add(quotation.totalAmount()), order.dispatchMode(), order.timeline(),
                confirmed, order.eta());
    }

    private String statusLabel(ServiceOrderStatus status) {
        return switch (status) {
            case PENDING_DISPATCH -> "待派单";
            case PENDING_ACCEPT -> "待接单";
            case ON_THE_WAY -> "出发中";
            case ARRIVED -> "已到达";
            case WAITING_SUPPLEMENT_PAYMENT -> "待补款";
            case IN_SERVICE -> "施工中";
            case COMPLETED -> "已完成";
            case CANCELLED -> "已取消";
            case REFUNDING -> "退款中";
            case AFTER_SALES -> "售后中";
            case PENDING_PAYMENT -> "待支付";
        };
    }
}
