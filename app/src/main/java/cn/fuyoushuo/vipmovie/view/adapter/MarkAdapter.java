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
import cn.fuyoushuo.domain.entity.BookMark;
import cn.fuyoushuo.vipmovie.R;
import rx.functions.Action1;

/**
 * Created by QA on 2017/3/20.
 */

public class MarkAdapter extends BaseListAdapter<BookMark>{

    private BookmarkListener bookmarkListener;

    public void setBookmarkListener(BookmarkListener bookmarkListener) {
        this.bookmarkListener = bookmarkListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hisormark_view, parent, false);
        return new MarkAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder,position);
        final MarkAdapter.ItemViewHolder currentHolder = (MarkAdapter.ItemViewHolder)holder;
        final BookMark item = getItem(position);
        RxView.clicks(currentHolder.itemView).throttleFirst(1000, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                bookmarkListener.onClick(currentHolder.itemView, item);
            }
        });

        RxView.clicks(currentHolder.deleteImage).throttleFirst(1000,TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                bookmarkListener.onDelete(currentHolder.itemView,item);
            }
        });
        currentHolder.headImage.setImageResource(R.mipmap.bookmark);

        String markName = item.getMarkName();
        if(!TextUtils.isEmpty(markName) && !markName.startsWith("http")){
            markName = CommonUtils.getShortTitle(markName);
        }
        currentHolder.hisText.setText(markName);
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

    public interface BookmarkListener{
        void onClick(View view, BookMark bookMark);

        void onDelete(View view,BookMark bookMark);
    }

}
