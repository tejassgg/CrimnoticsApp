package com.example.crimnoticsapp;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class LOCPageAdapter extends FragmentPagerAdapter {

    int tabcount;
    public LOCPageAdapter(FragmentManager fm, int behavior) {
        super(fm);
        tabcount=behavior;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position)
        {
            case 0: return new ReportedCrimeFrag();
            case 1: return new FiledComplaintFrag();
            case 2: return new MissedPersonFrag();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return tabcount;
    }


}
