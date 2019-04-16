package com.tut.abiz.base;

/**
 * Created by abiz on 4/15/2019.
 */

public interface NetServiceListener {
    public void onTextResposeReady(String response);
    public void onFailure(String error);
}
