package cn.fuyoushuo.commonlib.utils;

import java.io.File;

/**
 * @Project CommonProject
 * @Packate com.micky.commonlib.utils
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Date 2015-12-30 17:43
 * @Version 1.0
 */
public class Constants {

    //网络相关
    public static final int HTTP_RESPONSE_DISK_CACHE_MAX_SIZE = 10 * 1024 * 1024;

    public static final String ENDPOINT_IP = "http://ip.taobao.com";
    public static final String ENDPOINT_WEATHER = " http://api.map.baidu.com";
    public static final String BAIDU_AK = "MPDgj92wUYvRmyaUdQs1XwCf";

    //测试
    //public static final String ENDPOINT_FQBB_LOCAL = "http://testwww.fanqianbb.com";

    public static final String ENDPOINT_FQBB = "http://www.fanqianbb.com";

    public static final String ENDPOINT_NEWS="http://newswifiapi.dftoutiao.com";

    public static final String ENDPOINT_BAIDU="http://www.baidu.com";

//    public static final String ENDPOINT_VIPKDY="http://www.sviphome.com";
//
//    public static final String ENDPOINT_WWW_VIP = "http://www.sviphome.com";

    //test
    public static final String ENDPOINT_VIPKDY="http://180.76.146.164:8085";

    //--------------------------------自由解析变量----------------------------------------------------

    public static final String FreeResovleUrl = "http://c.vipkdy.com/q2.htm?";

    public static final String CloudResovleUrl = "http://www.vipkdy.com/vc/wi.htm";

    public static final String CookieGetUrl = "http://180.76.146.164:8085/va/sc.htm";


    public static final boolean DEBUG = false;

    //日志相关
    public static final String BASE_FILE_PATH = "vipmovie";
    public static final String LOG_PATH = BASE_FILE_PATH + File.separator + "log";
    public static final String LOG_FILE = BASE_FILE_PATH + ".log";
}
