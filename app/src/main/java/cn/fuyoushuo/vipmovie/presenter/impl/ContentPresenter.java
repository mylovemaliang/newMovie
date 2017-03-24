package cn.fuyoushuo.vipmovie.presenter.impl;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by QA on 2017/3/22.
 */

public class ContentPresenter extends BasePresenter{

    private OkHttpClient okHttpClient = new OkHttpClient();

    private static Pattern compile = Pattern.compile("([a-zA-Z]{0,2}.status=[a-zA-Z]{0,2}),APP.postMessage\\(\"VIP_NOAD_VIDEO\",\\{tvid:[a-zA-Z]{0,2}\\}\\)\\);");

    public String jsReplaceToKillAd(final String url){
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            Response execute = okHttpClient.newCall(request).execute();
            String result = execute.body().string();
            String replace = getReplace(result);
            return replace;
        } catch (IOException e) {
            return "";
        }
    }


    private String getReplace(String input){
        Matcher matcher = compile.matcher(input);
        if(matcher.find()){
            String all = matcher.group(0);
            String group1 = matcher.group(1);
            String comcat = all + group1+";";
            String replace = input.replace(all,comcat);
            return replace;
        }
        return input;
    }
}
