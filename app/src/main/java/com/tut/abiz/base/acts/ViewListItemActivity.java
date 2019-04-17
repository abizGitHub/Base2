package com.tut.abiz.base.acts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.tut.abiz.base.Consts;
import com.tut.abiz.base.R;
import com.tut.abiz.base.listener.CheckListener;
import com.tut.abiz.base.model.GeneralModel;

/**
 * Created by abiz on 4/17/2019.
 */

public class ViewListItemActivity extends AppCompatActivity {

    Toolbar toolbar;
    GeneralModel generalModel;
    TextView title, body, footL, headerL,
            headerR, footR;
    ToggleButton star;
    ViewGroup layout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sheet_linear);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        generalModel = (GeneralModel) getIntent().getExtras().getSerializable(Consts.GENERALMODEL);
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
        star = (ToggleButton) findViewById(R.id.sheetStar);
        star.setChecked(generalModel.getStared());
        star.setOnCheckedChangeListener(new CheckListener(generalModel, this));
    }

}
