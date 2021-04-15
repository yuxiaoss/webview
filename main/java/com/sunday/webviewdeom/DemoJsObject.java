package com.sunday.webviewdeom;

import android.util.Log;
import android.webkit.JavascriptInterface;

/**
 * Created by Sunday on 2018/6/16.
 */

public class DemoJsObject {

    @JavascriptInterface
    public String print (String msg) {
        Log.e("DemoJsObject", "msg ：" + msg);
        return "这是android的返回值";
    }
}
