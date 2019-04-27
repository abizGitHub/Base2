package com.tut.abiz.base.async;

import android.os.AsyncTask;
import android.util.Log;

import com.tut.abiz.base.NetServiceListener;
import com.tut.abiz.base.model.GeneralModel;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by abiz on 4/15/2019.
 */

public class GetListTask extends AsyncTask<String, Void, String> {

    String url, response = "";
    NetServiceListener netServiceListener;
    HttpClient httpClient;

    public GetListTask(NetServiceListener netServiceListener) {
        this.netServiceListener = netServiceListener;
        httpClient = new DefaultHttpClient();
    }

    public void setUrl(String url) {
        this.url = url;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.e("started>", url);
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            HttpEntity entity = GetData();
            response = getASCIIContentFromEntity(entity);
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            response = e.getMessage();
            cancel(true);
        }
        netServiceListener.onTextResponseReady(response);
        ArrayList<GeneralModel> gms = new ArrayList<GeneralModel>();
        try {
            JSONArray jsonArray = new JSONObject(response).getJSONArray("dataList");
            //int tableId = new JSONObject(response).getInt("tableId");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json1 = jsonArray.getJSONObject(i);
                GeneralModel generalModel = null;
                try {
                    generalModel = new GeneralModel(json1);
                } catch (JSONException e) {
                    Log.e("ERROR", e.getMessage(), e);
                    response = e.getMessage();
                }
                gms.add(generalModel);
            }
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage(), e);
            response = e.getMessage();
        }
        netServiceListener.onGeneralListReady(gms);
        Log.e("finished>", url);
        return response;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        netServiceListener.onFailure(response);
    }

    public HttpEntity GetData() throws IOException {
        HttpGet httpGet = new HttpGet(url);
        HttpResponse httpResponse = httpClient.execute(httpGet);
        HttpEntity httpEntity = httpResponse.getEntity();
        return httpEntity;
    }

    protected String getASCIIContentFromEntity(HttpEntity entity) {
        InputStream in = null;
        try {
            in = entity.getContent();
        } catch (IOException e) {
            Log.e("ERROR", e.getMessage(), e);
            response = e.getMessage();
        }
        StringBuffer out = new StringBuffer();
        int n = 1;
        while (n > 0) {
            byte[] b = new byte[4096];
            try {
                n = in.read(b);
            } catch (Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                response = e.getMessage();
            }
            if (n > 0) out.append(new String(b, 0, n));
        }
        return out.toString();
    }

}
