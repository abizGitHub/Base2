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
}
