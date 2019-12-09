package com.lee.android.components.alipay;


public interface OnPayResultListener {

    /**
     * 支付失败
     * @param resultStatus
     */
    void onPayFailed(String resultStatus, String detailMessage);

    /**
     * 支付成功
     */
    void onPaySuccess();

    /**
     * 取消支付
     * @param errorcode
     * @param des
     */
    void onPayCancel(String errorcode, String des);

}
