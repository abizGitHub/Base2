package com.tut.abiz.base;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.tut.abiz.base.service.DbHelper;
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
    SharedPreferences pref, visiblityPref;
    Boolean isRunBefore = false;

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
        doPrefs();
        //Intent intent = new Intent(SecondActivity.this,Main2Activ.class);
        //startActivity(intent);
    }

    private void doPrefs() {
        pref = getSharedPreferences(Consts.SHEREDPREF, MODE_PRIVATE);
        visiblityPref = getSharedPreferences(Consts.VISIBLITYPREF, MODE_PRIVATE);
        isRunBefore = pref.getBoolean(Consts.ISRUNBEFORE, false);
        if (!isRunBefore) {
            DbHelper dbHelper = new DbHelper(this);
            dbHelper.initPageNames(dbHelper.getWritableDatabase());
            Toast.makeText(this, "firstRun", Toast.LENGTH_LONG).show();
            pref.edit().putBoolean(Consts.ISRUNBEFORE, true).apply();
            pref.edit().putString(Consts.TABLENAMES[0], "monaq").apply();
            pref.edit().putString(Consts.TABLENAMES[1], "mozay").apply();
            pref.edit().putInt(Consts.TABLECOUNT, 2).apply();

            visiblityPref.edit().putBoolean(GeneralModel.TITLE$ + 1, true).apply();
            visiblityPref.edit().putBoolean(GeneralModel.HEADERR$ + 1, true).apply();
            visiblityPref.edit().putBoolean(GeneralModel.FOOTERR$ + 1, true).apply();
            visiblityPref.edit().putBoolean(GeneralModel.FOOTERL$ + 1, false).apply();

            visiblityPref.edit().putBoolean(GeneralModel.TITLE$ + 2, true).apply();
            visiblityPref.edit().putBoolean(GeneralModel.HEADERR$ + 2, true).apply();
            visiblityPref.edit().putBoolean(GeneralModel.HEADERL$ + 2, true).apply();
            visiblityPref.edit().putBoolean(GeneralModel.FOOTERL$ + 2, true).apply();
            visiblityPref.edit().putBoolean(GeneralModel.FOOTERR$ + 2, false).apply();
        }
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
        } else if (selectedMenuAct == R.id.nav_netGetList) {
            Intent intent = new Intent(ActsInMenuAct.this, PagerActivity.class);
            intent.putExtra(Consts.NAVPAGER, R.id.nav_netGetList);
            startActivity(intent);
        } else if (selectedMenuAct == R.id.nav_postList) {
            Intent intent = new Intent(ActsInMenuAct.this, PagerActivity.class);
            intent.putExtra(Consts.NAVPAGER, R.id.nav_postList);
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

