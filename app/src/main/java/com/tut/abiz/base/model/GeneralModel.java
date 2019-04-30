package com.tut.abiz.base.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Abiz
 * Date: 2/28/19
 * Time: 10:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class GeneralModel implements Serializable {

    public static final String TITLE$ = "title";
    public static final String HEADERL$ = "headerL";
    public static final String HEADERR$ = "headerR";
    public static final String BODY$ = "body";
    public static final String FOOTERL$ = "footerL";
    public static final String FOOTERR$ = "footerR";
    public static final String STAR$ = "star";

    String title;
    String body;
    String headerL;
    String headerR;
    String footerL;
    String footerR;
    Long id;
    boolean stared = false;
    //TITLE = 1;HEADER_R = 2;HEADER_L = 3;BODY = 4;FOOTER_R = 5;FOOTER_L = 6;
    public static int TITLE = 1;
    public static int HEADER_R = 2;
    public static int HEADER_L = 3;
    public static int BODY = 4;
    public static int FOOTER_R = 5;
    public static int FOOTER_L = 6;
    public static int STAR = 7;

    public GeneralModel() {
    }

    public GeneralModel(JSONObject json) throws JSONException {
        this.setId(Long.valueOf(json.get("id").toString()));
        this.setTitle(json.get("title").toString());
        this.setHeaderL(json.get("headerL").toString());
        this.setHeaderR(json.get("headerR").toString());
        this.setBody(json.get("body").toString());
        this.setFooterL(json.get("footerL").toString());
        this.setFooterR(json.get("footerR").toString());
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getHeaderL() {
        return headerL;
    }

    public void setHeaderL(String headerL) {
        this.headerL = headerL;
    }

    public String getHeaderR() {
        return headerR;
    }

    public void setHeaderR(String headerR) {
        this.headerR = headerR;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean getStared() {
        return stared;
    }

    public void setStared(boolean stared) {
        this.stared = stared;
    }

    public String getFooterL() {
        return footerL;
    }

    public void setFooterL(String footerL) {
        this.footerL = footerL;
    }

    public String getFooterR() {
        return footerR;
    }

    public void setFooterR(String footerR) {
        this.footerR = footerR;
    }

    public void applyModelMap(HashMap<Integer, HashMap<Integer, String>> columnHash) {
        if (columnHash.isEmpty())
            return;
        HashMap<Integer, String> intValueHash = columnHash.get(TITLE);
        if (intValueHash != null && !intValueHash.isEmpty())
            this.setTitle(intValueHash.get(Integer.parseInt(this.getTitle())));
        intValueHash = columnHash.get(HEADER_R);
        if (intValueHash != null && !intValueHash.isEmpty())
            this.setHeaderR(intValueHash.get(Integer.parseInt(this.getHeaderR())));
        intValueHash = columnHash.get(HEADER_L);
        if (intValueHash != null && !intValueHash.isEmpty())
            this.setHeaderL(intValueHash.get(Integer.parseInt(this.getHeaderL())));
        intValueHash = columnHash.get(BODY);
        if (intValueHash != null && !intValueHash.isEmpty())
            this.setBody(intValueHash.get(Integer.parseInt(this.getBody())));
        intValueHash = columnHash.get(FOOTER_R);
        if (intValueHash != null && !intValueHash.isEmpty())
            this.setFooterR(intValueHash.get(Integer.parseInt(this.getFooterR())));
        intValueHash = columnHash.get(FOOTER_L);
        if (intValueHash != null && !intValueHash.isEmpty())
            this.setFooterL(intValueHash.get(Integer.parseInt(this.getFooterL())));
    }

    public void fillMock() {
        int rnd = new Random().nextInt(100);
        setTitle("title-" + rnd);
        setBody("body-" + rnd);
        setHeaderL("headerL-" + rnd);
        setHeaderR("headerR-" + rnd);
        setFooterL("footerL-" + rnd);
        setFooterR("footerR-" + rnd);
        setId(Long.valueOf(rnd));
        setStared(rnd > 50);
    }

}
