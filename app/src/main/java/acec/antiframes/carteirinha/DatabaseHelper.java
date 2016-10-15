package acec.antiframes.carteirinha;


import java.util.List;

import io.realm.Realm;

class DatabaseHelper {

    static void saveToDatabase(final MenuItem news){
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(news);
            }
        });
    }

    static List<MenuItem> getNews(){
        return Realm.getDefaultInstance().where(MenuItem.class)
                .equalTo("isNews",true)
                .findAll();
    }


    static List<MenuItem> getMenuItems(){
        return Realm.getDefaultInstance().where(MenuItem.class)
                .equalTo("isNews",false)
                .findAll();
    }

}
