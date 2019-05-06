package com.tut.abiz.base.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.tut.abiz.base.Consts;
import com.tut.abiz.base.R;
import com.tut.abiz.base.acts.BaseActivity;
import com.tut.abiz.base.acts.ViewListItemActivity;
import com.tut.abiz.base.frags.ListPagerFrag;
import com.tut.abiz.base.listener.CheckListener;
import com.tut.abiz.base.model.GeneralModel;
import com.tut.abiz.base.model.TagVisiblity;
import com.tut.abiz.base.service.DbHelper;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by abiz on 4/14/2019.
 */

public class GeneralListAdapter extends ArrayAdapter {

    BaseActivity baseActivity;
    ArrayList<GeneralModel> generalModels;
    TagVisiblity visiblity;
    int sheet_itemLayout;
    TextView title, body, footL, headerL,
            headerR, footR;
    ToggleButton star;
    View row;
    ViewGroup layout;
    ArrayList<String> titles;
    Intent intent;

    public GeneralListAdapter(BaseActivity baseActivity, ArrayList<GeneralModel> generalModels, TagVisiblity visiblity, ArrayList<String> titles, int sheet_item) {
        super(baseActivity, sheet_item, R.id.sheetTitle, titles);
        this.generalModels = generalModels;
        this.baseActivity = baseActivity;
        this.visiblity = visiblity;
        this.sheet_itemLayout = sheet_item;
        this.titles = titles;
        intent = new Intent(baseActivity, ViewListItemActivity.class);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        row = inflater.inflate(sheet_itemLayout, parent, false);
        layout = (ViewGroup) row.findViewById(R.id.item_content);
        int minHeight = 0;
        int rowHeight = 100;
        title = (TextView) row.findViewById(R.id.sheetTitle);
        if (visiblity.isTitleVisible()) {
            title.setText(generalModels.get(position).getTitle());
            minHeight += rowHeight;
        } else {
            layout.removeView(title);
            title.setVisibility(View.INVISIBLE);
        }
        body = (TextView) row.findViewById(R.id.sheetBody);
        if (visiblity.isBodyVisible()) {
            body.setText(generalModels.get(position).getBody());
            minHeight += rowHeight;
        } else {
            layout.removeView(body);
            body.setVisibility(View.INVISIBLE);
        }
        headerL = (TextView) row.findViewById(R.id.sheetHeaderL);
        if (visiblity.isHeaderLVisible()) {
            headerL.setText(generalModels.get(position).getHeaderL());
            minHeight += rowHeight;
        } else {
            layout.removeView(headerL);
            headerL.setVisibility(View.INVISIBLE);
        }
        headerR = (TextView) row.findViewById(R.id.sheetHeaderR);
        if (visiblity.isHeaderRVisible()) {
            headerR.setText(generalModels.get(position).getHeaderR());
            if (!visiblity.isHeaderLVisible())
                minHeight += rowHeight;
        } else {
            layout.removeView(headerR);
            headerR.setVisibility(View.INVISIBLE);
        }
        footL = (TextView) row.findViewById(R.id.sheetFootL);
        if (visiblity.isFooterLVisible()) {
            footL.setText(generalModels.get(position).getFooterL());
            minHeight += rowHeight;
        } else {
            layout.removeView(footL);
            footL.setVisibility(View.INVISIBLE);
        }
        footR = (TextView) row.findViewById(R.id.sheetFootR);
        if (visiblity.isFooterRVisible()) {
            footR.setText(generalModels.get(position).getFooterR());
            if (!visiblity.isFooterLVisible())
                minHeight += rowHeight;
        } else {
            layout.removeView(footR);
            footR.setVisibility(View.INVISIBLE);
        }
        star = (ToggleButton) row.findViewById(R.id.sheetStar);
        if (visiblity.isStarVisible()) {
            star.setChecked(generalModels.get(position).getStared());
            star.setOnCheckedChangeListener(new CheckListener(baseActivity, this, generalModels, titles, position));
            minHeight += rowHeight;
        } else {
            layout.removeView(star);
            star.setVisibility(View.INVISIBLE);
        }
        if (sheet_itemLayout == R.layout.sheet_itemlinear)
            doMoreRemove();
        layout.setOnClickListener(new LayoutClickListener(this, generalModels, titles, position));
        layout.setMinimumHeight(minHeight);
        return row;
    }

    private void doMoreRemove() {
        if (!visiblity.isHeaderLVisible() && !visiblity.isHeaderRVisible())
            layout.removeView(row.findViewById(R.id.sheetHeaderLR));
        if (!visiblity.isFooterLVisible() && !visiblity.isFooterRVisible())
            layout.removeView(row.findViewById(R.id.sheetFootRL));
        if (!visiblity.isTitleVisible() && !visiblity.isStarVisible())
            layout.removeView(row.findViewById(R.id.sheetTitle_S));
        if (!visiblity.isBodyVisible())
            layout.removeView(row.findViewById(R.id.sheetBody));
    }

    class LayoutClickListener implements View.OnClickListener {
        private GeneralListAdapter adapter;
        private ArrayList<GeneralModel> generalModels;
        private ArrayList<String> titles;
        private int position;

        public LayoutClickListener(GeneralListAdapter adapter, ArrayList<GeneralModel> generalModels, ArrayList<String> titles, int position) {
            this.adapter = adapter;
            this.generalModels = generalModels;
            this.titles = titles;
            this.position = position;
        }

        @Override
        public void onClick(View view) {
            intent.putExtra(Consts.GENERALMODEL, generalModels.get(position));
            intent.putExtra(Consts.IX, position);
            intent.putExtra(Consts.NAVPAGER, baseActivity.getNavMenu());
            intent.putExtra(Consts.TITLES, titles.get(position));
            intent.putExtra(Consts.CURRENTPAGE, baseActivity.getSelectedTable());
            intent.putExtra(Consts.NAVTITLE, baseActivity.getNavTitle() + " detail");
            baseActivity.startActivity(intent);
        }
    }

}
