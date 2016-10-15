package acec.antiframes.carteirinha;

import android.content.Context;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

class DatabaseHelper {

    static void saveNews(final MenuItem news){
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(news);
            }
        });
    }

    static List<MenuItem> getNews(){
        return Realm.getDefaultInstance().where(MenuItem.class)
                .findAll();
    }

}
