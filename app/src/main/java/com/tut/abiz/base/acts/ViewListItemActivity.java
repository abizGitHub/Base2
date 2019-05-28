package com.tut.abiz.base.acts;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.tut.abiz.base.Consts;
import com.tut.abiz.base.R;
import com.tut.abiz.base.adapter.ImageV;
import com.tut.abiz.base.listener.CheckListener;
import com.tut.abiz.base.model.GeneralModel;

import java.util.ArrayList;

/**
 * Created by abiz on 4/17/2019.
 */

public class ViewListItemActivity extends BaseActivity {

    GeneralModel generalModel;
    String title$;
    int position;
    TextView title, body, footL, headerL,
            headerR, footR;
    ImageV star;
    ViewGroup layout;
    AppCompatButton btnImg;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sheet_linear);
        generalModel = (GeneralModel) getIntent().getExtras().getSerializable(Consts.GENERALMODEL);
        title$ = (String) getIntent().getExtras().getSerializable(Consts.TITLES);
        position = getIntent().getExtras().getInt(Consts.IX);
        layout = (ViewGroup) findViewById(R.id.item_content);
        title = (TextView) findViewById(R.id.sheetTitle);
        title.setText(generalModel.getTitle());
        body = (TextView) findViewById(R.id.sheetBody);
        body.setText(generalModel.getBody());
        headerL = (TextView) findViewById(R.id.sheetHeaderL);
        headerL.setText(generalModel.getHeaderL());
        headerR = (TextView) findViewById(R.id.sheetHeaderR);
        headerR.setText(generalModel.getHeaderR());
        footL = (TextView) findViewById(R.id.sheetFootL);
        footL.setText(generalModel.getFooterL());
        footR = (TextView) findViewById(R.id.sheetFootR);
        footR.setText(generalModel.getFooterR());
        star = (ImageV) findViewById(R.id.sheetStar);
        star.setScale(0.7f);
        ((ImageV) findViewById(R.id.sheetStar2)).setScale(0.7f);
        star.setChecked(generalModel.getStared());
        star.setOnCheckedChangeListener(new CheckListener(this, null, generalModel, position));
        btnImg = (AppCompatButton) findViewById(R.id.viewImage);
        btnImg.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Boolean hasUserPermision = getDbHelper().getBriefConfiq().getHasUserPermision();
                if (hasUserPermision == null || !hasUserPermision) {
                    startDialog(getResources().getString(R.string.accountNotRegistered));
                    return;
                }
                Intent intent = new Intent(ViewListItemActivity.this, DownLoadImgAct.class);
                intent.putExtra(Consts.GENERALMODEL, generalModel);
                intent.putExtra(Consts.CURRENTPAGE, ViewListItemActivity.this.getSelectedTable());
                startActivity(intent);
            }

        });

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
