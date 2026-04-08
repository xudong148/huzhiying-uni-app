<!--
  文档说明：
  1. 当前文件为 师傅端接口文档，由 scripts/export-openapi-markdown.mjs 自动生成。
  2. 数据源地址：http://localhost:8080/v3/api-docs/master。
  3. 字段表会展开请求参数、请求体和响应体，便于前后端对照联调。
-->

# 师傅端接口文档

## 基本信息

- 文档标题：`呼之应开放接口`
- 文档版本：`v1`
- 分组：`master`
- 接口数量：`12`
- OpenAPI 地址：`http://localhost:8080/v3/api-docs/master`

## 鉴权约定

- `bearerAuth`，类型：http，协议：bearer，格式：JWT

## 接口目录

- [GET /api/dispatch/tasks](#get-api-dispatch-tasks)
- [POST /api/dispatch/tasks/{id}/claim](#post-api-dispatch-tasks-id-claim)
- [POST /api/dispatch/tasks/{id}/force-assign](#post-api-dispatch-tasks-id-force-assign)
- [POST /api/master/apply](#post-api-master-apply)
- [GET /api/master/dashboard](#get-api-master-dashboard)
- [POST /api/master/orders/{id}/after-work-media](#post-api-master-orders-id-after-work-media)
- [POST /api/master/orders/{id}/before-work-media](#post-api-master-orders-id-before-work-media)
- [POST /api/master/orders/{id}/check-in](#post-api-master-orders-id-check-in)
- [GET /api/master/settings](#get-api-master-settings)
- [POST /api/master/settings](#post-api-master-settings)
- [GET /api/master/wallet](#get-api-master-wallet)
- [GET /api/notifications](#get-api-notifications)

## GET /api/dispatch/tasks

- 标题：查询派单任务
- 标签：dispatch

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `GET` |
| 路径 | `/api/dispatch/tasks` |
| OperationId | `tasks` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

- 无显式参数。

### 请求体

- 无请求体。

### 响应体

#### 响应 `200`

- 说明：OK

- Content-Type：`*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `object` | 否 | - |
| `data` | `object` | 否 | - |

## POST /api/dispatch/tasks/{id}/claim

- 标题：师傅抢单
- 标签：dispatch

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `POST` |
| 路径 | `/api/dispatch/tasks/{id}/claim` |
| OperationId | `claim` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `string` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `masterName` | `string` | 否 | - |

### 响应体

#### 响应 `200`

- 说明：OK

- Content-Type：`*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `object` | 否 | - |
| `data` | `object` | 否 | - |

## POST /api/dispatch/tasks/{id}/force-assign

- 标题：后台强派
- 标签：dispatch

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `POST` |
| 路径 | `/api/dispatch/tasks/{id}/force-assign` |
| OperationId | `forceAssign` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `string` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `masterName` | `string` | 否 | - |

### 响应体

#### 响应 `200`

- 说明：OK

- Content-Type：`*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `object` | 否 | - |
| `data` | `object` | 否 | - |

## POST /api/master/apply

- 标题：提交师傅入驻申请
- 标签：master

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `POST` |
| 路径 | `/api/master/apply` |
| OperationId | `apply` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

- 无显式参数。

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `realName` | `string` | 否 | - |
| `mobile` | `string` | 否 | - |
| `skills` | `string` | 否 | - |
| `area` | `string` | 否 | - |

### 响应体

#### 响应 `200`

- 说明：OK

- Content-Type：`*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `object` | 否 | - |
| `data` | `object` | 否 | - |

## GET /api/master/dashboard

- 标题：查询师傅工作台
- 标签：master

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `GET` |
| 路径 | `/api/master/dashboard` |
| OperationId | `dashboard` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

- 无显式参数。

### 请求体

- 无请求体。

### 响应体

#### 响应 `200`

- 说明：OK

- Content-Type：`*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `object` | 否 | - |
| `data` | `object` | 否 | - |

## POST /api/master/orders/{id}/after-work-media

- 标题：上传完工媒体
- 标签：master

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `POST` |
| 路径 | `/api/master/orders/{id}/after-work-media` |
| OperationId | `afterWorkMedia` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
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

### 响应体

#### 响应 `200`

- 说明：OK

- Content-Type：`*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `object` | 否 | - |
| `data` | `object` | 否 | - |

## POST /api/master/orders/{id}/before-work-media

- 标题：上传施工前媒体
- 标签：master

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `POST` |
| 路径 | `/api/master/orders/{id}/before-work-media` |
| OperationId | `beforeWorkMedia` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
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

### 响应体

#### 响应 `200`

- 说明：OK

- Content-Type：`*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `object` | 否 | - |
| `data` | `object` | 否 | - |

## POST /api/master/orders/{id}/check-in

- 标题：师傅到场签到
- 标签：master

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `POST` |
| 路径 | `/api/master/orders/{id}/check-in` |
| OperationId | `checkIn` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
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

### 响应体

#### 响应 `200`

- 说明：OK

- Content-Type：`*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `object` | 否 | - |
| `data` | `object` | 否 | - |

## GET /api/master/settings

- 标题：查询师傅设置
- 标签：master

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `GET` |
| 路径 | `/api/master/settings` |
| OperationId | `settings` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

- 无显式参数。

### 请求体

- 无请求体。

### 响应体

#### 响应 `200`

- 说明：OK

- Content-Type：`*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `object` | 否 | - |
| `data` | `object` | 否 | - |

## POST /api/master/settings

- 标题：保存师傅设置
- 标签：master

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `POST` |
| 路径 | `/api/master/settings` |
| OperationId | `saveSettings` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

- 无显式参数。

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `listening` | `boolean` | 否 | - |
| `maxDistance` | `string` | 否 | - |
| `privacyNumber` | `boolean` | 否 | - |

### 响应体

#### 响应 `200`

- 说明：OK

- Content-Type：`*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `object` | 否 | - |
| `data` | `object` | 否 | - |

## GET /api/master/wallet

- 标题：查询师傅钱包
- 标签：master

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `GET` |
| 路径 | `/api/master/wallet` |
| OperationId | `wallet` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

- 无显式参数。

### 请求体

- 无请求体。

### 响应体

#### 响应 `200`

- 说明：OK

- Content-Type：`*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `object` | 否 | - |
| `data` | `object` | 否 | - |

## GET /api/notifications

- 标题：查询通知中心
- 标签：master

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `GET` |
| 路径 | `/api/notifications` |
| OperationId | `notifications` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

- 无显式参数。

### 请求体

- 无请求体。

### 响应体

#### 响应 `200`

- 说明：OK

- Content-Type：`*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `object` | 否 | - |
| `data` | `object` | 否 | - |
