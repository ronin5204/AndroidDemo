package com.agriculture.farmer.ui.home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.agriculture.farmer.R;
import com.agriculture.farmer.bean.Transcationdata;

import java.util.List;

public class AgrLinearAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<Transcationdata> mLinearList;
    private View mFooterView;
    private String footertext = "上拉繼續加載...";
    private OnitemClicklistener mListener;
    private boolean footer_gone = false;


    AgrLinearAdapter(Context context, List<Transcationdata> exampleList) {
        this.mContext = context;
        this.mLinearList = exampleList;
    }

    @Override @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == 1){
            return new FooterViewHolder(mFooterView);
        }else{
            View v = LayoutInflater.from(mContext).inflate(R.layout.transcation_agr_item, parent, false);
            return new AgrTranscationViewHolder(v);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount()-1&&mFooterView!=null){
            //return 1的時候應該加載footer
            return 1;
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        if(mFooterView!=null){
            return mLinearList.size()+1;
        }else{
            return mLinearList.size();
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Log.d("aa", "跑進bindviewholder了！ ");
        if(position==getItemCount()-1&&mFooterView!=null){
            //說明為footer的postion 不做動作
//            ((footerviewholder)holder).footer.setVisibility(View.GONE);
            if(footer_gone){
                ((FooterViewHolder)holder).footer.setVisibility(View.GONE);
            }else{
                ((FooterViewHolder)holder).footer.setVisibility(View.VISIBLE);
                ((FooterViewHolder)holder).footer.setText(footertext);
            }
        }else{
            Transcationdata transcationdata = mLinearList.get(position);
            Log.d("test", mLinearList.get(position).getAverageprice());
            String date =transcationdata.getDate();
            String crop = "作物名稱："+transcationdata.getCropname()+"("+transcationdata.getCropnum()+")";
            String market = "市場名稱："+transcationdata.getMarketname()+"("+transcationdata.getMarketnum()+")";
            String up = "上價："+transcationdata.getTopprice();
            String middler = "中價："+transcationdata.getMiddleprice();
            String down = "下價格："+transcationdata.getDownprice();
            String average = "平均價："+transcationdata.getAverageprice();
            String tra = "交易量："+transcationdata.getTradingvolume();
            ((AgrTranscationViewHolder)holder).mTextViewDate.setText(date);
            ((AgrTranscationViewHolder)holder).mTextViewCropname.setText(crop);
            ((AgrTranscationViewHolder)holder).mTextViewMarketname.setText(market);
            ((AgrTranscationViewHolder)holder).mTextViewTopprice.setText(up);
            ((AgrTranscationViewHolder)holder).mTextViewMiddleprice.setText(middler);
            ((AgrTranscationViewHolder)holder).mTextViewDownprice.setText(down);
            ((AgrTranscationViewHolder)holder).mTextViewAverageprice.setText(average);
            ((AgrTranscationViewHolder)holder).mTextViewTradingvolume.setText(tra);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onclick(position);
            }
        });

    }
    void setFooterView(View footerView,String text) {
        mFooterView = footerView;
        footertext=text;
        notifyDataSetChanged();
    }
     void setFooter_gone(Boolean isgone){
        this.footer_gone = isgone;
        notifyDataSetChanged();
    }

     void adddata(List<Transcationdata> data){
        mLinearList.addAll(data);
        notifyDataSetChanged();;
    }

    void setdata(List<Transcationdata> data){
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
