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
import com.tut.abiz.base.model.ModelMap;
import com.tut.abiz.base.model.TagVisiblity;

import java.util.ArrayList;
import java.util.HashMap;

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

    private HashMap<Integer, ArrayList<ModelMap>> modelMapListHash;
    private HashMap<Integer, HashMap<Integer, HashMap<Integer, String>>> modelMapHashs; // tableIX > columnIx > intVal > string

    public DbHelper(Context context) {
        super(context, DBNAME, null, 1);
        modelMapListHash = new HashMap<>();
        modelMapHashs = new HashMap<>();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + PAGESTITLE + " (TABLE_ID INTEGER PRIMARY KEY,NAME TEXT)");
        db.execSQL("CREATE TABLE " + MODELMAP + " (ID INTEGER PRIMARY KEY,TABLE_ID INTEGER,COLUMN_IX INTEGER,INT_VALUE INTEGER,STRING_VALUE TEXT)");
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
        values.put(Confiq.USERNAME, Consts.NEWUSERNAME);
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

    public ArrayList<GeneralModel> getAllGeneralFrom(int ix) {
        HashMap<Integer, HashMap<Integer, String>> modelMapHash = getColumnHash(ix);
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
                generalModel.applyModelMap(modelMapHash);
                generalModels.add(generalModel);
            }
        db.close();
        return generalModels;
    }

    public ArrayList<GeneralModel> getAllStaredGeneralFrom(int ix) {
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
                generalModel.applyModelMap(getColumnHash(ix));
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

    public void setTagVisiblitys(ArrayList<TagVisiblity> tagVisiblity) {

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

    public void setUserNameId(Long userId, String userName, Boolean hasUserPermision) {

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

}
