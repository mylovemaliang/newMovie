package cn.fuyoushuo.domain.httpservice;

import com.alibaba.fastjson.JSONObject;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;


/**
 * Created by maliang on 2016/6/30.
 */
public interface NewsHttpService {

    @GET("/jsonnew/refresh?qid=7qudh")
    Observable<JSONObject> lastestNews(@Query("type") String type);

    @GET("/jsonnew/next?qid=7qudh")
    Observable<JSONObject> nextNews(@Query("type") String type,@Query("startkey") String startKey);

}
