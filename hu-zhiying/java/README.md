# 呼之应工作区

<!--
  本文件用于说明当前仓库的本地运行方式和接口文档入口。
  1. 三端运行时 mock 分支已从业务代码中移除。
  2. 当前默认联调基线为 Spring Boot test profile + H2。
  3. 接口文档以 Knife4j / OpenAPI / Markdown 三份出口同步维护。
-->

## 仓库结构

- `docs/spec`：项目规格包单一事实来源。
- `hu.md`：历史基线说明。
- `hu-zhiying/uni-app`：C 端与师傅端共用的 uni-app 工程。
- `hu-zhiying/admin`：Vue 3 + Element Plus 管理后台。
- `hu-zhiying/java`：Spring Boot 后端工程。
- `docs/api`：根据 OpenAPI 导出的 Markdown 接口文档。

## 规格文档

- 产品摘要：`docs/spec/PRODUCT-SUMMARY.md`
- 主规格：`docs/spec/PRODUCT-SPEC.md`
- 领域与接口：`docs/spec/DOMAIN-API-SPEC.md`

## 本地启动

1. 进入 Java 工程：`cd hu-zhiying/java`
2. 启动基础依赖：`docker compose up -d`
3. 启动后端：`powershell -ExecutionPolicy Bypass -File .\scripts\start-hu-server.ps1 -Profile test`
4. 启动后台：`cd ../admin && npm.cmd install && npm.cmd run dev`
5. 使用 HBuilderX 打开 `../uni-app` 运行移动端工程

## 当前基线

- `uni-app` 与 `admin` 默认走真实后端接口。
- `mock`、`useMock`、`getAdminMock`、`sandbox` 以及前端伪造支付成功链路已从运行时代码移除。
- 运行时不再依赖演示账号、示例身份或默认示例姓名；`test` profile 的最小种子数据仅用于本地联调与文档运行。
- 后台配置模块与后台业务页已接入真实 CRUD / 动作接口。
- 微信预支付接口在未配置真实商户参数时会明确失败，不再返回演示凭据。
- 后端文档已同时暴露 Knife4j、OpenAPI 分组地址与 Markdown 导出文件。

## 接口文档

- Knife4j：`http://localhost:8080/doc.html`
- 兼容入口：`http://localhost:8080/swagger-ui/index.html`
- 全量 OpenAPI：`http://localhost:8080/v3/api-docs`
- YAML：`http://localhost:8080/v3/api-docs.yaml`
- 移动端分组：`http://localhost:8080/v3/api-docs/mobile`
- 师傅端分组：`http://localhost:8080/v3/api-docs/master`
- 后台分组：`http://localhost:8080/v3/api-docs/admin`
- 支付分组：`http://localhost:8080/v3/api-docs/payment`

## Markdown 文档导出

- 导出脚本：`scripts/export-openapi-markdown.mjs`
- 执行命令：`node scripts/export-openapi-markdown.mjs`
- 输出目录：`docs/api`

## 已验证项

- 后端测试：`cd hu-server && mvn -q test`
- 后台构建：`cd ../admin && npm.cmd run build`

## 剩余人工验证

- `uni-app` 仍需在 HBuilderX、真机或模拟器完成一次完整冒烟。
- 重点关注：首页、上传、地图、聊天、支付唤起以及跨角色订单流转。
