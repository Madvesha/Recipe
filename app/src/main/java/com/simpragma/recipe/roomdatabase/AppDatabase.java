package com.simpragma.recipe.roomdatabase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by Madvesha on 2/12/18.
 */
@Database(entities = {RecipeRoomDB.class}, version = 2)

public abstract class AppDatabase extends RoomDatabase {

    public abstract RecipeDao recipeDao();

}
