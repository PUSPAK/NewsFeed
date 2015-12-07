package com.example.divum.newsfeed;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ListView;

import com.RssReader.RssReader;
import com.app.adapter.FeedListAdapter;
import com.databasehandler.DatabaseHandler;
import com.datamodel.RssItem;

import java.util.ArrayList;
import java.util.List;


public class Activity_feedList extends AppCompatActivity {
    List<RssItem> items;
    ListView mList;
    Context context;
    FeedListAdapter adapter;
    DatabaseHandler db;
    List<RssItem> newsList;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        initToolbar();
        mList = (ListView) findViewById(R.id.list);
        items = new ArrayList<>();
        newsList = new ArrayList<>();
        context = this;
        db = new DatabaseHandler(this);


        Cursor cursor = db.getNews();
        if (cursor != null && cursor.getCount() == 0) {

            new GetRssFeed().execute("http://www.pcworld.com/index.rss");


        } else {


            new GetRssFeedfromDB().execute();
        }
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_menu_black_24dp);

    }


    private class GetRssFeed extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            findViewById(R.id.progresslayout).setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                RssReader rssReader = new RssReader(params[0]);

                items = rssReader.getItems();

                db.addNews(items);

            } catch (Exception e) {
                Log.v("Error Parsing Data", e + "");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            newsList = db.getAllNews();
            System.out.print("newsList SIZE" + newsList.size());
            if (newsList.size() > 0) {
                findViewById(R.id.progresslayout).setVisibility(View.GONE);
                findViewById(R.id.list).setVisibility(View.VISIBLE);
                adapter = new FeedListAdapter(context, newsList);
                mList.setAdapter(adapter);

            } else {
                findViewById(R.id.progresslayout).setVisibility(View.GONE);
                findViewById(R.id.nodatalayout).setVisibility(View.VISIBLE);
            }

        }
    }

    private class GetRssFeedfromDB extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            findViewById(R.id.progresslayout).setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                newsList = db.getAllNews();
                System.out.println("List item size" + newsList.size());

            } catch (Exception e) {
                Log.v("Error Parsing Data", e + "");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            System.out.print("newsList SIZE" + newsList.size());
            if (newsList.size() > 0) {
                findViewById(R.id.progresslayout).setVisibility(View.GONE);
                findViewById(R.id.list).setVisibility(View.VISIBLE);
                adapter = new FeedListAdapter(context, newsList);
                mList.setAdapter(adapter);

            } else {
                findViewById(R.id.progresslayout).setVisibility(View.GONE);
                findViewById(R.id.nodatalayout).setVisibility(View.VISIBLE);
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

}
