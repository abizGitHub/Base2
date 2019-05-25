package com.tut.abiz.base.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.tut.abiz.base.Consts;
import com.tut.abiz.base.NetServiceListener;
import com.tut.abiz.base.adapter.JsonUtil;
import com.tut.abiz.base.async.BrowsePageTask;
import com.tut.abiz.base.async.GetListTask;
import com.tut.abiz.base.async.PostListTask;
import com.tut.abiz.base.model.Confiq;
import com.tut.abiz.base.model.FragmentPack;
import com.tut.abiz.base.model.GeneralModel;
import com.tut.abiz.base.model.Group;
import com.tut.abiz.base.model.TagVisiblity;
import com.tut.abiz.base.util.Utils;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.tut.abiz.base.util.Utils.getFragPack;
import static com.tut.abiz.base.util.Utils.getTagVisFromPref;

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
    SharedPreferences pref, visiblityPref, isStringPref;

    public NetService(NetServiceListener netServiceListener, Context context) {
        listener = netServiceListener;
        dbHelper = new DbHelper(context);
        pref = context.getSharedPreferences(Consts.SHEREDPREF, MODE_PRIVATE);
        visiblityPref = context.getSharedPreferences(Consts.VISIBLITYPREF, MODE_PRIVATE);
        isStringPref = context.getSharedPreferences(Consts.ISSTRINGPREF, MODE_PRIVATE);
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
            fragmentPacks.add(getFragPack(generalModels, "page-" + i, new TagVisiblity(i + 1).fillMock(), i + 1));
        }
        return fragmentPacks;
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

    @Override
    public void onConfiqReady(Confiq confiqRemote) {
        this.confiqRemote = confiqRemote;
        wait4List = false;
    }

    @Override
    public void onGroupListReady(ArrayList<Group> groups, ArrayList<Integer> registered, ArrayList<Integer> ordered) {

    }

    @Override
    public void onUpdateAccountReady(int response) {

    }

}
