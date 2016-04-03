package sayem.picosoft.banglaassistant.helper;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Debug;
import android.util.Log;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import sayem.picosoft.banglaassistant.R;
import sayem.picosoft.banglaassistant.model.SingleProcessItem;

/**
 * Created by sayemkcn on 4/2/16.
 */
public class PageProcessHelper {
    private Activity context;

    public PageProcessHelper(Activity activity) {
        this.context = activity;
    }

    public List<SingleProcessItem> getProcessList() {
        List<SingleProcessItem> singleProcessItemList = new ArrayList<>();

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        PackageManager packageManager = context.getPackageManager();
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfoList = activityManager.getRunningAppProcesses();

        for (int i = 0; i < runningAppProcessInfoList.size(); i++) {
            ActivityManager.RunningAppProcessInfo runningAppProcessInfo = runningAppProcessInfoList.get(i);
            SingleProcessItem singleProcessItem = new SingleProcessItem();
            ApplicationInfo applicationInfo = null;
            try {
                applicationInfo = packageManager.getApplicationInfo(runningAppProcessInfo.processName, PackageManager.GET_META_DATA);
                String applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
                singleProcessItem.setApplicationTitle(applicationName);
            } catch (PackageManager.NameNotFoundException e) {
                singleProcessItem.setApplicationTitle(runningAppProcessInfo.processName);
            }
            try {
                Drawable appIcon = packageManager.getApplicationIcon(applicationInfo);
                if (appIcon != null)
                    singleProcessItem.setAppIcon(packageManager.getApplicationIcon(applicationInfo));
                else
                    singleProcessItem.setAppIcon(context.getResources().getDrawable(R.drawable.default_icon));
            } catch (Exception e) {
                singleProcessItem.setAppIcon(context.getResources().getDrawable(R.drawable.default_icon));
            }

//            Log.e("CPU_USAGE", String.valueOf(readUsage()));

            singleProcessItem.setCpuUsage(100);
            singleProcessItem.setMemoryUsage(100);
            singleProcessItem.setIsChecked(true);
            singleProcessItemList.add(singleProcessItem);
        }

        return singleProcessItemList;
    }

    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return (pkgInfo.applicationInfo.flags &
                ApplicationInfo.FLAG_SYSTEM) != 0;
    }
    private float readUsage() {
        try {
            RandomAccessFile reader = new RandomAccessFile("/proc/stat", "r");
            String load = reader.readLine();

            String[] toks = load.split(" ");

            long idle1 = Long.parseLong(toks[5]);
            long cpu1 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4])
                    + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

            try {
                Thread.sleep(360);
            } catch (Exception e) {}

            reader.seek(0);
            load = reader.readLine();
            reader.close();

            toks = load.split(" ");

            long idle2 = Long.parseLong(toks[5]);
            long cpu2 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[4])
                    + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

            return (float)(cpu2 - cpu1) / ((cpu2 + idle2) - (cpu1 + idle1));

        } catch (IOException ex) {
            Log.e("CPU_USAGE_EXCEPTION",ex.toString());
        }

        return 0;
    }
}
