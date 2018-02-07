package com.simpragma.recipe.recipeapplication1;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class RecipeInformation extends AppCompatActivity {

    TextView textView_title,textView_ingradients,textView_href;
    ImageView imageView;
    String TAG=getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_information);
        textView_ingradients=(TextView) findViewById(R.id.textView_ingradients);
        textView_title=(TextView) findViewById(R.id.textView_title);
        imageView=(ImageView) findViewById(R.id.imageView);
        textView_href=(TextView)findViewById(R.id.textView_href);

        String passedArg = getIntent().getExtras().getString("image");
        if (passedArg.equals("") || passedArg.equals(null)) {
            Picasso.with(RecipeInformation.this).load(R.mipmap.ic_launcher).into(imageView);
        } else {
            Picasso.with(RecipeInformation.this).load(passedArg).error(R.mipmap.ic_launcher).into(imageView);
        }
        Log.d(TAG, getIntent().getExtras().getString("title"));
        Log.d(TAG, getIntent().getExtras().getString("ingredients"));
        textView_title.setText("Title:" + "\n" + getIntent().getExtras().getString("title").trim());
        textView_ingradients.setText("Ingredients:" + "\n" + getIntent().getExtras().getString("ingredients").trim());
        textView_href.setText(getIntent().getExtras().getString("href").toString().trim());

        textView_href.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse(getIntent().getExtras().getString("href").toString().trim()));
                startActivity(browserIntent);

            }
        });


    }
}
