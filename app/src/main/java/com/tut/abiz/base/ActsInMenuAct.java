package com.tut.abiz.base;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.tut.abiz.base.acts.Act1;
import com.tut.abiz.base.acts.Act2;
import com.tut.abiz.base.acts.Act3;
import com.tut.abiz.base.acts.ListActivity;
import com.tut.abiz.base.acts.NetConnectionActivity;
import com.tut.abiz.base.acts.PagerActivity;
import com.tut.abiz.base.acts.SchedulActivity;
import com.tut.abiz.base.acts.SearchActivity;
import com.tut.abiz.base.frags.Frag1;
import com.tut.abiz.base.model.GeneralModel;
import com.tut.abiz.base.model.TagVisiblity;
import com.tut.abiz.base.service.DbHelper;
import com.tut.abiz.base.service.GeneralService;
import com.tut.abiz.base.service.SchedulService;

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
        if (getIntent() == null || getIntent().getExtras() == null)
            SchedulActivity.phase = SchedulService.DOCONNECT;
        else
            startDialog(getIntent().getExtras().getString(SchedulService.DOCONNECT));
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
        service = new GeneralService(this);
        testList = service.getTestGeneralList();
        ImageButton searchButton = (ImageButton) findViewById(R.id.search_btn);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActsInMenuAct.this, SearchActivity.class);
                intent.putExtra(Consts.CURRENTPAGE, 1);
                intent.putExtra(Consts.NAVPAGER, selectedMenuAct);
                intent.putExtra(Consts.NAVTITLE, getResources().getString(R.string.dbView));
                startActivity(intent);
            }
        });

    }

    private void startDialog(String command) {
        //Toast.makeText(this, getIntent().getExtras().getString(SchedulService.DOCONNECT), Toast.LENGTH_SHORT).show();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);//, R.drawable.background_dialog);

        if (command.equals(SchedulService.CONNECTED))
            alertDialogBuilder.setMessage(getResources().getString(R.string.wait4UpdateMessage));
        else
            alertDialogBuilder.setMessage(getResources().getString(R.string.canNotconnectMessage));
/*

        alertDialogBuilder.setPositiveButton(getResources().getString(R.string.understood),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //Toast.makeText(ActsInMenuAct.this, "You clicked yes button", Toast.LENGTH_LONG).show();
                    }
                });
*/

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finish();
            }
        });

/*
        alertDialogBuilder.setNeutralButton("Neutral",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Toast.makeText(ActsInMenuAct.this, "You clicked Neutral button", Toast.LENGTH_LONG).show();
                    }
                });
*/

        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog.show();
            }
        }, 300);
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
            return;
        }
        Intent intent;
        if (selectedMenuAct == R.id.nav_list) {
            intent = new Intent(ActsInMenuAct.this, ListActivity.class);
            intent.putExtra(Consts.GENERALLIST, testList);
            TagVisiblity visiblity = new TagVisiblity(-1).
                    doBodyVisible(true).
                    doFooterLVisible(true).
                    doHeaderRVisible(true).
                    doStarVisible(true);
            intent.putExtra(Consts.VISIBLITY, visiblity);
        } else if (selectedMenuAct == R.id.nav_dbView) {
            intent = new Intent(ActsInMenuAct.this, ListActivity.class);
            TagVisiblity visiblity = new TagVisiblity(-1).
                    doBodyVisible(true).
                    doTitleVisible(true);
            intent.putExtra(Consts.VISIBLITY, visiblity);
        } else if (selectedMenuAct == R.id.nav_paginator) {
            intent = new Intent(ActsInMenuAct.this, PagerActivity.class);
        } else if (selectedMenuAct == R.id.nav_pagerList) {
            intent = new Intent(ActsInMenuAct.this, PagerActivity.class);
        } else if (selectedMenuAct == R.id.nav_netGetList) {
            intent = new Intent(ActsInMenuAct.this, PagerActivity.class);
        } else if (selectedMenuAct == R.id.nav_postList) {
            intent = new Intent(ActsInMenuAct.this, PagerActivity.class);
        } else if (selectedMenuAct == R.id.nav_staredList) {
            intent = new Intent(ActsInMenuAct.this, PagerActivity.class);
        } else if (selectedMenuAct == R.id.nav_net) {
            intent = new Intent(ActsInMenuAct.this, NetConnectionActivity.class);
        } else if (selectedMenuAct == R.id.nav_Group) {
            intent = new Intent(ActsInMenuAct.this, ListActivity.class);
        } else {
            intent = new Intent(ActsInMenuAct.this, activity);
        }
        intent.putExtra(Consts.CURRENTPAGE, 1);
        intent.putExtra(Consts.NAVPAGER, selectedMenuAct);
        intent.putExtra(Consts.NAVTITLE, title);
        startActivity(intent);
        //getSupportActionBar().setTitle(title);
    }

}

