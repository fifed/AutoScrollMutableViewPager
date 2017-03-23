package com.fifed.viewpager;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Fedir on 21.03.2017.
 */

public abstract class FragmentAdapterForMutableViewPager extends FragmentPagerAdapter implements AdapterForMutableViewPager {

    public FragmentAdapterForMutableViewPager(FragmentManager fm) {
        super(fm);
    }
}
