package alpitsolutions.com.bakingmadeeasy.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

@Database(entities = {TbRecipeEntity.class, TbIngredientEntity.class, TbStepEntity.class}, version = 1, exportSchema = false)
public abstract class RecipeDatabase extends RoomDatabase {

    private static final String TAG = RecipeDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "recipes";
    private static RecipeDatabase sInstance;

    public static synchronized RecipeDatabase getInstance(Context context)
    {
        if (sInstance == null) {
            Log.d(TAG, "Creating new database instance");
            sInstance = Room.databaseBuilder(context,
                    RecipeDatabase.class, RecipeDatabase.DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        Log.d(TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract RecipesDao getRecipesDao();
    public abstract IngredientsDao getIngredientsDao();
    public abstract StepsDao getStepsDao();

}
