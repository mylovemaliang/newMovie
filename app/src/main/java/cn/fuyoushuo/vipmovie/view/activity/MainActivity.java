package cn.fuyoushuo.vipmovie.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.fuyoushuo.commonlib.utils.RxBus;
import cn.fuyoushuo.domain.entity.TabItem;
import cn.fuyoushuo.vipmovie.MyApplication;
import cn.fuyoushuo.vipmovie.R;
import cn.fuyoushuo.vipmovie.ext.BitmapManger;
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

    List<Fragment> backFragments = new ArrayList<Fragment>();

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
                     SwipeDialogFragment.deleteTabEvent event = (SwipeDialogFragment.deleteTabEvent) busEvent;
                     deleteTab(event.getTabItem());

                 }
                 else if(busEvent instanceof SwipeDialogFragment.clickTabEvent){
                     SwipeDialogFragment.clickTabEvent event = (SwipeDialogFragment.clickTabEvent) busEvent;
                     clickTab(event.getTabItem());
                 }
            }
        }));
    }

    //点击tab
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

    //删除tab
    public void deleteTab(TabItem tabItem){
        Integer fragmentId = tabItem.getFragmentId();
        String fragmentTag = tabItem.getFragmentTag();
        Fragment deleteFragment = fragmentManager.findFragmentByTag(fragmentTag);
        //开始删除元素
        LocalFragmentManger.getIntance().removeFragment(fragmentId);
        BitmapManger.getIntance().removeBitmap(fragmentId);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(deleteFragment == mContent){
            TabItem firstFragment = LocalFragmentManger.getIntance().getFirstFragment();
            String headFragmentTag = firstFragment.getFragmentTag();
            Fragment headFragmentByTag = fragmentManager.findFragmentByTag(headFragmentTag);
            switchContent(mContent,headFragmentByTag,"");
            LocalFragmentManger.getIntance().setCurrentId(firstFragment.getFragmentId());
            fragmentTransaction.remove(deleteFragment);
        }else{
            fragmentTransaction.remove(deleteFragment);
        }
        fragmentTransaction.commitAllowingStateLoss();
        fragmentManager.executePendingTransactions();
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

    private long exitTime = 0;

    @Override
    public void onBackPressed() {
        boolean isBackDone = false;
        if(mContent != null && mContent instanceof TabFragment){
            isBackDone = ((TabFragment) mContent).goback();
        }
        if(!isBackDone){
            exit();
        }
    }


    //所有Activity退出
    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finishAll();
        }
    }

    public void finishAll(){
        MyApplication.getMyapplication().finishAllActivity();
        MyApplication.getMyapplication().finishProgram();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mSubscriptions.hasSubscriptions()){
            mSubscriptions.unsubscribe();
        }
    }
}
