package com.huzhiying.server.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 轻量接口文档入口页，统一暴露 OpenAPI 分组地址，便于本地浏览和 Apifox / Apiform 导入。
 */
@RestController
public class OpenApiPortalController {

    @GetMapping(value = "/swagger-ui/index.html", produces = MediaType.TEXT_HTML_VALUE)
    public String portal() {
        return """
                <!doctype html>
                <html lang="zh-CN">
                <head>
                  <meta charset="UTF-8" />
                  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
                  <title>呼之应 OpenAPI 文档入口</title>
                  <style>
                    body { font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", sans-serif; background: #f5f7fb; color: #111827; margin: 0; }
                    main { max-width: 880px; margin: 0 auto; padding: 48px 24px; }
                    h1 { margin: 0 0 12px; font-size: 32px; }
                    p { color: #4b5563; line-height: 1.7; }
                    .card { background: #fff; border-radius: 20px; padding: 24px; box-shadow: 0 16px 48px rgba(17, 24, 39, 0.08); margin-top: 20px; }
                    ul { margin: 16px 0 0; padding-left: 20px; }
                    li { margin: 10px 0; }
                    a { color: #2563eb; text-decoration: none; }
                    code { background: #eef2ff; color: #1d4ed8; padding: 2px 6px; border-radius: 8px; }
                  </style>
                </head>
                <body>
                <main>
                  <h1>呼之应 OpenAPI 文档</h1>
                  <p>将以下 JSON 或 YAML 地址导入 Apifox / Apiform，即可查看每个接口的请求参数、响应参数和示例值。文档已经按端拆分，便于分组联调。</p>
                  <div class="card">
                    <strong>默认文档</strong>
                    <ul>
                      <li><a href="/v3/api-docs" target="_blank">/v3/api-docs</a></li>
                      <li><a href="/v3/api-docs.yaml" target="_blank">/v3/api-docs.yaml</a></li>
                    </ul>
                  </div>
                  <div class="card">
                    <strong>分组文档</strong>
                    <ul>
                      <li><a href="/v3/api-docs/mobile" target="_blank">/v3/api-docs/mobile</a></li>
                      <li><a href="/v3/api-docs/master" target="_blank">/v3/api-docs/master</a></li>
                      <li><a href="/v3/api-docs/admin" target="_blank">/v3/api-docs/admin</a></li>
                      <li><a href="/v3/api-docs/payment" target="_blank">/v3/api-docs/payment</a></li>
                    </ul>
                  </div>
                  <div class="card">
                    <strong>导入建议</strong>
                    <ul>
                      <li>在 Apifox 中选择 <code>导入 -&gt; OpenAPI / Swagger</code>，填入任意一个上面的 URL。</li>
                      <li>如果只联调后台配置页，优先导入 <code>/v3/api-docs/admin</code>。</li>
                      <li>如果移动端与师傅端并行联调，分别导入 <code>/v3/api-docs/mobile</code> 和 <code>/v3/api-docs/master</code>。</li>
                    </ul>
                  </div>
                </main>
                </body>
                </html>
                """;
    }
}
