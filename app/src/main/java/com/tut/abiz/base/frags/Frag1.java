package com.tut.abiz.base.frags;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tut.abiz.base.Consts;
import com.tut.abiz.base.R;
import com.tut.abiz.base.acts.BaseActivity;
import com.tut.abiz.base.acts.ViewListItemActivity;
import com.tut.abiz.base.adapter.GeneralListAdapter;
import com.tut.abiz.base.adapter.GeneralPagerAdapter;
import com.tut.abiz.base.model.FragmentPack;
import com.tut.abiz.base.model.GeneralModel;
import com.tut.abiz.base.service.GeneralService;
import com.tut.abiz.base.service.NetService;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by abiz on 4/11/201
 */

public class Frag1 {

    ArrayList<FragmentPack> allFragmentPacks;
    ViewPager pager;
    GeneralPagerAdapter pagerAdapter;
    GeneralListAdapter generalListAdapter;
    ListPagerFrag pagerFrag;
    TabLayout tabs;
    NetService netService;
    GeneralService service;
    BaseActivity activity;
    View iconDown, iconUp;

    public void onCreateView(final BaseActivity activity) {
        //View view = inflater.inflate(R.layout.frag_paginator, container, false);
        //activity = (BaseActivity) getActivity();
        this.activity = activity;
        service = new GeneralService(activity);
        netService = new NetService(null, activity);
        tabs = (TabLayout) activity.findViewById(R.id.tabLayout);
        pager = (ViewPager) activity.findViewById(R.id.pager);
        tabs.setupWithViewPager(pager);
        tabs.setSelectedTabIndicatorColor(activity.getResources().getColor(R.color.colorLightGray));
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                activity.setSelectedTable(pager.getCurrentItem() + 1);
                for (int i = 0; i < tabs.getTabCount(); i++) {
                    setIconSelected(i, i == pager.getCurrentItem());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        tabs.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabs.setTabMode(TabLayout.MODE_FIXED);

        allFragmentPacks = service.getAllList();
        FragmentManager fm = activity.getSupportFragmentManager();
        pagerAdapter = new GeneralPagerAdapter(fm, allFragmentPacks);
        pager.setAdapter(pagerAdapter);
        setUpTabIcons();
    }


    private void setUpTabIcons() {
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int ix = 0;
        View view;
        iconDown = inflater.inflate(R.layout.tab_icon, null);
        iconUp = inflater.inflate(R.layout.tab_icon_up, null);
        for (FragmentPack fragPack : allFragmentPacks) {
            if (ix == 0)
                view = iconUp;
            else
                view = iconDown;
            TextView text = (TextView) view.findViewById(R.id.tabText);
            text.setText(fragPack.getPageTitle());
            tabs.getTabAt(ix).setCustomView(view);
            ix++;
        }
        setIconSelected(0, true);
    }

    public void onResume() {
        if (pagerFrag != null && getGeneralList() != null) {
            for (int ix = 0; ix < allFragmentPacks.size(); ix++) {
                ArrayList<GeneralModel> gList = service.getAllGeneralFrom(ix + 1);
                if (gList != null)

                    for (int i = 0; i < gList.size(); i++) {
                        boolean stared = gList.get(i).getStared();
                        try {
                            ((ListPagerFrag) allFragmentPacks.get(ix).getPagerFragment()).getGeneralList().get(i).setStared(stared);
                        } catch (Exception e) {
                            Log.e("www", "eee");
                        }
                    }
            }
        }
    }

    public ArrayAdapter getListAdapter() {
        return generalListAdapter;
    }

    public ArrayList<String> getGeneralTitles() {
        return pagerFrag.getTitles();
    }

    public ArrayList<GeneralModel> getGeneralList() {
        return pagerFrag.getGeneralList();
    }

    public void doStaredTasks() {
        pagerFrag = (ListPagerFrag) allFragmentPacks.get(activity.getSelectedTable() - 1).getPagerFragment();
        generalListAdapter = (GeneralListAdapter) pagerFrag.getAdapter();
    }

    public boolean getIsStared() {
        return activity.getSharedPreferences(Consts.SHEREDPREF, MODE_PRIVATE).getBoolean(Consts.STARED, false);
    }

    public void setIsStared(boolean b) {
        activity.getSharedPreferences(Consts.SHEREDPREF, MODE_PRIVATE).edit().putBoolean(Consts.STARED, b).apply();
    }

    private void setIconSelected(int i, boolean selected) {
        ImageView img = (ImageView) tabs.getTabAt(i).getCustomView().findViewById(R.id.tabIcon);
        TextView text = (TextView) tabs.getTabAt(i).getCustomView().findViewById(R.id.tabText);
        if (selected) {
            //text.setTextColor(activity.getResources().getColor(R.color.colorWhite));
            img.setBackgroundResource(R.drawable.baseline_account_balance_white_48);
        } else {
            img.setBackgroundResource(R.drawable.baseline_account_balance_black_48);
            if (i == 0)
                text.setTextColor(activity.getResources().getColor(R.color.colorRed));
            else
                text.setTextColor(activity.getResources().getColor(R.color.colorGreen));
        }
    }


}
