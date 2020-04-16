package com.dblgroup14.app.user;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import java.util.ArrayList;
import java.util.List;
/*FragmentStatePagerAdapter class that is an adapter for a ViewPager that shows the different tabs on the user profile page*/
public class TabAdapter extends FragmentStatePagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    
    public TabAdapter(FragmentManager fm) {
        super(fm);
    }
    //getItem method
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }
    
    //addFragment method
    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }
    
    //getPageTitle method
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
    
    //getCount method
    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
