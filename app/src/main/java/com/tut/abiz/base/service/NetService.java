package com.tut.abiz.base.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.tut.abiz.base.Consts;
import com.tut.abiz.base.NetServiceListener;
import com.tut.abiz.base.R;
import com.tut.abiz.base.adapter.JsonUtil;
import com.tut.abiz.base.async.BrowsePageTask;
import com.tut.abiz.base.async.GetListTask;
import com.tut.abiz.base.async.PostListTask;
import com.tut.abiz.base.frags.ListPagerFrag;
import com.tut.abiz.base.model.Confiq;
import com.tut.abiz.base.model.FragmentPack;
import com.tut.abiz.base.model.GeneralModel;
import com.tut.abiz.base.model.TagVisiblity;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by abiz on 4/15/2019.
 */

public class NetService implements NetServiceListener {

    BrowsePageTask browsePageTask;
    GetListTask listTask;
    NetServiceListener listener;
    Boolean wait4List;
    int waitCount;
    ArrayList<GeneralModel> generalModels;
    DbHelper dbHelper;
    Confiq confiqRemote;
    SharedPreferences pref, visiblityPref;

    public NetService(NetServiceListener netServiceListener, Context context) {
        listener = netServiceListener;
        dbHelper = new DbHelper(context);
        pref = context.getSharedPreferences(Consts.SHEREDPREF, MODE_PRIVATE);
        visiblityPref = context.getSharedPreferences(Consts.VISIBLITYPREF, MODE_PRIVATE);
    }

    public void browsePage(String urlString) {
        browsePageTask = new BrowsePageTask(listener);
        browsePageTask.setUrl(urlString);
        browsePageTask.execute("");
    }

    private void doGetList(String url) {
        listTask = new GetListTask(this);
        listTask.setUrl(url);
        listTask.execute("");
    }

    private FragmentPack getFragPack(ArrayList<GeneralModel> generalList, String title, TagVisiblity visiblity) {
        ListPagerFrag pFrag1 = new ListPagerFrag();
        Bundle data1 = new Bundle();
        data1.putInt(Consts.CURRENTPAGE, 0);
        data1.putInt(Consts.PAGELAYOUT, R.layout.list_frag);
        data1.putSerializable(Consts.GENERALLIST, generalList);
        data1.putSerializable(Consts.VISIBLITY, visiblity);
        FragmentPack fragmentPack1 = new FragmentPack(title, pFrag1, data1);
        return fragmentPack1;
    }

    public ArrayList<FragmentPack> getAllNetList() {
        ArrayList<FragmentPack> fragmentPacks = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            wait4List = true;
            waitCount = 0;
            doGetList(Consts.SERVERADDRESS + "/gm/getTestGeneralList");
            while (wait4List) {
                try {
                    waitCount++;
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            fragmentPacks.add(getFragPack(generalModels, "page-" + i, new TagVisiblity().fillMock()));
        }
        return fragmentPacks;
    }

    public ArrayList<FragmentPack> allNetList() {
        Confiq confiqLocal = dbHelper.getConfiq();
        doPostConfig(Consts.SERVERADDRESS + "/gm/getConfiq", confiqLocal);
        if (confiqRemote != null)
            applyNewUserSetting(confiqLocal, confiqRemote);
        if (confiqRemote != null) {
            applyNewModelMap(confiqLocal, confiqRemote);
            applyNewTagVisiblity(confiqLocal, confiqRemote);
        }
        ArrayList<FragmentPack> fragmentPacks = applyNewGmList(confiqLocal, confiqRemote);
        return fragmentPacks;
    }

    private void applyNewUserSetting(Confiq confiqLocal, Confiq confiqRemote) {

        if (confiqRemote.getHaveNewChange() != null && confiqRemote.getHaveNewChange()) {
            if (confiqRemote.getLastTablesName() != null && confiqRemote.getLastTablesName().size() > 0) {
                for (int i = 0; i < 6; i++) {
                    pref.edit().remove(Consts.TABLENAMES[i]).apply();
                }
                int ix = 0;
                for (String tName : confiqRemote.getLastTablesName()) {
                    pref.edit().putString(Consts.TABLENAMES[ix++], tName).apply();
                }
                pref.edit().putInt(Consts.TABLECOUNT, ix).apply();
            }
        }

        if (confiqRemote.getClearDB() != null && confiqRemote.getClearDB()) {
            dbHelper.clearDB();
            ArrayList<Long> lastIds = new ArrayList<>();
            ArrayList<String> lastTablesName = confiqRemote.getLastTablesName();
            for (String t : lastTablesName) {
                lastIds.add(0L);
            }
            confiqLocal.setLastTablesName(confiqRemote.getLastTablesName());
            confiqLocal.setLastIds(lastIds);
            dbHelper.insertModelMaps(confiqRemote.getLastModelMap());
            confiqLocal.setLastModelMap(confiqRemote.getLastModelMap());
            confiqLocal.setLastModelMapId(dbHelper.getLastModelMapId());
            dbHelper.setTagVisiblitys(confiqRemote.getTagVisiblity());
            confiqLocal.setTagVisiblity(confiqRemote.getTagVisiblity());
            dbHelper.insertTableNames(confiqRemote.getLastTablesName());
        }

        if (confiqRemote.getUserId() != confiqLocal.getUserId() || confiqRemote.getHasUserPermision() != confiqLocal.getHasUserPermision()) {
            dbHelper.setUserNameId(confiqRemote.getUserId(), confiqRemote.getUserName(), confiqRemote.getHasUserPermision());
        }

    }

    private ArrayList<FragmentPack> applyNewGmList(Confiq confiqLocal, Confiq confiqRemote) {
        ArrayList<FragmentPack> fragmentPacks = new ArrayList<>();
        int tableCount = pref.getInt(Consts.TABLECOUNT, 2);
        ArrayList<Long> lastIds = confiqLocal.getLastIds();
        for (int ix = 1; ix < tableCount + 1; ix++) {
            if (confiqRemote != null && confiqRemote.getLastIds().get(ix - 1) > confiqLocal.getLastIds().get(ix - 1)) {
                wait4List = true;
                waitCount = 0;
                doPostList(Consts.SERVERADDRESS + "/gm/" + ix + "/" + lastIds.get(ix - 1), confiqLocal);
                while (wait4List) {
                    try {
                        waitCount++;
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                dbHelper.insertGMs(generalModels, ix);
            }
            fragmentPacks.add(getFragPack(dbHelper.getAllGeneralFrom(ix), pref.getString(Consts.TABLENAMES[ix - 1], "-"), getTagVisFromPref(ix)));
        }
        return fragmentPacks;
    }

    private TagVisiblity getTagVisFromPref(int ix) { // 1 2
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

    private void doPostList(String url, Confiq confiqLocal) {
        PostListTask postListTask = new PostListTask(this);
        postListTask.setUrlAndMessage(url, JsonUtil.parseConfiq(confiqLocal));
        postListTask.execute(PostListTask.GETLIST);
        wait4List = true;
        waitCount = 0;
        while (wait4List) {
            try {
                waitCount++;
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void applyNewTagVisiblity(Confiq confiqLocal, Confiq confiqRemote) {
        if (confiqRemote.getHaveNewChange() != null && confiqRemote.getHaveNewChange() && confiqRemote.getTagVisiblity() != null) {
            ArrayList<TagVisiblity> visiblity = confiqRemote.getTagVisiblity();
            dbHelper.updateTagVisiblity(visiblity);
            int ix = 1;
            for (TagVisiblity vis : visiblity) {
                visiblityPref.edit().putBoolean(GeneralModel.TITLE$ + ix, vis.isTitleVisible()).apply();
                visiblityPref.edit().putBoolean(GeneralModel.BODY$ + ix, vis.isBodyVisible()).apply();
                visiblityPref.edit().putBoolean(GeneralModel.HEADERR$ + ix, vis.isHeaderRVisible()).apply();
                visiblityPref.edit().putBoolean(GeneralModel.HEADERL$ + ix, vis.isHeaderLVisible()).apply();
                visiblityPref.edit().putBoolean(GeneralModel.FOOTERR$ + ix, vis.isFooterRVisible()).apply();
                visiblityPref.edit().putBoolean(GeneralModel.FOOTERL$ + ix, vis.isFooterLVisible()).apply();
                visiblityPref.edit().putBoolean(GeneralModel.STAR$ + ix, vis.isStarVisible()).apply();
                ix++;
            }
        }
    }

    private void applyNewModelMap(Confiq confiqLocal, Confiq confiqRemote) {
        if (confiqRemote.getHaveNewChange() != null && confiqRemote.getHaveNewChange()) {
            if (confiqRemote.getLastModelMap() != null && confiqRemote.getLastModelMap().size() > 0)
                dbHelper.updateModelMap(confiqRemote.getLastModelMap());
            if (confiqRemote.getModelMap2Delete() != null && confiqRemote.getModelMap2Delete().size() > 0)
                dbHelper.deleteModelMap(confiqRemote.getModelMap2Delete());
        }
    }

    private void doPostConfig(String url, Confiq confiq) {
        PostListTask postListTask = new PostListTask(this);
        postListTask.setUrlAndMessage(url, JsonUtil.parseConfiq(confiq));
        postListTask.execute(PostListTask.GETCONGIQ);
        wait4List = true;
        waitCount = 0;
        while (wait4List) {
            try {
                waitCount++;
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onTextResponseReady(String response) {

    }

    @Override
    public void onFailure(String error) {

    }

    @Override
    public void onGeneralListReady(ArrayList<GeneralModel> generalModels) {
        this.generalModels = generalModels;
        wait4List = false;
    }

    public void onConfiqReady(Confiq confiqRemote) {
        this.confiqRemote = confiqRemote;
        wait4List = false;
    }

}
