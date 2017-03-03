package cn.fuyoushuo.domain.entity;

import java.io.Serializable;

/**
 * Created by QA on 2017/3/3.
 */

public class SiteItem implements Serializable{

    private String name;

    private String imgUrl;

    private String contentUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
