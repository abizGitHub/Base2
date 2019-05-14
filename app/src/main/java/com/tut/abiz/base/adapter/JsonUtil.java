package com.tut.abiz.base.adapter;

import com.tut.abiz.base.Consts;
import com.tut.abiz.base.model.Confiq;
import com.tut.abiz.base.model.GeneralModel;
import com.tut.abiz.base.model.Group;
import com.tut.abiz.base.model.ModelMap;
import com.tut.abiz.base.model.TagVisiblity;
import com.tut.abiz.base.model.UserAccount;

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

    public static GeneralModel extractGM(JSONObject json) {
        GeneralModel generalModel = new GeneralModel();
        try {
            generalModel.setId(Long.valueOf(json.get(Consts.ID).toString()));
        } catch (JSONException e) {
        }
        try {
            generalModel.setTitle(json.get(GeneralModel.TITLE$).toString());
        } catch (JSONException e) {
        }
        try {
            generalModel.setHeaderL(json.get(GeneralModel.HEADERL$).toString());
        } catch (JSONException e) {
        }
        try {
            generalModel.setHeaderR(json.get(GeneralModel.HEADERR$).toString());
        } catch (JSONException e) {
        }
        try {
            generalModel.setBody(json.get(GeneralModel.BODY$).toString());
        } catch (JSONException e) {
        }
        try {
            generalModel.setFooterL(json.get(GeneralModel.FOOTERL$).toString());
        } catch (JSONException e) {
        }
        try {
            generalModel.setFooterR(json.get(GeneralModel.FOOTERR$).toString());
        } catch (JSONException e) {
        }
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
            confiq.setHasUserPermision(json.getBoolean(Confiq.HASUSERPERMISSION));
        } catch (Exception e) {
        }
        try {
            confiq.setUpdateGroup(json.getBoolean(Confiq.UPDATEGROUP));
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
            JSONArray jsonArray = json.getJSONArray(Confiq.TAGVISIBLITY);
            ArrayList<TagVisiblity> visiblities = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                visiblities.add(extractTagVisiblity((JSONObject) jsonArray.get(i)));
            }
            confiq.setTagVisiblity(visiblities);
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
        try {
            JSONArray jsonArray = json.getJSONArray(Confiq.MODELMAP2DELETE);
            ArrayList<Long> list = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                list.add(jsonArray.getLong(i));
            }
            confiq.setModelMap2Delete(list);
        } catch (Exception e) {
        }
        return confiq;
    }

    private static TagVisiblity extractTagVisiblity(JSONObject jsonObject) {
        TagVisiblity visiblity = null;
        try {
            visiblity = new TagVisiblity(jsonObject.getInt("TableId"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            visiblity.setBodyVisible(jsonObject.getBoolean(GeneralModel.BODY$));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            visiblity.setTitleVisible(jsonObject.getBoolean(GeneralModel.TITLE$));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            visiblity.setHeaderLVisible(jsonObject.getBoolean(GeneralModel.HEADERL$));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            visiblity.setHeaderRVisible(jsonObject.getBoolean(GeneralModel.HEADERR$));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            visiblity.setFooterLVisible(jsonObject.getBoolean(GeneralModel.FOOTERL$));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            visiblity.setFooterRVisible(jsonObject.getBoolean(GeneralModel.FOOTERR$));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            visiblity.setStarVisible(jsonObject.getBoolean(GeneralModel.STAR$));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            visiblity.setBodyString(jsonObject.getBoolean("S" + GeneralModel.BODY$));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            visiblity.setTitleString(jsonObject.getBoolean("S" + GeneralModel.TITLE$));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            visiblity.setHeaderLString(jsonObject.getBoolean("S" + GeneralModel.HEADERL$));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            visiblity.setHeaderRString(jsonObject.getBoolean("S" + GeneralModel.HEADERR$));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            visiblity.setFooterLString(jsonObject.getBoolean("S" + GeneralModel.FOOTERL$));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            visiblity.setFooterRString(jsonObject.getBoolean("S" + GeneralModel.FOOTERR$));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return visiblity;
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

    private static JSONObject parseModelMap(ModelMap mm) {
        JSONObject json = new JSONObject();
        try {
            json.put(ModelMap.INTVALUE, mm.getIntValue());
            json.put(ModelMap.STRINGVALUE, mm.getStringValue());
            json.put(ModelMap.COLUMNIX, mm.getColumnIx());
            json.put(Consts.ID, mm.getId());
            json.put(ModelMap.TABLEID, mm.getTableId());
        } catch (JSONException e) {
        }
        return json;
    }

    public static JSONObject parseTagVisiblity(TagVisiblity visiblity) {
        JSONObject json = new JSONObject();
        try {
            json.put("TableId", visiblity.getTableId());
        } catch (JSONException e) {
        }
        try {
            json.put(GeneralModel.BODY$, visiblity.isBodyVisible());
        } catch (JSONException e) {
        }
        try {
            json.put(GeneralModel.TITLE$, visiblity.isTitleVisible());
        } catch (JSONException e) {
        }
        try {
            json.put(GeneralModel.HEADERL$, visiblity.isHeaderLVisible());
        } catch (JSONException e) {
        }
        try {
            json.put(GeneralModel.HEADERR$, visiblity.isHeaderRVisible());
        } catch (JSONException e) {
        }
        try {
            json.put(GeneralModel.FOOTERL$, visiblity.isFooterLVisible());
        } catch (JSONException e) {
        }
        try {
            json.put(GeneralModel.FOOTERR$, visiblity.isFooterRVisible());
        } catch (JSONException e) {
        }
        try {
            json.put(GeneralModel.STAR$, visiblity.isStarVisible());
        } catch (JSONException e) {
        }
        try {
            json.put("S" + GeneralModel.BODY$, visiblity.isBodyString());
        } catch (JSONException e) {
        }
        try {
            json.put("S" + GeneralModel.TITLE$, visiblity.isTitleString());
        } catch (JSONException e) {
        }
        try {
            json.put("S" + GeneralModel.HEADERL$, visiblity.isHeaderLString());
        } catch (JSONException e) {
        }
        try {
            json.put("S" + GeneralModel.HEADERR$, visiblity.isHeaderRString());
        } catch (JSONException e) {
        }
        try {
            json.put("S" + GeneralModel.FOOTERL$, visiblity.isFooterLString());
        } catch (JSONException e) {
        }
        try {
            json.put("S" + GeneralModel.FOOTERR$, visiblity.isFooterRString());
        } catch (JSONException e) {
        }
        return json;
    }

    public static JSONObject parseConfiq(Confiq confiq) {
        JSONObject json = new JSONObject();
        try {
            json.put(Confiq.HASUSERPERMISSION, confiq.getHasUserPermision());
        } catch (JSONException e) {
        }
        try {
            json.put(Confiq.HAVENEWCHANGE, confiq.getHaveNewChange());
        } catch (JSONException e) {
        }
        try {
            json.put(Confiq.UPDATEGROUP, confiq.getUpdateGroup());
        } catch (JSONException e) {
        }
        try {
            if (confiq.getLastIds() != null)
                json.put(Confiq.LASTIDS, new JSONArray(confiq.getLastIds()));
        } catch (JSONException e) {
        }
        if (confiq.getLastIds() != null)
            try {
                json.put(Confiq.LASTIDS, new JSONArray(confiq.getLastIds()));
            } catch (JSONException e) {
            }
        if (confiq.getLastModelMap() != null)
            try {
                JSONArray array = new JSONArray();
                for (ModelMap mm : confiq.getLastModelMap()) {
                    array.put(parseModelMap(mm));
                }
                json.put(Confiq.LASTMODELMAP, array);
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
        try {
            JSONArray array = new JSONArray();
            if (confiq.getTagVisiblity() != null)
                for (TagVisiblity tv : confiq.getTagVisiblity()) {
                    array.put(parseTagVisiblity(tv));
                }
            json.put(Confiq.TAGVISIBLITY, array);
        } catch (JSONException e) {
        }
        return json;
    }

    public static JSONObject parseGroups(ArrayList<Integer> orderedGroups, ArrayList<Integer> registeredGroups) {
        JSONObject json = new JSONObject();
        try {
            json.put(Group.ORDERED$, new JSONArray(orderedGroups));
            json.put(Group.REGISTERED$, new JSONArray(registeredGroups));
        } catch (JSONException e) {
        }
        return json;
    }

    public static ArrayList<Integer> extractGroupIds(JSONObject json, String status) throws JSONException {
        ArrayList<Integer> list = new ArrayList<>();
        JSONArray array = json.getJSONArray(status);
        for (int i = 0; i < array.length(); i++) {
            list.add(array.getInt(i));
        }
        return list;
    }

    public static ArrayList<Group> extractGroups(JSONArray array) throws JSONException {
        ArrayList<Group> groups = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            groups.add(extractGr((JSONObject) array.get(i)));
        }
        return groups;
    }

    private static Group extractGr(JSONObject json) {
        Group group = new Group();
        try {
            group.setId(Integer.valueOf(json.get(Consts.ID).toString()));
        } catch (JSONException e) {
        }
        try {
            group.setName(json.get(Consts.NAME).toString());
        } catch (JSONException e) {
        }
        try {
            group.setStatus((Integer.valueOf(json.get(Consts.STATUS).toString())));
        } catch (JSONException e) {
        }
        return group;
    }


    public static UserAccount extractUserAccount(JSONObject reqJson) {
        UserAccount user = new UserAccount();
        try {
            user.setUserName(reqJson.get(Consts.USERNAME).toString());
        } catch (JSONException e) {
        }
        try {
            user.setPassword(reqJson.get(Consts.PASSWORD).toString());
        } catch (JSONException e) {
        }
        try {
            user.setEmail(reqJson.get(Consts.EMAIL).toString());
        } catch (JSONException e) {
        }
        try {
            user.setPhone(reqJson.get(Consts.PHONE).toString());
        } catch (JSONException e) {
        }
        return user;
    }

    public static JSONObject parseUserAccount(UserAccount userAccount) {
        JSONObject json = new JSONObject();
        try {
            json.put(Consts.USERNAME, userAccount.getUserName());
            json.put(Consts.PASSWORD, userAccount.getPassword());
            json.put(Consts.PHONE, userAccount.getPhone());
            json.put(Consts.EMAIL, userAccount.getEmail());
        } catch (JSONException e) {
        }
        return json;
    }

}
