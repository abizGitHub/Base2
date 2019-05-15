package com.tut.abiz.base.async;

import android.os.AsyncTask;
import android.util.Log;

import com.tut.abiz.base.Consts;
import com.tut.abiz.base.NetServiceListener;
import com.tut.abiz.base.acts.BaseActivity;
import com.tut.abiz.base.adapter.JsonUtil;
import com.tut.abiz.base.model.Confiq;
import com.tut.abiz.base.model.GeneralModel;
import com.tut.abiz.base.model.Group;
import com.tut.abiz.base.service.OffLineTestService;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by abiz on 4/18/2019.
 */

public class PostListTask extends AsyncTask<String, Void, String> {

    String url;
    NetServiceListener netService;
    HttpClient httpClient;
    JSONObject sentJson;

    public static String GETCONGIQ = "getConfiq", GETLIST = "getList",
            GETGROUP = "getGroup", REGISTERUSER = "registerUser", EDITUSER = "editUser";

    public PostListTask(NetServiceListener netService) {
        this.netService = netService;
        httpClient = new DefaultHttpClient();
    }

    public void setUrlAndMessage(String url, JSONObject sentJson) {
        this.url = url;
        this.sentJson = sentJson;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.e("started>", url);
    }

    @Override
    protected String doInBackground(String... strings) {
        try {
            JSONObject jsonObject;
            if (BaseActivity.offline)
                jsonObject = OffLineTestService.postData(url, sentJson);
            else
                jsonObject = postData();
            if (strings[0].equals(GETCONGIQ)) {
                netService.onConfiqReady(extractConfiq(jsonObject));
            } else if (strings[0].equals(GETLIST)) {
                netService.onGeneralListReady(extractList(jsonObject));
            } else if (strings[0].equals(GETGROUP)) {
                netService.onGroupListReady(extractGroups(jsonObject));
            } else if (strings[0].equals(EDITUSER)) {
                netService.onUpdateAccountReady(extractRegResponse(jsonObject));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Log.e("doInBackground", e.getMessage());
            if (strings[0].equals(GETCONGIQ)) {
                netService.onConfiqReady(null);
            } else if (strings[0].equals(GETLIST)) {
                netService.onGeneralListReady(null);
            } else if (strings[0].equals(GETGROUP)) {
                netService.onGroupListReady(null);
            } else if (strings[0].equals(REGISTERUSER)) {
                netService.onUpdateAccountReady(Consts.CANTREGISTERE);
            }
        }
        Log.e("finished>", url);
        return "done";
    }

    private int extractRegResponse(JSONObject json) throws JSONException {
        return json.getInt("response");
    }

    public JSONObject postData() throws IOException, JSONException {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("last", "e1ro");
        StringEntity entity = new StringEntity(sentJson.toString());
        httpPost.setEntity(entity);
        HttpResponse httpResponse = httpClient.execute(httpPost);
        String resp = readResponse(httpResponse);
        if (resp.trim().isEmpty())
            return new JSONObject();
        Log.e("response:",resp);
        return new JSONObject(resp);
    }

    public String readResponse(HttpResponse res) {
        InputStream is;
        String return_text = "";
        try {
            is = res.getEntity().getContent();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line = "";
            StringBuffer sb = new StringBuffer();
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            return_text = sb.toString();
        } catch (Exception e) {
            Log.e("started>", url);
        }
        return return_text;
    }

    private Confiq extractConfiq(JSONObject json) throws JSONException {
        if (json == null)
            return null;
        return JsonUtil.extractConfiq(json.getJSONObject("confiq"));
    }

    private ArrayList<GeneralModel> extractList(JSONObject json) throws JSONException {
        if (json == null)
            return null;
        return JsonUtil.extractGMs(json.getJSONArray("dataList"));
    }

    private ArrayList<Group> extractGroups(JSONObject json) throws JSONException {
        if (json == null)
            return null;
        return JsonUtil.extractGroups(json.getJSONArray("groupList"));
    }

}
