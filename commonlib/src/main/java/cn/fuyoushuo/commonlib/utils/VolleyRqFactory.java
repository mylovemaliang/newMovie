package cn.fuyoushuo.commonlib.utils;

import android.content.Context;
import cn.fuyoushuo.commonlib.utils.okhttp.OkHttpStack;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleyRqFactory {

    private static VolleyRqFactory volleyRqFactory;

    private RequestQueue mRequestQueue;

    public static VolleyRqFactory getInstance() {
        if(volleyRqFactory==null){
            synchronized (VolleyRqFactory.class) {
                if(volleyRqFactory==null){
                    volleyRqFactory = new VolleyRqFactory();
                }
            }
        }
        return volleyRqFactory;
    }

    public RequestQueue getQequestQueue(Context context){
        if (mRequestQueue == null) {
            synchronized (VolleyRqFactory.class) {
                if(mRequestQueue==null){
                    Context ctx = context;
                    mRequestQueue = Volley.newRequestQueue(ctx, new OkHttpStack(ctx));
                }
            }
        }

        return mRequestQueue;
    }

}
