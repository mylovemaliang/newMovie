package cn.fuyoushuo.domain.httpservice;

import cn.fuyoushuo.domain.entity.HttpResp;
import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by QA on 2017/3/28.
 */

public interface VipwwwHttpService {

    @GET("/va/vcsession.htm")
    Observable<HttpResp> getSession();

    @GET("/va/sc.htm")
    Observable<HttpResp> getCookie(@Query("sessionid") String sessionId,@Query("t") Long t,@Query("mc") String mc,@Query("vurl") String domain);

}
