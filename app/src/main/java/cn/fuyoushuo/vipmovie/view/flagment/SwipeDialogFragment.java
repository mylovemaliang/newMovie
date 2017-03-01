package cn.fuyoushuo.vipmovie.view.flagment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jakewharton.rxbinding.view.RxView;
import com.trello.rxlifecycle.FragmentEvent;
import com.trello.rxlifecycle.components.support.RxDialogFragment;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.fuyoushuo.commonlib.utils.RxBus;
import cn.fuyoushuo.domain.entity.TabItem;
import cn.fuyoushuo.vipmovie.R;
import cn.fuyoushuo.vipmovie.ext.LocalFragmentManger;
import cn.fuyoushuo.vipmovie.view.adapter.TabDataAdapter;
import cn.fuyoushuo.vipmovie.view.layout.SimpleTouchCallback;
import cn.fuyoushuo.vipmovie.view.layout.TabItemDecoration;
import rx.functions.Action1;

/**
 * Created by QA on 2017/2/27.
 */

public class SwipeDialogFragment extends RxDialogFragment {

    @Bind(R.id.tab_recycleview)
    RecyclerView recyclerView;

    @Bind(R.id.addTab)
    ImageView addImage;

    @Bind(R.id.backto)
    ImageView backImage;

    TabDataAdapter tabDataAdapter;

    ItemTouchHelper itemTouchHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,R.style.fullScreenDialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_swipe, container, false);
        ButterKnife.bind(this,inflate);
        return inflate;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabDataAdapter = new TabDataAdapter();
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getActivity());
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager1);
        recyclerView.addItemDecoration(new TabItemDecoration());
        tabDataAdapter.setItemActionListener(new TabDataAdapter.ItemActionListener() {
            @Override
            public void onItemRemove(TabItem tabItem,boolean isOnlyOne) {
                //item删除触发事件
                if(isOnlyOne){
                    dismissAllowingStateLoss();
                }else{
                    RxBus.getInstance().send(new deleteTabEvent(tabItem));
                }
            }

            @Override
            public void onItemClicked(TabItem tabItem) {
                //item长点击触发事件
                RxBus.getInstance().send(new clickTabEvent(tabItem));
                dismissAllowingStateLoss();
            }
        });
        recyclerView.setAdapter(tabDataAdapter);
        ItemTouchHelper.Callback simpleTouchCallback = new SimpleTouchCallback(tabDataAdapter);
        itemTouchHelper = new ItemTouchHelper(simpleTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        tabDataAdapter.setData(LocalFragmentManger.getIntance().MapToTabItems());
        tabDataAdapter.notifyDataSetChanged();

        RxView.clicks(addImage).compose(this.<Void>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .throttleFirst(1000, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                         RxBus.getInstance().send(new addTabEvent());
                         dismissAllowingStateLoss();
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public static SwipeDialogFragment newInstance() {
        Bundle args = new Bundle();
        SwipeDialogFragment fragment = new SwipeDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }



    //------------------------------------总线事件----------------------------------------------------------

    public class addTabEvent extends RxBus.BusEvent{}

    //删除tab事件
    public class deleteTabEvent extends RxBus.BusEvent{

        private TabItem tabItem;

        public TabItem getTabItem() {
            return tabItem;
        }

        public void setTabItem(TabItem tabItem) {
            this.tabItem = tabItem;
        }

        public deleteTabEvent(TabItem tabItem) {
            this.tabItem = tabItem;
        }
    }

    //点击tab事件
    public class clickTabEvent extends RxBus.BusEvent{

        private TabItem tabItem;

        public clickTabEvent(TabItem tabItem) {
            this.tabItem = tabItem;
        }

        public TabItem getTabItem() {
            return tabItem;
        }

        public void setTabItem(TabItem tabItem) {
            this.tabItem = tabItem;
        }
    }

}
