package com.simpragma.recipe.recipeapplication1;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;

public class SearchItemActivity extends AppCompatActivity implements Filterable,View.OnClickListener {

    EditText eidiText;
    Button button;
    String TAG=getClass().getSimpleName();
    ArrayList<Result> results;
    private ArrayList<Result> mFilteredList;
    Recipie post;
    RecyclerView recyclerView;
    private DataAdapter customAdapter;
    DataBaseAdapter dataBaseAdapter;
    ArrayList<DataBaseResult> dataBaseResults;
    DatabaseHandler db;
    DataBaseResult dataBaseResult;
    String text,url;
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_item1);

        if(Constants1.Type.Staging == Constants1.Type.Staging)

        {
            Log.d(TAG,"FREE VERSION");
        }
        else
        {
            Log.d(TAG," PAID VERSION");
        }


        eidiText=(EditText) findViewById(R.id.editText_search);
        button=(Button) findViewById(R.id.button_search);
        textView=(TextView) findViewById(R.id.textnotfound);
        recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(SearchItemActivity.this,2));
        db = new DatabaseHandler(this);


        button.setOnClickListener(this);
        //requestJsonObject();

//        Intent intent = getIntent();
//        startActivity(intent);
        dataBaseResults = db.getAllContacts();
        Log.d("DataBase Result",dataBaseResults.size()+"");
        if(dataBaseResults.size()>0)
        {
            dataBaseAdapter=new DataBaseAdapter(SearchItemActivity.this,dataBaseResults);
            recyclerView.setBackgroundColor(Color.LTGRAY);
            recyclerView.setAdapter(dataBaseAdapter);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

         //requestJsonObject();
       // button.setOnClickListener(this);

        dataBaseResults = db.getAllContacts();
        Log.d("DataBase Result",dataBaseResults.size()+"");
        if(dataBaseResults.size()>0)
        {
            dataBaseAdapter=new DataBaseAdapter(SearchItemActivity.this,dataBaseResults);
            recyclerView.setAdapter(dataBaseAdapter);
        }

        for (DataBaseResult cn : dataBaseResults) {
            String log = "Id: "+cn.getId()+" ,Title: " + cn.getTitle()
                    + " ,Ingredients: " + cn.getIngredients() +"Href"+ cn.getHref()+ " ,Thumbnail"+ cn.getThumbnail();
            // Writing Contacts to log
            Log.d("Name: ", log);
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

    private void requestJsonObject(){

        text=eidiText.getText().toString();

        Uri uri = Uri.parse("http://www.recipepuppy.com/api/?i="+text+"");
        url=uri.toString();


        RequestQueue queue = Volley.newRequestQueue(this);


      // String url ="http://www.recipepuppy.com/api/?i="+text;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response " + url);

                Log.d(TAG, "Response " + response);


                GsonBuilder builder = new GsonBuilder();
                Gson mGson = builder.create();
                 post = mGson.fromJson(response, Recipie.class);
                 results = (ArrayList<Result>) post.getResults();

//                eidiText.addTextChangedListener(new TextWatcher() {
//
//                    @Override
//                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//                        // TODO Auto-generated method s
//                       text.toLowerCase(Locale.getDefault());
//                        getFilter().filter(text);
//
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable s) {
//                         text.toLowerCase(Locale.getDefault());
//                        getFilter().filter(text);
//
//                    }
//
//                    @Override
//                    public void beforeTextChanged(CharSequence s, int start, int count,
//                                                  int after) {
//                        // TODO Auto-generated method stub
//
//                    }
//
//                });
                recyclerView.setVisibility(View.VISIBLE);

                customAdapter = new DataAdapter(SearchItemActivity.this, post, results);
                recyclerView.setBackgroundColor(Color.CYAN);
                recyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView

                db.deleteContact(dataBaseResult);
                Log.d("Deleted Items", "All items Deleted");

                for (Result rs : results) {
                    db.addContact(new DataBaseResult(rs.getId(), rs.getTitle(), rs.getIngredients(), rs.getHref() ,rs.getThumbnail()));
                    Log.d("Inside Button Click", "Title" + rs.getTitle() + "Ingredients" + rs.getIngredients() + "Thumbnail" + rs.getThumbnail());
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
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    results = mFilteredList;
                } else {
                    ArrayList<Result> filteredList = new ArrayList<>();
                    for (Result row : results) {
                        if (row.getIngredients().toLowerCase().contains(charString.toLowerCase()) || row.getTitle().contains(charString.toLowerCase()) ) {
                            filteredList.add(row);
                        }
                    }
                    results =  filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = results;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                results = (ArrayList<Result>) filterResults.values;
                if(results != null)
                {
                    results = (ArrayList<Result>) filterResults.values;
                }
                else
                {
                    results = (ArrayList<Result>) post.getResults();
                }
            }
        };

    }

    @Override
    public void onClick(View v) {

//        Uri uri = Uri.parse("http://www.recipepuppy.com/api/?i="+text+"");
//        url=uri.toString();

        requestJsonObject();

/*        customAdapter = new DataAdapter(SearchItemActivity.this, post, results);
        recyclerView.setBackgroundColor(Color.CYAN);
        recyclerView.setAdapter(customAdapter); // set the Adapter to RecyclerView
        db.deleteContact(dataBaseResult);
        Log.d("Deleted Items", "All items Deleted");

            for (Result rs : results) {
                db.addContact(new DataBaseResult(rs.getId(), rs.getTitle(), rs.getIngredients(), rs.getThumbnail()));
                Log.d("Inside Button Click", "Title" + rs.getTitle() + "Ingredients" + rs.getIngredients() + "Thumbnail" + rs.getThumbnail());
        }*/
    }
}


