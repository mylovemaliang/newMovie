package cn.fuyoushuo.vipmovie.view.flagment.hisormark;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jakewharton.rxbinding.view.RxView;
import com.trello.rxlifecycle.FragmentEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import cn.fuyoushuo.commonlib.utils.RxBus;
import cn.fuyoushuo.domain.entity.HistoryItem;
import cn.fuyoushuo.domain.entity.UserTrack;
import cn.fuyoushuo.vipmovie.MyApplication;
import cn.fuyoushuo.vipmovie.R;
import cn.fuyoushuo.vipmovie.presenter.impl.SearchPresenter;
import cn.fuyoushuo.vipmovie.view.adapter.HisAdapter;
import cn.fuyoushuo.vipmovie.view.adapter.UserTrackAdapter;
import cn.fuyoushuo.vipmovie.view.flagment.BaseFragment;
import cn.fuyoushuo.vipmovie.view.flagment.HisOrMarkDialogFragment;
import cn.fuyoushuo.vipmovie.view.layout.DividerItemDecoration;
import rx.functions.Action1;

/**
 * Created by QA on 2017/3/20.
 * 记录用户踪迹
 */
public class hisFragment extends BaseFragment {

    @Bind(R.id.his_rview)
    RecyclerView rview;

    @Bind(R.id.clear_history)
    TextView chearHistory;

    UserTrackAdapter userTrackAdapter;

    private int parentFragmentId;

    private SearchPresenter searchPresenter;

    @Override
    protected String getPageName() {
        return "his_fragment";
    }

    @Override
    protected int getRootLayoutId() {
        return R.layout.view_his;
    }

    @Override
    protected void initData() {
        searchPresenter = new SearchPresenter();
        if(getArguments() != null){
            parentFragmentId = getArguments().getInt("parentFragmentId",-1);
        }
    }

    @Override
    protected void initView() {
        super.initView();
        userTrackAdapter = new UserTrackAdapter();
        userTrackAdapter.setOnUtClick(new UserTrackAdapter.OnUtClick() {
            @Override
            public void onClick(View view, UserTrack userTrack) {
                   RxBus.getInstance().send(new toContentPageFromHisPage(parentFragmentId,userTrack));
                   ((HisOrMarkDialogFragment)getParentFragment()).dismissAllowingStateLoss();
            }
        });
        rview.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rview.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));
        rview.setLayoutManager(layoutManager);
        //resultRview.setNestedScrollingEnabled(true);
        rview.setAdapter(userTrackAdapter);

        RxView.clicks(chearHistory).compose(this.<Void>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .throttleFirst(1000, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        searchPresenter.deleteAllUserTrack(new SearchPresenter.DeleteAllUtCallback() {
                            @Override
                            public void onDeleteAllUserTrack(boolean isOk) {
                                if(isOk) {
                                    Toast.makeText(MyApplication.getContext(), "历史记录清空成功", Toast.LENGTH_SHORT).show();
                                    userTrackAdapter.setData(new ArrayList<UserTrack>());
                                    userTrackAdapter.notifyDataSetChanged();
                                }
                            }
                        });
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        searchPresenter.findAllUserTrack(new SearchPresenter.FindAllUtCallback() {
            @Override
            public void onFindAllUserTrack(List<UserTrack> result, boolean isOk) {
                if(isOk){
                    userTrackAdapter.setData(result);
                    userTrackAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public static hisFragment newInstance(int parentFragmentId) {
        Bundle args = new Bundle();
        args.putInt("parentFragmentId",parentFragmentId);
        hisFragment fragment = new hisFragment();
        fragment.setArguments(args);
        return fragment;
    }

   //--------------------------------------------自定义事件--------------------------------------------
   public class toContentPageFromHisPage extends RxBus.BusEvent{

       private UserTrack userTrack;

       private int parentFragmentId;

       public toContentPageFromHisPage(int parentFragmentId, UserTrack userTrack) {
           this.parentFragmentId = parentFragmentId;
           this.userTrack = userTrack;
       }

       public UserTrack getUserTrack() {
           return userTrack;
       }

       public void setUserTrack(UserTrack userTrack) {
           this.userTrack = userTrack;
       }

       public int getParentFragmentId() {
           return parentFragmentId;
       }

       public void setParentFragmentId(int parentFragmentId) {
           this.parentFragmentId = parentFragmentId;
       }
   }
}


