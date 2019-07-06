package com.tut.abiz.base.acts;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tut.abiz.base.R;
import com.tut.abiz.base.adapter.GeneralListAdapter;
import com.tut.abiz.base.adapter.GeneralPagerAdapter;
import com.tut.abiz.base.frags.ListPagerFrag;
import com.tut.abiz.base.model.FragmentPack;
import com.tut.abiz.base.model.GeneralModel;

import java.util.ArrayList;


/**
 * Created by abiz on 4/14/2019.
 */

public class PagerActivity extends BaseActivity {

    ArrayList<FragmentPack> allFragmentPacks;
    ViewPager pager;
    GeneralPagerAdapter pagerAdapter;
    GeneralListAdapter generalListAdapter;
    ListPagerFrag pagerFrag;
    TabLayout tabs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_paginator);
        tabs = (TabLayout) findViewById(R.id.tabLayout);
        pager = (ViewPager) findViewById(R.id.pager);
        tabs.setupWithViewPager(pager);
        tabs.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorLightGray));
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                setSelectedTable(pager.getCurrentItem() + 1);
                for (int i = 0; i < tabs.getTabCount(); i++) {
                    setIconSelected(i, i == pager.getCurrentItem());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        /*if (R.id.nav_pagerList == getNavMenu())
            allFragmentPacks = service.getAllNetGetFrag();
        else if (R.id.nav_paginator == getNavMenu())
            allFragmentPacks = service.getAllFragPacks();
        else if (R.id.nav_netGetList == getNavMenu()) {
            Toast.makeText(this, "wait4List", Toast.LENGTH_SHORT).show();
            allFragmentPacks = netService.getAllNetList();
        } else if (R.id.nav_postList == getNavMenu()) {
            allFragmentPacks = service.getAllList();
        } else */if (R.id.nav_staredList == getNavMenu()) {
            allFragmentPacks = service.getStaredList();
        } else if (R.id.nav_Group == getNavMenu()) {
            allFragmentPacks = service.getGroupPacks();
        }
        tabs.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabs.setTabMode(TabLayout.MODE_FIXED);

        /** Getting fragment manager */
        FragmentManager fm = getSupportFragmentManager();

        /** Instantiating FragmentPagerAdapter */
        pagerAdapter = new GeneralPagerAdapter(fm, allFragmentPacks);
        /** Setting the pagerAdapter to the pager object */
        pager.setAdapter(pagerAdapter);
        setUpTabIcons();
    }

    private void setIconSelected(int i, boolean selected) {
        ImageView img = (ImageView) tabs.getTabAt(i).getCustomView().findViewById(R.id.tabIcon);
        TextView text = (TextView) tabs.getTabAt(i).getCustomView().findViewById(R.id.tabText);
        if (selected) {
            //text.setTextColor(getResources().getColor(R.color.colorWhite));
            img.setBackgroundResource(R.drawable.baseline_account_balance_white_48);
        } else {
            img.setBackgroundResource(R.drawable.baseline_account_balance_black_48);
            if (i == 0)
                text.setTextColor(getResources().getColor(R.color.colorRed));
            else
                text.setTextColor(getResources().getColor(R.color.colorGreen));
        }
    }

    @Override
    protected void doStaredTasks() {
        pagerFrag = (ListPagerFrag) allFragmentPacks.get(getSelectedTable() - 1).getPagerFragment();
        generalListAdapter = (GeneralListAdapter) pagerFrag.getAdapter();
    }

    private void setUpTabIcons() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int ix = 0;
        View view;
        for (FragmentPack fragPack : allFragmentPacks) {
            if (ix == 0)
                view = inflater.inflate(R.layout.tab_icon_up, null);
            else
                view = inflater.inflate(R.layout.tab_icon, null);
            TextView text = (TextView) view.findViewById(R.id.tabText);
            text.setText(fragPack.getPageTitle());
            tabs.getTabAt(ix).setCustomView(view);
            ix++;
        }
        setIconSelected(0, true);
    }

    @Override
    protected ArrayAdapter getListAdapter() {
        return generalListAdapter;
    }

    @Override
    protected ArrayList<String> getGeneralTitles() {
        return pagerFrag.getTitles();
    }

    @Override
    protected ArrayList<GeneralModel> getGeneralList() {
        return pagerFrag.getGeneralList();
    }

    @Override
    public void onStarChanged(int position, boolean checked) {

    }

}
