package com.tut.abiz.base.util;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.tut.abiz.base.Consts;
import com.tut.abiz.base.R;
import com.tut.abiz.base.frags.ListPagerFrag;
import com.tut.abiz.base.model.FragmentPack;
import com.tut.abiz.base.model.GeneralModel;
import com.tut.abiz.base.model.TagVisiblity;

import java.util.ArrayList;

/**
 * Created by abiz on 4/30/2019.
 */

public class Utils {

    public static TagVisiblity getTagVisFromPref(int ix, SharedPreferences visiblityPref) {
        TagVisiblity visiblity = new TagVisiblity();
        visiblity.setTitleVisible(visiblityPref.getBoolean(GeneralModel.TITLE$ + ix, false));
        visiblity.setBodyVisible(visiblityPref.getBoolean(GeneralModel.BODY$ + ix, false));
        visiblity.setHeaderRVisible(visiblityPref.getBoolean(GeneralModel.HEADERR$ + ix, false));
        visiblity.setHeaderLVisible(visiblityPref.getBoolean(GeneralModel.HEADERL$ + ix, false));
        visiblity.setFooterLVisible(visiblityPref.getBoolean(GeneralModel.FOOTERL$ + ix, false));
        visiblity.setFooterRVisible(visiblityPref.getBoolean(GeneralModel.FOOTERR$ + ix, false));
        visiblity.setStarVisible(visiblityPref.getBoolean(GeneralModel.STAR$ + ix, false));
        return visiblity;
    }

    public static FragmentPack getFragPack(ArrayList<GeneralModel> generalList, String title, TagVisiblity visiblity ,int pageNum) {
        ListPagerFrag pFrag = new ListPagerFrag();
        Bundle data1 = new Bundle();
        data1.putInt(Consts.CURRENTPAGE, pageNum);
        data1.putInt(Consts.PAGELAYOUT, R.layout.list_frag);
        data1.putSerializable(Consts.GENERALLIST, generalList);
        data1.putSerializable(Consts.VISIBLITY, visiblity);
        FragmentPack fragmentPack = new FragmentPack(title, pFrag, data1);
        return fragmentPack;
    }

}
