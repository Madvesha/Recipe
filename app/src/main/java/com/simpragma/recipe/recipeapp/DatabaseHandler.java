package com.simpragma.recipe.recipeapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.simpragma.recipe.recipeapp.ResultList;

import java.util.ArrayList;

/**
 * Created by Madvesha on 2/4/18.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;
    // Database Name
    private static final String DATABASE_NAME = "recipeManager";
    // Recipe table name
    private static final String TABLE_RECIPE = "recipeitems";

    String TAG = getClass().getSimpleName();

    // Recipe Table Columns names
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
        //Creating  Recipe Table
        Log.d(TAG, "Creating Recipe Table");
        String CREATE_RECIPE_TABLE = "CREATE TABLE " + TABLE_RECIPE + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT,"
                + KEY_INGREDIENTS + " TEXT,"
                + KEY_HREF + " TEXT,"
                + KEY_THUMBNAIL + " TEXT" + ")";
        db.execSQL(CREATE_RECIPE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.d(TAG, "in onUpgrade. Old is: " + oldVersion + " New is: " + newVersion);

        if (newVersion > oldVersion) {
            // db.execSQL("ALTER TABLE Recipe ADD COLUMN NOTES TEXT");
            Log.d(TAG, "If any change alteration in table above table excute");
        }
    }


    // Adding new Recipe
    public void addRecipe(ResultList result) {
        Log.d(TAG, "Inserting values to Recipe Table");

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, result.getTitle()); // Recipe Title
        values.put(KEY_INGREDIENTS, result.getIngredients()); // Recipe Ingredients
        values.put(KEY_HREF, result.getHref()); // Recipe URL
        values.put(KEY_THUMBNAIL, result.getThumbnail()); // Recipe Image

        Log.d("KEY_TITLE", result.getTitle());
        Log.d("KEY_INGREDIENTS", result.getIngredients());
        Log.d("KEY_THUMBNAIL", result.getThumbnail());
        Log.d("KEY_HREF", result.getHref());

        // Inserting Recipe table Row
        db.insert(TABLE_RECIPE, null, values);
        db.close(); // Closing database connection
    }

    // Deleting  Recipe table rows
    public void deleteRecipe(ResultList result) {

        //Delete rows from Recipe Table
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d(TAG, "Delete row from Recipe Table");
        db.execSQL("delete from " + TABLE_RECIPE);
        db.close();
    }

    // Getting All Recipe
    public ArrayList<ResultList> getAllRecipe() {

        Log.d(TAG, "Retrieving values from Recipe Table");

        ArrayList<ResultList> recipeResults = new ArrayList<ResultList>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_RECIPE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ResultList recipeResult = new ResultList();
                recipeResult.setId(Integer.parseInt(cursor.getString(0)));
                recipeResult.setTitle(cursor.getString(1));
                recipeResult.setIngredients(cursor.getString(2));
                recipeResult.setHref(cursor.getString(3));
                recipeResult.setThumbnail(cursor.getString(4));
                // Adding Recipe to list
                recipeResults.add(recipeResult);
            } while (cursor.moveToNext());
        }
        // return Recipe list
        return recipeResults;
    }

}


