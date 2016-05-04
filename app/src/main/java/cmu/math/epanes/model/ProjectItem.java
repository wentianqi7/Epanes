package cmu.math.epanes.model;

/**
 * Created by STuotuo.Wen on 2016/5/1.
 */
public class ProjectItem {
    private int pid;
    private String title;
    private String url;
    private long time;

    public ProjectItem(int pid, String title, String url) {
        this.pid = pid;
        this.title = title;
        this.url = url;
        this.time = System.currentTimeMillis();
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getPid() {
        return pid;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public Long getTime() {
        return time;
    }
}
