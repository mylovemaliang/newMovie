package cn.fuyoushuo.vipmovie.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.fuyoushuo.commonlib.utils.CommonUtils;
import cn.fuyoushuo.domain.entity.HistoryItem;
import cn.fuyoushuo.vipmovie.R;
import rx.functions.Action1;

/**
 * Created by QA on 2017/3/20.
 */

public class HisAdapter extends BaseListAdapter<HistoryItem>{

    private OnHisClick onHisClick;

    public void setOnHisClick(OnHisClick onHisClick) {
        this.onHisClick = onHisClick;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hisormark_view, parent, false);
        return new HisAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder,position);
        final ItemViewHolder currentHolder = (ItemViewHolder) holder;
        final HistoryItem item = getItem(position);
        RxView.clicks(currentHolder.itemView).throttleFirst(1000, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if(onHisClick != null){
                   onHisClick.onClick(currentHolder.itemView, item);
                }
            }
        });
        RxView.clicks(currentHolder.endArea).throttleFirst(1000,TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if(onHisClick != null){
                    onHisClick.onDelete(currentHolder.itemView,item);
                }
            }
        });
        int historyType = item.getHistoryType();
        if(historyType == 1){
            currentHolder.headImage.setImageResource(R.mipmap.site);
        }else{
            currentHolder.headImage.setImageResource(R.mipmap.search_word);
        }
        String historyTitle = item.getHistoryTitle();
        if(!TextUtils.isEmpty(historyTitle) && !historyTitle.startsWith("http")){
            historyTitle = CommonUtils.getShortTitle(historyTitle);
        }
        currentHolder.hisText.setText(historyTitle);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.item_text)
        TextView hisText;

        @Bind(R.id.head_image)
        ImageView headImage;

        @Bind(R.id.end_image)
        ImageView deleteImage;

        @Bind(R.id.end)
        RelativeLayout endArea;

        public ItemViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface OnHisClick {
        void onClick(View view, HistoryItem typeItem);

        void onDelete(View view,HistoryItem historyItem);
    }
}
