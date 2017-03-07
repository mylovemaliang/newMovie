package cn.fuyoushuo.vipmovie.view.flagment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.android.percent.support.PercentRelativeLayout;

import java.util.List;

import butterknife.Bind;
import cn.fuyoushuo.commonlib.utils.RxBus;
import cn.fuyoushuo.domain.entity.FGoodItem;
import cn.fuyoushuo.domain.entity.NewItem;
import cn.fuyoushuo.domain.entity.NewType;
import cn.fuyoushuo.domain.entity.SiteItem;
import cn.fuyoushuo.vipmovie.MyApplication;
import cn.fuyoushuo.vipmovie.R;
import cn.fuyoushuo.vipmovie.presenter.impl.MainPresenter;
import cn.fuyoushuo.vipmovie.view.adapter.FgoodDataAdapter;
import cn.fuyoushuo.vipmovie.view.adapter.NewsAdapter;
import cn.fuyoushuo.vipmovie.view.adapter.SiteItemAdapter;
import cn.fuyoushuo.vipmovie.view.adapter.TypeDataAdapter;
import cn.fuyoushuo.vipmovie.view.iview.IMainView;
import cn.fuyoushuo.vipmovie.view.layout.MyGridLayoutManager;
import cn.fuyoushuo.vipmovie.view.layout.MyScrollingView;
import cn.fuyoushuo.vipmovie.view.layout.NewsItemDecoration;
import cn.fuyoushuo.vipmovie.view.layout.SiteItemDecoration;
import cn.fuyoushuo.vipmovie.view.layout.TypeItemsDecoration;

/**
 * Created by QA on 2017/2/22.
 */

public class MainFragment extends BaseFragment implements IMainView{


    @Bind(R.id.scrollView)
    MyScrollingView myScrollingView;

    @Bind(R.id.topImage)
    ImageView topImageView;

    @Bind(R.id.title)
    TextView title;

    @Bind(R.id.top)
    RecyclerView top;

    @Bind(R.id.title_container)
    PercentRelativeLayout titleContainer;

    @Bind(R.id.top_container)
    LinearLayout topContainer;

    @Bind(R.id.title_ess_container)
    PercentRelativeLayout titleEssContainer;

    @Bind(R.id.top_ess_container)
    LinearLayout topEssContainer;

//    @Bind(R.id.main_flagment_refreshLayout)
//    RefreshLayout myRefreshLayout;

    @Bind(R.id.main_bottomRcycleView)
    RecyclerView myRecycleView;

    private int titleAreaHeight;

    private int topAreaHeight;

    private MainPresenter mainPresenter;

    FgoodDataAdapter fgoodDataAdapter;

    Handler handler = new Handler();

    NewsAdapter newsAdapter;

    TypeDataAdapter typeDataAdapter;

    @Bind(R.id.sites_area)
    RecyclerView siteRview;

    SiteItemAdapter siteItemAdapter;

    @Override
    protected String getPageName() {
        return "main_fragment_page";
    }

    @Override
    protected int getRootLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void initData() {
       mainPresenter = new MainPresenter(this);
    }

    @Override
    protected void initView() {
        super.initView();
        myScrollingView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                titleAreaHeight = titleContainer.getTop();
                topAreaHeight = topContainer.getTop() - titleContainer.getHeight();
              }
            });

        myScrollingView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                boolean isCloseTop = false;
                int dy = scrollY;
                if(oldScrollY <= scrollY){
                    isCloseTop = false;
                }else if(oldScrollY > scrollY){
                    isCloseTop = true;
                }

                if (!isCloseTop && dy < titleAreaHeight) {
                    HeaderTranslate(dy);
                }
                if (!isCloseTop && dy > titleAreaHeight) {
                    HeaderTranslate(titleAreaHeight);
                    if (title.getParent() == titleContainer) {
                        titleContainer.removeView(title);
                        titleEssContainer.addView(title);
                        titleEssContainer.setBackgroundColor(getResources().getColor(R.color.module_19));
                    }
                    if (dy > topAreaHeight) {
                        if (top.getParent() == topContainer) {
                            topContainer.removeView(top);
                            topEssContainer.addView(top);
                        }
                    }
                } else if (isCloseTop && dy < topAreaHeight) {
                    if (top.getParent() == topEssContainer) {
                        topEssContainer.removeView(top);
                        topContainer.addView(top);
                    }
                    if (dy < titleAreaHeight) {
                        HeaderTranslate(dy);
                        if (title.getParent() == titleEssContainer) {
                            titleEssContainer.removeView(title);
                            titleEssContainer.setBackgroundColor(getResources().getColor(R.color.transparent));
                            titleContainer.addView(title);
                        }
                    }
                }
//                View childAtFirst = v.getChildAt(0);
//                if(childAtFirst.getMeasuredHeight() <= scrollY + v.getHeight()){
//                    handler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            loadMore();
//                        }
//                    },500);
//
//                }
            }
        });

//        fgoodDataAdapter = new FgoodDataAdapter();
//        myRecycleView.setHasFixedSize(true);
//        //mainBottomRView.addItemDecoration(new GoodItemsDecoration(10,5));
//        final MyGridLayoutManager gridLayoutManager = new MyGridLayoutManager(mactivity, 2);
//        gridLayoutManager.setSpeedFast();
//        //gridLayoutManager.setSpeedSlow();
//        gridLayoutManager.setAutoMeasureEnabled(true);
//        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
//        gridLayoutManager.setSmoothScrollbarEnabled(true);
//        gridLayoutManager.setAutoMeasureEnabled(true);
//        myRecycleView.setLayoutManager(gridLayoutManager);
//        myRecycleView.setNestedScrollingEnabled(true);
//        fgoodDataAdapter.setOnLoad(new FgoodDataAdapter.OnLoad() {
//            @Override
//            public void onLoadImage(SimpleDraweeView view, FGoodItem goodItem) {
//                int mScreenWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth();
//                int intHundred = CommonUtils.getIntHundred(mScreenWidth/2);
//                if(intHundred > 800){
//                    intHundred = 800;
//                }
//                if(!BaseActivity.isTablet(mactivity)){
//                    intHundred = 300;
//                }
//                String imgurl = goodItem.getImageUrl();
//                imgurl = imgurl.replaceFirst("_[1-9][0-9]{0,2}x[1-9][0-9]{0,2}\\.jpg","");
//                imgurl = imgurl+ "_"+intHundred+"x"+intHundred+".jpg";
//                view.setAspectRatio(1.0F);
//                view.setImageURI(Uri.parse(imgurl));
//            }
//
//            @Override
//            public void onGoodItemClick(View clickView, FGoodItem goodItem) {
//
//            }
//        });
//        myRecycleView.setAdapter(fgoodDataAdapter);

          typeDataAdapter = new TypeDataAdapter();
          typeDataAdapter.setOnCateClick(new TypeDataAdapter.OnTypeClick() {
             @Override
             public void onClick(View view, NewType typeItem, int lastPosition) {
                  typeItem.setRed(true);
                  NewType oldItem = typeDataAdapter.getItem(lastPosition);
                  if(oldItem != typeItem){
                     oldItem.setRed(false);
                  }
                  typeDataAdapter.notifyDataSetChanged();
                  String type = typeItem.getTypeCode();
                  mainPresenter.getNews(type,"",false);
             }
          });
          top.setHasFixedSize(true);
          LinearLayoutManager layoutManager1 = new LinearLayoutManager(mactivity);
          layoutManager1.setOrientation(LinearLayout.HORIZONTAL);
          top.addItemDecoration(new TypeItemsDecoration());
          top.setLayoutManager(layoutManager1);
          top.setNestedScrollingEnabled(true);
          top.setAdapter(typeDataAdapter);
          typeDataAdapter.setData(TypeDataAdapter.types);

          //新闻
          newsAdapter = new NewsAdapter();
          newsAdapter.setLoadListener(new NewsAdapter.LoadListener() {
              @Override
              public void onLoadMore(NewItem lastNewItem) {
                  String rowKey = lastNewItem.getRowKey();
                  String type = newsAdapter.getType();
                  mainPresenter.getNews(type,rowKey,true);
              }

              @Override
              public void onClickNews(NewItem newItem) {
                   RxBus.getInstance().send(new toContentViewEvent(newItem));
              }
          });
          myRecycleView.setHasFixedSize(true);
          LinearLayoutManager layoutManager = new LinearLayoutManager(mactivity);

          layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
          myRecycleView.addItemDecoration(new NewsItemDecoration());
          myRecycleView.setLayoutManager(layoutManager);
          myRecycleView.setNestedScrollingEnabled(true);
          myRecycleView.setAdapter(newsAdapter);

          siteItemAdapter = new SiteItemAdapter();
          siteRview.setHasFixedSize(true);
          final MyGridLayoutManager gridLayoutManager = new MyGridLayoutManager(mactivity,5);
          gridLayoutManager.setAutoMeasureEnabled(true);
          siteRview.addItemDecoration(new SiteItemDecoration());
          siteRview.setLayoutManager(gridLayoutManager);
          siteRview.setNestedScrollingEnabled(true);
          siteRview.setAdapter(siteItemAdapter);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        typeDataAdapter.notifyDataSetChanged();
        if(mainPresenter != null){
            mainPresenter.getNews("toutiao","",false);
            mainPresenter.getHeadSites();
        }
    }

    //加载更多
    private void loadMore(){
//        if(!loadFooter.isShown()){
//            loadFooter.setVisibility(View.VISIBLE);
//        }
        Integer page = fgoodDataAdapter.getCurrentPage();
        Long cateId = fgoodDataAdapter.getCateId();
        mainPresenter.getFGoods(cateId,page+1,false);
    }

    //recycleview 返回顶部
    private void backToCommonPosi(){
        int scrollY = myScrollingView.getScrollY();
        if(scrollY > topAreaHeight){
             myScrollingView.smoothScrollBy(0,topAreaHeight-scrollY);
        }
    }

    private void reload(){
        Long cateId = fgoodDataAdapter.getCateId();
        mainPresenter.getFGoods(cateId,1,true);
    }

    private void HeaderTranslate(float distance) {
        topImageView.setTranslationY(-distance);
        topImageView.setTranslationY(distance/2);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mainPresenter != null){
            mainPresenter.onDestroy();
        }
    }

    public static MainFragment newInstance() {
        
        Bundle args = new Bundle();
        
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //----------------------------------view 回调 ----------------------------------------------------------

    @Override
    public void setupFgoodsView(Integer page, Long cateId, List<FGoodItem> goodItems, boolean isRefresh) {
        if(isRefresh){
             fgoodDataAdapter.setData(goodItems);
         }else{
             fgoodDataAdapter.appendDataList(goodItems);
         }
        fgoodDataAdapter.notifyDataSetChanged();
        fgoodDataAdapter.setCurrentPage(page);
        fgoodDataAdapter.setCateId(cateId);
//        Observable.timer(1000, TimeUnit.MILLISECONDS)
//                .compose(this.<Long>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
//                .subscribeOn(Schedulers.computation())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<Long>() {
//                    @Override
//                    public void call(Long aLong) {
//                        if(loadFooter.isShown()){
//                            loadFooter.setVisibility(View.GONE);
//                        }
//                    }
//                });
     }

    @Override
    public void setupNewsView(List<NewItem> newItems,String type,boolean isNext, boolean isSucc) {
        newsAdapter.setType(type);
        if(isSucc){
            if(!isNext){
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        backToCommonPosi();
                    }
                },600);
                newsAdapter.setData(newItems);
                newsAdapter.notifyDataSetChanged();
            }else{
                newsAdapter.appendDataList(newItems);
                newsAdapter.notifyDataSetChanged();
            }
        }else{
            Toast.makeText(MyApplication.getContext(),"网速不给力,请稍候",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setupHeadSites(List<SiteItem> siteItems, boolean isSucc) {
         if(isSucc){
              siteItemAdapter.setData(siteItems);
              siteItemAdapter.notifyDataSetChanged();
         }else {

         }
    }


  //-------------------------------------总线事件---------------------------------------------------------
   public class toContentViewEvent extends RxBus.BusEvent{

      private NewItem newItem;

      public toContentViewEvent(NewItem newItem) {
          this.newItem = newItem;
      }

      public NewItem getNewItem() {
          return newItem;
      }

      public void setNewItem(NewItem newItem) {
          this.newItem = newItem;
      }
   }
}
