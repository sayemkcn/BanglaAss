package sayem.picosoft.banglaassistant.model;

/**
 * Created by sayemkcn on 4/2/16.
 */
public class SingleProcessItem {
    private int iconId;
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


    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    @Override
    public String toString() {
        return "SingleProcessItem{" +
                "iconId=" + iconId +
                ", applicationTitle='" + applicationTitle + '\'' +
                ", cpuUsage=" + cpuUsage +
                ", memoryUsage=" + memoryUsage +
                ", isChecked=" + isChecked +
                '}';
    }
}
