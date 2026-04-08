import { buildAbsoluteUrl, request, uploadFile } from '../utils/request';

/**
 * 上传媒体文件并返回平台生成的文件元数据。
 * @param {string} filePath
 * @param {string} bizType
 * @param {string} bizId
 * @returns {Promise<{data: object|null}>}
 */
export async function uploadMedia(filePath, bizType = 'general', bizId = '') {
  const response = await uploadFile(filePath, {
    bizType,
    bizId,
  });
  return {
    data: normalizeMediaFile(response.data),
  };
}

/**
 * 查询文件元数据。
 * @param {number|string} fileId
 * @returns {Promise<{data: object|null}>}
 */
export async function getMediaFile(fileId) {
  const response = await request({
    url: `/api/files/${fileId}`,
  });
  return {
    data: normalizeMediaFile(response.data),
  };
}

function normalizeMediaFile(item) {
  if (!item) {
    return null;
  }
  return {
    ...item,
    fileId: item.id,
    url: /^https?:\/\//.test(item.url) ? item.url : buildAbsoluteUrl(item.url),
  };
}
