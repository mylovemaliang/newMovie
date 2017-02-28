package cn.fuyoushuo.vipmovie.view.flagment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.animation.AnimatorCompatHelper;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.jakewharton.rxbinding.view.RxView;
import com.trello.rxlifecycle.FragmentEvent;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import cn.fuyoushuo.vipmovie.R;
import cn.fuyoushuo.vipmovie.presenter.impl.TabPresenter;
import rx.functions.Action1;

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
        super.onCreate(savedInstanceState);
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
                                SwipeDialogFragment.newInstance().show(getFragmentManager(),"swipeDialogFragment");
                           }

                           @Override
                           public void onError() {
                                Log.d("capture callback","error");
                           }
                       });
                    }
                });

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        tabPresenter = new TabPresenter(activity);
    }

    public static TabFragment newInstance(Integer fragmentId) {
        Bundle args = new Bundle();
        TabFragment fragment = new TabFragment();
        args.putInt("fragmentId",fragmentId);
        fragment.setArguments(args);
        return fragment;
    }


}
