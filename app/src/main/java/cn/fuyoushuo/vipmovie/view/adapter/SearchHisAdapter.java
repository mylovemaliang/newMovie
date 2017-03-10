package cn.fuyoushuo.vipmovie.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.fuyoushuo.domain.entity.HistoryItem;
import cn.fuyoushuo.domain.entity.NewType;
import cn.fuyoushuo.vipmovie.MyApplication;
import cn.fuyoushuo.vipmovie.R;
import rx.functions.Action1;

/**
 * Created by QA on 2016/6/27.
 */
public class SearchHisAdapter extends BaseListAdapter<HistoryItem>{

    private OnHisClick onHisClick;


    public void setOnHisClick(OnHisClick onHisClick) {
        this.onHisClick = onHisClick;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search_history, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder,position);
        final ItemViewHolder currentHolder = (ItemViewHolder) holder;
        final HistoryItem item = getItem(position);
        RxView.clicks(currentHolder.itemView).throttleFirst(1000,TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                onHisClick.onClick(currentHolder.itemView, item);
            }
        });
        currentHolder.hisText.setText(item.getHistoryTitle());
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.search_item_text) public TextView hisText;

        public ItemViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(ItemViewHolder.this,itemView);
        }
    }

    public interface OnHisClick {
        void onClick(View view, HistoryItem typeItem);
    }

}
