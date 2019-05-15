package com.tut.abiz.base.acts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.tut.abiz.base.R;
import com.tut.abiz.base.adapter.GeneralListAdapter;
import com.tut.abiz.base.adapter.GeneralPagerAdapter;
import com.tut.abiz.base.frags.ListPagerFrag;
import com.tut.abiz.base.model.FragmentPack;
import com.tut.abiz.base.model.GeneralModel;
import com.tut.abiz.base.service.GeneralService;
import com.tut.abiz.base.service.NetService;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_paginator);
        pager = (ViewPager) findViewById(R.id.pager);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                setSelectedTable(pager.getCurrentItem() + 1);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        if (R.id.nav_pagerList == getNavMenu())
            allFragmentPacks = service.getAllNetGetFrag();
        else if (R.id.nav_paginator == getNavMenu())
            allFragmentPacks = service.getAllFragPacks();
        else if (R.id.nav_netGetList == getNavMenu()) {
            Toast.makeText(this, "wait4List", Toast.LENGTH_SHORT).show();
            allFragmentPacks = netService.getAllNetList();
        } else if (R.id.nav_postList == getNavMenu()) {
            allFragmentPacks = service.getAllList();
        } else if (R.id.nav_staredList == getNavMenu()) {
            allFragmentPacks = service.getStaredList();
        }else if (R.id.nav_Group == getNavMenu()) {
            allFragmentPacks = service.getGroupPacks();
        }


        /** Getting fragment manager */
        FragmentManager fm = getSupportFragmentManager();

        /** Instantiating FragmentPagerAdapter */
        pagerAdapter = new GeneralPagerAdapter(fm, allFragmentPacks, this);
        /** Setting the pagerAdapter to the pager object */
        pager.setAdapter(pagerAdapter);
    }

    @Override
    protected void doStaredTasks() {
        pagerFrag = (ListPagerFrag) allFragmentPacks.get(getSelectedTable() - 1).getPagerFragment();
        generalListAdapter = (GeneralListAdapter) pagerFrag.getAdapter();
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
