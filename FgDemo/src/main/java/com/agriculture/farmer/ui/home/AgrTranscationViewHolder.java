package com.agriculture.farmer.ui.home;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.agriculture.farmer.R;

class AgrTranscationViewHolder extends RecyclerView.ViewHolder {
    TextView mTextViewDate;
    TextView mTextViewCropname;
    TextView mTextViewMarketname;
    TextView mTextViewTopprice;
    TextView mTextViewMiddleprice;
    TextView mTextViewDownprice;
    TextView mTextViewAverageprice;
    TextView mTextViewTradingvolume;
    AgrTranscationViewHolder(View itemView) {
       super(itemView);
       mTextViewDate = itemView.findViewById(R.id.agrdate);
       mTextViewCropname = itemView.findViewById(R.id.cropname);
       mTextViewMarketname = itemView.findViewById(R.id.marketname);
       mTextViewTopprice = itemView.findViewById(R.id.veg_price_up);
       mTextViewMiddleprice = itemView.findViewById(R.id.veg_price_mid);
       mTextViewDownprice = itemView.findViewById(R.id.veg_price_down);
       mTextViewAverageprice = itemView.findViewById(R.id.veg_avg);
       mTextViewTradingvolume = itemView.findViewById(R.id.tradingvolume);
   }
}