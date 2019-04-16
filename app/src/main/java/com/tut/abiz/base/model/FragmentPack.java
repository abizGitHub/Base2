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
    int ix = 0;

    public String getPageTitle() {
        return pageTitle;
    }

    public PagerFragment getPagerFragment() {
        return pagerFragment;
    }

    public FragmentPack(String pageTitle, PagerFragment pagerFragment, Bundle data) {
        this.pageTitle = pageTitle;
        this.pagerFragment = pagerFragment;
        pagerFragment.setArguments(data);
    }

    public int getIx() {
        return ix;
    }

    public void setIx(int ix) {
        this.ix = ix;
    }
}
