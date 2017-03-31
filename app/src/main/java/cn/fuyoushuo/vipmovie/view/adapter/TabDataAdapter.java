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
import cn.fuyoushuo.domain.entity.TabItem;
import cn.fuyoushuo.vipmovie.MyApplication;
import cn.fuyoushuo.vipmovie.R;
import cn.fuyoushuo.vipmovie.ext.BitmapManger;
import cn.fuyoushuo.vipmovie.ext.LocalFragmentManger;
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
        String title = item.getTitle();
        Integer fragmentId = item.getFragmentId();
        currentHolder.imageView.setImageBitmap(BitmapManger.getIntance().getBitmap(fragmentId));
        if(!TextUtils.isEmpty(title)){
            currentHolder.titleView.setText(CommonUtils.getShortTitle(title));
        }
        //如果是当前当前页面,就改变head的颜色
        if(LocalFragmentManger.getIntance().getCurrentId() == fragmentId){
            currentHolder.headArea.setBackgroundColor(MyApplication.getContext().getResources().getColor(R.color.module_6));
        }
        RxView.clicks(currentHolder.itemView).throttleFirst(1000, TimeUnit.MILLISECONDS)
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
        TabItem tabItem = mDataList.get(position);
        boolean isOnlyOne = true;
          if(mDataList.size() == 1){
              isOnlyOne = true;
          }else{
              isOnlyOne = false;
          }
          mDataList.remove(position);
          notifyItemRemoved(position);
          if(isOnlyOne){
             if(itemActionListener != null){
                 itemActionListener.onItemRemove(tabItem,true);
             }
          }else{
              if(itemActionListener != null){
                  itemActionListener.onItemRemove(tabItem,false);
              }
          }
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.tab_item_image)
        ImageView imageView;

        @Bind(R.id.tab_title)
        TextView titleView;

        @Bind(R.id.close_tab)
        ImageView closeArea;

        @Bind(R.id.headArea)
        RelativeLayout headArea;

        public ItemViewHolder(final View itemView){
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface ItemActionListener{

          void onItemRemove(TabItem tabItem,boolean isOnlyOne);

          void onItemClicked(TabItem tabItem);
    }
}
