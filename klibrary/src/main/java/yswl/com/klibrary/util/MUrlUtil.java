package yswl.com.klibrary.util;


import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import yswl.com.klibrary.MApplication;

/**
 * url 管理
 *
 * @author nixn@yunhetong.net
 */
public class MUrlUtil {

//    public static String getCasAuthUrl() {
//        Resources res = MAppManager.getApplication().getResources();
//        String url =BuildConfig.YHTServer_cas + res.getString(R.string.url_cas_cas_v1_tickets);
//        return url;
//    }
//
//    public static String getHTTPSWWW() {
//        String url =  BuildConfig.YHTServer_swww;
//        return url;
//    }
//
//    public static String getHTTPWWW() {
//        String url = BuildConfig.YHTServer_wwww;
//        return url;
//    }

    public static String getUrl(Activity ac, int res) {
        return ac.getResources().getString(res);
    }

    //    public static String getUrl(int res) {
////        return getHTTPSWWW() + MApplication.getApplication().getResources().getString(res);
//    }
    public static String getUrl(int res) {
        return MApplication.getApplication().getResources().getString(res);
    }

    public static String getUrl(Activity ac, int res, Object... formatArgs) {
        return ac.getResources().getString(res, formatArgs);
    }

    public static String getUrl(Context ac, int res, Object... formatArgs) {
        return ac.getResources().getString(res, formatArgs);
    }

    public static String getUrl(Context ac, int res) {
        return ac.getResources().getString(res);
    }

    public static String getUrl(Fragment ac, int res) {
        return ac.getResources().getString(res);
    }

    public static String getUrl(Fragment ac, int res, Object... formatArgs) {
        return ac.getResources().getString(res, formatArgs);
    }

    public static String getUrl(FragmentActivity ac, int res) {
        return ac.getResources().getString(res);
    }

    public static String setParam(String url, String key, String value) {
        if (TextUtils.isEmpty(value))
            return url;
        if (url.contains("?")) {
            url = url + "&" + key + "=" + value;
        } else {
            url = url + "?" + key + "=" + value;
        }
        return url;
    }

    public static String setParam(String url, String key, int value) {
        if (value == -1)
            return url;
        if (url.contains("?")) {
            url = url + "&" + key + "=" + value;
        } else {
            url = url + "?" + key + "=" + value;
        }
        return url;
    }

    public static String setParam(String url, String key, long value) {
        if (value == -1)
            return url;
        if (url.contains("?")) {
            url = url + "&" + key + "=" + value;
        } else {
            url = url + "?" + key + "=" + value;
        }
        return url;
    }


}
