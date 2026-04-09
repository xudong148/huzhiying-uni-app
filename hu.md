# 呼之应三端产品需求基线

> 说明  
> 1. 本文档为当前仓库的实现基线。  
> 2. 完成定义限定为“平台内闭环”，不接入真实微信商户、真实地图厂商、外部 IM 或 Elasticsearch。  
> 3. 所有端均以同一套 Spring Boot 接口为契约来源。  

## 1. 产品定位

呼之应是一套覆盖 C 端用户、B 端师傅、Admin 后台的本地生活服务平台，核心能力包括：

- 服务类业务：维修、安装、清洗、保洁等到家服务
- 商品类业务：商品购买、发货、安装工单联动
- 履约协同：下单、派单、抢单、签到、施工、报价、补款、完工
- 客服与仲裁：订单沟通、售后、退款、客服补偿、仲裁处理
- 营销会员：优惠券、会员等级、权益展示

## 2. 技术架构

### 2.1 前端

- `uni-app`：承载 C 端和师傅端
- `admin`：Vue 3 + Element Plus 管理后台

### 2.2 后端

- `java/hu-server`：Spring Boot 主应用
- 数据层：MySQL / H2、JPA、Flyway
- 实时能力：WebSocket
- 文档能力：OpenAPI + Knife4j + Markdown 导出

### 2.3 本地运行基线

- 默认开发文档基线：`test` profile + H2
- 启动命令：

```powershell
cd f:\呼之应-uni-app
powershell -ExecutionPolicy Bypass -File .\scripts\start-hu-server.ps1 -Profile test
```

## 3. 角色定义

### 3.1 用户端

- 浏览首页与搜索
- 查看服务和商品详情
- 下单、支付、催单、取消、退款
- 查看订单履约轨迹
- 在线聊天
- 维护地址、资料、优惠券、会员信息
- 提交师傅入驻申请

### 3.2 师傅端

- 抢单与接单
- 工作台查看当前工单
- 状态推进：出发、到场、施工、完工
- 上传施工前后媒体
- 提交报价
- 查看钱包与结算
- 管理接单设置、听单开关、接单距离、隐私号

### 3.3 Admin 后台

- 仪表盘
- 调度中心
- 订单管理
- 师傅管理
- 财务结算
- 仲裁中心
- 内容运营
- 营销会员
- 系统权限
- 类目、SKU、定价、服务区域等配置 CRUD

## 4. 业务主链路

### 4.1 服务订单

1. 用户浏览服务详情并下单  
2. 支付预付款  
3. 平台派单或师傅抢单  
4. 师傅出发、签到、施工  
5. 如有增项，提交报价并等待补款  
6. 用户补款后继续履约  
7. 师傅完工上传凭证  
8. 平台生成结算记录  

### 4.2 商品订单

1. 用户浏览商品详情并下单  
2. 支付成功  
3. 商品进入待发货  
4. 如果商品需要安装，自动生成安装工单  
5. 安装工单走服务订单履约链路  

### 4.3 售后与仲裁

1. 用户发起退款或售后  
2. 后台查看订单、聊天、轨迹、媒体摘要  
3. 客服可发券、改预约、处理退款  
4. 如有争议，进入仲裁中心  

## 5. 三端页面范围

### 5.1 C 端页面

- 首页
- 分类页
- 搜索页
- 服务/商品详情
- 全部评价
- 下单页
- 订单列表
- 订单详情
- 订单轨迹
- 售后申请
- 在线聊天
- 地址管理
- 地址编辑
- 个人资料
- 登录页
- 优惠券中心
- 师傅入驻页

### 5.2 师傅端页面

- 抢派单大厅
- 履约工作台
- 师傅钱包
- 接单设置

### 5.3 后台页面

- 仪表盘
- 订单调度中心
- 订单管理
- 师傅管理
- 定价与类目
- 财务结算
- 仲裁中心
- 优惠券与会员
- 内容运营
- 系统权限

## 6. 后端接口分组

- `/api/auth`
- `/api/users`
- `/api/addresses`
- `/api/categories`
- `/api/services`
- `/api/products`
- `/api/search`
- `/api/home`
- `/api/service-orders`
- `/api/product-orders`
- `/api/quotations`
- `/api/master`
- `/api/dispatch`
- `/api/messages`
- `/api/notifications`
- `/api/admin/*`
- `/api/payments/wechat/*`

## 7. 文档出口

### 7.1 在线接口文档

- `http://localhost:8080/doc.html`
- `http://localhost:8080/v3/api-docs`
- `http://localhost:8080/v3/api-docs/mobile`
- `http://localhost:8080/v3/api-docs/master`
- `http://localhost:8080/v3/api-docs/admin`
- `http://localhost:8080/v3/api-docs/payment`

### 7.2 Markdown 文档

- `docs/api/README.md`
- `docs/api/all.md`
- `docs/api/mobile.md`
- `docs/api/master.md`
- `docs/api/admin.md`
- `docs/api/payment.md`

## 8. 当前完成定义

### 8.1 已完成

- 三端运行时 mock 已移除
- 运行时不再依赖演示账号、示例身份或默认示例姓名
- OpenAPI / Knife4j / Markdown 文档链路已打通
- 后台配置 CRUD 已接真实接口
- 后台 5 个业务页已具备详情与动作接口
- 微信支付不再伪造成功参数
- 订单、报价、补款、退款、媒体、轨迹、聊天均走平台内真实接口

### 8.2 未纳入本阶段完成定义

- 真实微信商户支付
- 真实地图 SDK 与真实导航
- 外部 IM 平台
- Elasticsearch 正式联调
- 第三方客服平台
- `test` profile 保留最小 H2 夹具，仅用于联调与文档运行

## 9. 验收要求

### 9.1 后端

- `mvn test` 通过
- `/doc.html` 可访问
- `/v3/api-docs/*` 可导入 Apifox / Apiform

### 9.2 后台

- `npm run build` 通过
- 调度、订单、师傅、财务、仲裁、配置中心具备真实列表、详情、动作、刷新闭环

### 9.3 uni-app

- 路由页存在且标题正常
- 首页、搜索、详情、下单、订单详情、聊天、轨迹、师傅抢单、工作台、钱包、设置可冒烟

### 9.4 联调稳定性补充要求

- `uni-app` 必须支持在运行时修改后端地址，默认展示当前联调地址
- 非 H5 端如果后端地址仍为 `localhost / 127.0.0.1`，页面必须明确提示改成电脑局域网 IP
- 页面生命周期中的异步请求失败时，不得产生未处理 Promise 异常
- 微信支付未配置、当前端不支持或参数不完整时，必须给出可理解的失败提示
- Java 多模块工程的测试与启动以 `java` 根目录聚合执行为准，避免 `hu-server` 单模块陈旧依赖导致运行时错误
