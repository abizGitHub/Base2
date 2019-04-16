package com.tut.abiz.base.service;

import com.tut.abiz.base.NetServiceListener;
import com.tut.abiz.base.async.BrowsePageTask;

/**
 * Created by abiz on 4/15/2019.
 */

public class NetService {
    BrowsePageTask browsePageTask;
    NetServiceListener listener;

    public NetService(NetServiceListener netServiceListener) {
        listener = netServiceListener;
    }

    public void browsePage(String urlString) {
        browsePageTask = new BrowsePageTask(listener);
        browsePageTask.setUrl(urlString);
        browsePageTask.execute("");
    }


}
