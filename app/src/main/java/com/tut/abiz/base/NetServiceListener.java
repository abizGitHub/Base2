package com.tut.abiz.base;

import com.tut.abiz.base.model.Confiq;
import com.tut.abiz.base.model.GeneralModel;
import com.tut.abiz.base.model.Group;

import java.util.ArrayList;

/**
 * Created by abiz on 4/15/2019.
 */

public interface NetServiceListener {

    public void onTextResponseReady(String response);

    public void onFailure(String error);

    public void onGeneralListReady(ArrayList<GeneralModel> generalModels);

    public void onConfiqReady(Confiq confiq);

    public void onGroupListReady(ArrayList<Group> groups);

    public void onUpdateAccountReady(int response);

}
