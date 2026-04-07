package com.huzhiying.server.support;

import com.huzhiying.domain.enums.DomainEnums.DispatchMode;
import com.huzhiying.domain.enums.DomainEnums.PaymentStatus;
import com.huzhiying.domain.enums.DomainEnums.ProductOrderStatus;
import com.huzhiying.domain.enums.DomainEnums.QuotationStatus;
import com.huzhiying.domain.enums.DomainEnums.RoleCode;
import com.huzhiying.domain.enums.DomainEnums.ServiceOrderStatus;
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
import com.huzhiying.domain.model.DomainModels.Quotation;
import com.huzhiying.domain.model.DomainModels.QuotationItem;
import com.huzhiying.domain.model.DomainModels.SearchDocument;
import com.huzhiying.domain.model.DomainModels.ServiceCategory;
import com.huzhiying.domain.model.DomainModels.ServiceItem;
import com.huzhiying.domain.model.DomainModels.ServiceOrder;
import com.huzhiying.domain.model.DomainModels.Sku;
import com.huzhiying.domain.model.DomainModels.User;
import com.huzhiying.domain.model.DomainModels.WalletAccount;
import com.huzhiying.domain.model.DomainModels.WalletTransaction;
import com.huzhiying.domain.model.DomainModels.WorkStepRecord;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class DemoDataFactory {

    private DemoDataFactory() {}

    public static SeedData seed() {
        Address defaultAddress = new Address(1L, "家", "周女士", "138****5288",
                "上海市浦东新区张江高科园区 88 号 2 幢 1602", 31.2253, 121.5443, true);

        ServiceItem airRepair = new ServiceItem(201L, 2L, "空调不制冷上门维修", "基础检测 + 故障排查",
                BigDecimal.valueOf(58), BigDecimal.valueOf(30),
                List.of("不修不收", "迟到赔", "90 天质保"), List.of("空调维修", "快速上门"));
        ServiceItem lockInstall = new ServiceItem(301L, 3L, "智能锁标准安装", "含调试联网",
                BigDecimal.valueOf(128), BigDecimal.ZERO,
                List.of("品牌认证", "安装质保"), List.of("安装服务"));
        ServiceItem cleanService = new ServiceItem(401L, 4L, "日常保洁 3 小时", "厨房 / 卫生间 / 客厅",
                BigDecimal.valueOf(135), BigDecimal.ZERO,
                List.of("自带工具", "平台背书"), List.of("保洁", "高频服务"));

        List<ServiceCategory> categories = List.of(
                new ServiceCategory(2L, "专业维修", "/static/icons/screwdriver.svg", List.of(airRepair)),
                new ServiceCategory(3L, "上门安装", "/static/icons/installation.svg", List.of(lockInstall)),
                new ServiceCategory(4L, "保洁收纳", "/static/icons/cleaning.svg", List.of(cleanService))
        );

        List<Product> products = List.of(
                new Product(1001L, "智能锁 Pro 套装", "含安装工单联动", BigDecimal.valueOf(1699),
                        List.of(new Sku(1L, 1001L, "黑色标准款", BigDecimal.valueOf(1699), 36)), true),
                new Product(1002L, "空调清洗年卡", "全年 2 次深度清洗", BigDecimal.valueOf(499),
                        List.of(new Sku(2L, 1002L, "家庭版", BigDecimal.valueOf(499), 88)), false)
        );

        List<SearchDocument> searchDocuments = List.of(
                new SearchDocument("service-201", "service", airRepair.name(), airRepair.subtitle(), airRepair.basePrice(), "/static/icons/screwdriver.svg"),
                new SearchDocument("service-301", "service", lockInstall.name(), lockInstall.subtitle(), lockInstall.basePrice(), "/static/icons/installation.svg"),
                new SearchDocument("product-1001", "product", "智能锁 Pro 套装", "支持购买后自动生成安装工单", BigDecimal.valueOf(1699), "/static/icons/mall.svg")
        );

        Quotation quotation = new Quotation("QT20260408001", "SO20260407009",
                List.of(
                        new QuotationItem("锁体加固件", BigDecimal.valueOf(70)),
                        new QuotationItem("门体加厚打磨", BigDecimal.valueOf(100))
                ),
                BigDecimal.valueOf(170),
                QuotationStatus.PENDING_CONFIRM,
                "现场门体厚度超出标准安装范围，需新增配件和工时。");

        List<WorkStepRecord> timeline = List.of(
                step("created", "订单创建", "用户已完成预付款并提交故障描述", true),
                step("dispatch", "派单中", "系统已推送给 20km 内匹配师傅", true),
                step("accepted", "师傅接单", "张师傅已接单并准备出发", true),
                step("arrival", "上门检测", "等待师傅到达并提交检测结果", false),
                step("finish", "服务完成", "完工后生成电子服务报告", false)
        );

        Map<String, ServiceOrder> serviceOrders = new LinkedHashMap<>();
        serviceOrders.put("SO20260408001", new ServiceOrder("SO20260408001", airRepair.name(),
                ServiceOrderStatus.PENDING_ACCEPT, PaymentStatus.PARTIAL_PAID,
                "周女士", "待接单", "今天 14:00-16:00", defaultAddress,
                BigDecimal.valueOf(88), DispatchMode.ROB, timeline, null, "26 分钟"));
        serviceOrders.put("SO20260407009", new ServiceOrder("SO20260407009", lockInstall.name(),
                ServiceOrderStatus.WAITING_SUPPLEMENT_PAYMENT, PaymentStatus.PARTIAL_PAID,
                "周女士", "张师傅", "明天 09:00-11:00", defaultAddress,
                BigDecimal.valueOf(258), DispatchMode.FORCE_ASSIGN, timeline, quotation, "15 分钟"));

        Map<String, ProductOrder> productOrders = new LinkedHashMap<>();
        productOrders.put("PO20260406018", new ProductOrder("PO20260406018", "智能锁 Pro 套装",
                ProductOrderStatus.PENDING_SHIPMENT, PaymentStatus.PAID,
                "林先生", defaultAddress, BigDecimal.valueOf(1699), true));

        Map<String, DispatchTask> dispatchTasks = new LinkedHashMap<>();
        dispatchTasks.put("DISP-001", new DispatchTask("DISP-001", "SO20260408001", airRepair.name(),
                BigDecimal.valueOf(168), "3.2km", "浦东新区", DispatchMode.ROB, "待接单",
                List.of("空调维修", "预计 60 分钟")));
        dispatchTasks.put("DISP-002", new DispatchTask("DISP-002", "SO20260407009", lockInstall.name(),
                BigDecimal.valueOf(198), "5.6km", "徐汇区", DispatchMode.FORCE_ASSIGN, "张师傅",
                List.of("安装服务", "配件自带")));

        return new SeedData(
                List.of(new User(10001L, "周女士", "138****5288", RoleCode.USER, "/static/user.png", "SVIP 预备用户")),
                List.of(new MasterProfile(20001L, "张师傅", "空调维修 / 智能锁安装", "浦东新区 / 徐汇区", BigDecimal.valueOf(3000), 98, true)),
                List.of(defaultAddress),
                categories,
                products,
                searchDocuments,
                serviceOrders,
                productOrders,
                dispatchTasks,
                List.of(new Coupon(1L, "新用户上门立减券", BigDecimal.valueOf(30), "满 99 减 30", "2026-04-30")),
                new MemberLevel("SVIP 金卡", "服务 9 折 / 专属客服 / 优先派单", 2000),
                List.of(new WalletTransaction(1L, "SO20260406018 智能锁安装结算", BigDecimal.valueOf(268), "今天 10:18")),
                new WalletAccount(BigDecimal.valueOf(12860), BigDecimal.valueOf(3000), BigDecimal.valueOf(860)),
                List.of(new ArbitrationCase("ARB-001", "SO20260407009", "乱收费", "待裁决")),
                List.of(new MessageSession("MS-001", "SO20260407009", "智能锁安装沟通", "张师傅")),
                List.of(new MessageItem(1L, "MS-001", "system", "system", "订单已分配给张师傅，预计 26 分钟到达。", "14:02")),
                List.of(new Banner(1L, "家电维修专场", "不修不收，90 天质保", "banner-1")),
                List.of(new Notice(1L, "夜间服务费：22:00 后自动加收 30%", "warning"))
        );
    }

    private static WorkStepRecord step(String key, String label, String desc, boolean done) {
        return new WorkStepRecord(key, label, desc, done, LocalDateTime.now());
    }

    public record SeedData(
            List<User> users,
            List<MasterProfile> masters,
            List<Address> addresses,
            List<ServiceCategory> categories,
            List<Product> products,
            List<SearchDocument> searchDocuments,
            Map<String, ServiceOrder> serviceOrders,
            Map<String, ProductOrder> productOrders,
            Map<String, DispatchTask> dispatchTasks,
            List<Coupon> coupons,
            MemberLevel memberLevel,
            List<WalletTransaction> walletTransactions,
            WalletAccount walletAccount,
            List<ArbitrationCase> arbitrations,
            List<MessageSession> messageSessions,
            List<MessageItem> messageItems,
            List<Banner> banners,
            List<Notice> notices
    ) {}
}
