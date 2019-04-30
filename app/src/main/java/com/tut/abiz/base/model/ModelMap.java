package com.tut.abiz.base.model;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Abiz
 * Date: 3/28/19
 * Time: 12:46 AM
 * To change this template use File | Settings | File Templates.
 */

public class ModelMap implements Serializable {


    public static final String COLUMNIX = "columnIx";
    public static final String INTVALUE = "intValue";
    public static final String STRINGVALUE = "stringValue";
    public static final String TABLEID = "tableId";

    private int id;
    private int tableId;
    private int columnIx; // GeneralModel statics;
    private Integer intValue;
    private String stringValue;

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public int getColumnIx() {
        return columnIx;
    }

    public void setColumnIx(int columnIx) {
        this.columnIx = columnIx;
    }

    public Integer getIntValue() {
        return intValue;
    }

    public void setIntValue(Integer intValue) {
        this.intValue = intValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("tableId :  " + tableId + "\n");
        buffer.append("columnIx :  " + columnIx + "\n");
        buffer.append("intValue :  " + intValue + "\n");
        buffer.append("stringValue :  " + stringValue + "\n");
        return buffer.toString();
    }
}
