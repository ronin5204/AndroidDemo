package com.agriculture.farmer.ui.home;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.agriculture.farmer.R;
import com.agriculture.farmer.bean.Collegedata;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CollegeActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private LinearLayoutManager layoutManager;
    private CollegeLinearAdapter mLinearAdapter;
    private Handler mhandler = new Handler();
    private ProgressDialog mdialog;
    private List<Collegedata> collegedata;
    private List<Collegedata> datas;
    private Spinner mSpinner;
    private String now_spinner = "請選擇";
    private TextView mContenttxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.college_main);
        mdialog = new ProgressDialog(this); //設置剛進去時的dialog
        mdialog.setTitle("課程加載中..");
        mdialog.setCancelable(false);
        mdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mdialog.show();
        setupviewcomponent();
    }

    private void setupviewcomponent() {
        mRecyclerView = findViewById(R.id.m_recyclerview);
        //避免重新計算大小 當確定item改變不會變化recyclerview的寬高的時候
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false));
        layoutManager =(LinearLayoutManager)mRecyclerView.getLayoutManager();
        mContenttxt = findViewById(R.id.coll_content);
        okhttp();
        mSpinner = findViewById(R.id.coll_spinner);
        setTitle("農民學院");
    }
    private void setdata() {
        datas = new ArrayList<Collegedata>();

        for(int i = 0 ; i < collegedata.size() ; i++){
            if(now_spinner.equals("請選擇")){
                datas = collegedata;
                break;
            }else if(now_spinner.equals(collegedata.get(i).getStatus())){
                datas.add(collegedata.get(i));
            }
        }

        mLinearAdapter = new CollegeLinearAdapter(this,datas);
        mLinearAdapter.setOnClicklistener(new CollegeLinearAdapter.OnitemClicklistener() {
            //設定監聽 可以無視
            @Override
            public void onclick(int pos) {
                Log.d("aa", "onclick: "+pos);
                Toast.makeText(getApplicationContext(),pos+"",Toast.LENGTH_SHORT).show();
                String str = "聯絡資訊："+datas.get(pos).getConnection()+"\n"+"課程名稱："+datas.get(pos).getName()+"\n"+"課程日期："+datas.get(pos).getDate()+"\n"+
                        "課程費用："+datas.get(pos).getPrice()+"\n"+"報名時間："+datas.get(pos).getSigndate()+"\n"+
                        "類別："+datas.get(pos).getCategary()+"\t"+datas.get(pos).getCategary2()+"\t"+datas.get(pos).getCategary3()+"\t"+
                        "名額："+datas.get(pos).getQuota()+"\t"+"報名人數："+datas.get(pos).getSignnum()+"\n"+
                        "課程內容：\n"+datas.get(pos).getContent()+"\n";

                mContenttxt.setText(str);
            }
        });
        mRecyclerView.setAdapter(mLinearAdapter);
    }

    private void setspinner() {
        HashSet<String> m_hs= new HashSet<String>();
        for(int i = 0 ; i < collegedata.size() ; i++){
            m_hs.add(collegedata.get(i).getStatus());
        }
        ArrayAdapter<String> ad= new ArrayAdapter<String>
                (this, android.R.layout.simple_dropdown_item_1line);
        ad.add("請選擇");
        Iterator it = m_hs.iterator();
        while(it.hasNext()){
            ad.add(it.next().toString());
        }
        mSpinner.setAdapter(ad);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                now_spinner = parent.getSelectedItem().toString();
                setdata();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void okhttp() {
        OkHttpClient client = new OkHttpClient();
        String url = "https://data.coa.gov.tw/Service/OpenData/FromM/FarmerAcademyData.aspx";
        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback(){

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String myResponse = response.body().string();
                Log.d("aa", "onResponse: "+myResponse);
                CollegeActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        TypeToken typeToken = new TypeToken<List<Collegedata>>() {};
                        Type type = typeToken.getType();
                        collegedata = gson.fromJson(myResponse, type);
                        datas = collegedata;
                        setspinner();
                        setdata();
                        mdialog.cancel();
                        Log.d("test", collegedata.get(0).getDate());

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
