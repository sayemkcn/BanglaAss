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
import android.widget.Toast;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import sayem.picosoft.banglaassistant.R;
import sayem.picosoft.banglaassistant.model.SingleProcessItem;

/**
 * Created by sayemkcn on 4/2/16.
 */
public class PageProcessHelper {
    private Activity context;
    private static String COMMAND_CPU_USAGE = "/proc/stat";
    private static String COMMAND_MEMORY_INFO ="/proc/meminfo";

    private float cpuUsage;
    public PageProcessHelper(Activity activity) {
        this.context = activity;
    }

    public List<SingleProcessItem> getProcessList() {


        List<SingleProcessItem> singleProcessItemList = new ArrayList<>();

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        PackageManager packageManager = context.getPackageManager();
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfoList = activityManager.getRunningAppProcesses();

        SingleProcessItem singleProcessItem;
        ApplicationInfo applicationInfo = null;
        PackageInfo packageInfo = null;

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Log.e("DEBUG",executeTop());
//            }
//        }).start();

        for (int i = 0; i < runningAppProcessInfoList.size(); i++) {
            final ActivityManager.RunningAppProcessInfo runningAppProcessInfo = runningAppProcessInfoList.get(i);
            singleProcessItem = new SingleProcessItem();

            // read cpu usage
            new Thread(new Runnable() {
                @Override
                public void run() {
                    synchronized (this){
                        cpuUsage = readUsage(COMMAND_CPU_USAGE);
                    }
                }
            }).start();

            // app label
            try {
                applicationInfo = packageManager.getApplicationInfo(runningAppProcessInfo.processName, PackageManager.GET_META_DATA);
                String applicationName = (String) packageManager.getApplicationLabel(applicationInfo);
                singleProcessItem.setApplicationTitle(applicationName);
            } catch (PackageManager.NameNotFoundException e) {
                singleProcessItem.setApplicationTitle(runningAppProcessInfo.processName);
            }
            // app icon
            try {
                Drawable appIcon = packageManager.getApplicationIcon(applicationInfo);
                if (appIcon != null)
                    singleProcessItem.setAppIcon(packageManager.getApplicationIcon(applicationInfo));
                else
                    singleProcessItem.setAppIcon(context.getResources().getDrawable(R.drawable.default_icon));
            } catch (Exception e) {
                singleProcessItem.setAppIcon(context.getResources().getDrawable(R.drawable.default_icon));
            }

            // get package info to determine if system app
            try {
                if (applicationInfo != null)
                    packageInfo = packageManager.getPackageInfo(applicationInfo.packageName, 0);
            } catch (PackageManager.NameNotFoundException e) {
                Log.e("PackageName_not_Found", e.toString());
            }


            singleProcessItem.setCpuUsage(cpuUsage);

            ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
            activityManager.getMemoryInfo(memoryInfo);
            singleProcessItem.setMemoryUsage(memoryInfo.availMem/1048576L);

            if (packageInfo != null) {
                if (!this.isSystemPackage(packageInfo)) {
                    // check to avoid self destruction :P
                    if (!applicationInfo.packageName.equals(context.getPackageName())){
                        singleProcessItem.setIsChecked(true);
                    }
                } else {
                    singleProcessItem.setIsChecked(false);
                }
            } else {
                Log.d("PACKAGE INFO", "Package info null");
            }
//            singleProcessItem.setIsChecked(true);
            singleProcessItemList.add(singleProcessItem);


        }

        return singleProcessItemList;
    }

    private boolean isSystemPackage(PackageInfo pkgInfo) {
        return (pkgInfo.applicationInfo.flags &
                ApplicationInfo.FLAG_SYSTEM) != 0;
    }



    private String executeTop() {
        java.lang.Process p = null;
        BufferedReader in = null;
        String returnString = null;
        try {
            p = Runtime.getRuntime().exec("top");
//            in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringWriter stringWriter = new StringWriter();
            IOUtils.copy(p.getInputStream(), stringWriter, "UTF-8");
            return stringWriter.toString();
        } catch (IOException e) {
            Log.e("executeTop", "error in getting first line of top");
            e.printStackTrace();
        } finally {
            try {
                in.close();
                p.destroy();
            } catch (IOException e) {
                Log.e("executeTop",
                        "error in closing and destroying top process");
                e.printStackTrace();
            }
        }
        return returnString;
    }

    private String executeTopForProcess(final int pid) {
        final String[] usageString = new String[1];
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    java.lang.Process p = null;
                    BufferedReader in = null;
                    String returnString = null;
                    try {
                        p = Runtime.getRuntime().exec("top -pid " + pid);
                        in = new BufferedReader(new InputStreamReader(p.getInputStream()));
                        while (returnString == null || returnString.contentEquals("")) {
                            returnString = in.readLine();
                        }
                    } catch (IOException e) {
                        Log.e("executeTop", "error in getting first line of top");
                        e.printStackTrace();
                    } finally {
                        try {
                            in.close();
                            p.destroy();
                        } catch (IOException e) {
                            Log.e("executeTop",
                                    "error in closing and destroying top process");
                            e.printStackTrace();
                        }
                    }
                    usageString[0] = returnString;
                    notify();
                }
            }
        }).start();

        return usageString[0];

    }



    public int killBackgroundProcesses(){
        int count = 0;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(context.ACTIVITY_SERVICE);
        PackageManager packageManager = context.getPackageManager();
        List<ActivityManager.RunningAppProcessInfo> runningProcessList = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo runningProcess: runningProcessList){
            try {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(runningProcess.processName,PackageManager.GET_META_DATA);
                if (!applicationInfo.packageName.equals(context.getPackageName())){
                    activityManager.killBackgroundProcesses(applicationInfo.packageName);
                    count++;
                }
            } catch (Exception e) {
                Log.d("KILL_PROCESS_SYSTEM_APP",e.toString());
            }
        }

        return count;
    }

    public float readUsage(String command) {
        try {
            RandomAccessFile reader = new RandomAccessFile(command, "r");
            String load = reader.readLine();

            String[] toks = load.split(" +");  // Split on one or more spaces

            long idle1 = Long.parseLong(toks[4]);
            long cpu1 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[5])
                    + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

            try {
                Thread.sleep(360);
            } catch (Exception e) {}

            reader.seek(0);
            load = reader.readLine();
            reader.close();

            toks = load.split(" +");

            long idle2 = Long.parseLong(toks[4]);
            long cpu2 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[5])
                    + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

            return (float)(cpu2 - cpu1) / ((cpu2 + idle2) - (cpu1 + idle1));

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return 0;
    }
}
