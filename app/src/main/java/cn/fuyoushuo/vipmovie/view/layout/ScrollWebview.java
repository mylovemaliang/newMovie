package cn.fuyoushuo.vipmovie.view.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * Created by QA on 2017/2/23.
 */

public class ScrollWebview extends WebView {



    public ScrollWebview(Context context) {
        super(context);
    }

    public ScrollWebview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollWebview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
           return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
