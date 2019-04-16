package com.tut.abiz.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.tut.abiz.base.acts.Act1;
import com.tut.abiz.base.acts.Act2;
import com.tut.abiz.base.acts.Act3;
import com.tut.abiz.base.acts.ListActivity;
import com.tut.abiz.base.acts.NetConnectionActivity;
import com.tut.abiz.base.acts.PagerActivity;
import com.tut.abiz.base.frags.Frag1;
import com.tut.abiz.base.model.GeneralModel;
import com.tut.abiz.base.model.TagVisiblity;
import com.tut.abiz.base.service.GeneralService;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by abiz on 4/14/2019.
 */

public class ActsInMenuAct extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    HashMap<Integer, Class<? extends AppCompatActivity>> actsMap;
    Integer selectedMenuAct;
    Fragment homeFragment;
    ActionBarDrawerToggle toggle;
    DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar;
    GeneralService service;
    ArrayList<GeneralModel> testList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acts_in_menu);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actsMap = new HashMap<>();
        actsMap.put(R.id.nav_frag1, Act1.class);
        actsMap.put(R.id.nav_frag2, Act2.class);
        actsMap.put(R.id.nav_frag3, Act3.class);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        homeFragment = new Frag1();
        service = new GeneralService();
        testList = service.getTestGeneralList();
        //Intent intent = new Intent(SecondActivity.this,Main2Activ.class);
        //startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        onSelectMenu(R.id.nav_frag1, getResources().getString(R.string.farg1));
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Toast.makeText(this, "back", Toast.LENGTH_SHORT).show();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (selectedMenuAct != R.id.nav_frag1) {
            onResume();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        onSelectMenu(item.getItemId(), item.getTitle().toString());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void onSelectMenu(int fragId, String title) {
        selectedMenuAct = fragId;
        Class<? extends AppCompatActivity> activity = actsMap.get(selectedMenuAct);
        if (selectedMenuAct == R.id.nav_frag1) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, homeFragment).commit();
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();
        } else if (selectedMenuAct == R.id.nav_list) {
            Intent intent = new Intent(ActsInMenuAct.this, ListActivity.class);
            intent.putExtra(Consts.GENERALLIST, testList);
            TagVisiblity visiblity = new TagVisiblity().
                    doBodyVisible(true).
                    doFooterLVisible(true).
                    doHeaderRVisible(true).
                    doStarVisible(true);
            intent.putExtra(Consts.VISIBLITY, visiblity);
            startActivity(intent);
        } else if (selectedMenuAct == R.id.nav_paginator) {
            Intent intent = new Intent(ActsInMenuAct.this, PagerActivity.class);
            intent.putExtra(Consts.NAVPAGER, R.id.nav_paginator);
            startActivity(intent);
        } else if (selectedMenuAct == R.id.nav_pagerList) {
            Intent intent = new Intent(ActsInMenuAct.this, PagerActivity.class);
            intent.putExtra(Consts.NAVPAGER, R.id.nav_pagerList);
            startActivity(intent);
        } else if (selectedMenuAct == R.id.nav_net) {
            Intent intent = new Intent(ActsInMenuAct.this, NetConnectionActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(ActsInMenuAct.this, activity);
            startActivity(intent);
        }
        //getSupportActionBar().setTitle(title);
    }

}

