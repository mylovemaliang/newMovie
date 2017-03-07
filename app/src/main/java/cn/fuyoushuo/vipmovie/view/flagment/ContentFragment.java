package cn.fuyoushuo.vipmovie.view.flagment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import java.util.Stack;

import butterknife.Bind;
import cn.fuyoushuo.vipmovie.R;

/**
 * Created by QA on 2017/3/7.
 */

public class ContentFragment extends BaseFragment {

    @Bind(R.id.head_search_text)
    TextView headText;

    @Bind(R.id.content_wv)
    WebView webView;

    private String firstLoadUrl;

    private boolean isFirstPop = false;

    private Stack<String> historyStack = new Stack<String>();


    @Override
    protected String getPageName() {
        return "content_fragment";
    }

    @Override
    protected int getRootLayoutId() {
        return R.layout.fragment_content_view;
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void initView() {
        super.initView();
        webView.getSettings().setJavaScriptEnabled(true);
        //mytixianWebview.getSettings().setBuiltInZoomControls(true);//是否显示缩放按钮，默认false
        webView.getSettings().setSupportZoom(true);//是否可以缩放，默认true
        webView.getSettings().setDomStorageEnabled(true);

        webView.getSettings().setUseWideViewPort(true);// 设置此属性，可任意比例缩放。大视图模式
        webView.getSettings().setLoadWithOverviewMode(true);// 和setUseWideViewPort(true)一起解决网页自适应问题

        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);

        webView.requestFocusFromTouch();
        webView.setWebChromeClient(new WebChromeClient(){

            @Override
            public void onReceivedTitle(WebView view, final String title) {
                super.onReceivedTitle(view, title);
            }
        });

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(!TextUtils.isEmpty(url)){
                    view.loadUrl(url);
                    if(url.indexOf("&isPopPrecess=1") < 0){
                        historyStack.add(url);
                    }
                    return false;
                }
                return super.shouldOverrideUrlLoading(view,url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {

            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * 供外部调用加载url
     * @param url
     */
    public void loadUrl(String url){
      if(isDetached()) return;
      if(webView != null){
          isFirstPop = false;
          firstLoadUrl = url;
          webView.loadUrl(url);
      }
    }

    public static ContentFragment newInstance() {
        Bundle args = new Bundle();
        ContentFragment fragment = new ContentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public boolean goBack(){
        if(webView != null && !isHistoryEmpty()){
            String pop = historyStack.pop();
            webView.loadUrl(pop+"&isPopPrecess=1");
            return true;
        }
        else if(!isFirstPop){
            webView.loadUrl(firstLoadUrl+"&isPopPrecess=1");
            isFirstPop = true;
            return true;
        } else{
            return false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(webView!=null){
            ViewGroup viewGroup = (ViewGroup) webView.getParent();
            if(viewGroup!=null){
                viewGroup.removeView(webView);
            }
            webView.removeAllViews();
            webView.destroy();
            webView=null;
        }
    }

    //-----------------------------------------关于后退栈的操作-----------------------------------------------
    private boolean isHistoryEmpty(){
        if(historyStack.size() == 0){
            return true;
        }else{
            return false;
        }
    }
}
