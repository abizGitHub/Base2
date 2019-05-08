package com.tut.abiz.base.acts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.tut.abiz.base.ActsInMenuAct;
import com.tut.abiz.base.Consts;
import com.tut.abiz.base.R;
import com.tut.abiz.base.service.PostListService;
import com.tut.abiz.base.service.SchedulService;

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


}
