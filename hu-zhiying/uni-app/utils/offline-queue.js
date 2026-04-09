import { uploadMedia } from '../api/file';
import { createQuotation, updateServiceOrderStatus } from '../api/order';
import { checkInOrder, uploadAfterWorkMedia, uploadBeforeWorkMedia } from '../api/master';

const QUEUE_KEY = 'hzy-offline-queue';

let flushPromise = null;
let autoSyncStarted = false;

function readQueue() {
  try {
    return uni.getStorageSync(QUEUE_KEY) || [];
  } catch (error) {
    return [];
  }
}

function writeQueue(queue) {
  uni.setStorageSync(QUEUE_KEY, queue);
}

async function bindStageMedia(stage, targetOrderId, tempFilePaths, note) {
  const bizType = stage === 'before' ? 'before_work_media' : 'after_work_media';
  const uploaded = await Promise.all(
    tempFilePaths.map((filePath) => uploadMedia(filePath, bizType, targetOrderId)),
  );
  const fileIds = uploaded.map((item) => item.data.fileId);
  const payload = { fileIds, note };
  if (stage === 'before') {
    await uploadBeforeWorkMedia(targetOrderId, payload);
    return;
  }
  await uploadAfterWorkMedia(targetOrderId, payload);
}

async function executeDefaultTask(item) {
  switch (item.type) {
    case 'workbench-status':
      await updateServiceOrderStatus(item.orderId, item.status);
      break;
    case 'workbench-quotation':
      await createQuotation(item.orderId, item.remark);
      break;
    case 'workbench-checkin':
      await checkInOrder(item.orderId, item.payload);
      break;
    case 'workbench-media-before':
      await bindStageMedia('before', item.orderId, item.tempFilePaths || [], item.note);
      break;
    case 'workbench-media-after':
      await bindStageMedia('after', item.orderId, item.tempFilePaths || [], item.note);
      break;
    default:
      break;
  }
}

export function enqueueOfflineTask(task) {
  const queue = readQueue();
  queue.push({
    id: `${Date.now()}-${Math.random().toString(36).slice(2, 8)}`,
    createdAt: Date.now(),
    retryCount: 0,
    ...task,
  });
  writeQueue(queue);
}

export function listOfflineTasks() {
  return readQueue();
}

export async function flushOfflineQueue(executor) {
  if (flushPromise) {
    return flushPromise;
  }

  flushPromise = (async () => {
    const queue = readQueue();
    const failed = [];

    for (const item of queue) {
      try {
        await executor(item);
      } catch (error) {
        failed.push({
          ...item,
          retryCount: (item.retryCount || 0) + 1,
        });
      }
    }

    writeQueue(failed);
    return {
      total: queue.length,
      failed: failed.length,
    };
  })().finally(() => {
    flushPromise = null;
  });

  return flushPromise;
}

export async function syncOfflineQueueNow(options = {}) {
  const result = await flushOfflineQueue(executeDefaultTask);
  if (options.showToast && result.total) {
    uni.showToast({
      title: result.failed ? `已补发，剩余 ${result.failed} 条` : '离线任务已同步',
      icon: 'none',
    });
  }
  return result;
}

export function ensureOfflineQueueAutoSync(options = {}) {
  if (autoSyncStarted) {
    return;
  }
  autoSyncStarted = true;

  const showToastOnRecover = options.showToastOnRecover !== false;

  if (typeof uni.onNetworkStatusChange === 'function') {
    uni.onNetworkStatusChange(async (status) => {
      if (!status?.isConnected || !listOfflineTasks().length) {
        return;
      }
      try {
        await syncOfflineQueueNow({ showToast: showToastOnRecover });
      } catch (error) {
        console.warn('自动同步离线队列失败', error);
      }
    });
  }
}
