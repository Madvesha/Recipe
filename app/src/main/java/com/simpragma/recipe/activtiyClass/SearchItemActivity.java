package com.simpragma.recipe.activtiyClass;


import android.app.ProgressDialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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

import com.simpragma.recipe.Constants;
import com.simpragma.recipe.api.RecipeApi;
import com.simpragma.recipe.api.RecipeService;
import com.simpragma.recipe.recipeapp.R;
import com.simpragma.recipe.roomDatabase.AppDatabase;
import com.simpragma.recipe.adapterclass.DataAdapter;
import com.simpragma.recipe.pojoclass.Recipe;
import com.simpragma.recipe.roomDatabase.RecipeRoomEntityClass;
import com.simpragma.recipe.pojoclass.RecipePojoClass;
import com.simpragma.recipe.adapterclass.RoomViewAdapter;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import retrofit2.Call;
import retrofit2.Callback;

import static android.nfc.tech.MifareUltralight.PAGE_SIZE;

public class SearchItemActivity extends AppCompatActivity implements View.OnClickListener {

    EditText searchEditText;
    Button searchButton;
    TextView errorMessageTextView;

    String TAG = getClass().getSimpleName();
    ArrayList<RecipePojoClass> reciperesults;
    List<RecipeRoomEntityClass> recipesList;
    ArrayList<RecipePojoClass> addrecipeList;

    Recipe post;
    RecyclerView recyclerView;
    RecipePojoClass recipePojoClass;

    GridLayoutManager gridLayoutManager;
    ProgressBar progressBar;
    ProgressDialog mProgressDialog;

    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES;
    private int currentPage = 1;
    private RecipeService recipeService;

    private DataAdapter customAdapter;
    private RoomViewAdapter roomViewAdapter;

    AppDatabase roomORMClass;
    String version = "";

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
        errorMessageTextView = (TextView) findViewById(R.id.tv_notfound);
        progressBar = findViewById(R.id.loadmore_progress);
        reciperesults = new ArrayList<RecipePojoClass>();
        addrecipeList = new ArrayList<RecipePojoClass>();
        recipePojoClass = new RecipePojoClass();

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading....");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);

        recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        //Declare Room database object
        roomORMClass = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "recipeMangerRoom")
                .fallbackToDestructiveMigration()
                .build();

        new RecipeRetriveAsyncTask().execute();

        searchButton.setOnClickListener(this);
        recipeService = RecipeApi.getClient().create(RecipeService.class);

    }

    @Override
    protected void onStart() {
        super.onStart();
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

    @Override
    public void onClick(View view) {
        ConnectivityManager ConnectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected() == true) {
            currentPage = 1;
            reciperesults.clear();
            isLastPage = false;
            isLoading = false;

            new RecipeDeleteAsyncTask().execute();

            customAdapter = new DataAdapter(SearchItemActivity.this, post, reciperesults);
            // set the Adapter to RecyclerView
            recyclerView.setAdapter(customAdapter);
            customAdapter.notifyDataSetChanged();
            addrecipeList.clear();

            recyclerView.addOnScrollListener(new PaginationScrollListener(gridLayoutManager) {
                @Override
                protected void loadMoreItems() {
                    isLoading = true;
                    currentPage += 1;
                    Log.d("Current Page",currentPage+"");

                    // mocking network delay for API call
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadNextPage();
                        }
                    }, 3000);
                }

                @Override
                public int getTotalPageCount() {
                    return TOTAL_PAGES;
                }

                @Override
                public boolean isLastPage() {
                    return isLastPage;
                }

                @Override
                public boolean isLoading() {
                    return isLoading;
                }
            });

        } else {
            Toast.makeText(SearchItemActivity.this,
                    "Network Not Available", Toast.LENGTH_LONG).show();
        }
    }

    private String checkPackageInfo() {
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

    private void loadFirstPage() {
        Log.d(TAG, "loadFirstPage: ");

        callRecipeApi().enqueue(new Callback<Recipe>() {
            @Override
            public void onResponse(Call<Recipe> call, retrofit2.Response<Recipe> response) {
                // Got data. Send it to adapter
                if (response.code() == 500) {
                    recyclerView.setVisibility(View.GONE);
                    errorMessageTextView.setVisibility(View.VISIBLE);
                    errorMessageTextView.setText("Page is not found");
                } else if (response.body().getResults().size() == 0) {
                    recyclerView.setVisibility(View.GONE);
                    errorMessageTextView.setVisibility(View.VISIBLE);
                    errorMessageTextView.setText("No recipe available for given ingredients.");
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    errorMessageTextView.setVisibility(View.GONE);
                    List<RecipePojoClass> results = fetchResults(response);
                    customAdapter.addAll(results);
                    customAdapter.notifyDataSetChanged();
                }
                Log.d("addrecipeList", addrecipeList.size() + "");
                Log.d("reciperesults", reciperesults.size() + "");

                HashSet<RecipePojoClass> recipeListSet = new HashSet<>(addrecipeList);
                recipeListSet.addAll(addrecipeList);
                addrecipeList.clear();
                addrecipeList.addAll(recipeListSet);

                if (currentPage <= TOTAL_PAGES)
                    customAdapter.addLoadingFooter();
                else
                    isLastPage = true;
            }

            @Override
            public void onFailure(Call<Recipe> call, Throwable t) {
                t.printStackTrace();
            }
        });
        new RecipeInsetAsyncTask().execute();
    }

    /**
     * z
     *
     * @param response extracts List<{@link Recipe>} from response
     * @return
     */
    private List<RecipePojoClass> fetchResults(retrofit2.Response<Recipe> response) {
        Recipe recipeList = response.body();
        return recipeList.getResults();
    }

    private void loadNextPage() {
        Log.d(TAG, "loadNextPage: " + currentPage);
        callRecipeApi().enqueue(new Callback<Recipe>() {
            @Override
            public void onResponse(Call<Recipe> call, retrofit2.Response<Recipe> response) {
                customAdapter.removeLoadingFooter();
                isLoading = false;
                addrecipeList.removeAll(addrecipeList);

                if (response.code() == 500) {
                    customAdapter = new DataAdapter(SearchItemActivity.this, post, reciperesults);
                    // set the Adapter to RecyclerView
                    recyclerView.setAdapter(customAdapter);

                } else if (response.body().getResults().size() == 0 || response.body().getResults().equals("")) {
                    recyclerView.setVisibility(View.GONE);
                    errorMessageTextView.setVisibility(View.VISIBLE);
                    errorMessageTextView.setText("No recipe available for given ingredients");
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    errorMessageTextView.setVisibility(View.GONE);
                    List<RecipePojoClass> results = fetchResults(response);
                    customAdapter.addAll(results);
                    addrecipeList.addAll(results);
                    Log.d("Results", results.size() + "");
                    customAdapter.notifyDataSetChanged();
                }
                // addrecipeList.addAll(reciperesults);
                Log.d("addrecipeList", addrecipeList.size() + "");
                Log.d("reciperesults", reciperesults.size() + "");

                HashSet<RecipePojoClass> recipeListSet = new HashSet<>(addrecipeList);
                recipeListSet.addAll(addrecipeList);
                addrecipeList.clear();
                addrecipeList.addAll(recipeListSet);

                if (currentPage != TOTAL_PAGES)
                    customAdapter.addLoadingFooter();
                else
                    isLastPage = true;
            }

            @Override
            public void onFailure(Call<Recipe> call, Throwable t) {
                t.printStackTrace();
            }
        });

        new RecipeInsetAsyncTask().execute();
    }


    /**
     * Performs a Retrofit call to the top rated movies API.
     * Same API call for Pagination.
     * As {@link #currentPage} will be incremented automatically
     * by @{@link PaginationScrollListener} to load next page.
     */
    private Call<Recipe> callRecipeApi() {
        return recipeService.getRecipe(
                searchEditText.getText().toString(),
                currentPage);
    }

    private class RecipeRetriveAsyncTask extends AsyncTask<Void, Void, List<RecipeRoomEntityClass>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected List<RecipeRoomEntityClass> doInBackground(Void... voids) {

            recipesList = roomORMClass.recipeDao().getAll();
            Log.d("AsyncTask", recipesList.size() + "");
            return recipesList;
        }

        @Override
        protected void onPostExecute(List<RecipeRoomEntityClass> roomEntities) {
            super.onPostExecute(roomEntities);
            mProgressDialog.dismiss();
            Log.d("RoomDataBase ResultList", roomEntities.size() + "");
            if (roomEntities.size() > 0) {
                roomViewAdapter = new RoomViewAdapter(SearchItemActivity.this, roomEntities);
                recyclerView.setAdapter(roomViewAdapter);
            }

        }
    }

    private class RecipeDeleteAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            roomORMClass.recipeDao().deleteRecipe();
            Log.d("Deletd", "Recipes");
            return null;
        }

        @Override
        protected void onPostExecute(Void avoid) {
            super.onPostExecute(avoid);
            mProgressDialog.dismiss();
            Log.d("New Recipes", "Loading");
            loadFirstPage();
        }
    }

    private class RecipeInsetAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            Log.d("RecipeInsetAsyncTask", addrecipeList.size() + "");

            List<RecipePojoClass> myList = Collections.synchronizedList(new ArrayList<RecipePojoClass>());
            myList.addAll(addrecipeList);

            for (RecipePojoClass rs : myList) {
                roomORMClass.recipeDao().insertAll(new
                        RecipeRoomEntityClass(rs.getId(), rs.getTitle(), rs.getIngredients(), rs.getHref(), rs.getThumbnail()));
                Log.d("Inside Button Click", "Title" + rs.getTitle() + "Ingredients" +
                        rs.getIngredients() + "Thumbnail" + rs.getThumbnail());

            }
            return null;
        }
    }
}







