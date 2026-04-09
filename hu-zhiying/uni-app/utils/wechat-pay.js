/**
 * utils/wechat-pay.js
 * 封装微信支付核心逻辑
 */

import { getRequestErrorMessage } from './request';

export function isWechatPayCanceled(error) {
  return /cancel/i.test(String(error?.errMsg || error?.message || ''));
}

function loginWithWechat() {
  return new Promise((resolve, reject) => {
    uni.login({
      provider: 'weixin',
      success: resolve,
      fail: reject,
    });
  });
}

export async function buildWechatPrepayRequest(orderId) {
  if (!orderId) {
    throw new Error('缺少订单号，无法创建预支付请求');
  }

  const payload = { orderId, channel: 'JSAPI' };

  // #ifdef MP-WEIXIN
  const loginRes = await loginWithWechat();
  if (!loginRes?.code) {
    throw new Error('微信登录凭证获取失败，请稍后重试');
  }
  payload.channel = 'JSAPI';
  payload.authCode = loginRes.code;
  // #endif

  // #ifdef APP-PLUS
  payload.channel = 'APP';
  // #endif

  // #ifdef H5
  throw new Error('当前 H5 暂不支持直接调起微信支付，请在微信小程序或 App 内完成支付');
  // #endif

  return payload;
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
    const orderInfo = payParams.orderInfo || (
      payParams.channel === 'APP' && payParams.appId && payParams.partnerId && payParams.prepayId
        ? {
            appid: payParams.appId,
            partnerid: payParams.partnerId,
            prepayid: payParams.prepayId,
            package: packageValue || 'Sign=WXPay',
            noncestr: payParams.nonceStr,
            timestamp: timeStamp,
            sign: payParams.paySign,
          }
        : null
    );
    const hasAppOrderInfo = Boolean(orderInfo);
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
      ...(hasAppOrderInfo ? { orderInfo } : {}),
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
