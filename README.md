# android-components-alipay
A android component of alipay.

## How to use?
```
    AliPay aliPay = new AliPay(this);
    aliPay.pay(payInfo);//payInfo from your server end.
    aliPay.setPaymentResultListener(this);

    public interface OnPayResultListener {

        /**
         * 支付失败
         * @param resultStatus
         */
        void onPayFailed(String resultStatus,String detailMessage);

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
```
## version info:
- alipaySdk-15.6.8-20191021122514.aar
