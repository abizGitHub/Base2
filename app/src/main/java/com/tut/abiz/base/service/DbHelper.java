package com.tut.abiz.base.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.tut.abiz.base.Consts;
import com.tut.abiz.base.model.Confiq;
import com.tut.abiz.base.model.GeneralModel;
import com.tut.abiz.base.model.Group;
import com.tut.abiz.base.model.ModelMap;
import com.tut.abiz.base.model.TagVisiblity;
import com.tut.abiz.base.model.UserAccount;
import com.tut.abiz.base.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Abiz
 * Date: 4/6/18
 * Time: 11:34 PM
 * To change this template use File | Settings | File Templates.
 */

public class DbHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "abizDB";
    public static final String PAGESTITLE = "page_title";
    public static final String GENERALTABLEPREFIX = "tble00";
    public static final String MODELMAP = "modelMap";
    public static final String USERCONFIQ = "userConfiq";
    public static final String TAGVISIBLITY = "tagVisiblity";
    public static final String GROUP = "group0";
    public static final String USERACCOUNT = "userAccountT";


    private HashMap<Integer, ArrayList<ModelMap>> modelMapListHash;
    private HashMap<Integer, HashMap<Integer, HashMap<Integer, String>>> modelMapHashs; // tableIX > columnIx > intVal > string

    public DbHelper(Context context) {
        super(context, DBNAME, null, 1);
        modelMapListHash = new HashMap<>();
        modelMapHashs = new HashMap<>();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + USERACCOUNT + " (ID INTEGER,USERNAME TEXT,PASSWORD TEXT,EMAIL TEXT,PHONE TEXT)");
        db.execSQL("CREATE TABLE " + PAGESTITLE + " (TABLE_ID INTEGER PRIMARY KEY,NAME TEXT)");
        db.execSQL("CREATE TABLE " + GROUP + " (ID INTEGER,TABLE_ID INTEGER,NAME TEXT,STATUS INTEGER)");
        db.execSQL("CREATE TABLE " + MODELMAP +
                " (ID INTEGER PRIMARY KEY,TABLE_ID INTEGER,COLUMN_IX INTEGER NOT NULL,INT_VALUE INTEGER NOT NULL,STRING_VALUE TEXT)");
        //TITLE = 1;HEADER_R = 2;HEADER_L = 3;BODY = 4;FOOTER_R = 5;FOOTER_L = 6;
        //ID INTEGER PRIMARY KEY AUTOINCREMENT
        db.execSQL("CREATE TABLE " + GENERALTABLEPREFIX + 1 +
                " (ID INTEGER PRIMARY KEY,TITLE TEXT,HEADER_R TEXT,HEADER_L TEXT,BODY TEXT,FOOTER_R TEXT,FOOTER_L TEXT,STARED INTEGER)");
        db.execSQL("CREATE TABLE " + GENERALTABLEPREFIX + 2 +
                " (ID INTEGER PRIMARY KEY,TITLE TEXT,HEADER_R TEXT,HEADER_L TEXT,BODY TEXT,FOOTER_R TEXT,FOOTER_L TEXT,STARED INTEGER)");
        db.execSQL("CREATE TABLE " + GENERALTABLEPREFIX + 3 +
                " (ID INTEGER PRIMARY KEY,TITLE TEXT,HEADER_R TEXT,HEADER_L TEXT,BODY TEXT,FOOTER_R TEXT,FOOTER_L TEXT,STARED INTEGER)");
        db.execSQL("CREATE TABLE " + GENERALTABLEPREFIX + 4 +
                " (ID INTEGER PRIMARY KEY,TITLE TEXT,HEADER_R TEXT,HEADER_L TEXT,BODY TEXT,FOOTER_R TEXT,FOOTER_L TEXT,STARED INTEGER)");
        db.execSQL("CREATE TABLE " + GENERALTABLEPREFIX + 5 +
                " (ID INTEGER PRIMARY KEY,TITLE TEXT,HEADER_R TEXT,HEADER_L TEXT,BODY TEXT,FOOTER_R TEXT,FOOTER_L TEXT,STARED INTEGER)");
        db.execSQL("CREATE TABLE " + GENERALTABLEPREFIX + 6 +
                " (ID INTEGER PRIMARY KEY,TITLE TEXT,HEADER_R TEXT,HEADER_L TEXT,BODY TEXT,FOOTER_R TEXT,FOOTER_L TEXT,STARED INTEGER)");
        db.execSQL("CREATE TABLE " + USERCONFIQ + " (USERID INTEGER PRIMARY KEY,USERNAME TEXT,HASUSERPERMISSION INTEGER)");

        db.execSQL("CREATE TABLE " + TAGVISIBLITY +
                " (TABLE_ID INTEGER PRIMARY KEY," + // 0
                "V" + GeneralModel.TITLE + " INTEGER," + // 1
                "V" + GeneralModel.HEADER_R + " INTEGER," + // 2
                "V" + GeneralModel.HEADER_L + " INTEGER," + // 3
                "V" + GeneralModel.BODY + " INTEGER," + // 4
                "V" + GeneralModel.FOOTER_R + " INTEGER," + // 5
                "V" + GeneralModel.FOOTER_L + " INTEGER," + // 6
                "V" + GeneralModel.STAR + " INTEGER," +
                "S" + GeneralModel.TITLE + " INTEGER," + // 8
                "S" + GeneralModel.HEADER_R + " INTEGER," + // 9
                "S" + GeneralModel.HEADER_L + " INTEGER," + // 10
                "S" + GeneralModel.BODY + " INTEGER," + // 11
                "S" + GeneralModel.FOOTER_R + " INTEGER," + // 12
                "S" + GeneralModel.FOOTER_L + " INTEGER)"  // 13
        );
        Log.e("db", "--------------- DB created -------------------");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i2) {
        Log.e("db", "--------------- DB upgraded -------------------");
    }

    public boolean initPageNames(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put("TABLE_ID", Consts.MONAQ);
        values.put("NAME", "monaq");
        long result = db.insert(PAGESTITLE, null, values);
        values = new ContentValues();
        values.put("TABLE_ID", Consts.MOZAY);
        values.put("NAME", "mozay");
        result += db.insert(PAGESTITLE, null, values);
        values = new ContentValues();
        values.put(Confiq.USERID, Consts.NEWUSERID);
        long randSerial1 = 7 * ((long) new Random().nextInt(99999));
        long randSerial2 = 13 * ((long) new Random().nextInt(9999));
        values.put(Confiq.USERNAME, Consts.NEWUSERNAME + "-" + randSerial1 + "-" + randSerial2);
        values.put(Confiq.HASUSERPERMISSION, 0);
        result += db.insert(USERCONFIQ, null, values);
        db.close();
        return result != -3;
    }

    public boolean insertGM(GeneralModel gm, int tableId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TITLE", gm.getTitle());
        values.put("BODY", gm.getBody());
        values.put("FOOTER_L", gm.getFooterL());
        values.put("FOOTER_R", gm.getFooterR());
        values.put("HEADER_L", gm.getHeaderL());
        values.put("HEADER_R", gm.getHeaderR());
        values.put("ID", gm.getId());
        values.put("STARED", false);
        long result = db.insert(GENERALTABLEPREFIX + tableId, null, values);
        db.close();
        return result != -1;
    }

    public boolean insertGMs(ArrayList<GeneralModel> gms, int tableId) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = -1;
        ContentValues values = new ContentValues();
        for (GeneralModel gm : gms) {
            values.put("TITLE", gm.getTitle());
            values.put("BODY", gm.getBody());
            values.put("FOOTER_L", gm.getFooterL());
            values.put("FOOTER_R", gm.getFooterR());
            values.put("HEADER_L", gm.getHeaderL());
            values.put("HEADER_R", gm.getHeaderR());
            values.put("ID", gm.getId());
            values.put("STARED", false);
            result = db.insert(GENERALTABLEPREFIX + tableId, null, values);
            values.clear();
        }
        db.close();
        return result != -1;
    }

    public boolean changeStateOf(Integer tableId, Long id, Boolean state) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("STARED", state.booleanValue());
        long result = db.update(GENERALTABLEPREFIX + tableId, values, "ID=?", new String[]{id.toString()});
        db.close();
        return result != -1;
    }

    public void deleteAllRecOfTable(int ix) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE  FROM " + GENERALTABLEPREFIX + ix);
        db.close();
    }

    public void deleteUnStaredRecOfTable(int ix) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE  FROM " + GENERALTABLEPREFIX + ix + " WHERE STARED <> 1");
        db.close();
    }

    public ArrayList<String> getAllPagesTitle() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + PAGESTITLE, null);
        ArrayList<String> list = new ArrayList<String>();
        if (cursor != null && cursor.getCount() > 0)
            while (cursor.moveToNext()) {
                list.add(cursor.getString(0));
            }
        db.close();
        return list;
    }

    public HashMap<Integer, HashMap<Integer, String>> getColumnHash(int tableIx) {
        HashMap<Integer, HashMap<Integer, String>> columnHash = modelMapHashs.get(tableIx);
        if (columnHash == null || columnHash.isEmpty()) {
            columnHash = new HashMap<>();
            ArrayList<ModelMap> modelMaps = getModelMap(tableIx);
            for (ModelMap mm : modelMaps) {
                HashMap<Integer, String> intVal = columnHash.get(mm.getColumnIx());
                if (intVal == null) {
                    columnHash.put(mm.getColumnIx(), new HashMap<Integer, String>());
                    intVal = columnHash.get(mm.getColumnIx());
                }
                intVal.put(mm.getIntValue(), mm.getStringValue());
            }
            modelMapHashs.put(tableIx, columnHash);
        }
        return columnHash;
    }

    public ArrayList<ModelMap> getModelMap(int ix) {
        ArrayList<ModelMap> list = modelMapListHash.get(ix);
        if (list == null) {
            list = new ArrayList<>();
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor res = db.rawQuery("SELECT * FROM " + MODELMAP + " WHERE TABLE_ID =" + ix, null);
            if (res != null && res.getCount() > 0) {
                while (res.moveToNext()) {
                    //(TABLE_ID INTEGER,COLUMN_IX INTEGER,INT_VALUE INTEGER,STRING_VALUE TEXT)
                    ModelMap modelMap = new ModelMap();
                    modelMap.setId(res.getInt(0));
                    modelMap.setTableId(Integer.parseInt(res.getString(1))); // 1 2
                    modelMap.setColumnIx(Integer.parseInt(res.getString(2))); // 2 3
                    modelMap.setIntValue(Integer.valueOf(res.getString(3))); // 102 103
                    modelMap.setStringValue(res.getString(4));
                    list.add(modelMap);
                }
                modelMapListHash.put(ix, list);
            }
            db.close();
        }
        return list;
    }

    public ArrayList<Integer> getModelMapColumnIx(int ix, int columnIx, String inWord) {
        ArrayList<Integer> list = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT INT_VALUE FROM " + MODELMAP + " WHERE TABLE_ID =" + ix + " AND COLUMN_IX =" + columnIx;
        query += " AND STRING_VALUE LIKE '%" + inWord + "%'";
        Cursor res = db.rawQuery(query, null);
        if (res != null && res.getCount() > 0) {
            while (res.moveToNext()) {
                //(TABLE_ID INTEGER,COLUMN_IX INTEGER,INT_VALUE INTEGER,STRING_VALUE TEXT)
                list.add(res.getInt(0));
            }
        }
        db.close();
        return list;
    }

    public ArrayList<GeneralModel> getAllGeneralFrom(int ix) {
        HashMap<Integer, HashMap<Integer, String>> modelMapHash = getColumnHash(ix);
        TagVisiblity tag = getTagVisiblity(ix);
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<GeneralModel> generalModels = new ArrayList<GeneralModel>();
        Cursor res = db.rawQuery("SELECT * FROM " + GENERALTABLEPREFIX + ix, null);
        if (res != null && res.getCount() > 0)
            while (res.moveToNext()) {
                //TITLE = 1;HEADER_R = 2;HEADER_L = 3;BODY = 4;FOOTER_R = 5;FOOTER_L = 6;
                GeneralModel generalModel = new GeneralModel();
                generalModel.setId(res.getLong(0));
                generalModel.setTitle(res.getString(GeneralModel.TITLE));
                generalModel.setHeaderR(res.getString(GeneralModel.HEADER_R));
                generalModel.setHeaderL(res.getString(GeneralModel.HEADER_L));
                generalModel.setBody(res.getString(GeneralModel.BODY));
                generalModel.setFooterR(res.getString(GeneralModel.FOOTER_R));
                generalModel.setFooterL(res.getString(GeneralModel.FOOTER_L));
                generalModel.setStared(res.getInt(GeneralModel.STAR) == 1);
                generalModel.applyModelMap(modelMapHash, tag);
                generalModels.add(generalModel);
            }
        db.close();
        return generalModels;
    }

    public ArrayList<GeneralModel> getAllStaredGeneralFrom(int ix) {
        TagVisiblity tag = getTagVisiblity(ix);
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<GeneralModel> generalModels = new ArrayList<GeneralModel>();
        Cursor res = db.rawQuery("SELECT * FROM " + GENERALTABLEPREFIX + ix + " WHERE STARED ='1'", null);
        if (res != null && res.getCount() > 0)
            while (res.moveToNext()) {
                //TITLE = 1;HEADER_R = 2;HEADER_L = 3;BODY = 4;FOOTER_R = 5;FOOTER_L = 6;
                GeneralModel generalModel = new GeneralModel();
                generalModel.setId(res.getLong(0));
                generalModel.setTitle(res.getString(GeneralModel.TITLE));
                generalModel.setHeaderR(res.getString(GeneralModel.HEADER_R));
                generalModel.setHeaderL(res.getString(GeneralModel.HEADER_L));
                generalModel.setBody(res.getString(GeneralModel.BODY));
                generalModel.setFooterR(res.getString(GeneralModel.FOOTER_R));
                generalModel.setFooterL(res.getString(GeneralModel.FOOTER_L));
                generalModel.setStared(res.getInt(GeneralModel.STAR) == 1);
                generalModel.applyModelMap(getColumnHash(ix), tag);
                generalModels.add(generalModel);
            }
        db.close();
        return generalModels;
    }

    public void clearDB() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + PAGESTITLE);
        db.execSQL("DELETE FROM " + MODELMAP);
        for (int i = 1; i < 6; i++) {
            deleteAllRecOfTable(i);
        }
        initPageNames(db);
    }

    public Confiq getConfiq() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + USERCONFIQ, null);
        Confiq confiq = new Confiq();
        if (cursor != null && cursor.getCount() > 0)
            if (cursor.moveToNext()) {
                confiq.setUserId(cursor.getLong(0));
                confiq.setUserName(cursor.getString(1));
                confiq.setHasUserPermision(cursor.getInt(2) == 1);
            }
        cursor.close();
        cursor = db.rawQuery("SELECT MAX(ID) FROM " + MODELMAP, null);
        if (cursor != null && cursor.getCount() > 0 && cursor.moveToNext())
            confiq.setLastModelMapId(cursor.getLong(0));
        else
            confiq.setLastModelMapId(0L);
        cursor.close();
        cursor = db.rawQuery("SELECT name FROM " + PAGESTITLE, null);
        ArrayList<String> names = new ArrayList<>();
        while (cursor != null && cursor.moveToNext()) {
            names.add(cursor.getString(0));
        }
        cursor.close();
        confiq.setLastTablesName(names);
        ArrayList<Long> lastIds = new ArrayList<>();
        for (int i = 1; i < names.size() + 1; i++) {
            cursor = db.rawQuery("SELECT MAX(ID) FROM " + GENERALTABLEPREFIX + i, null);
            if (cursor != null && cursor.moveToNext()) {
                lastIds.add(cursor.getLong(0));
            }
        }
        cursor.close();
        confiq.setLastIds(lastIds);

        ArrayList<Integer> lastGroupIds = new ArrayList<>();
        for (int i = 1; i < names.size() + 1; i++) {
            cursor = db.rawQuery("SELECT MAX(ID) FROM " + GROUP + " WHERE TABLE_ID='" + i + "'", null);
            if (cursor != null && cursor.getCount() > 0 && cursor.moveToNext())
                lastGroupIds.add(cursor.getInt(0));
            else
                lastGroupIds.add(0);
        }
        cursor.close();
        confiq.setLastGroupIds(lastGroupIds);

        ArrayList<TagVisiblity> visiblityList = new ArrayList<>();
        for (int i = 0; i < lastIds.size(); i++) {
            visiblityList.add(getTagVisiblity(i + 1));
        }
        confiq.setTagVisiblity(visiblityList);
        return confiq;
    }

    public boolean insertModelMaps(ArrayList<ModelMap> modelMaps) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = -1;
        ContentValues values = new ContentValues();
        for (ModelMap gm : modelMaps) {
            values.put("ID", gm.getId());
            values.put("INT_VALUE", gm.getIntValue());
            values.put("STRING_VALUE", gm.getStringValue());
            values.put("COLUMN_IX", gm.getColumnIx());
            values.put("TABLE_ID", gm.getTableId());
            result = db.insert(MODELMAP, null, values);
            values.clear();
        }
        db.close();
        modelMapHashs = new HashMap<>();
        modelMapListHash = new HashMap<>();
        return result != -1;
    }

    public Long getLastModelMapId() {
        return null;
    }

    public TagVisiblity getTagVisiblity(int tableId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TAGVISIBLITY + " WHERE TABLE_ID=" + tableId;
        Cursor res = db.rawQuery(query, null);
        TagVisiblity visiblity = new TagVisiblity(tableId);
        if (res != null && res.getCount() > 0)
            while (res.moveToNext()) {
                //visiblity.setTableId(tableId);
                visiblity.setTitleVisible(res.getInt(GeneralModel.TITLE) == 1);
                visiblity.setHeaderRVisible(res.getInt(GeneralModel.HEADER_R) == 1);
                visiblity.setHeaderLVisible(res.getInt(GeneralModel.HEADER_L) == 1);
                visiblity.setBodyVisible(res.getInt(GeneralModel.BODY) == 1);
                visiblity.setFooterLVisible(res.getInt(GeneralModel.FOOTER_L) == 1);
                visiblity.setFooterRVisible(res.getInt(GeneralModel.FOOTER_R) == 1);
                visiblity.setStarVisible(res.getInt(GeneralModel.STAR) == 1);

                visiblity.setTitleString(res.getInt(GeneralModel.TITLE + 7));
                visiblity.setHeaderRString(res.getInt(GeneralModel.HEADER_R + 7));
                visiblity.setHeaderLString(res.getInt(GeneralModel.HEADER_L + 7));
                visiblity.setBodyString(res.getInt(GeneralModel.BODY + 7));
                visiblity.setFooterRString(res.getInt(GeneralModel.FOOTER_R + 7));
                visiblity.setFooterLString(res.getInt(GeneralModel.FOOTER_L + 7));
            }
        db.close();
        return visiblity;
    }

    public boolean insertTagVisiblitys(ArrayList<TagVisiblity> tagVisiblity) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = -1;
        ContentValues values = new ContentValues();
        for (TagVisiblity vis : tagVisiblity) {
            values.put("TABLE_ID", vis.getTableId());
            values.put("V" + GeneralModel.STAR, vis.isStarVisible() ? 1 : 0);
            values.put("S" + GeneralModel.TITLE, vis.getTitleString());
            values.put("V" + GeneralModel.TITLE, vis.isTitleVisible() ? 1 : 0);
            values.put("S" + GeneralModel.BODY, vis.getBodyString());
            values.put("V" + GeneralModel.BODY, vis.isBodyVisible() ? 1 : 0);
            values.put("S" + GeneralModel.HEADER_R, vis.getHeaderRString());
            values.put("V" + GeneralModel.HEADER_R, vis.isHeaderRVisible() ? 1 : 0);
            values.put("S" + GeneralModel.HEADER_L, vis.getHeaderLString());
            values.put("V" + GeneralModel.HEADER_L, vis.isHeaderLVisible() ? 1 : 0);
            values.put("S" + GeneralModel.FOOTER_R, vis.getFooterRString());
            values.put("V" + GeneralModel.FOOTER_R, vis.isFooterRVisible() ? 1 : 0);
            values.put("S" + GeneralModel.FOOTER_L, vis.getFooterLString());
            values.put("V" + GeneralModel.FOOTER_L, vis.isFooterLVisible() ? 1 : 0);
            result = db.insert(TAGVISIBLITY, null, values);
            values.clear();
        }
        db.close();
        return result != -1;
    }

    public void updateTableNames(ArrayList<String> lastTablesName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + PAGESTITLE);
        int ix = 0;
        for (String tblName : lastTablesName) {
            ContentValues values = new ContentValues();
            values.put("TABLE_ID", ++ix);
            values.put("NAME", tblName);
            db.insert(PAGESTITLE, null, values);
        }
        db.close();
    }

    public void updateUserId(Long userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Confiq.USERID, userId);
        values.put("USERID", userId);
        db.update(USERCONFIQ, values, null, null);
        db.close();
    }

    public void updateTagVisiblity(ArrayList<TagVisiblity> tagVisiblity) {

    }

    public void updateModelMap(ArrayList<ModelMap> lastModelMap) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (ModelMap modelMap : lastModelMap) {
            db.execSQL("DELETE FROM " + MODELMAP + " WHERE ID=" + modelMap.getId());
        }
        db.close();
        insertModelMaps(lastModelMap);
    }

    public void deleteModelMap(ArrayList<Long> mm) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (Long id : mm) {
            db.execSQL("DELETE FROM " + MODELMAP + " WHERE ID=" + id);
        }
        db.close();
        modelMapHashs = new HashMap<>();
        modelMapListHash = new HashMap<>();
    }

    public ArrayList<GeneralModel> getAllGeneralFrom(int ix, String inWord, TagVisiblity vis) {
        HashMap<Integer, HashMap<Integer, String>> modelMapHash = getColumnHash(ix);
        ArrayList<GeneralModel> generalModels = new ArrayList<GeneralModel>();
        TagVisiblity tag = getTagVisiblity(ix);
        String query = "SELECT * FROM " + GENERALTABLEPREFIX + ix + " WHERE 0 = 1 ";
        //TITLE TEXT,HEADER_R TEXT,HEADER_L TEXT,BODY TEXT,FOOTER_R TEXT,FOOTER_L TEXT
        ArrayList<Integer> modelMapIntVal;
        Utils utils = Utils.o().startQuery();
        if (vis.getTitleString() == 0)
            query += " OR " + GeneralModel.TITLE$ + " LIKE '%" + inWord + "%'";
        else {
            modelMapIntVal = getModelMapColumnIx(ix, vis.getTitleString(), inWord);
            utils.addQuery(GeneralModel.TITLE$, modelMapIntVal);
        }
        if (vis.getHeaderLString() == 0)
            query += " OR " + GeneralModel.HEADERL$ + " LIKE '%" + inWord + "%'";
        else {
            modelMapIntVal = getModelMapColumnIx(ix, vis.getHeaderLString(), inWord);
            utils.addQuery(GeneralModel.HEADERL$, modelMapIntVal);
        }
        if (vis.getHeaderRString() == 0)
            query += " OR " + GeneralModel.HEADERR$ + " LIKE '%" + inWord + "%'";
        else {
            modelMapIntVal = getModelMapColumnIx(ix, vis.getHeaderRString(), inWord);
            utils.addQuery(GeneralModel.HEADERR$, modelMapIntVal);
        }
        if (vis.getBodyString() == 0)
            query += " OR " + GeneralModel.BODY$ + " LIKE '%" + inWord + "%'";
        else {
            modelMapIntVal = getModelMapColumnIx(ix, vis.getBodyString(), inWord);
            utils.addQuery(GeneralModel.BODY$, modelMapIntVal);
        }
        if (vis.getFooterLString() == 0)
            query += " OR " + GeneralModel.FOOTERL$ + " LIKE '%" + inWord + "%'";
        else {
            modelMapIntVal = getModelMapColumnIx(ix, vis.getFooterLString(), inWord);
            utils.addQuery(GeneralModel.FOOTERL$, modelMapIntVal);
        }
        if (vis.getFooterRString() == 0)
            query += " OR " + GeneralModel.FOOTERR$ + " LIKE '%" + inWord + "%'";
        else {
            modelMapIntVal = getModelMapColumnIx(ix, vis.getFooterRString(), inWord);
            utils.addQuery(GeneralModel.FOOTERR$, modelMapIntVal);
        }
        if (utils.getQuery().contains("IN"))
            query += " OR " + utils.getQuery();
        Log.e("search:", query);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery(query, null);
        if (res != null && res.getCount() > 0)
            while (res.moveToNext()) {
                GeneralModel generalModel = new GeneralModel();
                generalModel.setId(res.getLong(0));
                generalModel.setTitle(res.getString(GeneralModel.TITLE));
                generalModel.setHeaderR(res.getString(GeneralModel.HEADER_R));
                generalModel.setHeaderL(res.getString(GeneralModel.HEADER_L));
                generalModel.setBody(res.getString(GeneralModel.BODY));
                generalModel.setFooterR(res.getString(GeneralModel.FOOTER_R));
                generalModel.setFooterL(res.getString(GeneralModel.FOOTER_L));
                generalModel.setStared(res.getInt(GeneralModel.STAR) == 1);
                generalModel.applyModelMap(modelMapHash, tag);
                generalModels.add(generalModel);
            }
        db.close();
        return generalModels;
    }


    public void clearTagVisiblitys() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE  FROM " + TAGVISIBLITY);
        db.close();
    }

    public void clearTable(String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE  FROM " + tableName);
        db.close();
    }

    public boolean updateGroups(ArrayList<Group> grs, int tableIx) {
        clearTable(GROUP, "WHERE TABLE_ID=" + tableIx);
        SQLiteDatabase db = this.getWritableDatabase();
        long result = -1;
        ContentValues values = new ContentValues();
        for (Group gr : grs) {
            values.put("ID", gr.getId());
            values.put("NAME", gr.getName());
            values.put("TABLE_ID", gr.getTableId());
            values.put(Consts.STATUS, gr.getStatus());
            result = db.insert(GROUP, null, values);
            values.clear();
        }
        db.close();
        return result != -1;
    }

    private void clearTable(String tableName, String where) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE  FROM " + tableName + " " + where);
        db.close();
    }

    public boolean changeStateOfGroup(Integer id, int state, int tableIx) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Consts.STATUS, state);
        long result = db.update(GROUP, values, "ID=? AND TABLE_ID=?", new String[]{id.toString(), String.valueOf(tableIx)});
        db.close();
        return result != -1;
    }

    public ArrayList<Integer> getGroupsByStatus(int tableIx, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Integer> groups = new ArrayList<>();
        Cursor res = db.rawQuery(
                "SELECT ID FROM " + GROUP + " WHERE STATUS ='" + status + "' AND TABLE_ID ='" + tableIx + "'", null);
        if (res != null && res.getCount() > 0)
            while (res.moveToNext()) {
                groups.add(res.getInt(0));
            }
        db.close();
        return groups;
    }

    public ArrayList<Group> getAllGroups(int tableIx) {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Group> groups = new ArrayList<>(); //(ID INTEGER,TABLE_ID INTEGER,NAME TEXT,STATUS INTEGER)");
        Cursor res = db.rawQuery("SELECT * FROM " + GROUP + " WHERE TABLE_ID ='" + tableIx + "' ORDER BY STATUS DESC", null);
        if (res != null && res.getCount() > 0)
            while (res.moveToNext()) {
                Group group = new Group();
                group.setId(res.getInt(0));
                group.setTableId(res.getInt(1));
                group.setName(res.getString(2));
                group.setStatus(res.getInt(3));
                groups.add(group);
            }
        db.close();
        return groups;
    }

    //  USERACCOUNT  (USERNAME  TEXT,  PASSWORD  TEXT,  EMAIL  TEXT,  PHONE  TEXT);
    public UserAccount getUserAccount() {
        SQLiteDatabase db = this.getWritableDatabase();
        UserAccount userAccount = new UserAccount();
        Cursor cursor = db.rawQuery("SELECT * FROM " + USERACCOUNT, null);
        if (cursor != null && cursor.moveToNext()) {
            userAccount.setId(cursor.getLong(0));
            userAccount.setUserName(cursor.getString(1));
            userAccount.setPassword(cursor.getString(2));
            userAccount.setEmail(cursor.getString(3));
            userAccount.setPhone(cursor.getString(4));
        } else {
            userAccount.setId(-1);
            userAccount.setUserName("");
            userAccount.setPassword("");
            userAccount.setEmail("");
            userAccount.setPhone("");
        }
        return userAccount;
    }

    //  USERACCOUNT  (USERNAME  TEXT,  PASSWORD  TEXT,  EMAIL  TEXT,  PHONE  TEXT);
    public void insertUserAccount(UserAccount userAccount, int id) {
        clearTable(USERACCOUNT);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ID", id);
        values.put("USERNAME", userAccount.getUserName());
        values.put("PASSWORD", userAccount.getPassword());
        values.put("EMAIL", userAccount.getEmail());
        values.put("PHONE", userAccount.getPhone());
        db.insert(USERACCOUNT, null, values);
        values.clear();
        values.put(Consts.USERNAME, userAccount.getUserName());
        values.put(Confiq.HASUSERPERMISSION, 1);
        values.put("USERID", id);
        db.update(USERCONFIQ, values, null, null);
        db.close();
    }

    public void updateUserAccount(UserAccount userAccount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ID", userAccount.getId());
        values.put("PASSWORD", userAccount.getPassword());
        values.put("EMAIL", userAccount.getEmail());
        values.put("PHONE", userAccount.getPhone());
        db.update(USERACCOUNT, values, null, null);
        db.close();
    }

    public boolean hasUserPermission() {
        boolean permitted = false;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + Confiq.HASUSERPERMISSION.toUpperCase() + " FROM " + USERCONFIQ, null);
        if (cursor != null && cursor.getCount() > 0)
            if (cursor.moveToNext()) {
                permitted = cursor.getInt(0) > 0;
            }
        cursor.close();
        return permitted;
    }

    public void updateUserPermision(Boolean hasUserPermision) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Confiq.HASUSERPERMISSION, hasUserPermision ? 1 : 0);
        db.update(USERCONFIQ, values, null, null);
        db.close();
    }

    public Confiq getBriefConfiq() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + USERCONFIQ, null);
        Confiq confiq = new Confiq();
        if (cursor != null && cursor.getCount() > 0)
            if (cursor.moveToNext()) {
                confiq.setUserId(cursor.getLong(0));
                confiq.setUserName(cursor.getString(1));
                confiq.setHasUserPermision(cursor.getInt(2) == 1);
            }
        cursor.close();
        cursor = db.rawQuery("SELECT name FROM " + PAGESTITLE, null);
        ArrayList<String> names = new ArrayList<>();
        while (cursor != null && cursor.moveToNext()) {
            names.add(cursor.getString(0));
        }
        cursor.close();
        ArrayList<Long> lastIds = new ArrayList<>();
        for (int i = 1; i < names.size() + 1; i++) {
            cursor = db.rawQuery("SELECT MAX(ID) FROM " + GENERALTABLEPREFIX + i, null);
            if (cursor != null && cursor.moveToNext()) {
                lastIds.add(cursor.getLong(0));
            }
        }
        cursor.close();
        confiq.setLastIds(lastIds);
        return confiq;
    }
}
