package com.tut.abiz.base.frags;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tut.abiz.base.R;


/**
 * Created by abiz on 4/11/2019.
 */

public class Frag1 extends Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.println(Log.ERROR, "11111", "11111");
        View view = inflater.inflate(R.layout.frag_first, container, false);
        return view;
    }



}
