package sayem.picosoft.banglaassistant.helper;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;

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
        List<SingleProcessItem> singleProcessItemList  = new ArrayList<>();

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo:activityManager.getRunningAppProcesses()){
            SingleProcessItem singleProcessItem = new SingleProcessItem();
            singleProcessItem.setApplicationTitle(runningAppProcessInfo.processName);
            singleProcessItem.setIconId(R.mipmap.ic_launcher);
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
}
