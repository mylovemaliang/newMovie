package cn.fuyoushuo.vipmovie;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.github.lazylibrary.util.NetWorkUtils;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import org.apache.log4j.Logger;

import cn.fuyoushuo.commonlib.utils.CommonUtils;
import cn.fuyoushuo.commonlib.utils.Constants;
import cn.fuyoushuo.domain.httpservice.BaiduHttpService;
import cn.fuyoushuo.domain.httpservice.FqbbHttpService;
import cn.fuyoushuo.domain.httpservice.NewsHttpService;
import cn.fuyoushuo.domain.httpservice.VipkdyHttpService;
import cn.fuyoushuo.domain.httpservice.VipwwwHttpService;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Connection;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @Project CommonProject
 * @Packate com.micky.commonproj.domain.service
 *
 * @Description Retrofit 服务管理
 *
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2015-12-22 14:43
 * @Version 1.0
 */
public class ServiceManager {

    private static HashMap<String, Object> mServiceMap = new HashMap<String, Object>();

    public static Context context = MyApplication.getContext();

    /**
     *  创建Retrofit Service
     * @param t Service类型
     * @param <T>
     * @return
     */
    public static <T> T createService(Class<T> t) {
        T service = (T) mServiceMap.get(t.getName());

        if (service == null) {
            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

            if (Constants.DEBUG) {
                //日志处理
                HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String s) {
                       Logger.getLogger(getClass()).debug(s);
                    }
                });
                loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                clientBuilder.addInterceptor(loggingInterceptor);
            }


            //缓存处理
            final File baseDir = CommonUtils.getCachePath(context);
            if (baseDir != null) {
                final File cacheDir = new File(baseDir, "HttpResponseCache");
                clientBuilder.cache(new Cache(cacheDir, Constants.HTTP_RESPONSE_DISK_CACHE_MAX_SIZE));
            }

            clientBuilder.interceptors().add(new ServiceInterceptor());
            if(t.getName().equals(BaiduHttpService.class.getName())){
                clientBuilder.interceptors().add(new JsonInterceptor());
            }

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(getEndPoint(t))
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(clientBuilder.build())
                    .build();

            service = retrofit.create(t);
            mServiceMap.put(t.getName(), service);
        }
        return service;
    }

    /**
     *  获取EndPoint URL
     * @param t Service类型
     * @param <T>
     * @return
     */
    public static <T> String getEndPoint(Class<T> t) {
        String endPoint = "";
        if (t.getName().equals(FqbbHttpService.class.getName())) {
            endPoint = Constants.ENDPOINT_FQBB;
        }
        if(t.getName().equals(NewsHttpService.class.getName())){
            endPoint = Constants.ENDPOINT_NEWS;
        }
        if(t.getName().equals(BaiduHttpService.class.getName())){
            endPoint = Constants.ENDPOINT_BAIDU;
        }
        if(t.getName().equals(VipkdyHttpService.class.getName())){
            endPoint = Constants.ENDPOINT_VIPKDY;
        }
        if(t.getName().equals(VipwwwHttpService.class.getName())){
            endPoint = Constants.ENDPOINT_WWW_VIP;
        }
        if ("".equals(endPoint)) {
            throw new IllegalArgumentException("Error: Can't get end point url. Please configure at the method " + ServiceManager.class.getSimpleName() + ".getEndPoint(T t)");
        }
        return endPoint;
    }

    /**
     * OkHttpClient的拦截器
     */
    static class ServiceInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Response response;
            if(NetWorkUtils.getNetworkTypeName(context).equals(NetWorkUtils.NETWORK_TYPE_DISCONNECT)){
                Request newRuquest = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
                response = chain.proceed(newRuquest);
            }else{
                Request newRuquest = request.newBuilder().cacheControl(CacheControl.FORCE_NETWORK).build();
                response = chain.proceed(newRuquest);
            }
            return response;
      }
    }

    static class JsonInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originResponse = chain.proceed(chain.request());
            String string = originResponse.body().string();
            String replaceString = string.replace("(", "").replace(")", "");
            //Log.d("json interceptor:",replaceString);
            Response newResponse = originResponse.newBuilder().body(ResponseBody.create(MediaType.parse("text/json,charset=gbk"), replaceString)).build();
            return newResponse;
        }
    }
}