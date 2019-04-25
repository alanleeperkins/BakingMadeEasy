package alpitsolutions.com.bakingmadeeasy.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface StepsDao {

    @Query("SELECT * FROM step WHERE recipeId=:recipeId ORDER BY id")
    LiveData<List<TbStepEntity>> loadAllStepsForRecipe(final Integer recipeId);

    @Insert
    void insert(TbStepEntity stepEntity);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateStep(TbStepEntity stepEntity);

    @Query("DELETE FROM step WHERE stepId = :stepId")
    void deleteStepByStepId(int stepId);

    @Query("DELETE FROM step")
    void deleteAllSteps();

    @Query("SELECT * FROM step WHERE stepId = :stepId")
    LiveData<TbStepEntity> getStepByStepId(int stepId);

}
