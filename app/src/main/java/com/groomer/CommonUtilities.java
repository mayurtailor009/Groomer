package com.groomer;

import android.content.Context;
import android.content.Intent;

public final class CommonUtilities {

    // give your server registration url here
    // public static final String SERVER_URL =
    // "http://192.168.0.105/gcm/gcm.php?shareRegId=1";

    // Google project id
    public static final String SENDER_ID = "groomer-1287";
    //Api KEY
    public static final String API_KEY = "AIzaSyAtqx1--xDu9SdIbhKHo0ck-H5drg3a094";
    /**
     * Tag used on log messages.
     */
    public static final String TAG = "GROOMER GCM";

    public static final String DISPLAY_MESSAGE_ACTION = "com.groomer.DISPLAY_MESSAGE";

    public static final String EXTRA_MESSAGE = "message";

    /**
     * Notifies UI to display a message.
     * <p/>
     * This method is defined in the common helper because it's used both by the
     * UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    public static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
}
