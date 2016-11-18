package acec.antiframes.carteirinha;


import java.util.List;

import io.realm.Realm;
import io.realm.Sort;

class DatabaseHelper {
    private static final int NEWS_LIMIT = 50;

    static void saveToDatabase(final MenuItem item){
        Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(item);
            }
        });
    }


    static void saveToDatabase(final NewsItem news){
        //bloqueia a mesma notícia de ser adicionada duas vezes
        //adiciona se notícia não estiver no banco
        if (noNewsForUrl(news))
            Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(news);
                }
            });
    }

    static boolean noNewsForUrl(final  NewsItem news){
        List<NewsItem> newsWithSameURL =
                Realm.getDefaultInstance().where(NewsItem.class).equalTo("url",news.getUrl()).findAll();
        return newsWithSameURL.size()==0;
    }

    static List<NewsItem> getNews(){
        return Realm.getDefaultInstance().where(NewsItem.class).findAll()
        .sort("timestamp").sort("timestamp", Sort.DESCENDING);
    }


    static List<MenuItem> getMenuItems(){
        return Realm.getDefaultInstance().where(MenuItem.class)
                .findAll();
    }

    static void clearOldNews(){
        List<NewsItem> news=getNews();
        if (news.size()<=NEWS_LIMIT)
            return;
        int exceeding = news.size()-NEWS_LIMIT;
        for (int i=0;i<exceeding;i++){
            news.get(i).deleteFromRealm();
        }
    }

}
