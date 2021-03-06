package cn.jiangzehui.mds.model;

/**
 * Created by jiangzehui on 16/11/9.
 */
@SuppressWarnings("ALL")
public class Gif {

    private String title;
    private String url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Gif(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public Gif() {
        super();
    }
}
