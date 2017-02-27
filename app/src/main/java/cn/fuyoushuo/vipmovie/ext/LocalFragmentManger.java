package cn.fuyoushuo.vipmovie.ext;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by QA on 2017/2/22.
 */

public class LocalFragmentManger {

    private Context context;

    private static class LocalFragmentMangerHolder{
        private static LocalFragmentManger INTANCE = new LocalFragmentManger();
    }

    public static LocalFragmentManger getIntance(){
        return LocalFragmentMangerHolder.INTANCE;
    }

    //初始化上下文
    public void initContext(Context context){
        this.context = context;
    }

    //此ID随着fragment的增加自增
    private Integer id = 0;

    private Integer currentId = 0;

    private LinkedHashMap<Integer,Pair> fragmentMap = new LinkedHashMap<>();

    //添加fragment
    public Integer addFragment(String fragmentTag){
        if(TextUtils.isEmpty(fragmentTag)) return null;
        Pair pair = new Pair(fragmentTag);
        String filePath = getCachePath(context) + "/fsnapshot/"+id;
        pair.setScreenshotPath(filePath);
        this.fragmentMap.put(id,pair);
        currentId = id;
        id++;
        return currentId;
    }

    //移除fragment
    public void removeFragment(Integer id){
        if(id == null) return;
        if(fragmentMap.containsKey(id)){
            fragmentMap.remove(id);
        }
    }

    public Pair getFragment(Integer id){
        if(id == null) return null;
        return fragmentMap.get(id);
    }

    //指定当前正在显示的ID
    private void setCurrentId(Integer currentId){
        this.currentId = currentId;
    }

    //获取当前正在显示的视图数量
    private Integer getTabSize(){
        return fragmentMap.size();
    }


    /**
     * 获取app缓存路径,保存界面截图效果
     * @param context
     * @return
     */
    public String getCachePath( Context context ){
        String cachePath ;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            //外部存储可用
            cachePath = context.getExternalCacheDir().getPath() ;
        }else {
            //外部存储不可用
            cachePath = context.getCacheDir().getPath() ;
        }
        return cachePath ;
    }

}
