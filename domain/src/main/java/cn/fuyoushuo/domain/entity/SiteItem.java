package cn.fuyoushuo.domain.entity;

import java.io.Serializable;

/**
 * Created by QA on 2017/3/3.
 */

public class SiteItem implements Serializable{

    private String name;

    private String url;

    private String img;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
