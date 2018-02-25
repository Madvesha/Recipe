package com.simpragma.recipe.api;


import com.simpragma.recipe.pojoclass.Recipe;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Pagination
 * Created by Madvesha.
 */

public interface RecipeService {

    @GET("/api/")
    Call<Recipe> getRecipe(
            @Query("i") String apiKey,
            @Query("p") int pageIndex
    );

}
