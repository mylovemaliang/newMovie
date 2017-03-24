package cn.fuyoushuo.vipmovie.view.flagment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.jakewharton.rxbinding.view.RxView;
import com.trello.rxlifecycle.FragmentEvent;
import com.trello.rxlifecycle.components.support.RxDialogFragment;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.fuyoushuo.vipmovie.R;
import cn.fuyoushuo.vipmovie.view.flagment.hisormark.hisFragment;
import cn.fuyoushuo.vipmovie.view.flagment.hisormark.markFragment;
import rx.functions.Action1;

/**
 * Created by QA on 2017/3/20.
 */

public class HisOrMarkDialogFragment extends RxDialogFragment{

    public static final String TAG = "HisOrMarkDialogFragment";

    @Bind(R.id.hisormark_backArea)
    RelativeLayout backArea;

    @Bind(R.id.tabLayout)
    TabLayout tabLayout;

    @Bind(R.id.viewpage)
    ViewPager viewPager;

    private int parentFragmentId;

    private Map<String,Fragment> fragmentMap = new LinkedHashMap<String, Fragment>();

    public Map<String, Fragment> getFragmentMap() {
        return fragmentMap;
    }

    public void setFragmentMap(Map<String, Fragment> fragmentMap) {
        this.fragmentMap = fragmentMap;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.fullScreenDialog);
        if(getArguments() != null){
            this.parentFragmentId = getArguments().getInt("parentFragmentId",-1);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_hisormark_layout, container, false);
        ButterKnife.bind(this,inflate);
        //初始化
        FragmentManager childFragmentManager = getChildFragmentManager();
        MyPageAdapter myPageAdapter = new MyPageAdapter(childFragmentManager);
        for(Map.Entry<String,Fragment> entry : this.getFragmentMap().entrySet()){
            myPageAdapter.addFragment(entry.getValue(),entry.getKey());
        }
        viewPager.setAdapter(myPageAdapter);
        viewPager.setOffscreenPageLimit(2);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout.setupWithViewPager(viewPager);
        return inflate;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RxView.clicks(backArea).compose(this.<Void>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .throttleFirst(1000, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        dismissAllowingStateLoss();
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fragmentMap.clear();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public static HisOrMarkDialogFragment newInstance(int parentFragmentId) {
        Bundle args = new Bundle();
        args.putInt("parentFragmentId",parentFragmentId);
        HisOrMarkDialogFragment fragment = new HisOrMarkDialogFragment();
        fragment.setArguments(args);
        fragment.getFragmentMap().put("历史记录", hisFragment.newInstance(parentFragmentId));
        fragment.getFragmentMap().put("书签", markFragment.newInstance(parentFragmentId));
        return fragment;
    }

    static class MyPageAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public MyPageAdapter(FragmentManager fm) {
            super(fm);
        }
        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }
        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }

        public List<String> getmFragmentTitles() {
            return mFragmentTitles;
        }

        public List<Fragment> getmFragments() {
            return mFragments;
        }
    }
}
