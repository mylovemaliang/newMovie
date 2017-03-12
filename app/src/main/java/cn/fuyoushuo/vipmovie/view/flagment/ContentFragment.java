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

import java.net.URLEncoder;
import java.util.Stack;

import butterknife.Bind;
import cn.fuyoushuo.vipmovie.R;
import cn.fuyoushuo.vipmovie.po.LoadItem;
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

    private Long currentHisId;

    private String keyword;

    // 1,普通url;2,搜索url;3,搜索关键字
    private int loadType;

    //标题重置没
    private boolean isTitleSet = false;


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
        Bundle arguments = getArguments();
        if(arguments != null){
            String loadUrl = arguments.getString("loadUrl","");
            int loadType = arguments.getInt("loadType",0);
            Long hisId = arguments.getLong("currentHisId");
            String keyWord = arguments.getString("keyWord","");
            initArgs(loadType,loadUrl,hisId,keyWord);

        }
    }

    private void initArgs(int loadType,String loadUrl,Long hisId ,String keyWord) {
        try{
        if(loadType == 1){
            this.loadType  = loadType;
            this.firstLoadUrl = loadUrl;
        }
        else if(loadType == 2){
            this.loadType = loadType;
            this.firstLoadUrl = loadUrl;
            this.currentHisId = hisId;
        }
        else if(loadType == 3){
            this.loadType = loadType;
            this.keyword = keyWord;
            this.firstLoadUrl = "https://m.baidu.com/s?wd="+ URLEncoder.encode(keyWord,"utf-8");
        }
        }catch (Exception e){

        }
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
                if(currentHisId != null && loadType == 2 && isTitleSet == false){
                   SearchPresenter.updateHistoryTitle(currentHisId,title);
                   isTitleSet = true;
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
                if (!TextUtils.isEmpty(url)) {
                     view.loadUrl(url);
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
        startPage();
    }

    private void startPage(){
        if(webView != null && !TextUtils.isEmpty(firstLoadUrl)){
            webView.loadUrl(firstLoadUrl);
        }
    }



    public static ContentFragment newInstance(LoadItem loadItem) {
        Bundle args = new Bundle();
        ContentFragment fragment = new ContentFragment();
        args.putInt("loadType",loadItem.getLoadType());
        args.putString("loadUrl",loadItem.getLoadURL());
        args.putString("keyWord",loadItem.getKeyWord());
        args.putLong("currentHisId",loadItem.getHistoryId());
        fragment.setArguments(args);
        return fragment;
    }

    public boolean goBack(){
        if(webView != null && webView.canGoBack()){
            webView.goBack();
            return true;
        }else{
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


}
