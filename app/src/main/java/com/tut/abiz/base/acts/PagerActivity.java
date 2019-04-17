package com.tut.abiz.base.acts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.tut.abiz.base.Consts;
import com.tut.abiz.base.R;
import com.tut.abiz.base.adapter.GeneralPagerAdapter;
import com.tut.abiz.base.model.FragmentPack;
import com.tut.abiz.base.model.GeneralModel;
import com.tut.abiz.base.service.GeneralService;
import com.tut.abiz.base.service.NetService;

import java.util.ArrayList;


/**
 * Created by abiz on 4/14/2019.
 */

public class PagerActivity extends AppCompatActivity {
    Toolbar toolbar;
    NetService netService;
    ArrayList<FragmentPack> allFragmentPacks;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_paginator);
        GeneralService service = new GeneralService();
        netService = new NetService(null);
        if (R.id.nav_pagerList == getIntent().getExtras().getInt(Consts.NAVPAGER))
            allFragmentPacks = service.getAllNetGetFrag();
        else if (R.id.nav_paginator == getIntent().getExtras().getInt(Consts.NAVPAGER))
            allFragmentPacks = service.getAllFragPacks();
        else {
            Toast.makeText(this, "wait4List", Toast.LENGTH_SHORT).show();
            allFragmentPacks = netService.getAllNetList();
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ViewPager pager = (ViewPager) findViewById(R.id.pager);

        /** Getting fragment manager */
        FragmentManager fm = getSupportFragmentManager();

        /** Instantiating FragmentPagerAdapter */
        GeneralPagerAdapter pagerAdapter = new GeneralPagerAdapter(fm, allFragmentPacks);

        /** Setting the pagerAdapter to the pager object */
        pager.setAdapter(pagerAdapter);
    }


}
