package com.zk.alipay;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alipay.sdk.app.AuthTask;
import com.alipay.sdk.app.PayTask;
import com.zk.alipay.shouquan.AuthResult;
import com.zk.alipay.util.OrderInfoUtil2_0;
import com.zk.alipay.zhifu.PayResult;

import java.util.Map;

/**
 * Sign 字段务必在服务端完成前面生成，不要在客户端本地前面
 * 支付请求中的订单金额 total_amount，要依赖服务器，不要轻信客户端上的数据
 */

public class PayMainActivity extends AppCompatActivity {
    /**
     * 支付宝支付业务：入参app_id
     */
    public static final String APPID     = "2014100900013222";
    /**
     * 支付宝账户登录授权业务：入参pid值  首页--个人中心--最下面查找
     */
    public static final String PID       = "2088501624560335";
    /**
     * 支付宝账户登录授权业务：入参target_id值
     */
    public static final String TARGET_ID = "20141225xxxx";

    /**
     * 商户私钥 pkcs8格式
     */
    public static final  String RSA2_PRIVATE  = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC98aoOKirBjPTKN4vLTi8vpYwWTLG6WJh701BnCl3C5gT3lQQGeXf+ZphXBwD9e3fRyJai9cpxiis7eUbonrqrmUoIvbyiAudoYpqnXVKTcaO/rSisr9aa7IOfMqFHEOS61nIlceJja2txCJfUNanS89aghMcvqHLcJ5m0YKN4cimbUUYPELMVI1PsCJyJUNKFiYorQq1/TWWqbvaKRaDRk8NHJgHsKtZFlyhbnm73NsP69NOdKvjQvTlLfpFZE+l8I61sy8Cz4V2E67bwJ2r9/G4f0FVt+F/U4YVegax4w8+w5Y55veazDJrm2gJ4abNk0EETZwyBLPgsbFR2/y3hAgMBAAECggEAe9iVQ7UUuaxZc3wyJvYsaAmtxGBvRYw8qAgJFZY5ujlWJcPAoyQSLAri62OCrsQRRPRf25MdU1h+hcG2jTfpiLdjAT4NPylbjsE0C0oa7E4dMX4K1kW0TMFHtMZDR93o9TWbqXSO4roIjOPIczImL4iTeYf5g8Z2VbtwSZ71FzNgmuAKLy7eobLfewXo1daqBe0ZDGk75RkQ2NR6cJmbLLMbBlTqAbQyBA8KIdue3gR5rruecgsdoMEEBnBPTrZ07U26l1+m3k2lHjE3EsztsQBMDpUwdFKuq4kk7gRBXolj8zqYoKRiOVWy88bvPz8Vx8Vo6XGa0X0S/yBnM5t3kQKBgQDopuLxmxS/3MWcJsIKRYLKUlnWe/So0HbFsV6ij/So/8rfcWkzh9k+qnnSBHtvkQFsnv0+GLd1Hy8Wad9vCyFDessvhuzQRY2Imk9FotaTkYbrchrz0reJUYY6nUklyCTxEAz2UxyLLEIQO0JAhXH8S18YNuh6hVEphmKT2NxpbQKBgQDRAY8vGqJzvznIcCdXXoJx4Ic65dnPRwoEB1abQHnpYqmf8VH/Styk7By1Ap+luP7X3T6QX4gSR4RqC5krJBoz9nAsucgo+t9aUXrhLV83hOoKWD7znX/C1+0b/osRPoexlBmaaf+n+RNIQ3hxU9uIl6WHT01daBL4GVBanxchxQKBgES/e/R1JS6E6If6FADBBaMPrqhovKVd5JsKjLJw45VE8QgSFUo67IFOEu1ykZ8oNEmKub6twxiC/IEdC/9eRJgSIxSKRFRPGUGyh5ZGRi4ZJMtSTpCaRc34HzgW3lShzfjGC26GpLqje2oceLlkNYieJR2crBn4Z0FkCqExxgAJAoGBAMdW/2NjudE/bzMWlM8lmrBV/2RTWPvyu0DAZv/H7P6FVVbw6M3ebrb1YyPZDr8WxCjKISO9maAlictCqKGW2074GmDuCFPdgi04TUR667eeE0IujEv5yaLiIolyqtyVkQHzSMAXnPht/NANWdBstJOAXyXAov8VhhIOwq7L0VopAoGAeZBY2TXgCvvD/7sDQ8hQjxEI0IVORT35bGoNtSce2QDa9lG5cwXOn775TcAhCiWAmLexRKo+1Q/aYVAYTTB6ImEvE5DVb+3vz7a+1Wgw8yz+pijwabVRT0oggBYRWFyNRVjIrrwg3Tg5Me+P65/p64ztOhDotbQl6mtF0CshC44=";
    public static final  String RSA_PRIVATE   = "";
    /**
     * 支付R
     */
    private static final int    SDK_PAY_FLAG  = 1;
    /**
     * 授权
     */
    private static final int    SDK_AUTH_FLAG = 2;


    // 商户收款账号
    //    public static final  String SELLER       = "支付宝收款账户";
    // 支付宝公钥
    //    public static final  String RSA_PUBLIC   = "支付宝公钥";


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {

                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&docType=1)
                     * 建议商户依赖异步通知
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();//同步返回状态码
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(PayMainActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(PayMainActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(PayMainActivity.this, "支付失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        Toast.makeText(PayMainActivity.this,
                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(PayMainActivity.this,
                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

                    }
                    break;
                }
                default:
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * 支付宝支付业务
     *
     * @param v
     */
    public void zhifu(View v) {
        //TODO 判断有没有配置APPID 和 私钥
        if (TextUtils.isEmpty(APPID) || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置APPID | RSA_PRIVATE")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //
                            finish();
                        }
                    }).show();
            return;
        }

        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo的获取必须来自服务端；
         */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2);

        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        /**
         * 特别注意,这里的签名 逻辑需要放在服务端，切忽将私钥泄漏在代码中
         */
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        /**
         * 完整的符合支付宝参数规范的订单信息
         */
        final String orderInfo = orderParam + "&" + sign;
        Log.i("msg", "pay_1: " + orderInfo);
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                /** 支付行为需要在独立的非ui线程执行
                 * orderInfo  app支付请求参数字符串   key=value形式，以&连接
                 * b   用户在点击商户app付款的时候，需要一个loading在钱包唤起之前的过度，这个值为true，直到付款页面loading才会消失
                 * */
                //PayTask对象主要为商户提供订单支付、查询功能，及获取当前开发包版本号。
                PayTask alipay = new PayTask(PayMainActivity.this);
                //调用支付接口，获取支付结果
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        /**
         * 必须异步调用
         * */
        Thread payThread = new Thread(payRunnable);
        payThread.start();

    }

    /**
     * 支付宝账户授权业务
     *
     * @param v
     */
    public void shouquan(View v) {
        if (TextUtils.isEmpty(PID) || TextUtils.isEmpty(APPID)
                || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))
                || TextUtils.isEmpty(TARGET_ID)) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置PARTNER |APP_ID| RSA_PRIVATE| TARGET_ID")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                        }
                    }).show();
            return;
        }

        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * authInfo的获取必须来自服务端；
         */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> authInfoMap = OrderInfoUtil2_0.buildAuthInfoMap(PID, APPID, TARGET_ID, rsa2);
        String info = OrderInfoUtil2_0.buildOrderParam(authInfoMap);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(authInfoMap, privateKey, rsa2);
        final String authInfo = info + "&" + sign;
        Runnable authRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造AuthTask 对象
                AuthTask authTask = new AuthTask(PayMainActivity.this);
                // 调用授权接口，获取授权结果
                Map<String, String> result = authTask.authV2(authInfo, true);

                Message msg = new Message();
                msg.what = SDK_AUTH_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread authThread = new Thread(authRunnable);
        authThread.start();
    }

    /**
     * 原生的H5（手机网页版支付切natvie支付） 【对应页面网页支付按钮】
     *
     * @param v
     */
    public void h5Pay(View v) {
        Intent intent = new Intent(this, H5PayDemoActivity.class);
        Bundle extras = new Bundle();
        /**
         * url 是要测试的网站，在 Demo App 中会使用 H5PayDemoActivity 内的 WebView 打开。
         *
         * 可以填写任一支持支付宝支付的网站（如淘宝或一号店），在网站中下订单并唤起支付宝；
         * 或者直接填写由支付宝文档提供的“网站 Demo”生成的订单地址
         * （如 https://mclient.alipay.com/h5Continue.htm?h5_route_token=303ff0894cd4dccf591b089761dexxxx）
         * 进行测试。
         *
         * H5PayDemoActivity 中的 MyWebViewClient.shouldOverrideUrlLoading() 实现了拦截 URL 唤起支付宝，
         * 可以参考它实现自定义的 URL 拦截逻辑。
         */
        String url = "http://m.taobao.com";
        extras.putString("url", url);
        intent.putExtras(extras);
        startActivity(intent);
    }

    /**
     * get the sdk version. 获取SDK版本号
     */
    public void getSDKVersion() {
        PayTask payTask = new PayTask(this);
        String version = payTask.getVersion();
        Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
    }


}
