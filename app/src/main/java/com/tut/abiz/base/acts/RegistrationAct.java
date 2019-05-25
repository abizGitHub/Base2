package com.tut.abiz.base.acts;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tut.abiz.base.Consts;
import com.tut.abiz.base.R;
import com.tut.abiz.base.adapter.JsonUtil;
import com.tut.abiz.base.async.PostSync;
import com.tut.abiz.base.model.GeneralModel;
import com.tut.abiz.base.model.UserAccount;
import com.tut.abiz.base.model.UserAcountForm;
import com.tut.abiz.base.service.OffLineTestService;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by abiz on 5/12/2019.
 */

public class RegistrationAct extends BaseActivity {

    EditText userName;
    EditText passWord;
    EditText phone;
    EditText eMail;
    Button btn;
    AlertDialog.Builder alertDialogBuilder;
    UserAccount userAccount;
    boolean userAlreadyRegistered;
    boolean editMode;
    HttpClient httpClient;
    ProgressDialog progressDialog;
    TextView actInact, regUnreg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_regitration);
        httpClient = new DefaultHttpClient();
        alertDialogBuilder = new AlertDialog.Builder(this);
        userName = (EditText) findViewById(R.id.input_name);
        passWord = (EditText) findViewById(R.id.input_pass);
        phone = (EditText) findViewById(R.id.input_phone);
        eMail = (EditText) findViewById(R.id.input_email);
        btn = (Button) findViewById(R.id.btn_submit);
        regUnreg = (TextView) findViewById(R.id.user_registered);
        actInact = (TextView) findViewById(R.id.user_active);
        progressDialog = new ProgressDialog(RegistrationAct.this);
        progressDialog.setMessage("Please Wait....");
        progressDialog.setCanceledOnTouchOutside(false);
        userAccount = getDbHelper().getUserAccount();
        if (userAccount.getId() < 1) {
            Long userId = getDbHelper().getBriefConfiq().getUserId();
            userAccount.setId(userId);
        }
        boolean permitted = getDbHelper().hasUserPermission();
        userAlreadyRegistered = isUserAccountRegistered();
        setUserTextActivated(permitted);
        setUserTextRegistered(userAlreadyRegistered);
        fillForm();
        if (userAlreadyRegistered) {
            setFormRegisteredMode();
        } else {
            setFormEditMode();
        }
        setNewListener();
    }

    private void setUserTextActivated(boolean b) {
        if (b) {
            actInact.setTextColor(Color.GREEN);
            actInact.setText(getResources().getString(R.string.active));
        } else {
            actInact.setTextColor(Color.RED);
            actInact.setText(getResources().getString(R.string.inActive));
        }
    }

    private void setUserTextRegistered(boolean b) {
        if (b) {
            regUnreg.setTextColor(Color.GREEN);
            regUnreg.setText(getResources().getString(R.string.registered));
        } else {
            regUnreg.setTextColor(Color.RED);
            regUnreg.setText(getResources().getString(R.string.unRegistered));
        }
    }

    private void dismisProgressDial() {
        progressDialog.dismiss();
    }

    private void showProgressDial() {
        progressDialog.show();
    }

    public void setNewListener() {
        btn.setOnClickListener(new RegisterListener());
    }

    private void fillAccount() {
        userAccount.setUserName(userName.getText().toString().trim());
        userAccount.setPassword(passWord.getText().toString().trim());
        userAccount.setEmail(eMail.getText().toString().trim());
        userAccount.setPhone(phone.getText().toString().trim());
    }

    private void fillForm() {
        userName.setText(userAccount.getUserName());
        passWord.setText(userAccount.getPassword());
        eMail.setText(userAccount.getEmail());
        phone.setText(userAccount.getPhone());
    }

    private void setFormEditMode() {
        editMode = true;
        userName.setEnabled(!userAlreadyRegistered);
        passWord.setEnabled(true);
        phone.setEnabled(true);
        eMail.setEnabled(true);
        btn.setText(getResources().getString(R.string.save));
    }

    private void setFormRegisteredMode() {
        editMode = false;
        userName.setEnabled(false);
        passWord.setEnabled(false);
        phone.setEnabled(false);
        eMail.setEnabled(false);
        btn.setText(getResources().getString(R.string.edit));
    }

    private String[] doRegister() {
        String message = getResources().getString(R.string.canNotconnectMessage);
        if (!isConnectedToNet())
            return new String[]{"-1", message};

        int[] status = doRegisterInNet(userAccount);

        if (status[0] == Consts.USERREGISTERED) {
            userAccount.setId(status[1]);
            getDbHelper().insertUserAccount(userAccount, status[1]);
            setUserAccountRegitered(true);
            setGroupMenuOpened(true);
            userAlreadyRegistered = true;
            setUserAccountEdited(true);
            message = getResources().getString(R.string.accountSuccessReg);
        }
        if (status[0] == Consts.USERNAMERESERVED) {
            message = getResources().getString(R.string.usernameReserved);
        }
        if (status[0] == Consts.CANTREGISTERE) {
            message = getResources().getString(R.string.canNotRegister);
        }
        if (status[0] == Consts.USERNAMEREVIVED) {
            userAccount.setId(status[1]);
            getDbHelper().insertUserAccount(userAccount, status[1]);
            getDbHelper().updateUserId((long) status[1]);
            setUserAccountRegitered(true);
            setGroupMenuOpened(true);
            userAlreadyRegistered = true;
            setUserAccountEdited(true);
            message = getResources().getString(R.string.accountSuccessRevived);
        }
        return new String[]{status[0] + "", message};
    }

    private int[] doRegisterInNet(UserAccount userAccount) {
        JSONObject jsonObject;
        if (BaseActivity.offline)
            jsonObject = OffLineTestService.postData(Consts.SERVERADDRESS + Consts.ADDRESSREGUSER, JsonUtil.parseUserAccount(userAccount));
        else {
            PostSync postSync = new PostSync(RegistrationAct.this, Consts.SERVERADDRESS + Consts.ADDRESSREGUSER, JsonUtil.parseUserAccount(userAccount));
            jsonObject = postSync.execute("");
        }
        try {
            if (jsonObject == null)
                return new int[]{Consts.CANTREGISTERE, -1};
            return new int[]{jsonObject.getInt("response"), jsonObject.getInt("id")};
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new int[]{Consts.CANTREGISTERE, -1};
    }

    private boolean isConnectedToNet() {
        ConnectivityManager conMan = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conMan.getActiveNetworkInfo();
        return (info != null && info.isAvailable() && info.isConnected());
    }

    private void showMessageDialog(String message) {
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setNegativeButton(getResources().getString(R.string.understood), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                alertDialog.show();
            }
        }, 300);
    }


    @Override
    protected void doStaredTasks() {
    }

    @Override
    protected ArrayAdapter getListAdapter() {
        return null;
    }

    @Override
    protected ArrayList<String> getGeneralTitles() {
        return null;
    }

    @Override
    protected ArrayList<GeneralModel> getGeneralList() {
        return null;
    }

    @Override
    public void onStarChanged(int position, boolean checked) {

    }

    class RegisterListener implements View.OnClickListener {
        String s = "";

        @Override
        public void onClick(View view) {
            fillAccount();
            if (!editMode) {
                setFormEditMode();
                return;
            }
            UserAcountForm form = new UserAcountForm(RegistrationAct.this, userAccount);
            if (!form.isUserAccountValid()) {
                showMessageDialog(form.getMessage());
                return;
            }
            if (userAlreadyRegistered) {
                getDbHelper().updateUserAccount(userAccount);
                setUserAccountEdited(true);
                setFormRegisteredMode();
            } else {
                showProgressDial();
                new AsyncTask<String, Void, String>() {
                    int registered;

                    @Override
                    protected String doInBackground(String... strings) {
                        String[] res = doRegister();
                        registered = Integer.parseInt(res[0]);
                        return res[1];
                    }

                    @Override
                    protected void onPostExecute(String s) {
                        dismisProgressDial();
                        showMessageDialog(s);
                        if (registered == Consts.USERREGISTERED || registered == Consts.USERNAMEREVIVED) {
                            RegistrationAct.this.setGroupMenuOpened(true);
                            setUserTextActivated(true);
                            setUserTextRegistered(true);
                        }
                    }
                }.execute("");
            }
            setFormRegisteredMode();
        }

    }

}



