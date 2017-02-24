package cn.fuyoushuo.vipmovie.ext;

import android.text.TextUtils;

import java.util.HashMap;

/**
 * Created by QA on 2017/2/22.
 */

public class LocalFragmentManger {

    private static class LocalFragmentMangerHolder{
        private static LocalFragmentManger INTANCE = new LocalFragmentManger();
    }

    public static LocalFragmentManger getIntance(){
        return LocalFragmentMangerHolder.INTANCE;
    }

    //此ID随着fragment的增加自增
    private Integer id = 0;

    private Integer currentId = 0;

    private HashMap<Integer,String> fragmentMap = new HashMap<>();

    //添加fragment
    private void addFragment(String fragmentTag){
        if(TextUtils.isEmpty(fragmentTag)) return;
        this.fragmentMap.put(id,fragmentTag);
        currentId = id;
        id++;
    }

    //移除fragment
    private void removeFragment(Integer id){
        if(id == null) return;
        if(fragmentMap.containsKey(id)){
            fragmentMap.remove(id);
        }
    }

    //指定当前正在显示的ID
    private void setCurrentId(Integer currentId){
        this.currentId = currentId;
    }

    //获取当前正在显示的
    private Integer getTabSize(){
        return fragmentMap.size();
    }

}
