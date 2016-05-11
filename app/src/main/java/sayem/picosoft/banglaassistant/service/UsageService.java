package sayem.picosoft.banglaassistant.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;

import java.io.IOException;
import java.io.RandomAccessFile;

import sayem.picosoft.banglaassistant.helper.PageProcessHelper;

public class UsageService extends Service {
    private static final String COMMAND_CPU_USAGE = "/proc/stat";

    public UsageService() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (System.currentTimeMillis() < System.currentTimeMillis() + 1000) {
                    synchronized (this){
                        // Cpu Usage
                        float cpuUsage = readUsage(COMMAND_CPU_USAGE);

                        // send broadcast to activity
                        Intent intent = new Intent("UsageUpdate");
                        // You can also include some extra data.
                        intent.putExtra("cpuUsage", cpuUsage);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }).start();

        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
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
            } catch (Exception e) {
            }

            reader.seek(0);
            load = reader.readLine();
            reader.close();

            toks = load.split(" +");

            long idle2 = Long.parseLong(toks[4]);
            long cpu2 = Long.parseLong(toks[2]) + Long.parseLong(toks[3]) + Long.parseLong(toks[5])
                    + Long.parseLong(toks[6]) + Long.parseLong(toks[7]) + Long.parseLong(toks[8]);

            return (float) (cpu2 - cpu1) / ((cpu2 + idle2) - (cpu1 + idle1));

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return 0;
    }

}
