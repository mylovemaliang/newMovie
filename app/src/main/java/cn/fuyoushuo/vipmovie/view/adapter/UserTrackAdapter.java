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

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.fuyoushuo.commonlib.utils.CommonUtils;
import cn.fuyoushuo.domain.entity.HistoryItem;
import cn.fuyoushuo.domain.entity.UserTrack;
import cn.fuyoushuo.vipmovie.R;
import rx.functions.Action1;

/**
 * Created by QA on 2017/3/20.
 */

public class UserTrackAdapter extends BaseListAdapter<UserTrack>{

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private OnUtClick onUtClick;

    public void setOnUtClick(OnUtClick onUtClick) {
        this.onUtClick = onUtClick;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_track_view, parent, false);
        return new UserTrackAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder,position);
        final ItemViewHolder currentHolder = (ItemViewHolder) holder;
        final UserTrack item = getItem(position);
        if(item.getCreateTime() != null){
            String dateString = dateFormat.format(item.getCreateTime());
            currentHolder.dateView.setText(dateString);
        }
        if(!TextUtils.isEmpty(item.getTrackName())){
            String nameString = CommonUtils.getShortString(item.getTrackName(),20);
            currentHolder.nameView.setText(nameString);
        }
        //点击事件
        RxView.clicks(holder.itemView).throttleFirst(1000,TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if(onUtClick != null){
                            onUtClick.onClick(holder.itemView,item);
                        }
                    }
                });
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.item_date_text)
        TextView dateView;

        @Bind(R.id.item_name_text)
        TextView nameView;

        public ItemViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface OnUtClick {

        void onClick(View view, UserTrack userTrack);
    }
}
