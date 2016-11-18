package acec.antiframes.carteirinha;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;


public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {


        new Thread(new Runnable() {
            @Override
            public void run() {
                List<NewsItem> news = RSSHelper.getNews();
                if(news==null)
                    return;
                for (int i=0;i<news.size()-1;i++){
                    DatabaseHelper.saveToDatabase(news.get(i));
                }


                for (NewsItem newsItem:news){
                }
                NewsItem lastItem = news.get(news.size()-1);
                boolean noNewsForUrl = DatabaseHelper.noNewsForUrl(lastItem);
                if (noNewsForUrl) {
                    DatabaseHelper.saveToDatabase(lastItem);
                    sendNotification(lastItem.getTitle(), context);
                }
                DatabaseHelper.clearOldNews();
            }
        }).start();

    }

    private void sendNotification(String lastNews,Context context){
        NotificationCompat.Builder builder=
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_notif)
                        .setContentTitle("ACEC - Novidades")
                        .setAutoCancel(true)
                        .setContentText(lastNews);
        Intent newIntent = new Intent(context, MainActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(newIntent);

        PendingIntent pendingIntent=
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        NotificationManager manager= (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0,builder.build());
    }


}
