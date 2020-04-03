package com.agriculture.farmer.ui.panel;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

public class AgrActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ListView listView;
    private ProgressDialog mdialog;
    private ArrayList<Map<String, Object>> arraylist;
    private String data_url;
    private JSONArray jsonarray;
    private String[][] detailarray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fishitem_main);
        setupviewcomponent();
    }

    private void setupviewcomponent() {

        listView = (ListView) findViewById(R.id.m_listview);
        listView.setOnItemClickListener(this);//listview設定監聽
        mdialog = new ProgressDialog(this); //設置剛進去時的dialog
        mdialog.setTitle("農業新聞加載中..");
        mdialog.setCancelable(false);
        mdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mdialog.show();
        load_data();
        setTitle("農業新聞");
    }

    private void load_data() {//TransTask()抓取資料的方法
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    arraylist = new ArrayList<Map<String,Object>>();
                    //第一層的jsonarray(讀取資料，第一次進入這個頁面才要讀取)--------------------------
                    data_url = new TransTask().execute("https://data.coa.gov.tw/Service/OpenData/Agriculturalnews_agriRss.aspx").get();
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
            detailarray = new String[jsonarray.length()][4];
            int num = 0;
            for (int i = 0; i < jsonarray.length(); i++){
                JSONObject pest_jsonOb = jsonarray.getJSONObject(i);

                String date = pest_jsonOb.getString("cDate");
                String title = pest_jsonOb.getString("title");
                String main_string = date+ "\n\n" + "標題："+ title;
                //title date city content suggest
                String content = pest_jsonOb.getString("description");
                String link =  pest_jsonOb.getString("link");
                detailarray[num][0]=title;
                detailarray[num][1]=date;
                detailarray[num][2]=content;
                detailarray[num][3]=link;
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
        it.setClass(this, AgrdetailActivity.class);

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
