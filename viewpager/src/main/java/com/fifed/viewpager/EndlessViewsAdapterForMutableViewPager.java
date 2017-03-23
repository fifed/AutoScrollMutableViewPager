package com.fifed.viewpager;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Fedir on 18.03.2017.
 */

public abstract class EndlessViewsAdapterForMutableViewPager extends PagerAdapter implements AdapterForMutableViewPager {

    public abstract int getCollectionCount();

    public abstract View getContentView(ViewGroup container, int position);

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        int tag = position;
        position = position % getCollectionCount();
        View contentView = getContentView(container, position);
        container.addView(contentView);
        contentView.setTag(tag);
        return contentView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object view) {
       container.removeView((View) view);
    }

    @Override
    public int getCount() {
        return getCollectionCount() * (Integer.MAX_VALUE / getCollectionCount());
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public int getStartingItemToEndlessBothDirectScrolling(){
        return getCollectionCount() * (Integer.MAX_VALUE / getCollectionCount() / 2);
    }
}
