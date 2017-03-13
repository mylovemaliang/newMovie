package cn.fuyoushuo.vipmovie.view.flagment;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.lzyzsd.jsbridge.BridgeUtil;
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.zhy.android.percent.support.PercentLinearLayout;

import java.net.URLEncoder;

import butterknife.Bind;
import cn.fuyoushuo.vipmovie.MyApplication;
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

    FrameLayout fullVideoArea;

    View customView;

    IX5WebChromeClient.CustomViewCallback customViewCallback;


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
        getActivity().getWindow().setFormat(PixelFormat.TRANSLUCENT);
        webView.getSettings().setJavaScriptEnabled(true);
        //mytixianWebview.getSettings().setBuiltInZoomControls(true);//是否显示缩放按钮，默认false
        webView.getSettings().setSupportZoom(true);//是否可以缩放，默认true
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.getSettings().setDomStorageEnabled(true);

        webView.getSettings().setUseWideViewPort(true);// 设置此属性，可任意比例缩放。大视图模式
        webView.getSettings().setLoadWithOverviewMode(true);// 和setUseWideViewPort(true)一起解决网页自适应问题

        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.getSettings().setBlockNetworkImage(true);
        webView.requestFocusFromTouch();
        webView.getSettings().setPluginState(com.tencent.smtt.sdk.WebSettings.PluginState.ON);
        webView.getSettings().setPluginsEnabled(true);//可以使用插件
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setDefaultTextEncodingName("UTF-8");

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            webView.getSettings().setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);

        if(Build.VERSION.SDK_INT <= 18){
            webView.getSettings().setSavePassword(false);
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
                    webView.getSettings().setBlockNetworkImage(false);
                }
                else{
                    if(progressBar != null){
                      progressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
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
                if(webView != null){
//                  String jsContent1 = BridgeUtil.assetFile2Str(MyApplication.getContext(), "vip3.js");
//                  webView.loadUrl("javascript:" + jsContent1);
                    String jsUrl = "http://testwww.fanqianbb.com/vip.js";
                    String js = "var newscript = document.createElement(\"script\");";
                    js += "newscript.src=\"" + jsUrl + "\";";
                    js += "document.scripts[0].parentNode.insertBefore(newscript,document.scripts[0]);";
                    view.loadUrl("javascript:" + js);
                }
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
        // 用户当前的首选方向
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
        // 退出全屏
        quitFullScreen();
        // 设置WebView可见
        webView.setVisibility(View.VISIBLE);
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
