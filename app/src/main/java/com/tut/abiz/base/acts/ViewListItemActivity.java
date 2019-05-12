package com.tut.abiz.base.acts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.tut.abiz.base.Consts;
import com.tut.abiz.base.R;
import com.tut.abiz.base.adapter.GeneralListAdapter;
import com.tut.abiz.base.listener.CheckListener;
import com.tut.abiz.base.model.GeneralModel;

import java.util.ArrayList;

/**
 * Created by abiz on 4/17/2019.
 */

public class ViewListItemActivity extends BaseActivity {

    GeneralModel generalModel_;
    String title_;
    int position;
    TextView title, body, footL, headerL,
            headerR, footR;
    ToggleButton star;
    ViewGroup layout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sheet_linear);
        generalModel_ = (GeneralModel) getIntent().getExtras().getSerializable(Consts.GENERALMODEL);
        title_ = (String) getIntent().getExtras().getSerializable(Consts.TITLES);
        position = getIntent().getExtras().getInt(Consts.IX);
        layout = (ViewGroup) findViewById(R.id.item_content);
        title = (TextView) findViewById(R.id.sheetTitle);
        title.setText(generalModel_.getTitle());
        body = (TextView) findViewById(R.id.sheetBody);
        body.setText(generalModel_.getBody());
        headerL = (TextView) findViewById(R.id.sheetHeaderL);
        headerL.setText(generalModel_.getHeaderL());
        headerR = (TextView) findViewById(R.id.sheetHeaderR);
        headerR.setText(generalModel_.getHeaderR());
        footL = (TextView) findViewById(R.id.sheetFootL);
        footL.setText(generalModel_.getFooterL());
        footR = (TextView) findViewById(R.id.sheetFootR);
        footR.setText(generalModel_.getFooterR());
        star = (ToggleButton) findViewById(R.id.sheetStar);
        star.setChecked(generalModel_.getStared());
        star.setOnCheckedChangeListener(new CheckListener(this, null, generalModel_, position));
    }

    @Override
    protected void doStaredTasks() {

    }

    @Override
    protected ArrayAdapter getListAdapter() {

        return null;
    }

    @Override
    protected ArrayList<String> getGeneralTitles() {

        return null;
    }

    @Override
    protected ArrayList<GeneralModel> getGeneralList() {

        return null;
    }

    @Override
    public void onStarChanged(int position, boolean checked) {
        super.clearStaredPosition();
        super.setStaredPosition(position);
        super.setNewChange();
        super.setIsStared(checked);
    }

}
