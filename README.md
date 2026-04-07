# 呼之应

## 仓库结构
- `hu.md`：需求文档
- `hu-zhiying/uni-app`：uni-app 移动端，包含 C 端和 B 端
- `hu-zhiying/admin`：Vue3 + Element Plus 后台
- `hu-zhiying/java`：Spring Boot 多模块后端

## 本地启动
1. `cd hu-zhiying`
2. `docker compose up -d`
3. `cd java && mvn -pl hu-server spring-boot:run`
4. `cd ../admin && npm.cmd install && npm.cmd run dev`
5. `uni-app` 使用 HBuilderX 或 uni CLI 打开 `hu-zhiying/uni-app`

## 说明
- 当前移动端和后台使用本地 mock 数据驱动页面，但后端接口与路径已按规划落地。
- 高德定位已预留真实 key 配置位；微信支付接口已经生成预支付/回调/退款的后端端点骨架，真实商户参数需要你后续补充。
- Java 后端当前使用内存态仓库承载演示数据，方便前后端先并行联调，后续可继续切到 MySQL / Redis / Elasticsearch 实现。
