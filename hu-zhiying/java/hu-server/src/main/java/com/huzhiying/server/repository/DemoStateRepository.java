package com.huzhiying.server.repository;

import com.huzhiying.domain.model.DomainModels.Address;
import com.huzhiying.domain.model.DomainModels.ArbitrationCase;
import com.huzhiying.domain.model.DomainModels.Banner;
import com.huzhiying.domain.model.DomainModels.Coupon;
import com.huzhiying.domain.model.DomainModels.DispatchTask;
import com.huzhiying.domain.model.DomainModels.MasterProfile;
import com.huzhiying.domain.model.DomainModels.MemberLevel;
import com.huzhiying.domain.model.DomainModels.MessageItem;
import com.huzhiying.domain.model.DomainModels.MessageSession;
import com.huzhiying.domain.model.DomainModels.Notice;
import com.huzhiying.domain.model.DomainModels.Product;
import com.huzhiying.domain.model.DomainModels.ProductOrder;
import com.huzhiying.domain.model.DomainModels.SearchDocument;
import com.huzhiying.domain.model.DomainModels.ServiceCategory;
import com.huzhiying.domain.model.DomainModels.ServiceOrder;
import com.huzhiying.domain.model.DomainModels.User;
import com.huzhiying.domain.model.DomainModels.WalletAccount;
import com.huzhiying.domain.model.DomainModels.WalletTransaction;
import com.huzhiying.server.support.DemoDataFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DemoStateRepository {

    private final List<User> users;
    private final List<MasterProfile> masters;
    private final List<Address> addresses;
    private final List<ServiceCategory> categories;
    private final List<Product> products;
    private final List<SearchDocument> searchDocuments;
    private final Map<String, ServiceOrder> serviceOrders;
    private final Map<String, ProductOrder> productOrders;
    private final Map<String, DispatchTask> dispatchTasks;
    private final List<Coupon> coupons;
    private final MemberLevel memberLevel;
    private final List<WalletTransaction> walletTransactions;
    private final WalletAccount walletAccount;
    private final List<ArbitrationCase> arbitrations;
    private final List<MessageSession> messageSessions;
    private final List<MessageItem> messageItems;
    private final List<Banner> banners;
    private final List<Notice> notices;

    public DemoStateRepository() {
        DemoDataFactory.SeedData seedData = DemoDataFactory.seed();
        this.users = new ArrayList<>(seedData.users());
        this.masters = new ArrayList<>(seedData.masters());
        this.addresses = new ArrayList<>(seedData.addresses());
        this.categories = new ArrayList<>(seedData.categories());
        this.products = new ArrayList<>(seedData.products());
        this.searchDocuments = new ArrayList<>(seedData.searchDocuments());
        this.serviceOrders = new LinkedHashMap<>(seedData.serviceOrders());
        this.productOrders = new LinkedHashMap<>(seedData.productOrders());
        this.dispatchTasks = new LinkedHashMap<>(seedData.dispatchTasks());
        this.coupons = new ArrayList<>(seedData.coupons());
        this.memberLevel = seedData.memberLevel();
        this.walletTransactions = new ArrayList<>(seedData.walletTransactions());
        this.walletAccount = seedData.walletAccount();
        this.arbitrations = new ArrayList<>(seedData.arbitrations());
        this.messageSessions = new ArrayList<>(seedData.messageSessions());
        this.messageItems = new ArrayList<>(seedData.messageItems());
        this.banners = new ArrayList<>(seedData.banners());
        this.notices = new ArrayList<>(seedData.notices());
    }

    public List<User> users() { return users; }
    public List<MasterProfile> masters() { return masters; }
    public List<Address> addresses() { return addresses; }
    public List<ServiceCategory> categories() { return categories; }
    public List<Product> products() { return products; }
    public List<SearchDocument> searchDocuments() { return searchDocuments; }
    public Map<String, ServiceOrder> serviceOrders() { return serviceOrders; }
    public Map<String, ProductOrder> productOrders() { return productOrders; }
    public Map<String, DispatchTask> dispatchTasks() { return dispatchTasks; }
    public List<Coupon> coupons() { return coupons; }
    public MemberLevel memberLevel() { return memberLevel; }
    public List<WalletTransaction> walletTransactions() { return walletTransactions; }
    public WalletAccount walletAccount() { return walletAccount; }
    public List<ArbitrationCase> arbitrations() { return arbitrations; }
    public List<MessageSession> messageSessions() { return messageSessions; }
    public List<MessageItem> messageItems() { return messageItems; }
    public List<Banner> banners() { return banners; }
    public List<Notice> notices() { return notices; }
}
