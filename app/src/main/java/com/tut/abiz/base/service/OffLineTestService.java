package com.tut.abiz.base.service;

import android.util.Log;

import com.tut.abiz.base.adapter.JsonUtil;
import com.tut.abiz.base.model.Confiq;
import com.tut.abiz.base.model.GeneralModel;
import com.tut.abiz.base.model.ModelMap;
import com.tut.abiz.base.model.TagVisiblity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by abiz on 5/5/2019.
 */

public class OffLineTestService {

    static HashMap<Integer, HashMap<Long, GeneralModel>> generalModelHash;
    static HashMap<Integer, ModelMap> modelMapHash;
    static ArrayList<Long> lastIds;

    static {
        generalModelHash = new HashMap<>();
        modelMapHash = new HashMap<>();
        lastIds = new ArrayList<>();
        lastIds.add(0L);
        lastIds.add(0L);
        generalModelHash.put(1, new HashMap<Long, GeneralModel>());
        generalModelHash.put(2, new HashMap<Long, GeneralModel>());
        for (int tableId = 1; tableId < 3; tableId++) {
            for (int j = 0; j < 150; j++) {
                ModelMap map = new ModelMap();
                map.setId(j + 1 + 1000 * tableId);
                map.setColumnIx(GeneralModel.HEADER_R);
                map.setIntValue(j + 1);
                map.setTableId(tableId);
                map.setStringValue(tableId + GeneralModel.HEADERR$ + "-iv-" + j);
                modelMapHash.put(map.getId(), map);
            }
            for (int j = 0; j < 150; j++) {
                ModelMap map = new ModelMap();
                map.setId(j + 10000 + 1000 * tableId);
                map.setColumnIx(GeneralModel.HEADER_L);
                map.setIntValue(j + 1);
                map.setTableId(tableId);
                map.setStringValue(tableId + GeneralModel.HEADERL$ + "-iv-" + j);
                modelMapHash.put(map.getId(), map);
            }
            for (int j = 0; j < 102; j++) {
                ModelMap map = new ModelMap();
                map.setId(j + 50000 + 1000 * tableId);
                map.setColumnIx(GeneralModel.FOOTER_L);
                map.setIntValue(j + 1);
                map.setTableId(tableId);
                map.setStringValue(tableId + GeneralModel.FOOTERL$ + "-iv-" + j);
                modelMapHash.put(map.getId(), map);
            }

            for (long i = 1; i < 100; i++) {
                GeneralModel generalModel = new GeneralModel();
                generalModel.fillMock();
                generalModel.setId(i);
                generalModel.setTitle(tableId + "-title-" + i);
                generalModel.setHeaderR(new Random().nextInt(100) + "");
                generalModel.setHeaderL(new Random().nextInt(100) + "");
                generalModel.setFooterL(new Random().nextInt(100) + "");
                generalModelHash.get(tableId).put(i, generalModel);
            }
            lastIds.set(tableId - 1, 141L);
        }

    }

    public static JSONObject postData(String url, JSONObject sentJson) {
        Confiq reqCnf = JsonUtil.extractConfiq(sentJson);
        if (url.contains("Confiq"))
            return returnMockConfiq(reqCnf);
        else {
            String[] split = url.split("/");
            int tableIx = Integer.parseInt(split[split.length - 2]);
            long id = Long.parseLong(split[split.length - 1]);
            return returnMockGMs(tableIx, id);
        }
    }

    private static JSONObject returnMockGMs(int tableIx, Long id) {
        JSONObject jsonObject = new JSONObject();
        JSONArray array = new JSONArray();
        ArrayList<GeneralModel> list = getGeneralListAfter(tableIx, id);
        for (GeneralModel model : list) {
            array.put(parseGM(model));
        }
        try {
            jsonObject.put("dataList", array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    private static ArrayList<GeneralModel> getGeneralListAfter(int tableIx, Long id) {
        ArrayList<GeneralModel> list = new ArrayList<>();
        for (Long id_ : generalModelHash.get(tableIx).keySet()) {
            list.add(generalModelHash.get(tableIx).get(id_));
        }
        return list;
    }

    private static void addNewGM(int tableIx, Long id) {
        for (long i = id + 1; i < id + 4; i++) {
            GeneralModel generalModel = new GeneralModel();
            generalModel.fillMock();
            generalModel.setId(i);
            generalModel.setTitle((tableIx) + "-title-" + i);
            generalModel.setHeaderR(new Random().nextInt(100) + "");
            generalModel.setHeaderL(new Random().nextInt(100) + "");
            generalModel.setFooterL(new Random().nextInt(100) + "");
            generalModelHash.get(tableIx).put(i, generalModel);
        }
        lastIds.set(tableIx - 1, id + 4);
    }

    private static JSONObject returnMockConfiq(Confiq reqCnf) {
        JSONObject jsonResponse = new JSONObject();
       // Log.e("req-ids> ", reqCnf.getLastIds().get(0) + "," + reqCnf.getLastIds().get(1));
       // Log.e("last-ids> ", lastIds.get(0) + "," + lastIds.get(1));
        try {
            Confiq confiq = getConfig();
            confiq.setLastIds(lastIds);
            confiq.setLastModelMap(getModelMapAfter(reqCnf.getLastModelMapId()));
            confiq.setModelMap2Delete(getModelMapAfter2Delete(reqCnf.getLastModelMapId()));
            JSONObject c = JsonUtil.parseConfiq(confiq);
            jsonResponse.put("confiq", c);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (new Random().nextBoolean())
            addNewGM(1, reqCnf.getLastIds().get(0));
        if (new Random().nextBoolean())
            addNewGM(2, reqCnf.getLastIds().get(1));
        return jsonResponse;
    }

    private static ArrayList<Long> getModelMapAfter2Delete(Long i) {
        ArrayList<Long> list = new ArrayList<>();
        list.add(i + 333L);
        list.add(i + 666L);
        return list;
    }

    private static ArrayList<ModelMap> getModelMapAfter(Long i) {
        ArrayList<ModelMap> list = new ArrayList<>();
        for (Integer id : modelMapHash.keySet()) {
            if (modelMapHash.get(id).getId() > i)
                list.add(modelMapHash.get(id));
        }
        return list;
    }

    private static Confiq getConfig() {
        Confiq confiq = new Confiq();
        confiq.setUserName("newSeen");
        confiq.setHaveNewChange(true);
        ArrayList<String> names = new ArrayList<>();
        names.add("one-");
        names.add("two-");
        confiq.setLastTablesName(names);
        ArrayList<TagVisiblity> tagV = new ArrayList<>();
        tagV.add(getTagVisiblity(1));
        tagV.add(getTagVisiblity(2));
        confiq.setTagVisiblity(tagV);
        return confiq;
    }

    private static TagVisiblity getTagVisiblity(int i) {
        TagVisiblity tv = new TagVisiblity(i);
        tv.setTitleVisible(true);
        tv.setFooterLVisible(true);
        tv.setHeaderRVisible(true);
        tv.setTitleString(true);
        tv.setBodyString(true);
        tv.setHeaderRString(false);
        tv.setHeaderLString(false);
        tv.setFooterLString(false);
        tv.doStarVisible(true);
        return tv;
    }


    public static JSONObject parseGM(GeneralModel gm) {
        JSONObject json = new JSONObject();
        try {
            json.put("ID", gm.getId());
            json.put(GeneralModel.BODY$, gm.getBody());
            json.put(GeneralModel.TITLE$, gm.getTitle());
            json.put(GeneralModel.HEADERL$, gm.getHeaderL());
            json.put(GeneralModel.HEADERR$, gm.getHeaderR());
            json.put(GeneralModel.FOOTERL$, gm.getFooterL());
            json.put(GeneralModel.FOOTERR$, gm.getFooterR());
            //json.put("stared", gm.getStared());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

}
