package com.databasehandler;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.datamodel.RssItem;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "NewsManager";

    // Contacts table name
    private static final String TABLE_NEWS = "News";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_LINK = "link";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_LIKE_BY_ME = "like";
    private static final String KEY_TYPE = "type";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NEWS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_DESCRIPTION + " TEXT," + KEY_LINK + " TEXT," + KEY_IMAGE + " TEXT," + KEY_LIKE_BY_ME + " TEXT," + KEY_TYPE + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NEWS);

        // Create tables again
        onCreate(db);
    }


    // Adding new news
    public void addNews(List<RssItem> news) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < news.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(KEY_NAME, news.get(i).getTitle());
            values.put(KEY_DESCRIPTION, news.get(i).getDescription());

            values.put(KEY_LINK, news.get(i).getLink());
            values.put(KEY_IMAGE, news.get(i).getImageUrl());
            values.put(KEY_LIKE_BY_ME, "NO");
            values.put(KEY_TYPE, news.get(i).getType());

            db.insert(TABLE_NEWS, null, values);
        }
        db.close(); // Closing database connection

    }


    // Getting All News
    public List<RssItem> getAllNews() {
        List<RssItem> newsList = new ArrayList<RssItem>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NEWS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                RssItem contact = new RssItem();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setTitle(cursor.getString(1));
                contact.setDescription(cursor.getString(2));
                contact.setLink(cursor.getString(3));
                contact.setImageUrl(cursor.getString(4));
                contact.setLikebyme(cursor.getString(5));
                contact.setType((cursor.getString(6)));

                newsList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return newsList;
    }

    // Updating single news
    public int updateContact(RssItem news) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_LIKE_BY_ME, "YES");

        // updating row
        return db.update(TABLE_NEWS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(news.getId())});
    }


    public Cursor getNews() {

        SQLiteDatabase db = null;
        try {
            db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NEWS, null);

            if (null != cursor && cursor.moveToFirst()) {
                return cursor;
            }
            return cursor;
        } catch (Exception e) {
            e.toString();
        }
        return null;

    }
}
