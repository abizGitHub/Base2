package com.tut.abiz.base.acts;

import android.os.Bundle;
import android.support.annotation.Nullable;
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

public class ListActivity extends BaseActivity {

    ListView listView;
    GeneralService service;
    ArrayList<GeneralModel> generalList;
    GeneralListAdapter adapter;
    ArrayList<String> titles;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.act_list);
        setSelectedTable(1);
        service = new GeneralService(this);
        if (getNavMenu() == R.id.nav_dbView) {
            generalList = service.getDBList();
        } else
            generalList = (ArrayList<GeneralModel>) getIntent().getExtras().get(Consts.GENERALLIST);
        TagVisiblity visiblity = (TagVisiblity) getIntent().getExtras().get(Consts.VISIBLITY);
        listView = (ListView) findViewById(R.id.list_sheet);
        titles = extractTitles(generalList);
        setSelectedTable(1);
        adapter = new GeneralListAdapter(this, generalList, visiblity, titles, R.layout.sheet_itemlinear);
        listView.setAdapter(adapter);
    }

    @Override
    protected void doStaredTasks() {

    }

    @Override
    protected GeneralListAdapter getGeneralListAdapter() {
        return adapter;
    }

    @Override
    protected ArrayList<String> getGeneralTitles() {
        return titles;
    }

    @Override
    protected ArrayList<GeneralModel> getGeneralList() {
        return generalList;
    }

    @Override
    public void onStarChanged(int position, boolean checked) {

    }


    private ArrayList<String> extractTitles(ArrayList<GeneralModel> generalList) {
        ArrayList<String> titles = new ArrayList<>();
        for (GeneralModel generalModel : generalList) {
            titles.add(generalModel.getTitle());
        }
        return titles;
    }


}

