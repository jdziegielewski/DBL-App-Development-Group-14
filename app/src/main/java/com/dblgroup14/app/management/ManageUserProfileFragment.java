package com.dblgroup14.app.management;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.dblgroup14.app.R;
import com.dblgroup14.app.user.FriendsListFragment;
import com.dblgroup14.app.user.MyProfileFragment;
import com.dblgroup14.app.user.TabAdapter;
import com.google.android.material.tabs.TabLayout;

public class ManageUserProfileFragment extends Fragment {
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_manage_user_profile, container, false);
    }
    
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        // Hide action bar and change window colors
        if (Build.VERSION.SDK_INT >= 21) {
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            actionBar.hide();
            
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }
        
        ViewPager viewPager = view.findViewById(R.id.viewPagerProfile);
        TabLayout tabLayout = view.findViewById(R.id.tabLayoutProfile);
        TabAdapter adapter = new TabAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new MyProfileFragment(), "Me");
        adapter.addFragment(new FriendsListFragment(), "My friends");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        
        // Show action bar and change window colors back
        if (Build.VERSION.SDK_INT >= 21) {
            ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            actionBar.show();
            
            Window window = getActivity().getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }
}
