<!--
  移动端接口文档，由 scripts/export-openapi-markdown.mjs 自动生成。
  1. OpenAPI 来源：http://localhost:8080/v3/api-docs/mobile
  2. 下文展开展示参数、请求体和响应字段，便于联调查阅。
  3. 接口契约变更后请重新执行导出脚本。
-->

# 移动端接口文档

## 基本信息

- 文档标题：`呼之应开放接口文档`
- 版本：`v1`
- 分组：`mobile`
- 接口数量：`41`
- OpenAPI 地址：`http://localhost:8080/v3/api-docs/mobile`

## 鉴权说明

- `bearerAuth`, 类型: http, 方案: bearer, 格式: JWT

## 接口目录

- [GET /api/addresses](#get-api-addresses)
- [POST /api/addresses](#post-api-addresses)
- [DELETE /api/addresses/{id}](#delete-api-addresses-id)
- [POST /api/auth/refresh](#post-api-auth-refresh)
- [POST /api/auth/sms-login](#post-api-auth-sms-login)
- [POST /api/auth/wechat-login](#post-api-auth-wechat-login)
- [GET /api/categories](#get-api-categories)
- [GET /api/coupons](#get-api-coupons)
- [GET /api/files/{id}](#get-api-files-id)
- [GET /api/files/{id}/content](#get-api-files-id-content)
- [POST /api/files/upload](#post-api-files-upload)
- [GET /api/home](#get-api-home)
- [POST /api/map/eta](#post-api-map-eta)
- [POST /api/map/geofence/check](#post-api-map-geofence-check)
- [POST /api/map/regeo](#post-api-map-regeo)
- [GET /api/map/service-cities](#get-api-map-service-cities)
- [GET /api/members/current](#get-api-members-current)
- [GET /api/messages/{sessionId}/items](#get-api-messages-sessionid-items)
- [POST /api/messages/{sessionId}/items](#post-api-messages-sessionid-items)
- [GET /api/messages/sessions](#get-api-messages-sessions)
- [POST /api/orders/{id}/cancel](#post-api-orders-id-cancel)
- [GET /api/orders/{id}/tracking](#get-api-orders-id-tracking)
- [POST /api/orders/{id}/urge](#post-api-orders-id-urge)
- [GET /api/product-orders](#get-api-product-orders)
- [POST /api/product-orders](#post-api-product-orders)
- [GET /api/product-orders/{id}](#get-api-product-orders-id)
- [GET /api/products](#get-api-products)
- [GET /api/products/{id}](#get-api-products-id)
- [POST /api/quotations](#post-api-quotations)
- [POST /api/quotations/{quotationId}/confirm](#post-api-quotations-quotationid-confirm)
- [GET /api/search](#get-api-search)
- [GET /api/service-orders](#get-api-service-orders)
- [POST /api/service-orders](#post-api-service-orders)
- [GET /api/service-orders/{id}](#get-api-service-orders-id)
- [POST /api/service-orders/{id}/status](#post-api-service-orders-id-status)
- [GET /api/service-orders/time-slots](#get-api-service-orders-time-slots)
- [GET /api/services](#get-api-services)
- [GET /api/services/{id}](#get-api-services-id)
- [GET /api/services/{id}/comments](#get-api-services-id-comments)
- [GET /api/users/me](#get-api-users-me)
- [PUT /api/users/me](#put-api-users-me)

## GET /api/addresses

- 摘要：查询地址列表
- 标签：user

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/addresses` |
| OperationId | `addresses` |
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

## POST /api/addresses

- 摘要：保存地址
- 标签：user

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/addresses` |
| OperationId | `saveAddress` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

- 当前接口没有显式参数。

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | - |
| `tag` | `string` | 否 | - |
| `name` | `string` | 否 | - |
| `mobile` | `string` | 否 | - |
| `detailAddress` | `string` | 否 | - |
| `isDefault` | `boolean` | 否 | - |

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

## DELETE /api/addresses/{id}

- 摘要：删除地址
- 标签：user

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `DELETE` |
| 路径 | `/api/addresses/{id}` |
| OperationId | `deleteAddress` |
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

## POST /api/auth/refresh

- 摘要：刷新登录态
- 标签：auth

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/auth/refresh` |
| OperationId | `refresh` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

- 当前接口没有显式参数。

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `refreshToken` | `string` | 是 | - |

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

## POST /api/auth/sms-login

- 摘要：短信登录
- 标签：auth

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/auth/sms-login` |
| OperationId | `smsLogin` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

- 当前接口没有显式参数。

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `role` | `string` | 是 | - |

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

## POST /api/auth/wechat-login

- 摘要：微信登录
- 标签：auth

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/auth/wechat-login` |
| OperationId | `wechatLogin` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

- 当前接口没有显式参数。

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `role` | `string` | 是 | - |

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

## GET /api/categories

- 摘要：查询服务类目
- 标签：mobile-catalog

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/categories` |
| OperationId | `categories` |
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

## GET /api/coupons

- 摘要：查询优惠券列表
- 标签：user

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/coupons` |
| OperationId | `coupons` |
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

## GET /api/home

- 摘要：查询首页聚合数据
- 标签：mobile-catalog

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/home` |
| OperationId | `home` |
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

## POST /api/map/eta

- 摘要：查询预计到达时间
- 标签：map

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/map/eta` |
| OperationId | `eta` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

- 当前接口没有显式参数。

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `orderId` | `string` | 否 | 订单 ID |
| `latitude` | `number` | 否 | 纬度 |
| `longitude` | `number` | 否 | 经度 |

### 响应说明

#### 响应 `200`

- 说明：OK

- Content-Type: `*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `EtaPayload` | 否 | - |
| `data.eta` | `string` | 否 | 预计到达时间 |
| `data.distance` | `string` | 否 | 路程说明 |

## POST /api/map/geofence/check

- 摘要：校验地址是否在服务范围
- 标签：map

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/map/geofence/check` |
| OperationId | `geofenceCheck` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

- 当前接口没有显式参数。

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `city` | `string` | 否 | 城市名称 |
| `district` | `string` | 否 | 区县名称 |

### 响应说明

#### 响应 `200`

- 说明：OK

- Content-Type: `*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `GeofenceCheckPayload` | 否 | - |
| `data.serviceable` | `boolean` | 否 | 是否可服务 |
| `data.matchedZone` | `string` | 否 | 命中的区域说明 |

## POST /api/map/regeo

- 摘要：逆地理解析
- 标签：map

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/map/regeo` |
| OperationId | `reverseGeocode` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

- 当前接口没有显式参数。

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `latitude` | `number` | 否 | 纬度 |
| `longitude` | `number` | 否 | 经度 |

### 响应说明

#### 响应 `200`

- 说明：OK

- Content-Type: `*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `RegeoPayload` | 否 | - |
| `data.city` | `string` | 否 | 城市名称 |
| `data.district` | `string` | 否 | 区县名称 |
| `data.address` | `string` | 否 | 详细地址 |
| `data.serviceable` | `boolean` | 否 | 是否可服务 |

## GET /api/map/service-cities

- 摘要：查询可服务城市与区域列表
- 标签：map

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/map/service-cities` |
| OperationId | `serviceCities` |
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
| `data` | `array<ServiceCityPayload>` | 否 | - |
| `data` | `array<ServiceCityPayload>` | 否 | - |
| `data[].id` | `integer` | 否 | 区域主键 ID |
| `data[].name` | `string` | 否 | 城市名称 |
| `data[].district` | `string` | 否 | 区县名称 |
| `data[].hot` | `boolean` | 否 | 是否热门城市 |

## GET /api/members/current

- 摘要：查询当前会员等级
- 标签：user

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/members/current` |
| OperationId | `member` |
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

## GET /api/product-orders

- 摘要：查询商品订单列表
- 标签：mobile-order

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/product-orders` |
| OperationId | `productOrders` |
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

## POST /api/product-orders

- 摘要：创建商品订单
- 标签：mobile-order

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/product-orders` |
| OperationId | `createProductOrder` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

- 当前接口没有显式参数。

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `productId` | `integer` | 否 | - |
| `skuId` | `integer` | 否 | - |
| `addressId` | `integer` | 否 | - |

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

## GET /api/product-orders/{id}

- 摘要：查询商品订单详情
- 标签：mobile-order

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/product-orders/{id}` |
| OperationId | `productOrder` |
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

## GET /api/products

- 摘要：查询商品列表
- 标签：mobile-catalog

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/products` |
| OperationId | `products` |
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

## GET /api/products/{id}

- 摘要：查询商品详情
- 标签：mobile-catalog

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/products/{id}` |
| OperationId | `productDetail` |
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

## GET /api/search

- 摘要：搜索服务与商品
- 标签：mobile-catalog

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/search` |
| OperationId | `search` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `keyword` | query | 否 | `string` | - |

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

## GET /api/services

- 摘要：查询服务列表
- 标签：mobile-catalog

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/services` |
| OperationId | `services` |
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

## GET /api/services/{id}

- 摘要：查询服务详情
- 标签：mobile-catalog

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/services/{id}` |
| OperationId | `serviceDetail` |
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

## GET /api/services/{id}/comments

- 摘要：查询服务评价列表
- 标签：mobile-catalog

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/services/{id}/comments` |
| OperationId | `serviceComments` |
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

## GET /api/users/me

- 摘要：查询当前用户信息
- 标签：user

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/users/me` |
| OperationId | `me` |
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

## PUT /api/users/me

- 摘要：更新用户资料
- 标签：user

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `PUT` |
| 路径 | `/api/users/me` |
| OperationId | `updateProfile` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

- 当前接口没有显式参数。

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `nickname` | `string` | 否 | - |
| `mobile` | `string` | 否 | - |

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
