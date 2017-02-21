package cn.fuyoushuo.vipmovie.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhy.android.percent.support.PercentRelativeLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.fuyoushuo.vipmovie.R;
import cn.fuyoushuo.vipmovie.view.layout.MyScrollingView;
import retrofit2.http.HEAD;

public class MainActivity extends AppCompatActivity implements MyScrollingView.ScrollingChangedListener{


    @Bind(R.id.scrollView)
    MyScrollingView myScrollingView;

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

    private float titleAreaHeight;

    private float topAreaHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        myScrollingView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
               titleAreaHeight = titleContainer.getTop();
               topAreaHeight = topContainer.getTop() - titleContainer.getHeight();
               myScrollingView.setScrollingChangedListener(MainActivity.this);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onScroll(int oldy, int dy, boolean isCloseTop) {
         if(!isCloseTop && dy < titleAreaHeight){
             HeaderTranslate(dy);
         }
         if(!isCloseTop && dy > titleAreaHeight){
               HeaderTranslate(titleAreaHeight);
               if(title.getParent() == titleContainer ){
                 titleContainer.removeView(title);
                 titleEssContainer.addView(title);
                 titleEssContainer.setBackgroundColor(getResources().getColor(R.color.module_15));
               }
             if(dy > topAreaHeight){
               if(top.getParent() == topContainer){
                  topContainer.removeView(top);
                  topEssContainer.addView(top);
               }
             }
         }
         else if(isCloseTop && dy < topAreaHeight){
             if(top.getParent() == topEssContainer){
                 topEssContainer.removeView(top);
                 topContainer.addView(top);
             }
             if(dy < titleAreaHeight){
                 HeaderTranslate(dy);
                 if(title.getParent() == titleEssContainer){
                     titleEssContainer.removeView(title);
                     titleEssContainer.setBackgroundColor(getResources().getColor(R.color.transparent));
                     titleContainer.addView(title);
                 }
             }
     }
   }

    private void HeaderTranslate(float distance) {
        topImageView.setTranslationY(-distance);
        topImageView.setTranslationY(distance/2);
    }
}
