# 呼之应规格包与落地路线

本文件只保留规格落地路线，完整业务规格已收口到 [`docs/spec`](./docs/spec/README.md)。

## 规格入口

- 产品摘要：[`docs/spec/PRODUCT-SUMMARY.md`](./docs/spec/PRODUCT-SUMMARY.md)
- 主规格：[`docs/spec/PRODUCT-SPEC.md`](./docs/spec/PRODUCT-SPEC.md)
- 领域与接口：[`docs/spec/DOMAIN-API-SPEC.md`](./docs/spec/DOMAIN-API-SPEC.md)

## 落地顺序

### Phase 1：单一事实来源

- 顶层文档改为索引入口，需求、流程、数据、接口统一收口到 `docs/spec`
- 页面矩阵、角色矩阵、状态机、错误码、审计规则进入正式规格
- 文档同步规则固定下来，禁止后续再分裂成多份口头版本

### Phase 2：接口与导出对齐

- `OpenApiConfig` 分组与实际控制器对齐
- `academy/community/admin content` 接口进入导出结果
- OpenAPI 测试补断言，防止后续再次漏导

### Phase 3：实现对齐

- 前端页面、后端 DTO、状态机、后台动作逐项对照规格修正
- 新生产实体按规格逐步实现：支付记录、退款申请、结算单、钱包账务、审计日志、通知任务
- 错误码、权限、审计与消息通知按规格逐步补齐

### Phase 4：验证与上线准备

- 规格一致性检查
- 主链路回归
- 渠道能力回归
- OpenAPI 导出、冒烟、压测、灰度和上线手册

## 当前已落地

- 规格包目录已建立
- 产品、页面、角色、状态机、数据结构、接口、错误码、审计规则已统一成正式文档
- OpenAPI `mobile` 分组将补齐内容接口，导出文档需随服务启动后重新生成
