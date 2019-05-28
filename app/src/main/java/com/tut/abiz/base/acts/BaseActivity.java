package com.tut.abiz.base.acts;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import com.tut.abiz.base.Consts;
import com.tut.abiz.base.R;
import com.tut.abiz.base.model.GeneralModel;
import com.tut.abiz.base.model.TagVisiblity;
import com.tut.abiz.base.service.DbHelper;
import com.tut.abiz.base.service.GeneralService;
import com.tut.abiz.base.service.NetService;
import com.tut.abiz.base.util.Utils;

import java.util.ArrayList;

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
    public static boolean offline = false;

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
                if (getNavMenu() == R.id.nav_Group)
                    getSharedPreferences(Consts.SHEREDPREF, MODE_PRIVATE).edit().putBoolean(Consts.MENUGROUPOPENED, true).apply();
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
            if (getListAdapter() != null)
                getListAdapter().notifyDataSetChanged();
            clearNewChange();
        }
    }

    protected abstract void doStaredTasks();

    protected abstract ArrayAdapter getListAdapter();

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

    public void setUserAccountEdited(boolean b) {
        getSharedPreferences(Consts.SHEREDPREF, MODE_PRIVATE).edit().putBoolean(Consts.USERACCOUNTEDITED, b).apply();
    }

    public void setGroupMenuOpened(boolean b) {
        getSharedPreferences(Consts.SHEREDPREF, MODE_PRIVATE).edit().putBoolean(Consts.MENUGROUPOPENED, b).apply();
    }

    public void setUserAccountRegitered(boolean b) {
        getSharedPreferences(Consts.SHEREDPREF, MODE_PRIVATE).edit().putBoolean(Consts.USERREGISTERED$, b).apply();
    }

    public boolean isUserAccountRegistered() {
        return getSharedPreferences(Consts.SHEREDPREF, MODE_PRIVATE).getBoolean(Consts.USERREGISTERED$, false);
    }

    public TagVisiblity getTagVisiblity(int tableIx) {
        return Utils.getTagVisFromPref(tableIx, getSharedPreferences(Consts.VISIBLITYPREF, MODE_PRIVATE),
                getSharedPreferences(Consts.ISSTRINGPREF, MODE_PRIVATE));
    }

    public void setTagVisiblity(int tableIx, TagVisiblity vis) {
        Utils.putVisInPref(vis, tableIx, getSharedPreferences(Consts.VISIBLITYPREF, MODE_PRIVATE),
                getSharedPreferences(Consts.ISSTRINGPREF, MODE_PRIVATE));
    }

    public void startDialog(String command) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(command);
        alertDialogBuilder.setNegativeButton(getResources().getString(R.string.understood), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
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
}
