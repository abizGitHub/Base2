package com.tut.abiz.base.acts;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.tut.abiz.base.Consts;
import com.tut.abiz.base.NetServiceListener;
import com.tut.abiz.base.R;
import com.tut.abiz.base.adapter.GeneralListAdapter;
import com.tut.abiz.base.model.Confiq;
import com.tut.abiz.base.model.GeneralModel;
import com.tut.abiz.base.model.Group;
import com.tut.abiz.base.model.TagVisiblity;
import com.tut.abiz.base.service.NetService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abiz on 4/15/2019.
 */

public class NetConnectionActivity extends AppCompatActivity implements NetServiceListener {
    Toolbar toolbar;
    EditText urlView;
    TextView responseView;
    Button netBtn;
    String urlString;
    NetService netService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_net_connection);
        if (android.os.Build.VERSION.SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        netBtn = (Button) findViewById(R.id.netGet_btn);
        urlView = (EditText) findViewById(R.id.url);
        responseView = (TextView) findViewById(R.id.net_response);
        netService = new NetService(this, this);
        netBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                urlString = urlView.getText().toString();
                netService.browsePage(urlString);
            }
        });

    }

    @Override
    public void onTextResponseReady(String response) {
        responseView.setText(response);
    }

    @Override
    public void onFailure(String error) {
        responseView.setText(error);
    }

    @Override
    public void onGeneralListReady(ArrayList<GeneralModel> generalModels) {

    }

    @Override
    public void onConfiqReady(Confiq confiq) {

    }

    @Override
    public void onGroupListReady(ArrayList<Group> groups, ArrayList<Integer> registered, ArrayList<Integer> ordered) {

    }

    @Override
    public void onUpdateAccountReady(int response) {

    }

}