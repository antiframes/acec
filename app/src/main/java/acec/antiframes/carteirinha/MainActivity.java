package acec.antiframes.carteirinha;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

        //pegar notícias
        List<MenuItem> newsFromDatabase=DatabaseHelper.getNews();
        if (newsFromDatabase.size()==0)
            new GetNewsTask().execute();
        else {
            menuButton.setVisibility(View.VISIBLE);
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
        }


        SharedPreferences prefs = this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        boolean firstTime=prefs.getBoolean("firstTime",true);
        if (firstTime){
            balloon.setVisibility(View.VISIBLE);
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

    //pegar notícias
    private class GetNewsTask extends AsyncTask<Void,Void,List<MenuItem>>{

        @Override
        protected List<MenuItem> doInBackground(Void... voids) {
            return RSSHelper.getNews();
        }

        @Override
        protected void onPostExecute(List<MenuItem> newses) {
            for (MenuItem news:newses)
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
        private List<MenuItem> newsList;
        NewsAdapter(List<MenuItem> newsList){
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
            MenuItem news = newsList.get(position);
            String urlText = news.getUrl();
            if (urlText.length()>60)
                urlText = urlText.substring(0,60)+"...";
            holder.newsUrl.setText(urlText);
            holder.url = news.getUrl();
            holder.newsTitle.setText(news.getTitle());

        }

        @Override
        public int getItemCount() {
            return newsList.size();
        }
    }

    private class ViewHolder extends RecyclerView.ViewHolder{
        TextView newsTitle;
        TextView newsUrl;
        String url;
        ViewHolder(View itemView) {
            super(itemView);
            newsTitle = (TextView) itemView.findViewById(R.id.news_title);
            newsUrl = (TextView) itemView.findViewById(R.id.news_url);
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
