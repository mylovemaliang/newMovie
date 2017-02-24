package cn.fuyoushuo.vipmovie.view.flagment;

import android.annotation.TargetApi;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.zhy.android.percent.support.PercentRelativeLayout;

import java.util.List;

import butterknife.Bind;
import cn.fuyoushuo.commonlib.utils.CommonUtils;
import cn.fuyoushuo.domain.entity.FGoodItem;
import cn.fuyoushuo.vipmovie.R;
import cn.fuyoushuo.vipmovie.presenter.impl.MainPresenter;
import cn.fuyoushuo.vipmovie.view.activity.BaseActivity;
import cn.fuyoushuo.vipmovie.view.adapter.FgoodDataAdapter;
import cn.fuyoushuo.vipmovie.view.iview.IMainView;
import cn.fuyoushuo.vipmovie.view.layout.MyGridLayoutManager;

/**
 * Created by QA on 2017/2/22.
 */

public class MainFragment extends BaseFragment implements IMainView{


    @Bind(R.id.scrollView)
    NestedScrollView myScrollingView;

    @Bind(R.id.topImage)
    ImageView topImageView;

    @Bind(R.id.title)
    TextView title;

    @Bind(R.id.top)
    TextView top;

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

    @Bind(R.id.loading_footer)
    RelativeLayout loadFooter;

    private float titleAreaHeight;

    private float topAreaHeight;

    private MainPresenter mainPresenter;

    FgoodDataAdapter fgoodDataAdapter;

    Handler handler = new Handler();

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

    @TargetApi(Build.VERSION_CODES.M)
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
                        titleEssContainer.setBackgroundColor(getResources().getColor(R.color.module_15));
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
                View childAtFirst = v.getChildAt(0);
                if(childAtFirst.getMeasuredHeight() <= scrollY + v.getHeight()){
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadMore();
                        }
                    },500);

                }
            }
        });

        fgoodDataAdapter = new FgoodDataAdapter();
        myRecycleView.setHasFixedSize(true);
        //mainBottomRView.addItemDecoration(new GoodItemsDecoration(10,5));
        final MyGridLayoutManager gridLayoutManager = new MyGridLayoutManager(mactivity, 2);
        gridLayoutManager.setSpeedFast();
        //gridLayoutManager.setSpeedSlow();
        gridLayoutManager.setAutoMeasureEnabled(true);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        gridLayoutManager.setSmoothScrollbarEnabled(true);
        gridLayoutManager.setAutoMeasureEnabled(true);
        myRecycleView.setLayoutManager(gridLayoutManager);
        myRecycleView.setNestedScrollingEnabled(true);
        fgoodDataAdapter.setOnLoad(new FgoodDataAdapter.OnLoad() {
            @Override
            public void onLoadImage(SimpleDraweeView view, FGoodItem goodItem) {
                int mScreenWidth = getActivity().getWindowManager().getDefaultDisplay().getWidth();
                int intHundred = CommonUtils.getIntHundred(mScreenWidth/2);
                if(intHundred > 800){
                    intHundred = 800;
                }
                if(!BaseActivity.isTablet(mactivity)){
                    intHundred = 300;
                }
                String imgurl = goodItem.getImageUrl();
                imgurl = imgurl.replaceFirst("_[1-9][0-9]{0,2}x[1-9][0-9]{0,2}\\.jpg","");
                imgurl = imgurl+ "_"+intHundred+"x"+intHundred+".jpg";
                view.setAspectRatio(1.0F);
                view.setImageURI(Uri.parse(imgurl));
            }

            @Override
            public void onGoodItemClick(View clickView, FGoodItem goodItem) {

            }
        });
        myRecycleView.setAdapter(fgoodDataAdapter);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(mainPresenter != null){
            mainPresenter.getFGoods(0l,1,false);
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
}
