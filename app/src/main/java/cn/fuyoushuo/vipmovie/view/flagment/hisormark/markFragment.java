package cn.fuyoushuo.vipmovie.view.flagment.hisormark;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import java.util.List;

import butterknife.Bind;
import cn.fuyoushuo.commonlib.utils.RxBus;
import cn.fuyoushuo.domain.entity.BookMark;
import cn.fuyoushuo.vipmovie.R;
import cn.fuyoushuo.vipmovie.presenter.impl.SearchPresenter;
import cn.fuyoushuo.vipmovie.view.adapter.MarkAdapter;
import cn.fuyoushuo.vipmovie.view.flagment.BaseFragment;
import cn.fuyoushuo.vipmovie.view.flagment.HisOrMarkDialogFragment;
import cn.fuyoushuo.vipmovie.view.layout.DividerItemDecoration;

/**
 * Created by QA on 2017/3/20.
 */

public class markFragment extends BaseFragment {

    private MarkAdapter markAdapter;

    @Bind(R.id.mark_rview)
    RecyclerView markRview;

    private int parentFragmentId;

    private SearchPresenter searchPresenter;



    @Override
    protected String getPageName() {
        return "mark_fragment";
    }

    @Override
    protected int getRootLayoutId() {
        return R.layout.view_mark;
    }

    @Override
    protected void initData() {
        searchPresenter = new SearchPresenter();
        if(getArguments() != null){
            parentFragmentId = getArguments().getInt("parentFragmentId",-1);
        }
    }

    @Override
    protected void initView() {
        super.initView();
        markAdapter = new MarkAdapter();
        markAdapter.setBookmarkListener(new MarkAdapter.BookmarkListener() {
            @Override
            public void onClick(View view, BookMark bookMark) {
                 RxBus.getInstance().send(new toContentPageFromMarkPage(parentFragmentId,bookMark));
                 ((HisOrMarkDialogFragment)getParentFragment()).dismissAllowingStateLoss();
            }

            @Override
            public void onDelete(View view, BookMark bookMark) {
                searchPresenter.deleteBookMark(bookMark, new SearchPresenter.DeleteBookmarkCallback() {
                    @Override
                    public void onDeleteBookMark(boolean isOk) {
                        if(isOk){
                            searchPresenter.findAllBookmarks(new SearchPresenter.FindAllBookMarkCallback() {
                                @Override
                                public void onFindAllBookmarks(List<BookMark> result, boolean isOk) {
                                     if(isOk){
                                         markAdapter.setData(result);
                                         markAdapter.notifyDataSetChanged();
                                     }
                                }
                            });
                        }
                    }
                });
            }
        });
        markRview.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        markRview.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));
        markRview.setLayoutManager(layoutManager);
        //resultRview.setNestedScrollingEnabled(true);
        markRview.setAdapter(markAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        searchPresenter.findAllBookmarks(new SearchPresenter.FindAllBookMarkCallback() {
            @Override
            public void onFindAllBookmarks(List<BookMark> result, boolean isOk) {
                 if(isOk){
                     markAdapter.setData(result);
                     markAdapter.notifyDataSetChanged();
                 }
            }
        });
    }

    public static markFragment newInstance(int parentFragmentId) {
        Bundle args = new Bundle();
        args.putInt("parentFragmentId",parentFragmentId);
        markFragment fragment = new markFragment();
        fragment.setArguments(args);
        return fragment;
    }

   //--------------------------------------自定义事件------------------------------------------------
   public class toContentPageFromMarkPage extends RxBus.BusEvent{

       private BookMark bookMark;

       private int parentFragmentId;

       public toContentPageFromMarkPage(int parentFragmentId, BookMark bookMark) {
           this.parentFragmentId = parentFragmentId;
           this.bookMark = bookMark;
       }

       public int getParentFragmentId() {
           return parentFragmentId;
       }

       public void setParentFragmentId(int parentFragmentId) {
           this.parentFragmentId = parentFragmentId;
       }

       public BookMark getBookMark() {
           return bookMark;
       }

       public void setBookMark(BookMark bookMark) {
           this.bookMark = bookMark;
       }
   }
}
