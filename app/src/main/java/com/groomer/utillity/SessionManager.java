package com.groomer.utillity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.groomer.login.LoginActivity;

public class SessionManager {

    private static final String TAG = "<SessionManager>";
    private Context _context;

    // Constructor
    public SessionManager(Context context) {
        this._context = context;

        Log.d(TAG, "session manager onstructor called");
    }


    /**
     * Clear session details
     */
    public static void logoutUser(Context mContext) {
        // Clearing all data from Shared Preferences
        GroomerPreference.removeObjectIntoPref(mContext, Constants.USER_INFO);
        //TraphoriaPreference.setLoggedIn(mContext, false);

        // After logout redirect user to Loing Activity
        Intent i = new Intent(mContext, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        mContext.startActivity(i);
        Utils.ShowLog(TAG, "logging out user");
    }

    /**
     * Quick check for login
     * *
     */

    // Get Login State
    //public boolean isLoggedIn(Context mContext) {
    //   return DealPreferences.isLoggedIn(mContext);
    //}
}
