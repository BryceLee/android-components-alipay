package com.lee.android.components.alipay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import com.alipay.sdk.app.PayTask;

public class AliPay {

    private static final int SDK_PAY_FLAG = 1;
    private Activity mActivity;
    private OnPayResultListener mPaymentResult;

    public AliPay(Activity activity) {
        this.mActivity = activity;
    }
    //TODO
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);

                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        if (mPaymentResult != null)
                            mPaymentResult.onPaySuccess();
                    }  else if (TextUtils.equals(resultStatus, "6001")) {
                        if (mPaymentResult != null)
                            mPaymentResult.onPayCancel(resultStatus,payResult.toString());
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (mPaymentResult != null)
                            mPaymentResult.onPayFailed(resultStatus,payResult.toString());
                        //if (TextUtils.equals(resultStatus, "8000")) {
                        //    if (mPaymentResult != null)
                        //        mPaymentResult.onPayFailed(resultStatus);
                        //
                        //} else {
                        //    // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                        //    if (mPaymentResult != null)
                        //        mPaymentResult.onPayFailed(resultStatus);
                        //
                        //}
                    }
                    break;
                }
                default:
                    break;
            }
        }

    };

    /**
     * 调用SDK支付
     */
    public void pay(final String payInfo) {

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(mActivity);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    public void setPaymentResultListener(OnPayResultListener payInterfaces) {
        this.mPaymentResult = payInterfaces;
    }

}
