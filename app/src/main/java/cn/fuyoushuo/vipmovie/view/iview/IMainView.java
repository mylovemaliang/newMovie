package cn.fuyoushuo.vipmovie.view.iview;

import java.util.List;

import cn.fuyoushuo.domain.entity.FGoodItem;

/**
 * Created by QA on 2017/2/24.
 */

public interface IMainView{

    void setupFgoodsView(Integer page, Long cateId, List<FGoodItem> goodItems, boolean isRefresh);

}
