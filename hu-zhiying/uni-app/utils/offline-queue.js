const QUEUE_KEY = 'hzy-offline-queue';

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
}
