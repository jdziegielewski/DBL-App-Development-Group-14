package com.dblgroup14.app.management;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.dblgroup14.app.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ManageChallengesFragment extends Fragment {
    @StringRes
    private static final int[] TITLES = new int[] {R.string.tab_default_challenges, R.string.tab_my_challenges};
    
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_manage_challenges, container, false);
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Get components
        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);
        
        // Initialize tabs
        initializeTabs();
    }
    
    private void initializeTabs() {
        // Set view pager adapter
        viewPager.setAdapter(new ViewPagerAdapter());
        
        // Attach view pager to tab layout through TabLayoutMediator
        TabLayoutMediator mediator = new TabLayoutMediator(tabLayout, viewPager, (tab, pos) -> tab.setText(TITLES[pos]));
        mediator.attach();
    }
    
    private class ViewPagerAdapter extends FragmentStateAdapter {
        public ViewPagerAdapter() {
            super(getActivity());
        }
        
        @NonNull
        @Override
        public Fragment createFragment(int position) {
            // Create arguments
            Bundle args = new Bundle();
            args.putInt(ChallengesListFragment.KEY_TYPE, position);
            
            // Create new challenges list fragment
            ChallengesListFragment fragment = new ChallengesListFragment();
            fragment.setArguments(args);
            
            return fragment;
        }
        
        @Override
        public int getItemCount() {
            return 2;   // two tabs
        }
    }
}
