package acec.antiframes.carteirinha;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.ACTION_VIEW;

public class MainActivity extends Activity {
    private RecyclerView recyclerView;
    private FloatingActionButton menuButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.news_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        menuButton = (FloatingActionButton) findViewById(R.id.button_menu);
        new GetNewsTask().execute();
        new GetMenuItemsTask().execute();
    }

    public void showCard(View v){
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
            menuItems=items;
        }
    }



    //region Adapter das Notícias
    private class NewsAdapter extends RecyclerView.Adapter<ViewHolder>{
        private List<MenuItem> newsList;
        public NewsAdapter(List<MenuItem> newsList){
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
            if (urlText.length()>45)
                urlText = urlText.substring(0,45)+"...";
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
