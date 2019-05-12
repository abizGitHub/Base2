package com.tut.abiz.base.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.tut.abiz.base.Consts;
import com.tut.abiz.base.NetServiceListener;
import com.tut.abiz.base.adapter.JsonUtil;
import com.tut.abiz.base.async.PostListTask;
import com.tut.abiz.base.model.Confiq;
import com.tut.abiz.base.model.GeneralModel;
import com.tut.abiz.base.model.Group;
import com.tut.abiz.base.model.TagVisiblity;
import com.tut.abiz.base.util.Utils;

import java.util.ArrayList;

/**
 * Created by abiz on 5/8/2019.
 */

public class PostListService extends IntentService implements NetServiceListener {

    static int created = 0, dead = 0;
    public static final String NOTIFICATION = "PostListService";
    public static final String UPDATEDCOUNT = "updated_Count";
    public static final String RESULT = "result";
    static int i = 0;
    int CONNECTED = 1;
    int CANTCONNECT = -1;
    private DbHelper dbHelper;
    private Confiq confiqRemote;
    private Boolean wait4List;
    private int waitCount;
    private ArrayList<GeneralModel> generalModels;
    private ArrayList<Group> groups;
    SharedPreferences pref, visiblityPref, isStringPref;

    public PostListService() {
        super("-haha-");
        created++;
        dbHelper = new DbHelper(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        pref = getApplicationContext().getSharedPreferences(Consts.SHEREDPREF, MODE_PRIVATE);
        visiblityPref = getSharedPreferences(Consts.VISIBLITYPREF, MODE_PRIVATE);
        isStringPref = getSharedPreferences(Consts.ISSTRINGPREF, MODE_PRIVATE);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Confiq confiqLocal = dbHelper.getConfiq();
        confiqRemote = doPostConfig(Consts.SERVERADDRESS + "/gm/getConfiq", confiqLocal);
        if (confiqRemote != null)
            if (i % 2 == 0) {
                applyNewUserSetting(confiqLocal, confiqRemote);
                applyNewModelMap(confiqRemote);
                applyNewTagVisiblity(confiqRemote, confiqLocal);
            } else {
                ArrayList<Integer> updateCount = applyNewGmList(confiqLocal, confiqRemote);
                publishResults(updateCount, CONNECTED);
            }
        i++;
        if (i > 10) i = 0;
        ArrayList<Integer> orderedGroups = dbHelper.getGroupsByStatus(Group.ORDERED);
        if (orderedGroups.size() > 0 || (confiqRemote != null && confiqRemote.getUpdateGroup() != null && confiqRemote.getUpdateGroup())) {
            ArrayList<Integer> registeredGroups = dbHelper.getGroupsByStatus(Group.REGISTERED);
            ArrayList<Group> groups = doPostGroups(orderedGroups, registeredGroups);
            if (groups != null && groups.size() > 0)
                dbHelper.updateGroups(groups);
        }
    }

    private ArrayList<Group> doPostGroups(ArrayList<Integer> orderedGroups, ArrayList<Integer> registeredGroups) {
        PostListTask postListTask = new PostListTask(this);
        postListTask.setUrlAndMessage(Consts.SERVERADDRESS + "/gm/groups", JsonUtil.parseGroups(orderedGroups, registeredGroups));
        postListTask.execute(PostListTask.GETGROUP);
        wait4List = true;
        waitCount = 0;
        while (wait4List) {
            try {
                waitCount++;
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Log.e(">>??", e.getMessage());
                e.printStackTrace();
            }
        }
        return groups;
    }


    private ArrayList<Integer> applyNewGmList(Confiq confiqLocal, Confiq confiqRemote) {
        ArrayList<Integer> updateCount = new ArrayList<>();
        int tableCount = pref.getInt(Consts.TABLECOUNT, 2);
        ArrayList<Long> lastIds = confiqLocal.getLastIds();
        for (int ix = 1; ix < tableCount + 1; ix++) {
            Log.e(ix + ">", confiqRemote.getLastIds().get(ix - 1) + "," + confiqLocal.getLastIds().get(ix - 1));
            if (confiqRemote != null && confiqRemote.getLastIds().get(ix - 1) > confiqLocal.getLastIds().get(ix - 1)) {
                ArrayList<GeneralModel> generalModels = doPostList(Consts.SERVERADDRESS + "/gm/" + ix + "/" + lastIds.get(ix - 1), confiqLocal);
                dbHelper.insertGMs(generalModels, ix);
                updateCount.add(generalModels.size());
            } else
                updateCount.add(0);
        }
        return updateCount;
    }

    private ArrayList<GeneralModel> doPostList(String url, Confiq confiqLocal) {
        PostListTask postListTask = new PostListTask(this);
        postListTask.setUrlAndMessage(url, JsonUtil.parseConfiq(confiqLocal));
        postListTask.execute(PostListTask.GETLIST);
        wait4List = true;
        waitCount = 0;
        while (wait4List) {
            try {
                waitCount++;
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return generalModels;
    }

    private Confiq doPostConfig(String url, Confiq localConfiq) {
        PostListTask postListTask = new PostListTask(this);
        postListTask.setUrlAndMessage(url, JsonUtil.parseConfiq(localConfiq));
        postListTask.execute(PostListTask.GETCONGIQ);
        wait4List = true;
        waitCount = 0;
        while (wait4List) {
            try {
                waitCount++;
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Log.e(">>??", e.getMessage());
                e.printStackTrace();
            }
        }
        return confiqRemote;
    }

    private void publishResults(ArrayList<Integer> list, int result) {
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra(UPDATEDCOUNT, list);
        intent.putExtra(RESULT, result);
        sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        dead++;
        super.onDestroy();
    }

    private void applyNewUserSetting(Confiq confiqLocal, Confiq confiqRemote) {

        if (confiqRemote.getHaveNewChange() != null && confiqRemote.getHaveNewChange()) {
            if (confiqRemote.getLastTablesName() != null && confiqRemote.getLastTablesName().size() > 0) {
                boolean shouldRename = confiqLocal.getLastTablesName().size() != confiqRemote.getLastTablesName().size();
                if (!shouldRename)
                    for (int j = 0; j < confiqRemote.getLastTablesName().size(); j++) {
                        shouldRename = !confiqRemote.getLastTablesName().get(j).equals(confiqLocal.getLastTablesName().get(j));
                        if (shouldRename)
                            break;
                    }

                if (shouldRename) {
                    for (int i = 0; i < 6; i++) {
                        pref.edit().remove(Consts.TABLENAMES[i]).apply();
                    }
                    int ix = 0;
                    for (String tName : confiqRemote.getLastTablesName()) {
                        pref.edit().putString(Consts.TABLENAMES[ix++], tName).apply();
                    }
                    pref.edit().putInt(Consts.TABLECOUNT, ix).apply();
                    dbHelper.updateTableNames(confiqRemote.getLastTablesName());
                }
            }
        }

        if (confiqRemote.getClearDB() != null && confiqRemote.getClearDB()) {
            dbHelper.clearDB();
            ArrayList<Long> lastIds = new ArrayList<>();
            ArrayList<String> lastTablesName = confiqRemote.getLastTablesName();
            for (String t : lastTablesName) {
                lastIds.add(0L);
            }
            confiqLocal.setLastTablesName(confiqRemote.getLastTablesName());
            confiqLocal.setLastIds(lastIds);
            dbHelper.insertModelMaps(confiqRemote.getLastModelMap());
            confiqLocal.setLastModelMap(confiqRemote.getLastModelMap());
            confiqLocal.setLastModelMapId(dbHelper.getLastModelMapId());
            dbHelper.updateTableNames(confiqRemote.getLastTablesName());
        }

    }

    private void applyNewTagVisiblity(Confiq confiqRemote, Confiq confiqLocal) {
        if (confiqRemote.getHaveNewChange() != null && confiqRemote.getHaveNewChange() && confiqRemote.getTagVisiblity() != null) {
            boolean notEqual = confiqLocal.getTagVisiblity().size() != confiqRemote.getTagVisiblity().size();
            if (!notEqual)
                for (int j = 0; j < confiqRemote.getTagVisiblity().size(); j++) {
                    notEqual = !confiqRemote.getTagVisiblity().get(j).equal(confiqLocal.getTagVisiblity().get(j));
                    if (notEqual)
                        break;
                }

            if (notEqual) {
                dbHelper.clearTagVisiblitys();
                dbHelper.insertTagVisiblitys(confiqRemote.getTagVisiblity());
                int ix = 1;
                for (TagVisiblity vis : confiqRemote.getTagVisiblity()) {
                    Utils.putVisInPref(vis, ix, visiblityPref, isStringPref);
                    ix++;
                }
            }

        }
    }

    private void applyNewModelMap(Confiq confiqRemote) {
        if (confiqRemote.getHaveNewChange() != null && confiqRemote.getHaveNewChange()) {
            if (confiqRemote.getLastModelMap() != null && confiqRemote.getLastModelMap().size() > 0)
                dbHelper.updateModelMap(confiqRemote.getLastModelMap());
            if (confiqRemote.getModelMap2Delete() != null && confiqRemote.getModelMap2Delete().size() > 0)
                dbHelper.deleteModelMap(confiqRemote.getModelMap2Delete());
        }
    }

    @Override
    public void onTextResponseReady(String response) {

    }

    @Override
    public void onFailure(String error) {

    }

    @Override
    public void onGeneralListReady(ArrayList<GeneralModel> generalModels) {
        this.generalModels = generalModels;
        wait4List = false;
    }

    @Override
    public void onConfiqReady(Confiq confiqRemote) {
        this.confiqRemote = confiqRemote;
        wait4List = false;
    }

    @Override
    public void onGroupListReady(ArrayList<Group> groups) {
        this.groups = groups;
        wait4List = false;
    }


}