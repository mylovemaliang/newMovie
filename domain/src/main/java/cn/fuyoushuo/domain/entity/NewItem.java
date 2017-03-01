package cn.fuyoushuo.domain.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by QA on 2017/3/1.
 */

public class NewItem implements Serializable {

   private String topic;

   private String rowKey;

   private Integer imageSize;

   private List<String> imageUrls = new ArrayList<>();

   private String source;

   private String newUrl;

    public Integer getImageSize() {
        return imageSize;
    }

    public void setImageSize(Integer imageSize) {
        this.imageSize = imageSize;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getNewUrl() {
        return newUrl;
    }

    public void setNewUrl(String newUrl) {
        this.newUrl = newUrl;
    }

    public String getRowKey() {
        return rowKey;
    }

    public void setRowKey(String rowKey) {
        this.rowKey = rowKey;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}
