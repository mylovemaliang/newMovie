package cn.fuyoushuo.vipmovie.ext;

import java.io.Serializable;

/**
 * Created by QA on 2017/2/27.
 */

public class Pair implements Serializable {

     private String fragmentTag;

     private String screenshotPath;

    public Pair(String fragmentTag) {
        this.fragmentTag = fragmentTag;
    }

    public String getFragmentTag() {
        return fragmentTag;
    }

    public void setFragmentTag(String fragmentTag) {
        this.fragmentTag = fragmentTag;
    }

    public String getScreenshotPath() {
        return screenshotPath;
    }

    public void setScreenshotPath(String screenshotPath) {
        this.screenshotPath = screenshotPath;
    }
}
