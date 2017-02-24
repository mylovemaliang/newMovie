package cn.fuyoushuo.vipmovie.view.flagment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import cn.fuyoushuo.vipmovie.R;

/**
 * Created by QA on 2017/2/22.
 */

public class TabFragment extends BaseFragment{


    FragmentManager fragmentManager;

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
    protected void initView() {
        super.initView();
        MainFragment mainFragment = MainFragment.newInstance();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.tab_fragment_area,mainFragment,"main_fragment").show(mainFragment);
        fragmentTransaction.commitAllowingStateLoss();
        fragmentManager.executePendingTransactions();
    }

    public static TabFragment newInstance() {
        TabFragment fragment = new TabFragment();
        return fragment;
    }
}
