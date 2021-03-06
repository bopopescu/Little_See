package com.clwater.littlesee.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.clwater.littlesee.R;
import com.clwater.littlesee.eventbus.EventBus_RunInBack;
import com.clwater.littlesee.eventbus.EventBus_RunInFront;
import com.clwater.littlesee.utils.Analysis;
import com.clwater.littlesee.utils.OkHttpUtils;
import com.clwater.littlesee.utils.SPHelper;
import com.clwater.littlesee.utils.WebContent;
import com.google.android.flexbox.FlexboxLayout;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yszsyf on 17/3/16.
 */

public class ChooseItemActivity extends AppCompatActivity {

    @BindView(R.id.flexboxlayout_chooseitem_activity) FlexboxLayout flexboxLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.textview_choose_enter) TextView textview_choose_enter;

    private AppCompatActivity activity;
    private String[] result;
    private boolean[] _resultStatu;
    private int chooseListCount = -1;
    private TextView[] _textviewitem;

    private Snackbar snackbar;

    private int _index ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chooseitem);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        activity = this;

        Intent intent = getIntent();
        String index =  intent.getStringExtra("index");

        if (index.equals("diary")) {
            _index = 0;
            showDialogPor();
            initToolBar("优选话题");
            init();
        }else if (index.equals("news")){
            _index = 1;
          //  showDialogPor();
            initToolBar("即刻类别");
            result = new String[]{"中国新闻网"};
            EventBus.getDefault().post(new EventBus_RunInFront("getClassAnalysis"));
        }else if (index.equals("image")){
            _index = 2;
            //  showDialogPor();
            initToolBar("佳景类别");
            result = new String[]{"Bing"};
            EventBus.getDefault().post(new EventBus_RunInFront("getClassAnalysis"));
        }


    }

    private void getDefaultChoose() {
        String r = "";
        if (_index == 0) {
            r = SPHelper.getDiaryclass(activity);
        }else if (_index == 1){
            r = SPHelper.getNewsclass(activity);
        }else if (_index == 2){
            r = SPHelper.getImageclass(activity);
        }

        String[] rr = r.split(",");
        for (int i = 0 ; i < rr.length ; i++){
            //Log.d("gzb" , " " + rr[i]);
            for (int j = i ; j < result.length ; j++){
                if (result[j].equals(rr[i])){
                    _resultStatu[j] = false;
                }else {
                    _resultStatu[j] = true;
                    chooseListCount++;
                }
            }
        }
        upDateChooseList();

    }

    private void upDateChooseList() {
        for (int i = 0 ; i < result.length ; i++){
            upDateChoose(i);
        }
    }

    private void upDateChoose(int index) {
        if (_resultStatu[index]){
            chooseListCount--;
            _textviewitem[index].setBackground(getResources().getDrawable(R.drawable.textview_border));
            _textviewitem[index].setTextColor(Color.parseColor("#000000"));
            _resultStatu[index] = false;
        }else {
            chooseListCount++;
            _textviewitem[index].setBackground(getResources().getDrawable(R.drawable.textview_border_blue));
            _textviewitem[index].setTextColor(Color.parseColor("#ffffff"));
            _resultStatu[index] = true;
        }

        if (chooseListCount > 0){
            textview_choose_enter.setVisibility(View.VISIBLE);
        }else {
            textview_choose_enter.setVisibility(View.GONE);
        }
    }

    @OnClick(R.id.textview_choose_enter)
    public void onclice_textview_choose_enter(){
        saveChooseList();
    }

    private void saveChooseList() {
        String _choose = "";
        for (int i = 0  ;  i < result.length ; i++){
            if (_resultStatu[i]){
                _choose = _choose + result[i] + ",";
            }
        }
        if (_index == 0 ) {
            SPHelper.saveDiaryclass(activity, _choose);
        }else if (_index == 1){
            SPHelper.saveNewsclass(activity, _choose);
        }else if (_index == 2){
            SPHelper.saveImageclass(activity, _choose);
        }

        activity.finish();
    }

    private void showDialogPor() {
        snackbar = Snackbar.with(activity)
                .type(SnackbarType.MULTI_LINE)
                .duration(Snackbar.SnackbarDuration.LENGTH_INDEFINITE)
                .text("loading...");
        SnackbarManager.show(snackbar);
    }

    private void initToolBar(String title) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chooseListCount == 0){
                    showchooseerror(0);
                }else {
                    saveChooseList();
                    activity.finish();
                }
            }
        });
    }

    private void showchooseerror(int i) {
        if (i == 0) {
            SnackbarManager.show(
                    Snackbar.with(activity)
                            .text("你还没有选择关注的栏目"));
        }else {
            saveChooseList();
            activity.finish();
        }
    }

    public void onBackPressed() {
        if (chooseListCount == 0){
            showchooseerror(0);
        }else {
            saveChooseList();
            activity.finish();
        }
    }

    private void init() {
        getDiaryCLassFromServer();

    }

    private void getDiaryCLassFromServer() {
        EventBus.getDefault().post(new EventBus_RunInBack("getDiaryCLassFromServer"));
    }


    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void EventBus_getDiaryCLassFromServer(EventBus_RunInBack e){
        if (e.getTag().equals("getDiaryCLassFromServer")){
            String url = WebContent.ServerAddress +  "/dirayClass";
            //Log.d("gzb" , "url: " + url);
            String _result = OkHttpUtils.okhttp_get(url);
            result = Analysis.AnalysisDiaryClass(_result);
            EventBus.getDefault().post(new EventBus_RunInFront("getClassAnalysis"));
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void EventBus_showgetClassAnalysis(EventBus_RunInFront e){
        if (e.getTag().equals("getClassAnalysis")){
            chooseListCount = 0;
            showDiaryClass();
            getDefaultChoose();
        }
    }

    private void showDiaryClass() {
        _textviewitem = new TextView[result.length];
        _resultStatu = new boolean[result.length];
        for (int i= 0 ; i < result.length ; i++ ) {
            final TextView textView = createBaseFlexItemTextView(result[i]);
            textView.setLayoutParams(createDefaultLayoutParams());
            final int finalI = i;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    upDateChoose(finalI);
                }
            });
            flexboxLayout.addView(textView);
            _resultStatu[i] = false;
            _textviewitem[i] = textView;
        }
        if (_index == 0) {
            snackbar.dismiss();
        }

    }

    private TextView createBaseFlexItemTextView(String classname) {
        TextView textView = new TextView(this);
        textView.setBackground(getResources().getDrawable(R.drawable.textview_border));
        textView.setText(classname);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding( 20 , 10 , 20 , 10 );
        return textView;
    }

    private FlexboxLayout.LayoutParams createDefaultLayoutParams() {
        FlexboxLayout.LayoutParams lp = new FlexboxLayout.LayoutParams(FlexboxLayout.LayoutParams.WRAP_CONTENT , FlexboxLayout.LayoutParams.WRAP_CONTENT );
        lp.order = 1;
        lp.flexGrow = 0;
        lp.flexShrink = 1;
        lp.bottomMargin = 10;
        lp.topMargin = 10;
        lp.rightMargin = 20;
        lp.leftMargin = 20;

        int flexBasisPercent = -1;
        lp.flexBasisPercent = flexBasisPercent == -1 ? -1 : (float) (flexBasisPercent / 100.0);
        return lp;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
