package com.tut.abiz.base.acts;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import com.tut.abiz.base.R;
import com.tut.abiz.base.adapter.GeneralListAdapter;
import com.tut.abiz.base.model.GeneralModel;
import com.tut.abiz.base.model.TagVisiblity;

import java.util.ArrayList;
import java.util.HashMap;

import static com.tut.abiz.base.util.Utils.extractTitles;

/**
 * Created by abiz on 5/4/2019.
 */

public class SearchActivity extends BaseActivity {

    ListView listView;
    ArrayList<GeneralModel> generalList;
    GeneralListAdapter adapter;
    ArrayList<String> titles;
    HashMap<Integer, RadioButton> rbs;
    int checkedTable;
    HorizontalScrollView scrollView;
    String searchWord;
    EditText editText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.act_search_list);
        setSelectedTable(1);
        checkedTable = 1;
        if (getNavMenu() == R.id.nav_dbView) {
            generalList = service.getDBList();
        } else
            generalList = service.getTestGeneralList();
        TagVisiblity visiblity = new TagVisiblity(checkedTable).fillMock();
        listView = (ListView) findViewById(R.id.list_sheet);
        titles = extractTitles(generalList);
        setSelectedTable(1);
        adapter = new GeneralListAdapter(this, generalList, visiblity, titles, R.layout.sheet_itemlinear);
        listView.setAdapter(adapter);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        ViewCompat.setLayoutDirection(radioGroup, ViewCompat.LAYOUT_DIRECTION_RTL);
        scrollView = (HorizontalScrollView) findViewById(R.id.scro);
        scrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
            }
        }, 100L);
        rbs = new HashMap<>();
        for (int i = 0; i < getTablesCount(); i++) {
            RadioButton rb = (RadioButton) radioGroup.getChildAt(5 - i);
            rb.setText(getTableName(i));
            if (i == 0)
                rb.setChecked(true);
            rb.setTag(i + "");
            rb.setVisibility(View.VISIBLE);
            rbs.put(rb.getId(), rb);

            editText = (EditText) findViewById(R.id.search_word);

            ImageButton srch = (ImageButton) findViewById(R.id.search_btn);
            srch.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    searchWord = editText.getText().toString().trim();
                    generalList.clear();
                    titles.clear();
                    ArrayList<GeneralModel> allGeneralFrom;
                    if (searchWord.isEmpty())
                        allGeneralFrom = service.getAllGeneralFrom(checkedTable);
                    else
                        allGeneralFrom = service.getAllGeneralFrom(checkedTable, searchWord, getTagVisiblity(checkedTable));
                    for (GeneralModel model : allGeneralFrom) {
                        getGeneralList().add(model);
                        titles.add(model.getTitle());
                    }
                    adapter.notifyDataSetChanged();
                }
            });
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                RadioButton radioButton = rbs.get(i);
                for (Integer key : rbs.keySet()) {
                    rbs.get(key).setChecked(false);
                }
                radioButton.setChecked(true);
                checkedTable = Integer.parseInt(radioButton.getTag().toString()) + 1;
            }
        });

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
}
