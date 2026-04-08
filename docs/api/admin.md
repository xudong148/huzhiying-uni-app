<!--
  文档说明：
  1. 当前文件为 后台接口文档，由 scripts/export-openapi-markdown.mjs 自动生成。
  2. 数据源地址：http://localhost:8080/v3/api-docs/admin。
  3. 字段表会展开请求参数、请求体和响应体，便于前后端对照联调。
-->

# 后台接口文档

## 基本信息

- 文档标题：`呼之应开放接口`
- 文档版本：`v1`
- 分组：`admin`
- 接口数量：`92`
- OpenAPI 地址：`http://localhost:8080/v3/api-docs/admin`

## 鉴权约定

- `bearerAuth`，类型：http，协议：bearer，格式：JWT

## 接口目录

- [GET /api/admin/arbitrations](#get-api-admin-arbitrations)
- [GET /api/admin/arbitrations/{id}](#get-api-admin-arbitrations-id)
- [POST /api/admin/arbitrations/{id}/resolve](#post-api-admin-arbitrations-id-resolve)
- [GET /api/admin/catalog/categories](#get-api-admin-catalog-categories)
- [POST /api/admin/catalog/categories](#post-api-admin-catalog-categories)
- [DELETE /api/admin/catalog/categories/{id}](#delete-api-admin-catalog-categories-id)
- [GET /api/admin/catalog/categories/{id}](#get-api-admin-catalog-categories-id)
- [PUT /api/admin/catalog/categories/{id}](#put-api-admin-catalog-categories-id)
- [GET /api/admin/catalog/products](#get-api-admin-catalog-products)
- [POST /api/admin/catalog/products](#post-api-admin-catalog-products)
- [DELETE /api/admin/catalog/products/{id}](#delete-api-admin-catalog-products-id)
- [GET /api/admin/catalog/products/{id}](#get-api-admin-catalog-products-id)
- [PUT /api/admin/catalog/products/{id}](#put-api-admin-catalog-products-id)
- [GET /api/admin/catalog/service-items](#get-api-admin-catalog-service-items)
- [POST /api/admin/catalog/service-items](#post-api-admin-catalog-service-items)
- [DELETE /api/admin/catalog/service-items/{id}](#delete-api-admin-catalog-service-items-id)
- [GET /api/admin/catalog/service-items/{id}](#get-api-admin-catalog-service-items-id)
- [PUT /api/admin/catalog/service-items/{id}](#put-api-admin-catalog-service-items-id)
- [GET /api/admin/catalog/skus](#get-api-admin-catalog-skus)
- [POST /api/admin/catalog/skus](#post-api-admin-catalog-skus)
- [DELETE /api/admin/catalog/skus/{id}](#delete-api-admin-catalog-skus-id)
- [GET /api/admin/catalog/skus/{id}](#get-api-admin-catalog-skus-id)
- [PUT /api/admin/catalog/skus/{id}](#put-api-admin-catalog-skus-id)
- [GET /api/admin/content/agreements](#get-api-admin-content-agreements)
- [POST /api/admin/content/agreements](#post-api-admin-content-agreements)
- [DELETE /api/admin/content/agreements/{id}](#delete-api-admin-content-agreements-id)
- [GET /api/admin/content/agreements/{id}](#get-api-admin-content-agreements-id)
- [PUT /api/admin/content/agreements/{id}](#put-api-admin-content-agreements-id)
- [GET /api/admin/content/banners](#get-api-admin-content-banners)
- [POST /api/admin/content/banners](#post-api-admin-content-banners)
- [DELETE /api/admin/content/banners/{id}](#delete-api-admin-content-banners-id)
- [GET /api/admin/content/banners/{id}](#get-api-admin-content-banners-id)
- [PUT /api/admin/content/banners/{id}](#put-api-admin-content-banners-id)
- [GET /api/admin/content/notices](#get-api-admin-content-notices)
- [POST /api/admin/content/notices](#post-api-admin-content-notices)
- [DELETE /api/admin/content/notices/{id}](#delete-api-admin-content-notices-id)
- [GET /api/admin/content/notices/{id}](#get-api-admin-content-notices-id)
- [PUT /api/admin/content/notices/{id}](#put-api-admin-content-notices-id)
- [GET /api/admin/dashboard](#get-api-admin-dashboard)
- [GET /api/admin/dispatch](#get-api-admin-dispatch)
- [GET /api/admin/dispatch/{taskId}](#get-api-admin-dispatch-taskid)
- [POST /api/admin/dispatch/{taskId}/assign](#post-api-admin-dispatch-taskid-assign)
- [POST /api/admin/dispatch/{taskId}/cancel-order](#post-api-admin-dispatch-taskid-cancel-order)
- [GET /api/admin/dispatch/zones](#get-api-admin-dispatch-zones)
- [POST /api/admin/dispatch/zones](#post-api-admin-dispatch-zones)
- [DELETE /api/admin/dispatch/zones/{id}](#delete-api-admin-dispatch-zones-id)
- [GET /api/admin/dispatch/zones/{id}](#get-api-admin-dispatch-zones-id)
- [PUT /api/admin/dispatch/zones/{id}](#put-api-admin-dispatch-zones-id)
- [GET /api/admin/finance](#get-api-admin-finance)
- [GET /api/admin/finance/{billNo}](#get-api-admin-finance-billno)
- [POST /api/admin/finance/{billNo}/approve](#post-api-admin-finance-billno-approve)
- [GET /api/admin/marketing/coupons](#get-api-admin-marketing-coupons)
- [POST /api/admin/marketing/coupons](#post-api-admin-marketing-coupons)
- [DELETE /api/admin/marketing/coupons/{id}](#delete-api-admin-marketing-coupons-id)
- [GET /api/admin/marketing/coupons/{id}](#get-api-admin-marketing-coupons-id)
- [PUT /api/admin/marketing/coupons/{id}](#put-api-admin-marketing-coupons-id)
- [GET /api/admin/marketing/member-levels](#get-api-admin-marketing-member-levels)
- [POST /api/admin/marketing/member-levels](#post-api-admin-marketing-member-levels)
- [DELETE /api/admin/marketing/member-levels/{id}](#delete-api-admin-marketing-member-levels-id)
- [GET /api/admin/marketing/member-levels/{id}](#get-api-admin-marketing-member-levels-id)
- [PUT /api/admin/marketing/member-levels/{id}](#put-api-admin-marketing-member-levels-id)
- [GET /api/admin/masters](#get-api-admin-masters)
- [GET /api/admin/masters/{userId}](#get-api-admin-masters-userid)
- [PUT /api/admin/masters/{userId}](#put-api-admin-masters-userid)
- [POST /api/admin/masters/{userId}/credit-score](#post-api-admin-masters-userid-credit-score)
- [POST /api/admin/masters/{userId}/disable](#post-api-admin-masters-userid-disable)
- [POST /api/admin/masters/{userId}/enable](#post-api-admin-masters-userid-enable)
- [GET /api/admin/orders](#get-api-admin-orders)
- [GET /api/admin/orders/{orderId}](#get-api-admin-orders-orderid)
- [POST /api/admin/orders/{orderId}/cancel](#post-api-admin-orders-orderid-cancel)
- [POST /api/admin/orders/{orderId}/refund](#post-api-admin-orders-orderid-refund)
- [GET /api/admin/pricing](#get-api-admin-pricing)
- [GET /api/admin/pricing/rules](#get-api-admin-pricing-rules)
- [POST /api/admin/pricing/rules](#post-api-admin-pricing-rules)
- [DELETE /api/admin/pricing/rules/{id}](#delete-api-admin-pricing-rules-id)
- [GET /api/admin/pricing/rules/{id}](#get-api-admin-pricing-rules-id)
- [PUT /api/admin/pricing/rules/{id}](#put-api-admin-pricing-rules-id)
- [GET /api/admin/system/menus](#get-api-admin-system-menus)
- [POST /api/admin/system/menus](#post-api-admin-system-menus)
- [DELETE /api/admin/system/menus/{id}](#delete-api-admin-system-menus-id)
- [GET /api/admin/system/menus/{id}](#get-api-admin-system-menus-id)
- [PUT /api/admin/system/menus/{id}](#put-api-admin-system-menus-id)
- [GET /api/admin/system/permissions](#get-api-admin-system-permissions)
- [POST /api/admin/system/permissions](#post-api-admin-system-permissions)
- [DELETE /api/admin/system/permissions/{id}](#delete-api-admin-system-permissions-id)
- [GET /api/admin/system/permissions/{id}](#get-api-admin-system-permissions-id)
- [PUT /api/admin/system/permissions/{id}](#put-api-admin-system-permissions-id)
- [GET /api/admin/system/roles](#get-api-admin-system-roles)
- [POST /api/admin/system/roles](#post-api-admin-system-roles)
- [DELETE /api/admin/system/roles/{id}](#delete-api-admin-system-roles-id)
- [GET /api/admin/system/roles/{id}](#get-api-admin-system-roles-id)
- [PUT /api/admin/system/roles/{id}](#put-api-admin-system-roles-id)

## GET /api/admin/arbitrations

- 标题：查询仲裁工单列表
- 标签：admin-dashboard

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `GET` |
| 路径 | `/api/admin/arbitrations` |
| OperationId | `arbitrations` |
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

## GET /api/admin/arbitrations/{id}

- 标题：查询仲裁详情
- 标签：admin-business

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `GET` |
| 路径 | `/api/admin/arbitrations/{id}` |
| OperationId | `arbitrationDetail` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `string` | - |

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

## POST /api/admin/arbitrations/{id}/resolve

- 标题：提交仲裁裁决
- 标签：admin-business

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `POST` |
| 路径 | `/api/admin/arbitrations/{id}/resolve` |
| OperationId | `resolveArbitration` |
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
| `statusText` | `string` | 是 | 裁决后的状态文案 |
| `resultText` | `string` | 是 | 裁决结果说明 |

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

## GET /api/admin/catalog/categories

- 标题：查询服务类目列表
- 标签：admin-catalog

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `GET` |
| 路径 | `/api/admin/catalog/categories` |
| OperationId | `categories` |
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
| `data` | `array<ServiceCategoryPayload>` | 否 | - |
| `data` | `array<ServiceCategoryPayload>` | 否 | - |
| `data[].id` | `integer` | 否 | 主键 ID |
| `data[].name` | `string` | 否 | 类目名称 |
| `data[].icon` | `string` | 否 | 图标地址 |
| `data[].sortOrder` | `integer` | 否 | 排序值 |
| `data[].enabled` | `boolean` | 否 | 是否启用 |

## POST /api/admin/catalog/categories

- 标题：新增服务类目
- 标签：admin-catalog

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `POST` |
| 路径 | `/api/admin/catalog/categories` |
| OperationId | `createCategory` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

- 无显式参数。

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `name` | `string` | 否 | 类目名称 |
| `icon` | `string` | 否 | 图标地址 |
| `sortOrder` | `integer` | 否 | 排序值 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应体

#### 响应 `200`

- 说明：OK

- Content-Type：`*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `ServiceCategoryPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.name` | `string` | 否 | 类目名称 |
| `data.icon` | `string` | 否 | 图标地址 |
| `data.sortOrder` | `integer` | 否 | 排序值 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## DELETE /api/admin/catalog/categories/{id}

- 标题：删除服务类目
- 标签：admin-catalog

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `DELETE` |
| 路径 | `/api/admin/catalog/categories/{id}` |
| OperationId | `deleteCategory` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

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
| `data` | `boolean` | 否 | - |

## GET /api/admin/catalog/categories/{id}

- 标题：查询服务类目详情
- 标签：admin-catalog

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `GET` |
| 路径 | `/api/admin/catalog/categories/{id}` |
| OperationId | `category` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

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
| `data` | `ServiceCategoryPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.name` | `string` | 否 | 类目名称 |
| `data.icon` | `string` | 否 | 图标地址 |
| `data.sortOrder` | `integer` | 否 | 排序值 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## PUT /api/admin/catalog/categories/{id}

- 标题：更新服务类目
- 标签：admin-catalog

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `PUT` |
| 路径 | `/api/admin/catalog/categories/{id}` |
| OperationId | `updateCategory` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `name` | `string` | 否 | 类目名称 |
| `icon` | `string` | 否 | 图标地址 |
| `sortOrder` | `integer` | 否 | 排序值 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应体

#### 响应 `200`

- 说明：OK

- Content-Type：`*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `ServiceCategoryPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.name` | `string` | 否 | 类目名称 |
| `data.icon` | `string` | 否 | 图标地址 |
| `data.sortOrder` | `integer` | 否 | 排序值 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## GET /api/admin/catalog/products

- 标题：查询商品列表
- 标签：admin-catalog

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `GET` |
| 路径 | `/api/admin/catalog/products` |
| OperationId | `products` |
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
| `data` | `array<ProductPayload>` | 否 | - |
| `data` | `array<ProductPayload>` | 否 | - |
| `data[].id` | `integer` | 否 | 主键 ID |
| `data[].name` | `string` | 否 | 商品名称 |
| `data[].descriptionText` | `string` | 否 | 商品描述 |
| `data[].price` | `number` | 否 | 商品价格 |
| `data[].createInstallOrder` | `boolean` | 否 | 是否自动生成安装工单 |
| `data[].imageUrl` | `string` | 否 | 商品图片 |
| `data[].enabled` | `boolean` | 否 | 是否启用 |

## POST /api/admin/catalog/products

- 标题：新增商品
- 标签：admin-catalog

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `POST` |
| 路径 | `/api/admin/catalog/products` |
| OperationId | `createProduct` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

- 无显式参数。

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `name` | `string` | 否 | 商品名称 |
| `descriptionText` | `string` | 否 | 商品描述 |
| `price` | `number` | 否 | 商品价格 |
| `createInstallOrder` | `boolean` | 否 | 是否自动生成安装工单 |
| `imageUrl` | `string` | 否 | 商品图片 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应体

#### 响应 `200`

- 说明：OK

- Content-Type：`*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `ProductPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.name` | `string` | 否 | 商品名称 |
| `data.descriptionText` | `string` | 否 | 商品描述 |
| `data.price` | `number` | 否 | 商品价格 |
| `data.createInstallOrder` | `boolean` | 否 | 是否自动生成安装工单 |
| `data.imageUrl` | `string` | 否 | 商品图片 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## DELETE /api/admin/catalog/products/{id}

- 标题：删除商品
- 标签：admin-catalog

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `DELETE` |
| 路径 | `/api/admin/catalog/products/{id}` |
| OperationId | `deleteProduct` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

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
| `data` | `boolean` | 否 | - |

## GET /api/admin/catalog/products/{id}

- 标题：查询商品详情
- 标签：admin-catalog

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `GET` |
| 路径 | `/api/admin/catalog/products/{id}` |
| OperationId | `product` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

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
| `data` | `ProductPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.name` | `string` | 否 | 商品名称 |
| `data.descriptionText` | `string` | 否 | 商品描述 |
| `data.price` | `number` | 否 | 商品价格 |
| `data.createInstallOrder` | `boolean` | 否 | 是否自动生成安装工单 |
| `data.imageUrl` | `string` | 否 | 商品图片 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## PUT /api/admin/catalog/products/{id}

- 标题：更新商品
- 标签：admin-catalog

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `PUT` |
| 路径 | `/api/admin/catalog/products/{id}` |
| OperationId | `updateProduct` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `name` | `string` | 否 | 商品名称 |
| `descriptionText` | `string` | 否 | 商品描述 |
| `price` | `number` | 否 | 商品价格 |
| `createInstallOrder` | `boolean` | 否 | 是否自动生成安装工单 |
| `imageUrl` | `string` | 否 | 商品图片 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应体

#### 响应 `200`

- 说明：OK

- Content-Type：`*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `ProductPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.name` | `string` | 否 | 商品名称 |
| `data.descriptionText` | `string` | 否 | 商品描述 |
| `data.price` | `number` | 否 | 商品价格 |
| `data.createInstallOrder` | `boolean` | 否 | 是否自动生成安装工单 |
| `data.imageUrl` | `string` | 否 | 商品图片 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## GET /api/admin/catalog/service-items

- 标题：查询服务项列表
- 标签：admin-catalog

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `GET` |
| 路径 | `/api/admin/catalog/service-items` |
| OperationId | `serviceItems` |
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
| `data` | `array<ServiceItemPayload>` | 否 | - |
| `data` | `array<ServiceItemPayload>` | 否 | - |
| `data[].id` | `integer` | 否 | 主键 ID |
| `data[].categoryId` | `integer` | 否 | 所属类目 ID |
| `data[].name` | `string` | 否 | 服务名称 |
| `data[].subtitle` | `string` | 否 | 副标题 |
| `data[].basePrice` | `number` | 否 | 基础价 |
| `data[].doorPrice` | `number` | 否 | 上门费 |
| `data[].guidePrice` | `string` | 否 | 指导价 |
| `data[].warrantyText` | `string` | 否 | 质保说明 |
| `data[].guaranteesText` | `string` | 否 | 保障说明 |
| `data[].tagsText` | `string` | 否 | 标签说明 |
| `data[].imageUrls` | `string` | 否 | 图片地址，使用 \| 分隔 |
| `data[].processSteps` | `string` | 否 | 流程步骤，使用 \| 分隔 |
| `data[].enabled` | `boolean` | 否 | 是否启用 |

## POST /api/admin/catalog/service-items

- 标题：新增服务项
- 标签：admin-catalog

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `POST` |
| 路径 | `/api/admin/catalog/service-items` |
| OperationId | `createServiceItem` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

- 无显式参数。

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `categoryId` | `integer` | 否 | 所属类目 ID |
| `name` | `string` | 否 | 服务名称 |
| `subtitle` | `string` | 否 | 副标题 |
| `basePrice` | `number` | 否 | 基础价 |
| `doorPrice` | `number` | 否 | 上门费 |
| `guidePrice` | `string` | 否 | 指导价 |
| `warrantyText` | `string` | 否 | 质保说明 |
| `guaranteesText` | `string` | 否 | 保障说明 |
| `tagsText` | `string` | 否 | 标签说明 |
| `imageUrls` | `string` | 否 | 图片地址，使用 \| 分隔 |
| `processSteps` | `string` | 否 | 流程步骤，使用 \| 分隔 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应体

#### 响应 `200`

- 说明：OK

- Content-Type：`*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `ServiceItemPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.categoryId` | `integer` | 否 | 所属类目 ID |
| `data.name` | `string` | 否 | 服务名称 |
| `data.subtitle` | `string` | 否 | 副标题 |
| `data.basePrice` | `number` | 否 | 基础价 |
| `data.doorPrice` | `number` | 否 | 上门费 |
| `data.guidePrice` | `string` | 否 | 指导价 |
| `data.warrantyText` | `string` | 否 | 质保说明 |
| `data.guaranteesText` | `string` | 否 | 保障说明 |
| `data.tagsText` | `string` | 否 | 标签说明 |
| `data.imageUrls` | `string` | 否 | 图片地址，使用 \| 分隔 |
| `data.processSteps` | `string` | 否 | 流程步骤，使用 \| 分隔 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## DELETE /api/admin/catalog/service-items/{id}

- 标题：删除服务项
- 标签：admin-catalog

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `DELETE` |
| 路径 | `/api/admin/catalog/service-items/{id}` |
| OperationId | `deleteServiceItem` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

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
| `data` | `boolean` | 否 | - |

## GET /api/admin/catalog/service-items/{id}

- 标题：查询服务项详情
- 标签：admin-catalog

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `GET` |
| 路径 | `/api/admin/catalog/service-items/{id}` |
| OperationId | `serviceItem` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

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
| `data` | `ServiceItemPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.categoryId` | `integer` | 否 | 所属类目 ID |
| `data.name` | `string` | 否 | 服务名称 |
| `data.subtitle` | `string` | 否 | 副标题 |
| `data.basePrice` | `number` | 否 | 基础价 |
| `data.doorPrice` | `number` | 否 | 上门费 |
| `data.guidePrice` | `string` | 否 | 指导价 |
| `data.warrantyText` | `string` | 否 | 质保说明 |
| `data.guaranteesText` | `string` | 否 | 保障说明 |
| `data.tagsText` | `string` | 否 | 标签说明 |
| `data.imageUrls` | `string` | 否 | 图片地址，使用 \| 分隔 |
| `data.processSteps` | `string` | 否 | 流程步骤，使用 \| 分隔 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## PUT /api/admin/catalog/service-items/{id}

- 标题：更新服务项
- 标签：admin-catalog

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `PUT` |
| 路径 | `/api/admin/catalog/service-items/{id}` |
| OperationId | `updateServiceItem` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `categoryId` | `integer` | 否 | 所属类目 ID |
| `name` | `string` | 否 | 服务名称 |
| `subtitle` | `string` | 否 | 副标题 |
| `basePrice` | `number` | 否 | 基础价 |
| `doorPrice` | `number` | 否 | 上门费 |
| `guidePrice` | `string` | 否 | 指导价 |
| `warrantyText` | `string` | 否 | 质保说明 |
| `guaranteesText` | `string` | 否 | 保障说明 |
| `tagsText` | `string` | 否 | 标签说明 |
| `imageUrls` | `string` | 否 | 图片地址，使用 \| 分隔 |
| `processSteps` | `string` | 否 | 流程步骤，使用 \| 分隔 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应体

#### 响应 `200`

- 说明：OK

- Content-Type：`*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `ServiceItemPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.categoryId` | `integer` | 否 | 所属类目 ID |
| `data.name` | `string` | 否 | 服务名称 |
| `data.subtitle` | `string` | 否 | 副标题 |
| `data.basePrice` | `number` | 否 | 基础价 |
| `data.doorPrice` | `number` | 否 | 上门费 |
| `data.guidePrice` | `string` | 否 | 指导价 |
| `data.warrantyText` | `string` | 否 | 质保说明 |
| `data.guaranteesText` | `string` | 否 | 保障说明 |
| `data.tagsText` | `string` | 否 | 标签说明 |
| `data.imageUrls` | `string` | 否 | 图片地址，使用 \| 分隔 |
| `data.processSteps` | `string` | 否 | 流程步骤，使用 \| 分隔 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## GET /api/admin/catalog/skus

- 标题：查询 SKU 列表
- 标签：admin-catalog

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `GET` |
| 路径 | `/api/admin/catalog/skus` |
| OperationId | `skus` |
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
| `data` | `array<SkuPayload>` | 否 | - |
| `data` | `array<SkuPayload>` | 否 | - |
| `data[].id` | `integer` | 否 | 主键 ID |
| `data[].productId` | `integer` | 否 | 所属商品 ID |
| `data[].name` | `string` | 否 | SKU 名称 |
| `data[].price` | `number` | 否 | 价格 |
| `data[].stock` | `integer` | 否 | 库存 |
| `data[].enabled` | `boolean` | 否 | 是否启用 |

## POST /api/admin/catalog/skus

- 标题：新增 SKU
- 标签：admin-catalog

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `POST` |
| 路径 | `/api/admin/catalog/skus` |
| OperationId | `createSku` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

- 无显式参数。

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `productId` | `integer` | 否 | 所属商品 ID |
| `name` | `string` | 否 | SKU 名称 |
| `price` | `number` | 否 | 价格 |
| `stock` | `integer` | 否 | 库存 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应体

#### 响应 `200`

- 说明：OK

- Content-Type：`*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `SkuPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.productId` | `integer` | 否 | 所属商品 ID |
| `data.name` | `string` | 否 | SKU 名称 |
| `data.price` | `number` | 否 | 价格 |
| `data.stock` | `integer` | 否 | 库存 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## DELETE /api/admin/catalog/skus/{id}

- 标题：删除 SKU
- 标签：admin-catalog

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `DELETE` |
| 路径 | `/api/admin/catalog/skus/{id}` |
| OperationId | `deleteSku` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

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
| `data` | `boolean` | 否 | - |

## GET /api/admin/catalog/skus/{id}

- 标题：查询 SKU 详情
- 标签：admin-catalog

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `GET` |
| 路径 | `/api/admin/catalog/skus/{id}` |
| OperationId | `sku` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

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
| `data` | `SkuPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.productId` | `integer` | 否 | 所属商品 ID |
| `data.name` | `string` | 否 | SKU 名称 |
| `data.price` | `number` | 否 | 价格 |
| `data.stock` | `integer` | 否 | 库存 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## PUT /api/admin/catalog/skus/{id}

- 标题：更新 SKU
- 标签：admin-catalog

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `PUT` |
| 路径 | `/api/admin/catalog/skus/{id}` |
| OperationId | `updateSku` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `productId` | `integer` | 否 | 所属商品 ID |
| `name` | `string` | 否 | SKU 名称 |
| `price` | `number` | 否 | 价格 |
| `stock` | `integer` | 否 | 库存 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应体

#### 响应 `200`

- 说明：OK

- Content-Type：`*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `SkuPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.productId` | `integer` | 否 | 所属商品 ID |
| `data.name` | `string` | 否 | SKU 名称 |
| `data.price` | `number` | 否 | 价格 |
| `data.stock` | `integer` | 否 | 库存 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## GET /api/admin/content/agreements

- 标题：查询协议列表
- 标签：admin-content

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `GET` |
| 路径 | `/api/admin/content/agreements` |
| OperationId | `agreements` |
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
| `data` | `array<AgreementPayload>` | 否 | - |
| `data` | `array<AgreementPayload>` | 否 | - |
| `data[].id` | `integer` | 否 | 主键 ID |
| `data[].title` | `string` | 否 | 协议标题 |
| `data[].version` | `string` | 否 | 版本号 |
| `data[].content` | `string` | 否 | 正文内容 |
| `data[].enabled` | `boolean` | 否 | 是否启用 |

## POST /api/admin/content/agreements

- 标题：新增协议
- 标签：admin-content

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `POST` |
| 路径 | `/api/admin/content/agreements` |
| OperationId | `createAgreement` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

- 无显式参数。

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `title` | `string` | 否 | 协议标题 |
| `version` | `string` | 否 | 版本号 |
| `content` | `string` | 否 | 正文内容 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应体

#### 响应 `200`

- 说明：OK

- Content-Type：`*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `AgreementPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.title` | `string` | 否 | 协议标题 |
| `data.version` | `string` | 否 | 版本号 |
| `data.content` | `string` | 否 | 正文内容 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## DELETE /api/admin/content/agreements/{id}

- 标题：删除协议
- 标签：admin-content

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `DELETE` |
| 路径 | `/api/admin/content/agreements/{id}` |
| OperationId | `deleteAgreement` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

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
| `data` | `boolean` | 否 | - |

## GET /api/admin/content/agreements/{id}

- 标题：查询协议详情
- 标签：admin-content

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `GET` |
| 路径 | `/api/admin/content/agreements/{id}` |
| OperationId | `agreement` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

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
| `data` | `AgreementPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.title` | `string` | 否 | 协议标题 |
| `data.version` | `string` | 否 | 版本号 |
| `data.content` | `string` | 否 | 正文内容 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## PUT /api/admin/content/agreements/{id}

- 标题：更新协议
- 标签：admin-content

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `PUT` |
| 路径 | `/api/admin/content/agreements/{id}` |
| OperationId | `updateAgreement` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `title` | `string` | 否 | 协议标题 |
| `version` | `string` | 否 | 版本号 |
| `content` | `string` | 否 | 正文内容 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应体

#### 响应 `200`

- 说明：OK

- Content-Type：`*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `AgreementPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.title` | `string` | 否 | 协议标题 |
| `data.version` | `string` | 否 | 版本号 |
| `data.content` | `string` | 否 | 正文内容 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## GET /api/admin/content/banners

- 标题：查询 Banner 列表
- 标签：admin-content

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `GET` |
| 路径 | `/api/admin/content/banners` |
| OperationId | `banners` |
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
| `data` | `array<BannerPayload>` | 否 | - |
| `data` | `array<BannerPayload>` | 否 | - |
| `data[].id` | `integer` | 否 | 主键 ID |
| `data[].title` | `string` | 否 | 标题 |
| `data[].subtitle` | `string` | 否 | 副标题 |
| `data[].image` | `string` | 否 | 图片地址 |
| `data[].link` | `string` | 否 | 跳转链接 |
| `data[].sortOrder` | `integer` | 否 | 排序值 |
| `data[].enabled` | `boolean` | 否 | 是否启用 |

## POST /api/admin/content/banners

- 标题：新增 Banner
- 标签：admin-content

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `POST` |
| 路径 | `/api/admin/content/banners` |
| OperationId | `createBanner` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

- 无显式参数。

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `title` | `string` | 否 | 标题 |
| `subtitle` | `string` | 否 | 副标题 |
| `image` | `string` | 否 | 图片地址 |
| `link` | `string` | 否 | 跳转链接 |
| `sortOrder` | `integer` | 否 | 排序值 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应体

#### 响应 `200`

- 说明：OK

- Content-Type：`*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `BannerPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.title` | `string` | 否 | 标题 |
| `data.subtitle` | `string` | 否 | 副标题 |
| `data.image` | `string` | 否 | 图片地址 |
| `data.link` | `string` | 否 | 跳转链接 |
| `data.sortOrder` | `integer` | 否 | 排序值 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## DELETE /api/admin/content/banners/{id}

- 标题：删除 Banner
- 标签：admin-content

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `DELETE` |
| 路径 | `/api/admin/content/banners/{id}` |
| OperationId | `deleteBanner` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

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
| `data` | `boolean` | 否 | - |

## GET /api/admin/content/banners/{id}

- 标题：查询 Banner 详情
- 标签：admin-content

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `GET` |
| 路径 | `/api/admin/content/banners/{id}` |
| OperationId | `banner` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

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
| `data` | `BannerPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.title` | `string` | 否 | 标题 |
| `data.subtitle` | `string` | 否 | 副标题 |
| `data.image` | `string` | 否 | 图片地址 |
| `data.link` | `string` | 否 | 跳转链接 |
| `data.sortOrder` | `integer` | 否 | 排序值 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## PUT /api/admin/content/banners/{id}

- 标题：更新 Banner
- 标签：admin-content

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `PUT` |
| 路径 | `/api/admin/content/banners/{id}` |
| OperationId | `updateBanner` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `title` | `string` | 否 | 标题 |
| `subtitle` | `string` | 否 | 副标题 |
| `image` | `string` | 否 | 图片地址 |
| `link` | `string` | 否 | 跳转链接 |
| `sortOrder` | `integer` | 否 | 排序值 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应体

#### 响应 `200`

- 说明：OK

- Content-Type：`*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `BannerPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.title` | `string` | 否 | 标题 |
| `data.subtitle` | `string` | 否 | 副标题 |
| `data.image` | `string` | 否 | 图片地址 |
| `data.link` | `string` | 否 | 跳转链接 |
| `data.sortOrder` | `integer` | 否 | 排序值 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## GET /api/admin/content/notices

- 标题：查询公告列表
- 标签：admin-content

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `GET` |
| 路径 | `/api/admin/content/notices` |
| OperationId | `notices` |
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
| `data` | `array<NoticePayload>` | 否 | - |
| `data` | `array<NoticePayload>` | 否 | - |
| `data[].id` | `integer` | 否 | 主键 ID |
| `data[].title` | `string` | 否 | 公告标题 |
| `data[].content` | `string` | 否 | 公告内容 |
| `data[].levelCode` | `string` | 否 | 级别 |
| `data[].enabled` | `boolean` | 否 | 是否启用 |

## POST /api/admin/content/notices

- 标题：新增公告
- 标签：admin-content

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `POST` |
| 路径 | `/api/admin/content/notices` |
| OperationId | `createNotice` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

- 无显式参数。

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `title` | `string` | 否 | 公告标题 |
| `content` | `string` | 否 | 公告内容 |
| `levelCode` | `string` | 否 | 级别 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应体

#### 响应 `200`

- 说明：OK

- Content-Type：`*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `NoticePayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.title` | `string` | 否 | 公告标题 |
| `data.content` | `string` | 否 | 公告内容 |
| `data.levelCode` | `string` | 否 | 级别 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## DELETE /api/admin/content/notices/{id}

- 标题：删除公告
- 标签：admin-content

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `DELETE` |
| 路径 | `/api/admin/content/notices/{id}` |
| OperationId | `deleteNotice` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

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
| `data` | `boolean` | 否 | - |

## GET /api/admin/content/notices/{id}

- 标题：查询公告详情
- 标签：admin-content

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `GET` |
| 路径 | `/api/admin/content/notices/{id}` |
| OperationId | `notice` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

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
| `data` | `NoticePayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.title` | `string` | 否 | 公告标题 |
| `data.content` | `string` | 否 | 公告内容 |
| `data.levelCode` | `string` | 否 | 级别 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## PUT /api/admin/content/notices/{id}

- 标题：更新公告
- 标签：admin-content

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `PUT` |
| 路径 | `/api/admin/content/notices/{id}` |
| OperationId | `updateNotice` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `title` | `string` | 否 | 公告标题 |
| `content` | `string` | 否 | 公告内容 |
| `levelCode` | `string` | 否 | 级别 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应体

#### 响应 `200`

- 说明：OK

- Content-Type：`*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `NoticePayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.title` | `string` | 否 | 公告标题 |
| `data.content` | `string` | 否 | 公告内容 |
| `data.levelCode` | `string` | 否 | 级别 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## GET /api/admin/dashboard

- 标题：查询后台仪表盘
- 标签：admin-dashboard

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `GET` |
| 路径 | `/api/admin/dashboard` |
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

## GET /api/admin/dispatch

- 标题：查询后台调度列表
- 标签：admin-dashboard

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `GET` |
| 路径 | `/api/admin/dispatch` |
| OperationId | `dispatch` |
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

## GET /api/admin/dispatch/{taskId}

- 标题：查询调度任务详情
- 标签：admin-business

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `GET` |
| 路径 | `/api/admin/dispatch/{taskId}` |
| OperationId | `dispatchDetail` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `taskId` | path | 是 | `string` | - |

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

## POST /api/admin/dispatch/{taskId}/assign

- 标题：后台指派师傅
- 标签：admin-business

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `POST` |
| 路径 | `/api/admin/dispatch/{taskId}/assign` |
| OperationId | `assignDispatch` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `taskId` | path | 是 | `string` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `masterUserId` | `integer` | 否 | 目标师傅用户 ID |
| `masterName` | `string` | 否 | 目标师傅姓名，和 masterUserId 二选一即可 |

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

## POST /api/admin/dispatch/{taskId}/cancel-order

- 标题：后台从调度中心取消订单
- 标签：admin-business

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `POST` |
| 路径 | `/api/admin/dispatch/{taskId}/cancel-order` |
| OperationId | `cancelDispatchOrder` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `taskId` | path | 是 | `string` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `reason` | `string` | 否 | 原因或备注 |

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

## GET /api/admin/dispatch/zones

- 标题：查询服务区域列表
- 标签：admin-pricing

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `GET` |
| 路径 | `/api/admin/dispatch/zones` |
| OperationId | `dispatchZones` |
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
| `data` | `array<DispatchZonePayload>` | 否 | - |
| `data` | `array<DispatchZonePayload>` | 否 | - |
| `data[].id` | `integer` | 否 | 主键 ID |
| `data[].cityName` | `string` | 否 | 城市名称 |
| `data[].districtName` | `string` | 否 | 区县名称 |
| `data[].sortOrder` | `integer` | 否 | 排序值 |
| `data[].enabled` | `boolean` | 否 | 是否启用 |

## POST /api/admin/dispatch/zones

- 标题：新增服务区域
- 标签：admin-pricing

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `POST` |
| 路径 | `/api/admin/dispatch/zones` |
| OperationId | `createDispatchZone` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

- 无显式参数。

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `cityName` | `string` | 否 | 城市名称 |
| `districtName` | `string` | 否 | 区县名称 |
| `sortOrder` | `integer` | 否 | 排序值 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应体

#### 响应 `200`

- 说明：OK

- Content-Type：`*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `DispatchZonePayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.cityName` | `string` | 否 | 城市名称 |
| `data.districtName` | `string` | 否 | 区县名称 |
| `data.sortOrder` | `integer` | 否 | 排序值 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## DELETE /api/admin/dispatch/zones/{id}

- 标题：删除服务区域
- 标签：admin-pricing

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `DELETE` |
| 路径 | `/api/admin/dispatch/zones/{id}` |
| OperationId | `deleteDispatchZone` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

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
| `data` | `boolean` | 否 | - |

## GET /api/admin/dispatch/zones/{id}

- 标题：查询服务区域详情
- 标签：admin-pricing

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `GET` |
| 路径 | `/api/admin/dispatch/zones/{id}` |
| OperationId | `dispatchZone` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

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
| `data` | `DispatchZonePayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.cityName` | `string` | 否 | 城市名称 |
| `data.districtName` | `string` | 否 | 区县名称 |
| `data.sortOrder` | `integer` | 否 | 排序值 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## PUT /api/admin/dispatch/zones/{id}

- 标题：更新服务区域
- 标签：admin-pricing

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `PUT` |
| 路径 | `/api/admin/dispatch/zones/{id}` |
| OperationId | `updateDispatchZone` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `cityName` | `string` | 否 | 城市名称 |
| `districtName` | `string` | 否 | 区县名称 |
| `sortOrder` | `integer` | 否 | 排序值 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应体

#### 响应 `200`

- 说明：OK

- Content-Type：`*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `DispatchZonePayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.cityName` | `string` | 否 | 城市名称 |
| `data.districtName` | `string` | 否 | 区县名称 |
| `data.sortOrder` | `integer` | 否 | 排序值 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## GET /api/admin/finance

- 标题：查询财务列表
- 标签：admin-dashboard

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `GET` |
| 路径 | `/api/admin/finance` |
| OperationId | `finance` |
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

## GET /api/admin/finance/{billNo}

- 标题：查询财务单详情
- 标签：admin-business

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `GET` |
| 路径 | `/api/admin/finance/{billNo}` |
| OperationId | `financeDetail` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `billNo` | path | 是 | `string` | - |

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

## POST /api/admin/finance/{billNo}/approve

- 标题：审核财务单
- 标签：admin-business

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `POST` |
| 路径 | `/api/admin/finance/{billNo}/approve` |
| OperationId | `approveFinance` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `billNo` | path | 是 | `string` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `remark` | `string` | 否 | 审核备注 |

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

## GET /api/admin/marketing/coupons

- 标题：查询优惠券列表
- 标签：admin-marketing

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `GET` |
| 路径 | `/api/admin/marketing/coupons` |
| OperationId | `coupons` |
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
| `data` | `array<CouponPayload>` | 否 | - |
| `data` | `array<CouponPayload>` | 否 | - |
| `data[].id` | `integer` | 否 | 主键 ID |
| `data[].title` | `string` | 否 | 优惠券标题 |
| `data[].amount` | `number` | 否 | 面额 |
| `data[].thresholdText` | `string` | 否 | 门槛说明 |
| `data[].expireAt` | `string` | 否 | 过期时间 |
| `data[].enabled` | `boolean` | 否 | 是否启用 |

## POST /api/admin/marketing/coupons

- 标题：新增优惠券
- 标签：admin-marketing

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `POST` |
| 路径 | `/api/admin/marketing/coupons` |
| OperationId | `createCoupon` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

- 无显式参数。

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `title` | `string` | 否 | 优惠券标题 |
| `amount` | `number` | 否 | 面额 |
| `thresholdText` | `string` | 否 | 门槛说明 |
| `expireAt` | `string` | 否 | 过期时间 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应体

#### 响应 `200`

- 说明：OK

- Content-Type：`*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `CouponPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.title` | `string` | 否 | 优惠券标题 |
| `data.amount` | `number` | 否 | 面额 |
| `data.thresholdText` | `string` | 否 | 门槛说明 |
| `data.expireAt` | `string` | 否 | 过期时间 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## DELETE /api/admin/marketing/coupons/{id}

- 标题：删除优惠券
- 标签：admin-marketing

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `DELETE` |
| 路径 | `/api/admin/marketing/coupons/{id}` |
| OperationId | `deleteCoupon` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

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
| `data` | `boolean` | 否 | - |

## GET /api/admin/marketing/coupons/{id}

- 标题：查询优惠券详情
- 标签：admin-marketing

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `GET` |
| 路径 | `/api/admin/marketing/coupons/{id}` |
| OperationId | `coupon` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

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
| `data` | `CouponPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.title` | `string` | 否 | 优惠券标题 |
| `data.amount` | `number` | 否 | 面额 |
| `data.thresholdText` | `string` | 否 | 门槛说明 |
| `data.expireAt` | `string` | 否 | 过期时间 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## PUT /api/admin/marketing/coupons/{id}

- 标题：更新优惠券
- 标签：admin-marketing

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `PUT` |
| 路径 | `/api/admin/marketing/coupons/{id}` |
| OperationId | `updateCoupon` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `title` | `string` | 否 | 优惠券标题 |
| `amount` | `number` | 否 | 面额 |
| `thresholdText` | `string` | 否 | 门槛说明 |
| `expireAt` | `string` | 否 | 过期时间 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应体

#### 响应 `200`

- 说明：OK

- Content-Type：`*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `CouponPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.title` | `string` | 否 | 优惠券标题 |
| `data.amount` | `number` | 否 | 面额 |
| `data.thresholdText` | `string` | 否 | 门槛说明 |
| `data.expireAt` | `string` | 否 | 过期时间 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## GET /api/admin/marketing/member-levels

- 标题：查询会员等级列表
- 标签：admin-marketing

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `GET` |
| 路径 | `/api/admin/marketing/member-levels` |
| OperationId | `memberLevels` |
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
| `data` | `array<MemberLevelPayload>` | 否 | - |
| `data` | `array<MemberLevelPayload>` | 否 | - |
| `data[].id` | `integer` | 否 | 主键 ID |
| `data[].name` | `string` | 否 | 等级名称 |
| `data[].benefitText` | `string` | 否 | 权益说明 |
| `data[].pointsRequired` | `integer` | 否 | 所需积分 |
| `data[].enabled` | `boolean` | 否 | 是否启用 |

## POST /api/admin/marketing/member-levels

- 标题：新增会员等级
- 标签：admin-marketing

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `POST` |
| 路径 | `/api/admin/marketing/member-levels` |
| OperationId | `createMemberLevel` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

- 无显式参数。

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `name` | `string` | 否 | 等级名称 |
| `benefitText` | `string` | 否 | 权益说明 |
| `pointsRequired` | `integer` | 否 | 所需积分 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应体

#### 响应 `200`

- 说明：OK

- Content-Type：`*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `MemberLevelPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.name` | `string` | 否 | 等级名称 |
| `data.benefitText` | `string` | 否 | 权益说明 |
| `data.pointsRequired` | `integer` | 否 | 所需积分 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## DELETE /api/admin/marketing/member-levels/{id}

- 标题：删除会员等级
- 标签：admin-marketing

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `DELETE` |
| 路径 | `/api/admin/marketing/member-levels/{id}` |
| OperationId | `deleteMemberLevel` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

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
| `data` | `boolean` | 否 | - |

## GET /api/admin/marketing/member-levels/{id}

- 标题：查询会员等级详情
- 标签：admin-marketing

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `GET` |
| 路径 | `/api/admin/marketing/member-levels/{id}` |
| OperationId | `memberLevel` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

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
| `data` | `MemberLevelPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.name` | `string` | 否 | 等级名称 |
| `data.benefitText` | `string` | 否 | 权益说明 |
| `data.pointsRequired` | `integer` | 否 | 所需积分 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## PUT /api/admin/marketing/member-levels/{id}

- 标题：更新会员等级
- 标签：admin-marketing

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `PUT` |
| 路径 | `/api/admin/marketing/member-levels/{id}` |
| OperationId | `updateMemberLevel` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `name` | `string` | 否 | 等级名称 |
| `benefitText` | `string` | 否 | 权益说明 |
| `pointsRequired` | `integer` | 否 | 所需积分 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应体

#### 响应 `200`

- 说明：OK

- Content-Type：`*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `MemberLevelPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.name` | `string` | 否 | 等级名称 |
| `data.benefitText` | `string` | 否 | 权益说明 |
| `data.pointsRequired` | `integer` | 否 | 所需积分 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## GET /api/admin/masters

- 标题：查询师傅管理列表
- 标签：admin-dashboard

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `GET` |
| 路径 | `/api/admin/masters` |
| OperationId | `masters` |
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

## GET /api/admin/masters/{userId}

- 标题：查询师傅详情
- 标签：admin-business

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `GET` |
| 路径 | `/api/admin/masters/{userId}` |
| OperationId | `masterDetail` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `userId` | path | 是 | `integer` | - |

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

## PUT /api/admin/masters/{userId}

- 标题：更新师傅资料
- 标签：admin-business

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `PUT` |
| 路径 | `/api/admin/masters/{userId}` |
| OperationId | `updateMaster` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `userId` | path | 是 | `integer` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `realName` | `string` | 否 | 师傅姓名 |
| `mobile` | `string` | 否 | 手机号 |
| `skillTags` | `array<string>` | 否 | - |
| `skillTags` | `array<string>` | 否 | - |
| `serviceAreas` | `array<string>` | 否 | - |
| `serviceAreas` | `array<string>` | 否 | - |
| `deposit` | `number` | 否 | 保证金 |
| `online` | `boolean` | 否 | 是否在线 |
| `listening` | `boolean` | 否 | 是否启用听单 |
| `maxDistanceKm` | `integer` | 否 | 最大接单距离，单位 km |
| `privacyNumber` | `boolean` | 否 | 是否启用隐私号 |
| `enabled` | `boolean` | 否 | 是否启用该师傅 |

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

## POST /api/admin/masters/{userId}/credit-score

- 标题：调整师傅信用分
- 标签：admin-business

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `POST` |
| 路径 | `/api/admin/masters/{userId}/credit-score` |
| OperationId | `updateCreditScore` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `userId` | path | 是 | `integer` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `creditScore` | `integer` | 否 | 最新信用分 |

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

## POST /api/admin/masters/{userId}/disable

- 标题：停用师傅
- 标签：admin-business

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `POST` |
| 路径 | `/api/admin/masters/{userId}/disable` |
| OperationId | `disableMaster` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `userId` | path | 是 | `integer` | - |

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

## POST /api/admin/masters/{userId}/enable

- 标题：启用师傅
- 标签：admin-business

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `POST` |
| 路径 | `/api/admin/masters/{userId}/enable` |
| OperationId | `enableMaster` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `userId` | path | 是 | `integer` | - |

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

## GET /api/admin/orders

- 标题：查询后台订单列表
- 标签：admin-dashboard

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `GET` |
| 路径 | `/api/admin/orders` |
| OperationId | `orders` |
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

## GET /api/admin/orders/{orderId}

- 标题：查询订单详情
- 标签：admin-business

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `GET` |
| 路径 | `/api/admin/orders/{orderId}` |
| OperationId | `orderDetail` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `orderId` | path | 是 | `string` | - |

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

## POST /api/admin/orders/{orderId}/cancel

- 标题：后台取消订单
- 标签：admin-business

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `POST` |
| 路径 | `/api/admin/orders/{orderId}/cancel` |
| OperationId | `cancelOrder` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `orderId` | path | 是 | `string` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `reason` | `string` | 否 | 原因或备注 |

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

## POST /api/admin/orders/{orderId}/refund

- 标题：后台发起或审核退款
- 标签：admin-business

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `POST` |
| 路径 | `/api/admin/orders/{orderId}/refund` |
| OperationId | `refundOrder` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `orderId` | path | 是 | `string` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `reason` | `string` | 否 | 原因或备注 |

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

## GET /api/admin/pricing

- 标题：查询定价概览
- 标签：admin-dashboard

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `GET` |
| 路径 | `/api/admin/pricing` |
| OperationId | `pricing` |
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

## GET /api/admin/pricing/rules

- 标题：查询定价规则列表
- 标签：admin-pricing

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `GET` |
| 路径 | `/api/admin/pricing/rules` |
| OperationId | `pricingRules` |
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
| `data` | `array<PricingRulePayload>` | 否 | - |
| `data` | `array<PricingRulePayload>` | 否 | - |
| `data[].id` | `integer` | 否 | 主键 ID |
| `data[].categoryId` | `integer` | 否 | 类目 ID |
| `data[].label` | `string` | 否 | 规则名称 |
| `data[].basePrice` | `number` | 否 | 基础价 |
| `data[].coefficient` | `string` | 否 | 系数说明 |
| `data[].guidePrice` | `string` | 否 | 指导价说明 |
| `data[].enabled` | `boolean` | 否 | 是否启用 |

## POST /api/admin/pricing/rules

- 标题：新增定价规则
- 标签：admin-pricing

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `POST` |
| 路径 | `/api/admin/pricing/rules` |
| OperationId | `createPricingRule` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

- 无显式参数。

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `categoryId` | `integer` | 否 | 类目 ID |
| `label` | `string` | 否 | 规则名称 |
| `basePrice` | `number` | 否 | 基础价 |
| `coefficient` | `string` | 否 | 系数说明 |
| `guidePrice` | `string` | 否 | 指导价说明 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应体

#### 响应 `200`

- 说明：OK

- Content-Type：`*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `PricingRulePayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.categoryId` | `integer` | 否 | 类目 ID |
| `data.label` | `string` | 否 | 规则名称 |
| `data.basePrice` | `number` | 否 | 基础价 |
| `data.coefficient` | `string` | 否 | 系数说明 |
| `data.guidePrice` | `string` | 否 | 指导价说明 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## DELETE /api/admin/pricing/rules/{id}

- 标题：删除定价规则
- 标签：admin-pricing

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `DELETE` |
| 路径 | `/api/admin/pricing/rules/{id}` |
| OperationId | `deletePricingRule` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

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
| `data` | `boolean` | 否 | - |

## GET /api/admin/pricing/rules/{id}

- 标题：查询定价规则详情
- 标签：admin-pricing

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `GET` |
| 路径 | `/api/admin/pricing/rules/{id}` |
| OperationId | `pricingRule` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

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
| `data` | `PricingRulePayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.categoryId` | `integer` | 否 | 类目 ID |
| `data.label` | `string` | 否 | 规则名称 |
| `data.basePrice` | `number` | 否 | 基础价 |
| `data.coefficient` | `string` | 否 | 系数说明 |
| `data.guidePrice` | `string` | 否 | 指导价说明 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## PUT /api/admin/pricing/rules/{id}

- 标题：更新定价规则
- 标签：admin-pricing

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `PUT` |
| 路径 | `/api/admin/pricing/rules/{id}` |
| OperationId | `updatePricingRule` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `categoryId` | `integer` | 否 | 类目 ID |
| `label` | `string` | 否 | 规则名称 |
| `basePrice` | `number` | 否 | 基础价 |
| `coefficient` | `string` | 否 | 系数说明 |
| `guidePrice` | `string` | 否 | 指导价说明 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应体

#### 响应 `200`

- 说明：OK

- Content-Type：`*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `PricingRulePayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.categoryId` | `integer` | 否 | 类目 ID |
| `data.label` | `string` | 否 | 规则名称 |
| `data.basePrice` | `number` | 否 | 基础价 |
| `data.coefficient` | `string` | 否 | 系数说明 |
| `data.guidePrice` | `string` | 否 | 指导价说明 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## GET /api/admin/system/menus

- 标题：查询菜单列表
- 标签：admin-system

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `GET` |
| 路径 | `/api/admin/system/menus` |
| OperationId | `menus` |
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
| `data` | `array<MenuPayload>` | 否 | - |
| `data` | `array<MenuPayload>` | 否 | - |
| `data[].id` | `integer` | 否 | 主键 ID |
| `data[].name` | `string` | 否 | 菜单名称 |
| `data[].path` | `string` | 否 | 路由路径 |
| `data[].icon` | `string` | 否 | 图标 |
| `data[].sortOrder` | `integer` | 否 | 排序值 |
| `data[].enabled` | `boolean` | 否 | 是否启用 |

## POST /api/admin/system/menus

- 标题：新增菜单
- 标签：admin-system

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `POST` |
| 路径 | `/api/admin/system/menus` |
| OperationId | `createMenu` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

- 无显式参数。

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `name` | `string` | 否 | 菜单名称 |
| `path` | `string` | 否 | 路由路径 |
| `icon` | `string` | 否 | 图标 |
| `sortOrder` | `integer` | 否 | 排序值 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应体

#### 响应 `200`

- 说明：OK

- Content-Type：`*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `MenuPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.name` | `string` | 否 | 菜单名称 |
| `data.path` | `string` | 否 | 路由路径 |
| `data.icon` | `string` | 否 | 图标 |
| `data.sortOrder` | `integer` | 否 | 排序值 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## DELETE /api/admin/system/menus/{id}

- 标题：删除菜单
- 标签：admin-system

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `DELETE` |
| 路径 | `/api/admin/system/menus/{id}` |
| OperationId | `deleteMenu` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

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
| `data` | `boolean` | 否 | - |

## GET /api/admin/system/menus/{id}

- 标题：查询菜单详情
- 标签：admin-system

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `GET` |
| 路径 | `/api/admin/system/menus/{id}` |
| OperationId | `menu` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

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
| `data` | `MenuPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.name` | `string` | 否 | 菜单名称 |
| `data.path` | `string` | 否 | 路由路径 |
| `data.icon` | `string` | 否 | 图标 |
| `data.sortOrder` | `integer` | 否 | 排序值 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## PUT /api/admin/system/menus/{id}

- 标题：更新菜单
- 标签：admin-system

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `PUT` |
| 路径 | `/api/admin/system/menus/{id}` |
| OperationId | `updateMenu` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `name` | `string` | 否 | 菜单名称 |
| `path` | `string` | 否 | 路由路径 |
| `icon` | `string` | 否 | 图标 |
| `sortOrder` | `integer` | 否 | 排序值 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应体

#### 响应 `200`

- 说明：OK

- Content-Type：`*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `MenuPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.name` | `string` | 否 | 菜单名称 |
| `data.path` | `string` | 否 | 路由路径 |
| `data.icon` | `string` | 否 | 图标 |
| `data.sortOrder` | `integer` | 否 | 排序值 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## GET /api/admin/system/permissions

- 标题：查询权限点列表
- 标签：admin-system

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `GET` |
| 路径 | `/api/admin/system/permissions` |
| OperationId | `permissions` |
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
| `data` | `array<PermissionPayload>` | 否 | - |
| `data` | `array<PermissionPayload>` | 否 | - |
| `data[].id` | `integer` | 否 | 主键 ID |
| `data[].code` | `string` | 否 | 权限编码 |
| `data[].name` | `string` | 否 | 权限名称 |
| `data[].description` | `string` | 否 | 描述 |
| `data[].enabled` | `boolean` | 否 | 是否启用 |

## POST /api/admin/system/permissions

- 标题：新增权限点
- 标签：admin-system

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `POST` |
| 路径 | `/api/admin/system/permissions` |
| OperationId | `createPermission` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

- 无显式参数。

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `code` | `string` | 否 | 权限编码 |
| `name` | `string` | 否 | 权限名称 |
| `description` | `string` | 否 | 描述 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应体

#### 响应 `200`

- 说明：OK

- Content-Type：`*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `PermissionPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.code` | `string` | 否 | 权限编码 |
| `data.name` | `string` | 否 | 权限名称 |
| `data.description` | `string` | 否 | 描述 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## DELETE /api/admin/system/permissions/{id}

- 标题：删除权限点
- 标签：admin-system

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `DELETE` |
| 路径 | `/api/admin/system/permissions/{id}` |
| OperationId | `deletePermission` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

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
| `data` | `boolean` | 否 | - |

## GET /api/admin/system/permissions/{id}

- 标题：查询权限点详情
- 标签：admin-system

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `GET` |
| 路径 | `/api/admin/system/permissions/{id}` |
| OperationId | `permission` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

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
| `data` | `PermissionPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.code` | `string` | 否 | 权限编码 |
| `data.name` | `string` | 否 | 权限名称 |
| `data.description` | `string` | 否 | 描述 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## PUT /api/admin/system/permissions/{id}

- 标题：更新权限点
- 标签：admin-system

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `PUT` |
| 路径 | `/api/admin/system/permissions/{id}` |
| OperationId | `updatePermission` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `code` | `string` | 否 | 权限编码 |
| `name` | `string` | 否 | 权限名称 |
| `description` | `string` | 否 | 描述 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应体

#### 响应 `200`

- 说明：OK

- Content-Type：`*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `PermissionPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.code` | `string` | 否 | 权限编码 |
| `data.name` | `string` | 否 | 权限名称 |
| `data.description` | `string` | 否 | 描述 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## GET /api/admin/system/roles

- 标题：查询角色列表
- 标签：admin-system

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `GET` |
| 路径 | `/api/admin/system/roles` |
| OperationId | `roles` |
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
| `data` | `array<RolePayload>` | 否 | - |
| `data` | `array<RolePayload>` | 否 | - |
| `data[].id` | `integer` | 否 | 主键 ID |
| `data[].code` | `string` | 否 | 角色编码 |
| `data[].name` | `string` | 否 | 角色名称 |
| `data[].description` | `string` | 否 | 描述 |
| `data[].enabled` | `boolean` | 否 | 是否启用 |

## POST /api/admin/system/roles

- 标题：新增角色
- 标签：admin-system

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `POST` |
| 路径 | `/api/admin/system/roles` |
| OperationId | `createRole` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

- 无显式参数。

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `code` | `string` | 否 | 角色编码 |
| `name` | `string` | 否 | 角色名称 |
| `description` | `string` | 否 | 描述 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应体

#### 响应 `200`

- 说明：OK

- Content-Type：`*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `RolePayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.code` | `string` | 否 | 角色编码 |
| `data.name` | `string` | 否 | 角色名称 |
| `data.description` | `string` | 否 | 描述 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## DELETE /api/admin/system/roles/{id}

- 标题：删除角色
- 标签：admin-system

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `DELETE` |
| 路径 | `/api/admin/system/roles/{id}` |
| OperationId | `deleteRole` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

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
| `data` | `boolean` | 否 | - |

## GET /api/admin/system/roles/{id}

- 标题：查询角色详情
- 标签：admin-system

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `GET` |
| 路径 | `/api/admin/system/roles/{id}` |
| OperationId | `role` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

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
| `data` | `RolePayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.code` | `string` | 否 | 角色编码 |
| `data.name` | `string` | 否 | 角色名称 |
| `data.description` | `string` | 否 | 描述 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## PUT /api/admin/system/roles/{id}

- 标题：更新角色
- 标签：admin-system

### 请求信息

| 项目 | 内容 |
| --- | --- |
| 方法 | `PUT` |
| 路径 | `/api/admin/system/roles/{id}` |
| OperationId | `updateRole` |
| 鉴权 | `bearerAuth` |

### 路径 / Query 参数

| 名称 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `code` | `string` | 否 | 角色编码 |
| `name` | `string` | 否 | 角色名称 |
| `description` | `string` | 否 | 描述 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应体

#### 响应 `200`

- 说明：OK

- Content-Type：`*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `RolePayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.code` | `string` | 否 | 角色编码 |
| `data.name` | `string` | 否 | 角色名称 |
| `data.description` | `string` | 否 | 描述 |
| `data.enabled` | `boolean` | 否 | 是否启用 |
