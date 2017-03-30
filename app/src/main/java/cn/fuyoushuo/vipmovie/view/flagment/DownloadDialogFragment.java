package cn.fuyoushuo.vipmovie.view.flagment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.jakewharton.rxbinding.view.RxView;
import com.trello.rxlifecycle.FragmentEvent;
import com.trello.rxlifecycle.components.support.RxDialogFragment;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.fuyoushuo.domain.entity.DownloadTask;
import cn.fuyoushuo.vipmovie.R;
import cn.fuyoushuo.vipmovie.presenter.impl.DownloadPresenter;
import cn.fuyoushuo.vipmovie.view.adapter.DownloadAdapter;
import cn.fuyoushuo.vipmovie.view.layout.DividerItemDecoration;
import cn.fuyoushuo.vipmovie.view.layout.MyGridLayoutManager;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by QA on 2017/3/27.
 * 查询当前的下载详情
 */

public class DownloadDialogFragment extends RxDialogFragment{

    @Bind(R.id.download_backArea)
    RelativeLayout backArea;

    @Bind(R.id.download_rview)
    RecyclerView downloadRview;

    DownloadAdapter downloadAdapter;

    private Subscription subscription;

    DownloadPresenter downloadPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.fullScreenDialog);
        downloadPresenter = new DownloadPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_download, container,false);
        ButterKnife.bind(this,inflate);
        downloadAdapter = new DownloadAdapter();
        downloadAdapter.setDownloadCallback(new DownloadAdapter.DownloadCallback() {
            @Override
            public void onLoadProgress(NumberProgressBar progressBar,TextView downloadInfoText,DownloadTask downloadTask) {
                downloadPresenter.updateDownloadView(downloadTask,progressBar,downloadInfoText);
            }
        });
        downloadRview.setHasFixedSize(true);
        final MyGridLayoutManager gridLayoutManager = new MyGridLayoutManager(getActivity(),1);
        gridLayoutManager.setSpeedFast();
        gridLayoutManager.setAutoMeasureEnabled(true);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        downloadRview.setLayoutManager(gridLayoutManager);
        downloadRview.setAdapter(downloadAdapter);


        downloadRview.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));
        return inflate;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RxView.clicks(backArea).throttleFirst(1000,TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                       dismissAllowingStateLoss();
                    }
                });
    }




    @Override
    public void onStart() {
        super.onStart();
        downloadPresenter.getAllDownloads(new DownloadPresenter.FindAllDownloadCallback() {
            @Override
            public void onAllDownloadsReturn(List<DownloadTask> downloadTasks, boolean isOk) {
                if(downloadAdapter != null && isOk){
                    downloadAdapter.setData(downloadTasks);
                    downloadAdapter.notifyDataSetChanged();
                    initProgressObserver();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        if(downloadPresenter != null){
            downloadPresenter.onDestroy();
        }
        if(!subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }
    }

    //初始化download监听
    private void initProgressObserver(){
        subscription = Observable.interval(1000, TimeUnit.MILLISECONDS).compose(this.<Long>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        if (downloadAdapter != null) {
                            downloadAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    public static DownloadDialogFragment newInstance() {
        
        Bundle args = new Bundle();
        
        DownloadDialogFragment fragment = new DownloadDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
