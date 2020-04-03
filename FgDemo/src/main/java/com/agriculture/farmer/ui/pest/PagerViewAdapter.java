package com.agriculture.farmer.ui.pest;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;

class PagerViewAdapter extends PagerAdapter {
    private ArrayList<View> img_arraylist;
    private int num;
    public PagerViewAdapter(ArrayList<View> img_arraylist, int m_num){
        this.img_arraylist = img_arraylist;
        this.num = m_num;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Log.d("MainActivityDestroy",position+"");
//        if (img_arraylist.get(position)!=null) {
//            container.removeView(img_arraylist.get(position%img_arraylist.size()));
//        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
//        container.addView(img_arraylist.get(position%length));
//        Log.d("MainActivityInstanti",position+"");
//        return img_arraylist.get(position%length);

        //对ViewPager页号求模取出View列表中要显示的项
        position %= img_arraylist.size();
        View view = img_arraylist.get(position);
        //如果View已经在之前添加到了一个父组件，则必须先remove，否则会抛出IllegalStateException。
        ViewParent vp =view.getParent();

        if (vp!=null){
            ViewGroup parent = (ViewGroup)vp;
            parent.removeView(view);
        }

        container.addView(view);
        return view;



    }

    @Override
    public int getCount() {
        return num;
        // return img_arraylist.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object==view;
    }
}

