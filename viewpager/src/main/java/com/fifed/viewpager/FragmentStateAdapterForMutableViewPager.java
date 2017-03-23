package com.fifed.viewpager;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Fedir on 21.03.2017.
 */

public abstract class FragmentStateAdapterForMutableViewPager extends FragmentStatePagerAdapter implements AdapterForMutableViewPager{
    public FragmentStateAdapterForMutableViewPager(FragmentManager fm) {
        super(fm);
    }
}
