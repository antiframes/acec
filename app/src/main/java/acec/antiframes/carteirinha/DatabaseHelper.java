package acec.antiframes.carteirinha;


import java.util.List;

import io.realm.Realm;

class DatabaseHelper {

    static void saveToDatabase(final MenuItem item){
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(item);
            }
        });
    }


    static void saveToDatabase(final NewsItem news){
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(news);
            }
        });
    }

    static List<NewsItem> getNews(){
        return Realm.getDefaultInstance().where(NewsItem.class)
                .findAll();
    }


    static List<MenuItem> getMenuItems(){
        return Realm.getDefaultInstance().where(MenuItem.class)
                .findAll();
    }

}
