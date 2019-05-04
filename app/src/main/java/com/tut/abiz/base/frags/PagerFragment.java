package com.tut.abiz.base.frags;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.tut.abiz.base.Consts;
import com.tut.abiz.base.R;

import java.util.ArrayList;


/**
 * Created by abiz on 4/13/2019.
 */

public class PagerFragment extends Fragment {

    int ix = 1;
    int pageLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /** Getting the arguments to the Bundle object */
        Bundle data = getArguments();
        /** Getting integer data of the key current_page from the bundle */
        ix = data.getInt(Consts.CURRENTPAGE, 1);
        pageLayout = data.getInt(Consts.PAGELAYOUT); // @TODO set default no Content page
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v;
        v = inflater.inflate(pageLayout, container, false);
        return v;
    }

    public int getIx() {
        return ix;
    }

    public void setIx(int ix) {
        this.ix = ix;
    }

    public int getPageLayout() {
        return pageLayout;
    }

    public void setPageLayout(int pageLayout) {
        this.pageLayout = pageLayout;
    }
}
