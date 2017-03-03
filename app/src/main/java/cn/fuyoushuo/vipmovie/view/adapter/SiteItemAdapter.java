package cn.fuyoushuo.vipmovie.view.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.zxing.oned.ITFReader;
import com.jakewharton.rxbinding.view.RxView;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.fuyoushuo.domain.entity.NewType;
import cn.fuyoushuo.domain.entity.SiteItem;
import cn.fuyoushuo.vipmovie.MyApplication;
import cn.fuyoushuo.vipmovie.R;
import rx.functions.Action1;

/**
 * Created by QA on 2016/6/27.
 */
public class SiteItemAdapter extends BaseListAdapter<SiteItem>{

    private OnSiteClick onSiteClick;

    public void setOnSiteClick(OnSiteClick onSiteClick) {
        this.onSiteClick = onSiteClick;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_site_view, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder,position);
        final ItemViewHolder currentHolder = (ItemViewHolder) holder;
        final SiteItem item = getItem(position);
        RxView.clicks(currentHolder.itemView).throttleFirst(1000,TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if(onSiteClick != null) {
                    onSiteClick.onClick(currentHolder.itemView, item);
                }
            }
        });
        currentHolder.siteName.setText(item.getName());
        currentHolder.siteImage.setImageURI(Uri.parse(item.getImgUrl()));
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.site_name) TextView siteName;

        @Bind(R.id.site_image) SimpleDraweeView siteImage;

        public ItemViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(ItemViewHolder.this,itemView);
        }
    }

    public interface OnSiteClick {
        void onClick(View view, SiteItem typeItem);
    }

}
