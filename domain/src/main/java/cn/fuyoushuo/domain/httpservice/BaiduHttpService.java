package cn.fuyoushuo.domain.httpservice;

import com.alibaba.fastjson.JSONObject;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by QA on 2017/3/10.
 */

public interface BaiduHttpService {

    @GET("/su?p=3&cb=&qq-pf-to=pcqq.c2c")
    Observable<JSONObject> getBaiduKeyWords(@Query("wd") String keyword);

}
