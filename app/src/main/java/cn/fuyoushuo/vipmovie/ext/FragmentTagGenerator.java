package cn.fuyoushuo.vipmovie.ext;

import java.util.UUID;

/**
 * Created by QA on 2017/2/27.
 */

public class FragmentTagGenerator {

    public static String getFragmentTag(){
        return "tab-"+ UUID.randomUUID().toString();
    }

}
