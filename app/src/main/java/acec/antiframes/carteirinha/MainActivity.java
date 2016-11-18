package acec.antiframes.carteirinha;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import io.realm.Realm;

import static android.content.Intent.ACTION_VIEW;

public class MainActivity extends Activity {
    private RecyclerView recyclerView;
    private RelativeLayout balloon;
    private TextView buttonHint;

    private LinearLayout menuButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Realm.init(getApplicationContext());
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.news_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        menuButton = (LinearLayout) findViewById(R.id.button_menu);
        balloon = (RelativeLayout) findViewById(R.id.balloon_touch);
        buttonHint=(TextView) findViewById(R.id.touch_hint);

        //inicia o daemon das notícias
        //startService(new Intent(getApplicationContext(),NotificationService.class));
        Intent alarmIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, alarmIntent, 0);

        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, AlarmManager.INTERVAL_HOUR, AlarmManager.INTERVAL_HOUR, pendingIntent);


        //pegar notícias
        List<NewsItem> newsFromDatabase=DatabaseHelper.getNews();
        if (newsFromDatabase.size()==0)
            new GetNewsTask().execute();
        else {

            recyclerView.setAdapter(
                    new NewsAdapter(DatabaseHelper.getNews())
            );
        }

        //pegar ítens do menu
        List<MenuItem> itemsFromDatabase=DatabaseHelper.getMenuItems();
        if (itemsFromDatabase.size()==0)
            new GetMenuItemsTask().execute();
        else {
            menuItems=itemsFromDatabase;
            menuButton.setVisibility(View.VISIBLE);
        }


        SharedPreferences prefs = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        boolean firstTime=prefs.getBoolean("firstTime",true);
        if (firstTime){
            balloon.setVisibility(View.VISIBLE);
            buttonHint.setVisibility(View.VISIBLE);
            prefs.edit().putBoolean("firstTime",false).apply();
        }
    }

    public void showCard(View v){
        balloon.setVisibility(View.GONE);
        Intent intent=new Intent(MainActivity.this,CardActivity.class);
        startActivity(intent);
    }


    public void showMenu(View v){

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        MenuDialog newFragment = new MenuDialog();
        newFragment.show(ft, "dialog");

    }

    public void hideHint(View v){
        balloon.setVisibility(View.GONE);
        buttonHint.setVisibility(View.GONE);
    }

    //pegar notícias
    private class GetNewsTask extends AsyncTask<Void,Void,List<NewsItem>>{

        @Override
        protected List<NewsItem> doInBackground(Void... voids) {
            return RSSHelper.getNews();
        }

        @Override
        protected void onPostExecute(List<NewsItem> newses) {
            for (NewsItem news:newses)
                DatabaseHelper.saveToDatabase(news);
            recyclerView.setAdapter(new NewsAdapter(newses));
        }
    }

    //pegar menu

    private List<MenuItem> menuItems;

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    private class GetMenuItemsTask extends AsyncTask<Void,Void,List<MenuItem>>{

        @Override
        protected List<MenuItem> doInBackground(Void... voids) {
            return RSSHelper.getMenuItems();
        }

        @Override
        protected void onPostExecute(List<MenuItem> items) {
            menuButton.setVisibility(View.VISIBLE);
            for (MenuItem item:items)
                DatabaseHelper.saveToDatabase(item);

            menuItems=items;
        }
    }



    //region Adapter das Notícias
    private class NewsAdapter extends RecyclerView.Adapter<ViewHolder>{
        private List<NewsItem> newsList;
        NewsAdapter(List<NewsItem> newsList){
            this.newsList=newsList;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_news,parent,false);
            return  new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            NewsItem news = newsList.get(position);
            holder.url = news.getUrl();
            holder.newsTitle.setPaintFlags(holder.newsTitle.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
            holder.newsTitle.setText(news.getTitle());

        }

        @Override
        public int getItemCount() {
            return newsList.size();
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder{
        TextView newsTitle;
        String url;
        ViewHolder(View itemView) {
            super(itemView);
            newsTitle = (TextView) itemView.findViewById(R.id.news_title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                }
            });
        }
    }
    //endregion

}
