package cn.fuyoushuo.vipmovie.view.flagment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.jakewharton.rxbinding.view.RxView;
import com.trello.rxlifecycle.FragmentEvent;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import cn.fuyoushuo.commonlib.utils.RxBus;
import cn.fuyoushuo.domain.entity.HistoryItem;
import cn.fuyoushuo.vipmovie.R;
import cn.fuyoushuo.vipmovie.ext.FragmentTagGenerator;
import cn.fuyoushuo.vipmovie.ext.LocalFragmentManger;
import cn.fuyoushuo.vipmovie.po.LoadItem;
import cn.fuyoushuo.vipmovie.presenter.impl.TabPresenter;
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
        mainFragment = MainFragment.newInstance();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.tab_fragment_area,mainFragment,"main_fragment").show(mainFragment);
        mContent = mainFragment;
        fragmentTransaction.commitAllowingStateLoss();
        fragmentManager.executePendingTransactions();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
                           }

                           @Override
                           public void onError() {
                                Log.d("capture callback","error");
                           }
                       });
                    }
                });

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
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                return;
            }

            @Override
            public void onNext(RxBus.BusEvent busEvent) {
                if(busEvent instanceof MainFragment.toContentViewEvent){
                    //替换子fragment,这里需要新建fragment,不能使用hide,show
                    MainFragment.toContentViewEvent event = (MainFragment.toContentViewEvent) busEvent;
                    String url = event.getNewItem().getNewUrl();
                    handCommonToContentEvent(url);
                }
                else if(busEvent instanceof SearchDialogFragment.toContentPageFromSearchEvent){
                    SearchDialogFragment.toContentPageFromSearchEvent event = (SearchDialogFragment.toContentPageFromSearchEvent) busEvent;
                    HistoryItem historyItem = event.getHistoryItem();
                    handToContentEvent(historyItem);
                }
            }
        }));
    }

    private void handCommonToContentEvent(String url){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        if(contentFragment != null && fragmentManager.findFragmentByTag("content_fragment") != null){
            fragmentTransaction.remove(contentFragment);
        }
        LoadItem loadItem = new LoadItem(1,url,null).bindId(-1l);
        ContentFragment contentFragment = ContentFragment.newInstance(loadItem);
        this.contentFragment = contentFragment;
        fragmentTransaction.hide(mainFragment).add(R.id.tab_fragment_area,this.contentFragment,"content_fragment").show(contentFragment);
        mContent = this.contentFragment;
        fragmentTransaction.commitAllowingStateLoss();
        fragmentManager.executePendingTransactions();
    }

    private void handToContentEvent(HistoryItem historyItem){
        int historyType = historyItem.getHistoryType();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.addToBackStack(null);
        if(contentFragment != null && fragmentManager.findFragmentByTag("content_fragment") != null){
            fragmentTransaction.remove(contentFragment);
        }
        if(historyType == 1){
            Long id = historyItem.getId();
            String historyUrl = historyItem.getHistoryUrl();
            LoadItem loadItem = new LoadItem(2,historyUrl,null).bindId(id);
            ContentFragment contentFragment = ContentFragment.newInstance(loadItem);
            this.contentFragment = contentFragment;
            fragmentTransaction.hide(mainFragment).add(R.id.tab_fragment_area,this.contentFragment,"content_fragment").show(contentFragment);
            mContent = this.contentFragment;
        }
        if(historyType == 2){
            Long id = historyItem.getId();
            String keyword = historyItem.getHistoryTitle();
            LoadItem loadItem = new LoadItem(3,null,keyword).bindId(id);
            ContentFragment contentFragment = ContentFragment.newInstance(loadItem);
            this.contentFragment = contentFragment;
            fragmentTransaction.hide(mainFragment).add(R.id.tab_fragment_area,this.contentFragment,"content_fragment").show(contentFragment);
            mContent = this.contentFragment;
        }
        fragmentTransaction.commitAllowingStateLoss();
        fragmentManager.executePendingTransactions();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        tabPresenter = new TabPresenter(activity);
    }

    @Override
    public void onStart() {
        super.onStart();
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
