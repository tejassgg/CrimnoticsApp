package com.example.crimnoticsapp.Admin;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.crimnoticsapp.Admin.AdminFragments.Acknowledged.MissedPersonAcknowledgedFrag;
import com.example.crimnoticsapp.Admin.AdminFragments.Acknowledged.ReportedCrimeAcknowledgedFrag;
import com.example.crimnoticsapp.Admin.AdminFragments.AdminFiledComplaintFrag;

public class AdminAcknowledgedPageAdapter extends FragmentPagerAdapter {

    int tabcount;

    public AdminAcknowledgedPageAdapter(FragmentManager fm,int behavior) {
        super(fm);
        tabcount = behavior;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position)
        {
            case 0: return new ReportedCrimeAcknowledgedFrag();
            case 1: return new AdminFiledComplaintFrag();
            case 2: return new MissedPersonAcknowledgedFrag();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return tabcount;
    }
}
