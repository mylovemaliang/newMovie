package cn.fuyoushuo.commonlib.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by QA on 2016/7/11.
 */
public class CommonUtils {


    public static String getShortTitle(String title){
        if(TextUtils.isEmpty(title)){
            return "";
        }
        if(title.length() <= 10){
            return title;
        }
        return title.substring(0,10)+"...";
    }

    public static String getShortString(String url,int size){
        if(TextUtils.isEmpty(url)){
            return "";
        }
        if(url.length() <= size){
            return url;
        }
        return url.substring(0,size)+"...";
    }

    public static String getShortTitleForDownload(String url){
        String name = new File(url).getName();
        String endString = "";
        if(name.contains(".")){
            int end = name.lastIndexOf(".");
            endString = name.substring(end+1,name.length());
            name = name.substring(0,end);
        }
        if(name.length() <= 16){
            return name+"."+endString;
        }else{
            String before = name.substring(0,8);
            String after = name.substring(name.length()-9,name.length());
            return before+"..."+after+"."+endString;
        }
    }

    /**
     * 字符串格式化为字符串数组
     * @param itemsstring
     * @return List<String>
     */
    public static List<String> toStringList(String itemsstring){
        List<String> resultList = new ArrayList<String>();
        if(TextUtils.isEmpty(itemsstring)) return resultList;
        if(itemsstring.startsWith(",") && itemsstring.endsWith(",")){
            itemsstring = itemsstring.substring(1,itemsstring.length()-1);
        }
        String[] split = itemsstring.split(",");
        return Arrays.asList(split);
    }

    /**
     * 得到整百数
     * @param origin
     * @return
     */
    public static int getIntHundred(int origin){
        int hundreds = origin / 100;
        if(hundreds%100 != 0){
            hundreds += 1;
        }
        return hundreds*100;
    }

    /**
     * 得到浮点数的整数部分
     * @param origin
     * @return
     */
    public static int getIntHundred(float origin){
       return (int)origin;
    }


    /**
     * 获取app缓存路径
     * @param context
     * @return
     */
    public static File getCachePath( Context context ){
        File cache ;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            //外部存储可用
            cache = context.getExternalCacheDir();
        }else {
            //外部存储不可用
            cache = context.getCacheDir();
        }
        return cache;
    }

    public static String string2JsJson(String s) {
        StringBuffer sb = new StringBuffer ();
        char[] chars = s.toCharArray();
        for (int i=0; i<chars.length;i++){
          char c = s.charAt(i);
          switch (c) {
            case '\"':
                sb.append("\\\"");
                break;
            case '\\':
                sb.append("\\\\");
                break;
            case '/':
                sb.append("\\/");
                break;
            case '\b':
                sb.append("\\b");
                break;
            case '\f':
                sb.append("\\f");
                break;
            case '\n':
                sb.append("\\n");
                break;
            case '\r':
                sb.append("\\r");
                break;
            case '\t':
                sb.append("\\t");
                break;
            default:
                sb.append(c);
          }
        }
        return sb.toString();
    }

}
