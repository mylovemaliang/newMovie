package cn.fuyoushuo.vipmovie.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;

import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.fuyoushuo.domain.entity.TabItem;
import cn.fuyoushuo.vipmovie.R;
import cn.fuyoushuo.vipmovie.ext.BitmapManger;
import cn.fuyoushuo.vipmovie.view.layout.SimpleTouchCallback;
import rx.functions.Action1;

/**
 * Created by QA on 2017/2/28.
 */

public class TabDataAdapter extends BaseListAdapter<TabItem> implements SimpleTouchCallback.onMoveListener {


    private ItemActionListener itemActionListener;

    public void setItemActionListener(ItemActionListener itemActionListener) {
        this.itemActionListener = itemActionListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_swipe_tab, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        final ItemViewHolder currentHolder = (ItemViewHolder) holder;
        final TabItem item = getItem(position);
        currentHolder.imageView.setImageBitmap(BitmapManger.getIntance().getBitmap(item.getFragmentId()));
        String title = item.getTitle();
        if(!TextUtils.isEmpty(title)){
            currentHolder.titleView.setText(title);
        }
        RxView.longClicks(currentHolder.itemView).throttleFirst(1000, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                         if(itemActionListener != null){
                             itemActionListener.onItemClicked(item);
                         }
                    }
                });
    }

    @Override
    public void onItemDismiss(int position) {
          mDataList.remove(position);
          //删除RecycleView列表对应的item
          notifyItemRemoved(position);
          if(itemActionListener != null){
              itemActionListener.onItemRemove(mDataList.get(position));
          }
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.tab_item_image)
        ImageView imageView;

        @Bind(R.id.tab_title)
        TextView titleView;

        @Bind(R.id.close_tab)
        ImageView closeArea;

        public ItemViewHolder(final View itemView){
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface ItemActionListener{

          void onItemRemove(TabItem tabItem);

          void onItemClicked(TabItem tabItem);
    }
}
