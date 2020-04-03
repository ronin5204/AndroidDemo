package com.agriculture.farmer.ui.home;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.agriculture.farmer.R;

class CollegeViewHolder extends RecyclerView.ViewHolder {
    TextView mTextViewName;
    TextView mTextViewCategory;
    TextView mTextViewStatus;
    CollegeViewHolder(View itemView) {
       super(itemView);
        mTextViewName = itemView.findViewById(R.id.coll_name);
        mTextViewCategory = itemView.findViewById(R.id.coll_category);
        mTextViewStatus = itemView.findViewById(R.id.coll_status);
   }
}