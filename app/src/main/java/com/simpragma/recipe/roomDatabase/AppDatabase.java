package com.simpragma.recipe.roomDatabase;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by Madvesha on 2/12/18.
 */
@Database(entities = {RecipeRoomEntityClass.class}, version = 3)

public abstract class AppDatabase extends RoomDatabase {

    public abstract RecipeDao recipeDao();

}
