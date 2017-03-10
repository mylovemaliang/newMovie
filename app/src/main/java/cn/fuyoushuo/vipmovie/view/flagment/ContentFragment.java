package cn.fuyoushuo.vipmovie.view.flagment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zhy.android.percent.support.PercentLinearLayout;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Stack;

import butterknife.Bind;
import cn.fuyoushuo.domain.entity.HistoryItem;
import cn.fuyoushuo.vipmovie.R;
import cn.fuyoushuo.vipmovie.presenter.impl.SearchPresenter;

/**
 * Created by QA on 2017/3/7.
 */

public class ContentFragment extends BaseFragment {

    @Bind(R.id.content_searchText)
    TextView headText;

    @Bind(R.id.content_wv)
    WebView webView;

    @Bind(R.id.content_head_area)
    PercentLinearLayout ContentHeadArea;

    @Bind(R.id.progressBar)
    ProgressBar progressBar;

    private String firstLoadUrl;

    private boolean isFirstPop = false;

    private Long currentHisId;

    private boolean onlyOnePage = true;

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
                headText.setText(title);
                if(currentHisId != null){
                  SearchPresenter.updateHistoryTitle(currentHisId,title);
                }
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if(newProgress==100){
                    progressBar.setVisibility(View.GONE);//加载完网页进度条消失
                }
                else{
                    progressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
                    progressBar.setProgress(newProgress);//设置进度值
                }
            }
        });

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                WebView.HitTestResult hitTestResult = view.getHitTestResult();
                //hitTestResult==null解决重定向问题
               if (!TextUtils.isEmpty(url) && hitTestResult.getExtra() == null) {
                     view.loadUrl(url);
                     return true;
                }
                historyStack.push(url);
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
      if(isDetched) return;
      currentHisId = null;
      onlyOnePage  = true;
      if(webView != null){
          isFirstPop = false;
          firstLoadUrl = url;
          webView.loadUrl(url);
      }
    }

    /**
     * 供搜索页加载页面
     * @param historyItem
     */
    public void loadUrl(HistoryItem historyItem){
    try {
        if(isDetched) return;
        onlyOnePage = true;
        ContentHeadArea.setVisibility(View.GONE);
        Long id = historyItem.getId();
        int historyType = historyItem.getHistoryType();
        String historyTitle = historyItem.getHistoryTitle();
        String historyUrl = historyItem.getHistoryUrl();
        if(historyType == 1){
            currentHisId = id;
            isFirstPop = false;
            firstLoadUrl = historyUrl;
            if(webView != null){
                webView.loadUrl(historyUrl);
            }

        }
        if(historyType == 2){
           String url = "https://m.baidu.com/s?wd="+ URLEncoder.encode(historyTitle,"utf-8");
            isFirstPop = false;
            firstLoadUrl  = url;
            if(webView != null){
                webView.loadUrl(url);
            }
        }
       } catch (UnsupportedEncodingException e) {

       }
    }

    public static ContentFragment newInstance() {
        Bundle args = new Bundle();
        ContentFragment fragment = new ContentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public boolean goBack(){
        if(!isHistoryEmpty()){
            //移除当前页面
            historyStack.pop();
        }
        if(webView != null && !isHistoryEmpty()){
            String pop = historyStack.pop();
            webView.loadUrl(pop+"&isPopPrecess=1");
            return true;
        }
        else if(!isFirstPop){
            if(onlyOnePage){
                return false;
            }
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
