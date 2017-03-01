package cn.fuyoushuo.vipmovie.view.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.fuyoushuo.domain.entity.NewItem;
import cn.fuyoushuo.vipmovie.R;

/**
 * 多类型新闻条目的实现
 * Created by QA on 2017/3/1.
 */

public class NewsAdapter extends BaseListAdapter<NewItem>{

    public static int ITEM_VIEW_TYPE_IMAGES_1 = 1;

    public static int ITEM_VIEW_TYPE_IMAGES_3 = 2;


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         if(viewType == ITEM_VIEW_TYPE_IMAGES_1){
             View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bottom_rview1, parent, false);
             return new OneImageViewHolder(view1);
         }
         else if(viewType == ITEM_VIEW_TYPE_IMAGES_3){
             View view3 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bottom_rview2, parent, false);
             return new ThreeImageViewHolder(view3);
         }
         return super.onCreateViewHolder(parent,viewType);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = holder.getItemViewType();
        NewItem newItem = mDataList.get(position);
        if(itemViewType == ITEM_VIEW_TYPE_IMAGES_1){
            OneImageViewHolder holder1 = (OneImageViewHolder) holder;
            holder1.newTitle.setText(newItem.getTopic());
            holder1.newSource.setText(newItem.getSource());
            holder1.simpleDraweeView.setImageURI(Uri.parse(newItem.getImageUrls().get(0)));
        }
        else if(itemViewType == ITEM_VIEW_TYPE_IMAGES_3){
            ThreeImageViewHolder holder2 = (ThreeImageViewHolder) holder;
            holder2.newTitle.setText(newItem.getTopic());
            holder2.newSource.setText(newItem.getSource());
            holder2.image1.setImageURI(Uri.parse(newItem.getImageUrls().get(0)));
            holder2.image2.setImageURI(Uri.parse(newItem.getImageUrls().get(1)));
            holder2.image3.setImageURI(Uri.parse(newItem.getImageUrls().get(2)));
        }
        else{

        }
    }


    @Override
    public int getItemViewType(int position) {
        NewItem newItem = mDataList.get(position);
        Integer imageSize = newItem.getImageSize();
        if(imageSize == 1){
            return ITEM_VIEW_TYPE_IMAGES_1;
        }
        else if(imageSize == 3){
            return ITEM_VIEW_TYPE_IMAGES_3;
        }
        return super.getItemViewType(position);
    }

    public class OneImageViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.news_image)
        SimpleDraweeView simpleDraweeView;

        @Bind(R.id.news_title)
        TextView newTitle;

        @Bind(R.id.news_source)
        TextView newSource;

        public OneImageViewHolder(View itemView){
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public class ThreeImageViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.news3_image1)
        SimpleDraweeView image1;

        @Bind(R.id.news3_image2)
        SimpleDraweeView image2;

        @Bind(R.id.news3_image3)
        SimpleDraweeView image3;

        @Bind(R.id.news3_title)
        TextView newTitle;

        @Bind(R.id.news3_source)
        TextView newSource;

        public ThreeImageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }


}
