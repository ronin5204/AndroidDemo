package com.agriculture.farmer.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.agriculture.farmer.R;
import com.agriculture.farmer.WebView;
import com.agriculture.farmer.ui.mask.MaskdataActivity;
import com.agriculture.farmer.ui.panel.EpidemicActivity;
import com.agriculture.farmer.ui.panel.FishActivity;
import com.agriculture.farmer.ui.panel.AgrActivity;
import com.agriculture.farmer.ui.pest.Activity_pest;
import com.agriculture.my_library.CircleImageView;
import com.bumptech.glide.Glide;
import com.navdrawer.SimpleSideDrawer;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    private SimpleSideDrawer mNav;
    private View mView;
    private ArrayList<String> memdata = new ArrayList<String>();
    private CircleImageView img;
    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNav = new SimpleSideDrawer(this);     // libary中的class
        mNav.setLeftBehindContentView(R.layout.panel);
        mView = findViewById(R.id.appbar_show);
        mView.setOnClickListener(this);
        mNav.findViewById(R.id.panel_agr).setOnClickListener(this);
        mNav.findViewById(R.id.panel_fish).setOnClickListener(this);
        mNav.findViewById(R.id.panel_plant).setOnClickListener(this);
        mNav.findViewById(R.id.panel_pest).setOnClickListener(this);
        mNav.findViewById(R.id.panel_mask).setOnClickListener(this);
        findViewById(R.id.homeimgco2).setOnClickListener(this);
        findViewById(R.id.homeimguv).setOnClickListener(this);
        findViewById(R.id.homeimg01).setOnClickListener(this);
        findViewById(R.id.homeimg02).setOnClickListener(this);

        setheader();

    }

    private void setheader() {
        img = mNav.findViewById(R.id.panel_header);
        Intent it= this.getIntent();
        Bundle bd = it.getBundleExtra("BUNDLE");
        memdata = bd.getStringArrayList("memdata");
        Log.d("aa", memdata.get(2));
        Glide.with(this).load(memdata.get(2)).into(img);
    }

    @Override
    public void onClick(View v) {
        Intent it = new Intent();
        Bundle bd = new Bundle();
        switch (v.getId()){
            case R.id.panel_agr:
                it.setClass(this, AgrActivity.class);
                startActivity(it);
                break;
            case R.id.panel_fish:
                it.setClass(this, FishActivity.class);
                startActivity(it);
                break;
            case R.id.panel_plant:
                it.setClass(this, EpidemicActivity.class);
                startActivity(it);
                break;
            case R.id.panel_pest:
                it.setClass(this, Activity_pest.class);
                startActivity(it);
                break;
            case R.id.panel_mask:
                it.setClass(this, MaskdataActivity.class);
                startActivity(it);
                break;
            case R.id.appbar_show:
                mNav.toggleLeftDrawer();
                break;
            case R.id.homeimgco2:
                it.setClass(this, WebView.class);
                bd.putString("url", "https://taqm.epa.gov.tw/taqm/tw/default.aspx");
                it.putExtras(bd);
                startActivity(it);
                break;
            case R.id.homeimguv:
                it.setClass(this, WebView.class);
                bd.putString("url", "https://www.cwb.gov.tw/V8/C/W/OBS_UVI.html");
                it.putExtras(bd);
                startActivity(it);
                break;
            case R.id.homeimg01:
                it.setClass(this, AgrTranscationActivity.class);
                startActivity(it);
                break;
            case R.id.homeimg02:
                it.setClass(this, CollegeActivity.class);
                startActivity(it);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.panel, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.show_panel:
                mNav.toggleRightDrawer();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
