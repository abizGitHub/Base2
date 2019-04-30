package com.tut.abiz.base.acts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.tut.abiz.base.Consts;
import com.tut.abiz.base.R;
import com.tut.abiz.base.adapter.GeneralListAdapter;
import com.tut.abiz.base.model.GeneralModel;
import com.tut.abiz.base.model.TagVisiblity;
import com.tut.abiz.base.service.GeneralService;

import java.util.ArrayList;

/**
 * Created by abiz on 4/14/2019.
 */

public class ListActivity extends AppCompatActivity {
    Toolbar toolbar;
    ListView listView;
    GeneralService service;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_list);
        service = new GeneralService(this);
        ArrayList<GeneralModel> generalList = null;
        if (((Integer) getIntent().getExtras().get(Consts.GENERALLIST)).equals(R.id.nav_dbView)) {
            generalList = service.getDBList();
        } else
            generalList = (ArrayList<GeneralModel>) getIntent().getExtras().get(Consts.GENERALLIST);
        TagVisiblity visiblity = (TagVisiblity) getIntent().getExtras().get(Consts.VISIBLITY);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        listView = (ListView) findViewById(R.id.list_sheet);
        ArrayList<String> titles = extractTitles(generalList);
        GeneralListAdapter adapter = new GeneralListAdapter(this, generalList, visiblity, titles, R.layout.sheet_itemlinear);
        listView.setAdapter(adapter);
    }

    private ArrayList<String> extractTitles(ArrayList<GeneralModel> generalList) {
        ArrayList<String> titles = new ArrayList<>();
        for (GeneralModel generalModel : generalList) {
            titles.add(generalModel.getTitle());
        }
        return titles;
    }


}

