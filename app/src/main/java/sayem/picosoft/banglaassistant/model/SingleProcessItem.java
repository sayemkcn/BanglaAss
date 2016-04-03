package sayem.picosoft.banglaassistant.model;

import android.graphics.drawable.Drawable;

/**
 * Created by sayemkcn on 4/2/16.
 */
public class SingleProcessItem {
    private Drawable appIcon;
    private String applicationTitle;
    private float cpuUsage;
    private float memoryUsage;
    private boolean isChecked;

    public String getApplicationTitle() {
        return applicationTitle;
    }

    public void setApplicationTitle(String applicationTitle) {
        this.applicationTitle = applicationTitle;
    }

    public float getCpuUsage() {
        return cpuUsage;
    }

    public void setCpuUsage(float cpuUsage) {
        this.cpuUsage = cpuUsage;
    }

    public float getMemoryUsage() {
        return memoryUsage;
    }

    public void setMemoryUsage(float memoryUsage) {
        this.memoryUsage = memoryUsage;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }


    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    @Override
    public String toString() {
        return "SingleProcessItem{" +
                "appIcon=" + appIcon +
                ", applicationTitle='" + applicationTitle + '\'' +
                ", cpuUsage=" + cpuUsage +
                ", memoryUsage=" + memoryUsage +
                ", isChecked=" + isChecked +
                '}';
    }
}
