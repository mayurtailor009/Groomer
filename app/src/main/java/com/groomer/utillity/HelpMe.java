package com.groomer.utillity;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Locale;

public class HelpMe {

    public HelpMe() {
        Calendar rightNow = Calendar.getInstance();
    }


    // Check for valid mobile number of 10 digits
    public static void setLocale(String languageCode, Context mContext) {
        String code = null;
        if (languageCode.equalsIgnoreCase(Constants.LANG_ENGLISH_CODE)) {
            code = "en";
        } else {
            code = "ar";
        }
        Locale locale = new Locale(code);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        mContext.getResources().updateConfiguration(config,
                mContext.getResources().getDisplayMetrics());
    }


    public static String getDistanceUnitSign(String distanceUnitSign, Context mContext) {

        if (distanceUnitSign.equalsIgnoreCase(Constants.DISTANCE_UNIT_KM_ENG)) {
            return isArabic(mContext) ? Constants.DISTANCE_UNIT_KM_ARA : Constants.DISTANCE_UNIT_KM_ENG;
        } else {
            return isArabic(mContext) ? Constants.DISTANCE_UNIT_MILES_ARA : Constants.DISTANCE_UNIT_MILES_ENG;
        }
    }


    public static String getRelatedPreferenceText(Context mContext, String titleEng, String titleAra) {

        return isArabic(mContext) ? ((titleAra == null || titleAra.equalsIgnoreCase("")) ? titleEng : titleAra) :
                titleEng;

    }

    public static String getCurrencySign(Context mContext) {

        return isArabic(mContext) ? GroomerPreference.getCurrencyAra(mContext) :
                GroomerPreference.getCurrencyEng(mContext);

    }

    public static boolean isArabic(Context mContext) {
        if (GroomerPreference.getAPP_LANG(mContext).contains(Constants.LANG_ARABIC_CODE)) {
            return true;
        } else
            return false;
    }


    public static boolean isMiles(Context mContext) {
        if (GroomerPreference.getDistanceUnit(mContext).equalsIgnoreCase(Constants.DISTANCE_UNIT_MILES_ENG)) {
            return true;
        } else
            return false;
    }
    public static boolean isCurrencyCheck(Context mContext, String currencyNameEng) {
        if (GroomerPreference.getCurrencyEng(mContext).equalsIgnoreCase(currencyNameEng)) {
            return true;
        } else
            return false;
    }
    public static double convertKMToMiles(String kilometers) {

        DecimalFormat decimalFormat = new DecimalFormat("#");
        int km = Integer.parseInt(kilometers);
        double miles = 0.621 * km;

        decimalFormat.format(miles);
        return    Math.abs(miles);
    }

    public static void pullDb(Context context) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "data/data/" + context.getPackageName() + "/databases/memories.db";
                String backupDBPath = "memories.sqlite";
                File currentDB = new File(currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
