package com.agriculture.farmer.ui.pest;


import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import me.relex.circleindicator.CircleIndicator;

public class Indicator extends CircleIndicator {

    private ViewPager mViewpager;
    private int length;

    public Indicator(Context context) {
        super(context);
    }

    public Indicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Indicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public Indicator(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setViewPager(ViewPager viewPager,int count) {
        mViewpager = viewPager;
        this.length = count;//多傳了一個count進來，是img_arraylist的長度
        if (mViewpager != null && mViewpager.getAdapter() != null) {
            mLastPosition = -1;
            createIndicators();
            mViewpager.removeOnPageChangeListener(mInternalPageChangeListener);
            mViewpager.addOnPageChangeListener(mInternalPageChangeListener);
            mInternalPageChangeListener.onPageSelected(mViewpager.getCurrentItem()%length);
        }
    }

    private void createIndicators() {
        PagerAdapter adapter = mViewpager.getAdapter();
        int count;
        if (adapter == null) {
            count = 0;
        } else {
            count = length;
            //依據圖片的長度來設定indicator的個數，因為我們把adapter設定成長度的100倍，這樣就可以左右循環滑動
        }

        createIndicators(count, mViewpager.getCurrentItem());
    }

    private final ViewPager.OnPageChangeListener mInternalPageChangeListener =
            new ViewPager.OnPageChangeListener() {

                @Override public void onPageScrolled(int position, float positionOffset,
                                                     int positionOffsetPixels) {
                }

                @Override public void onPageSelected(int position) {

                    if (mViewpager.getAdapter() == null
                            || mViewpager.getAdapter().getCount() <= 0) {
                        return;
                    }
                    animatePageSelected(position%length);
                }

                @Override public void onPageScrollStateChanged(int state) {
                }
            };

    public DataSetObserver getDataSetObserver() {
        return mInternalDataSetObserver;
    }

    private final DataSetObserver mInternalDataSetObserver = new DataSetObserver() {
        @Override public void onChanged() {
            super.onChanged();
            if (mViewpager == null) {
                return;
            }
            PagerAdapter adapter = mViewpager.getAdapter();
            int newCount = adapter != null ? adapter.getCount() : 0;
            int currentCount = getChildCount();
            if (newCount == currentCount) {
                // No change
                return;
            } else if (mLastPosition < newCount) {
                mLastPosition = mViewpager.getCurrentItem();
            } else {
                mLastPosition = -1;
            }
            createIndicators();
        }
    };

    /**
     * @deprecated User ViewPager addOnPageChangeListener
     */
    @Deprecated public void setOnPageChangeListener(
            ViewPager.OnPageChangeListener onPageChangeListener) {
        if (mViewpager == null) {
            throw new NullPointerException("can not find Viewpager , setViewPager first");
        }
        mViewpager.removeOnPageChangeListener(onPageChangeListener);
        mViewpager.addOnPageChangeListener(onPageChangeListener);
    }
}
