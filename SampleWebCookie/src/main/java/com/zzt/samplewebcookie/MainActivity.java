package com.zzt.samplewebcookie;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ClientCertRequest;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.HttpAuthHandler;
import android.webkit.JsResult;
import android.webkit.RenderProcessGoneDetail;
import android.webkit.SafeBrowsingResponse;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    WebView webView;
    ViewGroup rel_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webView);
        rel_content = findViewById(R.id.rel_content);
        webView.getSettings().setDefaultTextEncodingName("UTF-8");
        webView.getSettings().setSupportMultipleWindows(true);// 设置允许开启多窗口
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        synCookies(MainActivity.this, webView);
        // 开启javascript 渲染
        webView.getSettings().setJavaScriptEnabled(true);

        // Enable plugins
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);

        // Increase the priority of the rendering thread to high
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);

        // Enable application caching
        webView.getSettings().setAppCacheEnabled(true);

        // Enable HTML5 local storage and make it persistent
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);//支持自动加载图片
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowFileAccessFromFileURLs(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);

        webView.getSettings().setDatabaseEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);//支持js
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);//根据cache-control决定是否从网络上取数据。。

        webView.getSettings().setSupportMultipleWindows(true);

        webView.setWebViewClient(new WebViewClient() {


            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                StringBuffer stringBuffer = new StringBuffer();
                stringBuffer.append("\n");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Uri url = request.getUrl();
                    stringBuffer.append("url:" + url);
                    stringBuffer.append("\n");

                    String method = request.getMethod();
                    stringBuffer.append("method:" + method);
                    stringBuffer.append("\n");

                    Map<String, String> requestHeaders = request.getRequestHeaders();
                    Set<Map.Entry<String, String>> entries = requestHeaders.entrySet();
                    stringBuffer.append("Headers:");
                    for (Map.Entry<String, String> entry                                                                                 entries) {
                        stringBuffer.append("\n\t\t key:" + entry.getKey() + " value:" + entry.getValue());
                    }
                    stringBuffer.append("\n");
                }
                Log.d(TAG, "---------------------------------shouldInterceptRequest----------------------------------" + stringBuffer.toString());
                return super.shouldInterceptRequest(view, request);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.v(TAG, ">>>>>>>> shouldOverrideUrlLoading url ===");
                logUrlCookies(getBaseContext(), url, "shouldOverrideUrlLoading 2 ");
                loadVConsoleJsStart();
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.v(TAG, ">>>>>>>> shouldOverrideUrlLoading request ===");
                logUrlCookies(getBaseContext(), view.getUrl(), "shouldOverrideUrlLoading 1 ");
                loadVConsoleJsStart();
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.v(TAG, ">>>>>>>> onPageStarted ===");
                logUrlCookies(getBaseContext(), url, " onPageStarted ");
                loadVConsoleJsStart();
                super.onPageStarted(view, url, favicon);
                loadVConsoleJsStart();
            }


            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Log.e(TAG, "<<<<<<<< onReceivedError ");
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                Log.e(TAG, "<<<<<<<< onReceivedError ");
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
                Log.e(TAG, "<<<<<<<< onReceivedError ");
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
                Log.e(TAG, "<<<<<<<< onReceivedError ");
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.e(TAG, "<<<<<<<< onPageFinished ===" + url);
                logUrlCookies(getBaseContext(), url, "onPageFinished");


                webView.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        loadVConsoleJs();

                    }
                }, 2000);


                CookieManager cookieManager = CookieManager.getInstance();
                Log.i(TAG, "========" + cookieManager.acceptCookie() +
                        "======" + cookieManager.allowFileSchemeCookies());
            }


            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                Log.w(TAG, "-------- onLoadResource ");
            }

            @Override
            public void onPageCommitVisible(WebView view, String url) {
                super.onPageCommitVisible(view, url);
                Log.w(TAG, "-------- onPageCommitVisible ");
            }

            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                Log.w(TAG, "-------- shouldInterceptRequest ");
                return super.shouldInterceptRequest(view, url);
            }

            @Override
            public void onTooManyRedirects(WebView view, Message cancelMsg, Message continueMsg) {
                super.onTooManyRedirects(view, cancelMsg, continueMsg);
                Log.w(TAG, "-------- onTooManyRedirects ");
            }

            @Override
            public void onFormResubmission(WebView view, Message dontResend, Message resend) {
                super.onFormResubmission(view, dontResend, resend);
                Log.w(TAG, "-------- onFormResubmission ");
            }

            @Override
            public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
                super.doUpdateVisitedHistory(view, url, isReload);
                Log.w(TAG, "-------- doUpdateVisitedHistory ");
            }

            @Override
            public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
                super.onReceivedClientCertRequest(view, request);
                Log.w(TAG, "-------- onReceivedClientCertRequest ");
            }

            @Override
            public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
                super.onReceivedHttpAuthRequest(view, handler, host, realm);
                Log.w(TAG, "-------- onReceivedHttpAuthRequest ");
            }

            @Override
            public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
                Log.w(TAG, "-------- shouldOverrideKeyEvent ");
                return super.shouldOverrideKeyEvent(view, event);
            }

            @Override
            public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
                super.onUnhandledKeyEvent(view, event);
                Log.w(TAG, "-------- onUnhandledKeyEvent ");
            }

            @Override
            public void onScaleChanged(WebView view, float oldScale, float newScale) {
                super.onScaleChanged(view, oldScale, newScale);
                Log.w(TAG, "-------- onScaleChanged ");
            }

            @Override
            public void onReceivedLoginRequest(WebView view, String realm, @Nullable String account, String args) {
                super.onReceivedLoginRequest(view, realm, account, args);
                Log.w(TAG, "-------- onReceivedLoginRequest ");
            }

            @Override
            public boolean onRenderProcessGone(WebView view, RenderProcessGoneDetail detail) {
                Log.w(TAG, "-------- onRenderProcessGone ");
                return super.onRenderProcessGone(view, detail);
            }

            @Override
            public void onSafeBrowsingHit(WebView view, WebResourceRequest request, int threatType, SafeBrowsingResponse callback) {
                super.onSafeBrowsingHit(view, request, threatType, callback);
                Log.w(TAG, "-------- onSafeBrowsingHit ");
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }

            @Override
            public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
                Log.w(TAG, "--------------onCreateWindow-----------");
                Log.e(TAG, "onCreateWindow===message=");
                WebView newWebView = new WebView(MainActivity.this);
                //不使用缓存
                newWebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                newWebView.getSettings().setAppCacheEnabled(false);//不缓存
                newWebView.getSettings().setDefaultTextEncodingName("UTF-8");
                newWebView.getSettings().setSupportMultipleWindows(true);// 设置允许开启多窗口
                //        wv.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
                newWebView.getSettings().setJavaScriptEnabled(true);
                newWebView.getSettings().setDomStorageEnabled(true);
                rel_content.addView(newWebView);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
                newWebView.setLayoutParams(layoutParams);
                WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
                transport.setWebView(newWebView);
                resultMsg.sendToTarget();
                newWebView.setWebViewClient(new WebViewClient() {
                    @Override

                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                        try {
//                            //去掉注释使用系统浏览器打开
//                            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
//                            browserIntent.setData(Uri.parse(url));
//                            startActivity(browserIntent);
//                            rel_content.removeView(newWebView);
//                        } catch (Exception e) {
//                            rel_content.removeView(newWebView);
//                            showCusToast("App is not installed");
//                            e.printStackTrace();
//                        }
//
//                        Log.e(TAG, "onCreateWindow=====" + url);
                        view.loadUrl(url);
                        return true;
                    }

                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                        return super.shouldOverrideUrlLoading(view, request);
                    }

                });

                newWebView.setWebChromeClient(new WebChromeClient());
                return true;
            }
        });

        webView.setWebContentsDebuggingEnabled(true);


        // 载入内容
//        webView.loadUrl("file:///android_asset/HtmlCookies.html");
        String playProo = "http://phk.technovedic.cc/ipay_vnd/deposit.html?tk=41a10db3-da3a-43f5-b51a-a35bcfebfa7f&tradeToken=41a10db3-da3a-43f5-b51a-a35bcfebfa7f&amount=100&uuid=A2F9E0EF3C9EF284&device=1&sourceId=10&language=en-US&methodId=31&countryId=0&exchangeId=7&v=144&vipType=0&rechargeSource=&packageId=&retryId=4034398&versionNo=5&_t=843164&timeZoneOffset=28800&sourceOfEntry=fv_deposit_less500&tp=15903563&email=kkk07@gmail.com&firstName=h&lastName=h&currency=MYR";
        String playTest = "http://test-static.daily-fx.net/recharge/ipay_vnd/deposit.html?tk=4f1016a2-c5bd-4978-9ee2-66f08c402847&tradeToken=4f1016a2-c5bd-4978-9ee2-66f08c402847&amount=100&uuid=65682BCDBAEBE61B&device=1&sourceId=10&language=zh-CN&methodId=31&countryId=0&exchangeId=7&v=144&vipType=0&rechargeSource=&packageId=&retryId=16799&versionNo=5&_t=316424&timeZoneOffset=28800&sourceOfEntry=fv_deposit_less500&tp=73681598&phone=96300398&firstName=d&lastName=f&currency=MYR";
        webView.loadUrl(playProo);
//        webView.loadUrl("http://www.baidu.com");
    }


    @Nullable
    @Override
    public View onCreatePanelView(int featureId) {
        Log.w(TAG, "--------------onCreatePanelView-----------");
        return super.onCreatePanelView(featureId);
    }


    public void loadVConsoleJsStart() {
//        String url = "file:///android_asset/vconsole.min.js";
        String url = "https://cdn.bootcss.com/vConsole/3.3.4/vconsole.min.js";
        String js = "javascript:var d=document;" +
                "var s=d.createElement('script');" +
                "s.setAttribute('src', '" + url + "');" +
                "d.head.appendChild(s);";
        webView.loadUrl(js);
    }

    public void loadVConsoleJs() {
//        String urlNew = "file:///android_asset/vconsoleNew.js";
//        String jsNew = "javascript:var d=document;" +
//                "var s=d.createElement('script');" +
//                "s.setAttribute('src', '" + urlNew + "');" +
//                "d.body.appendChild(s);";
//        webView.loadUrl(jsNew);


        String aaa = "javascript:alert('cookie====='+document.cookie)";
        webView.loadUrl(aaa);

//        String bbb = "javascript:var vConsole = new VConsole();   console.log('cookie=====', document.cookie)";
//        webView.loadUrl(bbb);
    }

    public void synCookies(Context context, WebView webView) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);// 允许接受 Cookie
        CookieManager.setAcceptFileSchemeCookies(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.setAcceptThirdPartyCookies(webView, true);  //跨域cookie读取
            cookieManager.acceptThirdPartyCookies(webView);
        }

    }

    public String logUrlCookies(Context context, String htmlUrl, String tag) {
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        String cookieStr = cookieManager.getCookie(htmlUrl);
        Log.e(TAG, "html_url " + tag + "  url:" + htmlUrl + " 对应 CookieStr:" + cookieStr);
        return cookieStr;
    }
}