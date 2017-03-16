package cn.fuyoushuo.vipmovie.view.flagment;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.zhy.android.percent.support.PercentLinearLayout;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import cn.fuyoushuo.vipmovie.R;
import cn.fuyoushuo.vipmovie.po.LoadItem;
import cn.fuyoushuo.vipmovie.presenter.impl.SearchPresenter;
import cn.fuyoushuo.vipmovie.view.X5View.X5BridgeUtils;
import cn.fuyoushuo.vipmovie.view.X5View.X5BridgeWebView;
import cn.fuyoushuo.vipmovie.view.X5View.X5BridgeWebViewClient;

/**
 * Created by QA on 2017/3/7.
 */

public class ContentFragment extends BaseFragment {

    @Bind(R.id.head_image)
    ImageView headImage;

    @Bind(R.id.content_searchText)
    TextView headText;

    @Bind(R.id.x5_webview)
    X5BridgeWebView webView;

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

    FrameLayout fullVideoArea;

    View customView;

    IX5WebChromeClient.CustomViewCallback customViewCallback;

    private int parentFragmentId;

    private static Map<String,String> defaultHeaders = new HashMap<String,String>();

    static {
        defaultHeaders.put("User-Agent","Mozilla/5.0 (Linux; Android 6.0; Le X620 Build/HEXCNFN5902012151S) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.91 Mobile Safari/537.36");
    }


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
            this.parentFragmentId = getArguments().getInt("parentFragmentId",-1);
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


    private TabFragment getMyTabFragment(){
        return (TabFragment)getParentFragment();
    }

    @Override
    protected void initView() {
        super.initView();
        getActivity().getWindow().setFormat(PixelFormat.TRANSLUCENT);

        WebSettings webSetting = webView.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        //webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true);
        webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);
        //webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setGeolocationEnabled(true);
        webSetting.setAppCacheMaxSize(Long.MAX_VALUE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            webSetting.setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);

        if(Build.VERSION.SDK_INT <= 18){
            webSetting.setSavePassword(false);
        }


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
                    if(progressBar != null){
                        progressBar.setVisibility(View.GONE);//加载完网页进度条消失
                     }
                }
                else{
                    if(progressBar != null){
                      progressBar.setVisibility(View.VISIBLE);
                      //开始加载网页时显示进度条
                      progressBar.setProgress(newProgress);//设置进度值
                    }
                }
            }

            @Override
            public void onShowCustomView(View view, IX5WebChromeClient.CustomViewCallback callback) {
                // 赋值给callback
                customViewCallback = callback;
                // 设置webView隐藏
                webView.setVisibility(View.GONE);
                customView = view;
                // 声明video，把之后的视频放到这里面去
                fullVideoArea = (FrameLayout) getActivity().findViewById(R.id.videoContainer);
                // 将video放到当前视图中
                fullVideoArea.addView(view);
                if(getMyTabFragment() != null){
                    getMyTabFragment().hideBottomBar();
                }
                // 横屏显示
                getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                // 设置全屏
                setFullScreen();
            }

            @Override
            public void onHideCustomView() {
                hideCustomView();
            }
        });

        webView.setWebViewClient(new X5BridgeWebViewClient(webView){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view,url);
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView webView, WebResourceRequest webResourceRequest) {
                return super.shouldInterceptRequest(webView, webResourceRequest);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view,url);
                if(webView != null){
//                    String jsContent1 = BridgeUtil.assetFile2Str(MyApplication.getContext(), "vip3.js");
//                    webView.loadUrl("javascript:" + jsContent1);

                      String jsUrl = "http://testwww.fanqianbb.com/vip.js";
                      X5BridgeUtils.webViewLoadJs(webView,jsUrl);
                }
            }

            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
                super.onReceivedSslError(webView, sslErrorHandler, sslError);
                sslErrorHandler.proceed();
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        startPage();
        if(loadType == 2 || loadType == 3){
            ContentHeadArea.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    private void startPage(){
        if(webView != null && !TextUtils.isEmpty(firstLoadUrl)){
            webView.loadUrl(firstLoadUrl);
            //webView.loadUrl("file:///android_asset/index.html");
        }
    }

    /**
     * 设置全屏
     */
    private void setFullScreen() {
        if(getActivity() == null) return;
        // 设置全屏的相关属性，获取当前的屏幕状态，然后设置全屏
       getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 全屏下的状态码：1098974464
        // 窗口下的状态吗：1098973440
    }

    /**
     * 退出全屏
     */
    private void quitFullScreen() {
        if(getActivity() == null) return;
        // 声明当前屏幕状态的参数并获取
        final WindowManager.LayoutParams attrs = getActivity().getWindow().getAttributes();
        attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getActivity().getWindow().setAttributes(attrs);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    private void hideCustomView(){
        if (customViewCallback != null) {
            // 隐藏掉
            customViewCallback.onCustomViewHidden();
        }
        if(customView != null){
            fullVideoArea.removeView(customView);
        }

        customView = null;
        fullVideoArea = null;
        if(getMyTabFragment() != null){
            getMyTabFragment().showBottomeBar();
        }
        // 用户当前的首选方向
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
        // 退出全屏
        quitFullScreen();
        // 设置WebView可见
        webView.setVisibility(View.VISIBLE);
    }

    public void onSwipeApear(){
        webView.callHandler("pause", "", new CallBackFunction() {
            @Override
            public void onCallBack(String data) {
                   webView.onPause();
            }
        });
    }

    public void onSwipeDiss(){
        webView.onResume();
        try {
            Thread.sleep(500l);
            webView.callHandler("play", "", new CallBackFunction() {
                @Override
                public void onCallBack(String data) {

                }
            });
        } catch (InterruptedException e) {

        }
    }



    public static ContentFragment newInstance(int parentFragmentId,LoadItem loadItem) {
        Bundle args = new Bundle();
        ContentFragment fragment = new ContentFragment();
        args.putInt("loadType",loadItem.getLoadType());
        args.putString("loadUrl",loadItem.getLoadURL());
        args.putString("keyWord",loadItem.getKeyWord());
        args.putLong("currentHisId",loadItem.getHistoryId());
        args.putInt("parentFragmentId",parentFragmentId);
        fragment.setArguments(args);
        return fragment;
    }

    public boolean goBack(){
        if(customView != null){
            hideCustomView();
            return true;
        }
        if(webView != null && webView.canGoBack()){
            webView.goBack();
            return true;
        }else{
            return false;
        }
    }

    public boolean goForward(){
        if(webView != null && webView.canGoForward()){
            webView.goForward();
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            webView.callHandler("play", "", new CallBackFunction() {
                @Override
                public void onCallBack(String data) {
                    webView.onPause();
                }
            });
        }else{
            try {
                webView.onResume();
                Thread.sleep(500l);
                webView.callHandler("pause", "", new CallBackFunction() {
                    @Override
                    public void onCallBack(String data) {

                    }
                });
            } catch (InterruptedException e) {

            }
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
            webView.freeMemory();
            webView.destroy();
            webView=null;
        }
    }
}
