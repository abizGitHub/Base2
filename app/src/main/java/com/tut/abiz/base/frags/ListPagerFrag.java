package com.tut.abiz.base.frags;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.tut.abiz.base.Consts;
import com.tut.abiz.base.R;
import com.tut.abiz.base.adapter.GeneralListAdapter;
import com.tut.abiz.base.model.GeneralModel;
import com.tut.abiz.base.model.TagVisiblity;

import java.util.ArrayList;

/**
 * Created by abiz on 4/16/2019.
 */

public class ListPagerFrag extends PagerFragment {
    ListView listView;
    View view;
    ArrayList<GeneralModel> generalList;
    TagVisiblity visiblity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data = getArguments();
        generalList = (ArrayList<GeneralModel>) data.get(Consts.GENERALLIST);
        visiblity = (TagVisiblity) data.get(Consts.VISIBLITY);
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
        ArrayList<String> titles = extractTitles(generalList);
        GeneralListAdapter adapter = new GeneralListAdapter(getActivity(), generalList, visiblity, titles, R.layout.sheet_itemlinear);
        listView.setAdapter(adapter);
        return view;
    }

}
