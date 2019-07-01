package com.tut.abiz.base.frags;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.tut.abiz.base.Consts;
import com.tut.abiz.base.R;
import com.tut.abiz.base.acts.BaseActivity;
import com.tut.abiz.base.adapter.GeneralListAdapter;
import com.tut.abiz.base.adapter.GroupListAdapter;
import com.tut.abiz.base.model.GeneralModel;
import com.tut.abiz.base.model.Group;
import com.tut.abiz.base.model.TagVisiblity;
import com.tut.abiz.base.util.Utils;

import java.util.ArrayList;

/**
 * Created by abiz on 4/16/2019.
 */

public class ListPagerFrag extends PagerFragment {
    ListView listView;
    View view;
    ArrayList<GeneralModel> generalList;
    ArrayList<Group> groupList;
    TagVisiblity visiblity;
    ArrayAdapter adapter;
    ArrayList<String> titles;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data = getArguments();
        generalList = (ArrayList<GeneralModel>) data.get(Consts.GENERALLIST);
        visiblity = (TagVisiblity) data.get(Consts.VISIBLITY);
        groupList = (ArrayList<Group>) data.get(Consts.GROUPLIST);
    }

    private ArrayList<String> extractTitles(ArrayList<GeneralModel> generalList) {
        ArrayList<String> titles = new ArrayList<>();
        for (GeneralModel generalModel : generalList) {
            titles.add(generalModel.getTitle());
        }
        return titles;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = super.onCreateView(inflater, container, savedInstanceState);
        listView = (ListView) view.findViewById(R.id.list_sheet);

        if (R.id.nav_Group == ((BaseActivity) getActivity()).getNavMenu()) {
            titles = Utils.extractGroupTitles(groupList);
            adapter = new GroupListAdapter((BaseActivity) getActivity(), groupList, titles, ix);
        } else {
            titles = extractTitles(generalList);
            adapter = new GeneralListAdapter((BaseActivity) getActivity(), generalList, visiblity, titles, R.layout.sheet_itemlinear);
        }
        listView.setAdapter(adapter);
        return view;
    }

    public ArrayList<GeneralModel> getGeneralList() {
        return generalList;
    }

    public ArrayAdapter getAdapter() {
        return adapter;
    }

    public ArrayList<String> getTitles() {
        return titles;
    }
}
