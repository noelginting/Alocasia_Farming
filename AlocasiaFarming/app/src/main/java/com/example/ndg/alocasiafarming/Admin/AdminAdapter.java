package com.example.ndg.alocasiafarming.Admin;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.ndg.alocasiafarming.R;

public class AdminAdapter extends FragmentPagerAdapter {
    private Context mContext;
    public AdminAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext=context;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position==0){
            return mContext.getString(R.string.admin_tumbuhan);
        } else if (position==1){
            return mContext.getString(R.string.admin_pupuk);
        } else {
            return mContext.getString(R.string.admin_penanaman);
        }
    }

    @Override
    public Fragment getItem(int position) {
        if (position==0){
            return new TumbuhanFragment();
        } else if (position==1){
            return new PupukFragment();
        } else {
            return new PenanamanFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
