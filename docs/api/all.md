<!--
  全量接口文档，由 scripts/export-openapi-markdown.mjs 自动生成。
  1. OpenAPI 来源：http://localhost:8080/v3/api-docs
  2. 下文展开展示参数、请求体和响应字段，便于联调查阅。
  3. 接口契约变更后请重新执行导出脚本。
-->

# 全量接口文档

## 基本信息

- 文档标题：`呼之应开放接口文档`
- 版本：`v1`
- 分组：`all`
- 接口数量：`196`
- OpenAPI 地址：`http://localhost:8080/v3/api-docs`

## 鉴权说明

- `bearerAuth`, 类型: http, 方案: bearer, 格式: JWT

## 接口目录

- [GET /api/academy/articles](#get-api-academy-articles)
- [GET /api/academy/articles/{id}](#get-api-academy-articles-id)
- [GET /api/academy/categories](#get-api-academy-categories)
- [GET /api/addresses](#get-api-addresses)
- [POST /api/addresses](#post-api-addresses)
- [DELETE /api/addresses/{id}](#delete-api-addresses-id)
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
- [GET /api/admin/content/academy-articles](#get-api-admin-content-academy-articles)
- [POST /api/admin/content/academy-articles](#post-api-admin-content-academy-articles)
- [DELETE /api/admin/content/academy-articles/{id}](#delete-api-admin-content-academy-articles-id)
- [GET /api/admin/content/academy-articles/{id}](#get-api-admin-content-academy-articles-id)
- [PUT /api/admin/content/academy-articles/{id}](#put-api-admin-content-academy-articles-id)
- [GET /api/admin/content/academy-categories](#get-api-admin-content-academy-categories)
- [POST /api/admin/content/academy-categories](#post-api-admin-content-academy-categories)
- [DELETE /api/admin/content/academy-categories/{id}](#delete-api-admin-content-academy-categories-id)
- [GET /api/admin/content/academy-categories/{id}](#get-api-admin-content-academy-categories-id)
- [PUT /api/admin/content/academy-categories/{id}](#put-api-admin-content-academy-categories-id)
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
- [GET /api/admin/content/community-posts](#get-api-admin-content-community-posts)
- [POST /api/admin/content/community-posts](#post-api-admin-content-community-posts)
- [DELETE /api/admin/content/community-posts/{id}](#delete-api-admin-content-community-posts-id)
- [GET /api/admin/content/community-posts/{id}](#get-api-admin-content-community-posts-id)
- [PUT /api/admin/content/community-posts/{id}](#put-api-admin-content-community-posts-id)
- [GET /api/admin/content/community-reports](#get-api-admin-content-community-reports)
- [POST /api/admin/content/community-reports](#post-api-admin-content-community-reports)
- [DELETE /api/admin/content/community-reports/{id}](#delete-api-admin-content-community-reports-id)
- [GET /api/admin/content/community-reports/{id}](#get-api-admin-content-community-reports-id)
- [PUT /api/admin/content/community-reports/{id}](#put-api-admin-content-community-reports-id)
- [GET /api/admin/content/ecosystem-cards](#get-api-admin-content-ecosystem-cards)
- [POST /api/admin/content/ecosystem-cards](#post-api-admin-content-ecosystem-cards)
- [DELETE /api/admin/content/ecosystem-cards/{id}](#delete-api-admin-content-ecosystem-cards-id)
- [GET /api/admin/content/ecosystem-cards/{id}](#get-api-admin-content-ecosystem-cards-id)
- [PUT /api/admin/content/ecosystem-cards/{id}](#put-api-admin-content-ecosystem-cards-id)
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
- [GET /api/admin/notifications](#get-api-admin-notifications)
- [POST /api/admin/notifications/dispatch](#post-api-admin-notifications-dispatch)
- [GET /api/admin/orders](#get-api-admin-orders)
- [GET /api/admin/orders/{orderId}](#get-api-admin-orders-orderid)
- [PUT /api/admin/orders/{orderId}/appointment](#put-api-admin-orders-orderid-appointment)
- [POST /api/admin/orders/{orderId}/cancel](#post-api-admin-orders-orderid-cancel)
- [POST /api/admin/orders/{orderId}/grant-coupon](#post-api-admin-orders-orderid-grant-coupon)
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
- [GET /api/admin/system/roles/{id}/grants](#get-api-admin-system-roles-id-grants)
- [PUT /api/admin/system/roles/{id}/grants](#put-api-admin-system-roles-id-grants)
- [POST /api/auth/admin-login](#post-api-auth-admin-login)
- [POST /api/auth/mobile-login](#post-api-auth-mobile-login)
- [POST /api/auth/refresh](#post-api-auth-refresh)
- [POST /api/auth/register](#post-api-auth-register)
- [POST /api/auth/send-code](#post-api-auth-send-code)
- [GET /api/auth/session](#get-api-auth-session)
- [POST /api/auth/sms-login](#post-api-auth-sms-login)
- [POST /api/auth/wechat-login](#post-api-auth-wechat-login)
- [GET /api/categories](#get-api-categories)
- [GET /api/community/posts](#get-api-community-posts)
- [POST /api/community/posts](#post-api-community-posts)
- [GET /api/community/posts/{id}](#get-api-community-posts-id)
- [GET /api/community/posts/{id}/comments](#get-api-community-posts-id-comments)
- [POST /api/community/posts/{id}/comments](#post-api-community-posts-id-comments)
- [POST /api/community/posts/{id}/like](#post-api-community-posts-id-like)
- [POST /api/community/posts/{id}/report](#post-api-community-posts-id-report)
- [GET /api/coupons](#get-api-coupons)
- [GET /api/dispatch/tasks](#get-api-dispatch-tasks)
- [POST /api/dispatch/tasks/{id}/claim](#post-api-dispatch-tasks-id-claim)
- [POST /api/dispatch/tasks/{id}/force-assign](#post-api-dispatch-tasks-id-force-assign)
- [GET /api/files/{id}](#get-api-files-id)
- [GET /api/files/{id}/content](#get-api-files-id-content)
- [POST /api/files/upload](#post-api-files-upload)
- [GET /api/home](#get-api-home)
- [POST /api/map/eta](#post-api-map-eta)
- [POST /api/map/geofence/check](#post-api-map-geofence-check)
- [POST /api/map/regeo](#post-api-map-regeo)
- [GET /api/map/service-cities](#get-api-map-service-cities)
- [POST /api/master/apply](#post-api-master-apply)
- [GET /api/master/dashboard](#get-api-master-dashboard)
- [POST /api/master/orders/{id}/after-work-media](#post-api-master-orders-id-after-work-media)
- [POST /api/master/orders/{id}/before-work-media](#post-api-master-orders-id-before-work-media)
- [POST /api/master/orders/{id}/check-in](#post-api-master-orders-id-check-in)
- [GET /api/master/settings](#get-api-master-settings)
- [POST /api/master/settings](#post-api-master-settings)
- [GET /api/master/wallet](#get-api-master-wallet)
- [GET /api/members/current](#get-api-members-current)
- [GET /api/messages/{sessionId}/items](#get-api-messages-sessionid-items)
- [POST /api/messages/{sessionId}/items](#post-api-messages-sessionid-items)
- [POST /api/messages/{sessionId}/read](#post-api-messages-sessionid-read)
- [GET /api/messages/sessions](#get-api-messages-sessions)
- [GET /api/notifications](#get-api-notifications)
- [POST /api/orders/{id}/cancel](#post-api-orders-id-cancel)
- [GET /api/orders/{id}/tracking](#get-api-orders-id-tracking)
- [POST /api/orders/{id}/urge](#post-api-orders-id-urge)
- [POST /api/payments/wechat/{orderId}/callback](#post-api-payments-wechat-orderid-callback)
- [POST /api/payments/wechat/prepay](#post-api-payments-wechat-prepay)
- [POST /api/payments/wechat/refund](#post-api-payments-wechat-refund)
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
- [GET /swagger-ui/index.html](#get-swagger-ui-index-html)

## GET /api/academy/articles

- 摘要：查询学堂文章
- 标签：mobile-content

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/academy/articles` |
| OperationId | `academyArticles_1` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `categoryId` | query | 否 | `integer` | - |

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

## GET /api/academy/articles/{id}

- 摘要：查询学堂文章详情
- 标签：mobile-content

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/academy/articles/{id}` |
| OperationId | `academyArticle_1` |
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

## GET /api/academy/categories

- 摘要：查询学堂栏目
- 标签：mobile-content

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/academy/categories` |
| OperationId | `academyCategories_1` |
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

## GET /api/admin/arbitrations

- 摘要：查询仲裁列表
- 标签：admin-dashboard

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/admin/arbitrations` |
| OperationId | `arbitrations` |
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

## GET /api/admin/arbitrations/{id}

- 摘要：查询仲裁详情
- 标签：admin-business

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/admin/arbitrations/{id}` |
| OperationId | `arbitrationDetail` |
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

## POST /api/admin/arbitrations/{id}/resolve

- 摘要：提交仲裁裁决
- 标签：admin-business

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/admin/arbitrations/{id}/resolve` |
| OperationId | `resolveArbitration` |
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
| `statusText` | `string` | 是 | 裁决后的状态文案 |
| `resultText` | `string` | 是 | 裁决结果说明 |

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

## GET /api/admin/catalog/categories

- 摘要：查询服务类目列表
- 标签：admin-catalog

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/admin/catalog/categories` |
| OperationId | `categories_1` |
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
| `data` | `array<ServiceCategoryPayload>` | 否 | - |
| `data` | `array<ServiceCategoryPayload>` | 否 | - |
| `data[].id` | `integer` | 否 | 主键 ID |
| `data[].name` | `string` | 是 | 类目名称 |
| `data[].icon` | `string` | 否 | 图标地址 |
| `data[].sortOrder` | `integer` | 是 | 排序值 |
| `data[].enabled` | `boolean` | 否 | 是否启用 |

## POST /api/admin/catalog/categories

- 摘要：新增服务类目
- 标签：admin-catalog

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/admin/catalog/categories` |
| OperationId | `createCategory` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

- 当前接口没有显式参数。

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `name` | `string` | 是 | 类目名称 |
| `icon` | `string` | 否 | 图标地址 |
| `sortOrder` | `integer` | 是 | 排序值 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应说明

#### 响应 `200`

- 说明：OK

- Content-Type: `*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `ServiceCategoryPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.name` | `string` | 是 | 类目名称 |
| `data.icon` | `string` | 否 | 图标地址 |
| `data.sortOrder` | `integer` | 是 | 排序值 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## DELETE /api/admin/catalog/categories/{id}

- 摘要：删除服务类目
- 标签：admin-catalog

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `DELETE` |
| 路径 | `/api/admin/catalog/categories/{id}` |
| OperationId | `deleteCategory` |
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
| `data` | `boolean` | 否 | - |

## GET /api/admin/catalog/categories/{id}

- 摘要：查询服务类目详情
- 标签：admin-catalog

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/admin/catalog/categories/{id}` |
| OperationId | `category` |
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
| `data` | `ServiceCategoryPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.name` | `string` | 是 | 类目名称 |
| `data.icon` | `string` | 否 | 图标地址 |
| `data.sortOrder` | `integer` | 是 | 排序值 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## PUT /api/admin/catalog/categories/{id}

- 摘要：更新服务类目
- 标签：admin-catalog

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `PUT` |
| 路径 | `/api/admin/catalog/categories/{id}` |
| OperationId | `updateCategory` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `name` | `string` | 是 | 类目名称 |
| `icon` | `string` | 否 | 图标地址 |
| `sortOrder` | `integer` | 是 | 排序值 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应说明

#### 响应 `200`

- 说明：OK

- Content-Type: `*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `ServiceCategoryPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.name` | `string` | 是 | 类目名称 |
| `data.icon` | `string` | 否 | 图标地址 |
| `data.sortOrder` | `integer` | 是 | 排序值 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## GET /api/admin/catalog/products

- 摘要：查询商品列表
- 标签：admin-catalog

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/admin/catalog/products` |
| OperationId | `products_1` |
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
| `data` | `array<ProductPayload>` | 否 | - |
| `data` | `array<ProductPayload>` | 否 | - |
| `data[].id` | `integer` | 否 | 主键 ID |
| `data[].name` | `string` | 是 | 商品名称 |
| `data[].descriptionText` | `string` | 否 | 商品描述 |
| `data[].price` | `number` | 是 | 成交价 |
| `data[].tagPrice` | `number` | 否 | 标签价 |
| `data[].discountPrice` | `number` | 否 | 折扣价 |
| `data[].createInstallOrder` | `boolean` | 否 | 是否自动生成安装工单 |
| `data[].imageUrl` | `string` | 否 | 商品图片 |
| `data[].enabled` | `boolean` | 否 | 是否启用 |

## POST /api/admin/catalog/products

- 摘要：新增商品
- 标签：admin-catalog

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/admin/catalog/products` |
| OperationId | `createProduct` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

- 当前接口没有显式参数。

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `name` | `string` | 是 | 商品名称 |
| `descriptionText` | `string` | 否 | 商品描述 |
| `price` | `number` | 是 | 成交价 |
| `tagPrice` | `number` | 否 | 标签价 |
| `discountPrice` | `number` | 否 | 折扣价 |
| `createInstallOrder` | `boolean` | 否 | 是否自动生成安装工单 |
| `imageUrl` | `string` | 否 | 商品图片 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应说明

#### 响应 `200`

- 说明：OK

- Content-Type: `*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `ProductPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.name` | `string` | 是 | 商品名称 |
| `data.descriptionText` | `string` | 否 | 商品描述 |
| `data.price` | `number` | 是 | 成交价 |
| `data.tagPrice` | `number` | 否 | 标签价 |
| `data.discountPrice` | `number` | 否 | 折扣价 |
| `data.createInstallOrder` | `boolean` | 否 | 是否自动生成安装工单 |
| `data.imageUrl` | `string` | 否 | 商品图片 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## DELETE /api/admin/catalog/products/{id}

- 摘要：删除商品
- 标签：admin-catalog

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `DELETE` |
| 路径 | `/api/admin/catalog/products/{id}` |
| OperationId | `deleteProduct` |
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
| `data` | `boolean` | 否 | - |

## GET /api/admin/catalog/products/{id}

- 摘要：查询商品详情
- 标签：admin-catalog

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/admin/catalog/products/{id}` |
| OperationId | `product` |
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
| `data` | `ProductPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.name` | `string` | 是 | 商品名称 |
| `data.descriptionText` | `string` | 否 | 商品描述 |
| `data.price` | `number` | 是 | 成交价 |
| `data.tagPrice` | `number` | 否 | 标签价 |
| `data.discountPrice` | `number` | 否 | 折扣价 |
| `data.createInstallOrder` | `boolean` | 否 | 是否自动生成安装工单 |
| `data.imageUrl` | `string` | 否 | 商品图片 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## PUT /api/admin/catalog/products/{id}

- 摘要：更新商品
- 标签：admin-catalog

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `PUT` |
| 路径 | `/api/admin/catalog/products/{id}` |
| OperationId | `updateProduct` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `name` | `string` | 是 | 商品名称 |
| `descriptionText` | `string` | 否 | 商品描述 |
| `price` | `number` | 是 | 成交价 |
| `tagPrice` | `number` | 否 | 标签价 |
| `discountPrice` | `number` | 否 | 折扣价 |
| `createInstallOrder` | `boolean` | 否 | 是否自动生成安装工单 |
| `imageUrl` | `string` | 否 | 商品图片 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应说明

#### 响应 `200`

- 说明：OK

- Content-Type: `*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `ProductPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.name` | `string` | 是 | 商品名称 |
| `data.descriptionText` | `string` | 否 | 商品描述 |
| `data.price` | `number` | 是 | 成交价 |
| `data.tagPrice` | `number` | 否 | 标签价 |
| `data.discountPrice` | `number` | 否 | 折扣价 |
| `data.createInstallOrder` | `boolean` | 否 | 是否自动生成安装工单 |
| `data.imageUrl` | `string` | 否 | 商品图片 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## GET /api/admin/catalog/service-items

- 摘要：查询服务项列表
- 标签：admin-catalog

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/admin/catalog/service-items` |
| OperationId | `serviceItems` |
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
| `data` | `array<ServiceItemPayload>` | 否 | - |
| `data` | `array<ServiceItemPayload>` | 否 | - |
| `data[].id` | `integer` | 否 | 主键 ID |
| `data[].categoryId` | `integer` | 是 | 类目 ID |
| `data[].name` | `string` | 是 | 服务名称 |
| `data[].subtitle` | `string` | 否 | 副标题 |
| `data[].basePrice` | `number` | 是 | 基础价 |
| `data[].doorPrice` | `number` | 是 | 上门费 |
| `data[].guidePrice` | `string` | 否 | 指导价 |
| `data[].warrantyText` | `string` | 否 | 质保说明 |
| `data[].guaranteesText` | `string` | 否 | 保障说明，使用 \| 分隔 |
| `data[].tagsText` | `string` | 否 | 标签说明，使用 \| 分隔 |
| `data[].imageUrls` | `string` | 否 | 图片地址，使用 \| 分隔 |
| `data[].processSteps` | `string` | 否 | 流程步骤，使用 \| 分隔 |
| `data[].enabled` | `boolean` | 否 | 是否启用 |

## POST /api/admin/catalog/service-items

- 摘要：新增服务项
- 标签：admin-catalog

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/admin/catalog/service-items` |
| OperationId | `createServiceItem` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

- 当前接口没有显式参数。

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `categoryId` | `integer` | 是 | 类目 ID |
| `name` | `string` | 是 | 服务名称 |
| `subtitle` | `string` | 否 | 副标题 |
| `basePrice` | `number` | 是 | 基础价 |
| `doorPrice` | `number` | 是 | 上门费 |
| `guidePrice` | `string` | 否 | 指导价 |
| `warrantyText` | `string` | 否 | 质保说明 |
| `guaranteesText` | `string` | 否 | 保障说明，使用 \| 分隔 |
| `tagsText` | `string` | 否 | 标签说明，使用 \| 分隔 |
| `imageUrls` | `string` | 否 | 图片地址，使用 \| 分隔 |
| `processSteps` | `string` | 否 | 流程步骤，使用 \| 分隔 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应说明

#### 响应 `200`

- 说明：OK

- Content-Type: `*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `ServiceItemPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.categoryId` | `integer` | 是 | 类目 ID |
| `data.name` | `string` | 是 | 服务名称 |
| `data.subtitle` | `string` | 否 | 副标题 |
| `data.basePrice` | `number` | 是 | 基础价 |
| `data.doorPrice` | `number` | 是 | 上门费 |
| `data.guidePrice` | `string` | 否 | 指导价 |
| `data.warrantyText` | `string` | 否 | 质保说明 |
| `data.guaranteesText` | `string` | 否 | 保障说明，使用 \| 分隔 |
| `data.tagsText` | `string` | 否 | 标签说明，使用 \| 分隔 |
| `data.imageUrls` | `string` | 否 | 图片地址，使用 \| 分隔 |
| `data.processSteps` | `string` | 否 | 流程步骤，使用 \| 分隔 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## DELETE /api/admin/catalog/service-items/{id}

- 摘要：删除服务项
- 标签：admin-catalog

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `DELETE` |
| 路径 | `/api/admin/catalog/service-items/{id}` |
| OperationId | `deleteServiceItem` |
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
| `data` | `boolean` | 否 | - |

## GET /api/admin/catalog/service-items/{id}

- 摘要：查询服务项详情
- 标签：admin-catalog

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/admin/catalog/service-items/{id}` |
| OperationId | `serviceItem` |
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
| `data` | `ServiceItemPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.categoryId` | `integer` | 是 | 类目 ID |
| `data.name` | `string` | 是 | 服务名称 |
| `data.subtitle` | `string` | 否 | 副标题 |
| `data.basePrice` | `number` | 是 | 基础价 |
| `data.doorPrice` | `number` | 是 | 上门费 |
| `data.guidePrice` | `string` | 否 | 指导价 |
| `data.warrantyText` | `string` | 否 | 质保说明 |
| `data.guaranteesText` | `string` | 否 | 保障说明，使用 \| 分隔 |
| `data.tagsText` | `string` | 否 | 标签说明，使用 \| 分隔 |
| `data.imageUrls` | `string` | 否 | 图片地址，使用 \| 分隔 |
| `data.processSteps` | `string` | 否 | 流程步骤，使用 \| 分隔 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## PUT /api/admin/catalog/service-items/{id}

- 摘要：更新服务项
- 标签：admin-catalog

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `PUT` |
| 路径 | `/api/admin/catalog/service-items/{id}` |
| OperationId | `updateServiceItem` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `categoryId` | `integer` | 是 | 类目 ID |
| `name` | `string` | 是 | 服务名称 |
| `subtitle` | `string` | 否 | 副标题 |
| `basePrice` | `number` | 是 | 基础价 |
| `doorPrice` | `number` | 是 | 上门费 |
| `guidePrice` | `string` | 否 | 指导价 |
| `warrantyText` | `string` | 否 | 质保说明 |
| `guaranteesText` | `string` | 否 | 保障说明，使用 \| 分隔 |
| `tagsText` | `string` | 否 | 标签说明，使用 \| 分隔 |
| `imageUrls` | `string` | 否 | 图片地址，使用 \| 分隔 |
| `processSteps` | `string` | 否 | 流程步骤，使用 \| 分隔 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应说明

#### 响应 `200`

- 说明：OK

- Content-Type: `*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `ServiceItemPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.categoryId` | `integer` | 是 | 类目 ID |
| `data.name` | `string` | 是 | 服务名称 |
| `data.subtitle` | `string` | 否 | 副标题 |
| `data.basePrice` | `number` | 是 | 基础价 |
| `data.doorPrice` | `number` | 是 | 上门费 |
| `data.guidePrice` | `string` | 否 | 指导价 |
| `data.warrantyText` | `string` | 否 | 质保说明 |
| `data.guaranteesText` | `string` | 否 | 保障说明，使用 \| 分隔 |
| `data.tagsText` | `string` | 否 | 标签说明，使用 \| 分隔 |
| `data.imageUrls` | `string` | 否 | 图片地址，使用 \| 分隔 |
| `data.processSteps` | `string` | 否 | 流程步骤，使用 \| 分隔 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## GET /api/admin/catalog/skus

- 摘要：查询 SKU 列表
- 标签：admin-catalog

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/admin/catalog/skus` |
| OperationId | `skus` |
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
| `data` | `array<SkuPayload>` | 否 | - |
| `data` | `array<SkuPayload>` | 否 | - |
| `data[].id` | `integer` | 否 | 主键 ID |
| `data[].productId` | `integer` | 是 | 商品 ID |
| `data[].name` | `string` | 是 | SKU 名称 |
| `data[].price` | `number` | 是 | 成交价 |
| `data[].tagPrice` | `number` | 否 | 标签价 |
| `data[].discountPrice` | `number` | 否 | 折扣价 |
| `data[].stock` | `integer` | 是 | 库存 |
| `data[].enabled` | `boolean` | 否 | 是否启用 |

## POST /api/admin/catalog/skus

- 摘要：新增 SKU
- 标签：admin-catalog

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/admin/catalog/skus` |
| OperationId | `createSku` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

- 当前接口没有显式参数。

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `productId` | `integer` | 是 | 商品 ID |
| `name` | `string` | 是 | SKU 名称 |
| `price` | `number` | 是 | 成交价 |
| `tagPrice` | `number` | 否 | 标签价 |
| `discountPrice` | `number` | 否 | 折扣价 |
| `stock` | `integer` | 是 | 库存 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应说明

#### 响应 `200`

- 说明：OK

- Content-Type: `*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `SkuPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.productId` | `integer` | 是 | 商品 ID |
| `data.name` | `string` | 是 | SKU 名称 |
| `data.price` | `number` | 是 | 成交价 |
| `data.tagPrice` | `number` | 否 | 标签价 |
| `data.discountPrice` | `number` | 否 | 折扣价 |
| `data.stock` | `integer` | 是 | 库存 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## DELETE /api/admin/catalog/skus/{id}

- 摘要：删除 SKU
- 标签：admin-catalog

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `DELETE` |
| 路径 | `/api/admin/catalog/skus/{id}` |
| OperationId | `deleteSku` |
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
| `data` | `boolean` | 否 | - |

## GET /api/admin/catalog/skus/{id}

- 摘要：查询 SKU 详情
- 标签：admin-catalog

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/admin/catalog/skus/{id}` |
| OperationId | `sku` |
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
| `data` | `SkuPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.productId` | `integer` | 是 | 商品 ID |
| `data.name` | `string` | 是 | SKU 名称 |
| `data.price` | `number` | 是 | 成交价 |
| `data.tagPrice` | `number` | 否 | 标签价 |
| `data.discountPrice` | `number` | 否 | 折扣价 |
| `data.stock` | `integer` | 是 | 库存 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## PUT /api/admin/catalog/skus/{id}

- 摘要：更新 SKU
- 标签：admin-catalog

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `PUT` |
| 路径 | `/api/admin/catalog/skus/{id}` |
| OperationId | `updateSku` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `productId` | `integer` | 是 | 商品 ID |
| `name` | `string` | 是 | SKU 名称 |
| `price` | `number` | 是 | 成交价 |
| `tagPrice` | `number` | 否 | 标签价 |
| `discountPrice` | `number` | 否 | 折扣价 |
| `stock` | `integer` | 是 | 库存 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应说明

#### 响应 `200`

- 说明：OK

- Content-Type: `*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `SkuPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.productId` | `integer` | 是 | 商品 ID |
| `data.name` | `string` | 是 | SKU 名称 |
| `data.price` | `number` | 是 | 成交价 |
| `data.tagPrice` | `number` | 否 | 标签价 |
| `data.discountPrice` | `number` | 否 | 折扣价 |
| `data.stock` | `integer` | 是 | 库存 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## GET /api/admin/content/academy-articles

- 摘要：查询学堂文章
- 标签：admin-production-content

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/admin/content/academy-articles` |
| OperationId | `academyArticles` |
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

## POST /api/admin/content/academy-articles

- 摘要：新增学堂文章
- 标签：admin-production-content

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/admin/content/academy-articles` |
| OperationId | `createAcademyArticle` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

- 当前接口没有显式参数。

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | - |
| `categoryId` | `integer` | 否 | - |
| `title` | `string` | 否 | - |
| `summary` | `string` | 否 | - |
| `content` | `string` | 否 | - |
| `coverImage` | `string` | 否 | - |
| `author` | `string` | 否 | - |
| `viewCount` | `integer` | 否 | - |
| `sortOrder` | `integer` | 否 | - |
| `published` | `boolean` | 否 | - |

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

## DELETE /api/admin/content/academy-articles/{id}

- 摘要：删除学堂文章
- 标签：admin-production-content

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `DELETE` |
| 路径 | `/api/admin/content/academy-articles/{id}` |
| OperationId | `deleteAcademyArticle` |
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
| `data` | `boolean` | 否 | - |

## GET /api/admin/content/academy-articles/{id}

- 摘要：查询学堂文章详情
- 标签：admin-production-content

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/admin/content/academy-articles/{id}` |
| OperationId | `academyArticle` |
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

## PUT /api/admin/content/academy-articles/{id}

- 摘要：更新学堂文章
- 标签：admin-production-content

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `PUT` |
| 路径 | `/api/admin/content/academy-articles/{id}` |
| OperationId | `updateAcademyArticle` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | - |
| `categoryId` | `integer` | 否 | - |
| `title` | `string` | 否 | - |
| `summary` | `string` | 否 | - |
| `content` | `string` | 否 | - |
| `coverImage` | `string` | 否 | - |
| `author` | `string` | 否 | - |
| `viewCount` | `integer` | 否 | - |
| `sortOrder` | `integer` | 否 | - |
| `published` | `boolean` | 否 | - |

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

## GET /api/admin/content/academy-categories

- 摘要：查询学堂栏目
- 标签：admin-production-content

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/admin/content/academy-categories` |
| OperationId | `academyCategories` |
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

## POST /api/admin/content/academy-categories

- 摘要：新增学堂栏目
- 标签：admin-production-content

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/admin/content/academy-categories` |
| OperationId | `createAcademyCategory` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

- 当前接口没有显式参数。

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | - |
| `name` | `string` | 否 | - |
| `icon` | `string` | 否 | - |
| `description` | `string` | 否 | - |
| `sortOrder` | `integer` | 否 | - |
| `enabled` | `boolean` | 否 | - |

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

## DELETE /api/admin/content/academy-categories/{id}

- 摘要：删除学堂栏目
- 标签：admin-production-content

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `DELETE` |
| 路径 | `/api/admin/content/academy-categories/{id}` |
| OperationId | `deleteAcademyCategory` |
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
| `data` | `boolean` | 否 | - |

## GET /api/admin/content/academy-categories/{id}

- 摘要：查询学堂栏目详情
- 标签：admin-production-content

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/admin/content/academy-categories/{id}` |
| OperationId | `academyCategory` |
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

## PUT /api/admin/content/academy-categories/{id}

- 摘要：更新学堂栏目
- 标签：admin-production-content

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `PUT` |
| 路径 | `/api/admin/content/academy-categories/{id}` |
| OperationId | `updateAcademyCategory` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | - |
| `name` | `string` | 否 | - |
| `icon` | `string` | 否 | - |
| `description` | `string` | 否 | - |
| `sortOrder` | `integer` | 否 | - |
| `enabled` | `boolean` | 否 | - |

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

## GET /api/admin/content/agreements

- 摘要：查询协议列表
- 标签：admin-content

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/admin/content/agreements` |
| OperationId | `agreements` |
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
| `data` | `array<AgreementPayload>` | 否 | - |
| `data` | `array<AgreementPayload>` | 否 | - |
| `data[].id` | `integer` | 否 | 主键 ID |
| `data[].title` | `string` | 是 | 标题 |
| `data[].version` | `string` | 是 | 版本号 |
| `data[].content` | `string` | 是 | 正文 |
| `data[].enabled` | `boolean` | 否 | 是否启用 |

## POST /api/admin/content/agreements

- 摘要：新增协议
- 标签：admin-content

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/admin/content/agreements` |
| OperationId | `createAgreement` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

- 当前接口没有显式参数。

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `title` | `string` | 是 | 标题 |
| `version` | `string` | 是 | 版本号 |
| `content` | `string` | 是 | 正文 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应说明

#### 响应 `200`

- 说明：OK

- Content-Type: `*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `AgreementPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.title` | `string` | 是 | 标题 |
| `data.version` | `string` | 是 | 版本号 |
| `data.content` | `string` | 是 | 正文 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## DELETE /api/admin/content/agreements/{id}

- 摘要：删除协议
- 标签：admin-content

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `DELETE` |
| 路径 | `/api/admin/content/agreements/{id}` |
| OperationId | `deleteAgreement` |
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
| `data` | `boolean` | 否 | - |

## GET /api/admin/content/agreements/{id}

- 摘要：查询协议详情
- 标签：admin-content

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/admin/content/agreements/{id}` |
| OperationId | `agreement` |
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
| `data` | `AgreementPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.title` | `string` | 是 | 标题 |
| `data.version` | `string` | 是 | 版本号 |
| `data.content` | `string` | 是 | 正文 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## PUT /api/admin/content/agreements/{id}

- 摘要：更新协议
- 标签：admin-content

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `PUT` |
| 路径 | `/api/admin/content/agreements/{id}` |
| OperationId | `updateAgreement` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `title` | `string` | 是 | 标题 |
| `version` | `string` | 是 | 版本号 |
| `content` | `string` | 是 | 正文 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应说明

#### 响应 `200`

- 说明：OK

- Content-Type: `*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `AgreementPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.title` | `string` | 是 | 标题 |
| `data.version` | `string` | 是 | 版本号 |
| `data.content` | `string` | 是 | 正文 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## GET /api/admin/content/banners

- 摘要：查询 Banner 列表
- 标签：admin-content

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/admin/content/banners` |
| OperationId | `banners` |
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
| `data` | `array<BannerPayload>` | 否 | - |
| `data` | `array<BannerPayload>` | 否 | - |
| `data[].id` | `integer` | 否 | 主键 ID |
| `data[].title` | `string` | 是 | 标题 |
| `data[].subtitle` | `string` | 否 | 副标题 |
| `data[].image` | `string` | 是 | 图片地址 |
| `data[].link` | `string` | 是 | 跳转链接 |
| `data[].sortOrder` | `integer` | 是 | 排序值 |
| `data[].enabled` | `boolean` | 否 | 是否启用 |

## POST /api/admin/content/banners

- 摘要：新增 Banner
- 标签：admin-content

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/admin/content/banners` |
| OperationId | `createBanner` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

- 当前接口没有显式参数。

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `title` | `string` | 是 | 标题 |
| `subtitle` | `string` | 否 | 副标题 |
| `image` | `string` | 是 | 图片地址 |
| `link` | `string` | 是 | 跳转链接 |
| `sortOrder` | `integer` | 是 | 排序值 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应说明

#### 响应 `200`

- 说明：OK

- Content-Type: `*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `BannerPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.title` | `string` | 是 | 标题 |
| `data.subtitle` | `string` | 否 | 副标题 |
| `data.image` | `string` | 是 | 图片地址 |
| `data.link` | `string` | 是 | 跳转链接 |
| `data.sortOrder` | `integer` | 是 | 排序值 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## DELETE /api/admin/content/banners/{id}

- 摘要：删除 Banner
- 标签：admin-content

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `DELETE` |
| 路径 | `/api/admin/content/banners/{id}` |
| OperationId | `deleteBanner` |
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
| `data` | `boolean` | 否 | - |

## GET /api/admin/content/banners/{id}

- 摘要：查询 Banner 详情
- 标签：admin-content

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/admin/content/banners/{id}` |
| OperationId | `banner` |
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
| `data` | `BannerPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.title` | `string` | 是 | 标题 |
| `data.subtitle` | `string` | 否 | 副标题 |
| `data.image` | `string` | 是 | 图片地址 |
| `data.link` | `string` | 是 | 跳转链接 |
| `data.sortOrder` | `integer` | 是 | 排序值 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## PUT /api/admin/content/banners/{id}

- 摘要：更新 Banner
- 标签：admin-content

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `PUT` |
| 路径 | `/api/admin/content/banners/{id}` |
| OperationId | `updateBanner` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `title` | `string` | 是 | 标题 |
| `subtitle` | `string` | 否 | 副标题 |
| `image` | `string` | 是 | 图片地址 |
| `link` | `string` | 是 | 跳转链接 |
| `sortOrder` | `integer` | 是 | 排序值 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应说明

#### 响应 `200`

- 说明：OK

- Content-Type: `*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `BannerPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.title` | `string` | 是 | 标题 |
| `data.subtitle` | `string` | 否 | 副标题 |
| `data.image` | `string` | 是 | 图片地址 |
| `data.link` | `string` | 是 | 跳转链接 |
| `data.sortOrder` | `integer` | 是 | 排序值 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## GET /api/admin/content/community-posts

- 摘要：查询圈子帖子
- 标签：admin-production-content

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/admin/content/community-posts` |
| OperationId | `communityPosts_1` |
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

## POST /api/admin/content/community-posts

- 摘要：新增圈子帖子
- 标签：admin-production-content

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/admin/content/community-posts` |
| OperationId | `createCommunityPost_1` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

- 当前接口没有显式参数。

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | - |
| `cityName` | `string` | 否 | - |
| `title` | `string` | 否 | - |
| `content` | `string` | 否 | - |
| `coverImage` | `string` | 否 | - |
| `images` | `string` | 否 | - |
| `authorName` | `string` | 否 | - |
| `likeCount` | `integer` | 否 | - |
| `commentCount` | `integer` | 否 | - |
| `published` | `boolean` | 否 | - |

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

## DELETE /api/admin/content/community-posts/{id}

- 摘要：删除圈子帖子
- 标签：admin-production-content

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `DELETE` |
| 路径 | `/api/admin/content/community-posts/{id}` |
| OperationId | `deleteCommunityPost` |
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
| `data` | `boolean` | 否 | - |

## GET /api/admin/content/community-posts/{id}

- 摘要：查询圈子帖子详情
- 标签：admin-production-content

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/admin/content/community-posts/{id}` |
| OperationId | `communityPost_1` |
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

## PUT /api/admin/content/community-posts/{id}

- 摘要：更新圈子帖子
- 标签：admin-production-content

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `PUT` |
| 路径 | `/api/admin/content/community-posts/{id}` |
| OperationId | `updateCommunityPost` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | - |
| `cityName` | `string` | 否 | - |
| `title` | `string` | 否 | - |
| `content` | `string` | 否 | - |
| `coverImage` | `string` | 否 | - |
| `images` | `string` | 否 | - |
| `authorName` | `string` | 否 | - |
| `likeCount` | `integer` | 否 | - |
| `commentCount` | `integer` | 否 | - |
| `published` | `boolean` | 否 | - |

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

## GET /api/admin/content/community-reports

- 摘要：查询圈子举报
- 标签：admin-production-content

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/admin/content/community-reports` |
| OperationId | `communityReports` |
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

## POST /api/admin/content/community-reports

- 摘要：新增圈子举报
- 标签：admin-production-content

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/admin/content/community-reports` |
| OperationId | `createCommunityReport` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

- 当前接口没有显式参数。

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | - |
| `postId` | `integer` | 否 | - |
| `reason` | `string` | 否 | - |
| `detail` | `string` | 否 | - |
| `reporterName` | `string` | 否 | - |
| `status` | `string` | 否 | - |
| `handled` | `boolean` | 否 | - |

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

## DELETE /api/admin/content/community-reports/{id}

- 摘要：删除圈子举报
- 标签：admin-production-content

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `DELETE` |
| 路径 | `/api/admin/content/community-reports/{id}` |
| OperationId | `deleteCommunityReport` |
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
| `data` | `boolean` | 否 | - |

## GET /api/admin/content/community-reports/{id}

- 摘要：查询圈子举报详情
- 标签：admin-production-content

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/admin/content/community-reports/{id}` |
| OperationId | `communityReport` |
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

## PUT /api/admin/content/community-reports/{id}

- 摘要：更新圈子举报
- 标签：admin-production-content

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `PUT` |
| 路径 | `/api/admin/content/community-reports/{id}` |
| OperationId | `updateCommunityReport` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | - |
| `postId` | `integer` | 否 | - |
| `reason` | `string` | 否 | - |
| `detail` | `string` | 否 | - |
| `reporterName` | `string` | 否 | - |
| `status` | `string` | 否 | - |
| `handled` | `boolean` | 否 | - |

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

## GET /api/admin/content/ecosystem-cards

- 摘要：查询生态卡配置
- 标签：admin-production-content

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/admin/content/ecosystem-cards` |
| OperationId | `ecosystemCards` |
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

## POST /api/admin/content/ecosystem-cards

- 摘要：新增生态卡
- 标签：admin-production-content

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/admin/content/ecosystem-cards` |
| OperationId | `createEcosystemCard` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

- 当前接口没有显式参数。

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | - |
| `name` | `string` | 否 | - |
| `description` | `string` | 否 | - |
| `icon` | `string` | 否 | - |
| `color` | `string` | 否 | - |
| `link` | `string` | 否 | - |
| `sortOrder` | `integer` | 否 | - |
| `enabled` | `boolean` | 否 | - |

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

## DELETE /api/admin/content/ecosystem-cards/{id}

- 摘要：删除生态卡
- 标签：admin-production-content

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `DELETE` |
| 路径 | `/api/admin/content/ecosystem-cards/{id}` |
| OperationId | `deleteEcosystemCard` |
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
| `data` | `boolean` | 否 | - |

## GET /api/admin/content/ecosystem-cards/{id}

- 摘要：查询生态卡详情
- 标签：admin-production-content

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/admin/content/ecosystem-cards/{id}` |
| OperationId | `ecosystemCard` |
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

## PUT /api/admin/content/ecosystem-cards/{id}

- 摘要：更新生态卡
- 标签：admin-production-content

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `PUT` |
| 路径 | `/api/admin/content/ecosystem-cards/{id}` |
| OperationId | `updateEcosystemCard` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | - |
| `name` | `string` | 否 | - |
| `description` | `string` | 否 | - |
| `icon` | `string` | 否 | - |
| `color` | `string` | 否 | - |
| `link` | `string` | 否 | - |
| `sortOrder` | `integer` | 否 | - |
| `enabled` | `boolean` | 否 | - |

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

## GET /api/admin/content/notices

- 摘要：查询公告列表
- 标签：admin-content

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/admin/content/notices` |
| OperationId | `notices` |
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
| `data` | `array<NoticePayload>` | 否 | - |
| `data` | `array<NoticePayload>` | 否 | - |
| `data[].id` | `integer` | 否 | 主键 ID |
| `data[].title` | `string` | 是 | 标题 |
| `data[].content` | `string` | 是 | 内容 |
| `data[].levelCode` | `string` | 是 | 级别 |
| `data[].enabled` | `boolean` | 否 | 是否启用 |

## POST /api/admin/content/notices

- 摘要：新增公告
- 标签：admin-content

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/admin/content/notices` |
| OperationId | `createNotice` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

- 当前接口没有显式参数。

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `title` | `string` | 是 | 标题 |
| `content` | `string` | 是 | 内容 |
| `levelCode` | `string` | 是 | 级别 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应说明

#### 响应 `200`

- 说明：OK

- Content-Type: `*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `NoticePayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.title` | `string` | 是 | 标题 |
| `data.content` | `string` | 是 | 内容 |
| `data.levelCode` | `string` | 是 | 级别 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## DELETE /api/admin/content/notices/{id}

- 摘要：删除公告
- 标签：admin-content

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `DELETE` |
| 路径 | `/api/admin/content/notices/{id}` |
| OperationId | `deleteNotice` |
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
| `data` | `boolean` | 否 | - |

## GET /api/admin/content/notices/{id}

- 摘要：查询公告详情
- 标签：admin-content

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/admin/content/notices/{id}` |
| OperationId | `notice` |
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
| `data` | `NoticePayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.title` | `string` | 是 | 标题 |
| `data.content` | `string` | 是 | 内容 |
| `data.levelCode` | `string` | 是 | 级别 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## PUT /api/admin/content/notices/{id}

- 摘要：更新公告
- 标签：admin-content

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `PUT` |
| 路径 | `/api/admin/content/notices/{id}` |
| OperationId | `updateNotice` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `title` | `string` | 是 | 标题 |
| `content` | `string` | 是 | 内容 |
| `levelCode` | `string` | 是 | 级别 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应说明

#### 响应 `200`

- 说明：OK

- Content-Type: `*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `NoticePayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.title` | `string` | 是 | 标题 |
| `data.content` | `string` | 是 | 内容 |
| `data.levelCode` | `string` | 是 | 级别 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## GET /api/admin/dashboard

- 摘要：查询后台仪表盘
- 标签：admin-dashboard

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/admin/dashboard` |
| OperationId | `dashboard_1` |
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

## GET /api/admin/dispatch

- 摘要：查询调度任务列表
- 标签：admin-dashboard

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/admin/dispatch` |
| OperationId | `dispatch` |
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

## GET /api/admin/dispatch/{taskId}

- 摘要：查询调度任务详情
- 标签：admin-business

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/admin/dispatch/{taskId}` |
| OperationId | `dispatchDetail` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `taskId` | path | 是 | `string` | - |

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

## POST /api/admin/dispatch/{taskId}/assign

- 摘要：后台指派师傅
- 标签：admin-business

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/admin/dispatch/{taskId}/assign` |
| OperationId | `assignDispatch` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `taskId` | path | 是 | `string` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `masterUserId` | `integer` | 否 | 目标师傅用户 ID |
| `masterName` | `string` | 否 | 目标师傅姓名，和 masterUserId 二选一即可 |

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

## POST /api/admin/dispatch/{taskId}/cancel-order

- 摘要：从调度中心取消订单
- 标签：admin-business

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/admin/dispatch/{taskId}/cancel-order` |
| OperationId | `cancelDispatchOrder` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `taskId` | path | 是 | `string` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `reason` | `string` | 否 | 原因或备注 |

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

## GET /api/admin/dispatch/zones

- 摘要：查询服务区域列表
- 标签：admin-pricing

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/admin/dispatch/zones` |
| OperationId | `dispatchZones` |
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
| `data` | `array<DispatchZonePayload>` | 否 | - |
| `data` | `array<DispatchZonePayload>` | 否 | - |
| `data[].id` | `integer` | 否 | 主键 ID |
| `data[].cityName` | `string` | 是 | 城市名称 |
| `data[].districtName` | `string` | 是 | 区县名称 |
| `data[].sortOrder` | `integer` | 是 | 排序值 |
| `data[].enabled` | `boolean` | 否 | 是否启用 |

## POST /api/admin/dispatch/zones

- 摘要：新增服务区域
- 标签：admin-pricing

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/admin/dispatch/zones` |
| OperationId | `createDispatchZone` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

- 当前接口没有显式参数。

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `cityName` | `string` | 是 | 城市名称 |
| `districtName` | `string` | 是 | 区县名称 |
| `sortOrder` | `integer` | 是 | 排序值 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应说明

#### 响应 `200`

- 说明：OK

- Content-Type: `*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `DispatchZonePayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.cityName` | `string` | 是 | 城市名称 |
| `data.districtName` | `string` | 是 | 区县名称 |
| `data.sortOrder` | `integer` | 是 | 排序值 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## DELETE /api/admin/dispatch/zones/{id}

- 摘要：删除服务区域
- 标签：admin-pricing

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `DELETE` |
| 路径 | `/api/admin/dispatch/zones/{id}` |
| OperationId | `deleteDispatchZone` |
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
| `data` | `boolean` | 否 | - |

## GET /api/admin/dispatch/zones/{id}

- 摘要：查询服务区域详情
- 标签：admin-pricing

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/admin/dispatch/zones/{id}` |
| OperationId | `dispatchZone` |
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
| `data` | `DispatchZonePayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.cityName` | `string` | 是 | 城市名称 |
| `data.districtName` | `string` | 是 | 区县名称 |
| `data.sortOrder` | `integer` | 是 | 排序值 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## PUT /api/admin/dispatch/zones/{id}

- 摘要：更新服务区域
- 标签：admin-pricing

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `PUT` |
| 路径 | `/api/admin/dispatch/zones/{id}` |
| OperationId | `updateDispatchZone` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `cityName` | `string` | 是 | 城市名称 |
| `districtName` | `string` | 是 | 区县名称 |
| `sortOrder` | `integer` | 是 | 排序值 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应说明

#### 响应 `200`

- 说明：OK

- Content-Type: `*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `DispatchZonePayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.cityName` | `string` | 是 | 城市名称 |
| `data.districtName` | `string` | 是 | 区县名称 |
| `data.sortOrder` | `integer` | 是 | 排序值 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## GET /api/admin/finance

- 摘要：查询财务结算列表
- 标签：admin-dashboard

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/admin/finance` |
| OperationId | `finance` |
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

## GET /api/admin/finance/{billNo}

- 摘要：查询财务单详情
- 标签：admin-business

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/admin/finance/{billNo}` |
| OperationId | `financeDetail` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `billNo` | path | 是 | `string` | - |

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

## POST /api/admin/finance/{billNo}/approve

- 摘要：审核财务单
- 标签：admin-business

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/admin/finance/{billNo}/approve` |
| OperationId | `approveFinance` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `billNo` | path | 是 | `string` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `remark` | `string` | 否 | 审核备注 |

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

## GET /api/admin/marketing/coupons

- 摘要：查询优惠券列表
- 标签：admin-marketing

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/admin/marketing/coupons` |
| OperationId | `coupons_1` |
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
| `data` | `array<CouponPayload>` | 否 | - |
| `data` | `array<CouponPayload>` | 否 | - |
| `data[].id` | `integer` | 否 | 主键 ID |
| `data[].title` | `string` | 是 | 标题 |
| `data[].amount` | `number` | 是 | 面额 |
| `data[].thresholdText` | `string` | 是 | 门槛说明 |
| `data[].expireAt` | `string` | 是 | 过期时间 |
| `data[].enabled` | `boolean` | 否 | 是否启用 |

## POST /api/admin/marketing/coupons

- 摘要：新增优惠券
- 标签：admin-marketing

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/admin/marketing/coupons` |
| OperationId | `createCoupon` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

- 当前接口没有显式参数。

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `title` | `string` | 是 | 标题 |
| `amount` | `number` | 是 | 面额 |
| `thresholdText` | `string` | 是 | 门槛说明 |
| `expireAt` | `string` | 是 | 过期时间 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应说明

#### 响应 `200`

- 说明：OK

- Content-Type: `*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `CouponPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.title` | `string` | 是 | 标题 |
| `data.amount` | `number` | 是 | 面额 |
| `data.thresholdText` | `string` | 是 | 门槛说明 |
| `data.expireAt` | `string` | 是 | 过期时间 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## DELETE /api/admin/marketing/coupons/{id}

- 摘要：删除优惠券
- 标签：admin-marketing

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `DELETE` |
| 路径 | `/api/admin/marketing/coupons/{id}` |
| OperationId | `deleteCoupon` |
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
| `data` | `boolean` | 否 | - |

## GET /api/admin/marketing/coupons/{id}

- 摘要：查询优惠券详情
- 标签：admin-marketing

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/admin/marketing/coupons/{id}` |
| OperationId | `coupon` |
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
| `data` | `CouponPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.title` | `string` | 是 | 标题 |
| `data.amount` | `number` | 是 | 面额 |
| `data.thresholdText` | `string` | 是 | 门槛说明 |
| `data.expireAt` | `string` | 是 | 过期时间 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## PUT /api/admin/marketing/coupons/{id}

- 摘要：更新优惠券
- 标签：admin-marketing

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `PUT` |
| 路径 | `/api/admin/marketing/coupons/{id}` |
| OperationId | `updateCoupon` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `title` | `string` | 是 | 标题 |
| `amount` | `number` | 是 | 面额 |
| `thresholdText` | `string` | 是 | 门槛说明 |
| `expireAt` | `string` | 是 | 过期时间 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应说明

#### 响应 `200`

- 说明：OK

- Content-Type: `*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `CouponPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.title` | `string` | 是 | 标题 |
| `data.amount` | `number` | 是 | 面额 |
| `data.thresholdText` | `string` | 是 | 门槛说明 |
| `data.expireAt` | `string` | 是 | 过期时间 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## GET /api/admin/marketing/member-levels

- 摘要：查询会员等级列表
- 标签：admin-marketing

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/admin/marketing/member-levels` |
| OperationId | `memberLevels` |
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
| `data` | `array<MemberLevelPayload>` | 否 | - |
| `data` | `array<MemberLevelPayload>` | 否 | - |
| `data[].id` | `integer` | 否 | 主键 ID |
| `data[].name` | `string` | 是 | 等级名称 |
| `data[].benefitText` | `string` | 是 | 权益说明 |
| `data[].pointsRequired` | `integer` | 是 | 所需积分 |
| `data[].enabled` | `boolean` | 否 | 是否启用 |

## POST /api/admin/marketing/member-levels

- 摘要：新增会员等级
- 标签：admin-marketing

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/admin/marketing/member-levels` |
| OperationId | `createMemberLevel` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

- 当前接口没有显式参数。

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `name` | `string` | 是 | 等级名称 |
| `benefitText` | `string` | 是 | 权益说明 |
| `pointsRequired` | `integer` | 是 | 所需积分 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应说明

#### 响应 `200`

- 说明：OK

- Content-Type: `*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `MemberLevelPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.name` | `string` | 是 | 等级名称 |
| `data.benefitText` | `string` | 是 | 权益说明 |
| `data.pointsRequired` | `integer` | 是 | 所需积分 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## DELETE /api/admin/marketing/member-levels/{id}

- 摘要：删除会员等级
- 标签：admin-marketing

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `DELETE` |
| 路径 | `/api/admin/marketing/member-levels/{id}` |
| OperationId | `deleteMemberLevel` |
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
| `data` | `boolean` | 否 | - |

## GET /api/admin/marketing/member-levels/{id}

- 摘要：查询会员等级详情
- 标签：admin-marketing

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/admin/marketing/member-levels/{id}` |
| OperationId | `memberLevel` |
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
| `data` | `MemberLevelPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.name` | `string` | 是 | 等级名称 |
| `data.benefitText` | `string` | 是 | 权益说明 |
| `data.pointsRequired` | `integer` | 是 | 所需积分 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## PUT /api/admin/marketing/member-levels/{id}

- 摘要：更新会员等级
- 标签：admin-marketing

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `PUT` |
| 路径 | `/api/admin/marketing/member-levels/{id}` |
| OperationId | `updateMemberLevel` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `name` | `string` | 是 | 等级名称 |
| `benefitText` | `string` | 是 | 权益说明 |
| `pointsRequired` | `integer` | 是 | 所需积分 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应说明

#### 响应 `200`

- 说明：OK

- Content-Type: `*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `MemberLevelPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.name` | `string` | 是 | 等级名称 |
| `data.benefitText` | `string` | 是 | 权益说明 |
| `data.pointsRequired` | `integer` | 是 | 所需积分 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## GET /api/admin/masters

- 摘要：查询师傅列表
- 标签：admin-dashboard

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/admin/masters` |
| OperationId | `masters` |
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

## GET /api/admin/masters/{userId}

- 摘要：查询师傅详情
- 标签：admin-business

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/admin/masters/{userId}` |
| OperationId | `masterDetail` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `userId` | path | 是 | `integer` | - |

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

## PUT /api/admin/masters/{userId}

- 摘要：更新师傅资料
- 标签：admin-business

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `PUT` |
| 路径 | `/api/admin/masters/{userId}` |
| OperationId | `updateMaster` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
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

## POST /api/admin/masters/{userId}/credit-score

- 摘要：调整师傅信用分
- 标签：admin-business

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/admin/masters/{userId}/credit-score` |
| OperationId | `updateCreditScore` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `userId` | path | 是 | `integer` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `creditScore` | `integer` | 否 | 最新信用分 |

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

## POST /api/admin/masters/{userId}/disable

- 摘要：停用师傅
- 标签：admin-business

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/admin/masters/{userId}/disable` |
| OperationId | `disableMaster` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `userId` | path | 是 | `integer` | - |

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

## POST /api/admin/masters/{userId}/enable

- 摘要：启用师傅
- 标签：admin-business

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/admin/masters/{userId}/enable` |
| OperationId | `enableMaster` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `userId` | path | 是 | `integer` | - |

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

## GET /api/admin/notifications

- 摘要：查询通知任务列表
- 标签：admin-dashboard

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/admin/notifications` |
| OperationId | `notifications_1` |
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

## POST /api/admin/notifications/dispatch

- 摘要：手动执行通知任务
- 标签：admin-dashboard

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/admin/notifications/dispatch` |
| OperationId | `dispatchNotifications` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `limit` | query | 否 | `integer` | - |

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

## GET /api/admin/orders

- 摘要：查询后台订单列表
- 标签：admin-dashboard

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/admin/orders` |
| OperationId | `orders` |
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

## GET /api/admin/orders/{orderId}

- 摘要：查询订单详情
- 标签：admin-business

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/admin/orders/{orderId}` |
| OperationId | `orderDetail` |
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

## PUT /api/admin/orders/{orderId}/appointment

- 摘要：后台改预约
- 标签：admin-business

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `PUT` |
| 路径 | `/api/admin/orders/{orderId}/appointment` |
| OperationId | `updateAppointment` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `orderId` | path | 是 | `string` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `appointment` | `string` | 是 | 新的预约时间 |

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

## POST /api/admin/orders/{orderId}/cancel

- 摘要：后台取消订单
- 标签：admin-business

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/admin/orders/{orderId}/cancel` |
| OperationId | `cancelOrder` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `orderId` | path | 是 | `string` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `reason` | `string` | 否 | 原因或备注 |

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

## POST /api/admin/orders/{orderId}/grant-coupon

- 摘要：后台手工发券
- 标签：admin-business

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/admin/orders/{orderId}/grant-coupon` |
| OperationId | `grantCoupon` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `orderId` | path | 是 | `string` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `couponId` | `integer` | 否 | 优惠券 ID，为空时默认发放第一张可用券 |
| `remark` | `string` | 否 | 发券备注 |

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

## POST /api/admin/orders/{orderId}/refund

- 摘要：后台发起或审核退款
- 标签：admin-business

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/admin/orders/{orderId}/refund` |
| OperationId | `refundOrder` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `orderId` | path | 是 | `string` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `reason` | `string` | 否 | 原因或备注 |

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

## GET /api/admin/pricing

- 摘要：查询定价规则总览
- 标签：admin-dashboard

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/admin/pricing` |
| OperationId | `pricing` |
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

## GET /api/admin/pricing/rules

- 摘要：查询定价规则列表
- 标签：admin-pricing

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/admin/pricing/rules` |
| OperationId | `pricingRules` |
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
| `data` | `array<PricingRulePayload>` | 否 | - |
| `data` | `array<PricingRulePayload>` | 否 | - |
| `data[].id` | `integer` | 否 | 主键 ID |
| `data[].categoryId` | `integer` | 是 | 类目 ID |
| `data[].label` | `string` | 是 | 规则名称 |
| `data[].basePrice` | `number` | 是 | 基础价 |
| `data[].coefficient` | `string` | 否 | 系数说明 |
| `data[].guidePrice` | `string` | 否 | 指导价说明 |
| `data[].enabled` | `boolean` | 否 | 是否启用 |

## POST /api/admin/pricing/rules

- 摘要：新增定价规则
- 标签：admin-pricing

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/admin/pricing/rules` |
| OperationId | `createPricingRule` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

- 当前接口没有显式参数。

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `categoryId` | `integer` | 是 | 类目 ID |
| `label` | `string` | 是 | 规则名称 |
| `basePrice` | `number` | 是 | 基础价 |
| `coefficient` | `string` | 否 | 系数说明 |
| `guidePrice` | `string` | 否 | 指导价说明 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应说明

#### 响应 `200`

- 说明：OK

- Content-Type: `*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `PricingRulePayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.categoryId` | `integer` | 是 | 类目 ID |
| `data.label` | `string` | 是 | 规则名称 |
| `data.basePrice` | `number` | 是 | 基础价 |
| `data.coefficient` | `string` | 否 | 系数说明 |
| `data.guidePrice` | `string` | 否 | 指导价说明 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## DELETE /api/admin/pricing/rules/{id}

- 摘要：删除定价规则
- 标签：admin-pricing

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `DELETE` |
| 路径 | `/api/admin/pricing/rules/{id}` |
| OperationId | `deletePricingRule` |
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
| `data` | `boolean` | 否 | - |

## GET /api/admin/pricing/rules/{id}

- 摘要：查询定价规则详情
- 标签：admin-pricing

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/admin/pricing/rules/{id}` |
| OperationId | `pricingRule` |
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
| `data` | `PricingRulePayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.categoryId` | `integer` | 是 | 类目 ID |
| `data.label` | `string` | 是 | 规则名称 |
| `data.basePrice` | `number` | 是 | 基础价 |
| `data.coefficient` | `string` | 否 | 系数说明 |
| `data.guidePrice` | `string` | 否 | 指导价说明 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## PUT /api/admin/pricing/rules/{id}

- 摘要：更新定价规则
- 标签：admin-pricing

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `PUT` |
| 路径 | `/api/admin/pricing/rules/{id}` |
| OperationId | `updatePricingRule` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `categoryId` | `integer` | 是 | 类目 ID |
| `label` | `string` | 是 | 规则名称 |
| `basePrice` | `number` | 是 | 基础价 |
| `coefficient` | `string` | 否 | 系数说明 |
| `guidePrice` | `string` | 否 | 指导价说明 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应说明

#### 响应 `200`

- 说明：OK

- Content-Type: `*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `PricingRulePayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.categoryId` | `integer` | 是 | 类目 ID |
| `data.label` | `string` | 是 | 规则名称 |
| `data.basePrice` | `number` | 是 | 基础价 |
| `data.coefficient` | `string` | 否 | 系数说明 |
| `data.guidePrice` | `string` | 否 | 指导价说明 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## GET /api/admin/system/menus

- 摘要：查询菜单列表
- 标签：admin-system

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/admin/system/menus` |
| OperationId | `menus` |
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
| `data` | `array<MenuPayload>` | 否 | - |
| `data` | `array<MenuPayload>` | 否 | - |
| `data[].id` | `integer` | 否 | 主键 ID |
| `data[].name` | `string` | 是 | 菜单名称 |
| `data[].path` | `string` | 是 | 菜单路径 |
| `data[].icon` | `string` | 否 | 图标 |
| `data[].sortOrder` | `integer` | 是 | 排序值 |
| `data[].enabled` | `boolean` | 否 | 是否启用 |

## POST /api/admin/system/menus

- 摘要：新增菜单
- 标签：admin-system

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/admin/system/menus` |
| OperationId | `createMenu` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

- 当前接口没有显式参数。

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `name` | `string` | 是 | 菜单名称 |
| `path` | `string` | 是 | 菜单路径 |
| `icon` | `string` | 否 | 图标 |
| `sortOrder` | `integer` | 是 | 排序值 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应说明

#### 响应 `200`

- 说明：OK

- Content-Type: `*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `MenuPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.name` | `string` | 是 | 菜单名称 |
| `data.path` | `string` | 是 | 菜单路径 |
| `data.icon` | `string` | 否 | 图标 |
| `data.sortOrder` | `integer` | 是 | 排序值 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## DELETE /api/admin/system/menus/{id}

- 摘要：删除菜单
- 标签：admin-system

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `DELETE` |
| 路径 | `/api/admin/system/menus/{id}` |
| OperationId | `deleteMenu` |
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
| `data` | `boolean` | 否 | - |

## GET /api/admin/system/menus/{id}

- 摘要：查询菜单详情
- 标签：admin-system

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/admin/system/menus/{id}` |
| OperationId | `menu` |
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
| `data` | `MenuPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.name` | `string` | 是 | 菜单名称 |
| `data.path` | `string` | 是 | 菜单路径 |
| `data.icon` | `string` | 否 | 图标 |
| `data.sortOrder` | `integer` | 是 | 排序值 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## PUT /api/admin/system/menus/{id}

- 摘要：更新菜单
- 标签：admin-system

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `PUT` |
| 路径 | `/api/admin/system/menus/{id}` |
| OperationId | `updateMenu` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `name` | `string` | 是 | 菜单名称 |
| `path` | `string` | 是 | 菜单路径 |
| `icon` | `string` | 否 | 图标 |
| `sortOrder` | `integer` | 是 | 排序值 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应说明

#### 响应 `200`

- 说明：OK

- Content-Type: `*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `MenuPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.name` | `string` | 是 | 菜单名称 |
| `data.path` | `string` | 是 | 菜单路径 |
| `data.icon` | `string` | 否 | 图标 |
| `data.sortOrder` | `integer` | 是 | 排序值 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## GET /api/admin/system/permissions

- 摘要：查询权限点列表
- 标签：admin-system

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/admin/system/permissions` |
| OperationId | `permissions` |
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
| `data` | `array<PermissionPayload>` | 否 | - |
| `data` | `array<PermissionPayload>` | 否 | - |
| `data[].id` | `integer` | 否 | 主键 ID |
| `data[].code` | `string` | 是 | 权限编码 |
| `data[].name` | `string` | 是 | 权限名称 |
| `data[].description` | `string` | 否 | 描述 |
| `data[].enabled` | `boolean` | 否 | 是否启用 |

## POST /api/admin/system/permissions

- 摘要：新增权限点
- 标签：admin-system

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/admin/system/permissions` |
| OperationId | `createPermission` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

- 当前接口没有显式参数。

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `code` | `string` | 是 | 权限编码 |
| `name` | `string` | 是 | 权限名称 |
| `description` | `string` | 否 | 描述 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应说明

#### 响应 `200`

- 说明：OK

- Content-Type: `*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `PermissionPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.code` | `string` | 是 | 权限编码 |
| `data.name` | `string` | 是 | 权限名称 |
| `data.description` | `string` | 否 | 描述 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## DELETE /api/admin/system/permissions/{id}

- 摘要：删除权限点
- 标签：admin-system

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `DELETE` |
| 路径 | `/api/admin/system/permissions/{id}` |
| OperationId | `deletePermission` |
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
| `data` | `boolean` | 否 | - |

## GET /api/admin/system/permissions/{id}

- 摘要：查询权限点详情
- 标签：admin-system

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/admin/system/permissions/{id}` |
| OperationId | `permission` |
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
| `data` | `PermissionPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.code` | `string` | 是 | 权限编码 |
| `data.name` | `string` | 是 | 权限名称 |
| `data.description` | `string` | 否 | 描述 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## PUT /api/admin/system/permissions/{id}

- 摘要：更新权限点
- 标签：admin-system

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `PUT` |
| 路径 | `/api/admin/system/permissions/{id}` |
| OperationId | `updatePermission` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `code` | `string` | 是 | 权限编码 |
| `name` | `string` | 是 | 权限名称 |
| `description` | `string` | 否 | 描述 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应说明

#### 响应 `200`

- 说明：OK

- Content-Type: `*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `PermissionPayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.code` | `string` | 是 | 权限编码 |
| `data.name` | `string` | 是 | 权限名称 |
| `data.description` | `string` | 否 | 描述 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## GET /api/admin/system/roles

- 摘要：查询角色列表
- 标签：admin-system

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/admin/system/roles` |
| OperationId | `roles` |
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
| `data` | `array<RolePayload>` | 否 | - |
| `data` | `array<RolePayload>` | 否 | - |
| `data[].id` | `integer` | 否 | 主键 ID |
| `data[].code` | `string` | 是 | 角色编码 |
| `data[].name` | `string` | 是 | 角色名称 |
| `data[].description` | `string` | 否 | 描述 |
| `data[].enabled` | `boolean` | 否 | 是否启用 |

## POST /api/admin/system/roles

- 摘要：新增角色
- 标签：admin-system

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/admin/system/roles` |
| OperationId | `createRole` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

- 当前接口没有显式参数。

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `code` | `string` | 是 | 角色编码 |
| `name` | `string` | 是 | 角色名称 |
| `description` | `string` | 否 | 描述 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应说明

#### 响应 `200`

- 说明：OK

- Content-Type: `*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `RolePayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.code` | `string` | 是 | 角色编码 |
| `data.name` | `string` | 是 | 角色名称 |
| `data.description` | `string` | 否 | 描述 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## DELETE /api/admin/system/roles/{id}

- 摘要：删除角色
- 标签：admin-system

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `DELETE` |
| 路径 | `/api/admin/system/roles/{id}` |
| OperationId | `deleteRole` |
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
| `data` | `boolean` | 否 | - |

## GET /api/admin/system/roles/{id}

- 摘要：查询角色详情
- 标签：admin-system

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/admin/system/roles/{id}` |
| OperationId | `role` |
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
| `data` | `RolePayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.code` | `string` | 是 | 角色编码 |
| `data.name` | `string` | 是 | 角色名称 |
| `data.description` | `string` | 否 | 描述 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## PUT /api/admin/system/roles/{id}

- 摘要：更新角色
- 标签：admin-system

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `PUT` |
| 路径 | `/api/admin/system/roles/{id}` |
| OperationId | `updateRole` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `id` | `integer` | 否 | 主键 ID |
| `code` | `string` | 是 | 角色编码 |
| `name` | `string` | 是 | 角色名称 |
| `description` | `string` | 否 | 描述 |
| `enabled` | `boolean` | 否 | 是否启用 |

### 响应说明

#### 响应 `200`

- 说明：OK

- Content-Type: `*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `RolePayload` | 否 | - |
| `data.id` | `integer` | 否 | 主键 ID |
| `data.code` | `string` | 是 | 角色编码 |
| `data.name` | `string` | 是 | 角色名称 |
| `data.description` | `string` | 否 | 描述 |
| `data.enabled` | `boolean` | 否 | 是否启用 |

## GET /api/admin/system/roles/{id}/grants

- 摘要：查询角色菜单与权限绑定
- 标签：admin-system

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/admin/system/roles/{id}/grants` |
| OperationId | `roleGrant` |
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
| `data` | `RoleGrantPayload` | 否 | - |
| `data.roleId` | `integer` | 否 | 角色 ID |
| `data.menuIds` | `array<integer>` | 否 | 菜单 ID 列表 |
| `data.menuIds` | `array<integer>` | 否 | 菜单 ID 列表 |
| `data.permissionIds` | `array<integer>` | 否 | 权限 ID 列表 |
| `data.permissionIds` | `array<integer>` | 否 | 权限 ID 列表 |

## PUT /api/admin/system/roles/{id}/grants

- 摘要：保存角色菜单与权限绑定
- 标签：admin-system

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `PUT` |
| 路径 | `/api/admin/system/roles/{id}/grants` |
| OperationId | `saveRoleGrant` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `roleId` | `integer` | 否 | 角色 ID |
| `menuIds` | `array<integer>` | 否 | 菜单 ID 列表 |
| `menuIds` | `array<integer>` | 否 | 菜单 ID 列表 |
| `permissionIds` | `array<integer>` | 否 | 权限 ID 列表 |
| `permissionIds` | `array<integer>` | 否 | 权限 ID 列表 |

### 响应说明

#### 响应 `200`

- 说明：OK

- Content-Type: `*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `RoleGrantPayload` | 否 | - |
| `data.roleId` | `integer` | 否 | 角色 ID |
| `data.menuIds` | `array<integer>` | 否 | 菜单 ID 列表 |
| `data.menuIds` | `array<integer>` | 否 | 菜单 ID 列表 |
| `data.permissionIds` | `array<integer>` | 否 | 权限 ID 列表 |
| `data.permissionIds` | `array<integer>` | 否 | 权限 ID 列表 |

## POST /api/auth/admin-login

- 摘要：后台账号密码登录
- 标签：auth

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/auth/admin-login` |
| OperationId | `adminLogin` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

- 当前接口没有显式参数。

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `username` | `string` | 是 | - |
| `password` | `string` | 是 | - |

### 响应说明

#### 响应 `200`

- 说明：OK

- Content-Type: `*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `AuthSessionPayload` | 否 | - |
| `data.token` | `string` | 否 | 访问令牌 |
| `data.refreshToken` | `string` | 否 | 刷新令牌 |
| `data.role` | `string` | 否 | 当前登录角色 |
| `data.accessExpiresAt` | `string` | 否 | 访问令牌过期时间 |
| `data.refreshExpiresAt` | `string` | 否 | 刷新令牌过期时间 |
| `data.profile` | `AuthProfilePayload` | 否 | - |
| `data.profile.id` | `integer` | 否 | 用户 ID |
| `data.profile.username` | `string` | 否 | 登录用户名 |
| `data.profile.name` | `string` | 否 | 显示名称 |
| `data.profile.mobile` | `string` | 否 | 手机号 |
| `data.profile.roleCode` | `string` | 否 | 角色编码 |
| `data.profile.roleName` | `string` | 否 | 角色名称 |
| `data.menus` | `array<AuthMenuPayload>` | 否 | - |
| `data.menus` | `array<AuthMenuPayload>` | 否 | - |
| `data.menus[].id` | `integer` | 否 | 菜单 ID |
| `data.menus[].name` | `string` | 否 | 菜单名称 |
| `data.menus[].path` | `string` | 否 | 菜单路径 |
| `data.menus[].icon` | `string` | 否 | 图标 |
| `data.permissions` | `array<string>` | 否 | - |
| `data.permissions` | `array<string>` | 否 | - |

## POST /api/auth/mobile-login

- 摘要：手机号验证码登录
- 标签：auth

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/auth/mobile-login` |
| OperationId | `mobileLogin` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

- 当前接口没有显式参数。

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `mobile` | `string` | 是 | - |
| `code` | `string` | 是 | - |
| `role` | `string` | 否 | - |

### 响应说明

#### 响应 `200`

- 说明：OK

- Content-Type: `*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `AuthSessionPayload` | 否 | - |
| `data.token` | `string` | 否 | 访问令牌 |
| `data.refreshToken` | `string` | 否 | 刷新令牌 |
| `data.role` | `string` | 否 | 当前登录角色 |
| `data.accessExpiresAt` | `string` | 否 | 访问令牌过期时间 |
| `data.refreshExpiresAt` | `string` | 否 | 刷新令牌过期时间 |
| `data.profile` | `AuthProfilePayload` | 否 | - |
| `data.profile.id` | `integer` | 否 | 用户 ID |
| `data.profile.username` | `string` | 否 | 登录用户名 |
| `data.profile.name` | `string` | 否 | 显示名称 |
| `data.profile.mobile` | `string` | 否 | 手机号 |
| `data.profile.roleCode` | `string` | 否 | 角色编码 |
| `data.profile.roleName` | `string` | 否 | 角色名称 |
| `data.menus` | `array<AuthMenuPayload>` | 否 | - |
| `data.menus` | `array<AuthMenuPayload>` | 否 | - |
| `data.menus[].id` | `integer` | 否 | 菜单 ID |
| `data.menus[].name` | `string` | 否 | 菜单名称 |
| `data.menus[].path` | `string` | 否 | 菜单路径 |
| `data.menus[].icon` | `string` | 否 | 图标 |
| `data.permissions` | `array<string>` | 否 | - |
| `data.permissions` | `array<string>` | 否 | - |

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
| `clientType` | `string` | 否 | - |

### 响应说明

#### 响应 `200`

- 说明：OK

- Content-Type: `*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `AuthSessionPayload` | 否 | - |
| `data.token` | `string` | 否 | 访问令牌 |
| `data.refreshToken` | `string` | 否 | 刷新令牌 |
| `data.role` | `string` | 否 | 当前登录角色 |
| `data.accessExpiresAt` | `string` | 否 | 访问令牌过期时间 |
| `data.refreshExpiresAt` | `string` | 否 | 刷新令牌过期时间 |
| `data.profile` | `AuthProfilePayload` | 否 | - |
| `data.profile.id` | `integer` | 否 | 用户 ID |
| `data.profile.username` | `string` | 否 | 登录用户名 |
| `data.profile.name` | `string` | 否 | 显示名称 |
| `data.profile.mobile` | `string` | 否 | 手机号 |
| `data.profile.roleCode` | `string` | 否 | 角色编码 |
| `data.profile.roleName` | `string` | 否 | 角色名称 |
| `data.menus` | `array<AuthMenuPayload>` | 否 | - |
| `data.menus` | `array<AuthMenuPayload>` | 否 | - |
| `data.menus[].id` | `integer` | 否 | 菜单 ID |
| `data.menus[].name` | `string` | 否 | 菜单名称 |
| `data.menus[].path` | `string` | 否 | 菜单路径 |
| `data.menus[].icon` | `string` | 否 | 图标 |
| `data.permissions` | `array<string>` | 否 | - |
| `data.permissions` | `array<string>` | 否 | - |

## POST /api/auth/register

- 摘要：手机号注册
- 标签：auth

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/auth/register` |
| OperationId | `register` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

- 当前接口没有显式参数。

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `mobile` | `string` | 是 | - |
| `code` | `string` | 是 | - |
| `nickname` | `string` | 是 | - |

### 响应说明

#### 响应 `200`

- 说明：OK

- Content-Type: `*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `AuthSessionPayload` | 否 | - |
| `data.token` | `string` | 否 | 访问令牌 |
| `data.refreshToken` | `string` | 否 | 刷新令牌 |
| `data.role` | `string` | 否 | 当前登录角色 |
| `data.accessExpiresAt` | `string` | 否 | 访问令牌过期时间 |
| `data.refreshExpiresAt` | `string` | 否 | 刷新令牌过期时间 |
| `data.profile` | `AuthProfilePayload` | 否 | - |
| `data.profile.id` | `integer` | 否 | 用户 ID |
| `data.profile.username` | `string` | 否 | 登录用户名 |
| `data.profile.name` | `string` | 否 | 显示名称 |
| `data.profile.mobile` | `string` | 否 | 手机号 |
| `data.profile.roleCode` | `string` | 否 | 角色编码 |
| `data.profile.roleName` | `string` | 否 | 角色名称 |
| `data.menus` | `array<AuthMenuPayload>` | 否 | - |
| `data.menus` | `array<AuthMenuPayload>` | 否 | - |
| `data.menus[].id` | `integer` | 否 | 菜单 ID |
| `data.menus[].name` | `string` | 否 | 菜单名称 |
| `data.menus[].path` | `string` | 否 | 菜单路径 |
| `data.menus[].icon` | `string` | 否 | 图标 |
| `data.permissions` | `array<string>` | 否 | - |
| `data.permissions` | `array<string>` | 否 | - |

## POST /api/auth/send-code

- 摘要：发送手机验证码
- 标签：auth

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/auth/send-code` |
| OperationId | `sendCode` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

- 当前接口没有显式参数。

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `mobile` | `string` | 是 | - |
| `purpose` | `string` | 是 | - |

### 响应说明

#### 响应 `200`

- 说明：OK

- Content-Type: `*/*`

#### 响应字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `success` | `boolean` | 否 | - |
| `message` | `string` | 否 | - |
| `data` | `SmsCodePayload` | 否 | - |
| `data.mobile` | `string` | 否 | 手机号 |
| `data.purpose` | `string` | 否 | 用途 |
| `data.expiresInSeconds` | `integer` | 否 | 过期秒数 |

## GET /api/auth/session

- 摘要：读取当前登录态
- 标签：auth

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/auth/session` |
| OperationId | `session` |
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
| `data` | `AuthSessionPayload` | 否 | - |
| `data.token` | `string` | 否 | 访问令牌 |
| `data.refreshToken` | `string` | 否 | 刷新令牌 |
| `data.role` | `string` | 否 | 当前登录角色 |
| `data.accessExpiresAt` | `string` | 否 | 访问令牌过期时间 |
| `data.refreshExpiresAt` | `string` | 否 | 刷新令牌过期时间 |
| `data.profile` | `AuthProfilePayload` | 否 | - |
| `data.profile.id` | `integer` | 否 | 用户 ID |
| `data.profile.username` | `string` | 否 | 登录用户名 |
| `data.profile.name` | `string` | 否 | 显示名称 |
| `data.profile.mobile` | `string` | 否 | 手机号 |
| `data.profile.roleCode` | `string` | 否 | 角色编码 |
| `data.profile.roleName` | `string` | 否 | 角色名称 |
| `data.menus` | `array<AuthMenuPayload>` | 否 | - |
| `data.menus` | `array<AuthMenuPayload>` | 否 | - |
| `data.menus[].id` | `integer` | 否 | 菜单 ID |
| `data.menus[].name` | `string` | 否 | 菜单名称 |
| `data.menus[].path` | `string` | 否 | 菜单路径 |
| `data.menus[].icon` | `string` | 否 | 图标 |
| `data.permissions` | `array<string>` | 否 | - |
| `data.permissions` | `array<string>` | 否 | - |

## POST /api/auth/sms-login

- 摘要：兼容保留：短信登录
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
| `role` | `string` | 否 | - |

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

- 摘要：兼容保留：微信登录
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
| `role` | `string` | 否 | - |

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

## GET /api/community/posts

- 摘要：查询圈子帖子
- 标签：mobile-content

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/community/posts` |
| OperationId | `communityPosts` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `cityName` | query | 否 | `string` | - |

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

## POST /api/community/posts

- 摘要：发布圈子帖子
- 标签：mobile-content

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/community/posts` |
| OperationId | `createCommunityPost` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

- 当前接口没有显式参数。

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `cityName` | `string` | 否 | 城市 |
| `title` | `string` | 否 | 标题 |
| `content` | `string` | 否 | 正文 |
| `images` | `array<string>` | 否 | - |
| `images` | `array<string>` | 否 | - |

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

## GET /api/community/posts/{id}

- 摘要：查询圈子帖子详情
- 标签：mobile-content

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/community/posts/{id}` |
| OperationId | `communityPost` |
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

## GET /api/community/posts/{id}/comments

- 摘要：查询圈子评论
- 标签：mobile-content

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/api/community/posts/{id}/comments` |
| OperationId | `communityComments` |
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

## POST /api/community/posts/{id}/comments

- 摘要：发布圈子评论
- 标签：mobile-content

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/community/posts/{id}/comments` |
| OperationId | `createCommunityComment` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `content` | `string` | 否 | 评论内容 |

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

## POST /api/community/posts/{id}/like

- 摘要：点赞圈子帖子
- 标签：mobile-content

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/community/posts/{id}/like` |
| OperationId | `likeCommunityPost` |
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

## POST /api/community/posts/{id}/report

- 摘要：举报圈子帖子
- 标签：mobile-content

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `POST` |
| 路径 | `/api/community/posts/{id}/report` |
| OperationId | `reportCommunityPost` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

| 参数名 | 位置 | 必填 | 类型 | 说明 |
| --- | --- | --- | --- | --- |
| `id` | path | 是 | `integer` | - |

### 请求体

#### Content-Type: `application/json`

#### 请求字段

| 字段 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `reason` | `string` | 否 | 举报原因 |
| `detail` | `string` | 否 | 补充说明 |

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

## GET /swagger-ui/index.html

- 摘要：未提供
- 标签：open-api-portal-controller

### 请求信息

| 项目 | 值 |
| --- | --- |
| 请求方法 | `GET` |
| 路径 | `/swagger-ui/index.html` |
| OperationId | `redirectToKnife4j` |
| 鉴权 | `bearerAuth` |

### 路径 / 查询参数

- 当前接口没有显式参数。

### 请求体

- 当前接口没有请求体。

### 响应说明

#### 响应 `200`

- 说明：OK

- 当前响应没有结构化响应体。
