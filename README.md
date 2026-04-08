# 呼之应

## 仓库结构
- `hu.md`：需求文档
- `hu-zhiying/uni-app`：`uni-app` 移动端工程，承载 C 端和师傅端
- `hu-zhiying/admin`：`Vue 3 + Element Plus` 管理后台
- `hu-zhiying/java`：`Spring Boot` 多模块后端

## 本地启动
1. `cd hu-zhiying`
2. `docker compose up -d`
3. `cd java && mvn -pl hu-server spring-boot:run`
4. `cd ../admin && npm.cmd install && npm.cmd run dev`
5. 用 `HBuilderX` 或 `uni CLI` 打开 `hu-zhiying/uni-app`

## 当前状态
- 移动端、师傅端和后台运行时代码都默认走真实后端接口，不再保留 `mock/useMock/getAdminMock/sandbox` 分支。
- 后台配置中心的内容运营、营销会员、系统权限、类目商品、SKU、定价规则、服务区域都已接入真实 CRUD 接口。
- Java 后端已启用 `OpenAPI + Swagger UI`，支持按端分组导出接口文档，可直接导入 `Apifox / Apiform`。
- 微信支付当前采用正式接口路径，未配置商户参数时只会创建订单，不会伪造支付成功。

## OpenAPI 导入地址
- 默认文档：`http://localhost:8080/v3/api-docs`
- YAML 文档：`http://localhost:8080/v3/api-docs.yaml`
- 移动端分组：`http://localhost:8080/v3/api-docs/mobile`
- 师傅端分组：`http://localhost:8080/v3/api-docs/master`
- 后台分组：`http://localhost:8080/v3/api-docs/admin`
- 支付分组：`http://localhost:8080/v3/api-docs/payment`
- 浏览入口：`http://localhost:8080/swagger-ui/index.html`

## 导入建议
- 在 `Apifox / Apiform` 中选择 `导入 OpenAPI / Swagger`，填入上面的任一地址即可。
- 如果只联调后台配置页，优先导入 `/v3/api-docs/admin`。
- 如果移动端和师傅端并行联调，分别导入 `/v3/api-docs/mobile` 和 `/v3/api-docs/master`。

## 已完成验证
- `admin`：`npm.cmd run build`
- `java`：`mvn -q test`

## 待继续验证
- `uni-app` 仍需在 `HBuilderX` 或真机环境完成一次完整冒烟，重点验证上传、地图、聊天和支付唤起流程。
