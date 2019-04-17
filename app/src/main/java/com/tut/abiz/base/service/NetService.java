package com.tut.abiz.base.service;

import android.os.Bundle;

import com.tut.abiz.base.Consts;
import com.tut.abiz.base.NetServiceListener;
import com.tut.abiz.base.R;
import com.tut.abiz.base.async.BrowsePageTask;
import com.tut.abiz.base.async.GetListTask;
import com.tut.abiz.base.frags.ListPagerFrag;
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

    public NetService(NetServiceListener netServiceListener) {
        listener = netServiceListener;
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
            doGetList("http://10.0.2.2:8085/rest/gm/getTestGeneralList");
            while (wait4List) {
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

}
