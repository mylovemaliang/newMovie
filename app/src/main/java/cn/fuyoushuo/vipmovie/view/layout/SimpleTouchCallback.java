package cn.fuyoushuo.vipmovie.view.layout;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import static android.support.v7.widget.helper.ItemTouchHelper.DOWN;
import static android.support.v7.widget.helper.ItemTouchHelper.UP;

/**
 * Created by QA on 2017/2/28.
 */

public class SimpleTouchCallback extends ItemTouchHelper.Callback {


    private onMoveListener onMoveListener;

    public SimpleTouchCallback(SimpleTouchCallback.onMoveListener onMoveListener) {
        this.onMoveListener = onMoveListener;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

        if(recyclerView.getLayoutManager() instanceof LinearLayoutManager){
             final int swipeFlags = UP | DOWN;
             return makeMovementFlags(0,swipeFlags);
        }else{
             return 0;
        }
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
         if(direction == UP){
             onMoveListener.onItemDismiss(viewHolder.getAdapterPosition());
         }
    }

    public interface onMoveListener{

         void onItemDismiss(int position);

    }
}
