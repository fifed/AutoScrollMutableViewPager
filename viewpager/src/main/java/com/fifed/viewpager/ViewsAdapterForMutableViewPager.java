package com.fifed.viewpager;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Fedir on 21.03.2017.
 */

public abstract class ViewsAdapterForMutableViewPager extends PagerAdapter implements AdapterForMutableViewPager  {

    public abstract View getContentView(ViewGroup container, int position);

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View contentView = getContentView(container, position);
        container.addView(contentView);
        contentView.setTag(position);
        return contentView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object view) {
        container.removeView((View) view);
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}
