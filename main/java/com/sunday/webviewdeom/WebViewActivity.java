package com.sunday.webviewdeom;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WebViewActivity extends AppCompatActivity {

    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        mWebView = findViewById(R.id.webview);

//        mWebView.loadUrl("http://www.baidu.com/");
//        mWebView.loadUrl("file://" + Environment.getExternalStorageDirectory().getPath() + "/1/index.html");
//        mWebView.loadUrl("file:///android_asset/index.html");

//        mWebView.loadUrl("http://192.168.2.124:3000/");

//        mWebView.loadData("<h1>这是我们通过loadData添加进来的内容</h1>","text/html; charset=utf-8", null);
//        https://www.imooc.com/static/img/index/logo.png
//        mWebView.loadDataWithBaseURL("https://www.imooc.com/" , "<img src=\"static/img/index/logo.png\"/><a href=\"http://www.baidu.com\">toBaiDu.com</a>", "text/html", "utf-8","http://www.sogou.com/");

//        mWebView.loadUrl("http://www.baidu.com/");

        mWebView.loadUrl("http://192.168.2.124:3000/");
//        mWebView.loadUrl("http://192.168.2.128:3000/");

        mWebView.addJavascriptInterface(new DemoJsObject(), "android");

        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                Log.e("WebViewActivity","webview-》onReceivedError : 加载了url：" + failingUrl + " - 错误描述：" + description+ " - 错误代码：" + errorCode);
                view.loadUrl("http://192.168.2.124:3000/");
            }


            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Log.e("WebViewActivity","webview-》onReceivedError (android6.0以上调用) : 加载了url：" + request.getUrl().toString() + " - 错误描述：" + error.getDescription()+ " - 错误代码：" + error.getErrorCode());
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e("WebViewActivity","webview-》shouldOverrideUrlLoading : 加载了url：" + url);
                if ("http://www.baidu.com/".equals(url)) {
//                    view.loadUrl("http://www.sogou.com/");
                    Toast.makeText(WebViewActivity.this, "webview-》shouldOverrideUrlLoading : 加载了url：" + url, Toast.LENGTH_SHORT).show();
                    return true;
                }

                Uri uri = Uri.parse(url);
                if ("android".equals(uri.getScheme())) {
                    String functionName = uri.getAuthority();
                    if ("print".equals(functionName)) {
                        String msg = uri.getQueryParameter("msg");
                        print(msg);
                        return true;
                    }
                }

                return super.shouldOverrideUrlLoading(view, url);
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                Log.e("WebViewActivity","webview-》shouldOverrideUrlLoading(Android7.0以上调用) : 加载了url：" + request.getUrl().toString());
                return super.shouldOverrideUrlLoading(view, request);
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                WebResourceResponse result = super.shouldInterceptRequest(view, url);
                Log.e("WebViewActivity","webview-》shouldInterceptRequest请求了url：" + url);
                Log.e("WebViewActivity", "result = " + result);
//                if ("http://www.baidu.com/".equals(url)) {
//                    return new WebResourceResponse("text/html", "utf-8", null);
//                }
                return result;
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                Log.e("WebViewActivity","webview-》shouldInterceptRequest请求了(android5.0之上调用)url：" + request.getUrl().toString());
                return super.shouldInterceptRequest(view, request);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.e("WebViewActivity","webview-》onPageStarted 网页开始进行加载url：" + url);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                Log.e("WebViewActivity","webview-》onLoadResource 网页开始加载资源url：" + url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.e("WebViewActivity","webview-》onPageFinished 网页已经加载完成url：" + url);
            }
        });

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                Log.e("webViewActivity", "newProgress:" + newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                Log.e("webViewActivity", "title:" + title);
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                boolean res = super.onJsAlert(view, url, message, result);
                res = true;
                Log.e("webViewActivity", "onJsAlert - url : " + url + " - message : " + message + "  - res : " + res);
                Toast.makeText(WebViewActivity.this, message, Toast.LENGTH_SHORT).show();
                result.confirm();
                return res;
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
                boolean res = super.onJsConfirm(view, url, message, result);
                res = true;
                Log.e("webViewActivity", "onJsConfirm - url : " + url + " - message : " + message + "  - res : " + res);
                AlertDialog.Builder builder = new AlertDialog.Builder(WebViewActivity.this);
                builder.setMessage(message);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        result.confirm();
                    }
                });

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        result.cancel();
                    }
                });
                builder.create().show();
                return res;
            }

            @Override
            public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
                boolean res = super.onJsPrompt(view, url, message, defaultValue, result);
                res = true;
                Log.e("webViewActivity", "onJsConfirm - url : " + url + " - message : " + message + " - defaultValue : " + defaultValue + "  - res : " + res);
                AlertDialog.Builder builder = new AlertDialog.Builder(WebViewActivity.this);
                builder.setMessage(message);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        result.confirm("这是点击了确定按钮之后的输入框内容");
                    }
                });

                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        result.cancel();
                    }
                });
                builder.create().show();
                return res;
            }
        });

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
//        webSettings.setSupportZoom(true);
//        webSettings.setBuiltInZoomControls(true);
//        webSettings.setDisplayZoomControls(true);
//        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ONLY);
//        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.clearCache(true);
    }

//    public void onCanGoBack (View v) {
//        Toast.makeText(this, String.valueOf(mWebView.canGoBack()), Toast.LENGTH_SHORT).show();
//    }
//
//    public void onGoBack (View v) {
//        mWebView.goBack();
//    }
//
//    public void onCanGoForward (View v) {
//        Toast.makeText(this, String.valueOf(mWebView.canGoForward()), Toast.LENGTH_SHORT).show();
//    }
//
//    public void onGoForward (View v) {
//        mWebView.goForward();
//    }
//
//    public void onCanGoBackOrForward (View v) {
//        int steps = Integer.valueOf(((EditText)findViewById(R.id.steps)).getText().toString());
//        Toast.makeText(this, String.valueOf(mWebView.canGoBackOrForward(steps)), Toast.LENGTH_SHORT).show();
//    }
//
//    public void onGoBackOrForward (View v) {
//        int steps = Integer.valueOf(((EditText)findViewById(R.id.steps)).getText().toString());
//        mWebView.goBackOrForward(steps);
//    }
//
//    public void onClearHistory (View v) {
//        mWebView.clearHistory();
//    }

    public void onShowAlertFromloadUrl (View v) {
        mWebView.loadUrl("javascript:showAlert()");
    }

    public void onSumFromloadUrl (View v) {
        mWebView.loadUrl("javascript:alert(sum(2, 3))");
    }

    public void onSumFromEVJS (View v) {
        mWebView.evaluateJavascript("javascript:sum(2, 3)", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {
                Toast.makeText(WebViewActivity.this, "evaluateJavascript - " + s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void print (String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        String result = "这是android的返回值";
        mWebView.loadUrl("javascript:showAlert('" + result + "')");
    }

    @Override
    protected void onPause() {
        super.onPause();

        mWebView.onPause();
//        mWebView.pauseTimers();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mWebView.onResume();
//        mWebView.resumeTimers();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mWebView.destroy();
    }
}
