package com.tut.abiz.base.model;

import java.util.ArrayList;

/**
 * Created by abiz on 4/17/2019.
 */

public class Confiq {

    public static final String USERID = "userId";
    public static final String USERNAME = "userName";
    public static final String LASTMODELMAPID = "lastModelMapId";
    public static final String CLEARDB = "clearDB";
    public static final String HAVENEWCHANGE = "haveNewChange";
    public static String HASUSERPERMISSION = "hasUserPermission";
    public static String LASTIDS = "lastIds";
    public static String LASTMODELMAP = "lastModelMap";
    public static String LASTTABLESNAME = "lastTablesName";
    public static String TAGVISIBLITY = "tagVisiblity";

    String userName;
    Long userId;
    Boolean hasUserPermision;
    Boolean clearDB;

    ArrayList<Long> lastIds;
    ArrayList<String> lastTablesName;
    ArrayList<TagVisiblity> tagVisiblity;
    ArrayList<ModelMap> lastModelMap;
    ArrayList<Long> modelMap2Delete;
    Long lastModelMapId;
    Boolean haveNewChange;// if (tagVisiblity or lastModelMap)
    // send by server differ from local

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Boolean getHasUserPermision() {
        return hasUserPermision;
    }

    public void setHasUserPermision(Boolean hasUserPermision) {
        this.hasUserPermision = hasUserPermision;
    }

    public Boolean getClearDB() {
        return clearDB;
    }

    public void setClearDB(Boolean clearDB) {
        this.clearDB = clearDB;
    }

    public ArrayList<Long> getLastIds() {
        return lastIds;
    }

    public void setLastIds(ArrayList<Long> lastIds) {
        this.lastIds = lastIds;
    }

    public ArrayList<String> getLastTablesName() {
        return lastTablesName;
    }

    public void setLastTablesName(ArrayList<String> lastTablesName) {
        this.lastTablesName = lastTablesName;
    }

    public ArrayList<TagVisiblity> getTagVisiblity() {
        return tagVisiblity;
    }

    public void setTagVisiblity(ArrayList<TagVisiblity> tagVisiblity) {
        this.tagVisiblity = tagVisiblity;
    }

    public ArrayList<ModelMap> getLastModelMap() {
        return lastModelMap;
    }

    public void setLastModelMap(ArrayList<ModelMap> lastModelMap) {
        this.lastModelMap = lastModelMap;
    }

    public Long getLastModelMapId() {
        return lastModelMapId;
    }

    public void setLastModelMapId(Long lastModelMapId) {
        this.lastModelMapId = lastModelMapId;
    }

    public Boolean getHaveNewChange() {
        return haveNewChange;
    }

    public void setHaveNewChange(Boolean haveNewChange) {
        this.haveNewChange = haveNewChange;
    }

    public ArrayList<Long> getModelMap2Delete() {
        return modelMap2Delete;
    }

    public void setModelMap2Delete(ArrayList<Long> modelMap2Delete) {
        this.modelMap2Delete = modelMap2Delete;
    }
}
