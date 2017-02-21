package cn.fuyoushuo.vipmovie.view.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by QA on 2017/2/21.
 */

public class MyScrollingView extends ScrollView {

    private ScrollingChangedListener scrollingChangedListener;

    public void setScrollingChangedListener(ScrollingChangedListener scrollingChangedListener) {
        this.scrollingChangedListener = scrollingChangedListener;
    }

    public MyScrollingView(Context context) {
        super(context);
    }

    public MyScrollingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onScrollChanged(int l, int y, int oldl, int oldy) {
        super.onScrollChanged(l, y, oldl, oldy);
        if(scrollingChangedListener != null){
            //手指上滑
            if(oldy < y){
               scrollingChangedListener.onScroll(oldy,y,false);
            }else if(oldy > y){
               scrollingChangedListener.onScroll(oldy,y,true);
            }
        }
    }

    public interface ScrollingChangedListener{

        /**
         * 当滑动的时候,进行监听
         * @param oldy 上次滑动的y坐标
         * @param dy   此次滑动的y坐标
         * @param isCloseTop  是否接近顶部
         */
        void onScroll(int oldy, int dy, boolean isCloseTop);

    }


}

