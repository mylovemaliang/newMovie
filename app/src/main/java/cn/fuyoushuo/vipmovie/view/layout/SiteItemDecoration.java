package cn.fuyoushuo.vipmovie.view.layout;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by QA on 2016/7/14.
 */
public class SiteItemDecoration extends RecyclerView.ItemDecoration {


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //由于每行都只有3个，所以第一个都是3的倍数，把左边距设为0
        outRect.left = 20;
        outRect.right = 20;
        outRect.top = 20;
    }
}
