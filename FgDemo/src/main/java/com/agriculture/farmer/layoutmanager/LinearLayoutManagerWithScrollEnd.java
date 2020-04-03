package com.agriculture.farmer.layoutmanager;

import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

public class LinearLayoutManagerWithScrollEnd extends LinearLayoutManager {

    public LinearLayoutManagerWithScrollEnd(Context context) {
        super(context);
    }

    public LinearLayoutManagerWithScrollEnd(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public LinearLayoutManagerWithScrollEnd(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
//        LinearLayoutManager 446行 等於是改變下方繼承LinearSmoothScroller預設的getVerticalSnapPreference()
//        方法的返回值
        EndSnappedSmoothScroller endSnappedSmoothScroller = new EndSnappedSmoothScroller(recyclerView.getContext());
        endSnappedSmoothScroller.setTargetPosition(position);
        startSmoothScroll(endSnappedSmoothScroller);
    }

    @Override
    public void scrollToPosition(int position) {
        super.scrollToPosition(position);
    }

    class EndSnappedSmoothScroller extends LinearSmoothScroller {

        public EndSnappedSmoothScroller(Context context) {
            super(context);
        }

        @Nullable
        @Override
        public PointF computeScrollVectorForPosition(int targetPosition) {
            return LinearLayoutManagerWithScrollEnd.this.computeScrollVectorForPosition(targetPosition);
        }

        @Override
        protected int getVerticalSnapPreference() {
            //LinearSmoothScroller 220行设置滚动位置
            //他的預設如下，也就是position在可視圖上方的時候是跑到頂部，position在可視圖下方的時候使跑到底部
            //如果position在可視圖裡面的話就跑SNAP TO ANY了
//            return mTargetVector == null || mTargetVector.y == 0 ? SNAP_TO_ANY :
//                    mTargetVector.y > 0 ? SNAP_TO_END : SNAP_TO_START;
            return SNAP_TO_END;
        }
    }
}
