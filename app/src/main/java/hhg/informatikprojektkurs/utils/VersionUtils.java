package hhg.informatikprojektkurs.utils;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class VersionUtils {

    public static String getVersionCode(Activity activity) {
        String version = "";

        try {
            PackageInfo pInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return "<b>Aktuelle Version: </b>" + version;
    }
}
