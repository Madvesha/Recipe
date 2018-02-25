package com.simpragma.recipe.roomDatabase;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * Created by Madvesha on 2/12/18.
 */
@Dao
public interface RecipeDao {

    //Retrieving all the data from RoomORM
    @Query("SELECT * FROM recipetable")
    public List<RecipeRoomEntityClass> getAll();

    //Inserting Values to RoomDB
    @Insert
    void insertAll(RecipeRoomEntityClass... recipetable);

    //Deleting Values from RoomDB
    @Query("DELETE FROM recipetable")
    public void deleteRecipe();
}
