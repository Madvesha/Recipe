
package com.simpragma.recipe.pojoclass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecipetList {

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
    public RecipetList() {

    }

    // constructor
    public RecipetList(int id, String title, String ingredients, String href, String thumbnail) {
        this.id = id;
        this.title = title;
        this.ingredients = ingredients;
        this.href = href;
        this.thumbnail = thumbnail;
    }

    // constructor
    public RecipetList(String title, String ingredients, String href, String thumbnail) {
        this.thumbnail = title;
        this.ingredients = ingredients;
        this.href = href;
        this.thumbnail = thumbnail;
    }


}
