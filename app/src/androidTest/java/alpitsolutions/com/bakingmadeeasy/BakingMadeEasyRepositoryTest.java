package alpitsolutions.com.bakingmadeeasy;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import alpitsolutions.com.bakingmadeeasy.database.IngredientsDao;
import alpitsolutions.com.bakingmadeeasy.database.RecipeDatabase;
import alpitsolutions.com.bakingmadeeasy.database.RecipesDao;
import alpitsolutions.com.bakingmadeeasy.database.StepsDao;
import alpitsolutions.com.bakingmadeeasy.database.TbIngredientEntity;
import alpitsolutions.com.bakingmadeeasy.database.TbRecipeEntity;
import alpitsolutions.com.bakingmadeeasy.database.TbStepEntity;
import alpitsolutions.com.bakingmadeeasy.utility.LiveDataTestUtil;


import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(AndroidJUnit4.class)
public class BakingMadeEasyRepositoryTest {

    private RecipesDao recipesDao;
    private IngredientsDao ingredientsDao;
    private StepsDao stepsDao;

    private RecipeDatabase recipeDatabase;

    /**
     *
     */
    @Before
    public void createDatabase() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        recipeDatabase = Room.inMemoryDatabaseBuilder(appContext, RecipeDatabase.class).allowMainThreadQueries().build();

        recipesDao = recipeDatabase.getRecipesDao();
        ingredientsDao = recipeDatabase.getIngredientsDao();
        stepsDao = recipeDatabase.getStepsDao();
    }

    /**
     *
     * @throws IOException
     */
    @After
    public void closeDatabase() throws IOException {
        recipeDatabase.close();
    }

    /***
     *
     * @throws Exception
     */
    @Test
    public void writeAndReadAndRemoveRecipe() throws Exception {
        Integer recipeId = 101;
        writeAndReadRecipeInList(recipeId);

        writeAndReadRecipeIngredientsInList(recipeId);
        writeAndReadRecipeStepsInList(recipeId);

        deleteRecipeData_Cascading(recipeId);
    }

    /**
     *
     * @param recipeId
     * @throws Exception
     */
    private void writeAndReadRecipeInList(Integer recipeId) throws Exception{
        String recipeName = "Tests1";
        String recipeImage = "DummyImage.jpg";
        int recipeServings = 10;

        // create and insert recipe
        TbRecipeEntity recipeEntity = new TbRecipeEntity(recipeId, recipeName, recipeServings,recipeImage );
        recipesDao.insert(recipeEntity);

        // check the content count
        List<TbRecipeEntity> recipes = LiveDataTestUtil.getValue(recipesDao.loadAllRecipes());
        assertNotNull(recipes);
        assertNotEquals(0, recipes.size());

        // check the included recipe
        TbRecipeEntity resultData = LiveDataTestUtil.getValue(recipesDao.getRecipeByRecipeId(recipeId));
        assertNotNull(resultData);

        assertEquals((Integer)recipeId, resultData.getRecipeId());
        assertEquals((Integer)recipeServings, resultData.getServings());
        assertEquals(recipeName, resultData.getName());
        assertEquals(recipeImage, resultData.getImage());
    }

    /**
     *
     * @param recipeId
     * @throws Exception
     */
    private void writeAndReadRecipeIngredientsInList(Integer recipeId) throws Exception{
        String ingredientName = "Ing1";
        String ingredientMeasure = "TSB";
        Double ingredientQuantity = 10.0;

        TbIngredientEntity ingredientEntity = new TbIngredientEntity(ingredientName);
        ingredientEntity.setRecipeId(recipeId);
        ingredientEntity.setIngredient(ingredientName);
        ingredientEntity.setMeasure(ingredientMeasure);
        ingredientEntity.setQuantity(ingredientQuantity);
        ingredientsDao.insert(ingredientEntity);

        // check the content count
        List<TbIngredientEntity> ingredientEntities = LiveDataTestUtil.getValue(ingredientsDao.loadAllIngredientsForRecipe(recipeId));
        assertNotNull(ingredientEntities);
        assertEquals(1,ingredientEntities.size());

        // check the included ingredient
        TbIngredientEntity resultData = LiveDataTestUtil.getValue(ingredientsDao.getIngredientByRecipeIdAndName(recipeId, ingredientName));
        assertNotNull(resultData);

        assertEquals((Integer)1 , resultData.getId());
        assertEquals((Integer)recipeId , resultData.getRecipeId());
        assertEquals(ingredientName , resultData.getIngredient());
        assertEquals(ingredientMeasure , resultData.getMeasure());
        assertEquals(ingredientQuantity , resultData.getQuantity());
    }

    /***
     *
     * @param recipeId
     * @throws Exception
     */
    private void writeAndReadRecipeStepsInList(Integer recipeId) throws Exception{
        Integer stepId = 1000;
        String stepShortDescription = "Short Description 1";
        String stepDescription = "Full Description 1";
        String stepVideoURL = "whatever.mpg";
        String stepThumbnailURL = "whatever.jpg";

        TbStepEntity stepEntity = new TbStepEntity(stepId, recipeId,stepShortDescription,stepDescription,stepVideoURL,stepThumbnailURL);
        stepsDao.insert(stepEntity);

        // check the content count
        List<TbStepEntity> stepEntities = LiveDataTestUtil.getValue(stepsDao.loadAllStepsForRecipe(recipeId));
        assertNotNull(stepEntities);
        assertEquals(1,stepEntities.size());

        // check the included step
        TbStepEntity resultData = LiveDataTestUtil.getValue(stepsDao.getStepByStepId(stepId));
        assertNotNull(resultData);

        assertEquals((Integer)1 , resultData.getId());
        assertEquals((Integer)recipeId , resultData.getRecipeId());
        assertEquals((Integer)stepId , resultData.getStepId());
        assertEquals(stepVideoURL , resultData.getVideoURL());
        assertEquals(stepThumbnailURL , resultData.getThumbnailURL());
        assertEquals(stepShortDescription , resultData.getShortDescription());
        assertEquals(stepDescription , resultData.getDescription());
    }

    /**
     *
     * @param recipeId
     * @throws Exception
     */
    private void deleteRecipeData_Cascading(Integer recipeId) throws Exception{

        // cascading removal of a recipe and its ingredients, steps
        TbRecipeEntity resultData = LiveDataTestUtil.getValue(recipesDao.getRecipeByRecipeId(recipeId));
        assertNotNull(resultData);

        recipesDao.deleteRecipeByRecipeId(recipeId);

        TbRecipeEntity resultDataRecipe = LiveDataTestUtil.getValue(recipesDao.getRecipeByRecipeId(recipeId));
        Assert.assertNull (resultDataRecipe);

        List<TbRecipeEntity> recipeEntities = LiveDataTestUtil.getValue(recipesDao.loadAllRecipes());
        assertEquals(0,recipeEntities.size());

        List<TbStepEntity> stepEntities = LiveDataTestUtil.getValue(stepsDao.loadAllStepsForRecipe(recipeId));
        //Assert.assertNull(stepEntities);
        assertEquals(0,stepEntities.size());

        List<TbIngredientEntity> ingredientEntities = LiveDataTestUtil.getValue(ingredientsDao.loadAllIngredientsForRecipe(recipeId));
        //Assert.assertNull(ingredientEntities);
        assertEquals(0,ingredientEntities.size());
    }

}
