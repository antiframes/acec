package acec.antiframes.carteirinha;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class MenuItem  extends RealmObject{
    private String title;
    private boolean isNews;

    @PrimaryKey
    private String url;

    public MenuItem(){}
    public MenuItem(String title, String url) {
        this.title = title;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    String getUrl() {
        return url;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    void setUrl(String url) {
        this.url = url;
    }

    public boolean isNews() {
        return isNews;
    }

    void setNews(boolean news) {
        isNews = news;
    }
}
