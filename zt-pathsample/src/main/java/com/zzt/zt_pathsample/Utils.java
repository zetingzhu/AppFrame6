package com.zzt.zt_pathsample;

import android.app.Application;
import android.util.Log;

public final class Utils {

    private static Application sApp;

    public static void init(final Application app) {
        if (app == null) {
            Log.e("Utils", "app is null.");
            return;
        }
        if (sApp == null) {
            sApp = app;
            return;
        }
    }


    public static Application getApp() {
        if (sApp != null) return sApp;
        return sApp;
    }

}
