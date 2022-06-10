package com.example.ndg.alocasiafarming;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends FragmentPagerAdapter {
    private final List<Fragment>fragmentList= new ArrayList<>();
    private final  List<String> fragmentString= new ArrayList<>();
    public HistoryAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentString.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
    public void addFrag(Fragment fragment, String title){
        fragmentList.add(fragment);
        fragmentString.add(title);
    }
}
