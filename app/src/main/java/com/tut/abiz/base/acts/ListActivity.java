package com.tut.abiz.base.acts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.tut.abiz.base.Consts;
import com.tut.abiz.base.R;
import com.tut.abiz.base.adapter.GeneralListAdapter;
import com.tut.abiz.base.adapter.GroupListAdapter;
import com.tut.abiz.base.model.GeneralModel;
import com.tut.abiz.base.model.Group;
import com.tut.abiz.base.model.TagVisiblity;

import java.util.ArrayList;

import static com.tut.abiz.base.util.Utils.extractGeneralTitles;
import static com.tut.abiz.base.util.Utils.extractGroupTitles;

/**
 * Created by abiz on 4/14/2019.
 */

public class ListActivity extends BaseActivity {

    ListView listView;
    ArrayList<GeneralModel> generalList;
    ArrayAdapter adapter;
    ArrayList<String> titles;
    ArrayList<Group> groups;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.act_list);
        setSelectedTable(1);
        if (getNavMenu() == R.id.nav_dbView) {
            generalList = service.getDBList();
        } else if (R.id.nav_Group == getNavMenu()) {
            groups = service.getGroupList();
        } else
            generalList = (ArrayList<GeneralModel>) getIntent().getExtras().get(Consts.GENERALLIST);
        TagVisiblity visiblity = (TagVisiblity) getIntent().getExtras().get(Consts.VISIBLITY);
        listView = (ListView) findViewById(R.id.list_sheet);
        setSelectedTable(1);
        if (R.id.nav_Group == getNavMenu()){
            titles = extractGroupTitles(groups);
            adapter = new GroupListAdapter(this, groups, titles);
        }
        else{
            titles = extractGeneralTitles(generalList);
            adapter = new GeneralListAdapter(this, generalList, visiblity, titles, R.layout.sheet_itemlinear);
        }
        listView.setAdapter(adapter);
    }

    @Override
    protected void doStaredTasks() {

    }

    @Override
    protected ArrayAdapter getListAdapter() {
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

}

