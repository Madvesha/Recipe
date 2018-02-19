package com.simpragma.recipe.util;


import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
import com.simpragma.recipe.Constants;
import com.simpragma.recipe.recipeapp.R;
import com.simpragma.recipe.roomDatabase.AppDatabase;
import com.simpragma.recipe.adapterclass.DataAdapter;
import com.simpragma.recipe.pojoclass.Recipe;
import com.simpragma.recipe.roomDatabase.RecipeRoomDB;
import com.simpragma.recipe.pojoclass.RecipetList;
import com.simpragma.recipe.roomDatabase.RoomDatabaseAdapter;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SearchItemActivity extends AppCompatActivity implements View.OnClickListener {

    EditText searchEditText;
    Button searchButton;
    String TAG = getClass().getSimpleName(), url;
    ArrayList<RecipetList> reciperesults;
    ArrayList<RecipetList> addRecipelist;
    Recipe post;
    RecyclerView recyclerView;

    GridLayoutManager gridLayoutManager;
    ProgressBar progressBar;

    private final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 3;
    private int currentPage;

    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    private DataAdapter customAdapter;
    TextView textView;
    AppDatabase roomDB;
    RecipeRoomDB recipeRoomDB;
    List<com.simpragma.recipe.roomDatabase.RecipeRoomDB> recipesList;
    RoomDatabaseAdapter roomDataBaseAdapter;
    String version = "";
    Set<RecipetList> set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_item1);

        if (com.simpragma.recipe.Constants.type == Constants.Type.Production) {
            version = checkPackageInfo();
            Log.d(TAG, " PRODUCTION VERSION" + version);
        } else {
            version = checkPackageInfo();
            Log.d(TAG, " STAGING VERSION" + version);
        }

        searchEditText = (EditText) findViewById(R.id.et_search);
        searchButton = (Button) findViewById(R.id.bt_search);
        textView = (TextView) findViewById(R.id.tv_notfound);
        addRecipelist = new ArrayList<RecipetList>();
        reciperesults = new ArrayList<RecipetList>();
        set = new HashSet<RecipetList>();

        recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        //Declare Room database object
        roomDB = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "recipeMangerRoom").allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

        recipesList = roomDB.recipeDao().getAll();
        searchButton.setOnClickListener(this);

        Log.d("RoomDataBase ResultList", recipesList.size() + "");
        if (recipesList.size() > 0) {
            roomDataBaseAdapter = new RoomDatabaseAdapter(SearchItemActivity.this, recipesList);
            recyclerView.setAdapter(roomDataBaseAdapter);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d("RoomDataBase ResultList", recipesList.size() + "");
        if (recipesList.size() > 0) {
            roomDataBaseAdapter = new RoomDatabaseAdapter(SearchItemActivity.this, recipesList);
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
        Uri uri = Uri.parse(getString(R.string.url) + searchEditText.getText().toString() + "&p=" + currentPage);
        url = uri.toString();
        Log.d(TAG, "URL NAME " + url + " " + currentPage);

        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d(TAG, "Response " + response.toString());
                // creating GSON Builder
                GsonBuilder builder = new GsonBuilder();
                Gson mGson = builder.create();

                recyclerView.setVisibility(View.VISIBLE);
                //JSON paring Vales
                post = mGson.fromJson(response, Recipe.class);
                reciperesults = (ArrayList<RecipetList>) post.getResults();
                if (reciperesults.size() == 0) {
                    recyclerView.setVisibility(View.GONE);
                    textView.setVisibility(View.VISIBLE);
                    textView.setText("No recipe available given ingredients");

                } else {
                    textView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    addRecipelist.addAll(reciperesults);
                    Log.d("Add Array List Size", addRecipelist.size() + "");
                    customAdapter = new DataAdapter(SearchItemActivity.this, post, reciperesults);
                    // set the Adapter to RecyclerView
                    recyclerView.setAdapter(customAdapter);
                    // customAdapter.notifyDataSetChanged();
                    customAdapter.notifyItemRangeChanged(0, customAdapter.getItemCount());
                }

                for (RecipeRoomDB model : recipesList) {
                    Log.d(TAG, model.getId() + "");
                    Log.d(TAG, model.getTitle());
                    Log.d(TAG, model.getIngredients());
                    Log.d(TAG, model.getHref());
                    Log.d(TAG, model.getThumbnail());
                }

                for (RecipetList re : addRecipelist) {
                    Log.d("AddRecipelist", "Title" + re.getTitle() + "Ingredients" +
                            re.getIngredients() + "Thumbnail" + re.getThumbnail());

                }
                for (RecipetList rs : reciperesults) {

                    roomDB.recipeDao().insertAll(new
                            com.simpragma.recipe.roomDatabase.RecipeRoomDB(rs.getId(), rs.getTitle(), rs.getIngredients(), rs.getHref(), rs.getThumbnail()));

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
        if (currentPage == PAGE_START) {
            Log.d(TAG, "Deletd Items From ROOMDB");
            roomDB.recipeDao().deleteRecipe();
        }
        Log.d("After Response", currentPage + "");
    }

    @Override
    public void onClick(View v) {
        ConnectivityManager ConnectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected() == true) {
            currentPage = PAGE_START;
            getRecipesByIngredients();
            addRecipelist.clear();
            reciperesults.clear();

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    if (dy > 0) //check for scroll down
                    {
                        visibleItemCount = gridLayoutManager.getChildCount();
                        totalItemCount = gridLayoutManager.getItemCount();
                        pastVisiblesItems = gridLayoutManager.findFirstVisibleItemPosition();

                        if (gridLayoutManager.findLastCompletelyVisibleItemPosition() == reciperesults.size() - 1) {
                            //bottom of list!
                            int position = gridLayoutManager.findLastCompletelyVisibleItemPosition();
                            int numberOfItems = recyclerView.getAdapter().getItemCount();
                            Log.d(TAG, position + " " + numberOfItems);
                            currentPage++;

                            if (position >= numberOfItems) {
                                customAdapter.notifyItemRangeChanged(0, customAdapter.getItemCount());
                            }
                            if (currentPage <= TOTAL_PAGES) {
                                loadNextPage();
                            }
                        }

                    } else if (dy < 0) {

                        Log.d("Add Array List Size", addRecipelist.size() + "");
                        customAdapter = new DataAdapter(SearchItemActivity.this, post, addRecipelist);
                        // set the Adapter to RecyclerView
                        recyclerView.setAdapter(customAdapter);
                        Log.d(TAG, addRecipelist.size() + "");
                    }
                }
            });

        } else {
            Toast.makeText(SearchItemActivity.this,
                    "Network Not Available", Toast.LENGTH_LONG).show();
        }
    }

    public String checkPackageInfo() {
        PackageInfo pInfo;

        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName + " " + pInfo.packageName;

        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Error: NameNotFoundException" + e.getMessage());
            Toast.makeText(this, "Error: NameNotFoundException" + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
        return version;
    }

    private void loadNextPage() {
        Log.d(TAG, "loadNextPage: " + currentPage);
        customAdapter.removeLoadingFooter();
        isLoading = false;
        getRecipesByIngredients();
    }
}


