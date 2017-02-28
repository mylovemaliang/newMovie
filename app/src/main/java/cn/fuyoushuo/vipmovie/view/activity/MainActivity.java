package cn.fuyoushuo.vipmovie.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import cn.fuyoushuo.commonlib.utils.RxBus;
import cn.fuyoushuo.domain.entity.TabItem;
import cn.fuyoushuo.vipmovie.R;
import cn.fuyoushuo.vipmovie.ext.FragmentTagGenerator;
import cn.fuyoushuo.vipmovie.ext.LocalFragmentManger;
import cn.fuyoushuo.vipmovie.view.flagment.SwipeDialogFragment;
import cn.fuyoushuo.vipmovie.view.flagment.TabFragment;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

public class MainActivity extends BaseActivity{


    FragmentManager fragmentManager;

    Fragment mContent;

    Handler handler = new Handler();

    private CompositeSubscription mSubscriptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        mSubscriptions = new CompositeSubscription();
        initFragment();
        initBusEvent();
    }

    private void initFragment(){
        //初始化状态
        String fragmentTag = FragmentTagGenerator.getFragmentTag();
        Integer fragmentId = LocalFragmentManger.getIntance().addFragment(fragmentTag);
        TabFragment tabFragment = TabFragment.newInstance(fragmentId);

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_area,tabFragment,fragmentTag).show(tabFragment);

        mContent = tabFragment;
        fragmentTransaction.commitAllowingStateLoss();
        fragmentManager.executePendingTransactions();
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
                 if(busEvent instanceof SwipeDialogFragment.addTabEvent){
                      //增加fragment
                     String fragmentTag = FragmentTagGenerator.getFragmentTag();
                     Integer fragmentId = LocalFragmentManger.getIntance().addFragment(fragmentTag);
                     TabFragment tabFragment = TabFragment.newInstance(fragmentId);
                     LocalFragmentManger.getIntance().setCurrentId(fragmentId);
                     switchContent(mContent,tabFragment,fragmentTag);
                 }
                 else if(busEvent instanceof SwipeDialogFragment.deleteTabEvent){

                 }
                 else if(busEvent instanceof SwipeDialogFragment.clickTabEvent){
                     SwipeDialogFragment.clickTabEvent event = (SwipeDialogFragment.clickTabEvent) busEvent;
                     clickTab(event.getTabItem());
                 }
            }
        }));
    }

    public void clickTab(TabItem tabItem){
       Integer fragmentId = tabItem.getFragmentId();
       String fragmentTag = tabItem.getFragmentTag();
       Fragment fragmentByTag = fragmentManager.findFragmentByTag(fragmentTag);
       if(!(fragmentByTag == mContent)) {
           LocalFragmentManger.getIntance().setCurrentId(fragmentId);
           switchContent(mContent,fragmentByTag,"");
       }else{
           return;
       }
    }


    //转换flagment
    public void switchContent(Fragment from,Fragment to,String fragmentTag){
        if (mContent != to) {
            mContent = to;
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.addToBackStack(null);
            if (!to.isAdded()) {    // 先判断是否被add过
                if(!TextUtils.isEmpty(fragmentTag)){
                   transaction.hide(from).add(R.id.fragment_area,to,fragmentTag).commitAllowingStateLoss();
                }else{
                    transaction.hide(from).add(R.id.fragment_area,to).commitAllowingStateLoss();
                }
                // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(from).show(to).commitAllowingStateLoss(); // 隐藏当前的fragment，显示下一个
            }
            fragmentManager.executePendingTransactions();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mSubscriptions.hasSubscriptions()){
            mSubscriptions.unsubscribe();
        }
    }
}
