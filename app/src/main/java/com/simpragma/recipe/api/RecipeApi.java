package com.simpragma.recipe.api;

import com.simpragma.recipe.recipeapp.R;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Pagination
 * Created by Madvesha.
 */

public class RecipeApi {

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("http://www.recipepuppy.com/")
                    .build();
        }
        return retrofit;
    }

}
