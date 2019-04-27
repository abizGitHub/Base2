package com.tut.abiz.base.adapter;

import com.tut.abiz.base.Consts;
import com.tut.abiz.base.model.Confiq;
import com.tut.abiz.base.model.GeneralModel;
import com.tut.abiz.base.model.ModelMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by abiz on 4/19/2019.
 */

public class JsonUtil {


    public static JSONObject parseGM(GeneralModel gm) {
        JSONObject json = new JSONObject();
        try {
            json.put(Consts.ID, gm.getId());
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

    public static GeneralModel extractGM(JSONObject json) throws JSONException {
        GeneralModel generalModel = new GeneralModel();
        generalModel.setId(Long.valueOf(json.get(Consts.ID).toString()));
        generalModel.setTitle(json.get(GeneralModel.TITLE$).toString());
        generalModel.setHeaderL(json.get(GeneralModel.HEADERL$).toString());
        generalModel.setHeaderR(json.get(GeneralModel.HEADERR$).toString());
        generalModel.setBody(json.get(GeneralModel.BODY$).toString());
        generalModel.setFooterL(json.get(GeneralModel.FOOTERL$).toString());
        generalModel.setFooterR(json.get(GeneralModel.FOOTERR$).toString());
        return generalModel;
    }

    public static ArrayList<GeneralModel> extractGMs(JSONArray array) throws JSONException {
        ArrayList<GeneralModel> generalModels = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            generalModels.add(extractGM((JSONObject) array.get(i)));
        }
        return generalModels;
    }

    public static Confiq extractConfiq(JSONObject json) {
        Confiq confiq = new Confiq();
        try {
            confiq.setClearDB(json.getBoolean(Confiq.CLEARDB));
        } catch (Exception e) {
        }
        try {
            confiq.setUserId(json.getLong(Confiq.USERID));
        } catch (Exception e) {
        }
        try {
            confiq.setUserName(json.getString(Confiq.USERNAME));
        } catch (Exception e) {
        }
        try {
            confiq.setHaveNewChange(json.getBoolean(Confiq.HAVENEWCHANGE));
        } catch (Exception e) {
        }
        try {
            confiq.setLastModelMapId(json.getLong(Confiq.LASTMODELMAPID));
        } catch (Exception e) {
        }
        try {
            JSONArray jsonArray = json.getJSONArray(Confiq.LASTMODELMAP);
            ArrayList<ModelMap> modelMaps = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                modelMaps.add(extractModelMap((JSONObject) jsonArray.get(i)));
            }
            confiq.setLastModelMap(modelMaps);
        } catch (Exception e) {
        }
        try {
            JSONArray jsonArray = json.getJSONArray(Confiq.LASTTABLESNAME);
            ArrayList<String> list = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                list.add(jsonArray.getString(i));
            }
            confiq.setLastTablesName(list);
        } catch (Exception e) {
        }
        try {
            JSONArray jsonArray = json.getJSONArray(Confiq.LASTIDS);
            ArrayList<Long> list = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                list.add(jsonArray.getLong(i));
            }
            confiq.setLastIds(list);
        } catch (Exception e) {
        }
        return confiq;
    }


    public static ModelMap extractModelMap(JSONObject json) throws JSONException {
        ModelMap modelMap = new ModelMap();
        modelMap.setColumnIx(json.getInt(ModelMap.COLUMNIX));
        modelMap.setId(json.getInt(Consts.ID));
        modelMap.setIntValue(json.getInt(ModelMap.INTVALUE));
        modelMap.setStringValue(json.getString(ModelMap.STRINGVALUE));
        modelMap.setTableId(json.getInt(ModelMap.TABLEID));
        return modelMap;
    }

    public static JSONObject parseConfiq(Confiq confiq) {
        JSONObject json = new JSONObject();
        try {
            json.put(Confiq.HASUSERPERMISSION, confiq.getHasUserPermision());
        } catch (JSONException e) {
        }
        try {
            if (confiq.getLastIds() != null)
                json.put(Confiq.LASTIDS, new JSONArray(confiq.getLastIds()));
        } catch (JSONException e) {
        }
        try {
            if (confiq.getLastModelMap() != null)
                json.put(Confiq.LASTMODELMAP, new JSONArray(confiq.getLastModelMap()));
        } catch (JSONException e) {
        }
        try {
            if (confiq.getLastTablesName() != null)
                json.put(Confiq.LASTTABLESNAME, new JSONArray(confiq.getLastTablesName()));
        } catch (JSONException e) {
        }
        try {
            json.put(Confiq.USERID, confiq.getUserId());
        } catch (JSONException e) {
        }
        try {
            json.put(Confiq.USERNAME, confiq.getUserName());
        } catch (JSONException e) {
        }
        try {
            json.put(Confiq.LASTMODELMAPID, confiq.getLastModelMapId());
        } catch (JSONException e) {
        }
        return json;
    }

}   
