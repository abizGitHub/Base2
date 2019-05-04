package com.tut.abiz.base.listener;

import android.widget.CompoundButton;
import android.widget.Toast;

import com.tut.abiz.base.R;
import com.tut.abiz.base.acts.BaseActivity;
import com.tut.abiz.base.adapter.GeneralListAdapter;
import com.tut.abiz.base.model.GeneralModel;

import java.util.ArrayList;

/**
 * Created by abiz on 4/17/2019.
 */

public class CheckListener implements CompoundButton.OnCheckedChangeListener {

    private final ArrayList<String> titles;
    private final int position;
    BaseActivity context;
    ArrayList<GeneralModel> generalModels;
    GeneralListAdapter adapter;

    public CheckListener(BaseActivity baseActivity, GeneralListAdapter adapter, ArrayList<GeneralModel> generalModels, ArrayList<String> titles, int position) {
        this.context = baseActivity;
        this.adapter = adapter;
        this.generalModels = generalModels;
        this.titles = titles;
        this.position = position;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        context.getDbHelper().changeStateOf(context.getSelectedTable(), generalModels.get(position).getId(), b);
        Toast.makeText(context, context.getSelectedTable() + ">" + generalModels.get(position).getId() + ":state>>" + b, Toast.LENGTH_SHORT).show();
        Toast.makeText(context, context.getNavTitle(), Toast.LENGTH_SHORT).show();
        generalModels.get(position).setStared(b);
        if (context.getNavMenu() == R.id.nav_staredList) {
            titles.remove(titles.get(position));
            generalModels.remove(generalModels.get(position));
            if (adapter != null)
                adapter.notifyDataSetChanged();
        }
        context.onStarChanged(position, b);
    }

}
