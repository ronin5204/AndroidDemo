package com.agriculture.farmer.ui.mask;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.agriculture.farmer.R;
import com.agriculture.my_library.TransTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MaskdataActivity extends AppCompatActivity {

    private ListView listView;
    HashSet<String> HashSet = new HashSet<String>();
    private String[] city;
    private String time_1,data_url,m_city="";
    private ArrayList<Map<String, Object>> mList;
    private ProgressDialog mdialog;
    private TextView time;
    private Spinner s001;
    private boolean First=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mask_main);
        setupviewcomponent();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void setupviewcomponent() {

        s001=(Spinner)findViewById(R.id.spinner);
        time=(TextView)findViewById(R.id.time);

        // 動態調整高度 抓取使用裝置尺寸
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int newscrollheight = displayMetrics.heightPixels * 90 / 100;
        listView = (ListView) findViewById(R.id.listview1);
        listView.getLayoutParams().height = newscrollheight;
        listView.setLayoutParams(listView.getLayoutParams());

        mdialog = new ProgressDialog(this); //設置剛進去時的dialog
        mdialog.setTitle("加載資料中...");
        mdialog.setCancelable(false);
        mdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mdialog.show();
        allfunction();

    }

    private Spinner.OnItemSelectedListener spon = new Spinner.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(First){
                First=false;
            }else{
                m_city = city[position];
                mdialog.show();
                searchfunction2();
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


    private void allfunction() {  //顯示出全部
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    data_url = new TransTask().execute("https://raw.githubusercontent.com/kiang/pharmacies/master/json/points.json").get();
                    mList=new ArrayList<Map<String,Object>>();
                    JSONObject jsonobj1 = new JSONObject(data_url);
                    String aa = jsonobj1.getString("features");
                    JSONArray jsonarray1 = new JSONArray(aa);
                    for (int i = 0; i < jsonarray1.length(); i++){
                        JSONObject jsonobj2 = jsonarray1.getJSONObject(i);

                        String bb = jsonobj2.getString("properties");
                        JSONObject jsonobj3 = new JSONObject(bb);


                        String county = jsonobj3.getString("county");
                        String town = jsonobj3.getString("town");
                        String name = jsonobj3.getString("name");
                        String phone = jsonobj3.getString("phone");
                        String address = jsonobj3.getString("address");
                        String mask_adult =jsonobj3.getString("mask_adult");
                        String mask_child =jsonobj3.getString("mask_child");
                        String updated = jsonobj3.getString("updated");
                        HashSet.add(county);

                        Map<String, Object> item = new HashMap<String, Object>();

                        item.put("county", county);
                        item.put("town", town);
                        item.put("name", name);
                        item.put("phone", phone);
                        item.put("address", address);
                        item.put("mask_adult", mask_adult);
                        item.put("mask_child", mask_child);
                        time_1 = updated;


                        mList.add(item);
                    }
                    runOnUiThread(new Runnable() {//ui做改變，顯示listview
                        @Override
                        public void run() {
                            ArrayAdapter<String> adap = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item);

                            Object[] aaa=new Object[HashSet.size()+1]; //抓取城市
                            city = new String[HashSet.size()+1];
                            int i=1;
                            aaa[0]="全部縣市";
                            city[0]="請選擇縣市";
                            adap.add(city[0]);

                            Iterator it = HashSet.iterator();
                            while(it.hasNext()){
                                aaa[i]=it.next();
                                city[i] = aaa[i].toString();
                                adap.add(city[i]);
                                i++;
                            }
                            s001.setAdapter(adap);
                            s001.setOnItemSelectedListener(spon);
                            mdialog.cancel();
                            SimpleAdapter adapter1 = new SimpleAdapter(getApplicationContext(), mList, R.layout.mast_item, new String[]{"county","town","name","mask_adult","mask_child","phone","address"},
                                    new int[]{R.id.t001,R.id.t002,R.id.t003,R.id.t004,R.id.t005,R.id.t006,R.id.t007});
                            time.setText("更新時間："+time_1);
                            //設定list內的PM2.5的文字顏色和背景顏色
                            adapter1.setViewBinder(new SimpleAdapter.ViewBinder() {
                                @Override
                                public boolean setViewValue(View view, Object data, String textRepresentation) {

                                    if (view.getId() == R.id.t004) {
                                        TextView adult_m = (TextView) view;
                                        adult_m.setText(data.toString());
                                        if (checkIfDataIsInt(data.toString())) {
                                            int adult = Integer.parseInt(data.toString());
                                            //-判斷細懸浮微粒(PM2.5)指標對照
                                            if (adult == 0) {
                                                adult_m.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.Red));
                                                adult_m.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.Yellow));
                                            } else if(adult > 0 &&adult < 5) {
                                                adult_m.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.Black));
                                                adult_m.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.Yellow));
                                            } else if(adult >= 5 &&adult < 20) {
                                                adult_m.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.Black));
                                                adult_m.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.Aqua));
                                            } else if (adult >= 20 &&adult < 60) {
                                                adult_m.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.Black));
                                                adult_m.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.Olive));
                                            } else if (adult >= 60 && adult <= 150) {
                                                adult_m.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.Black));
                                                adult_m.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.Lime));
                                            } else if (adult > 150) {
                                                adult_m.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.Black));
                                                adult_m.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.Silver));
                                            } else {
                                                adult_m.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.Black));
                                                adult_m.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.White));
                                            }
                                        }

                                    }
                                    if (view.getId() == R.id.t005) {
                                        TextView child_m = (TextView) view;
                                        child_m.setText(data.toString());
                                        if (checkIfDataIsInt(data.toString())) {
                                            int child = Integer.parseInt(data.toString());
                                            //-判斷細懸浮微粒(PM2.5)指標對照
                                            if (child == 0) {
                                                child_m.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.Red));
                                                child_m.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.Yellow));
                                            } else if(child > 0 &&child < 5) {
                                                child_m.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.Black));
                                                child_m.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.Yellow));
                                            } else if(child >= 5 &&child < 20) {
                                                child_m.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.Black));
                                                child_m.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.Aqua));
                                            } else if (child >= 20 &&child < 60) {
                                                child_m.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.Black));
                                                child_m.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.Olive));
                                            } else if (child >= 60 && child <= 150) {
                                                child_m.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.Black));
                                                child_m.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.Lime));
                                            } else if (child > 150) {
                                                child_m.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.Black));
                                                child_m.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.Silver));
                                            } else {
                                                child_m.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.Black));
                                                child_m.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.White));
                                            }
                                        }

                                    }

                                    return false;
                                }
                            });

                            listView.setAdapter(adapter1);

                        }
                    });
                }  catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private boolean checkIfDataIsInt(String data) {     //變色的方法
        if (data.equals("") || data.isEmpty()) {
            return false;
        } else {
            try {
                int i = Integer.parseInt(data);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }


    private void searchfunction2() {  //顯示出spinner選擇的選項，判斷是在209行和204行的value
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    mList=new ArrayList<Map<String,Object>>();
                    JSONObject jsonobj1 = new JSONObject(data_url);
                    String aa = jsonobj1.getString("features");
                    JSONArray jsonarray1 = new JSONArray(aa);
                    for (int i = 0; i < jsonarray1.length(); i++){
                        JSONObject jsonobj2 = jsonarray1.getJSONObject(i);

                        String bb = jsonobj2.getString("properties");
                        JSONObject jsonobj3 = new JSONObject(bb);


                        String county = jsonobj3.getString("county");
                        String town = jsonobj3.getString("town");
                        String name = jsonobj3.getString("name");
                        String phone = jsonobj3.getString("phone");
                        String address = jsonobj3.getString("address");
                        String mask_adult =jsonobj3.getString("mask_adult");
                        String mask_child =jsonobj3.getString("mask_child");
                        String updated = jsonobj3.getString("updated");

                        Map<String, Object> item = new HashMap<String, Object>();
                        if(county.indexOf(m_city)==-1){
                            continue;
                        }else{
                            item.put("county", county);
                            item.put("town", town);
                            item.put("name", name);
                            item.put("phone", phone);
                            item.put("address", address);
                            item.put("mask_adult", mask_adult);
                            item.put("mask_child", mask_child);
                            time_1 = updated;
                            //item.put("t001",goldprice1);
                            mList.add(item);
                        }
                    }
                    runOnUiThread(new Runnable() {//ui做改變
                        @Override
                        public void run() {
//                                mdialog.cancel();
                            mdialog.cancel();
                            SimpleAdapter adapter1 = new SimpleAdapter(getApplicationContext(), mList, R.layout.mast_item, new String[]{"county","town","name","mask_adult","mask_child","phone","address"},
                                    new int[]{R.id.t001,R.id.t002,R.id.t003,R.id.t004,R.id.t005,R.id.t006,R.id.t007});
                            time.setText("更新時間："+time_1);
                            //設定list內的PM2.5的文字顏色和背景顏色
                            adapter1.setViewBinder(new SimpleAdapter.ViewBinder() {
                                @Override
                                public boolean setViewValue(View view, Object data, String textRepresentation) {

                                    if (view.getId() == R.id.t004) {
                                        TextView adult_m = (TextView) view;
                                        adult_m.setText(data.toString());
                                        if (checkIfDataIsInt(data.toString())) {
                                            int adult = Integer.parseInt(data.toString());
                                            //-判斷細懸浮微粒(PM2.5)指標對照
                                            if (adult == 0) {
                                                adult_m.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.Red));
                                                adult_m.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.Yellow));
                                            } else if(adult > 0 &&adult < 5) {
                                                adult_m.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.Black));
                                                adult_m.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.Yellow));
                                            } else if(adult >= 5 &&adult < 20) {
                                                adult_m.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.Black));
                                                adult_m.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.Aqua));
                                            } else if (adult >= 20 &&adult < 60) {
                                                adult_m.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.Black));
                                                adult_m.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.Olive));
                                            } else if (adult >= 60 && adult <= 150) {
                                                adult_m.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.Black));
                                                adult_m.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.Lime));
                                            } else if (adult > 150) {
                                                adult_m.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.Black));
                                                adult_m.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.Silver));
                                            } else {
                                                adult_m.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.Black));
                                                adult_m.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.White));
                                            }
                                        }

                                    }
                                    if (view.getId() == R.id.t005) {
                                        TextView child_m = (TextView) view;
                                        child_m.setText(data.toString());
                                        if (checkIfDataIsInt(data.toString())) {
                                            int child = Integer.parseInt(data.toString());
                                            //-判斷細懸浮微粒(PM2.5)指標對照
                                            if (child == 0) {
                                                child_m.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.Red));
                                                child_m.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.Yellow));
                                            } else if(child > 0 &&child < 5) {
                                                child_m.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.Black));
                                                child_m.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.Yellow));
                                            } else if(child >= 5 &&child < 20) {
                                                child_m.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.Black));
                                                child_m.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.Aqua));
                                            } else if (child >= 20 &&child < 60) {
                                                child_m.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.Black));
                                                child_m.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.Olive));
                                            } else if (child >= 60 && child <= 150) {
                                                child_m.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.Black));
                                                child_m.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.Lime));
                                            } else if (child > 150) {
                                                child_m.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.Black));
                                                child_m.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.Silver));
                                            } else {
                                                child_m.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.Black));
                                                child_m.setBackgroundColor(ContextCompat.getColor(getApplicationContext(),R.color.White));
                                            }
                                        }

                                    }



                                    return false;
                                }
                            });


                            listView.setAdapter(adapter1);

                        }
                    });
                }  catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();



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
