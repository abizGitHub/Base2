package com.tut.abiz.base.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.tut.abiz.base.Consts;
import com.tut.abiz.base.R;
import com.tut.abiz.base.frags.ListPagerFrag;
import com.tut.abiz.base.frags.PagerFragment;
import com.tut.abiz.base.model.Confiq;
import com.tut.abiz.base.model.FragmentPack;
import com.tut.abiz.base.model.GeneralModel;
import com.tut.abiz.base.model.Group;
import com.tut.abiz.base.model.Message;
import com.tut.abiz.base.model.ModelMap;
import com.tut.abiz.base.model.TagVisiblity;
import com.tut.abiz.base.util.Utils;

import java.util.ArrayList;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;
import static com.tut.abiz.base.util.Utils.getFragPack;
import static com.tut.abiz.base.util.Utils.getTagVisFromPref;

/**
 * Created by abiz on 4/14/2019.
 */

public class GeneralService {

    private ArrayList<FragmentPack> allFragPacks;
    private ArrayList<GeneralModel> testGeneralList;
    private DbHelper dbHelper;
    SharedPreferences pref, visiblityPref, isStringPref;

    public GeneralService(Context context) {
        dbHelper = new DbHelper(context);
        pref = context.getSharedPreferences(Consts.SHEREDPREF, MODE_PRIVATE);
        visiblityPref = context.getSharedPreferences(Consts.VISIBLITYPREF, MODE_PRIVATE);
        isStringPref = context.getSharedPreferences(Consts.ISSTRINGPREF, MODE_PRIVATE);
    }

    public ArrayList<GeneralModel> getTestGeneralList() {
        testGeneralList = new ArrayList<>();
        for (int i = 0; i < 3 + new Random().nextInt(10); i++) {
            GeneralModel generalModel = new GeneralModel();
            generalModel.fillMock();
            testGeneralList.add(generalModel);
        }
        return testGeneralList;
    }


    public ArrayList<FragmentPack> getAllFragPacks() {
        allFragPacks = new ArrayList<>();

        PagerFragment pFrag1 = new PagerFragment();
        Bundle data1 = new Bundle();
        data1.putInt(Consts.CURRENTPAGE, 1);
        data1.putInt(Consts.PAGELAYOUT, R.layout.pager_frag);
        FragmentPack fragmentPack1 = new FragmentPack("page-1", pFrag1, data1);

        PagerFragment pFrag2 = new PagerFragment();
        Bundle data2 = new Bundle();
        data2.putInt(Consts.CURRENTPAGE, 2);
        data2.putInt(Consts.PAGELAYOUT, R.layout.pager_frag);
        FragmentPack fragmentPack2 = new FragmentPack("page-2", pFrag2, data2);

        PagerFragment pFrag3 = new PagerFragment();
        Bundle data3 = new Bundle();
        data3.putInt(Consts.CURRENTPAGE, 3);
        data3.putInt(Consts.PAGELAYOUT, R.layout.pager_frag);
        FragmentPack fragmentPack3 = new FragmentPack("page-3", pFrag3, data3);

        allFragPacks.add(fragmentPack1);
        allFragPacks.add(fragmentPack2);
        allFragPacks.add(fragmentPack3);
        return allFragPacks;
    }

    public ArrayList<FragmentPack> getAllNetGetFrag() {
        allFragPacks = new ArrayList<>();

        ListPagerFrag pFrag1 = new ListPagerFrag();
        Bundle data1 = new Bundle();
        data1.putInt(Consts.CURRENTPAGE, 1);
        data1.putInt(Consts.PAGELAYOUT, R.layout.list_frag);
        data1.putSerializable(Consts.GENERALLIST, getTestGeneralList());
        data1.putSerializable(Consts.VISIBLITY, new TagVisiblity(1).fillMock());
        FragmentPack fragmentPack1 = new FragmentPack("page-1", pFrag1, data1);

        ListPagerFrag pFrag2 = new ListPagerFrag();
        Bundle data2 = new Bundle();
        data2.putInt(Consts.CURRENTPAGE, 2);
        data2.putInt(Consts.PAGELAYOUT, R.layout.list_frag);
        data2.putSerializable(Consts.GENERALLIST, getTestGeneralList());
        data2.putSerializable(Consts.VISIBLITY, new TagVisiblity(2).fillMock());
        FragmentPack fragmentPack2 = new FragmentPack("page-2", pFrag2, data2);

        ListPagerFrag pFrag3 = new ListPagerFrag();
        Bundle data3 = new Bundle();
        data3.putInt(Consts.CURRENTPAGE, 3);
        data3.putInt(Consts.PAGELAYOUT, R.layout.list_frag);
        data3.putSerializable(Consts.GENERALLIST, getTestGeneralList());
        data3.putSerializable(Consts.VISIBLITY, new TagVisiblity(3).fillMock());
        FragmentPack fragmentPack3 = new FragmentPack("page-3", pFrag3, data3);

        allFragPacks.add(fragmentPack1);
        allFragPacks.add(fragmentPack2);
        allFragPacks.add(fragmentPack3);
        return allFragPacks;
    }

    public ArrayList<GeneralModel> getDBList() {
        ArrayList<GeneralModel> list = new ArrayList<>();
        GeneralModel model = new GeneralModel();
        Confiq confiq = dbHelper.getConfiq();
        model.setTitle("confiq");
        model.setBody(confiq.toString());
        list.add(model);

        ArrayList<TagVisiblity> tagVisiblity = new ArrayList<>();
        tagVisiblity.add(dbHelper.getTagVisiblity(1));
        tagVisiblity.add(dbHelper.getTagVisiblity(2));
        for (TagVisiblity vis : tagVisiblity) {
            model = new GeneralModel();
            model.setTitle("tagVis-" + vis.getTableId());
            model.setBody(vis.toString());
            list.add(model);
        }

        GeneralModel mdl = new GeneralModel();
        mdl.setTitle("connectPeriod and wait for server");
        StringBuffer buffer = new StringBuffer("connectPeriod : ");
        buffer.append(pref.getInt(Confiq.CONNECTPERIOD, Consts.DEFAULTCONPERIOD));
        buffer.append("\n wait for server : ");
        buffer.append(pref.getInt(Confiq.WAIT4SERVER, Consts.DEFAULTWAIT4SERVER));
        mdl.setBody(buffer.toString());
        list.add(mdl);
        ArrayList<Message> messages = dbHelper.getAllMessages();
        for (Message message : messages) {
            mdl = new GeneralModel();
            mdl.setBody("msgId:" + message.getMsgId() + "-" + message.getBody() + "-" + message.getRegisterDate());
            mdl.setTitle("id:" + message.getId() + "-type:" + message.getType() + "-delivered:" + message.getDelivered());
            list.add(mdl);
        }
        ArrayList<Group> groups = dbHelper.getAllGroups(1);
        groups.addAll(dbHelper.getAllGroups(2));
        groups.addAll(dbHelper.getAllGroups(3));
        groups.addAll(dbHelper.getAllGroups(4));
        for (Group group : groups) {
            mdl = new GeneralModel();
            mdl.setTitle("group-" + group.getTableId());
            mdl.setBody(group.getId() + "-" + group.getName() + "-status:" + group.getStatus());
            list.add(mdl);
        }

        ArrayList<ModelMap> modelMap = dbHelper.getModelMap(1);
        modelMap.addAll(dbHelper.getModelMap(2));
        modelMap.addAll(dbHelper.getModelMap(3));
        modelMap.addAll(dbHelper.getModelMap(4));
        for (ModelMap map : modelMap) {
            mdl = new GeneralModel();
            mdl.setTitle("modelMap-id:" + map.getId());
            mdl.setBody(map.toString());
            list.add(mdl);
        }

        return list;
    }

    public ArrayList<FragmentPack> getStaredList() {
        ArrayList<FragmentPack> fragmentPacks = new ArrayList<>();
        int tableCount = pref.getInt(Consts.TABLECOUNT, 2);
        for (int ix = 1; ix < tableCount + 1; ix++) {
            fragmentPacks.add(getFragPack(dbHelper.getAllStaredGeneralFrom(ix), pref.getString(Consts.TABLENAMES[ix - 1], "-"), getTagVisFromPref(ix, visiblityPref, isStringPref), ix));
        }
        return fragmentPacks;
    }

    public ArrayList<FragmentPack> getAllList() {
        ArrayList<FragmentPack> fragmentPacks = new ArrayList<>();
        int tableCount = pref.getInt(Consts.TABLECOUNT, 2);
        for (int ix = 1; ix < tableCount + 1; ix++) {
            fragmentPacks.add(getFragPack(dbHelper.getAllGeneralFrom(ix), pref.getString(Consts.TABLENAMES[ix - 1], "-"), getTagVisFromPref(ix, visiblityPref, isStringPref), ix));
        }
        return fragmentPacks;
    }

    public ArrayList<GeneralModel> getAllGeneralFrom(int ix) {
        return dbHelper.getAllGeneralFrom(ix);
    }

    public ArrayList<GeneralModel> getAllGeneralFrom(int ix, String inWord, TagVisiblity tagVisiblity) {
        return dbHelper.getAllGeneralFrom(ix, inWord, tagVisiblity);
    }


    public ArrayList<Group> getGroupList() {
        return dbHelper.getAllGroups(1);
    }

    public ArrayList<FragmentPack> getGroupPacks() {
        ArrayList<FragmentPack> fragmentPacks = new ArrayList<>();
        int tableCount = pref.getInt(Consts.TABLECOUNT, 2);
        for (int ix = 1; ix < tableCount + 1; ix++) {
            fragmentPacks.add(Utils.getFragPack(dbHelper.getAllGroups(ix), pref.getString(Consts.TABLENAMES[ix - 1], "-"), ix));
        }
        return fragmentPacks;
    }


}
