package com.agriculture.farmer.ui.panel;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import androidx.appcompat.app.AppCompatActivity;

import com.agriculture.farmer.R;
import com.agriculture.my_library.TransTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class EpidemicActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private EditText search_et;
    private Button search_btn;
    private ProgressDialog mdialog;
    private ArrayList<Map<String, Object>> arraylist;
    private String data_url;
    private JSONArray jsonarray;
    private String[][] detailarray;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_main);
        setupviewcomponent();
    }

    private void setupviewcomponent() {
        search_et=(EditText)findViewById(R.id.epi_e001);
        search_btn = (Button)findViewById(R.id.epi_b001);
        search_btn.setOnClickListener(this);
        //設定edittext的即時監聽
        search_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                write_data();//即時監聽寫入資料
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        listView = (ListView) findViewById(R.id.m_listview);
        listView.setOnItemClickListener(this);//listview設定監聽
        mdialog = new ProgressDialog(this); //設置剛進去時的dialog
        mdialog.setTitle("疫情訊息加載中..");
        mdialog.setCancelable(false);
        mdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mdialog.show();
        load_data();
        setTitle("疫情訊息");
    }

    @Override
    public void onClick(View v) {
        write_data();//即時監聽寫入資料
    }
    private void load_data() {//TransTask()抓取資料的方法
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    arraylist = new ArrayList<Map<String,Object>>();
                    //第一層的jsonarray(讀取資料，第一次進入這個頁面才要讀取)--------------------------
                    data_url = new TransTask().execute("https://data.coa.gov.tw/Service/OpenData/FromM/PestNoticeData.aspx").get();
                    jsonarray = new JSONArray(data_url);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            write_data(); //抓取完之後寫入所有資料到listview
                            mdialog.cancel();

                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }
    private void write_data() {
        try {
            detailarray = new String[jsonarray.length()][5];
            int num = 0;
            for (int i = 0; i < jsonarray.length(); i++){
                JSONObject pest_jsonOb = jsonarray.getJSONObject(i);
                String user_input = search_et.getText().toString();//用戶輸入的
                String key_name1 = pest_jsonOb.getString("植物品項");
                String key_name2 = pest_jsonOb.getString("發布縣市");
                if(key_name1.indexOf(user_input)==-1&&key_name2.indexOf(user_input)==-1){
                    //用戶輸入的字不在相應的作物名稱當中的話就繼續循環
                    continue;
                }
                String date = pest_jsonOb.getString("發布日期");
                String title = pest_jsonOb.getString("主旨");
                String epi = pest_jsonOb.getString("植物品項");
                String main_string = date+ "\n\n" + title + "\n\n"+ epi+ "\n";
                //title date city content suggest
                String city = pest_jsonOb.getString("發布縣市");
                String content = pest_jsonOb.getString("疫情內容");
                String suggest = pest_jsonOb.getString("建議用藥");
                detailarray[num][0]=title;
                detailarray[num][1]=date;
                detailarray[num][2]=city;
                detailarray[num][3]=content;
                detailarray[num][4]=suggest;
                num++;
                //設定item
                //設定item，裝入arraylist
                Map<String, Object> item = new HashMap<String, Object>();
                item.put("main_string",main_string);
                arraylist.add(item);
            }
            m_setadapter();//arraylist設定完之後就設置adapter
        }catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void m_setadapter() { //重新設定adapter（也就是重新設定listview）
        SimpleAdapter pest_adapter = new SimpleAdapter(getApplicationContext(), arraylist,
                R.layout.epidemic_item, new String[]{"main_string"}, new int[]{R.id.epiitem_t001});
        listView.setAdapter(pest_adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent it = new Intent();
        it.setClass(this, EpidetailActivity.class);

        Bundle bd = new Bundle();
        //title date city content suggest
        bd.putStringArray("title", detailarray[position]);
        it.putExtras(bd);
        startActivity(it);
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
