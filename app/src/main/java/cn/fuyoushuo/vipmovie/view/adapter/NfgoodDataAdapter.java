package cn.fuyoushuo.vipmovie.view.adapter;

import android.content.Context;
import android.view.View;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jakewharton.rxbinding.view.RxView;
import com.superrecycleview.superlibrary.adapter.BaseViewHolder;
import com.superrecycleview.superlibrary.adapter.SuperBaseAdapter;

import java.util.concurrent.TimeUnit;

import cn.fuyoushuo.domain.entity.FGoodItem;
import cn.fuyoushuo.vipmovie.R;
import rx.functions.Action1;

/**
 * Created by QA on 2017/2/24.
 */

public class NfgoodDataAdapter extends SuperBaseAdapter<FGoodItem> {

    private int currentPage = 1;

    private Long cateId;

    public Long getCateId() {
        return cateId;
    }

    public void setCateId(Long cateId) {
        this.cateId = cateId;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    private OnLoad onLoad;

    public void setOnLoad(OnLoad onLoad) {
        this.onLoad = onLoad;
    }


    public NfgoodDataAdapter(Context context) {
        super(context);
    }

    @Override
    protected void convert(final BaseViewHolder holder, final FGoodItem item, int position) {

        holder.setText(R.id.bottom_item_good_titletext,item.getWebSmallTitle());
        holder.setText(R.id.bottom_item_good_originprice,"￥"+item.getPriceYuan());
        holder.setText(R.id.bottom_item_good_sellcount,"已售"+item.getSoldCount());
        holder.setText(R.id.bottom_item_good_discount,"返"+item.getFanliPercent());
        holder.setText(R.id.bottom_item_good_pricesaved,"约"+item.getFanliYuan()+"元");

        if(onLoad != null){
            onLoad.onLoadImage((SimpleDraweeView) holder.getView(R.id.bottom_item_good_image),item);

            RxView.clicks(holder.itemView).throttleFirst(1000, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
                @Override
                public void call(Void aVoid) {
                    onLoad.onGoodItemClick(holder.itemView,item);
                }
            });
        }


    }

    @Override
    protected int getItemViewLayoutId(int position, FGoodItem item) {
        return R.layout.bottom_recycle_item;
    }

    public interface OnLoad{
        void onLoadImage(SimpleDraweeView view, FGoodItem goodItem);

        void onGoodItemClick(View clickView, FGoodItem goodItem);
    }
}
