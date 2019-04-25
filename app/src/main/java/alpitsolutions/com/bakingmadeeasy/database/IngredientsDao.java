package alpitsolutions.com.bakingmadeeasy.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface IngredientsDao {
    @Query("SELECT * FROM ingredient WHERE recipeId=:recipeId ORDER BY id")
    LiveData<List<TbIngredientEntity>> loadAllIngredientsForRecipe(final Integer recipeId);

    @Insert
    void insert(TbIngredientEntity ingredientEntity);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateIngredient(TbIngredientEntity ingredientEntity);

    @Query("DELETE FROM ingredient WHERE id = :id")
    void deleteIngredientById(Integer id);

    @Query("DELETE FROM ingredient")
    void deleteAllIngredients();

    @Query("SELECT * FROM ingredient WHERE recipeId=:recipeId AND ingredient=:ingredient")
    LiveData<TbIngredientEntity> getIngredientByRecipeIdAndName(int recipeId, String ingredient);

}
