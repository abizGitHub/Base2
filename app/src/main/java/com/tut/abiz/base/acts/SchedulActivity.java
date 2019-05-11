package com.tut.abiz.base.acts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.tut.abiz.base.ActsInMenuAct;
import com.tut.abiz.base.Consts;
import com.tut.abiz.base.R;
import com.tut.abiz.base.model.GeneralModel;
import com.tut.abiz.base.model.TagVisiblity;
import com.tut.abiz.base.service.DbHelper;
import com.tut.abiz.base.service.SchedulService;
import com.tut.abiz.base.util.Utils;

import java.util.ArrayList;

import static com.tut.abiz.base.service.SchedulService.CONNECTED;
import static com.tut.abiz.base.service.SchedulService.DOCONNECT;
import static com.tut.abiz.base.service.SchedulService.ISINBACK;


/**
 * Created by abiz on 5/6/2019.
 */

public class SchedulActivity extends AppCompatActivity {

    private View mContentView;
    public static String phase = "";
    Intent act;
    SharedPreferences pref, visiblityPref, isStringPref;
    Boolean isRunBefore = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_schedul);
        mContentView = findViewById(R.id.fullscreen_content);
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        doPrefs();
        registerReceiver(receiver, new IntentFilter(
                SchedulService.NOTIFICATION));

        Intent service = new Intent(this, SchedulService.class);
        startService(service);
        act = new Intent(SchedulActivity.this, ActsInMenuAct.class);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (phase.equals(ISINBACK)) {
            act.putExtra(Consts.LASTACTIVITY, SchedulActivity.class.getSimpleName());
            act.putExtra(DOCONNECT, CONNECTED);
            startActivity(act);
            phase = DOCONNECT;
        } else if (phase.equals(DOCONNECT)) {
            Toast.makeText(this, "move To Back", Toast.LENGTH_SHORT).show();
            phase = ISINBACK;
            moveTaskToBack(true);
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                /*int resultCode = bundle.getInt(SchedulService.RESULT);
                int result = bundle.getInt(PostListService.RESULT);*/
                if (phase.isEmpty()) {
                    act.putExtra(Consts.LASTACTIVITY, SchedulActivity.class.getSimpleName());
                    act.putExtra(DOCONNECT, bundle.getString(DOCONNECT));
                    startActivity(act);
                    phase = DOCONNECT;
                }
            }
        }

    };

    private void doPrefs() {
        pref = getSharedPreferences(Consts.SHEREDPREF, MODE_PRIVATE);
        visiblityPref = getSharedPreferences(Consts.VISIBLITYPREF, MODE_PRIVATE);
        isStringPref = getSharedPreferences(Consts.ISSTRINGPREF, MODE_PRIVATE);
        isRunBefore = pref.getBoolean(Consts.ISRUNBEFORE, false);
        if (!isRunBefore) {
            DbHelper dbHelper = new DbHelper(this);
            dbHelper.initPageNames(dbHelper.getWritableDatabase());
            Toast.makeText(this, "firstRun", Toast.LENGTH_LONG).show();
            pref.edit().putBoolean(Consts.ISRUNBEFORE, true).apply();
            pref.edit().putString(Consts.TABLENAMES[0], "monaq").apply();
            pref.edit().putString(Consts.TABLENAMES[1], "mozay").apply();
            pref.edit().putInt(Consts.TABLECOUNT, 2).apply();
            TagVisiblity vis1 = new TagVisiblity(1);
            vis1.doTitleVisible(true).doHeaderRVisible(true);
            vis1.doFooterRVisible(true).doFooterLVisible(true);
            vis1.setTitleString(true);
            vis1.setBodyString(true);
            vis1.setHeaderRString(true);
            vis1.setHeaderLString(true);
            vis1.setFooterRString(true);
            vis1.setFooterLString(true);
            Utils.putVisInPref(vis1, 1, visiblityPref, isStringPref);
            TagVisiblity vis2 = new TagVisiblity(2);
            vis2.doTitleVisible(true).doHeaderRVisible(true);
            vis2.doFooterRVisible(true).doFooterLVisible(true);
            vis2.setTitleString(true);
            vis2.setBodyString(true);
            vis2.setHeaderRString(true);
            vis2.setHeaderLString(true);
            vis2.setFooterRString(true);
            vis2.setFooterLString(true);
            Utils.putVisInPref(vis2, 2, visiblityPref, isStringPref);
            ArrayList<TagVisiblity> list = new ArrayList<>();
            list.add(vis1);
            list.add(vis2);
            dbHelper.insertTagVisiblitys(list);
        }
    }


    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

}
