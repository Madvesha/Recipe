package com.simpragma.recipe.roomdatabase;


import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.simpragma.recipe.recipeapp.R;


import java.util.ArrayList;
import java.util.List;

public class SearchItemActivity extends AppCompatActivity implements View.OnClickListener {

    EditText searchEditText;
    Button searchButton;
    String TAG = getClass().getSimpleName(), url;

    ArrayList<ResultList> reciperesults;
    Recipe post;
    RecyclerView recyclerView;

    private DataAdapter customAdapter;

    ArrayList<ResultList> dataBaseResults;
    ResultList result;
    TextView textView;
    String version = "";
    PackageInfo pInfo;

    AppDatabase db1;
    RecipeRoomDB recipeRoomDB;
    List<com.simpragma.recipe.roomdatabase.RecipeRoomDB> recipesList;
    RoomDatabaseAdapter roomDataBaseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_item1);

        if (com.simpragma.recipe.Constants.type == com.simpragma.recipe.Constants.Type.Staging) {
            try {
                pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                version = pInfo.versionName + " " + pInfo.packageName;
                Log.d(TAG, " STAGING VERSION" + version);

            } catch (PackageManager.NameNotFoundException e) {
                Log.e(TAG, "Error: NameNotFoundException" + e.getMessage());
                Toast.makeText(this, "Error: NameNotFoundException" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {

            try {
                pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                version = pInfo.versionName + " " + pInfo.packageName;
                Log.d(TAG, " PRODUCTION VERSION" + version);
            } catch (PackageManager.NameNotFoundException e) {
                Log.e(TAG, "Error: NameNotFoundException" + e.getMessage());
                Toast.makeText(this, "Error: NameNotFoundException" + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        searchEditText = (EditText) findViewById(R.id.et_search);
        searchButton = (Button) findViewById(R.id.bt_search);
        textView = (TextView) findViewById(R.id.tv_notfound);
        recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(SearchItemActivity.this, 2));

        //Declare Room database object
        db1 = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "recipeMangerRoom").allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        recipesList = db1.recipeDao().getAll();

        searchButton.setOnClickListener(this);

        if (recipesList.size() > 0) {
            for (RecipeRoomDB model : recipesList) {
                Log.d(TAG, model.getId() + "");
                Log.d(TAG, model.getTitle());
                Log.d(TAG, model.getIngredients());
                Log.d(TAG, model.getHref());
                Log.d(TAG, model.getThumbnail());

            }

        }

        Log.d("RoomDataBase ResultList", recipesList.size() + "");
        if (recipesList.size() > 0) {
            roomDataBaseAdapter = new RoomDatabaseAdapter(SearchItemActivity.this, recipesList);
            recyclerView.setBackgroundColor(Color.LTGRAY);
            recyclerView.setAdapter(roomDataBaseAdapter);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();


        Log.d("RoomDataBase ResultList", recipesList.size() + "");
        if (recipesList.size() > 0) {
            roomDataBaseAdapter = new RoomDatabaseAdapter(SearchItemActivity.this, recipesList);
            recyclerView.setBackgroundColor(Color.LTGRAY);
            recyclerView.setAdapter(roomDataBaseAdapter);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = getIntent();
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void getRecipesByIngredients() {

        Uri uri = Uri.parse(getString(R.string.url) + searchEditText.getText().toString());
        url = uri.toString();

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Response " + url);
                Log.d(TAG, "Response " + response);
                // creating GSON Builder
                GsonBuilder builder = new GsonBuilder();
                Gson mGson = builder.create();

                //JSON paring Vales
                post = mGson.fromJson(response, Recipe.class);
                reciperesults = (ArrayList<ResultList>) post.getResults();

                recyclerView.setVisibility(View.VISIBLE);
                customAdapter = new DataAdapter(SearchItemActivity.this, post, reciperesults);
                recyclerView.setBackgroundColor(Color.CYAN);

                // set the Adapter to RecyclerView
                recyclerView.setAdapter(customAdapter);

                for (RecipeRoomDB model : recipesList) {
                    Log.d(TAG, model.getId() + "");
                    Log.d(TAG, model.getTitle());
                    Log.d(TAG, model.getIngredients());
                    Log.d(TAG, model.getHref());
                    Log.d(TAG, model.getThumbnail());
                }

                Log.d(TAG, "Deletd Items From ROOMDB");
                db1.recipeDao().deleteRecipe();

                for (ResultList rs : reciperesults) {

                    db1.recipeDao().insertAll(new
                            com.simpragma.recipe.roomdatabase.RecipeRoomDB(rs.getId(), rs.getTitle(), rs.getIngredients(), rs.getHref(), rs.getThumbnail()));

                    Log.d("Inside Button Click", "Title" + rs.getTitle() + "Ingredients" +
                            rs.getIngredients() + "Thumbnail" + rs.getThumbnail());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error " + error.getMessage());
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.statusCode == 500) {
                    recyclerView.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                }
            }
        });
        queue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {

        ConnectivityManager ConnectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected() == true) {
            getRecipesByIngredients();

        } else {
            Toast.makeText(SearchItemActivity.this, "Network Not Available", Toast.LENGTH_LONG).show();
        }
    }
}


