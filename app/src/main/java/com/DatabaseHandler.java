package com;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.simpragma.recipe.recipeapplication1.DataBaseResult;
import com.simpragma.recipe.recipeapplication1.Result;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shubhank on 2/4/18.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "recipeManager1";

    // Contacts table name
    private static final String TABLE_RECIPE = "recipeitems1";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_INGREDIENTS = "ingredients";
    private static final String KEY_THUMBNAIL = "thumbnail";
    private static final String KEY_HREF = "href";




    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_RECIPE + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT,"
                + KEY_INGREDIENTS + " TEXT,"
                + KEY_HREF + " TEXT,"
                + KEY_THUMBNAIL + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE);
        // Create tables again
        onCreate(db);
    }

    // Adding new contact
    public void addContact(DataBaseResult dataBaseResult) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, dataBaseResult.getTitle()); // Contact Name
        values.put(KEY_INGREDIENTS, dataBaseResult.getIngredients()); // Contact Phone Number
        values.put(KEY_HREF, dataBaseResult.getHref()); // Contact Phone Number
        values.put(KEY_THUMBNAIL, dataBaseResult.getThumbnail()); // Contact Phone Number


        Log.d("KEY_TITLE",dataBaseResult.getTitle());
        Log.d("KEY_INGREDIENTS",dataBaseResult.getIngredients());
        Log.d("KEY_THUMBNAIL",dataBaseResult.getThumbnail());
        Log.d("KEY_HREF",dataBaseResult.getHref());


        // Inserting Row
        db.insert(TABLE_RECIPE, null, values);
        db.close(); // Closing database connection
    }

    // Deleting single contact
    public void deleteContact(DataBaseResult dataBaseResult) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_RECIPE);
        db.close();
    }


    // Getting All Contacts
    public ArrayList<DataBaseResult> getAllContacts() {
        ArrayList<DataBaseResult> DBReseultList = new ArrayList<DataBaseResult>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_RECIPE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DataBaseResult dataBaseResult = new DataBaseResult();
                dataBaseResult.setId(Integer.parseInt(cursor.getString(0)));
                dataBaseResult .setTitle(cursor.getString(1));
                dataBaseResult.setIngredients(cursor.getString(2));
                dataBaseResult.setHref(cursor.getString(3));
                dataBaseResult.setThumbnail(cursor.getString(4));
                // Adding contact to list
                DBReseultList.add(dataBaseResult);
            } while (cursor.moveToNext());
        }
        // return contact list
        return DBReseultList;
    }


}


