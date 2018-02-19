package com.simpragma.recipe.roomDatabase;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.util.Log;

import java.util.List;

/**
 * Created by Madvesha on 2/12/18.
 */
@Dao
public interface RecipeDao {

    //Retrieving all the data from RoomDB
    @Query("SELECT * FROM reciperoomdb")
    List<RecipeRoomDB> getAll();

    //Inserting Values to RoomDB
    @Insert
    void insertAll(RecipeRoomDB... recipe);

    //Deleting Values from RoomDB
    @Query("DELETE FROM reciperoomdb")
    public void deleteRecipe();
}
