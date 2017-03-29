package cn.fuyoushuo.vipmovie.view.flagment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.trello.rxlifecycle.FragmentEvent;
import com.zhy.android.percent.support.PercentLinearLayout;

import java.util.Observer;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import cn.fuyoushuo.commonlib.utils.RxBus;
import cn.fuyoushuo.domain.entity.BookMark;
import cn.fuyoushuo.domain.entity.HistoryItem;
import cn.fuyoushuo.domain.entity.UserTrack;
import cn.fuyoushuo.vipmovie.MyApplication;
import cn.fuyoushuo.vipmovie.R;
import cn.fuyoushuo.vipmovie.ext.AppInfoManger;
import cn.fuyoushuo.vipmovie.ext.FragmentTagGenerator;
import cn.fuyoushuo.vipmovie.ext.LocalFragmentManger;
import cn.fuyoushuo.vipmovie.po.LoadItem;
import cn.fuyoushuo.vipmovie.presenter.impl.TabPresenter;
import cn.fuyoushuo.vipmovie.view.flagment.hisormark.hisFragment;
import cn.fuyoushuo.vipmovie.view.flagment.hisormark.markFragment;
import cn.fuyoushuo.vipmovie.view.layout.MenuWindow;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by QA on 2017/2/22.
 */

public class TabFragment extends BaseFragment{

    @Bind(R.id.tab_count_area)
    RelativeLayout tabCountArea;

    @Bind(R.id.back_area)
    RelativeLayout toLeftArea;

    @Bind(R.id.to_right_area)
    RelativeLayout toRightArea;

    @Bind(R.id.more_area)
    RelativeLayout menuArea;

    @Bind(R.id.shouye_area)
    RelativeLayout toMainArea;

    @Bind(R.id.tab_fragment_bottom)
    PercentLinearLayout bottomTabBar;

    @Bind(R.id.tab_fragment_area)
    LinearLayout tabFragmentLayout;

    @Bind(R.id.tab_count_text)
    TextView tabCountText;

    private Integer fragmentId;

    TabPresenter tabPresenter;

    public Integer getFragmentId() {
        return fragmentId;
    }

    public void setFragmentId(Integer fragmentId) {
        this.fragmentId = fragmentId;
    }

    FragmentManager fragmentManager;

    Fragment mContent;

    private CompositeSubscription mSubscriptions;

    //内容fragment
    ContentFragment contentFragment;

    //主fragment
    MainFragment mainFragment;

    Handler handler = new Handler();

    MenuWindow menuWindow;

    @Override
    protected String getPageName() {
        return "tab_fragment";
    }

    @Override
    protected int getRootLayoutId() {
        return R.layout.fragment_tab;
    }

    @Override
    protected void initData() {
        fragmentManager = getChildFragmentManager();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if(getArguments() != null){
            this.fragmentId = getArguments().getInt("fragmentId",0);
        }
        mSubscriptions = new CompositeSubscription();
        //初始化总线事件响应
        initBusEvent();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        super.initView();

        //初始化菜单
        menuWindow = new MenuWindow(mactivity,bottomTabBar).init();

        mainFragment = MainFragment.newInstance(this.fragmentId);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.tab_fragment_area,mainFragment,"main_fragment").show(mainFragment);
        mContent = mainFragment;
        fragmentTransaction.commitAllowingStateLoss();
        fragmentManager.executePendingTransactions();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //初始化tab的个数
        Integer tabSize = LocalFragmentManger.getIntance().getTabSize();
        tabCountText.setText(String.valueOf(tabSize));

        menuWindow.setOnItemClick(new MenuWindow.OnItemClick() {
            @Override
            public void onNoPicClick() {
                if(AppInfoManger.getIntance().isNoPic()){
                    Toast.makeText(MyApplication.getContext(),"无图模式已打开",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MyApplication.getContext(),"无图模式已关闭",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onHisClick() {
                HisOrMarkDialogFragment.newInstance(fragmentId).show(getFragmentManager(),"HisOrMarkDialogFragment");
            }

            @Override
            public void onAddMarkClick() {
                addBookmark();
            }

            @Override
            public void onRefreshClick() {
                 refreshView();
            }

            @Override
            public void onExit() {
                //关闭应用
                Toast.makeText(MyApplication.getContext(),"应用马上就要关闭了",Toast.LENGTH_SHORT).show();
                Observable.timer(3,TimeUnit.SECONDS)
                        .subscribe(new Action1<Long>() {
                            @Override
                            public void call(Long aLong) {
//                                MyApplication.getMyapplication().finishAllActivity();
//                                MyApplication.getMyapplication().finishProgram();
                                  DownloadDialogFragment.newInstance().show(getFragmentManager(),"DownloadDialogFragment");
                            }
                        });
            }
        });

        RxView.clicks(tabCountArea).compose(this.<Void>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .throttleFirst(1000,TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                       tabPresenter.captureScreen(fragmentId, new TabPresenter.CaptureCallback() {
                           @Override
                           public void onSuccess() {
                                Log.d("capture callback","success");
                                SwipeDialogFragment swipeDialogFragment = SwipeDialogFragment.newInstance();
                                swipeDialogFragment.show(getFragmentManager(),"swipeDialogFragment");
                                if(contentFragment != null && fragmentManager.findFragmentByTag("content_fragment") != null){
                                    contentFragment.onSwipeApear();
                                }
                           }

                           @Override
                           public void onError() {
                                Log.d("capture callback","error");
                           }
                       });
                    }
                });

        //向左
        RxView.clicks(toLeftArea).compose(this.<Void>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .throttleFirst(1000,TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if(mContent == mainFragment){
                            Toast.makeText(MyApplication.getContext(),"not allow to move back",Toast.LENGTH_SHORT).show();
                        }else{
                            //进入自身的goback流程
                            goback();
                        }
                    }
                });

        //向右
        RxView.clicks(toRightArea).compose(this.<Void>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .throttleFirst(1000,TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        goforward();
                    }
                });

        //菜单
        RxView.clicks(menuArea).compose(this.<Void>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .throttleFirst(1000,TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        // TODO: 2017/3/15
                        if(menuWindow.isShowing()){
                            menuWindow.dismissWindow();
                        }else{
                            menuWindow.showWindow();
                        }
                    }
                });

        RxView.clicks(toMainArea).compose(this.<Void>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .throttleFirst(1000,TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if(mContent != mainFragment){
                            switchContent(mContent,mainFragment);
                        }
                    }
                });


    }

    /**
     * 刷新页面
     */
    public void refreshView(){
        if(mainFragment != null && mContent == mainFragment){
              mainFragment.refresh();
        }
        else if(contentFragment != null &&  mContent == contentFragment)
        {
              contentFragment.refresh();
        }
    }

    public void updateTabSize(){
        if(isDetched) return;
        Integer tabSize = LocalFragmentManger.getIntance().getTabSize();
        tabCountText.setText(String.valueOf(tabSize));
    }

    //当fragment视图回到当前的fragment
    public void onClickToThisFragment(int fragmentId){
        if(isDetched || this.fragmentId != fragmentId) return;
        if(contentFragment != null && mContent == contentFragment && fragmentManager.findFragmentByTag("content_fragment") != null){
             contentFragment.onSwipeDiss();
        }
    }

    //添加标签
    private void addBookmark(){
        if(contentFragment != null && mContent == contentFragment){
            contentFragment.addBookmark();
        }else{
            Toast.makeText(MyApplication.getContext(),"当前页面不能添加标签",Toast.LENGTH_SHORT).show();
        }
    }


    //转换flagment
    public void switchContent(Fragment from, Fragment to){
        if (mContent != to) {
            mContent = to;
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.addToBackStack(null);
            if (!to.isAdded()) {    // 先判断是否被add过
                transaction.hide(from).add(R.id.tab_fragment_area,to).commitAllowingStateLoss();
                // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(from).show(to).commitAllowingStateLoss(); // 隐藏当前的fragment，显示下一个
            }
            fragmentManager.executePendingTransactions();
        }
    }

    private void initBusEvent(){
        mSubscriptions.add(RxBus.getInstance().toObserverable().compose(this.<RxBus.BusEvent>bindToLifecycle()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<RxBus.BusEvent>() {
            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {
                return;
            }

            @Override
            public void onNext(RxBus.BusEvent busEvent) {

            if(busEvent instanceof MainFragment.toContentViewEvent){

                    //替换子fragment,这里需要新建fragment,不能使用hide,show
                    MainFragment.toContentViewEvent event = (MainFragment.toContentViewEvent) busEvent;
                    final String url = event.getNewItem().getNewUrl();
                    int parentFragmentId = event.getParentFragmentId();
                    if(parentFragmentId != fragmentId) return;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            handCommonToContentEvent(url);
                        }
                    },50);
                    return;
            }
            else if(busEvent instanceof SearchDialogFragment.toContentPageFromSearchEvent){
                SearchDialogFragment.toContentPageFromSearchEvent event = (SearchDialogFragment.toContentPageFromSearchEvent) busEvent;
                final HistoryItem historyItem = event.getHistoryItem();
                int parentFragmentId = event.getParentFragmentId();
                if(parentFragmentId != fragmentId) return;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        handToContentEvent(historyItem);
                    }
                },50);
                return;
            }
            else if(busEvent instanceof hisFragment.toContentPageFromHisPage){
                hisFragment.toContentPageFromHisPage event = (hisFragment.toContentPageFromHisPage) busEvent;
                final UserTrack userTrack = event.getUserTrack();
                int parentFragmentId = event.getParentFragmentId();
                if(parentFragmentId != fragmentId) return;
                final String url = userTrack.getTrackUrl();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        handCommonToContentEvent(url);
                    }
                },50);
            }
            else if(busEvent instanceof markFragment.toContentPageFromMarkPage){
                markFragment.toContentPageFromMarkPage event = (markFragment.toContentPageFromMarkPage) busEvent;
                final BookMark bookMark = event.getBookMark();
                int parentFragmentId = event.getParentFragmentId();
                if(parentFragmentId != fragmentId) return;
                final String url = bookMark.getMarkUrl();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       handCommonToContentEvent(url);
                    }
                },50);
             }
            }
        }));
    }

    private void handCommonToContentEvent(String url){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(contentFragment != null && fragmentManager.findFragmentByTag("content_fragment") != null){
            fragmentTransaction.remove(contentFragment);
        }
        LoadItem loadItem = new LoadItem(1,url,null).bindId(-1l);
        ContentFragment contentFragment = ContentFragment.newInstance(this.fragmentId,loadItem);
        this.contentFragment = contentFragment;
        fragmentTransaction.hide(mainFragment).add(R.id.tab_fragment_area,contentFragment,"content_fragment");
        mContent = this.contentFragment;
        fragmentTransaction.commitAllowingStateLoss();
        fragmentManager.executePendingTransactions();
    }

    private void handToContentEvent(HistoryItem historyItem){
        int historyType = historyItem.getHistoryType();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(contentFragment != null && fragmentManager.findFragmentByTag("content_fragment") != null){
            fragmentTransaction.remove(contentFragment);
        }
        if(historyType == 1){
            Long id = historyItem.getId();
            String historyUrl = historyItem.getHistoryUrl();
            LoadItem loadItem = new LoadItem(2,historyUrl,null).bindId(id);
            ContentFragment contentFragment = ContentFragment.newInstance(this.fragmentId,loadItem);
            this.contentFragment = contentFragment;
            fragmentTransaction.hide(mainFragment).add(R.id.tab_fragment_area,contentFragment,"content_fragment");
            mContent = this.contentFragment;
        }
        if(historyType == 2){
            Long id = historyItem.getId();
            String keyword = historyItem.getHistoryTitle();
            LoadItem loadItem = new LoadItem(3,null,keyword).bindId(id);
            ContentFragment contentFragment = ContentFragment.newInstance(this.fragmentId,loadItem);
            this.contentFragment = contentFragment;
            fragmentTransaction.hide(mainFragment).add(R.id.tab_fragment_area,contentFragment,"content_fragment");
            mContent = this.contentFragment;
        }
        fragmentTransaction.commitAllowingStateLoss();
        fragmentManager.executePendingTransactions();
    }

    //隐藏底部工具栏
    public void hideBottomBar(){
        bottomTabBar.setVisibility(View.GONE);
    }

    //显示底部工具栏
    public void showBottomeBar(){
        bottomTabBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        tabPresenter = new TabPresenter(activity);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * fragment 可以进行回退操作
     * @return
     */
    public boolean goback(){
        if(isDetched) return false;
        if(mContent == contentFragment){
            boolean childGoBackDone = contentFragment.goBack();
            if(childGoBackDone){
                 return true;
            }
        }
        boolean isGobackDone = false;
        if(mContent == contentFragment){
            switchContent(mContent,mainFragment);
            isGobackDone = true;
        }else if(mContent == mainFragment){
            isGobackDone = false;
        }
        return isGobackDone;
    }

    /**
     * fragment 进行向前操作
     */
    public void goforward(){
       if(mContent == mainFragment && contentFragment == null){
            Toast.makeText(MyApplication.getContext(),"not allow to move forward",Toast.LENGTH_SHORT).show();
       }
       else if(mContent == mainFragment && contentFragment != null && fragmentManager.findFragmentByTag("content_fragment") != null){
            switchContent(mContent,contentFragment);
       }
       else if(mContent != null && mContent == contentFragment){
           boolean result = contentFragment.goForward();
           if(!result){
             Toast.makeText(MyApplication.getContext(),"not allow to move forward",Toast.LENGTH_SHORT).show();
           }
       }
    }

    public static TabFragment newInstance(Integer fragmentId) {
        Bundle args = new Bundle();
        TabFragment fragment = new TabFragment();
        args.putInt("fragmentId",fragmentId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(!mSubscriptions.hasSubscriptions()){
            mSubscriptions.unsubscribe();
        }
    }
}
