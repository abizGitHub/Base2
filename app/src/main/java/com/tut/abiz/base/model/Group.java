package com.tut.abiz.base.model;

import java.io.Serializable;

/**
 * Created by abiz on 5/11/2019.
 */

public class Group implements Serializable {

    public static final String ORDERED$ = "ORDERED";
    public static final String REGISTERED$ = "REGISTERED";
    private int id;
    private String name;
    public static int UNREGISTERED = 0;
    public static int ORDERED = 1;
    public static int REGISTERED = 2;
    private int status = UNREGISTERED;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Group fillMock(int i) {
        this.id = i;
        this.name = "123456 abc def ghi jkl > The crazy brown fox jumps over the lazy dog >id:" + i;
        this.name += " \n line two ";
        this.name += " \n line 3 three for group sample ";
        return this;
    }
}
