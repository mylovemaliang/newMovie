package cn.fuyoushuo.vipmovie.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.fuyoushuo.domain.entity.NewType;
import cn.fuyoushuo.vipmovie.MyApplication;
import cn.fuyoushuo.vipmovie.R;
import rx.functions.Action1;

/**
 * Created by QA on 2016/6/27.
 */
public class TypeDataAdapter extends BaseListAdapter<NewType>{

    private OnTypeClick onTypeClick;

    //当前所在的位置
    private int currentPosition = 0;

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public void setOnCateClick(OnTypeClick onCateClick){
        this.onTypeClick = onCateClick;
    }

    public static List<NewType> types = new ArrayList<NewType>();

    static {
         types.add(new NewType("toutiao","头条").bindRed(true));
         types.add(new NewType("shehui","社会"));
         types.add(new NewType("guonei","国内"));
         types.add(new NewType("guoji","国际"));
         types.add(new NewType("yule","娱乐"));
         types.add(new NewType("keji","科技"));
         types.add(new NewType("junshi","军事"));
         types.add(new NewType("tiyu","体育"));
         types.add(new NewType("shishang","时尚"));
         types.add(new NewType("caijing","财经"));
         types.add(new NewType("youxi","游戏"));
         types.add(new NewType("qiche","汽车"));
         types.add(new NewType("xiaohua","笑话"));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.top_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        super.onBindViewHolder(holder,position);
        final ItemViewHolder currentHolder = (ItemViewHolder) holder;
        final NewType item = getItem(position);
        RxView.clicks(currentHolder.itemView).throttleFirst(1000,TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                onTypeClick.onClick(currentHolder.itemView, item, currentPosition);
                currentPosition = position;
            }
        });
        currentHolder.cateName.setText(item.getTypeName());
        if(item.isRed()){
            currentHolder.cateName.setTextColor(MyApplication.getContext().getResources().getColor(R.color.module_11));
        }else{
            currentHolder.cateName.setTextColor(MyApplication.getContext().getResources().getColor(R.color.darkBackground));
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{
        @Bind(R.id.top_recycle_item_text) public TextView cateName;

        public ItemViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(ItemViewHolder.this,itemView);
        }
    }

    public interface OnTypeClick {
        void onClick(View view, NewType typeItem, int lastPosition);
    }

}
