package com.tut.abiz.base.listener;

import android.content.Context;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.tut.abiz.base.model.GeneralModel;

/**
 * Created by abiz on 4/17/2019.
 */

public class CheckListener implements CompoundButton.OnCheckedChangeListener {

    GeneralModel generalModel;
    Context context;

    public CheckListener(GeneralModel generalModel, Context context) {
        this.context = context;
        this.generalModel = generalModel;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        //boolean b1 = dbHelper.changeStateOf(DbHelper.MOZ_TABLE, ids.get(ix), b);
        Toast.makeText(context, generalModel.getId() + ":state>>" + b, Toast.LENGTH_SHORT).show();
    }

}
