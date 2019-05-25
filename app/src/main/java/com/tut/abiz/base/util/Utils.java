package com.tut.abiz.base.util;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.tut.abiz.base.Consts;
import com.tut.abiz.base.R;
import com.tut.abiz.base.frags.ListPagerFrag;
import com.tut.abiz.base.model.FragmentPack;
import com.tut.abiz.base.model.GeneralModel;
import com.tut.abiz.base.model.Group;
import com.tut.abiz.base.model.TagVisiblity;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by abiz on 4/30/2019.
 */

public class Utils {

    public static TagVisiblity getTagVisFromPref(int ix, SharedPreferences visiblityPref, SharedPreferences isStringPref) {
        TagVisiblity visiblity = new TagVisiblity(ix);
        visiblity.setTitleVisible(visiblityPref.getBoolean(GeneralModel.TITLE$ + ix, false));
        visiblity.setBodyVisible(visiblityPref.getBoolean(GeneralModel.BODY$ + ix, false));
        visiblity.setHeaderRVisible(visiblityPref.getBoolean(GeneralModel.HEADERR$ + ix, false));
        visiblity.setHeaderLVisible(visiblityPref.getBoolean(GeneralModel.HEADERL$ + ix, false));
        visiblity.setFooterLVisible(visiblityPref.getBoolean(GeneralModel.FOOTERL$ + ix, false));
        visiblity.setFooterRVisible(visiblityPref.getBoolean(GeneralModel.FOOTERR$ + ix, false));
        visiblity.setStarVisible(visiblityPref.getBoolean(GeneralModel.STAR$ + ix, false));

        visiblity.setTitleString(isStringPref.getInt(GeneralModel.TITLE$ + ix, 0));
        visiblity.setBodyString(isStringPref.getInt(GeneralModel.BODY$ + ix, 0));
        visiblity.setHeaderRString(isStringPref.getInt(GeneralModel.HEADERR$ + ix, 0));
        visiblity.setHeaderLString(isStringPref.getInt(GeneralModel.HEADERL$ + ix, 0));
        visiblity.setFooterLString(isStringPref.getInt(GeneralModel.FOOTERL$ + ix, 0));
        visiblity.setFooterRString(isStringPref.getInt(GeneralModel.FOOTERR$ + ix, 0));

        return visiblity;
    }

    public static void putVisInPref(TagVisiblity vis, int tableIx, SharedPreferences visiblityPref, SharedPreferences isStringPref) {
        visiblityPref.edit().putBoolean(GeneralModel.TITLE$ + tableIx, vis.isTitleVisible()).apply();
        visiblityPref.edit().putBoolean(GeneralModel.BODY$ + tableIx, vis.isBodyVisible()).apply();
        visiblityPref.edit().putBoolean(GeneralModel.HEADERR$ + tableIx, vis.isHeaderRVisible()).apply();
        visiblityPref.edit().putBoolean(GeneralModel.HEADERL$ + tableIx, vis.isHeaderLVisible()).apply();
        visiblityPref.edit().putBoolean(GeneralModel.FOOTERR$ + tableIx, vis.isFooterRVisible()).apply();
        visiblityPref.edit().putBoolean(GeneralModel.FOOTERL$ + tableIx, vis.isFooterLVisible()).apply();
        visiblityPref.edit().putBoolean(GeneralModel.STAR$ + tableIx, vis.isStarVisible()).apply();

        isStringPref.edit().putInt(GeneralModel.TITLE$ + tableIx, vis.getTitleString()).apply();
        isStringPref.edit().putInt(GeneralModel.BODY$ + tableIx, vis.getBodyString()).apply();
        isStringPref.edit().putInt(GeneralModel.HEADERR$ + tableIx, vis.getHeaderRString()).apply();
        isStringPref.edit().putInt(GeneralModel.HEADERL$ + tableIx, vis.getHeaderLString()).apply();
        isStringPref.edit().putInt(GeneralModel.FOOTERR$ + tableIx, vis.getFooterRString()).apply();
        isStringPref.edit().putInt(GeneralModel.FOOTERL$ + tableIx, vis.getFooterLString()).apply();
    }

    public static FragmentPack getFragPack(ArrayList<GeneralModel> generalList, String title, TagVisiblity visiblity, int pageNum) {
        ListPagerFrag pFrag = new ListPagerFrag();
        Bundle data1 = new Bundle();
        data1.putInt(Consts.CURRENTPAGE, pageNum);
        data1.putInt(Consts.PAGELAYOUT, R.layout.list_frag);
        data1.putSerializable(Consts.GENERALLIST, generalList);
        data1.putSerializable(Consts.VISIBLITY, visiblity);
        FragmentPack fragmentPack = new FragmentPack(title, pFrag, data1);
        return fragmentPack;
    }

    public static FragmentPack getFragPack(ArrayList<Group> allGroups, String title, int pageNum) {
        ListPagerFrag pFrag = new ListPagerFrag();
        Bundle data1 = new Bundle();
        data1.putInt(Consts.CURRENTPAGE, pageNum);
        data1.putInt(Consts.PAGELAYOUT, R.layout.list_frag);
        data1.putSerializable(Consts.GROUPLIST, allGroups);
        FragmentPack fragmentPack = new FragmentPack(title, pFrag, data1);
        return fragmentPack;
    }

    public static ArrayList<String> extractGeneralTitles(ArrayList<GeneralModel> generalList) {
        ArrayList<String> titles = new ArrayList<>();
        if (generalList == null || generalList.isEmpty())
            return titles;
        for (GeneralModel generalModel : generalList) {
            titles.add(generalModel.getTitle());
        }
        return titles;
    }

    public static ArrayList<String> extractGroupTitles(ArrayList<Group> groups) {
        ArrayList<String> titles = new ArrayList<>();
        if (groups == null || groups.isEmpty())
            return titles;
        for (Group gr : groups) {
            titles.add(gr.getName());
        }
        return titles;
    }

    public static String stringList(ArrayList<Integer> ints) {
        ArrayList<String> list = new ArrayList<>();
        for (Integer anInt : ints) {
            list.add("'" + anInt + "'");
        }
        return Arrays.toString(list.toArray()).replaceAll(" ", "").replaceAll("\\[", "(").replaceAll("]", ")");
    }

    public Utils addQuery(String colName, ArrayList<Integer> l) {
        String in = stringList(l);
        if (l.isEmpty())
            in = "('9999')";
        if (query.contains("IN"))
            query += " OR " + colName + " IN " + in;
        else
            query += colName + " IN " + in;
        return utils;
    }

    private static Utils utils = new Utils();

    private static String query;

    public static Utils o() {
        return utils;
    }

    public static String getQuery() {
        return query;
    }

    private Utils() {
    }

    public static Utils startQuery() {
        query = "";
        return utils;
    }
}
