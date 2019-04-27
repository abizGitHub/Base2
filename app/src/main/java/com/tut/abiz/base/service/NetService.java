package com.tut.abiz.base.service;

import android.content.Context;
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

    public NetService(NetServiceListener netServiceListener, Context context) {
        listener = netServiceListener;
        dbHelper = new DbHelper(context);
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

    private FragmentPack getFragPack(ArrayList<GeneralModel> generalList, String title) {
        ListPagerFrag pFrag1 = new ListPagerFrag();
        Bundle data1 = new Bundle();
        data1.putInt(Consts.CURRENTPAGE, 0);
        data1.putInt(Consts.PAGELAYOUT, R.layout.list_frag);
        data1.putSerializable(Consts.GENERALLIST, generalList);
        data1.putSerializable(Consts.VISIBLITY, new TagVisiblity().fillMock());
        FragmentPack fragmentPack1 = new FragmentPack(title, pFrag1, data1);
        return fragmentPack1;
    }

    public ArrayList<FragmentPack> getAllNetList() {
        ArrayList<FragmentPack> fragmentPacks = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            wait4List = true;
            waitCount = 0;
            doGetList(Consts.SERVERADDRESS + "/gm/getTestGeneralList");
            while (wait4List ) {
                try {
                    waitCount++;
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            fragmentPacks.add(getFragPack(generalModels, "page-" + i));
        }
        return fragmentPacks;
    }

    public ArrayList<FragmentPack> allNetList() {
        Confiq confiqLocal = dbHelper.getConfiq();
        doPostConfig(Consts.SERVERADDRESS + "/gm/getConfiq", confiqLocal);
        if (confiqRemote != null)
            applyNewUserSetting(confiqLocal, confiqRemote);
        ArrayList<FragmentPack> fragmentPacks = applyNewGmList(confiqLocal, confiqRemote);
        if (confiqRemote != null) {
            applyNewModelMap(confiqLocal, confiqRemote);
            applyNewTagVisiblity(confiqLocal, confiqRemote);
        }
        return fragmentPacks;
    }

    private void applyNewUserSetting(Confiq confiqLocal, Confiq confiqRemote) {

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
        ArrayList<String> tablesName = confiqLocal.getLastTablesName();
        if (confiqRemote != null)
            tablesName = confiqRemote.getLastTablesName();
        ArrayList<Long> lastIds = confiqLocal.getLastIds();
        int ix = 1;
        for (String tName : tablesName) {
            if (confiqRemote != null && confiqRemote.getLastIds().get(ix - 1) > confiqLocal.getLastIds().get(ix - 1)) {
                wait4List = true;
                waitCount = 0;
                doPostList(Consts.SERVERADDRESS + "/gm/" + ix + "/" + lastIds.get(ix - 1), confiqLocal);
                while (wait4List ) {
                    try {
                        waitCount++;
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                dbHelper.insertGMs(generalModels, ix);
            }
            fragmentPacks.add(getFragPack(dbHelper.getAllGeneralFrom(ix), tName));
            ix++;
        }
        return fragmentPacks;
    }

    private void doPostList(String url, Confiq confiqLocal) {
        PostListTask postListTask = new PostListTask(this);
        postListTask.setUrlAndMessage(url, JsonUtil.parseConfiq(confiqLocal));
        postListTask.execute(PostListTask.GETLIST);
        wait4List = true;
        waitCount = 0;
        while (wait4List ) {
            try {
                waitCount++;
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void applyNewTagVisiblity(Confiq confiqLocal, Confiq confiqRemote) {
        if (confiqRemote.getHaveNewChange() != null && confiqRemote.getHaveNewChange()) {
            dbHelper.updateTagVisiblity(confiqRemote.getTagVisiblity());
        }
    }

    private void applyNewModelMap(Confiq confiqLocal, Confiq confiqRemote) {
        if (confiqRemote.getHaveNewChange() != null && confiqRemote.getHaveNewChange()) {
            dbHelper.updateModelMap(confiqRemote.getLastModelMap());
        }
    }

    private void doPostConfig(String url, Confiq confiq) {
        PostListTask postListTask = new PostListTask(this);
        postListTask.setUrlAndMessage(url, JsonUtil.parseConfiq(confiq));
        postListTask.execute(PostListTask.GETCONGIQ);
        wait4List = true;
        waitCount = 0;
        while (wait4List ) {
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
