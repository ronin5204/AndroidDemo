package com.agriculture.farmer.ui.pest;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.agriculture.farmer.R;
import com.agriculture.my_library.My_library;
import com.agriculture.my_library.TransTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;


public class Activity_pest extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ListView listView;
    private ArrayList<Map<String, Object>> pest_arraylist;
    private ProgressDialog mdialog;
    private EditText search_et;
    private Button search_btn,small_btn,big_btn;
    private String data_url;
    private JSONArray pest_jsonarray;

    private String[][] picurl_array;
    private JSONArray[] pest_jsonarray2;
    private String[] pestdata_array;
    private int position=0;
    int sp = 16;

    //自己寫的function放在m_function，然後再調用
    My_library my_library = new My_library();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pest_main);
        setupviewcomponent();
    }


    private void setupviewcomponent() {

        my_library.ignoressl();//忽略ssl

        search_et=(EditText)findViewById(R.id.peste001);
        search_btn = (Button)findViewById(R.id.pestb001);
        small_btn = (Button)findViewById(R.id.pestsmall);
        big_btn = (Button)findViewById(R.id.pestbig);
        search_btn.setOnClickListener(this);
        small_btn.setOnClickListener(this);
        big_btn.setOnClickListener(this);
        
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
        // 動態調整高度 抓取使用裝置尺寸
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int newscrollheight = displayMetrics.heightPixels * 90 / 100;
        listView = (ListView) findViewById(R.id.pest_listview);
        listView.getLayoutParams().height = newscrollheight;
        listView.setLayoutParams(listView.getLayoutParams());
        listView.setOnItemClickListener(this);//listview設定監聽

        mdialog = new ProgressDialog(this); //設置剛進去時的dialog
        mdialog.setTitle("加載中..");
        mdialog.setCancelable(false);
        mdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mdialog.show();

        load_data(); //下載資料
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pestb001:
                search_et.setText("");
                break;
            case R.id.pestbig:
                if(sp<20){
                    sp++; //字體大小sp
                }
                m_setadapter();

                break;
            case R.id.pestsmall:
                if(sp>12){
                    sp--;
                }
                m_setadapter();
                break;
        }

    }

    private void load_data() {//TransTask()抓取資料的方法
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    pest_arraylist = new ArrayList<Map<String,Object>>();
                    //第一層的jsonarray(讀取資料，第一次進入這個頁面才要讀取)--------------------------
                    data_url = new TransTask().execute("https://data.coa.gov.tw/Service/OpenData/AgriculturalPests.aspx").get();
                    pest_jsonarray = new JSONArray(data_url);
                    //------------------------------------------------------------------------------
                    //設置第二層的jsonarray的長度
                    pest_jsonarray2 = new JSONArray[pest_jsonarray.length()];
                    picurl_array = new String[pest_jsonarray.length()][20];
                    pestdata_array = new String[pest_jsonarray.length()];
                    for (int i = 0; i < pest_jsonarray.length(); i++){
                        //第一層的jsonobject
                        JSONObject pest_jsonOb1 = pest_jsonarray.getJSONObject(i);
                        //"圖檔"的value是第二層的jsonarray(讀取資料)--------------------------
                        String pic_url = pest_jsonOb1.getString("圖檔");
                        String sec_data_url = new TransTask().execute(pic_url).get();
                        pest_jsonarray2[i] = new JSONArray(sec_data_url);

                    }
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
        boolean get_firstpic = true;
        String first_pic = "";
        try {
            pest_arraylist = new ArrayList<Map<String,Object>>();
            position = 0;
            for (int i = 0; i < pest_jsonarray.length(); i++){
                //第一層的jsonobject
                JSONObject pest_jsonOb1 = pest_jsonarray.getJSONObject(i);
                //key_name代表搜尋是以“作物名稱”來搜尋
                String key_name = pest_jsonOb1.getString("作物名稱");
                String user_input = search_et.getText().toString();//用戶輸入的

                if(key_name.indexOf(user_input)==-1){
                    //用戶輸入的字不在相應的作物名稱當中的話就繼續循環
                    continue;
                }

                //放在dialog裡面的害蟲具體信息(先放在array裡)---------------------
                String ch_branch = "害蟲科別：" + pest_jsonOb1.getString("中文科別");
                String pest_engname = "\n害蟲學名：" + pest_jsonOb1.getString("害蟲學名");
                String danger = "\n危害：";
                if(pest_jsonOb1.getString("危害_根").equals("Y")){danger+="根 ";}
                if(pest_jsonOb1.getString("危害_莖").equals("Y")){danger+="莖 ";}
                if(pest_jsonOb1.getString("危害_葉").equals("Y")){danger+="葉 ";}
                if(pest_jsonOb1.getString("危害_花").equals("Y")){danger+="花 ";}
                if(pest_jsonOb1.getString("危害_果").equals("Y")){danger+="果 ";}
                if(pest_jsonOb1.getString("危害_整株").equals("Y")){danger+="整株 ";}

                pestdata_array[position] = ch_branch + pest_engname + danger;
                //以上放在dialog裡面的害蟲具體信息(所以先放在array裡)---------------------
                
                int index = 0;//記錄有幾個蟲體的圖片
                for(int j=0;j<pest_jsonarray2[i].length();j++){
                    //第二層的jsonobject
                    JSONObject pest_jsonOb2 = pest_jsonarray2[i].getJSONObject(j);
                    String pic_type = pest_jsonOb2.getString("圖說");//植株or蟲體
                    String sec_pic_url = pest_jsonOb2.getString("圖檔聯結");//第一個都是植株的圖片
                    if(get_firstpic){
                        //每一筆資料獲得的第一個圖片都是植株（植株的圖片顯示在listview裡面）
                        first_pic = sec_pic_url;
                        get_firstpic = false;
                    }
                    if(pic_type.equals("蟲體")) {
                        index++;//記錄有幾個蟲體的圖片
                        picurl_array[position][index] = sec_pic_url;//蟲體的圖片放在陣列中，會員點擊listview要顯示所有蟲體的圖片

                    }
                }
                picurl_array[position][0] = String.valueOf(index);//picurl_array陣列的第一筆資料來記錄有幾個蟲體的圖片
                position++;
                get_firstpic = true;
                //設定item，裝入arraylist
                Map<String, Object> pest_item = new HashMap<String, Object>();
                String title = "圖片：植株(點擊查看害蟲詳情)";
                String crop_name = "\n作物名稱：" + pest_jsonOb1.getString("作物名稱");
                String crop_engname = "\n作物學名：" + pest_jsonOb1.getString("作物學名");
                String pest_chname = "\n害蟲名稱：" + pest_jsonOb1.getString("害蟲中文名");
                String txt_string = title + crop_name + crop_engname + pest_chname;
                //設定item
                pest_item.put("pest_url",first_pic);
                pest_item.put("pest_txt",txt_string);
                pest_item.put("pest_img",R.drawable.ic_launcher_background);
                //裝入arraylist
                pest_arraylist.add(pest_item);

            }
            m_setadapter();//arraylist設定完之後就設置adapter

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void m_setadapter() { //重新設定adapter（也就是重新設定listview）
        SimpleAdapter pest_adapter = new SimpleAdapter(getApplicationContext(), pest_arraylist, R.layout.list, new String[]{"pest_url","pest_txt","pest_img"}, new int[]{R.id.url,R.id.txt,R.id.img});
        pest_adapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                if (view.getId() == R.id.img) {
                    //因為將圖片url放進了url這個textview中，但是setViewBinder這個function一次
                    //只能抓到一個view，所以利用這個view先找到父控件（LinearLayout），在用這個
                    //LinearLayout抓取url的內容（也就是圖片的url），在利用自己所寫的set_image這個方法，
                    //將img設定成相應的圖片，在將url這個view隱藏
                    ImageView img = (ImageView) view;
                    
                    LinearLayout r01 = (LinearLayout) img.getParent();

                    LayoutInflater inflater = getLayoutInflater();//inflater物件取得現在所用的layout
                    View layout = inflater.inflate(R.layout.list, (ViewGroup)r01);

                    TextView url =(TextView)layout.findViewById(R.id.url);
                    String img_url = url.getText().toString();
                    url.setVisibility(View.INVISIBLE);

                    my_library.set_image(img,img_url,250, Activity_pest.this);

                    return true;
                }else if(view.getId() == R.id.txt){
                    //相應sp的字體設定不同的間隔
                    TextView txt = (TextView)view;
                    txt.setTextSize(sp);
                    int add = 80-sp*4;
                    txt.setLineSpacing(add,1);
                    return false;
                }else{
                    return false;
                }
            }

        });
        listView.setAdapter(pest_adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ArrayList<View> img_arraylist = new ArrayList<View>();
        String pest_string = pestdata_array[position];
        //陣列的第一筆的資料是該position裡面有幾個蟲體的圖片，第二個以後的資料是蟲體圖片的圖檔url
        Integer pestimg_num = Integer.parseInt(picurl_array[position][0]);
        for(int i = 1;i<=pestimg_num;i++){
            //無中生有一個imgeview，用來裝蟲體的圖片，每兩張裝進一次img_layout裡面（也就是每兩張圖片就增加一個img_layout）
            ImageView m_img = new ImageView(this);
            my_library.set_image(m_img,picurl_array[position][i],600, Activity_pest.this);//利用蟲體圖片的url設定給m_img
            img_arraylist.add(m_img);
        }
        Dialog m_dialog = set_vpDialog(img_arraylist,pest_string, Activity_pest.this);
        m_dialog.show();

    }

    private Dialog set_vpDialog(ArrayList<View> img_arraylist, String pest_string, Activity_pest context) {
        LayoutInflater inflater = getLayoutInflater();//inflater物件取得現在所用的layout
        View mylayout = inflater.inflate(R.layout.viewpager, (ViewGroup)findViewById(R.id.ll_viewpage));
        TextView txt = (TextView)mylayout.findViewById(R.id.vp_txt) ;
        Indicator indicator = (Indicator)mylayout.findViewById(R.id.vp_indicate);
        ViewPager viewpager =(ViewPager)mylayout.findViewById(R.id.vp_viewpager);

        PagerViewAdapter adapter;
        if(img_arraylist.size()==1){
            adapter = new PagerViewAdapter(img_arraylist,1);//要一個裝圖片的arraylist
        }else{
            adapter = new PagerViewAdapter(img_arraylist,img_arraylist.size()*200);//要一個裝圖片的arraylist
        }

        txt.setText(pest_string);//要一個string
        viewpager.setAdapter(adapter);
        if(img_arraylist.size()!=1) {
            viewpager.setCurrentItem(img_arraylist.size() * 100);//設置初始位置在第幾個view
            indicator.setViewPager(viewpager, img_arraylist.size());
            adapter.registerDataSetObserver(indicator.getDataSetObserver());
        }else{
            indicator.setViewPager(viewpager, img_arraylist.size());
        }

        Dialog m_dialog = new Dialog(context);//要一個context
        m_dialog.setCancelable(true);
        m_dialog.setContentView(mylayout);

        return m_dialog;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
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


