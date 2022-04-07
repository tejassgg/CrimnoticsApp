package com.example.crimnoticsapp.Admin;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.crimnoticsapp.Admin.AdminFragments.AdminFiledComplaintFrag;
import com.example.crimnoticsapp.Admin.AdminFragments.AdminMissedPersonFrag;
import com.example.crimnoticsapp.Admin.AdminFragments.AdminReportedCrimeFrag;

public class AdminLOCPageAdapter extends FragmentPagerAdapter {
    int tabcount;
    public AdminLOCPageAdapter(FragmentManager fm, int behavior) {
        super(fm);
        tabcount =behavior;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position)
        {
            case 0: return new AdminReportedCrimeFrag();
            case 1: return new AdminFiledComplaintFrag();
            case 2: return new AdminMissedPersonFrag();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return tabcount;
    }
}
