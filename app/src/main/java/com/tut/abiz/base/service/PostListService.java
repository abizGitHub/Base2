package com.tut.abiz.base.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by abiz on 5/8/2019.
 */

public class PostListService extends IntentService {

    static int created = 0, dead = 0;
    int i;
    public static final String NOTIFICATION = "PostListService";
    public static final String UPDATEDCOUNT = "updated_Count";
    public static final String RESULT = "result";
    int CONNECTED = 1;
    int CANTCONNECT = -1;


    public PostListService() {
        super("-haha-");
        created++;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(i++);
        list.add(created);
        list.add(dead);
        try {
            Thread.sleep(77);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        publishResults(list, CONNECTED);
    }

    private void publishResults(ArrayList<Integer> list, int result) {
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra(UPDATEDCOUNT, list);
        intent.putExtra(RESULT, result);
        sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        dead++;
        super.onDestroy();
    }

}
