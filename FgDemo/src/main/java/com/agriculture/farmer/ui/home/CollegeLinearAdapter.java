package com.agriculture.farmer.ui.home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.agriculture.farmer.R;
import com.agriculture.farmer.bean.Collegedata;
import com.agriculture.farmer.bean.Transcationdata;

import java.util.List;

public class CollegeLinearAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<Collegedata> mLinearList;
    private OnitemClicklistener mListener;


    CollegeLinearAdapter(Context context, List<Collegedata> exampleList) {
        this.mContext = context;
        this.mLinearList = exampleList;
    }

    @Override @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View v = LayoutInflater.from(mContext).inflate(R.layout.college_item, parent, false);
         return new CollegeViewHolder(v);


    }


    @Override
    public int getItemCount() {
        return mLinearList.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        Collegedata collegedata = mLinearList.get(position);
        String name =collegedata.getName();
        String catt = collegedata.getCategary();
        String status = collegedata.getStatus();
        ((CollegeViewHolder)holder).mTextViewName.setText(name);
        ((CollegeViewHolder)holder).mTextViewCategory.setText(catt);
        ((CollegeViewHolder)holder).mTextViewStatus.setText(status);



        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onclick(position);
            }
        });

    }

    void setdata(List<Collegedata> data){
        mLinearList = data;
        notifyDataSetChanged();;
    }


    public interface OnitemClicklistener{
        void onclick(int pos);
    }
    public void setOnClicklistener(OnitemClicklistener mListener){
        this.mListener = mListener;
    }

}
