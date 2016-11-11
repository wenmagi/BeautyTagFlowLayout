package com.wen.magi.labeltest.widgets;

/**
 * @author zhangzhaowen @ Zhihu Inc.
 * @since 11-01-2016
 */

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * 流标签ViewGroup
 *
 * @author zhangzhaowen @ Zhihu Inc.
 * @since 10-19-2016
 */

public class TagFlowLayout extends ViewGroup {

    private List<Integer> mViewWidth = new ArrayList<>();
    private List<View> mAllViews = new ArrayList<>();
    private List<View> mSortedView = new ArrayList<>();
    //排序过程中已经消耗掉的View位置
    private List<Integer> mSortedViewPos = new ArrayList<>();


    private List<List<View>> mLineViews = new ArrayList<>();
    private SparseIntArray mLineHeights = new SparseIntArray();
    private List<View> mLineView = new ArrayList<>();


    public TagFlowLayout(Context context) {
        super(context);
    }

    public TagFlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TagFlowLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new ViewGroup.MarginLayoutParams(getContext(), attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int sizeWidth = View.MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = View.MeasureSpec.getSize(heightMeasureSpec);
        int modeWidth = View.MeasureSpec.getMode(widthMeasureSpec);
        int modeHeight = View.MeasureSpec.getMode(heightMeasureSpec);

        int[] size = measureSize(widthMeasureSpec, heightMeasureSpec);
        if (size == null || size.length == 0)
            return;

        int width = size[0], height = size[1];
        setMeasuredDimension((modeWidth == MeasureSpec.EXACTLY) ? sizeWidth : width,
                (modeHeight == MeasureSpec.EXACTLY) ? sizeHeight : height);
        setMeasuredDimension((modeWidth == View.MeasureSpec.EXACTLY) ? sizeWidth : width,
                (modeHeight == View.MeasureSpec.EXACTLY) ? sizeHeight : height);
    }

    /**
     * 测量并记录每个子View的信息「宽度」「View」，测量ViewGroup的宽高
     *
     * @param widthMeasureSpec  parent widthMeasureSpec
     * @param heightMeasureSpec parent heightMeasureSpec
     * @return this.wh
     */
    private int[] measureSize(int widthMeasureSpec, int heightMeasureSpec) {
        mAllViews.clear();
        mViewWidth.clear();

        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int cCount = getChildCount();

        for (int i = 0; i < cCount; i++) {
            View child = getChildAt(i);

            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;

            mViewWidth.add(childWidth);
            mAllViews.add(child);
        }

        if (mViewWidth != null && mViewWidth.size() > 0) {
            quickSortViews(mViewWidth, 0, mViewWidth.size() - 1);
            sortLabelsForBeauty(sizeWidth);
        }

        return measureForRectArea(cCount, sizeWidth);
    }

    /**
     * 测量重新排序后，子View占的宽高
     *
     * @param cCount    子View数量
     * @param sizeWidth 父控件宽度
     * @return 父控件实际宽高
     */
    private int[] measureForRectArea(final int cCount, final int sizeWidth) {
        int[] wh = new int[2];

        int width = 0, height = 0;

        int lineWidth = 0, lineHeight = 0;
        for (int i = 0; i < cCount; i++) {
            View child = mAllViews.get(i);

            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

            if (lineWidth + childWidth > sizeWidth) {
                width = Math.max(lineWidth, childWidth);
                lineWidth = childWidth;

                height += lineHeight;
                lineHeight = childHeight;
            } else {
                lineWidth += childWidth;
                lineHeight = Math.max(lineHeight, childHeight);
            }
            if (i == cCount - 1) {
                width = Math.max(width, lineWidth);
                height += lineHeight;
            }
        }

        wh[0] = width;
        wh[1] = height;

        return wh;
    }

    /**
     * 动态排序，制定label顺序，子类可以复写排序规则
     *
     * @param parentWidth 父控件宽度
     */
    protected void sortLabelsForBeauty(final int parentWidth) {
        sortLabelsForBeautyDefault(parentWidth);
    }

    private void sortLabelsForBeautyDefault(final int parentWidth) {
        int cCount = mAllViews.size();
        mSortedViewPos.clear();

        for (int i = 0; i < cCount; i++) {

            if (mSortedViewPos.contains(i)) {
                continue;
            }

            mSortedView.add(mAllViews.get(i));

            int width = mViewWidth.get(i);
            int leftSpace = parentWidth - width;

            int minWidth = 0;
            int lastPos = cCount - 1;

            for (int minPos = cCount - 1; minPos > i; minPos--) {
                if (!mSortedViewPos.contains(minPos)) {
                    lastPos = minPos;
                    minWidth = mViewWidth.get(minPos);
                    break;
                }
            }

            if (leftSpace - minWidth <= 0)
                continue;

            for (int j = i + 1; j <= lastPos; j++) {
                if(mSortedViewPos.contains(j))
                    continue;

                View child = mAllViews.get(j);
                int cSpaceWidth = mViewWidth.get(j);

                if (cSpaceWidth > leftSpace)
                    continue;

                mSortedViewPos.add(j);
                mSortedView.add(child);

                leftSpace -= cSpaceWidth;
                if (leftSpace <= 0)
                    break;

            }

        }
    }


    private void quickSortViews(List<Integer> list, int low, int high) {
        if (low >= high)
            return;

        int start = low;
        int end = high;
        int key = list.get(low);
        View view = mAllViews.get(low);
        while (start < end) {
            while (start < end && list.get(end) <= key)
                end--;

            list.set(start, list.get(end));
            mAllViews.set(start, mAllViews.get(end));

            while (start < end && list.get(start) > key)
                start++;

            list.set(end, list.get(start));
            mAllViews.set(end, mAllViews.get(start));
        }
        list.set(end, key);
        mAllViews.set(end, view);
        quickSortViews(list, low, start - 1);
        quickSortViews(list, end + 1, high);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        measureViewParams();

        int lineHeight;
        int left = 0, top = 0;

        int lineNum = mLineViews.size();

        for (int i = 0; i < lineNum; i++) {
            mLineView = mLineViews.get(i);

            lineHeight = mLineHeights.get(i);
            for (int j = 0; j < mLineView.size(); j++) {
                View child = mLineView.get(j);

                if (child.getVisibility() == View.GONE)
                    continue;

                ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) child.getLayoutParams();

                int lc = left + lp.leftMargin;
                int tc = top + lp.topMargin;
                int rc = lc + child.getMeasuredWidth();
                int bc = tc + child.getMeasuredHeight();
                child.layout(lc, tc, rc, bc);
                Log.w("view info ", ((TextView) child).getText().toString());
                left += child.getMeasuredWidth() + lp.rightMargin + lp.leftMargin;
            }
            left = 0;
            top += lineHeight;
        }
    }

    /**
     * 测量每行View信息
     */
    private void measureViewParams() {
        mLineViews.clear();
        mLineHeights.clear();

        int parentWidth = getWidth();

        int lineWidth = 0, lineHeight = 0;


        int cCount = getChildCount();

        for (int i = 0; i < cCount; i++) {
            View child = mSortedView.get(i);

            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) child.getLayoutParams();
            int childWidth = child.getMeasuredWidth(), childHeight = child.getMeasuredHeight();

            if (childWidth + lp.leftMargin + lp.rightMargin + lineWidth > parentWidth) {
                mLineViews.add(mLineView);

                lineWidth = 0;
                mLineView = new ArrayList<>();
            }
            mLineHeights.put(i, lineHeight);
            lineWidth += childWidth + lp.leftMargin + lp.rightMargin;
            lineHeight = Math.max(lineHeight, childHeight + lp.topMargin + lp.bottomMargin);
            mLineView.add(child);
        }

        mLineHeights.put(cCount - 1, lineHeight);
        mLineViews.add(mLineView);
    }

}
