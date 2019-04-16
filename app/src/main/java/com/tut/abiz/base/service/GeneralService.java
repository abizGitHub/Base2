package com.tut.abiz.base.service;

import com.tut.abiz.base.R;
import com.tut.abiz.base.frags.PagerFragment;
import com.tut.abiz.base.model.FragmentPack;
import com.tut.abiz.base.model.GeneralModel;

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
        FragmentPack fragmentPack1 = new FragmentPack("page-1",new PagerFragment(), R.layout.pager_frag);
        FragmentPack fragmentPack2 = new FragmentPack("page-2",new PagerFragment(), R.layout.pager_frag);
        FragmentPack fragmentPack3 = new FragmentPack("page-3",new PagerFragment(), R.layout.pager_frag);
        allFragPacks.add(fragmentPack1);
        allFragPacks.add(fragmentPack2);
        allFragPacks.add(fragmentPack3);
        return allFragPacks;
    }

}
