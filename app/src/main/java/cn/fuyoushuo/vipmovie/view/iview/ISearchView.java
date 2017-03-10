package cn.fuyoushuo.vipmovie.view.iview;

import java.util.List;

import cn.fuyoushuo.domain.entity.HistoryItem;

/**
 * Created by QA on 2017/3/9.
 */

public interface ISearchView {

    void setHistoryItems(List<HistoryItem> result,boolean isOk);

    void onDelHistoryItem(boolean isOk);

    void onAddHistoryItem(boolean isOk);

    void setHistorySearchItems(List<HistoryItem> result,boolean isOk);

}
