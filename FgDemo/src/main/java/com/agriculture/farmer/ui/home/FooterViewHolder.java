package com.agriculture.farmer.ui.home;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.agriculture.farmer.R;

class FooterViewHolder extends RecyclerView.ViewHolder {
     TextView footer;


     FooterViewHolder(View itemView) {
        super(itemView);
        footer = itemView.findViewById(R.id.footer);
    }
}