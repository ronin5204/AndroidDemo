package com.agriculture.farmer.ui.panel;


import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.agriculture.farmer.R;


public class FishdetailActivity extends AppCompatActivity {


    private TextView txt_title,txt_date,txt_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fish_detail);
        setupviewcomponent();
    }

    private void setupviewcomponent() {
        txt_title = (TextView)findViewById(R.id.txt_title);
        txt_date = (TextView)findViewById(R.id.txt_date);
        txt_content = (TextView)findViewById(R.id.txt_content);
        //title date city content suggest
        Bundle bd = this.getIntent().getExtras();
        String[]  arr = bd.getStringArray("title");

        txt_title.setText(arr[0]);
        txt_date.setText(arr[1]);
        txt_content.setText(arr[2]);
        setTitle("漁業新聞");
    }


}
