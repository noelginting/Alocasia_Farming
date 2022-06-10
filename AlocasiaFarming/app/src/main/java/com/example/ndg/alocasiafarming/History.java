package com.example.ndg.alocasiafarming;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class History extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.history,container,false);
        ViewPager viewPager= (ViewPager) view.findViewById(R.id.viewpager);
        HistoryAdapter historyAdapter= new HistoryAdapter(getChildFragmentManager());
        historyAdapter.addFrag(new HistoryFragment(),"History");
        historyAdapter.addFrag(new HistoryCatatanFragment(),"Catatan");
        TabLayout tabLayout=(TabLayout)view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(historyAdapter);
        return view;
    }
}
