package com.tut.abiz.base.service;

import android.app.IntentService;
import android.content.Context;
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
import com.tut.abiz.base.model.Message;
import com.tut.abiz.base.model.TagVisiblity;
import com.tut.abiz.base.model.UserAccount;
import com.tut.abiz.base.util.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by abiz on 5/8/2019.
 */

public class PostListService extends IntentService implements NetServiceListener {

    static int alive = 0;
    public static final String NOTIFICATION = "PostListService";
    public static final String UPDATEDCOUNT = "updated_Count";
    public static final String RESULT = "result";

    static int i = 0;
    private DbHelper dbHelper;
    private Confiq confiqRemote;
    private Boolean wait4List;
    private int waitCount;
    private ArrayList<GeneralModel> generalModels;
    private ArrayList<Group> groups;
    SharedPreferences pref, visiblityPref, isStringPref;
    Integer updateAccount;
    int wait4Server;
    boolean forceStop = false;
    private ArrayList<Message> recMessages;
    private ArrayList<Integer> deliveredMsgIds;

    public PostListService() {
        super("-PostListService-");
        alive++;
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
        wait4Server = pref.getInt(Confiq.WAIT4SERVER, Consts.DEFAULTWAIT4SERVER);
        Confiq confiqLocal = dbHelper.getConfiq();
        if (pref.getBoolean(Consts.SENDDETAICONFIG, true))
            confiqRemote = doPostConfig(Consts.SERVERADDRESS + Consts.ADDRESSCONFIQ, confiqLocal);
        else
            confiqRemote = doPostConfig(Consts.SERVERADDRESS + Consts.ADDRESSCONFIQ, dbHelper.getBriefConfiq());
        //Toast.makeText(getContext(), Consts.SERVERADDRESS + Consts.ADDRESSCONFIQ + ">" + confiqLocal.toString(), Toast.LENGTH_LONG).show();
        //Toast.makeText(getContext(), "rec>" + confiqRemote.toString(), Toast.LENGTH_LONG).show();
        if (confiqRemote != null)
            if (i % 3 == 0) {
                applyNewUserSetting(confiqLocal, confiqRemote);
                applyNewModelMap(confiqRemote);
                applyNewTagVisiblity(confiqRemote, confiqLocal);
            } else if (i % 3 == 2) {
                if (isMessageMenuOpened()) {
                    applyMessages(confiqLocal);
                    setMessageMenuClosed();
                } else {
                    i++;
                    return;
                }
            } else {
                ArrayList<Integer> updateCount = applyNewGmList(confiqLocal, confiqRemote);
                publishResults(updateCount, SchedulService.SERVERCONECTED);
            }
        i++;
        if (i > 10) i = 0;
        int tableCount = pref.getInt(Consts.TABLECOUNT, 2);
        if (confiqRemote != null)
            for (int j = 1; j < tableCount + 1; j++) {
                boolean newGroup = false;
                if (confiqRemote.getLastGroupIds() != null && confiqRemote.getLastGroupIds().size() > 1) {
                    newGroup = confiqRemote.getLastGroupIds().get(j - 1) > confiqLocal.getLastGroupIds().get(j - 1);
                }
                if (newGroup || isGroupMenuOpened() || (confiqRemote.getUpdateGroup() != null && confiqRemote.getUpdateGroup())) {
                    ArrayList<Integer> orderedGroups = dbHelper.getGroupsByStatus(j, Group.ORDERED);
                    //ArrayList<Integer> registeredGroups = dbHelper.getGroupsByStatus(j, Group.REGISTERED);
                    if (confiqLocal.getUserId() != Consts.NEWUSERID) {
                        if (orderedGroups.size() > 0)
                            doOrderGroup(orderedGroups, confiqLocal, j);
                        else
                            doDeleteOrder(j, confiqLocal);
                    }
                    ArrayList<Group> groups = doPostGroups(j, confiqLocal);
                    if (groups != null && groups.size() > 0)
                        dbHelper.updateGroups(groups, j);
                }
            }
        if (isGroupMenuOpened())
            setGroupMenuClosed();
        if (pref.getBoolean(Consts.USERACCOUNTEDITED, true)) {
            Integer response = doPostUserAccount(dbHelper.getUserAccount());
            if (response != null && response == Consts.USERREGISTERED)
                pref.edit().putBoolean(Consts.USERACCOUNTEDITED, false).apply();
        }
        if (forceStop || confiqRemote == null) {
            publishResults(null, SchedulService.SERVERNOTRESPOND);
        }
        if (confiqRemote != null && confiqRemote.getSendDetail() != null)
            pref.edit().putBoolean(Consts.SENDDETAICONFIG, confiqRemote.getSendDetail()).apply();
        if (confiqRemote != null && confiqRemote.getWait4Server() != null)
            pref.edit().putInt(Confiq.WAIT4SERVER, confiqRemote.getWait4Server()).apply();
        if (confiqRemote != null && confiqRemote.getConnectPeriod() != null)
            pref.edit().putInt(Confiq.CONNECTPERIOD, confiqRemote.getConnectPeriod()).apply();

    }

    private void applyMessages(Confiq confiqLocal) {
        if (confiqRemote.getLastMsgId() != null && confiqLocal.getLastMsgId() != null && confiqRemote.getLastMsgId() > confiqLocal.getLastMsgId()) {
            doGetMessages(confiqLocal);
            if (recMessages != null && !recMessages.isEmpty())
                for (Message message : recMessages) {
                    dbHelper.insertMessage(message, Message.RECEIPT);
                }
            recMessages = null;
        }
        ArrayList<Message> messages = dbHelper.getUnDeliveredMessages();
        if (messages != null && !messages.isEmpty()) {
            doPostMessages(messages, confiqLocal);
            if (deliveredMsgIds != null && !deliveredMsgIds.isEmpty())
                for (Integer msgId : deliveredMsgIds) {
                    dbHelper.setMessageDelivered(msgId.longValue());
                }
            deliveredMsgIds = null;
        }
    }

    private void doPostMessages(ArrayList<Message> messages, Confiq confiqLocal) {
        boolean haveNewMsg = false;
        for (Message message : messages) {
            if (!getSentMessage().contains("[" + message.getId() + "]")) {
                haveNewMsg = true;
                break;
            }
        }
        if (!haveNewMsg)
            return;
        PostListTask postListTask = new PostListTask(this);
        JSONObject json = JsonUtil.parseConfiq(confiqLocal);
        try {
            json.put(Consts.MESSAGELIST, JsonUtil.parseMsgs(messages));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        postListTask.setUrlAndMessage(Consts.SERVERADDRESS + Consts.ADDRESSSENDMESSAGE, json);
        postListTask.execute(PostListTask.SENDMSG);
        wait4List = true;
        waitCount = 0;
        forceStop = false;
        while (wait4List) {
            try {
                waitCount++;
                Thread.sleep(100);
            } catch (InterruptedException e) {
                //log.e(">>??", e.getMessage());
                e.printStackTrace();
            }
            if (waitCount > (wait4Server / 100)) {
                wait4List = false;
                forceStop = true;
            }
        }
        if (!forceStop) {
            StringBuffer buffer = new StringBuffer();
            for (Message message : messages) {
                buffer.append("[").append(message.getId()).append("]");
            }
            setSentMessage(buffer.toString() + getSentMessage());
        }
    }

    private void doGetMessages(Confiq confiqLocal) {
        PostListTask postListTask = new PostListTask(this);
        JSONObject json = JsonUtil.parseConfiq(confiqLocal);
        postListTask.setUrlAndMessage(Consts.SERVERADDRESS + Consts.ADDRESSRECEIVEMESSAGE, json);
        postListTask.execute(PostListTask.RECEIVEMSG);
        wait4List = true;
        waitCount = 0;
        forceStop = false;
        while (wait4List) {
            try {
                waitCount++;
                Thread.sleep(100);
            } catch (InterruptedException e) {
                //log.e(">>??", e.getMessage());
                e.printStackTrace();
            }
            if (waitCount > (wait4Server / 100)) {
                wait4List = false;
                forceStop = true;
            }
        }
    }

    private void doDeleteOrder(int j, Confiq confiq) {
        if (getDelGroup().contains("[" + j + "]"))
            return;
        PostListTask postListTask = new PostListTask(this);
        JSONObject json = JsonUtil.parseConfiq(confiq);
        try {
            json.put(Consts.TABLEID, j);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        postListTask.setUrlAndMessage(Consts.SERVERADDRESS + Consts.ADDRESSDELORDERGROUP, json);
        postListTask.execute(PostListTask.DELORDERGROUP);
        while (wait4List) {
            try {
                waitCount++;
                Thread.sleep(100);
            } catch (InterruptedException e) {
                //log.e(">>??", e.getMessage());
                e.printStackTrace();
            }
            if (waitCount > (wait4Server / 100)) {
                wait4List = false;
                forceStop = true;
            }
        }
        if (!forceStop) {
            setDelGroup("[" + j + "]" + getDelGroup());
        }
    }

    private void doOrderGroup(ArrayList<Integer> orderedGroups, Confiq confiq, int j) {
        boolean haveNewGroup = false;
        for (Integer orderedGroup : orderedGroups) {
            if (!getOrderedGroup().contains("[" + j + "-" + orderedGroup + "]")) {
                haveNewGroup = true;
                break;
            }
        }
        if (!haveNewGroup)
            return;
        PostListTask postListTask = new PostListTask(this);
        JSONObject json = JsonUtil.parseGroups(orderedGroups, Group.ORDERED$, JsonUtil.parseConfiq(confiq));
        try {
            json.put(Consts.TABLEID, j);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        postListTask.setUrlAndMessage(Consts.SERVERADDRESS + Consts.ADDRESSORDERGROUP, json);
        postListTask.execute(PostListTask.ORDERGROUP);
        wait4List = true;
        waitCount = 0;
        forceStop = false;
        while (wait4List) {
            try {
                waitCount++;
                Thread.sleep(100);
            } catch (InterruptedException e) {
                //log.e(">>??", e.getMessage());
                e.printStackTrace();
            }
            if (waitCount > (wait4Server / 100)) {
                wait4List = false;
                forceStop = true;
            }
        }
        if (!forceStop) {
            StringBuffer buffer = new StringBuffer();
            for (Integer orderedGroup : orderedGroups) {
                buffer.append("[").append(j).append("-").append(orderedGroup.toString()).append("]");
            }
            setOrderedGroup(buffer.toString() + getOrderedGroup());
        }
    }

    private void setGroupMenuClosed() {
        getSharedPreferences(Consts.SHEREDPREF, MODE_PRIVATE).edit().putBoolean(Consts.MENUGROUPOPENED, false).apply();
    }

    private boolean isGroupMenuOpened() {
        return getSharedPreferences(Consts.SHEREDPREF, MODE_PRIVATE).getBoolean(Consts.MENUGROUPOPENED, false);
    }

    private void setMessageMenuClosed() {
        getSharedPreferences(Consts.SHEREDPREF, MODE_PRIVATE).edit().putBoolean(Consts.MENUMESSAGEOPENED, false).apply();
    }

    private boolean isMessageMenuOpened() {
        return getSharedPreferences(Consts.SHEREDPREF, MODE_PRIVATE).getBoolean(Consts.MENUMESSAGEOPENED, false);
    }

    private void setOrderedGroup(String orderedGroup) {
        getSharedPreferences(Consts.SHEREDPREF, MODE_PRIVATE).edit().putString(Consts.ORDEREDGROUP, orderedGroup).apply();
    }

    private String getOrderedGroup() {
        return getSharedPreferences(Consts.SHEREDPREF, MODE_PRIVATE).getString(Consts.ORDEREDGROUP, "");
    }

    private void setSentMessage(String msg) {
        getSharedPreferences(Consts.SHEREDPREF, MODE_PRIVATE).edit().putString(Consts.SENTMESSAGE, msg).apply();
    }

    private String getSentMessage() {
        return getSharedPreferences(Consts.SHEREDPREF, MODE_PRIVATE).getString(Consts.SENTMESSAGE, "");
    }

    private void setDelGroup(String delGroup) {
        getSharedPreferences(Consts.SHEREDPREF, MODE_PRIVATE).edit().putString(Consts.DELGROUP, delGroup).apply();
    }

    private String getDelGroup() {
        return getSharedPreferences(Consts.SHEREDPREF, MODE_PRIVATE).getString(Consts.DELGROUP, "");
    }

    private Integer doPostUserAccount(UserAccount userAccount) {
        PostListTask postListTask = new PostListTask(this);
        postListTask.setUrlAndMessage(Consts.SERVERADDRESS + Consts.ADDRESSUPDATEUSER, JsonUtil.parseUserAccount(userAccount));
        postListTask.execute(PostListTask.EDITUSER);
        wait4List = true;
        waitCount = 0;
        forceStop = false;
        while (wait4List) {
            try {
                waitCount++;
                Thread.sleep(100);
            } catch (InterruptedException e) {
                //log.e(">>??", e.getMessage());
                e.printStackTrace();
            }
            if (waitCount > (wait4Server / 100)) {
                wait4List = false;
                forceStop = true;
            }
        }
        return updateAccount;
    }

    private ArrayList<Group> doPostGroups(int tableId, Confiq confiq) {
        PostListTask postListTask = new PostListTask(this);
        postListTask.setUrlAndMessage(Consts.SERVERADDRESS + Consts.ADDRESSGROUP + "/" + tableId, JsonUtil.parseConfiq(confiq));
        postListTask.execute(PostListTask.GETGROUP);
        wait4List = true;
        waitCount = 0;
        forceStop = false;
        while (wait4List) {
            try {
                waitCount++;
                Thread.sleep(100);
            } catch (InterruptedException e) {
                //log.e(">>??", e.getMessage());
                e.printStackTrace();
            }
            if (waitCount > (wait4Server / 100)) {
                wait4List = false;
                forceStop = true;
            }
        }
        return groups;
    }


    private ArrayList<Integer> applyNewGmList(Confiq confiqLocal, Confiq confiqRemote) {
        ArrayList<Integer> updateCount = new ArrayList<>();
        int tableCount = pref.getInt(Consts.TABLECOUNT, 2);
        ArrayList<Long> lastIds = confiqLocal.getLastIds();
        if (confiqRemote.getLastIds() != null)
            for (int ix = 1; ix < tableCount + 1; ix++) {
                //log.e("ix:" + ix + ">remote lastID:", confiqRemote.getLastIds().get(ix - 1) +
                       // " , local lastID:" + confiqLocal.getLastIds().get(ix - 1));
                if (confiqRemote != null && confiqRemote.getLastIds().get(ix - 1) > confiqLocal.getLastIds().get(ix - 1)) {
                    ArrayList<GeneralModel> generalModels = doPostList(Consts.SERVERADDRESS + "/gm/" + ix + "/" + lastIds.get(ix - 1), confiqLocal);
                    if (generalModels != null) {
                        dbHelper.insertGMs(generalModels, ix);
                        updateCount.add(generalModels.size());
                    } else
                        updateCount.add(0);
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
        forceStop = false;
        while (wait4List) {
            try {
                waitCount++;
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (waitCount > (wait4Server / 100)) {
                wait4List = false;
                forceStop = true;
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
        forceStop = false;
        while (wait4List) {
            try {
                waitCount++;
                Thread.sleep(100);
            } catch (InterruptedException e) {
                //log.e(">>??", e.getMessage());
                e.printStackTrace();
            }
            if (waitCount > (wait4Server / 100)) {
                forceStop = true;
                wait4List = false;
            }
        }
        return confiqRemote;
    }

    private void publishResults(ArrayList<Integer> list, String result) {
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra(UPDATEDCOUNT, list);
        intent.putExtra(RESULT, result);
        sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        alive--;
        super.onDestroy();
    }

    private void applyNewUserSetting(Confiq confiqLocal, Confiq confiqRemote) {
        if (confiqLocal.getUserId() == Consts.NEWUSERID && confiqRemote.getUserId() != null && confiqRemote.getUserId() != Consts.NEWUSERID)
            dbHelper.updateUserId(confiqRemote.getUserId());
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

        if (confiqRemote.getHasUserPermision() != null) {
            dbHelper.updateUserPermision(confiqRemote.getHasUserPermision());
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
    public void onGroupListReady(ArrayList<Group> groups, ArrayList<Integer> registered, ArrayList<Integer> ordered) {
        HashMap<Integer, Group> map = null;
        if (registered != null && registered.size() > 0) {
            map = new HashMap<>();
            for (Group group : groups) {
                map.put(group.getId(), group);
            }
            for (Integer r : registered) {
                Group group = map.get(r);
                if (group != null)
                    group.setStatus(Group.REGISTERED);
            }
        }
        if (ordered != null && ordered.size() > 0) {
            if (map == null) {
                map = new HashMap<>();
                for (Group group : groups) {
                    map.put(group.getId(), group);
                }
            }
            for (Integer or : ordered) {
                Group group = map.get(or);
                if (group != null)
                    group.setStatus(Group.ORDERED);
            }
        }
        this.groups = groups;
        wait4List = false;
    }

    @Override
    public void onUpdateAccountReady(int response) {
        wait4List = false;
        updateAccount = response;
    }

    @Override
    public void onSendMsgReady(ArrayList<Integer> list) {
        deliveredMsgIds = list;
        wait4List = false;
    }

    @Override
    public void onReceiptMsgReady(ArrayList<Message> messages) {
        recMessages = messages;
        wait4List = false;
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public void onGroupOrderDone() {
        wait4List = false;
    }

    @Override
    public void onGroupDelDone() {
        wait4List = false;
    }

}