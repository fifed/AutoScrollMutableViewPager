package com.fifed.viewpager;

import android.content.Context;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by Fedir on 18.03.2017.
 */

public class MutableAutoScrollViewPager extends ViewPager {
    private AutoScrollThread autoScrollThread;
    private final long ANIM_DURATION = 250;
    private boolean isAnimStarted, isEnabledAutoSize = true;

    public MutableAutoScrollViewPager(Context context) {
        super(context);
    }

    public MutableAutoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(isEnabledAutoSize && !isAnimStarted && getAdapter() != null) {
            int height = 0;
            View child = null;
            if(getAdapter() instanceof ViewsAdapterForMutableViewPager || getAdapter() instanceof EndlessViewsAdapterForMutableViewPager) {
                child = findViewWithTag(getCurrentItem());
            } else if(getAdapter() instanceof FragmentAdapterForMutableViewPager || getAdapter() instanceof FragmentStateAdapterForMutableViewPager) {
                child = ((Fragment)getAdapter().instantiateItem(this, getCurrentItem())).getView();
            }
            if (child != null) {
                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                height = child.getMeasuredHeight();
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN && height < getMinimumHeight()) {
                    height = getMinimumHeight();
                }
                int newHeight = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
                if (getLayoutParams().height != 0 && heightMeasureSpec != newHeight) {
                    final int targetHeight = height;
                    final int currentHeight = getLayoutParams().height;
                    final int heightChange = targetHeight - currentHeight;

                    Animation a = new Animation() {
                        @Override
                        protected void applyTransformation(float interpolatedTime, Transformation t) {
                            if (interpolatedTime >= 1) {
                                getLayoutParams().height = targetHeight;
                            } else {
                                int stepHeight = (int) (heightChange * interpolatedTime);
                                getLayoutParams().height = currentHeight + stepHeight;
                            }
                            requestLayout();
                        }

                        @Override
                        public boolean willChangeBounds() {
                            return true;
                        }
                    };
                    a.setInterpolator(new AccelerateInterpolator());
                    a.setDuration(ANIM_DURATION);
                    startAnimation(a);
                    a.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            isAnimStarted = true;
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            isAnimStarted = false;
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {
                        }
                    });
                } else {
                    heightMeasureSpec = newHeight;
                }
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(autoScrollThread != null) {
            autoScrollThread.onTouch();
        }
        return super.onTouchEvent(ev);
    }

    public void initAutoScroll(long intervalMS){
        if(autoScrollThread == null) {
            autoScrollThread = new AutoScrollThread(intervalMS);
            autoScrollThread.setOnNextPageInformer(new AutoScrollThread.OnNextPageInformer() {
                @Override
                public void goToNextPage() {
                    post(new Runnable() {
                        @Override
                        public void run() {
                            MutableAutoScrollViewPager.this.goToNextPage();
                        }
                    });
                }
            });
        }
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        if(adapter instanceof AdapterForMutableViewPager){
            super.setAdapter(adapter);
        } else {
            throw new RuntimeException("Adapter can be AdapterForMutableViewPager type only!");
        }
    }

    public void stopAutoScroll(){
        if(autoScrollThread != null){
            autoScrollThread.onStop();
            autoScrollThread = null;
        }
    }

    public void pauseAutoScroll(){
        if(autoScrollThread != null){
            autoScrollThread.onPause();
        }
    }

    public void resumeAutoScroll(){
        if(autoScrollThread != null){
            autoScrollThread.onResume();
        }
    }

    public void setEnabledAutoSize(boolean isEnabled){
        isEnabledAutoSize = isEnabled;
    }

    private void goToNextPage(){
        if(getAdapter().getCount() > 0) {
            if (getAdapter().getCount() - 1 == getCurrentItem()) {
                setCurrentItem(0, false);
            } else {
                setCurrentItem(getCurrentItem() + 1);
            }
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(autoScrollThread != null) {
            autoScrollThread.onStop();
            autoScrollThread = null;
        }
    }

}
