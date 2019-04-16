package com.tut.abiz.base.service;

import android.os.Bundle;

import com.tut.abiz.base.Consts;
import com.tut.abiz.base.R;
import com.tut.abiz.base.frags.ListPagerFrag;
import com.tut.abiz.base.frags.PagerFragment;
import com.tut.abiz.base.model.FragmentPack;
import com.tut.abiz.base.model.GeneralModel;
import com.tut.abiz.base.model.TagVisiblity;

import java.util.ArrayList;

/**
 * Created by abiz on 4/14/2019.
 */

public class GeneralService {

    private ArrayList<FragmentPack> allFragPacks;
    private ArrayList<GeneralModel> testGeneralList;

    public GeneralService() {
    }

    public ArrayList<GeneralModel> getTestGeneralList() {
        testGeneralList = new ArrayList<>();
        for (int i = 0; i < 170; i++) {
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
        data1.putInt(Consts.CURRENTPAGE, 0);
        data1.putInt(Consts.PAGELAYOUT, R.layout.pager_frag);
        FragmentPack fragmentPack1 = new FragmentPack("page-1", pFrag1, data1);

        PagerFragment pFrag2 = new PagerFragment();
        Bundle data2 = new Bundle();
        data2.putInt(Consts.CURRENTPAGE, 1);
        data2.putInt(Consts.PAGELAYOUT, R.layout.pager_frag);
        FragmentPack fragmentPack2 = new FragmentPack("page-2", pFrag2, data2);

        PagerFragment pFrag3 = new PagerFragment();
        Bundle data3 = new Bundle();
        data3.putInt(Consts.CURRENTPAGE, 2);
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
        data1.putInt(Consts.CURRENTPAGE, 0);
        data1.putInt(Consts.PAGELAYOUT, R.layout.list_frag);
        data1.putSerializable(Consts.GENERALLIST,getTestGeneralList());
        data1.putSerializable(Consts.VISIBLITY,new TagVisiblity().fillMock());
        FragmentPack fragmentPack1 = new FragmentPack("page-1", pFrag1, data1);

        ListPagerFrag pFrag2 = new ListPagerFrag();
        Bundle data2 = new Bundle();
        data2.putInt(Consts.CURRENTPAGE, 1);
        data2.putInt(Consts.PAGELAYOUT, R.layout.list_frag);
        data2.putSerializable(Consts.GENERALLIST,getTestGeneralList());
        data2.putSerializable(Consts.VISIBLITY,new TagVisiblity().fillMock());
        FragmentPack fragmentPack2 = new FragmentPack("page-2", pFrag2, data2);

        ListPagerFrag pFrag3 = new ListPagerFrag();
        Bundle data3 = new Bundle();
        data3.putInt(Consts.CURRENTPAGE, 2);
        data3.putInt(Consts.PAGELAYOUT, R.layout.list_frag);
        data3.putSerializable(Consts.GENERALLIST,getTestGeneralList());
        data3.putSerializable(Consts.VISIBLITY,new TagVisiblity().fillMock());
        FragmentPack fragmentPack3 = new FragmentPack("page-3", pFrag3, data3);

        allFragPacks.add(fragmentPack1);
        allFragPacks.add(fragmentPack2);
        allFragPacks.add(fragmentPack3);
        return allFragPacks;
    }

}
