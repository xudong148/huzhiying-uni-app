package com.huzhiying.server.service;

import com.huzhiying.domain.enums.DomainEnums.ServiceOrderStatus;

@org.springframework.stereotype.Service
public class PlatformFacadeService {

    private final PlatformQueryService queryService;
    private final PlatformCommandService commandService;

    public PlatformFacadeService(PlatformQueryService queryService, PlatformCommandService commandService) {
        this.queryService = queryService;
        this.commandService = commandService;
    }

    public Object login(String role) { return commandService.login(role); }
    public Object categories() { return queryService.categories(); }
    public Object services() { return queryService.services(); }
    public Object serviceDetail(Long serviceItemId) { return queryService.serviceDetail(serviceItemId); }
    public Object products() { return queryService.products(); }
    public Object productDetail(Long productId) { return queryService.productDetail(productId); }
    public Object search(String keyword) { return queryService.search(keyword); }
    public Object currentUser() { return queryService.currentUser(); }
    public Object saveProfile(String nickname, String mobile) { return commandService.saveProfile(nickname, mobile); }
    public Object addresses() { return queryService.addresses(); }
    public Object saveAddress(Long id, String tag, String name, String mobile, String detailAddress, boolean isDefault) {
        return commandService.saveAddress(id, tag, name, mobile, detailAddress, isDefault);
    }
    public Object deleteAddress(Long id) { return commandService.deleteAddress(id); }
    public Object coupons() { return queryService.coupons(); }
    public Object currentMember() { return queryService.currentMember(); }
    public Object serviceOrders() { return queryService.serviceOrders(); }
    public Object serviceOrder(String id) { return queryService.serviceOrder(id); }
    public Object createServiceOrder(Long serviceItemId, String title, String appointment, Long addressId, String description, boolean emergency, boolean nightService) {
        return commandService.createServiceOrder(serviceItemId, title, appointment, addressId, description, emergency, nightService);
    }
    public Object updateServiceOrderStatus(String id, ServiceOrderStatus status) { return commandService.updateServiceOrderStatus(id, status); }
    public Object productOrders() { return queryService.productOrders(); }
    public Object productOrder(String id) { return queryService.productOrder(id); }
    public Object createProductOrder(Long productId, Long skuId, Long addressId) {
        return commandService.createProductOrder(productId, skuId, addressId);
    }
    public Object createQuotation(String orderId, String remark) { return commandService.createQuotation(orderId, remark); }
    public Object confirmQuotation(String quotationId) { return commandService.confirmQuotation(quotationId); }
    public Object dispatchTasks() { return queryService.dispatchTasks(); }
    public Object claimTask(String id, String masterName) { return commandService.claimTask(id, masterName); }
    public Object forceAssignTask(String id, String masterName) { return commandService.forceAssignTask(id, masterName); }
    public Object applyMaster(String realName, String mobile, String skills, String area) { return commandService.applyMaster(realName, mobile, skills, area); }
    public Object masterDashboard() { return queryService.masterDashboard(); }
    public Object wallet() { return queryService.wallet(); }
    public Object walletTransactions() { return queryService.walletTransactions(); }
    public Object masterSettings() { return queryService.masterSettings(); }
    public Object saveMasterSettings(boolean listening, String maxDistance, boolean privacyNumber) {
        return commandService.saveMasterSettings(listening, maxDistance, privacyNumber);
    }
    public Object messageSessions() { return queryService.messageSessions(); }
    public Object messageItems(String sessionId) { return queryService.messageItems(sessionId); }
    public Object sendMessage(String sessionId, String senderCode, String content) { return commandService.sendMessage(sessionId, senderCode, content); }
    public Object notices() { return queryService.notices(); }
    public Object adminDashboard() { return queryService.adminDashboard(); }
    public Object masters() { return queryService.masters(); }
    public Object arbitrations() { return queryService.arbitrations(); }
    public Object pricingRules() { return queryService.pricingRules(); }
    public Object adminDispatchRows() { return queryService.adminDispatchRows(); }
    public Object adminOrders() { return queryService.adminOrders(); }
    public Object financeRows() { return queryService.financeRows(); }
    public Object createWechatPrepay(String orderId) { return queryService.createWechatPrepay(orderId); }
    public Object refundOrder(String orderId) { return commandService.refundOrder(orderId); }
    public Object handleWechatCallback(String orderId) { return commandService.handleWechatCallback(orderId); }
}
