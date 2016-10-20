package acec.antiframes.carteirinha;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class NewsItem extends RealmObject{
    private String title;
    @PrimaryKey
    private long timestamp;
    private String url;

    public NewsItem() {
    }

    public NewsItem(String title, long timestamp, String url) {
        this.title = title;
        this.timestamp = timestamp;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
