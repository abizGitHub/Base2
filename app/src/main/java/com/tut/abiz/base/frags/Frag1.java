package com.tut.abiz.base.frags;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

public class Frag1 extends Fragment {

    ArrayList<FragmentPack> allFragmentPacks;
    ViewPager pager;
    GeneralPagerAdapter pagerAdapter;
    GeneralListAdapter generalListAdapter;
    ListPagerFrag pagerFrag;
    TabLayout tabs;
    private int navMenu;
    NetService netService;
    GeneralService service;
    BaseActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_paginator, container, false);
        activity = (BaseActivity) getActivity();
        navMenu = getActivity().getIntent().getExtras().getInt(Consts.NAVPAGER);
        service = new GeneralService(getActivity());
        netService = new NetService(null, getActivity());
        tabs = (TabLayout) view.findViewById(R.id.tabLayout);
        pager = (ViewPager) view.findViewById(R.id.pager);
        tabs.setupWithViewPager(pager);
        tabs.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorLightGray));
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                ((BaseActivity) getActivity()).setSelectedTable(pager.getCurrentItem() + 1);
                for (int i = 0; i < tabs.getTabCount(); i++) {
                    setIconSelected(i, i == pager.getCurrentItem());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        allFragmentPacks = service.getAllList();
        tabs.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabs.setTabMode(TabLayout.MODE_FIXED);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        pagerAdapter = new GeneralPagerAdapter(fm, allFragmentPacks);
        pager.setAdapter(pagerAdapter);
        setUpTabIcons();

        //View view = inflater.inflate(R.layout.frag_first, container, false);
        return view;
    }


    private void setUpTabIcons() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int ix = 0;
        View view;
        for (FragmentPack fragPack : allFragmentPacks) {
            if (ix == 0)
                view = inflater.inflate(R.layout.tab_icon, null);
            else
                view = inflater.inflate(R.layout.tab_icon_up, null);
            TextView text = (TextView) view.findViewById(R.id.tabText);
            text.setText(fragPack.getPageTitle());
            tabs.getTabAt(ix).setCustomView(view);
            ix++;
        }
        setIconSelected(0, true);
    }

    @Override
    public void onResume() {
        super.onResume();
        //doStarResume();
    }

    public void doStarResume() {
        String current = getActivity().getSharedPreferences(Consts.SHEREDPREF, MODE_PRIVATE).getString(Consts.CURRENTACTIVITY, "");
        getActivity().getSharedPreferences(Consts.SHEREDPREF, MODE_PRIVATE).edit().putString(Consts.LASTACTIVITY, current).apply();
        getActivity().getSharedPreferences(Consts.SHEREDPREF, MODE_PRIVATE).edit().putString(Consts.CURRENTACTIVITY, this.getClass().getName()).apply();
        boolean notify = activity.hasNewChange();
        int position = activity.getStaredPosition();
        if (notify && position > -1) {
            doStaredTasks();
            //ListPagerFrag pagerFrag = (ListPagerFrag) allFragmentPacks.get(getSelectedTable() - 1).getPagerFragment();
            if (getGeneralList() != null)
                getGeneralList().get(position).setStared(getIsStared());
            if (!getIsStared())
                if (activity.getNavMenu() == R.id.nav_staredList && ViewListItemActivity.class.getName().equals(activity.getLastActivityName())) {
                    getGeneralList().remove(position);
                    getGeneralTitles().remove(position);
                }
            if (getListAdapter() != null)
                getListAdapter().notifyDataSetChanged();
            activity.clearNewChange();
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
        pagerFrag = (ListPagerFrag) allFragmentPacks.get(((BaseActivity) getActivity()).getSelectedTable() - 1).getPagerFragment();
        generalListAdapter = (GeneralListAdapter) pagerFrag.getAdapter();
    }

    public boolean getIsStared() {
        return getActivity().getSharedPreferences(Consts.SHEREDPREF, MODE_PRIVATE).getBoolean(Consts.STARED, false);
    }

    public void setIsStared(boolean b) {
        getActivity().getSharedPreferences(Consts.SHEREDPREF, MODE_PRIVATE).edit().putBoolean(Consts.STARED, b).apply();
    }

    private void setIconSelected(int i, boolean selected) {
        //ImageView img = (ImageView) tabs.getTabAt(i).getCustomView().findViewById(R.id.tabIcon);
        TextView text = (TextView) tabs.getTabAt(i).getCustomView().findViewById(R.id.tabText);
        if (selected) {
            text.setTextColor(getResources().getColor(R.color.colorMagnet));
            tabs.getTabAt(i).getCustomView().setBackgroundColor(getResources().getColor(R.color.colorLightGray));
            //img.setBackground(getResources().getDrawable(R.drawable.));
            //img.setColorFilter(0xff00ffff, PorterDuff.Mode.MULTIPLY );
            //img.clearColorFilter();
        } else {
            text.setTextColor(getResources().getColor(R.color.colorLightGray));
            tabs.getTabAt(i).getCustomView().setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            //img.setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY );
            //img.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }
    }


}
