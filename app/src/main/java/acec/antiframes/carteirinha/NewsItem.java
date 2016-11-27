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



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    String getUrl() {
        return url;
    }

    void setUrl(String url) {
        this.url = url;
    }
}
