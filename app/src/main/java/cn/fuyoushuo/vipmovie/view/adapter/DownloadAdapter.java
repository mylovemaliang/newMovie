package cn.fuyoushuo.vipmovie.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import cn.fuyoushuo.domain.entity.DownloadTask;
import cn.fuyoushuo.vipmovie.R;
import com.daimajia.numberprogressbar.NumberProgressBar;
import java.text.SimpleDateFormat;

/**
 * Created by QA on 2017/3/27.
 */

public class DownloadAdapter extends BaseListAdapter<DownloadTask>{

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private DownloadCallback downloadCallback;

    public void setDownloadCallback(DownloadCallback downloadCallback) {
        this.downloadCallback = downloadCallback;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_download_view, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        final ItemViewHolder currentHolder = (ItemViewHolder) holder;
        DownloadTask item = getItem(position);
        String title = item.getTitle();
        int taskState = item.getTaskState();
        float currentMbs = item.getCurrentMbs();
        float totalMbs = item.getTotalMbs();
        if(!TextUtils.isEmpty(title)){
            currentHolder.fileName.setText(title);
        }
        if(downloadCallback != null){
            downloadCallback.onLoadProgress(currentHolder.downloadProgressBar,currentHolder.downloadInfo,item);
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.download_info_text)
        TextView downloadInfo;

        @Bind(R.id.download_name_text)
        TextView fileName;

        @Bind(R.id.download_progressBar) NumberProgressBar downloadProgressBar;

        public ItemViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface DownloadCallback{
        void onLoadProgress(NumberProgressBar progressBar,TextView downloadInfoText,DownloadTask downloadTask);

    }
}
