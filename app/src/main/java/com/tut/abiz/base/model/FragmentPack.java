package com.tut.abiz.base.model;

import android.os.Bundle;

import com.tut.abiz.base.Consts;
import com.tut.abiz.base.frags.PagerFragment;

/**
 * Created by abiz on 4/15/2019.
 */


public class FragmentPack {
    String pageTitle;
    PagerFragment pagerFragment;
    Integer pageLayout;
    int ix = 0;

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }

    public PagerFragment getPagerFragment() {
        return pagerFragment;
    }

    public void setPagerFragment(PagerFragment pagerFragment) {
        this.pagerFragment = pagerFragment;
    }

    public Integer getPageLayout() {
        return pageLayout;
    }

    public void setPageLayout(Integer pageLayout) {
        this.pageLayout = pageLayout;
    }

    public FragmentPack(String pageTitle, PagerFragment pagerFragment, Integer pageLayout) {
        this.pageTitle = pageTitle;
        this.pagerFragment = pagerFragment;
        this.pageLayout = pageLayout;
        Bundle data = new Bundle();
        data.putInt(Consts.CURRENTPAGE, ix);
        data.putInt(Consts.PAGELAYOUT, pageLayout);
        pagerFragment.setArguments(data);
        /*pagerFragment.setIx(ix);
        pagerFragment.setPageLayout(pageLayout);*/
    }

    public int getIx() {
        return ix;
    }

    public void setIx(int ix) {
        this.ix = ix;
    }
}
