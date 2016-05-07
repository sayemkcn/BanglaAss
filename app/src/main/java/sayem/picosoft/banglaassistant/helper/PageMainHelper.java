package sayem.picosoft.banglaassistant.helper;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;

import sayem.picosoft.banglaassistant.R;

/**
 * Created by sayemkcn on 5/5/16.
 */
public class PageMainHelper extends BroadcastReceiver{
    private Activity activity;
    private View rootView;
    private static final String COMMAND_CPU_USAGE = "/proc/stat";
    private PageProcessHelper pageProcessHelper;
    private TextView cpuUsageTextView;
    private TextView memoryUsageTextView;
    private Long availableMem;


    public PageMainHelper(FragmentActivity activity, View rootView) {
        this.activity = activity;
        this.rootView = rootView;
        pageProcessHelper = new PageProcessHelper(activity);
        cpuUsageTextView = (TextView) rootView.findViewById(R.id.cpuUsageChangableTextView);
        memoryUsageTextView = (TextView) rootView.findViewById(R.id.memoryUsageChangableTextView);

        LocalBroadcastManager.getInstance(this.activity).registerReceiver(
                this, new IntentFilter("UsageUpdate"));
    }




    @Override
    public void onReceive(Context context, Intent intent) {
        float cpuUsage = intent.getExtras().getFloat("cpuUsage");
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        cpuUsageTextView.setText(df.format(cpuUsage)+"%");
        ActivityManager activityManager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memInfo);
//        long memUsagePercentage = (memInfo.totalMem-memInfo.availMem)/memInfo.totalMem;
        Long availableMemory = getAvailableMemory();
        if (availableMemory!=null){
            memoryUsageTextView.setText(availableMemory+" MB");
        }

    }

    private Long getAvailableMemory(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this){
                    ActivityManager activityManager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
                    ActivityManager.MemoryInfo memInfo = new ActivityManager.MemoryInfo();
                    activityManager.getMemoryInfo(memInfo);
                    availableMem = memInfo.availMem/1048576L;
                }
            }
        }).start();
        return availableMem;
    }
}
