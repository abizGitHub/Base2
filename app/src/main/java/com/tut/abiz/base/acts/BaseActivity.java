package com.tut.abiz.base.acts;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.tut.abiz.base.Consts;
import com.tut.abiz.base.R;
import com.tut.abiz.base.adapter.GeneralListAdapter;
import com.tut.abiz.base.frags.ListPagerFrag;
import com.tut.abiz.base.model.FragmentPack;
import com.tut.abiz.base.model.GeneralModel;
import com.tut.abiz.base.model.TagVisiblity;
import com.tut.abiz.base.service.DbHelper;
import com.tut.abiz.base.service.GeneralService;
import com.tut.abiz.base.service.NetService;
import com.tut.abiz.base.util.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abiz on 5/1/2019.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DbHelper dbHelper;
    private int navMenu, selectedTable = 1;
    private String navTitle;
    NetService netService;
    GeneralService service;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DbHelper(this);
        navMenu = getIntent().getExtras().getInt(Consts.NAVPAGER);
        navTitle = getIntent().getExtras().getString(Consts.NAVTITLE);
        service = new GeneralService(this);
        netService = new NetService(null, this);
        setSelectedTable(getIntent().getExtras().getInt(Consts.CURRENTPAGE));
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getSupportActionBar().setTitle(navTitle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String current = getSharedPreferences(Consts.SHEREDPREF, MODE_PRIVATE).getString(Consts.CURRENTACTIVITY, "");
        getSharedPreferences(Consts.SHEREDPREF, MODE_PRIVATE).edit().putString(Consts.LASTACTIVITY, current).apply();
        getSharedPreferences(Consts.SHEREDPREF, MODE_PRIVATE).edit().putString(Consts.CURRENTACTIVITY, this.getClass().getName()).apply();
        boolean notify = hasNewChange();
        int position = getStaredPosition();
        if (notify && position > -1) {
            doStaredTasks();
            //ListPagerFrag pagerFrag = (ListPagerFrag) allFragmentPacks.get(getSelectedTable() - 1).getPagerFragment();
            if (getGeneralList() != null)
                getGeneralList().get(position).setStared(getIsStared());
            if (!getIsStared())
                if (getNavMenu() == R.id.nav_staredList && ViewListItemActivity.class.getName().equals(getLastActivityName())) {
                    getGeneralList().remove(position);
                    getGeneralTitles().remove(position);
                }
            if (getGeneralListAdapter() != null)
                getGeneralListAdapter().notifyDataSetChanged();
            clearNewChange();
        }
    }

    protected abstract void doStaredTasks();

    protected abstract GeneralListAdapter getGeneralListAdapter();

    protected abstract ArrayList<String> getGeneralTitles();

    protected abstract ArrayList<GeneralModel> getGeneralList();

    public DbHelper getDbHelper() {
        return dbHelper;
    }

    public abstract void onStarChanged(int position, boolean checked);

    public int getNavMenu() {
        return navMenu;
    }

    public String getNavTitle() {
        return navTitle;
    }

    public void setNavTitle(String navTitle) {
        this.navTitle = navTitle;
    }

    public int getSelectedTable() {
        Log.e(" - page : ", selectedTable + "");
        return selectedTable;
    }

    public void setSelectedTable(int selectedTable) {
        this.selectedTable = selectedTable;
    }

    public int getTablesCount() {
        return getSharedPreferences(Consts.SHEREDPREF, MODE_PRIVATE).getInt(Consts.TABLECOUNT, 0);
    }

    public String getTableName(int ix) {
        return getSharedPreferences(Consts.SHEREDPREF, MODE_PRIVATE).getString(Consts.TABLENAMES[ix], " - 1234567890 -");
    }

    public String getLastActivityName() {
        return getSharedPreferences(Consts.SHEREDPREF, MODE_PRIVATE).getString(Consts.LASTACTIVITY, "");
    }

    public int getStaredPosition() {
        return getSharedPreferences(Consts.SHEREDPREF, MODE_PRIVATE).getInt(Consts.STAREDPOSITION, -1);
    }

    public void clearNewChange() {
        getSharedPreferences(Consts.SHEREDPREF, MODE_PRIVATE).edit().remove(Consts.HASNEWCHANGE).apply();
    }

    public void clearStaredPosition() {
        getSharedPreferences(Consts.SHEREDPREF, MODE_PRIVATE).edit().remove(Consts.STAREDPOSITION).apply();
    }

    public void setStaredPosition(int position) {
        getSharedPreferences(Consts.SHEREDPREF, MODE_PRIVATE).edit().putInt(Consts.STAREDPOSITION, position).apply();
    }

    public void setNewChange() {
        getSharedPreferences(Consts.SHEREDPREF, MODE_PRIVATE).edit().putBoolean(Consts.HASNEWCHANGE, true).apply();
    }

    public boolean hasNewChange() {
        return getSharedPreferences(Consts.SHEREDPREF, MODE_PRIVATE).getBoolean(Consts.HASNEWCHANGE, false);
    }

    public boolean getIsStared() {
        return getSharedPreferences(Consts.SHEREDPREF, MODE_PRIVATE).getBoolean(Consts.STARED, false);
    }

    public void setIsStared(boolean b) {
        getSharedPreferences(Consts.SHEREDPREF, MODE_PRIVATE).edit().putBoolean(Consts.STARED, b).apply();
    }

    public TagVisiblity getTagVisiblity(int tableIx) {
        return Utils.getTagVisFromPref(tableIx, getSharedPreferences(Consts.VISIBLITYPREF, MODE_PRIVATE),
                getSharedPreferences(Consts.ISSTRINGPREF, MODE_PRIVATE));
    }

    public void setTagVisiblity(int tableIx, TagVisiblity vis) {
        netService.putVisInPref(vis, tableIx);
    }

}
