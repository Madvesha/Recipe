package com.simpragma.recipe.roomDatabase;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Madvesha on 2/12/18.
 * RecipeRoom Pojo class and here implement getter and setter methods
 * implement constructor
 */
@Entity (tableName = "recipetable")
public class RecipeRoomEntityClass {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "ingredients")
    private String ingredients;

    @ColumnInfo(name = "href")
    private String href;

    @ColumnInfo(name = "thumbnail")
    private String thumbnail;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public RecipeRoomEntityClass(int id, String title, String ingredients, String href, String thumbnail) {
        this.id = id;
        this.title = title;
        this.ingredients = ingredients;
        this.href = href;
        this.thumbnail = thumbnail;
    }
}
