package com.tut.abiz.base.model;

import android.content.Context;

import com.tut.abiz.base.R;

/**
 * Created by abiz on 5/12/2019.
 */

public class UserAcountForm {

    private UserAccount userAccount;
    private Context context;
    private String message;
    char[] invalidChars;

    public UserAcountForm(Context context, UserAccount userAccount) {
        this.userAccount = userAccount;
        this.context = context;
        invalidChars = new char[]
                {'!', '/', '\\', '\'', '%', '^', '"', '*', '(', ')', '[', ']'
                        , '~', '`', ';', ':', '.', '?', '|', '}', '{', '&', ','};
    }

    public boolean isUserAccountValid() {
        if (userAccount.getUserName().trim().length() < 4 || userAccount.getUserName().trim().length() > 8) {
            message = context.getResources().getString(R.string.usernameWarn);
            return false;
        }
        if (userAccount.getPassword().trim().length() < 4 || userAccount.getPassword().trim().length() > 8) {
            message = context.getResources().getString(R.string.passWarn);
            return false;
        }
        if (userAccount.getPhone().length() > 0
                && (!userAccount.getPhone().trim().startsWith("0") || userAccount.getPhone().trim().length() != 11)) {
            message = context.getResources().getString(R.string.phoneWarn);
            return false;
        }
        message = context.getResources().getString(R.string.emailWarn);
        if (userAccount.getEmail().length() > 0) {
            if (!userAccount.getEmail().contains("@"))
                return false;
            String[] split = userAccount.getEmail().split("@");
            if (split[0].length() < 2)
                return false;
            if (split[1].length() < 2)
                return false;
        }
        message = context.getResources().getString(R.string.invalidChars);
        for (char c : invalidChars) {
            if (userAccount.getUserName().toString().contains(c + ""))
                return false;
            if (userAccount.getPassword().toString().contains(c + ""))
                return false;
        }
        message = "";
        return true;
    }

    public String getMessage() {
        return message;
    }

}
