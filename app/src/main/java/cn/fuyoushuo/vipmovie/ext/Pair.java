package cn.fuyoushuo.vipmovie.ext;

import java.io.Serializable;

/**
 * 描述fragment的具体信息
 * Created by QA on 2017/2/27.
 */

public class Pair implements Serializable {

    private String fragmentTag;

    private String title;

    public Pair(String fragmentTag) {
        this.fragmentTag = fragmentTag;
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
}
