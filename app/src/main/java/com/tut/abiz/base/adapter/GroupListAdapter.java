package com.tut.abiz.base.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.tut.abiz.base.R;
import com.tut.abiz.base.acts.BaseActivity;
import com.tut.abiz.base.model.Group;

import java.util.ArrayList;

/**
 * Created by abiz on 5/11/2019.
 */

public class GroupListAdapter extends ArrayAdapter {

    BaseActivity baseActivity;
    ArrayList<Group> groups;
    ToggleButton orderBtn;
    View row;
    ViewGroup layout;
    ArrayList<String> titles;
    TextView title, sub;
    ArrayAdapter adapter;

    public GroupListAdapter(BaseActivity baseActivity, ArrayList<Group> groups, ArrayList<String> titles) {
        super(baseActivity, R.layout.sheet_item_group, R.id.sheetTitle, titles);
        this.groups = groups;
        this.baseActivity = baseActivity;
        this.titles = titles;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row = inflater.inflate(R.layout.sheet_item_group, parent, false);
        adapter = this;
        layout = (ViewGroup) row.findViewById(R.id.item_content);
        orderBtn = (ToggleButton) row.findViewById(R.id.sheetOrder);
        title = (TextView) row.findViewById(R.id.sheetTitle);
        sub = (TextView) row.findViewById(R.id.sheetOrderSub);
        Group group = groups.get(position);
        //Log.e("id : " + group.getId() + " > ", group.getStatus() + "");
        title.setText(group.getName());
        if (group.getStatus() == Group.UNREGISTERED) {
            orderBtn.setChecked(false);
            sub.setText(baseActivity.getResources().getString(R.string.add));
        }
        if (group.getStatus() == Group.REGISTERED) {
            sub.setText(baseActivity.getResources().getString(R.string.registered));
            orderBtn.setChecked(false);
            orderBtn.setVisibility(View.INVISIBLE);
        }
        if (group.getStatus() == Group.ORDERED) {
            orderBtn.setChecked(true);
            sub.setText(baseActivity.getResources().getString(R.string.ordered));
        }
        orderBtn.setOnCheckedChangeListener(new OrderListener(position));
        return row;
    }

    class OrderListener implements CompoundButton.OnCheckedChangeListener {
        int position;

        public OrderListener(int position) {
            this.position = position;
        }

        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            Group group = groups.get(position);
            if (b) {
                //sub.setText(baseActivity.getResources().getString(R.string.ordered));
                group.setStatus(Group.ORDERED);
                baseActivity.getDbHelper().changeStateOfGroup(group.getId(), Group.ORDERED);
            } else {
                //sub.setText(baseActivity.getResources().getString(R.string.add));
                group.setStatus(Group.UNREGISTERED);
                baseActivity.getDbHelper().changeStateOfGroup(group.getId(), Group.UNREGISTERED);
            }
            Toast.makeText(baseActivity, adapter + ">" + b + ">" + group.getStatus(), Toast.LENGTH_SHORT).show();
            adapter.notifyDataSetChanged();
        }

    }

}

