package com.agriculture.farmer.ui.panel;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.agriculture.farmer.R;
import com.agriculture.farmer.WebView;


public class AgrdetailActivity extends AppCompatActivity implements View.OnClickListener {


    private TextView txt_title,txt_content;
    private Button btn;
    private String[] arr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agr_detaill);
        setupviewcomponent();
    }

    private void setupviewcomponent() {
        txt_title = (TextView)findViewById(R.id.agr_t001);
        txt_content = (TextView)findViewById(R.id.agr_t002);
        btn = (Button)findViewById(R.id.agr_b001);
        btn.setOnClickListener(this);
        //title date city content suggest
        Bundle bd = this.getIntent().getExtras();
        arr = bd.getStringArray("title");

        txt_title.setText(arr[0]);
        txt_content.setText(arr[2]);
    }


    @Override
    public void onClick(View v) {
        Intent it = new Intent();
        it.setClass(this, WebView.class);

        Bundle bd = new Bundle();
        //title date city content suggest
        bd.putString("url", arr[3]);
        it.putExtras(bd);
        startActivity(it);
    }
}
