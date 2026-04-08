<!--
  支付接口文档，由 scripts/export-openapi-markdown.mjs 自动生成。
  1. OpenAPI 来源：http://localhost:8080/v3/api-docs/payment
  2. 下文展开展示参数、请求体和响应字段，便于联调查阅。
  3. 接口契约变更后请重新执行导出脚本。
-->

# 支付接口文档

## 基本信息

- 文档标题：`呼之应开放接口文档`
- 版本：`v1`
- 分组：`payment`
- 接口数量：`3`
- OpenAPI 地址：`http://localhost:8080/v3/api-docs/payment`

## 鉴权说明

- `bearerAuth`, 类型: http, 方案: bearer, 格式: JWT

## 接口目录

- [POST /api/payments/wechat/{orderId}/callback](#post-api-payments-wechat-orderid-callback)
- [POST /api/payments/wechat/prepay](#post-api-payments-wechat-prepay)
- [POST /api/payments/wechat/refund](#post-api-payments-wechat-refund)

## POST /api/payments/wechat/{orderId}/callback

- 摘要：接收微信支付回调
- 标签：payment

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/payments/wechat/{orderId}/callback` |
| OperationId | `callback` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `orderId` | path | 是 | `string` | - |

### 请求体

- 当前接口没有请求体。

### 响应说明

#### 响应 `200`

- 说明：OK

- Content-Type: `*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `object` | 否 | - |
| `data` | `object` | 否 | - |

## POST /api/payments/wechat/prepay

- 摘要：创建微信预支付单
- 标签：payment

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/payments/wechat/prepay` |
| OperationId | `prepay` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

- 当前接口没有显式参数。

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `orderId` | `string` | 否 | - |

### 响应说明

#### 响应 `200`

- 说明：OK

- Content-Type: `*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `WechatPrepayPayload` | 否 | - |
| `data.orderId` | `string` | 否 | 订单号 |
| `data.appId` | `string` | 否 | 微信应用 ID |
| `data.timeStamp` | `string` | 否 | 时间戳 |
| `data.nonceStr` | `string` | 否 | 随机串 |
| `data.packageValue` | `string` | 否 | 微信支付 package 字段 |
| `data.signType` | `string` | 否 | 签名算法 |
| `data.paySign` | `string` | 否 | 支付签名 |
| `data.payEnabled` | `boolean` | 否 | 是否已启用真实支付能力 |
| `data.message` | `string` | 否 | 提示文案 |

## POST /api/payments/wechat/refund

- 摘要：发起微信退款
- 标签：payment

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/payments/wechat/refund` |
| OperationId | `refund` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

- 当前接口没有显式参数。

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `orderId` | `string` | 否 | - |

### 响应说明

#### 响应 `200`

- 说明：OK

- Content-Type: `*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `object` | 否 | - |
| `data` | `object` | 否 | - |
