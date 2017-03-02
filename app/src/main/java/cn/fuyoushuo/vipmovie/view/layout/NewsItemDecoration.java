package cn.fuyoushuo.vipmovie.view.layout;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by QA on 2016/7/14.
 */
public class NewsItemDecoration extends RecyclerView.ItemDecoration {


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.bottom = 2;
            outRect.top = 2;
    }
}
