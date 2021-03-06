package cn.fuyoushuo.vipmovie.ext;

import android.content.Context;
import android.os.Environment;
import android.support.v7.app.WindowDecorActionBar;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.fuyoushuo.domain.entity.TabItem;

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

    private LinkedHashMap<Integer,Pair> fragmentMap = new LinkedHashMap<Integer, Pair>();

    /**
     * 添加fragment,首次添加fragment肯定为首页
     * @param fragmentTag
     * @return
     */
    public Integer addFragment(String fragmentTag){
        if(TextUtils.isEmpty(fragmentTag)) return null;
        Pair pair = new Pair(fragmentTag);
        pair.setTitle("首页");
        this.fragmentMap.put(id,pair);
        currentId = id;
        id++;
        return currentId;
    }

    /**
     *  设置title
     *  @param fragmentId
     *  @param title
     */
    public void setTitle(Integer fragmentId,String title){
        if(fragmentId == null) return;
        if(fragmentMap.containsKey(fragmentId)){
            Pair pair = fragmentMap.get(fragmentId);
            pair.setTitle(title);
        }
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

    //获取map里存在的第一个fragment元素
    public TabItem getFirstFragment(){
        Set<Map.Entry<Integer, Pair>> entries = fragmentMap.entrySet();
        TabItem resultItem = new TabItem();
        if(entries.isEmpty()) return resultItem;
        Iterator<Map.Entry<Integer, Pair>> iterator = entries.iterator();
        while (iterator.hasNext()){
            Map.Entry<Integer, Pair> next = iterator.next();
            Integer id = next.getKey();
            Pair value = next.getValue();
            String title = value.getTitle();
            String fragmentTag = value.getFragmentTag();
            resultItem = new TabItem(id,fragmentTag,title);
            break;
        }
        return resultItem;
    }

    //指定当前正在显示的ID
    public void setCurrentId(Integer currentId){
        this.currentId = currentId;
    }

    public Integer getCurrentId() {
        return currentId;
    }

    //获取当前正在显示的视图数量
    public Integer getTabSize(){
        return fragmentMap.size();
    }

    //将map转化成List用于展示
    public List<TabItem> MapToTabItems(){
         List<TabItem> resultList = new ArrayList<TabItem>();
         if(fragmentMap.isEmpty()) return resultList;
         for(Map.Entry<Integer, Pair> entry : fragmentMap.entrySet()){
             Integer id = entry.getKey();
             Log.d("fragmentMapId", "MapToTabItems: "+ id);
             Pair pair = entry.getValue();
             String fragmentTag = pair.getFragmentTag();
             String fragmentTitle = pair.getTitle();
             resultList.add(new TabItem(id,fragmentTag,fragmentTitle));
         }
         return resultList;
    }

    public int getNeedDeleteItem(){
        Iterator<Map.Entry<Integer, Pair>> iterator = fragmentMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<Integer, Pair> next = iterator.next();
            int id = next.getKey();
            if(id == currentId) continue;
            return id;
        }
        return -1;
    }

}
