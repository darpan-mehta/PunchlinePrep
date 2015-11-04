package com.myapplication.punchlineprep;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.SimpleTimeZone;

/**
 * Created by Jit on 10/26/2015.
 *
 */
public class JokeDBHandler extends SQLiteOpenHelper {

    private static JokeDBHandler sInstance;

    public static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "JokesDB";

    // Contacts table name
    private static final String JOKES_TABLE = "JokesTable";

    // Contacts Table Columns names
    private static final String COL_ID = "id";
    private static final String COL_TITLE = "title";
    private static final String COL_UPVOTES = "upvotes";
    private static final String COL_DOWNVOTES = "downvotes";
    private static final String COL_LENGTH = "length";
    private static final String COL_TIMESTAMP = "timestamp";
    private static final String COL_VOTED = "voted";

    public static synchronized JokeDBHandler getInstance(Context context){
        if (sInstance == null){
            sInstance = new JokeDBHandler(context.getApplicationContext());
        }
        return sInstance;
    }

    private JokeDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + JOKES_TABLE + "("
                + COL_ID + " INTEGER PRIMARY KEY," + COL_TITLE + " TEXT,"
                + COL_UPVOTES + " TEXT,"
                + COL_DOWNVOTES + " TEXT,"
                + COL_LENGTH + " TEXT,"
                + COL_TIMESTAMP + " TEXT,"
                + COL_VOTED + " TEXT"
                + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + JOKES_TABLE);

        // Create tables again
        onCreate(db);
    }

    public void updateTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + JOKES_TABLE);
        onCreate(db);
    }

    public void addJoke(JokeClass joke) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_TITLE, joke.getTitle());
        values.put(COL_UPVOTES, joke.getUpvotes());
        values.put(COL_DOWNVOTES, joke.getDownvotes());
        values.put(COL_LENGTH, joke.getLength());
        values.put(COL_TIMESTAMP, joke.getTimestamp());
        values.put(COL_VOTED, joke.getVoted());

        // Inserting Row
        db.insert(JOKES_TABLE, null, values);
        db.close(); // Closing database connection
    }

    public List<JokeClass> getAllJokes() {
        List<JokeClass> jokeList = new ArrayList<JokeClass>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + JOKES_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                JokeClass joke = new JokeClass();
                joke.setID(Integer.parseInt(cursor.getString(0)));
                joke.setTitle(cursor.getString(1));
                joke.setUpvotes(cursor.getString(2));
                joke.setDownvotes(cursor.getString(3));
                joke.setLength(cursor.getString(4));
                joke.setTimestamp(cursor.getString(5));
                joke.setVoted(cursor.getString(6));
                // Adding joke to list
                jokeList.add(joke);
            } while (cursor.moveToNext());
        }

        cursor.close();
        //db.close();

        // return joke list
        return jokeList;
    }

    public List<JokeClass> getTopJokes() {
        List<JokeClass> jokeList = new ArrayList<JokeClass>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + JOKES_TABLE + " ORDER BY " + COL_UPVOTES + " ASC";
        Log.v("mysqlquery",selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                JokeClass joke = new JokeClass();
                joke.setID(Integer.parseInt(cursor.getString(0)));
                joke.setTitle(cursor.getString(1));
                joke.setUpvotes(cursor.getString(2));
                joke.setDownvotes(cursor.getString(3));
                joke.setLength(cursor.getString(4));
                joke.setTimestamp(cursor.getString(5));
                joke.setVoted(cursor.getString(6));
                // Adding joke to list
                jokeList.add(joke);
            } while (cursor.moveToNext());
        }

        cursor.close();
        //db.close();

        // return joke list
        return jokeList;
    }

    public int getJokeCount() {
        String countQuery = "SELECT  * FROM " + JOKES_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        //cursor.close();
        //db.close();

        // return count
        return cursor.getCount();
    }

    public int updateJoke(JokeClass joke) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_TITLE, joke.getTitle());
        values.put(COL_UPVOTES, joke.getUpvotes());
        values.put(COL_DOWNVOTES, joke.getDownvotes());
        values.put(COL_LENGTH,joke.getLength());
        values.put(COL_TIMESTAMP,joke.getTimestamp());
        values.put(COL_VOTED, joke.getVoted());

        // updating row
        return db.update(JOKES_TABLE, values, COL_ID + " = ?",
                new String[]{String.valueOf(joke.getID())});
    }

    public void deleteJoke(JokeClass joke) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(JOKES_TABLE, COL_ID + " = ?",
                new String[]{String.valueOf(joke.getID())});
        db.close();
    }

}
