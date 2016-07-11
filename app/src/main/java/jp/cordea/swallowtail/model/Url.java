package jp.cordea.swallowtail.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Patterns;

/**
 * Created by Yoshihiro Tanaka on 2016/07/10.
 */

public class Url {

    private static final String URL_KEY = "Url";

    public static String getUrl(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(URL_KEY, "");
    }

    public static boolean setUrl(String url, Context context) {
        if (url == null || url.length() == 0 || !Patterns.WEB_URL.matcher(url).matches()) {
            return false;
        }
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.edit().putString(URL_KEY, url).commit();
    }

}
