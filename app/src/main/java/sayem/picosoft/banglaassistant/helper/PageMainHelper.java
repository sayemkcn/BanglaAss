package sayem.picosoft.banglaassistant.helper;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import sayem.picosoft.banglaassistant.R;

/**
 * Created by sayemkcn on 5/5/16.
 */
public class PageMainHelper {
    private Activity activity;
    private View rootView;
    private static final String COMMAND_CPU_USAGE = "/proc/stat";
    private PageProcessHelper pageProcessHelper;
    private TextView cpuUsageTextView;
    private TextView memoryUsageTextView;
    private float usage = 0;


    public PageMainHelper(FragmentActivity activity, View rootView) {
        this.activity = activity;
        this.rootView = rootView;
        pageProcessHelper = new PageProcessHelper(activity);
        cpuUsageTextView = (TextView) rootView.findViewById(R.id.cpuUsageChangableTextView);
        memoryUsageTextView = (TextView) rootView.findViewById(R.id.memoryUsageChangableTextView);


//        while (true){
//
//            try {
//                updateCpuUsage();
//                Thread.sleep(1000);
//                cpuUsageTextView.setText(this.usage+"%");
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

    }


    private void updateCpuUsage() {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this){
                    setUsage(pageProcessHelper.readUsage(COMMAND_CPU_USAGE));
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this){
                    setUsage(pageProcessHelper.readUsage(COMMAND_CPU_USAGE));
                }
            }
        });
        t1.start();
        t2.start();

    }


    public synchronized void setUsage(float usage) {
        this.usage = usage;
    }
}
