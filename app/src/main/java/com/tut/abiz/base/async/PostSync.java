package com.tut.abiz.base.async;

import android.content.Context;
import android.util.Log;

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
 * Created by abiz on 5/13/2019.
 */

public class PostSync {

    String url;
    HttpClient httpClient;
    JSONObject sentJson;
    Context context;

    public static String GETCONGIQ = "getConfiq", GETLIST = "getList",
            GETGROUP = "getGroup", REGISTERUSER = "registerUser", EDITUSER = "editUser";

    public PostSync(Context context, String url, JSONObject sentJson) {
        httpClient = new DefaultHttpClient();
        this.context = context;
        this.url = url;
        this.sentJson = sentJson;
    }


    public JSONObject execute(String command) {
        JSONObject jsonObject = null;
        try {
            if (BaseActivity.offline)
                jsonObject = OffLineTestService.postData(url, sentJson);
            else
                jsonObject = postData();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Log.e("doInBackground", e.getMessage());
        }
        Log.e("finished>", url);
        return jsonObject;
    }

    public int extractRegResponse(JSONObject json) throws JSONException {
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

    public Confiq extractConfiq(JSONObject json) throws JSONException {
        if (json == null)
            return null;
        return JsonUtil.extractConfiq(json.getJSONObject("confiq"));
    }

    public ArrayList<GeneralModel> extractList(JSONObject json) throws JSONException {
        if (json == null)
            return null;
        return JsonUtil.extractGMs(json.getJSONArray("dataList"));
    }

    public ArrayList<Group> extractGroups(JSONObject json) throws JSONException {
        if (json == null)
            return null;
        return JsonUtil.extractGroups(json.getJSONArray("groupList"));
    }

}
