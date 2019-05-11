package com.tut.abiz.base.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.widget.Toast;

import com.tut.abiz.base.ActsInMenuAct;
import com.tut.abiz.base.Consts;
import com.tut.abiz.base.R;
import com.tut.abiz.base.acts.SchedulActivity;

import java.util.ArrayList;
import java.util.Date;

import static com.tut.abiz.base.service.PostListService.RESULT;

/**
 * Created by abiz on 5/7/2019.
 */

public class SchedulService extends Service {

    public static final String NOTIFICATION = "SchedulService";

    NotificationCompat.BigTextStyle bigTextStyle;
    NotificationManager manager;
    Intent resultIntent;
    PendingIntent piIntent;
    NotificationCompat.Builder builder;
    public static String DOSTART = "do_Start";
    public static String DOCONNECT = "do_Connect";
    public static String CANTCONNECT = "cant_Connect";
    public static String CONNECTED = "connected";
    public static String ISINBACK = "is_In_Back";

    ConnectivityManager conMan;
    boolean connectedToNet;
    SharedPreferences pref;

    int i;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, " : started", Toast.LENGTH_SHORT).show();
        handler.post(periodicUpdate);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerReceiver(receiver, new IntentFilter(
                PostListService.NOTIFICATION));
        bigTextStyle = new android.support.v7.app.NotificationCompat.BigTextStyle();
        bigTextStyle.bigText("hello this is one").build();
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        resultIntent = new Intent(this, ActsInMenuAct.class);
        //resultIntent.putExtra(Consts.LASTACTIVITY, NOTIFICATION);
        piIntent = PendingIntent.getActivity(this, (int) new Date().getTime(), resultIntent, 0);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        builder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_menu_gallery)
                //.setContentTitle("START")
                .setStyle(bigTextStyle)
                .setContentIntent(piIntent);
        intent = new Intent(SchedulService.this, PostListService.class);
        pref = getApplicationContext().getSharedPreferences(Consts.SHEREDPREF, MODE_PRIVATE);
    }

    Handler handler = new Handler();
    Intent intent;

    private Runnable periodicUpdate = new Runnable() {
        @Override
        public void run() {
            if (intent.getExtras() != null)
                intent.getExtras().clear();
            intent.putExtra(RESULT, i++);
            startService(intent);
            handler.postDelayed(periodicUpdate, 1700);
        }
    };

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                int result = bundle.getInt(RESULT);
                nofitySchedulAct(result, isConnectedToNet());
                ArrayList<Integer> list = bundle.getIntegerArrayList(PostListService.UPDATEDCOUNT);
                if (SchedulActivity.phase.equals(ISINBACK))
                    notifyResults(list);
            }
        }

    };

    private void nofitySchedulAct(int result, boolean connectedToNet) {
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra(RESULT, result);
        intent.putExtra(DOCONNECT, connectedToNet ? CONNECTED : CANTCONNECT);
        sendBroadcast(intent);
    }


    private boolean isConnectedToNet() {
        conMan = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conMan.getActiveNetworkInfo();
        connectedToNet = (info != null && info.isAvailable() && info.isConnected());
        return connectedToNet;
    }

    private void notifyResults(ArrayList<Integer> updated) {
        String message = "";
        int tableCount = pref.getInt(Consts.TABLECOUNT, 2);
        if (updated.size() == tableCount) {
            for (int j = 0; j < tableCount; j++) {
                if (updated.get(j) > 0)
                    message += updated.get(j)
                            + getResources().getString(R.string.Case)
                            + " " + pref.getString(Consts.TABLENAMES[j], " ") + " " + getResources().getString(R.string.hasUpdated)
                            + "\n";
            }
            if (!message.isEmpty()) {
                bigTextStyle = new android.support.v7.app.NotificationCompat.BigTextStyle();
                bigTextStyle.bigText(message).build();
                builder.setStyle(bigTextStyle).setAutoCancel(true);
                //builder.setContentTitle("! content !");
                manager.notify(0, builder.build());
            }
        }
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }
}
