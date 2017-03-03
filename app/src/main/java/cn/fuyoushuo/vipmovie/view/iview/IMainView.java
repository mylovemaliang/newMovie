package cn.fuyoushuo.vipmovie.view.iview;

import java.util.List;

import cn.fuyoushuo.domain.entity.FGoodItem;
import cn.fuyoushuo.domain.entity.NewItem;
import cn.fuyoushuo.domain.entity.SiteItem;

/**
 * Created by QA on 2017/2/24.
 */

public interface IMainView{

    void setupFgoodsView(Integer page, Long cateId, List<FGoodItem> goodItems, boolean isRefresh);

    void setupNewsView(List<NewItem> newItems,String type,boolean isNext,boolean isSucc);

    void setupHeadSites(List<SiteItem> siteItems,boolean isSucc);

}
