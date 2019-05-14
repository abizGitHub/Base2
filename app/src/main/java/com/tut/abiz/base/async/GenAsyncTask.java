package com.tut.abiz.base.async;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

/**
 * Created by abiz on 5/13/2019.
 */


public abstract class GenAsyncTask extends AsyncTask<String, Void, String> {

    private int delay;
    private ProgressDialog progressDialog;
    Handler handler;

    public GenAsyncTask(int delay, ProgressDialog progressDialog) {
        this.delay = delay;
        this.progressDialog = progressDialog;
        handler = new Handler();
    }


    @Override
    protected String doInBackground(String... strings) {
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                Log.e("-", "---- force stop ----");
                onPostExecute("canceled");
                cancel(true);
            }

        }, delay);
        return doInBack(strings);
    }

    public abstract String doInBack(String... strings);

    @Override
    protected void onPostExecute(String s) {
        if (progressDialog != null)
            progressDialog.dismiss();
    }
}
