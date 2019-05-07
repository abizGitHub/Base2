package com.tut.abiz.base.acts;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.tut.abiz.base.ActsInMenuAct;
import com.tut.abiz.base.R;
import com.tut.abiz.base.service.NetService;


/**
 * Created by abiz on 5/6/2019.
 */

public class SchedulActivity extends AppCompatActivity {

    private View mContentView;
    public static String DOSTART = "do_Start";
    public static String DOCONNECT = "do_Connect";
    public static String CANTCONNECT = "cant_Connect";
    public static String CONNECTED = "connected";
    String phase;
    private SchedulTask task;
    int try_ = 0;
    NotificationCompat.BigTextStyle bigTextStyle;
    NotificationManager manager;
    Intent resultIntent;
    PendingIntent piIntent;
    NotificationCompat.Builder builder;
    NetService netService;
    ConnectivityManager conMan;
    boolean connectedToNet;

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

        netService = new NetService(null, this);

        bigTextStyle = new android.support.v7.app.NotificationCompat.BigTextStyle();
        bigTextStyle.bigText("hello this is one").build();
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        resultIntent = new Intent(this, SchedulActivity.class);
        piIntent = PendingIntent.getActivity(this, 666, resultIntent, 0);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        builder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("START")
                //.setContentText(" <content TEXT> ")
                .setStyle(bigTextStyle)
                .setContentIntent(piIntent);

        phase = DOSTART;
        task = new SchedulTask();
        task.execute(DOSTART);
    }

    private void doNotification(String note) {
        task = new SchedulTask();
        task.execute(DOCONNECT);
        if (isConnectedToNet()) {
            builder.setContentTitle(note).setContentText("<content-text><" + note + ">");
            manager.notify(0, builder.build());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (phase.equals(DOCONNECT)) {
            Toast.makeText(this, "move To Back", Toast.LENGTH_SHORT).show();
            moveTaskToBack(true);
        }
    }

    private boolean isConnectedToNet() {
        conMan = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conMan.getActiveNetworkInfo();
        connectedToNet = (info != null && info.isAvailable() && info.isConnected());
        return connectedToNet;
    }

//----------------------------------------------------------------------------------------------------------------------------------------------------------

    class SchedulTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            String connect = connect();
            if (phase.equals(DOSTART)) {
                Intent intent = new Intent(SchedulActivity.this, ActsInMenuAct.class);
                intent.putExtra(DOCONNECT, isConnectedToNet() ? CONNECTED : CANTCONNECT);
                startActivity(intent);
                phase = DOCONNECT;
            }
            doNotification(connect + ">" + try_++);
            return null;
        }

        private String connect() {
            Log.e(">", "connecting ...");
            netService.allNetList();
            /*try {
                Thread.sleep(111);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            return CONNECTED;
        }

    }

}
