package alpitsolutions.com.bakingmadeeasy.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface RecipesDao {

    @Query("SELECT COUNT(*) FROM recipe")
    int getCount();

    @Query("SELECT * FROM recipe ORDER BY id")
    LiveData<List<TbRecipeEntity>> loadAllRecipes();

    @Query("SELECT * FROM recipe ORDER BY id")
    List<TbRecipeEntity> getListOfAllRecipes();

    @Insert
    void insert(TbRecipeEntity recipesEntity);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateRecipe(TbRecipeEntity recipesEntity);

    @Query("DELETE FROM recipe WHERE recipeId = :recipeId")
    void deleteRecipeByRecipeId(int recipeId);

    @Query("DELETE FROM recipe")
    void deleteAllRecipes();

    @Query("SELECT * FROM recipe WHERE recipeId=:recipeId")
    LiveData<TbRecipeEntity> getRecipeByRecipeId(int recipeId);
}
