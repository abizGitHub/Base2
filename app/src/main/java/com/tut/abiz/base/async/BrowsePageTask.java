package com.tut.abiz.base.async;

import android.os.AsyncTask;
import android.util.Log;

import com.tut.abiz.base.NetServiceListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by abiz on 4/15/2019.
 */

public class BrowsePageTask extends AsyncTask<String, Void, String> {

    String url, response;
    NetServiceListener netServiceListener;

    public BrowsePageTask(NetServiceListener netServiceListener) {
        this.netServiceListener = netServiceListener;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    @Override
    protected void onPreExecute() {
        //log.e("started>", url);
    }

    @Override
    protected String doInBackground(String... strings) {
        HttpURLConnection urlConnection = null;
        try {
            URL urlx = new URL(url);
            urlConnection = (HttpURLConnection) urlx.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();
                response = stringBuilder.toString();
            } finally {
                urlConnection.disconnect();
            }
        } catch (Exception e) {
            //log.e("ERROR", e.getMessage(), e);
            response = e.getMessage();
            if (urlConnection != null)
                urlConnection.disconnect();
            cancel(true);
        }
        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        netServiceListener.onTextResponseReady(response);
        //log.e("finished>", url);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        netServiceListener.onFailure(response);
    }
}
