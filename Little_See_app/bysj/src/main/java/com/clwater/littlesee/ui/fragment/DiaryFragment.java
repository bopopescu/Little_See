package com.clwater.littlesee.ui.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;


import com.clwater.littlesee.R;
import com.clwater.littlesee.eventbus.EventBus_Network;
import com.clwater.littlesee.eventbus.EventBus_Network_Main;
import com.clwater.littlesee.ui.activity.ChooseItemActivity;
import com.clwater.littlesee.ui.activity.TextInfoActivity;
import com.clwater.littlesee.ui.adapter.NormalRecyclerViewAdapter;
import com.clwater.littlesee.utils.Analysis;
import com.clwater.littlesee.utils.Bean.DiaryBean;
import com.clwater.littlesee.utils.OkHttpUtils;
import com.clwater.littlesee.utils.SPHelper;
import com.clwater.littlesee.weight.ItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yszsyf on 17/2/26.
 */

//每日优选
public class DiaryFragment extends Fragment {
    @BindView(R.id.recylist_diarylist)
    RecyclerView recycleListView;

    @BindView(R.id.imageview_list_returntop)
    ImageView imageview_list_returntop;

    List<DiaryBean.DateBean> _DiaryList = new ArrayList<DiaryBean.DateBean>();

    private String _result;

    private int _baseLastItem;
    private boolean _baseLastItemStatu = true;

    public static boolean _chageReturnIconStatu = true;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_diary, container, false);
        ButterKnife.bind(this , view);
        EventBus.getDefault().register(this);

        checkIndexClass();


        Intent webintent = new Intent(this.getActivity() , TextInfoActivity.class);
        //startActivity(webintent);

        getDataFromServer();

        return view;
    }



    private void checkIndexClass() {
        if (SPHelper.getStringValue(getActivity() , "diary_class").isEmpty()){
            Intent intent = new Intent(this.getActivity() , ChooseItemActivity.class);
            // startActivity(intent);
        }

    }



    private void initList() {
        recycleListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycleListView.setAdapter(new NormalRecyclerViewAdapter(getActivity() , _DiaryList));
        recycleListView.addItemDecoration(new ItemDecoration(getActivity()));
        recycleListView.addOnScrollListener(new DiaryListOnScroll());

    }

    @OnClick(R.id.imageview_list_returntop)
    public void imageview_list_returntop_onclick(){
        recycleListView.smoothScrollToPosition(1);
    }

    private void getDataFromServer() {
        EventBus.getDefault().post(new EventBus_Network("diary"));

    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEventbusNetwork(EventBus_Network e){
        String url = "http://192.168.1.104:9007/diary?indexclass=('知乎日报','好奇心日报')";
        _result = OkHttpUtils.okhttp_get(url);

        //Log.d("gzb" , "_result : " + _result);
        _DiaryList = Analysis.AnalysisDiary(_result);

        for( DiaryBean.DateBean _dairy :_DiaryList){
            //Log.d("gzb" , _dairy.getTitle());
        }

        EventBus.getDefault().post(new EventBus_Network_Main("diary"));

    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventbusNetworkinMain(EventBus_Network_Main e){
        initList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }



    class DiaryListOnScroll extends RecyclerView.OnScrollListener{
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                int lastItemPosition = linearManager.findLastVisibleItemPosition();
                int firstItemPosition = linearManager.findFirstVisibleItemPosition();

                if (_baseLastItemStatu){
                    _baseLastItem = lastItemPosition;
                    _baseLastItemStatu = false;
                }

                if (firstItemPosition > _baseLastItem){
                    chageReturnIcon(true);

                }else {
                    chageReturnIcon(false);
                }
            }
        }

    }

    private void chageReturnIcon(boolean b) {
        if (b) {
            imageview_list_returntop.setVisibility(View.VISIBLE);
        }else {
            imageview_list_returntop.setVisibility(View.GONE);
        }
    }


}



