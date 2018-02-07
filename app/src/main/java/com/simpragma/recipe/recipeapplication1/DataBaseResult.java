package com.simpragma.recipe.recipeapplication1;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by shubhank on 2/4/18.
 */

public class DataBaseResult {


    private String title;
    private String href;
    private String ingredients;
    private String thumbnail;
    int id;

    public DataBaseResult() {

    }

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

 // constructor
    public DataBaseResult(int id, String title, String ingredients,String href,String thumbnail){
        this.id = id;
        this.title = title;
        this.ingredients = ingredients;
        this.href=href;
        this.thumbnail=thumbnail;
    }

    // constructor
    public DataBaseResult(String title, String ingredients,String href,String thumbnail){
        this.thumbnail = title;
        this.ingredients = ingredients;
        this.href=href;
        this.thumbnail=thumbnail;
    }


}
