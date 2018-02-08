package com.simpragma.recipe.recipeapp;


import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.DatabaseHandler;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.spimragma.recipe.Constants;


import java.util.ArrayList;

public class SearchItemActivity extends AppCompatActivity implements View.OnClickListener {

    EditText eidiText;
    Button search_button;
    String TAG = getClass().getSimpleName(), url;
    ArrayList<ResultList> reciperesults;
    Recipie post;
    RecyclerView recyclerView;
    private DataAdapter customAdapter;
    DataBaseAdapter dataBaseAdapter;
    ArrayList<ResultList> dataBaseResults;
    DatabaseHandler db;
    ResultList result;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_item1);

        if (com.spimragma.recipe.Constants.type == com.spimragma.recipe.Constants.Type.Staging) {
            Log.d(TAG, "STAGING VERSION");
        } else {
            Log.d(TAG, " PRODUCTION VERSION");
        }

        eidiText = (EditText) findViewById(R.id.editText_search);
        search_button = (Button) findViewById(R.id.button_search);
        textView = (TextView) findViewById(R.id.textnotfound);
        recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(SearchItemActivity.this, 2));
        db = new DatabaseHandler(this);

        search_button.setOnClickListener(this);

        dataBaseResults = db.getAllContacts();
        Log.d("DataBase ResultList", dataBaseResults.size() + "");
        if (dataBaseResults.size() > 0) {
            dataBaseAdapter = new DataBaseAdapter(SearchItemActivity.this, dataBaseResults);
            recyclerView.setBackgroundColor(Color.LTGRAY);
            recyclerView.setAdapter(dataBaseAdapter);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        dataBaseResults = db.getAllContacts();
        Log.d("DataBase ResultList", dataBaseResults.size() + "");
        if (dataBaseResults.size() > 0) {
            dataBaseAdapter = new DataBaseAdapter(SearchItemActivity.this, dataBaseResults);
            recyclerView.setAdapter(dataBaseAdapter);
        }

        for (ResultList cn : dataBaseResults) {
            String log = "Id: " + cn.getId() + " ,Title: " + cn.getTitle()
                    + " ,Ingredients: " + cn.getIngredients() + "Href" + cn.getHref() +
                    " ,Thumbnail" + cn.getThumbnail();
            // Inserting Database  to log
            Log.d("Insert Database Values:", log);
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

        Uri uri = Uri.parse(getString(R.string.url) + eidiText.getText().toString());
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
                post = mGson.fromJson(response, Recipie.class);
                reciperesults = (ArrayList<ResultList>) post.getResults();

                recyclerView.setVisibility(View.VISIBLE);
                customAdapter = new DataAdapter(SearchItemActivity.this, post, reciperesults);
                recyclerView.setBackgroundColor(Color.CYAN);
                // set the Adapter to RecyclerView
                recyclerView.setAdapter(customAdapter);

                //Deleteing DB values
                db.deleteContact(result);

                Log.d("Deleted Items", "All items Deleted");

                for (ResultList rs : reciperesults) {
                    db.addContact(new ResultList(rs.getId(), rs.getTitle(), rs.getIngredients(),
                            rs.getHref(), rs.getThumbnail()));

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
        getRecipesByIngredients();
    }

}


