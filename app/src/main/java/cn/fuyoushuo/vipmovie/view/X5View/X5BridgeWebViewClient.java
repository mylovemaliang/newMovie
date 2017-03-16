package cn.fuyoushuo.vipmovie.view.X5View;

import android.graphics.Bitmap;

import com.github.lzyzsd.jsbridge.BridgeUtil;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.Message;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by QA on 2017/3/14.
 */

public class X5BridgeWebViewClient extends WebViewClient {

    private X5BridgeWebView webView;

    public X5BridgeWebViewClient(X5BridgeWebView webView) {
        this.webView = webView;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        try {
            url = URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (url.startsWith(X5BridgeUtils.YY_RETURN_DATA)) { // 如果是返回数据
            webView.handlerReturnData(url);
            return true;
        } else if (url.startsWith(X5BridgeUtils.YY_OVERRIDE_SCHEMA)) { //
            webView.flushMessageQueue();
            return true;
        } else {
            return super.shouldOverrideUrlLoading(view, url);
        }
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);

        if (X5BridgeWebView.toLoadJs != null) {
            X5BridgeUtils.webViewLoadLocalJs(view, X5BridgeWebView.toLoadJs);
        }

        //
        if (webView.getStartupMessage() != null) {
            for (Message m : webView.getStartupMessage()) {
                webView.dispatchMessage(m);
            }
            webView.setStartupMessage(null);
        }
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
    }

}
