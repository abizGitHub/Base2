package com.tut.abiz.base.util;

import android.content.SharedPreferences;

import com.tut.abiz.base.model.GeneralModel;
import com.tut.abiz.base.model.TagVisiblity;

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
}
