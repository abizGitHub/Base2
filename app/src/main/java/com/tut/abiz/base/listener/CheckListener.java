package com.tut.abiz.base.listener;

import android.widget.CompoundButton;
import android.widget.Toast;

import com.tut.abiz.base.R;
import com.tut.abiz.base.acts.BaseActivity;
import com.tut.abiz.base.acts.ViewListItemActivity;
import com.tut.abiz.base.adapter.GeneralListAdapter;
import com.tut.abiz.base.model.GeneralModel;

import java.util.ArrayList;

/**
 * Created by abiz on 4/17/2019.
 */

public class CheckListener implements CompoundButton.OnCheckedChangeListener {

    ArrayList<String> titles;
    int position;
    BaseActivity context;
    ArrayList<GeneralModel> generalModels;
    GeneralListAdapter adapter;
    GeneralModel generalModel_;

    public CheckListener(BaseActivity baseActivity, GeneralListAdapter adapter, ArrayList<GeneralModel> generalModels, ArrayList<String> titles, int position) {
        this.context = baseActivity;
        this.adapter = adapter;
        this.generalModels = generalModels;
        this.titles = titles;
        this.position = position;
    }

    public CheckListener(ViewListItemActivity baseActivity, GeneralListAdapter adapter, GeneralModel generalModel_, int position) {
        this.context = baseActivity;
        this.adapter = adapter;
        this.generalModel_ = generalModel_;
        this.position = position;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (generalModels != null)
            generalModel_ = generalModels.get(position);
        context.getDbHelper().changeStateOf(context.getSelectedTable(), generalModel_.getId(), b);
        Toast.makeText(context, context.getSelectedTable() + ">" + generalModel_.getId() + ":state>>" + b, Toast.LENGTH_SHORT).show();
        Toast.makeText(context, context.getNavTitle(), Toast.LENGTH_SHORT).show();
        generalModel_.setStared(b);
        if (context.getNavMenu() == R.id.nav_staredList && generalModels != null) {
            titles.remove(titles.get(position));
            generalModels.remove(generalModels.get(position));
            if (adapter != null)
                adapter.notifyDataSetChanged();
        }
        context.onStarChanged(position, b);
    }

}
