<script>
import { getMessageSessions } from './api/user';
import { syncMessageBadgeBySessions } from './utils/message-center';
import { ensureOfflineQueueAutoSync, listOfflineTasks, syncOfflineQueueNow } from './utils/offline-queue';

async function syncAppMessageBadge() {
  try {
    const res = await getMessageSessions();
    syncMessageBadgeBySessions(res.data || []);
  } catch (error) {
    console.warn('应用前台同步消息角标失败', error);
  }
}

export default {
  onLaunch() {
    console.log('呼之应启动');
    ensureOfflineQueueAutoSync({ showToastOnRecover: true });
    syncAppMessageBadge();
  },
  async onShow() {
    console.log('呼之应显示');
    await syncAppMessageBadge();
    if (!listOfflineTasks().length) {
      return;
    }
    try {
      await syncOfflineQueueNow({ showToast: false });
    } catch (error) {
      console.warn('应用前台同步离线队列失败', error);
    }
  },
};
</script>

<style>
@import './styles/base.css';
@import './styles/iconfont/iconfont.css';
</style>
