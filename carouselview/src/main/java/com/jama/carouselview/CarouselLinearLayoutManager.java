package com.jama.carouselview;

import android.content.Context;
import android.graphics.PointF;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

public class CarouselLinearLayoutManager extends LinearLayoutManager {

  private static final float MILLISECONDS_PER_INCH = 5f;
  private boolean isOffsetStart;
  private boolean scaleOnScroll = false;

  CarouselLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
    super(context, orientation, reverseLayout);
  }

  @Override
  public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
    super.onLayoutChildren(recycler, state);
    scrollHorizontallyBy(0, recycler, state);
  }

  @Override
  public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
    int scrolled = super.scrollHorizontallyBy(dx, recycler, state);
    if (this.scaleOnScroll) {

      for (int i = 0; i < getChildCount(); i++) {
        View child = getChildAt(i);

        float childWidth = child.getRight() - child.getLeft();
        float childWidthHalf = childWidth / 2.f;
        float childCenter = child.getLeft() + childWidthHalf;

        float parentWidth = this.isOffsetStart ? childWidth : getWidth();
        float parentWidthHalf = parentWidth / 2.f;

        float d0 = 0.f;
        float mShrinkDistance = .75f;
        float d1 = mShrinkDistance * parentWidthHalf;
        float s0 = 1.f;
        float mShrinkAmount = 0.15f;
        float s1 = 1.f - mShrinkAmount;

        float d = Math.min(d1, Math.abs(parentWidthHalf - childCenter));

        float position = s0 + (s1 - s0) * (d - d0) / (d1 - d0);

        child.setScaleX(position);
        child.setScaleY(position);
      }
      return scrolled;
    } else {
      return scrolled;
    }
  }

  @Override
  public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
    final LinearSmoothScroller linearSmoothScroller = new LinearSmoothScroller(recyclerView.getContext()) {

      @Override
      public PointF computeScrollVectorForPosition(int targetPosition) {
        return super.computeScrollVectorForPosition(targetPosition);
      }

      @Override
      protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
        return MILLISECONDS_PER_INCH / displayMetrics.densityDpi;
      }
    };

    linearSmoothScroller.setTargetPosition(position);
    startSmoothScroll(linearSmoothScroller);
  }

  void isOffsetStart(boolean isOffsetStart) {
    this.isOffsetStart = isOffsetStart;
  }

  void setScaleOnScroll(boolean scaleOnScroll) {
    this.scaleOnScroll = scaleOnScroll;
  }

}
