package cn.fuyoushuo.domain.httpservice;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

import cn.fuyoushuo.domain.entity.HttpResp;
import cn.fuyoushuo.domain.entity.SiteItem;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by QA on 2017/3/10.
 */

public interface VipkdyHttpService {

    @GET("/t.json")
    Observable<List<SiteItem>> getHomeSiteInfo();

    @GET("/va/vcsession.htm")
    Observable<JSONObject> getSession();

}
