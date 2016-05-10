package sayem.picosoft.banglaassistant.helper;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

import sayem.picosoft.banglaassistant.R;
import sayem.picosoft.banglaassistant.commons.DeviceStorage;

/**
 * Created by sayemkcn on 5/5/16.
 */
public class PageMainHelper extends BroadcastReceiver {
    private Activity activity;
    private View cpuMemoryLayout;
    private static final String COMMAND_CPU_USAGE = "/proc/stat";
    private PageProcessHelper mPageProcessHelper;
    private TextView cpuUsageTextView;
    private TextView memoryUsageTextView;
    private TextView batteryLevelTextView;
    private TextView batteryTempTextView;
    private TextView batteryVoltageTextView;
    private TextView intAvailableMemPercentageTextView;
    private ProgressBar intMemProgressBar;
    private TextView intMemAvailTotalTextView;
    private TextView extAvailableMemPercentageTextView;
    private ProgressBar extMemProgressBar;
    private TextView extMemAvailTotalTextView;
    private Long availableMem;
    public static BroadcastReceiver BROADCASTRECEIVER;

    private ClickListener listener = new ClickListener();


    public PageMainHelper(FragmentActivity activity, View rootView) {
        this.activity = activity;
        this.mPageProcessHelper = new PageProcessHelper(activity);
        this.cpuUsageTextView = (TextView) rootView.findViewById(R.id.cpuUsageChangableTextView);
        this.memoryUsageTextView = (TextView) rootView.findViewById(R.id.memoryUsageChangableTextView);
        this.batteryLevelTextView = (TextView) rootView.findViewById(R.id.batteryLevelTextView);
        this.batteryTempTextView = (TextView) rootView.findViewById(R.id.batteryTempTextView);
        this.batteryVoltageTextView = (TextView) rootView.findViewById(R.id.batteryVoltageTextView);
        this.intAvailableMemPercentageTextView = (TextView) rootView.findViewById(R.id.intMemAvailPercentageTextView);
        this.intMemProgressBar = (ProgressBar) rootView.findViewById(R.id.intMemProgressBar);
        this.intMemAvailTotalTextView = (TextView) rootView.findViewById(R.id.intMemAvailTotalTextView);
        this.extAvailableMemPercentageTextView = (TextView) rootView.findViewById(R.id.extMemAvailPercentageTextView);
        this.extMemProgressBar = (ProgressBar) rootView.findViewById(R.id.extMemProgressBar);
        this.extMemAvailTotalTextView = (TextView) rootView.findViewById(R.id.extMemAvailTotalTextView);
        this.cpuMemoryLayout = rootView.findViewById(R.id.cpuMemoryLayout);

        if (BROADCASTRECEIVER == null) {
            BROADCASTRECEIVER = this;
        }


        /******** REGISTER LISTENER FORM MAINACTIVITY *******/
        LocalBroadcastManager.getInstance(this.activity).registerReceiver(
                BROADCASTRECEIVER, new IntentFilter("UsageUpdate"));

        batteryLevelTextView.setText(String.valueOf((int) getBatteryLevel(this.activity)));
        batteryTempTextView.setText(getBatteryTemperature(this.activity));
        batteryVoltageTextView.setText(getBatteryVoltage(this.activity));

        // for internal memory
        Long actualAvailInternalMemSize = DeviceStorage.getActualAvailableInternalMemorySize();
        Long actualTotalInternalMemSize = DeviceStorage.getActualTotalInternalMemorySize();
        Long usedInternalMemoryPercentage = ((actualTotalInternalMemSize - actualAvailInternalMemSize) * 100) / actualTotalInternalMemSize;

        intMemAvailTotalTextView.setText(DeviceStorage.getAvailableInternalMemorySize() + "/" + DeviceStorage.getTotalInternalMemorySize());
        intAvailableMemPercentageTextView.setText(usedInternalMemoryPercentage + "%");
        intMemProgressBar.setProgress((int) (long) usedInternalMemoryPercentage);
        // for external memory
        Long actualAvailExternalMemSize = DeviceStorage.getActualAvailableExternalMemorySize();
        Long actualTotalExternalMemSize = DeviceStorage.getActualTotalExternalMemorySize();
        Long usedExternalMemoryPercentage = 0l;
        if (actualAvailExternalMemSize != null && actualTotalExternalMemSize != null)
            usedExternalMemoryPercentage = ((actualTotalExternalMemSize - actualAvailExternalMemSize) * 100) / actualTotalExternalMemSize;

        extMemAvailTotalTextView.setText(DeviceStorage.getAvailableExternalMemorySize() + "/" + DeviceStorage.getTotalExternalMemorySize());
        extAvailableMemPercentageTextView.setText(usedExternalMemoryPercentage + "%");
        extMemProgressBar.setProgress((int) (long) usedExternalMemoryPercentage);


        // set actions for all of them
        intMemProgressBar.setOnClickListener(listener);
        extMemProgressBar.setOnClickListener(listener);
        cpuMemoryLayout.setOnClickListener(listener);


        Log.d("INTERNAL_MEMORY", "Total: " + DeviceStorage.getTotalInternalMemorySize() + " Available: " + DeviceStorage.getAvailableInternalMemorySize());
        Log.d("EXTERNALL_MEMORY", "Total: " + DeviceStorage.getTotalExternalMemorySize() + " Available: " + DeviceStorage.getAvailableExternalMemorySize());
    }

    private void openSystemFileManager() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        Uri uri = Uri.parse(Environment.getDataDirectory().getPath()); // a directory
        intent.setDataAndType(uri, "*/*");
        activity.startActivity(Intent.createChooser(intent, "Open folder"));
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        float cpuUsage = intent.getExtras().getFloat("cpuUsage");
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        cpuUsageTextView.setText(df.format(cpuUsage) + "%");
        ActivityManager activityManager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memInfo);
//        long memUsagePercentage = (memInfo.totalMem-memInfo.availMem)/memInfo.totalMem;
        Long availableMemory = getAvailableMemory();
        if (availableMemory != null) {
            memoryUsageTextView.setText(availableMemory + " MB");
        }

    }

    private Long getAvailableMemory() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    ActivityManager activityManager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
                    ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
                    activityManager.getMemoryInfo(memInfo);
                    availableMem = memInfo.availMem / 1048576L;
                }
            }
        }).start();
        return availableMem;
    }

    private float getBatteryLevel(Activity activity) {
        Intent batteryIntent = activity.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        // Error checking that probably isn't needed but I added just in case.
        if (level == -1 || scale == -1) {
            return 50.0f;
        }

        return ((float) level / (float) scale) * 100.0f;
    }

    private String getBatteryTemperature(Activity activity) {
        Intent batteryIntent = activity.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int temperature = batteryIntent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0) / 10;

        return String.valueOf(temperature) + "°C";
    }

    private String getBatteryVoltage(Activity activity) {
        Intent batteryIntent = activity.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        float voltage = (float) batteryIntent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0) / 1000;
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        return String.valueOf(df.format(voltage)) + "V";
    }


    private class ClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            if (id == R.id.intMemProgressBar) {
                openSystemFileManager();
            } else if (id == R.id.extMemProgressBar) {
                openSystemFileManager();
            } else if (id == R.id.cpuMemoryLayout) {
                final ProgressDialog progressDialog = new ProgressDialog(activity);
                progressDialog.setTitle("Please wait..");
                progressDialog.setMessage("Killing background processes and Boosting your phone.");
                progressDialog.setCancelable(false);
                progressDialog.show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final int killedAppCount = mPageProcessHelper.killBackgroundProcesses();
                        if (progressDialog.isShowing()) {
                            progressDialog.cancel();
                        }
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new AlertDialog.Builder(activity)
                                        .setTitle("Boosted!")
                                        .setMessage("Bamnn!!\nI've successfully Killed "+killedAppCount+" apps to boost your phone like a bomb!")
                                        .setCancelable(false)
                                        .setIcon(activity.getResources().getDrawable(R.mipmap.ic_launcher))
                                        .setPositiveButton("Thanks",null)
                                        .show();
                            }
                        });
//                        Toast.makeText(activity.getApplicationContext(), "Killed " + killedAppCount + " apps", Toast.LENGTH_SHORT).show();
                    }
                }).start();
            }
        }
    }
}