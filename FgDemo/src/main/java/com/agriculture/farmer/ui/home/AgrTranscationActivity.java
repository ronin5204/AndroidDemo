package com.agriculture.farmer.ui.home;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agriculture.farmer.R;
import com.agriculture.farmer.bean.Transcationdata;
import com.agriculture.farmer.layoutmanager.LinearLayoutManagerWithScrollEnd;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AgrTranscationActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView mRecyclerView;
    private LinearLayoutManagerWithScrollEnd layoutManager;
    private List<Transcationdata> Transcationdata;
    private List<Transcationdata> datas;
    private int num =0;
    private AgrLinearAdapter mAgrLinearAdapter;
    private int gotobottom = 0;
    private int footer_show = 0;
    private Handler mhandler = new Handler();
    private ProgressDialog mdialog;
    private EditText mEditSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transcation_agr_main);
        mdialog = new ProgressDialog(this); //設置剛進去時的dialog
        mdialog.setTitle("農業新聞加載中..");
        mdialog.setCancelable(false);
        mdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mdialog.show();
        setupviewcomponent();
    }

    private void setupviewcomponent() {

        findViewById(R.id.tran_b001).setOnClickListener(this);
        mEditSearch = findViewById(R.id.tran_e001);
        mRecyclerView = findViewById(R.id.m_recyclerview);
        //避免重新計算大小 當確定item改變不會變化recyclerview的寬高的時候
        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setLayoutManager(new LinearLayoutManagerWithScrollEnd(this));
        layoutManager =(LinearLayoutManagerWithScrollEnd)mRecyclerView.getLayoutManager();
        okhttp();
        setScrollListener();

        setTitle("農產品交易行情");

    }

    private void setScrollListener() {
        mRecyclerView.addOnScrollListener(m_listener);
    }

    private RecyclerView.OnScrollListener m_listener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            //0是SCROLL_STATE_IDLE（靜止） 1是SCROLL_STATE_DRAGGING（拖曳狀態） 2是SCROLL_STATE_SETTLING（自動滾動）
            super.onScrollStateChanged(recyclerView, newState);
            //arrFeature.size()=6573
            if(newState==2){//自動滾動狀態
//                    mLinearAdapter.setFooter_gone(true);//footer隱藏
            }else if(newState==1){//手指滑動狀態
                mAgrLinearAdapter.setFooter_gone(false);//footer出現
            }else if(gotobottom==0&&newState==0&&footer_show==1){
                //沒到底 但footer顯現出來 且停止滑動 要把footer隱藏的效果
                layoutManager.smoothScrollToPosition(mRecyclerView,null,
                        mAgrLinearAdapter.getItemCount()-2);
                Log.d("aa", footer_show+"11");
                footer_show=0;
            }else if(gotobottom==1&&newState==0){//到底且停止滑動
                setfooterview(mRecyclerView,"加載中..");
                mhandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        setdata(num,num+20);
                        setfooterview(mRecyclerView,"上拉繼續加載...");
                    }
                },1000);

            }

        }
        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            int num2 = ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
            Log.d("aa", "onScrolled: "+num2);
            if(mRecyclerView.canScrollVertically(1)==false){
                Log.d("aa", "onScrolled: 到底了");
                footer_show = 1;//footer漏出來了
                gotobottom = 1;
            }else if(num2>= mAgrLinearAdapter.getItemCount()-2){//拉到了footer但是沒到底部
                footer_show = 1;//footer漏出來了
                gotobottom = 0;
            }else{
                footer_show = 0;
                gotobottom = 0;
            }
        }

};


    private void setdata(int start,int end) {
        num=num+20;
        List<Transcationdata> thisdata;
        if(end<=datas.size()-1){
            thisdata = datas.subList(start,end);
        }else{
            thisdata = datas.subList(start,datas.size()-1);}
        Log.d("test", thisdata.get(0).getDate());
        if(start==0){
            mAgrLinearAdapter = new AgrLinearAdapter(this,thisdata);
            mAgrLinearAdapter.setOnClicklistener(new AgrLinearAdapter.OnitemClicklistener() {
                //設定監聽 可以無視
                @Override
                public void onclick(int pos) {
                    Log.d("aa", "onclick: "+pos);
                    Toast.makeText(getApplicationContext(),pos+"",Toast.LENGTH_SHORT).show();
                }
            });
            mRecyclerView.setAdapter(mAgrLinearAdapter);

        }else{
            mAgrLinearAdapter.adddata(thisdata);
        }
        if(end<=datas.size()-1){
            setfooterview(mRecyclerView,"上拉繼續加載");
            setScrollListener();
        }else{
            mRecyclerView.clearOnScrollListeners();
            setfooterview(null,"");}

    }
    @Override
    public void onClick(View v) {
        String userinput = mEditSearch.getText().toString();
        datas = new ArrayList<>();
        for (int i = 0 ; i < Transcationdata.size() ; i++)
        {
            String total = Transcationdata.get(i).getMarketname()+Transcationdata.get(i).getMarketnum()
                    +Transcationdata.get(i).getCropname()+Transcationdata.get(i).getCropnum();
            if(total.contains(userinput)){
                datas.add(Transcationdata.get(i));
            }
        }
        num = 0;
        setdata(num,num+20);
    }
    private void setfooterview(RecyclerView view,String foottext) {
        //改變footer的function
        if(view!=null){
            View footer = LayoutInflater.from(this).inflate(R.layout.footer_item, view, false);
            mAgrLinearAdapter.setFooterView(footer,foottext);
        }else{
            mAgrLinearAdapter.setFooterView(null,"");
        }
    }
    private void okhttp() {
        OkHttpClient client = new OkHttpClient();
        String url = "https://data.coa.gov.tw/Service/OpenData/FromM/FarmTransData.aspx";
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback(){

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String myResponse = response.body().string();
                Log.d("aa", "onResponse: "+myResponse);
                AgrTranscationActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        TypeToken typeToken = new TypeToken<List<Transcationdata>>() {};
                        Type type = typeToken.getType();
                        Transcationdata = gson.fromJson(myResponse, type);
                        datas = new ArrayList<>(Transcationdata);
//                        Mask userObject = gson.fromJson(myResponse, Mask.class);
//                        arrFeature = userObject.getFeature();
                        setdata(num,num+20);
                        mdialog.cancel();
                        Log.d("test", datas.get(0).getDate());

                    }
                });
            }
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.back, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.show_panel:
                this.finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
