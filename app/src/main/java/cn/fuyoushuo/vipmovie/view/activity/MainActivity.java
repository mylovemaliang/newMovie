package cn.fuyoushuo.vipmovie.view.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import cn.fuyoushuo.vipmovie.R;
import cn.fuyoushuo.vipmovie.ext.FragmentTagGenerator;
import cn.fuyoushuo.vipmovie.ext.LocalFragmentManger;
import cn.fuyoushuo.vipmovie.view.flagment.TabFragment;

public class MainActivity extends BaseActivity{


    FragmentManager fragmentManager;

    Fragment mContent;

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        initFragment();
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

    //转换flagment
    public void switchContent(Fragment from,Fragment to){
        if (mContent != to) {
            mContent = to;
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.addToBackStack(null);
            if (!to.isAdded()) {    // 先判断是否被add过
                transaction.hide(from).add(R.id.fragment_area, to).commitAllowingStateLoss(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(from).show(to).commitAllowingStateLoss(); // 隐藏当前的fragment，显示下一个
            }
            fragmentManager.executePendingTransactions();
        }
    }

}
