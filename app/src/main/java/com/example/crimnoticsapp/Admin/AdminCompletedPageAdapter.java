package com.example.crimnoticsapp.Admin;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.crimnoticsapp.Admin.AdminFragments.AdminFiledComplaintFrag;
import com.example.crimnoticsapp.Admin.AdminFragments.Completed.MissedPersonCompletedFrag;
import com.example.crimnoticsapp.Admin.AdminFragments.Completed.ReportedCrimeCompletedFrag;

public class AdminCompletedPageAdapter extends FragmentPagerAdapter {
    int tabcount;
    public AdminCompletedPageAdapter(FragmentManager fm, int behavior) {
        super(fm);
        tabcount = behavior;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position)
        {
            case 0: return new ReportedCrimeCompletedFrag();
            case 1: return new AdminFiledComplaintFrag();
            case 2: return new MissedPersonCompletedFrag();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return tabcount;
    }
}
