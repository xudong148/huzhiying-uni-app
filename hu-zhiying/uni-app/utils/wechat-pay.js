/**
 * utils/wechat-pay.js
 * 封装微信支付核心逻辑
 */

import { getRequestErrorMessage } from './request';

export function isWechatPayCanceled(error) {
  return /cancel/i.test(String(error?.errMsg || error?.message || ''));
}

/**
 * 调起微信支付
 * @param {Object} payParams - 后端预支付接口返回的支付参数
 * @returns {Promise} 返回支付成功的结果，失败或取消则抛出异常
 */
export function launchWechatPay(payParams) {
  return new Promise((resolve, reject) => {
    if (!payParams) {
      reject({ errMsg: '支付参数为空，无法发起支付' });
      return;
    }

    if (payParams.payEnabled === false) {
      reject({ errMsg: payParams.message || '当前环境未启用微信支付' });
      return;
    }

    if (typeof uni?.requestPayment !== 'function') {
      reject({ errMsg: '当前运行环境不支持微信支付' });
      return;
    }

    const timeStamp = payParams.timeStamp || payParams.timestamp;
    const packageValue = payParams.package || payParams.packageValue;
    const hasAppOrderInfo = Boolean(payParams.orderInfo);
    const hasMiniProgramParams = Boolean(timeStamp && payParams.nonceStr && packageValue && payParams.paySign);

    if (!hasAppOrderInfo && !hasMiniProgramParams) {
      reject({ errMsg: payParams.message || '微信预支付参数不完整' });
      return;
    }

    const requestPayload = {
      provider: 'wxpay',
      timeStamp,
      nonceStr: payParams.nonceStr,
      package: packageValue,
      signType: payParams.signType || 'RSA',
      paySign: payParams.paySign,
      ...(hasAppOrderInfo ? { orderInfo: payParams.orderInfo } : {}),
    };

    try {
      uni.requestPayment({
        ...requestPayload,
        success(res) {
          console.log('微信支付成功:', res);
          resolve(res);
        },
        fail(error) {
          console.warn('微信支付失败/取消:', error);
          reject(error);
        },
      });
    } catch (error) {
      reject({
        errMsg: getRequestErrorMessage(error, '微信支付拉起失败'),
      });
    }
  });
}
