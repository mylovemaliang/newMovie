package cn.fuyoushuo.vipmovie.view.flagment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.lazylibrary.util.Colors;
import com.github.lazylibrary.util.DateUtil;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.trello.rxlifecycle.FragmentEvent;
import com.trello.rxlifecycle.components.support.RxDialogFragment;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.fuyoushuo.commonlib.utils.RegexUtils;
import cn.fuyoushuo.commonlib.utils.RxBus;
import cn.fuyoushuo.domain.entity.HistoryItem;
import cn.fuyoushuo.vipmovie.MyApplication;
import cn.fuyoushuo.vipmovie.R;
import cn.fuyoushuo.vipmovie.presenter.impl.SearchPresenter;
import cn.fuyoushuo.vipmovie.view.adapter.SearchHisAdapter;
import cn.fuyoushuo.vipmovie.view.iview.ISearchView;
import cn.fuyoushuo.vipmovie.view.layout.DividerItemDecoration;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by QA on 2017/3/9.
 */

public class SearchDialogFragment extends RxDialogFragment implements ISearchView{

    @Bind(R.id.search_prompt_flagment_cancel_area)
    RelativeLayout backArea;

    @Bind(R.id.serach_prompt_flagment_searchText)
    EditText searchEditText;

    @Bind(R.id.edit_clear_area)
    RelativeLayout editClearArea;

    @Bind(R.id.search_prompt_flagment_search_area)
    RelativeLayout searchButton;

    @Bind(R.id.search_promt_result_rview)
    RecyclerView resultRview;

    SearchPresenter searchPresenter;

    InputMethodManager inputManager;

    SearchHisAdapter searchHisAdapter;

    private String input = "";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.fullScreenDialog);
        searchPresenter = new SearchPresenter(this);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.flagment_search_prompt, container, false);
        ButterKnife.bind(this,inflate);
        return inflate;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        inputManager = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        searchEditText.setFocusable(true);
        searchEditText.requestFocus();
        //延时弹出键盘
        (new Handler()).postDelayed(new Runnable() {
            public void run() {
                InputMethodManager inManager = (InputMethodManager)searchEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        },500);

        searchHisAdapter = new SearchHisAdapter();
        searchHisAdapter.setOnHisClick(new SearchHisAdapter.OnHisClick() {
            @Override
            public void onClick(View view, HistoryItem typeItem) {
                // TODO: 2017/3/10
            }
        });
        resultRview.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        resultRview.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));
        resultRview.setLayoutManager(layoutManager);
        //resultRview.setNestedScrollingEnabled(true);
        resultRview.setAdapter(searchHisAdapter);

        RxTextView.textChanges(searchEditText)
                .debounce(150, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<CharSequence>bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(new Subscriber<CharSequence>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        //todo
                    }

                    @Override
                    public void onNext(CharSequence charSequence) {
                        String q = charSequence.toString();
                        input = q;
                        if(TextUtils.isEmpty(q)){
                            searchPresenter.getAllHistory();
                        }else{
                            searchPresenter.searchKeyWord(q);
                        }
                    }
                });

        searchEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    //保存当前搜索词
                    /*隐藏软键盘*/
                    if (inputManager.isActive()) {
                        inputManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
                    }
                    if(TextUtils.isEmpty(input)){
                        Toast.makeText(MyApplication.getContext(),"搜索词不能为空",Toast.LENGTH_SHORT).show();
                        return false;
                    }else{
                        HistoryItem historyItem = handlerAddSearch();
                        RxBus.getInstance().send(new toContentPageFromSearchEvent(historyItem));
                        dismissAllowingStateLoss();
                        // TODO: 2017/3/10
                    }
                }
//                if(keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN){
//                    //解决键盘删除的BUG
//                    if(!TextUtils.isEmpty(seartchPo.getQ())){
//                        int index = searchText.getSelectionStart();
//                        Editable editable = searchText.getText();
//                        editable.delete(index-1, index);
//                    }
//                }
                return false;
            }
        });
    }

    //处理搜索
    private HistoryItem handlerAddSearch(){
        boolean isUrl = false;
        String result = input;
        final HistoryItem historyItem = new HistoryItem();
        if(!result.startsWith("http://") && !result.startsWith("https://")){
            result = "http://"+result;
        }
        isUrl = RegexUtils.isURL(result);
        if(isUrl){
            historyItem.setHistoryUrl(result);
            historyItem.setHistoryType(1);
            historyItem.setHistoryTitle(result);
            historyItem.setCreateTime(new Date());
        }else{
            historyItem.setHistoryType(2);
            historyItem.setHistoryTitle(input);
            historyItem.setCreateTime(new Date());
        }
        if(searchPresenter != null){
            searchPresenter.addHistory(historyItem, new SearchPresenter.addHistoryCallback() {
                @Override
                public void onAddCallBack(Long id, Boolean isOk) {
                       historyItem.setId(id);
                }
            });
        }
        return historyItem;
    }

    @Override
    public void onStart() {
        super.onStart();
        //当页面打开,获取所有的历史
        searchPresenter.getAllHistory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        searchPresenter.onDestroy();
    }

    public static SearchDialogFragment newInstance() {
        
        Bundle args = new Bundle();
        
        SearchDialogFragment fragment = new SearchDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //-------------------------------------------view层回调------------------------------------------------------------------

    @Override
    public void setHistoryItems(List<HistoryItem> result, boolean isOk) {
        searchHisAdapter.setData(result);
        searchHisAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDelHistoryItem(boolean isOk) {
        // TODO: 2017/3/10  
    }

    @Override
    public void onAddHistoryItem(boolean isOk) {
        // TODO: 2017/3/10
    }

    @Override
    public void setHistorySearchItems(List<HistoryItem> result, boolean isOk) {
        searchHisAdapter.setData(result);
        searchHisAdapter.notifyDataSetChanged();
    }

    //---------------------------------总线事件定义-------------------------------------------
    public class toContentPageFromSearchEvent extends RxBus.BusEvent{

         private HistoryItem historyItem;

        public toContentPageFromSearchEvent(HistoryItem historyItem) {
            this.historyItem = historyItem;
        }

        public HistoryItem getHistoryItem() {
            return historyItem;
        }

        public void setHistoryItem(HistoryItem historyItem) {
            this.historyItem = historyItem;
        }
    }
}
