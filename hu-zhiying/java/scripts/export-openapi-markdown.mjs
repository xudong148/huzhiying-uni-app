import fs from 'node:fs/promises';
import path from 'node:path';

const baseUrl = process.env.HZY_OPENAPI_BASE || process.argv[2] || 'http://localhost:8080';
const outputDir = path.resolve(process.cwd(), 'docs', 'api');
const generatedAt = new Date().toISOString();

const groups = [
  { id: 'all', title: '全量接口文档', sourcePath: '/v3/api-docs', fileName: 'all.md' },
  { id: 'mobile', title: '移动端接口文档', sourcePath: '/v3/api-docs/mobile', fileName: 'mobile.md' },
  { id: 'master', title: '师傅端接口文档', sourcePath: '/v3/api-docs/master', fileName: 'master.md' },
  { id: 'admin', title: '后台接口文档', sourcePath: '/v3/api-docs/admin', fileName: 'admin.md' },
  { id: 'payment', title: '支付接口文档', sourcePath: '/v3/api-docs/payment', fileName: 'payment.md' },
];

async function main() {
  await fs.mkdir(outputDir, { recursive: true });

  const indexSections = [
    '<!--',
    '  接口文档目录页，由 scripts/export-openapi-markdown.mjs 自动生成。',
    '  1. 默认读取本地 http://localhost:8080 的 OpenAPI 接口。',
    '  2. 后端接口契约发生变化后，需要重新执行导出脚本。',
    '  3. 本目录中的 Markdown 用于离线审阅、联调和代码评审。',
    '-->',
    '',
    '# 接口文档目录',
    '',
    '## 生成信息',
    '',
    `- 生成时间：\`${generatedAt}\``,
    `- 来源服务：\`${baseUrl}\``,
    '',
    '## 文档列表',
    '',
  ];

  for (const group of groups) {
    const spec = await fetchOpenApi(group.sourcePath);
    const markdown = renderDocument(spec, group);
    await fs.writeFile(path.join(outputDir, group.fileName), markdown, 'utf8');
    indexSections.push(`- [${group.title}](./${group.fileName})`);
  }

  indexSections.push(
    '',
    '## 说明',
    '',
    `- 交互式界面请使用 \`${baseUrl}/doc.html\`。`,
    '- Apifox / Apiform 可直接导入 `/v3/api-docs` 或各分组地址。',
    '- Markdown 文档适合离线查看和评审，联调时仍以实时接口和 Knife4j 页面为准。',
    ''
  );

  await fs.writeFile(path.join(outputDir, 'README.md'), indexSections.join('\n'), 'utf8');
}

async function fetchOpenApi(sourcePath) {
  const response = await fetch(`${baseUrl}${sourcePath}`);
  if (!response.ok) {
    throw new Error(`获取 OpenAPI 文档失败：${sourcePath} -> ${response.status}`);
  }
  return response.json();
}

function renderDocument(spec, group) {
  const endpointList = collectOperations(spec.paths || {});
  const sections = [
    '<!--',
    `  ${group.title}，由 scripts/export-openapi-markdown.mjs 自动生成。`,
    `  1. OpenAPI 来源：${baseUrl}${group.sourcePath}`,
    '  2. 下文展开展示参数、请求体和响应字段，便于联调查阅。',
    '  3. 接口契约变更后请重新执行导出脚本。',
    '-->',
    '',
    `# ${group.title}`,
    '',
    '## 基本信息',
    '',
    `- 文档标题：\`${spec.info?.title || '未知'}\``,
    `- 版本：\`${spec.info?.version || 'unknown'}\``,
    `- 分组：\`${group.id}\``,
    `- 接口数量：\`${endpointList.length}\``,
    `- OpenAPI 地址：\`${baseUrl}${group.sourcePath}\``,
    '',
    '## 鉴权说明',
    '',
    ...renderSecuritySchemes(spec),
    '',
    '## 接口目录',
    '',
    ...endpointList.map((operation) => `- [${operation.method} ${operation.path}](#${toAnchor(`${operation.method} ${operation.path}`)})`),
    '',
  ];

  endpointList.forEach((operation) => {
    sections.push(...renderOperation(spec, operation));
  });

  return sections.join('\n');
}

function collectOperations(paths) {
  const methods = ['get', 'post', 'put', 'delete', 'patch'];
  const operations = [];
  for (const [pathName, pathItem] of Object.entries(paths)) {
    methods.forEach((method) => {
      if (pathItem?.[method]) {
        operations.push({
          path: pathName,
          method: method.toUpperCase(),
          operation: pathItem[method],
          pathItem,
        });
      }
    });
  }
  return operations.sort((left, right) => `${left.path}-${left.method}`.localeCompare(`${right.path}-${right.method}`));
}

function renderSecuritySchemes(spec) {
  const schemes = spec.components?.securitySchemes || {};
  const entries = Object.entries(schemes);
  if (!entries.length) {
    return ['- 当前分组未声明统一鉴权方案。'];
  }
  return entries.map(([name, scheme]) => {
    const parts = [`- \`${name}\``];
    if (scheme.type) {
      parts.push(`类型: ${scheme.type}`);
    }
    if (scheme.scheme) {
      parts.push(`方案: ${scheme.scheme}`);
    }
    if (scheme.bearerFormat) {
      parts.push(`格式: ${scheme.bearerFormat}`);
    }
    return parts.join(', ');
  });
}

function renderOperation(spec, entry) {
  const operation = entry.operation;
  const sections = [
    `## ${entry.method} ${entry.path}`,
    '',
    `- 摘要：${normalizeText(operation.summary) || '未提供'}`,
    `- 标签：${(operation.tags || []).join(' / ') || '未分类'}`,
  ];

  if (operation.description) {
    sections.push(`- 说明：${normalizeText(operation.description)}`);
  }

  sections.push('');
  sections.push('### 请求信息', '');
  sections.push('| 项目 | 值 |');
  sections.push('| --- | --- |');
  sections.push(`| 请求方法 | \`${entry.method}\` |`);
  sections.push(`| 路径 | \`${entry.path}\` |`);
  sections.push(`| OperationId | \`${operation.operationId || '-'}\` |`);
  sections.push(`| 鉴权 | \`${renderSecurityRequirement(operation.security, spec.security)}\` |`);
  sections.push('');

  const parameters = [...(entry.pathItem.parameters || []), ...(operation.parameters || [])];
  sections.push(...renderParametersTable(spec, parameters));
  sections.push(...renderRequestBody(spec, operation.requestBody));
  sections.push(...renderResponses(spec, operation.responses || {}));

  return sections;
}

function renderSecurityRequirement(operationSecurity, globalSecurity) {
  const security = operationSecurity === undefined ? globalSecurity : operationSecurity;
  if (!security || !security.length) {
    return '无';
  }
  return security
    .map((item) => Object.keys(item).join(', ') || '匿名')
    .join(' | ');
}

function renderParametersTable(spec, parameters) {
  if (!parameters.length) {
    return ['### 路径 / 查询参数', '', '- 当前接口没有显式参数。', ''];
  }
  const rows = dedupeParameters(parameters).map((parameter) => {
    const normalized = resolveParameter(spec, parameter);
    const schema = normalized.schema || {};
    return `| \`${normalized.name}\` | ${normalized.in || '-'} | ${normalized.required ? '是' : '否'} | \`${describeSchemaType(schema, spec.components?.schemas || {})}\` | ${sanitizeText(normalizeText(normalized.description)) || '-'} |`;
  });

  return [
    '### 路径 / 查询参数',
    '',
    '| 参数名 | 位置 | 必填 | 类型 | 说明 |',
    '| --- | --- | --- | --- | --- |',
    ...rows,
    '',
  ];
}

function renderRequestBody(spec, requestBody) {
  if (!requestBody) {
    return ['### 请求体', '', '- 当前接口没有请求体。', ''];
  }

  const normalized = resolveRequestBody(spec, requestBody);
  const sections = ['### 请求体', ''];

  for (const [contentType, media] of Object.entries(normalized.content || {})) {
    sections.push(`#### Content-Type: \`${contentType}\``, '');
    sections.push(...renderSchemaTable('请求字段', media.schema, spec.components?.schemas || {}));
  }

  return sections;
}

function renderResponses(spec, responses) {
  const sections = ['### 响应说明', ''];
  const entries = Object.entries(responses);
  if (!entries.length) {
    sections.push('- 当前接口未声明结构化响应体。', '');
    return sections;
  }

  for (const [statusCode, response] of entries) {
    const normalized = resolveResponse(spec, response);
    sections.push(`#### 响应 \`${statusCode}\``, '');
    if (normalized.description) {
      sections.push(`- 说明：${normalizeText(normalized.description)}`, '');
    }
    const contentEntries = Object.entries(normalized.content || {});
    if (!contentEntries.length) {
      sections.push('- 当前响应没有结构化响应体。', '');
      continue;
    }

    for (const [contentType, media] of contentEntries) {
      sections.push(`- Content-Type: \`${contentType}\``, '');
      sections.push(...renderSchemaTable('响应字段', media.schema, spec.components?.schemas || {}));
    }
  }

  return sections;
}

function renderSchemaTable(title, schema, components) {
  const fields = flattenSchema(schema, components);
  if (!fields.length) {
    return [`#### ${title}`, '', '- 当前结构没有可展开字段。', ''];
  }

  return [
    `#### ${title}`,
    '',
    '| 字段 | 类型 | 必填 | 说明 |',
    '| --- | --- | --- | --- |',
    ...fields.map((field) => `| \`${field.name}\` | \`${field.type}\` | ${field.required ? '是' : '否'} | ${sanitizeText(normalizeText(field.description)) || '-'} |`),
    '',
  ];
}

function flattenSchema(schema, components, prefix = '', required = false, visited = new Set()) {
  if (!schema) {
    return [];
  }

  const directRef = getRefName(schema);
  if (directRef) {
    if (visited.has(`${prefix}:${directRef}`)) {
      return [];
    }
    visited.add(`${prefix}:${directRef}`);
    return flattenSchema(components[directRef], components, prefix, required, visited);
  }

  if (schema.allOf) {
    return schema.allOf.flatMap((item) => flattenSchema(item, components, prefix, required, visited));
  }

  if (schema.oneOf || schema.anyOf) {
    const variants = schema.oneOf || schema.anyOf;
    return [{
      name: prefix || 'value',
      type: variants.map((item) => describeSchemaType(item, components)).join(' | '),
      required,
      description: schema.description || '多态结构',
    }];
  }

  const type = schema.type || inferSchemaType(schema);

  if (type === 'object') {
    const properties = schema.properties || {};
    const requiredFields = new Set(schema.required || []);
    const rows = [];

    if (prefix && Object.keys(properties).length === 0) {
      rows.push({
        name: prefix,
        type: 'object',
        required,
        description: schema.description || '',
      });
    }

    for (const [key, value] of Object.entries(properties)) {
      const fieldName = prefix ? `${prefix}.${key}` : key;
      const fieldType = describeSchemaType(value, components);
      const fieldRequired = requiredFields.has(key);
      rows.push({
        name: fieldName,
        type: fieldType,
        required: fieldRequired,
        description: value.description || '',
      });

      if (shouldExpand(value, components)) {
        rows.push(...flattenSchema(value, components, fieldName, fieldRequired, visited));
      }
    }
    return rows;
  }

  if (type === 'array') {
    const arrayName = prefix || 'items';
    const rows = [{
      name: arrayName,
      type: describeSchemaType(schema, components),
      required,
      description: schema.description || '',
    }];

    if (schema.items && shouldExpand(schema.items, components)) {
      rows.push(...flattenSchema(schema.items, components, `${arrayName}[]`, false, visited));
    }
    return rows;
  }

  return prefix
    ? [{
        name: prefix,
        type: describeSchemaType(schema, components),
        required,
        description: schema.description || '',
      }]
    : [];
}

function shouldExpand(schema, components) {
  if (!schema) {
    return false;
  }
  if (schema.$ref) {
    const refSchema = components[getRefName(schema)] || {};
    return inferSchemaType(refSchema) === 'object' || inferSchemaType(refSchema) === 'array';
  }
  return inferSchemaType(schema) === 'object' || inferSchemaType(schema) === 'array' || Boolean(schema.allOf);
}

function describeSchemaType(schema, components) {
  if (!schema) {
    return 'object';
  }
  if (schema.$ref) {
    const refName = getRefName(schema);
    return refName || 'object';
  }
  if (schema.allOf) {
    return schema.allOf.map((item) => describeSchemaType(item, components)).join(' + ');
  }
  if (schema.oneOf) {
    return schema.oneOf.map((item) => describeSchemaType(item, components)).join(' | ');
  }
  if (schema.anyOf) {
    return schema.anyOf.map((item) => describeSchemaType(item, components)).join(' | ');
  }
  const type = inferSchemaType(schema);
  if (type === 'array') {
    return `array<${describeSchemaType(schema.items, components)}>`;
  }
  if (schema.enum?.length) {
    return `${type} (${schema.enum.join(' / ')})`;
  }
  return type;
}

function inferSchemaType(schema) {
  if (schema.type) {
    return schema.type;
  }
  if (schema.properties) {
    return 'object';
  }
  if (schema.items) {
    return 'array';
  }
  return 'object';
}

function resolveParameter(spec, parameter) {
  if (parameter.$ref) {
    const refName = parameter.$ref.split('/').pop();
    return spec.components?.parameters?.[refName] || {};
  }
  return parameter;
}

function resolveRequestBody(spec, requestBody) {
  if (requestBody.$ref) {
    const refName = requestBody.$ref.split('/').pop();
    return spec.components?.requestBodies?.[refName] || {};
  }
  return requestBody;
}

function resolveResponse(spec, response) {
  if (response.$ref) {
    const refName = response.$ref.split('/').pop();
    return spec.components?.responses?.[refName] || {};
  }
  return response;
}

function dedupeParameters(parameters) {
  const seen = new Set();
  return parameters.filter((parameter) => {
    const normalized = parameter.$ref ? parameter.$ref : `${parameter.in}:${parameter.name}`;
    if (seen.has(normalized)) {
      return false;
    }
    seen.add(normalized);
    return true;
  });
}

function getRefName(schema) {
  return schema?.$ref?.split('/').pop() || '';
}

function toAnchor(input) {
  return input
    .toLowerCase()
    .replace(/[`]/g, '')
    .replace(/[^a-z0-9\u4e00-\u9fa5]+/g, '-')
    .replace(/^-+|-+$/g, '');
}

function sanitizeText(value) {
  return String(value || '')
    .replace(/\|/g, '\\|')
    .replace(/\r?\n/g, '<br />')
    .trim();
}

function normalizeText(value) {
  const text = String(value || '').trim();
  if (!text) {
    return '';
  }
  return looksLikeMojibake(text) ? '' : text;
}

function looksLikeMojibake(text) {
  const suspicious = text.match(/[鍚鏌璁甯鎺鏀寰鍦绉浜夊彴]/g) || [];
  return suspicious.length >= 3;
}

main().catch((error) => {
  console.error(error);
  process.exitCode = 1;
});
