<!--
  师傅端接口文档，由 scripts/export-openapi-markdown.mjs 自动生成。
  1. OpenAPI 来源：http://localhost:8080/v3/api-docs/master
  2. 下文展开展示参数、请求体和响应字段，便于联调查阅。
  3. 接口契约变更后请重新执行导出脚本。
-->

# 师傅端接口文档

## 基本信息

- 文档标题：`呼之应开放接口文档`
- 版本：`v1`
- 分组：`master`
- 接口数量：`29`
- OpenAPI 地址：`http://localhost:8080/v3/api-docs/master`

## 鉴权说明

- `bearerAuth`, 类型: http, 方案: bearer, 格式: JWT

## 接口目录

- [GET /api/dispatch/tasks](#get-api-dispatch-tasks)
- [POST /api/dispatch/tasks/{id}/claim](#post-api-dispatch-tasks-id-claim)
- [POST /api/dispatch/tasks/{id}/force-assign](#post-api-dispatch-tasks-id-force-assign)
- [GET /api/files/{id}](#get-api-files-id)
- [GET /api/files/{id}/content](#get-api-files-id-content)
- [POST /api/files/upload](#post-api-files-upload)
- [POST /api/master/apply](#post-api-master-apply)
- [GET /api/master/dashboard](#get-api-master-dashboard)
- [POST /api/master/orders/{id}/after-work-media](#post-api-master-orders-id-after-work-media)
- [POST /api/master/orders/{id}/before-work-media](#post-api-master-orders-id-before-work-media)
- [POST /api/master/orders/{id}/check-in](#post-api-master-orders-id-check-in)
- [GET /api/master/settings](#get-api-master-settings)
- [POST /api/master/settings](#post-api-master-settings)
- [GET /api/master/wallet](#get-api-master-wallet)
- [GET /api/messages/{sessionId}/items](#get-api-messages-sessionid-items)
- [POST /api/messages/{sessionId}/items](#post-api-messages-sessionid-items)
- [POST /api/messages/{sessionId}/read](#post-api-messages-sessionid-read)
- [GET /api/messages/sessions](#get-api-messages-sessions)
- [GET /api/notifications](#get-api-notifications)
- [POST /api/orders/{id}/cancel](#post-api-orders-id-cancel)
- [GET /api/orders/{id}/tracking](#get-api-orders-id-tracking)
- [POST /api/orders/{id}/urge](#post-api-orders-id-urge)
- [POST /api/quotations](#post-api-quotations)
- [POST /api/quotations/{quotationId}/confirm](#post-api-quotations-quotationid-confirm)
- [GET /api/service-orders](#get-api-service-orders)
- [POST /api/service-orders](#post-api-service-orders)
- [GET /api/service-orders/{id}](#get-api-service-orders-id)
- [POST /api/service-orders/{id}/status](#post-api-service-orders-id-status)
- [GET /api/service-orders/time-slots](#get-api-service-orders-time-slots)

## GET /api/dispatch/tasks

- 摘要：查询派单任务
- 标签：dispatch

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/dispatch/tasks` |
| OperationId | `tasks` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

- 当前接口没有显式参数。

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

## POST /api/dispatch/tasks/{id}/claim

- 摘要：师傅抢单
- 标签：dispatch

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/dispatch/tasks/{id}/claim` |
| OperationId | `claim` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `string` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `masterName` | `string` | 否 | - |

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

## POST /api/dispatch/tasks/{id}/force-assign

- 摘要：后台强派
- 标签：dispatch

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/dispatch/tasks/{id}/force-assign` |
| OperationId | `forceAssign` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `string` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `masterName` | `string` | 否 | - |

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

## GET /api/files/{id}

- 摘要：查询文件元数据
- 标签：file

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/files/{id}` |
| OperationId | `file` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

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

## GET /api/files/{id}/content

- 摘要：下载文件内容
- 标签：file

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/files/{id}/content` |
| OperationId | `fileContent` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

### 请求体

- 当前接口没有请求体。

### 响应说明

#### 响应 `200`

- 说明：OK

- Content-Type: `*/*`

#### 响应字段

- 当前结构没有可展开字段。

## POST /api/files/upload

- 摘要：上传文件
- 标签：file

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/files/upload` |
| OperationId | `upload` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `bizType` | query | 否 | `string` | - |
| `bizId` | query | 否 | `string` | - |

### 请求体

#### Content-Type: `multipart/form-data`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `file` | `string` | 是 | - |

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

## POST /api/master/apply

- 摘要：提交师傅入驻申请
- 标签：master

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/master/apply` |
| OperationId | `apply` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

- 当前接口没有显式参数。

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `realName` | `string` | 否 | - |
| `mobile` | `string` | 否 | - |
| `skills` | `string` | 否 | - |
| `area` | `string` | 否 | - |

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

## GET /api/master/dashboard

- 摘要：查询师傅工作台
- 标签：master

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/master/dashboard` |
| OperationId | `dashboard` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

- 当前接口没有显式参数。

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

## POST /api/master/orders/{id}/after-work-media

- 摘要：上传完工媒体
- 标签：master

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/master/orders/{id}/after-work-media` |
| OperationId | `afterWorkMedia` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `string` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `fileIds` | `array<integer>` | 否 | - |
| `fileIds` | `array<integer>` | 否 | - |
| `note` | `string` | 否 | 补充说明 |

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

## POST /api/master/orders/{id}/before-work-media

- 摘要：上传施工前媒体
- 标签：master

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/master/orders/{id}/before-work-media` |
| OperationId | `beforeWorkMedia` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `string` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `fileIds` | `array<integer>` | 否 | - |
| `fileIds` | `array<integer>` | 否 | - |
| `note` | `string` | 否 | 补充说明 |

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

## POST /api/master/orders/{id}/check-in

- 摘要：师傅到场签到
- 标签：master

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/master/orders/{id}/check-in` |
| OperationId | `checkIn` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `string` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `latitude` | `number` | 否 | 纬度 |
| `longitude` | `number` | 否 | 经度 |
| `accuracy` | `number` | 否 | 定位精度，单位米 |

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

## GET /api/master/settings

- 摘要：查询师傅设置
- 标签：master

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/master/settings` |
| OperationId | `settings` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

- 当前接口没有显式参数。

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

## POST /api/master/settings

- 摘要：保存师傅设置
- 标签：master

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/master/settings` |
| OperationId | `saveSettings` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

- 当前接口没有显式参数。

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `listening` | `boolean` | 否 | - |
| `maxDistance` | `string` | 否 | - |
| `privacyNumber` | `boolean` | 否 | - |

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

## GET /api/master/wallet

- 摘要：查询师傅钱包
- 标签：master

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/master/wallet` |
| OperationId | `wallet` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

- 当前接口没有显式参数。

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

## GET /api/messages/{sessionId}/items

- 摘要：查询聊天消息
- 标签：master

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/messages/{sessionId}/items` |
| OperationId | `items` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `sessionId` | path | 是 | `string` | - |

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

## POST /api/messages/{sessionId}/items

- 摘要：发送聊天消息
- 标签：master

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/messages/{sessionId}/items` |
| OperationId | `send` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `sessionId` | path | 是 | `string` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `senderCode` | `string` | 否 | 发送方编码 |
| `messageType` | `string` | 否 | 消息类型 |
| `content` | `string` | 否 | 消息内容，文本消息为正文，图片或语音消息为文件 URL |

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

## POST /api/messages/{sessionId}/read

- 摘要：回写会话已读状态
- 标签：master

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/messages/{sessionId}/read` |
| OperationId | `markRead` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `sessionId` | path | 是 | `string` | - |

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

## GET /api/messages/sessions

- 摘要：查询聊天会话
- 标签：master

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/messages/sessions` |
| OperationId | `sessions` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

- 当前接口没有显式参数。

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

## GET /api/notifications

- 摘要：查询通知中心
- 标签：master

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/notifications` |
| OperationId | `notifications` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

- 当前接口没有显式参数。

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

## POST /api/orders/{id}/cancel

- 摘要：取消订单
- 标签：mobile-order

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/orders/{id}/cancel` |
| OperationId | `cancel` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `string` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `reason` | `string` | 否 | 取消原因 |

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

## GET /api/orders/{id}/tracking

- 摘要：查询订单轨迹
- 标签：mobile-order

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/orders/{id}/tracking` |
| OperationId | `tracking` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `string` | - |

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

## POST /api/orders/{id}/urge

- 摘要：催单
- 标签：mobile-order

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/orders/{id}/urge` |
| OperationId | `urge` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `string` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `remark` | `string` | 否 | 催单备注 |

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

## POST /api/quotations

- 摘要：创建增项报价
- 标签：mobile-order

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/quotations` |
| OperationId | `createQuotation` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

- 当前接口没有显式参数。

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `orderId` | `string` | 否 | - |
| `remark` | `string` | 否 | - |

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

## POST /api/quotations/{quotationId}/confirm

- 摘要：确认增项报价
- 标签：mobile-order

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/quotations/{quotationId}/confirm` |
| OperationId | `confirmQuotation` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `quotationId` | path | 是 | `string` | - |

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

## GET /api/service-orders

- 摘要：查询服务订单列表
- 标签：mobile-order

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/service-orders` |
| OperationId | `serviceOrders` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

- 当前接口没有显式参数。

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

## POST /api/service-orders

- 摘要：创建服务订单
- 标签：mobile-order

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/service-orders` |
| OperationId | `createServiceOrder` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

- 当前接口没有显式参数。

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `serviceItemId` | `integer` | 否 | - |
| `title` | `string` | 否 | - |
| `appointment` | `string` | 否 | - |
| `addressId` | `integer` | 否 | - |
| `description` | `string` | 否 | - |
| `emergency` | `boolean` | 否 | - |
| `nightService` | `boolean` | 否 | - |
| `evidenceFileIds` | `array<integer>` | 否 | - |
| `evidenceFileIds` | `array<integer>` | 否 | - |

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

## GET /api/service-orders/{id}

- 摘要：查询服务订单详情
- 标签：mobile-order

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/service-orders/{id}` |
| OperationId | `serviceOrder` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `string` | - |

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

## POST /api/service-orders/{id}/status

- 摘要：更新服务订单状态
- 标签：mobile-order

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/service-orders/{id}/status` |
| OperationId | `updateServiceOrderStatus` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `string` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `status` | `string` | 否 | - |

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

## GET /api/service-orders/time-slots

- 摘要：查询预约时段列表
- 标签：map

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/service-orders/time-slots` |
| OperationId | `timeSlots` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

- 当前接口没有显式参数。

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
| `data` | `array<TimeSlotPayload>` | 否 | - |
| `data` | `array<TimeSlotPayload>` | 否 | - |
| `data[].value` | `string` | 否 | 时段值 |
