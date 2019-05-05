package com.tut.abiz.base.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.tut.abiz.base.Consts;
import com.tut.abiz.base.model.FragmentPack;
import com.tut.abiz.base.model.GeneralModel;

import java.util.ArrayList;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by abiz on 5/5/2019.
 */

public class OffLineTestService {

    private ArrayList<FragmentPack> allFragPacks;
    private ArrayList<GeneralModel> testGeneralList;
    private DbHelper dbHelper;
    private SharedPreferences pref, visiblityPref, isStringPref;

    public OffLineTestService(Context context) {
        dbHelper = new DbHelper(context);
        pref = context.getSharedPreferences(Consts.SHEREDPREF, MODE_PRIVATE);
        visiblityPref = context.getSharedPreferences(Consts.VISIBLITYPREF, MODE_PRIVATE);
        isStringPref = context.getSharedPreferences(Consts.ISSTRINGPREF, MODE_PRIVATE);
    }


    public void fillMockForTest() {
        testGeneralList = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            GeneralModel generalModel = new GeneralModel();
            generalModel.fillMock();
            testGeneralList.add(generalModel);
        }


    }

}
