package cn.fuyoushuo.domain.entity;

import java.io.Serializable;

/**
 * Created by QA on 2017/2/28.
 */

public class TabItem implements Serializable {

     private Integer fragmentId;

     private String title;

     private String fragmentTag;


    public Integer getFragmentId() {
        return fragmentId;
    }

    public void setFragmentId(Integer fragmentId) {
        this.fragmentId = fragmentId;
    }

    public String getFragmentTag() {
        return fragmentTag;
    }

    public void setFragmentTag(String fragmentTag) {
        this.fragmentTag = fragmentTag;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public TabItem() {
    }

    public TabItem(Integer fragmentId, String fragmentTag, String title) {
        this.fragmentId = fragmentId;
        this.fragmentTag = fragmentTag;
        this.title = title;
    }
}
