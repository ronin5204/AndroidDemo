package com.agriculture.farmer.ui.home;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.agriculture.farmer.R;

public class RightItemDecoration extends RecyclerView.ItemDecoration {
    private final Context m_Context;

    public RightItemDecoration(Context c) {
        this.m_Context = c ;
    }

    @Override
    //這個方法是在item繪製之前可以使用，在item上在畫一些內容
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }
    //這個方法是在item繪製之後可以使用，在item上在畫一些內容
    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }
    //是在item周圍去繪製一些內容
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int d = m_Context.getResources().getDimensionPixelOffset(R.dimen.dividerHeight);
        outRect.set(d,d,d,d);
    }
}
