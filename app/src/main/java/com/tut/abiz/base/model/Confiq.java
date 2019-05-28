package com.tut.abiz.base.model;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by abiz on 4/17/2019.
 */

public class Confiq {

    public static final String USERID = "userId";
    public static final String USERNAME = "userName";
    public static final String LASTMODELMAPID = "lastModelMapId";
    public static final String CLEARDB = "clearDB";
    public static final String HAVENEWCHANGE = "haveNewChange";
    public static final String UPDATEGROUP = "updateGroup";
    public static String HASUSERPERMISSION = "hasUserPermission";
    public static String LASTIDS = "lastIds";
    public static String LASTMODELMAP = "lastModelMap";
    public static String LASTTABLESNAME = "lastTablesName";
    public static String TAGVISIBLITY = "tagVisiblity";
    public static String MODELMAP2DELETE = "modelMap2delete";
    public static final String LASTGROUPIDS = "lastGroupIds";
    public static final String WAIT4SERVER = "wait4Server";
    public static final String CONNECTPERIOD = "connectPeriod";
    public static String LASTMSGID = "lastMsgId";

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
    ArrayList<Integer> lastGroupIds;
    Boolean haveNewChange;// if (tagVisiblity or lastModelMap)
    // send by server differ from local
    Boolean updateGroup;
    Boolean sendDetail;
    Integer wait4Server;
    Integer connectPeriod;
    Long lastMsgId;

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
        if (tagVisiblity != null && tagVisiblity.size() > 0 &&
                tagVisiblity.get(0).getTableId() > tagVisiblity.get(1).getTableId())
            Collections.reverse(tagVisiblity);
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

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("userName :  " + userName + "\n");
        buffer.append("userId :  " + userId + "\n");
        buffer.append("lastModelMapId :  " + getLastModelMapId() + "\n");
        buffer.append("lastId-1 :  " + getLastIds().get(0) + "\n");
        buffer.append("lastId-2 :  " + getLastIds().get(1) + "\n");
        buffer.append("lastTableName-1 :  " + getLastTablesName().get(0) + "\n");
        buffer.append("lastTableName-2 :  " + getLastTablesName().get(1) + "\n");
        buffer.append("haveNewChange :  " + haveNewChange + "\n");
        buffer.append("hasUserPermision :  " + hasUserPermision + "\n");
        buffer.append("lastGroupIds :  " + lastGroupIds.get(0) + "," + lastGroupIds.get(1) + "\n");
        buffer.append("clearDB :  " + clearDB + "\n");
        return buffer.toString();
    }

    public Boolean getUpdateGroup() {
        return updateGroup;
    }

    public void setUpdateGroup(Boolean updateGroup) {
        this.updateGroup = updateGroup;
    }

    public ArrayList<Integer> getLastGroupIds() {
        return lastGroupIds;
    }

    public void setLastGroupIds(ArrayList<Integer> lastGroupIds) {
        this.lastGroupIds = lastGroupIds;
    }

    public Boolean getSendDetail() {
        return sendDetail;
    }

    public void setSendDetail(Boolean sendDetail) {
        this.sendDetail = sendDetail;
    }

    public Integer getWait4Server() {
        return wait4Server;
    }

    public void setWait4Server(Integer wait4Server) {
        this.wait4Server = wait4Server;
    }

    public Integer getConnectPeriod() {
        return connectPeriod;
    }

    public void setConnectPeriod(Integer connectPeriod) {
        this.connectPeriod = connectPeriod;
    }

    public Long getLastMsgId() {
        return lastMsgId;
    }

    public void setLastMsgId(Long lastMsgIsd) {
        this.lastMsgId = lastMsgIsd;
    }
}
