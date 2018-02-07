
package com.simpragma.recipe.recipeapplication1;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("href")
    @Expose
    private String href;
    @SerializedName("ingredients")
    @Expose
    private String ingredients;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;

    int id;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // Empty constructor
    public Result(){

    }
    // constructor
    public Result(int id, String title, String ingredients,String thumbnail){
        this.id = id;
        this.title = title;
        this.ingredients = ingredients;
        this.thumbnail=thumbnail;
    }

    // constructor
    public Result(String title, String ingredients,String thumbnail){
        this.thumbnail = title;
        this.ingredients = ingredients;
        this.thumbnail=thumbnail;
    }


}
