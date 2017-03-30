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
        if (parent.getChildLayoutPosition(view) % 5 == 0) {
            outRect.left = 10;
            outRect.right = 20;
        }else{
            outRect.left = 20;
            outRect.right = 20;
        }
        if(parent.getChildLayoutPosition(view) <= 4){
            outRect.top=30;
        }else{
            outRect.top = 20;
        }
    }
}
