package com.tut.abiz.base.model;

import java.io.Serializable;
import java.util.Random;

/**
 * Created by abiz on 4/14/2019.
 */

public class TagVisiblity implements Serializable {

    private boolean titleVisible;
    private boolean headerRVisible;
    private boolean headerLVisible;
    private boolean bodyVisible;
    private boolean footerRVisible;
    private boolean footerLVisible;
    private boolean starVisible;
    private int tableId;
    private int titleString;
    private int headerRString;
    private int headerLString;
    private int bodyString;
    private int footerRString;
    private int footerLString;

    public TagVisiblity(int tableId) {
        this.tableId = tableId;
    }

    public TagVisiblity doTitleVisible(boolean titleVisible) {
        this.titleVisible = titleVisible;
        return this;
    }

    public TagVisiblity doHeaderRVisible(boolean headerRVisible) {
        this.headerRVisible = headerRVisible;
        return this;
    }

    public TagVisiblity doHeaderLVisible(boolean headerLVisible) {
        this.headerLVisible = headerLVisible;
        return this;
    }

    public TagVisiblity doBodyVisible(boolean bodyVisible) {
        this.bodyVisible = bodyVisible;
        return this;
    }

    public TagVisiblity doFooterRVisible(boolean footerRVisible) {
        this.footerRVisible = footerRVisible;
        return this;
    }

    public TagVisiblity doFooterLVisible(boolean footerLVisible) {
        this.footerLVisible = footerLVisible;
        return this;
    }

    public TagVisiblity doStarVisible(boolean starVisible) {
        this.starVisible = starVisible;
        return this;
    }

    public boolean isTitleVisible() {
        return titleVisible;
    }

    public boolean isHeaderRVisible() {
        return headerRVisible;
    }

    public boolean isHeaderLVisible() {
        return headerLVisible;
    }

    public boolean isBodyVisible() {
        return bodyVisible;
    }

    public boolean isFooterRVisible() {
        return footerRVisible;
    }

    public boolean isFooterLVisible() {
        return footerLVisible;
    }

    public boolean isStarVisible() {
        return starVisible;
    }

    public void setTitleVisible(boolean titleVisible) {
        this.titleVisible = titleVisible;
    }

    public void setHeaderRVisible(boolean headerRVisible) {
        this.headerRVisible = headerRVisible;
    }

    public void setHeaderLVisible(boolean headerLVisible) {
        this.headerLVisible = headerLVisible;
    }

    public void setBodyVisible(boolean bodyVisible) {
        this.bodyVisible = bodyVisible;
    }

    public void setFooterRVisible(boolean footerRVisible) {
        this.footerRVisible = footerRVisible;
    }

    public void setFooterLVisible(boolean footerLVisible) {
        this.footerLVisible = footerLVisible;
    }

    public void setStarVisible(boolean starVisible) {
        this.starVisible = starVisible;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public int getTitleString() {
        return titleString;
    }

    public void setTitleString(int titleString) {
        this.titleString = titleString;
    }

    public int getHeaderRString() {
        return headerRString;
    }

    public void setHeaderRString(int headerRString) {
        this.headerRString = headerRString;
    }

    public int getHeaderLString() {
        return headerLString;
    }

    public void setHeaderLString(int headerLString) {
        this.headerLString = headerLString;
    }

    public int getBodyString() {
        return bodyString;
    }

    public void setBodyString(int bodyString) {
        this.bodyString = bodyString;
    }

    public int getFooterRString() {
        return footerRString;
    }

    public void setFooterRString(int footerRString) {
        this.footerRString = footerRString;
    }

    public int getFooterLString() {
        return footerLString;
    }

    public void setFooterLString(int footerLString) {
        this.footerLString = footerLString;
    }

    public TagVisiblity fillMock() {
        this.setTitleVisible(new Random().nextBoolean());
        this.setStarVisible(new Random().nextBoolean());
        this.setHeaderRVisible(new Random().nextBoolean());
        this.setHeaderLVisible(new Random().nextBoolean());
        this.setBodyVisible(new Random().nextBoolean());
        this.setFooterRVisible(new Random().nextBoolean());
        this.setFooterLVisible(new Random().nextBoolean());
        return this;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("tableId :  " + tableId + "\n");
        buffer.append("Title :  " + isTitleVisible() + " ,isString :" + titleString + "\n");
        buffer.append("Body :  " + isBodyVisible() + " ,isString :" + bodyString + "\n");
        buffer.append("HeaderR :  " + isHeaderRVisible() + " ,isString :" + headerRString + "\n");
        buffer.append("HeaderL :  " + isHeaderLVisible() + " ,isString :" + headerLString + "\n");
        buffer.append("FooterR :  " + isFooterRVisible() + " ,isString :" + footerRString + "\n");
        buffer.append("FooterL :  " + isFooterLVisible() + " ,isString :" + footerLString + "\n");
        return buffer.toString();
    }

    public boolean equal(TagVisiblity vis) {
        boolean eq = this.isTitleVisible() == vis.isTitleVisible();
        if (!eq)
            return false;
        eq = this.isBodyVisible() == vis.isBodyVisible();
        if (!eq)
            return false;
        eq = this.isHeaderLVisible() == vis.isHeaderLVisible();
        if (!eq)
            return false;
        eq = this.isHeaderRVisible() == vis.isHeaderRVisible();
        if (!eq)
            return false;
        eq = this.isFooterLVisible() == vis.isFooterLVisible();
        if (!eq)
            return false;
        eq = this.isFooterRVisible() == vis.isFooterRVisible();
        if (!eq)
            return false;
        eq = this.isStarVisible() == vis.isStarVisible();
        if (!eq)
            return false;

        eq = this.titleString == vis.titleString;
        if (!eq)
            return false;
        eq = this.bodyString == vis.bodyString;
        if (!eq)
            return false;
        eq = this.headerLString == vis.headerLString;
        if (!eq)
            return false;
        eq = this.headerRString == vis.headerRString;
        if (!eq)
            return false;
        eq = this.footerLString == vis.footerLString;
        if (!eq)
            return false;
        eq = this.footerRString == vis.footerRString;
        if (!eq)
            return false;
        return true;
    }

}
