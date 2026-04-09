# 呼之应领域与接口规格

## 1. 建模约定

### 1.1 统一字段规范

所有交易型实体必须统一具备以下字段语义：

| 字段 | 说明 |
| --- | --- |
| `id` / `bizNo` | 主键或业务编号，外部展示优先使用业务编号 |
| `status` | 当前业务状态，不允许页面自行推断 |
| `orderType` | `service` / `product` / `install` |
| `channel` | 创建来源或支付渠道 |
| `amount` | 当前金额或总金额，统一使用十进制定点数 |
| `userId` / `masterId` | 交易主体或履约主体 |
| `operatorId` | 最近一次人工写操作责任人 |
| `traceId` | 请求追踪 ID，用于串联前后端和审计 |
| `createdAt` / `updatedAt` | 创建和最近修改时间 |

### 1.2 文档与代码对应

- `hu-domain` 中的聚合和枚举是实现入口，不代表规格完备。
- 本文定义的实体若尚未全部代码化，仍视为正式规格，后续实现需补齐。

## 2. 核心实体

### 2.1 已有核心聚合

- `User`
- `Address`
- `MasterProfile`
- `ServiceCategory`
- `ServiceItem`
- `Product`
- `Sku`
- `ServiceOrder`
- `ProductOrder`
- `DispatchTask`
- `Quotation`
- `MessageSession`
- `MessageItem`
- `MediaFile`
- `OrderTrackPoint`
- `AcademyCategory`
- `AcademyArticle`
- `CommunityPost`
- `CommunityComment`
- `CommunityReport`

### 2.2 正式补充的生产实体

| 实体 | 用途 | 最小关键字段 |
| --- | --- | --- |
| `PaymentRecord` | 记录预支付、补款、回调、渠道流水 | `bizNo` `orderId` `paymentStage` `channel` `status` `amount` `channelTxnNo` `traceId` |
| `RefundRequest` | 记录售后退款申请与审核 | `bizNo` `orderId` `paymentRecordId` `status` `reason` `requestedBy` `reviewedBy` `amount` |
| `SettlementBill` | 记录师傅结算与平台审核 | `bizNo` `orderId` `masterId` `status` `grossAmount` `serviceFee` `netAmount` `approvedBy` |
| `WalletLedger` | 钱包账务明细，替代仅展示型流水 | `bizNo` `masterId` `direction` `scene` `relatedBizNo` `amount` `balanceAfter` `status` |
| `AuditLog` | 记录关键写操作 | `bizNo` `bizType` `action` `operatorId` `operatorRole` `beforeState` `afterState` `traceId` |
| `NotificationTask` | 记录异步通知投递 | `bizNo` `scene` `targetType` `targetId` `status` `payloadDigest` `retryCount` |

## 3. 关系与索引

### 3.1 关系约束

- 一个 `ProductOrder` 可以关联零个或一个 `ServiceOrder` 安装工单。
- 一个 `ServiceOrder` 可以关联多个 `OrderTrackPoint`、多个 `MediaFile`、零个或一个 `Quotation`。
- 一个 `RefundRequest` 必须绑定一个订单和至少一个支付记录。
- 一个 `SettlementBill` 必须绑定一个已完成订单和一个师傅。
- 一个 `WalletLedger` 必须可以追溯到结算单、退款逆向冲减或平台人工调整。

### 3.2 索引要求

| 对象 | 索引 |
| --- | --- |
| 订单 | `(user_id, status, created_at)` `(master_user_id, status, updated_at)` |
| 派单任务 | `(city_name, task_status, created_at)` |
| 支付记录 | `(order_id, payment_stage)` `(channel_txn_no)` |
| 退款申请 | `(order_id, status)` `(payment_record_id)` |
| 结算单 | `(master_id, status, created_at)` |
| 钱包流水 | `(master_id, created_at)` `(related_biz_no)` |
| 圈子内容 | `(city_name, status_code, created_at)` |
| 审计日志 | `(biz_type, biz_no, created_at)` `(operator_id, created_at)` |

## 4. 状态机

### 4.1 服务订单

`待支付 -> 待派单 -> 待接单 -> 出发中 -> 已到场 -> 待补款 -> 施工中 -> 已完成`

异常分支：

- `待支付 -> 已取消`
- `待派单/待接单/出发中/已到场/待补款/施工中 -> 退款中`
- `退款中 -> 售后中`
- `任一未完工状态 -> 已取消` 仅在满足业务规则时允许

禁止：

- 未支付直接进入待接单
- 已取消或已完成再回到履约态
- 前端直接通过支付状态推断订单完成

### 4.2 商品订单

`待支付 -> 已支付 -> 待发货 -> 已发货 -> 已完成`

异常分支：

- `待支付/已支付/待发货/已发货 -> 退款中`

禁止：

- 未支付直接发货
- 已完成再进入发货态

### 4.3 支付记录

`INIT -> PREPAY_CREATED -> PAYING -> PAID -> REFUNDING -> REFUNDED`

禁止：

- 未经回调验签从 `PREPAY_CREATED` 直接到 `PAID`
- 已退款再回到已支付

### 4.4 报价单

`PENDING_CONFIRM -> CONFIRMED / REJECTED`

禁止：

- 已确认后再次修改金额
- 被拒绝的旧报价继续用于补款

### 4.5 结算单

`INIT -> PENDING_REVIEW -> APPROVED / REJECTED -> SETTLED`

禁止：

- 未审批直接写钱包入账
- 退款逆向冲减后不回写结算状态

### 4.6 派单任务

`OPEN -> CLAIMED / ASSIGNED -> EXPIRED / CANCELLED / CLOSED`

### 4.7 师傅在线状态

`OFFLINE <-> ONLINE`

附加接单条件：

- 听单开关
- 服务区域命中
- 信用分达标
- 未被禁用

### 4.8 圈子审核状态

`DRAFT -> PUBLISHED -> OFFLINE`

举报处理：

`PENDING -> REVIEWING -> RESOLVED / REJECTED`

## 5. 接口分组

### 5.1 移动端

- 账号与身份：`/api/auth/**` `/api/users/**`
- 地址与地理：`/api/addresses/**` `/api/map/**`
- 首页与搜索：`/api/home` `/api/categories/**` `/api/search/**`
- 交易：`/api/services/**` `/api/products/**` `/api/service-orders/**` `/api/product-orders/**` `/api/orders/**` `/api/quotations/**`
- 内容：`/api/academy/**` `/api/community/**`
- 支持能力：`/api/messages/**` `/api/files/**` `/api/coupons` `/api/members/**`

### 5.2 师傅端

- 履约：`/api/master/**` `/api/dispatch/**`
- 工单能力：`/api/service-orders/**` `/api/orders/**` `/api/quotations/**`
- 消息与文件：`/api/messages/**` `/api/files/**`
- 通知：`/api/notifications/**`

### 5.3 后台端

- 运营后台统一走 `/api/admin/**`
- 子域分组：
  - `catalog`
  - `pricing`
  - `orders`
  - `dispatch`
  - `finance`
  - `arbitrations`
  - `marketing`
  - `content`
  - `system`

### 5.4 当前必须纳入导出文档的内容接口

- 移动端：
  - `/api/academy/categories`
  - `/api/academy/articles`
  - `/api/academy/articles/{id}`
  - `/api/community/posts`
  - `/api/community/posts/{id}`
  - `/api/community/posts/{id}/comments`
  - `/api/community/posts/{id}/like`
  - `/api/community/posts/{id}/report`
- 后台端：
  - `/api/admin/content/ecosystem-cards`
  - `/api/admin/content/academy-categories`
  - `/api/admin/content/academy-articles`
  - `/api/admin/content/community-posts`
  - `/api/admin/content/community-reports`

## 6. 错误码与提示

| 错误码 | 场景 | 用户提示方向 |
| --- | --- | --- |
| `AUTH_EXPIRED` | 身份过期 | 请重新登录后继续 |
| `AUTH_FORBIDDEN` | 角色越权 | 当前身份无权执行该操作 |
| `ADDRESS_OUT_OF_RANGE` | 地址超服务范围 | 当前地址暂未开通服务 |
| `ORDER_STATUS_INVALID` | 非法状态跳转 | 当前订单状态不支持此操作 |
| `PAYMENT_UNAVAILABLE` | 当前环境不支持支付 | 请更换支持支付的渠道或稍后重试 |
| `PAYMENT_CALLBACK_DUPLICATED` | 重复回调 | 系统已处理，请勿重复提交 |
| `REFUND_IN_PROGRESS` | 退款处理中 | 当前退款申请正在审核 |
| `UPLOAD_FAILED` | 文件上传失败 | 上传未成功，可重试或稍后补发 |
| `WEAK_NETWORK_QUEUED` | 弱网离线动作 | 已记录操作，网络恢复后自动补发 |
| `CONTENT_REJECTED` | 内容审核不通过 | 内容未通过审核，请修改后重新提交 |

## 7. 审计规则

以下动作必须写 `AuditLog`：

- 订单创建、改约、取消、完工
- 支付回调确认、退款审核、结算审核
- 派单、抢单、师傅禁用/启用
- 优惠券补偿、仲裁处理
- Banner、学堂、圈子内容发布和下架
- 角色、权限、菜单变更

审计日志至少包含：

- 操作者身份和角色
- 业务对象和编号
- 动作名称
- 变更前后摘要
- 请求来源、渠道、Trace ID
- 成功/失败结果

## 8. 文档同步规则

1. 新增或修改接口：
   - 先改后端 DTO / Controller / OpenAPI 分组
   - 再补集成测试
   - 最后重新导出 `../api/*`
2. 新增或修改状态机：
   - 先改本文状态机
   - 再改 `DomainEnums` / 服务层 / 页面文案
3. 新增页面或后台模块：
   - 先改 `PRODUCT-SPEC.md` 页面矩阵
   - 再改路由、页面和上线审计
