package alpitsolutions.com.bakingmadeeasy;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import alpitsolutions.com.bakingmadeeasy.database.TbIngredientEntity;
import alpitsolutions.com.bakingmadeeasy.database.TbRecipeEntity;
import alpitsolutions.com.bakingmadeeasy.database.TbStepEntity;
import alpitsolutions.com.bakingmadeeasy.models.RecipeIngredientEntity;
import alpitsolutions.com.bakingmadeeasy.models.RecipeStepEntity;
import alpitsolutions.com.bakingmadeeasy.models.RecipeEntity;
import alpitsolutions.com.bakingmadeeasy.utility.Converter;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ConverterTest {

    Integer MAX_STEPS = 5;
    Integer MAX_RECIPES = 5;
    Integer MAX_INGREDIENTS = 5;

    Integer INGREDIENT_RECIPE_ID = 1;
    Integer STEP_RECIPE_ID = 1;
    Integer STEP_ENTITY_ID = 0;

    Integer [] INGREDIENT_IDS = new Integer[] {1, 3, 5, 7, 9};
    String  [] INGREDIENT_NAMES = new String [] { "Graham Cracker crumbs", "unsalted butter, melted", "cream cheese(softened)", "granulated sugar", "salt" };
    String  []  INGREDIENT_MEASURES = new String[] {"G", "CUP", "K", "OZ", "TBLSP"};
    Double [] INGREDIENT_QUANTITIES = new Double[] {1.2, 3.4, 5.6, 7.8, 9.0};

    Integer [] STEP_IDS = new Integer[] { 1, 2, 3, 4, 5 };
    String  [] STEP_SHORT_DESCRIPTIONS = new String [] { "Do this", "Do that", "Do those", "Do these", "Thou shalt do these!" };
    String  []  STEP_DESCRIPTIONS = new String[] {"Long Description 1", "Long Description 2", "Long Description 3", "Long Description 4", "Long Description 5"};
    String  []  STEP_VIDEO_URLS = new String[] {"Video URL1", "Video URL2", "Video URL3", "Video URL4", "Video URL5"};
    String  []  STEP_THUMBNAIL_URLS = new String[] {"Thumbnail URL1", "Thumbnail URL2", "Thumbnail URL3", "Thumbnail URL4", "Thumbnail URL5"};

    Integer [] RECIPE_IDS = new Integer[] { 21, 22, 23, 24, 25 };
    String  [] RECIPE_NAMES = new String [] { "RecipeNumber1", "RecipeNumber2", "RecipeNumber3", "RecipeNumber4", "RecipeNumber5" };
    Integer  [] RECIPE_SERVINGS = new Integer [] { 23, 34, 42, 1, 23 };
    String  [] RECIPE_IMAGES = new String [] { "Image1", "", "", "Image4", "Image5" };

    /**
     * returns a list of remote reipe data
     * @return
     */
    private List<RecipeEntity> getListOfRecipeEntity() {

        List<RecipeEntity> list_recipe_remote = new ArrayList<>();

        for (int i=0; i<MAX_RECIPES; i++) {
            RecipeEntity recipe_remote = new RecipeEntity(RECIPE_IDS[i], RECIPE_NAMES[i], RECIPE_SERVINGS[i], RECIPE_IMAGES[i], getListOfRecipeIngredientEntity(), getListOfRecipeStepEntity());
            list_recipe_remote.add(recipe_remote);
        }

        return list_recipe_remote;
    }

    /**
     * return a list of remote recipe ingredient data
     * @return
     */
    private List<RecipeIngredientEntity> getListOfRecipeIngredientEntity() {
        // create a list of remote recipe data
        List<RecipeIngredientEntity> list_ingredient_remote = new ArrayList<>();
        for (int i=0; i<MAX_INGREDIENTS; i++) {
            RecipeIngredientEntity ingredient_remote = new RecipeIngredientEntity(INGREDIENT_QUANTITIES[i], INGREDIENT_MEASURES[i], INGREDIENT_NAMES[i]);
            list_ingredient_remote.add(ingredient_remote);
        }
        return list_ingredient_remote;
    }

    /**
     * return a list of remote recipe step data
     * @return
     */
    private List<RecipeStepEntity> getListOfRecipeStepEntity() {
        List<RecipeStepEntity> list_step_remote = new ArrayList<>();
        for (int i=0; i<MAX_STEPS; i++) {
            RecipeStepEntity step_remote = new RecipeStepEntity(STEP_IDS[i], STEP_SHORT_DESCRIPTIONS[i], STEP_DESCRIPTIONS[i], STEP_VIDEO_URLS[i],STEP_THUMBNAIL_URLS[i]);
            list_step_remote.add(step_remote);
        }

        return list_step_remote;
    }

    /**
     *
     */
    @Test
    public void convertRemoteToLocal_ListOfRecipe() {

        List<RecipeEntity> list_recipe_remote = getListOfRecipeEntity();
        Assert.assertNotNull(list_recipe_remote);
        assertEquals((int)MAX_RECIPES, list_recipe_remote.size());

        List<TbRecipeEntity> list_recipe_local = Converter.ToListOfTbRecipeEntity(list_recipe_remote);

        for (int i=0; i<MAX_RECIPES; i++) {
            assertEquals((Integer)RECIPE_IDS[i], list_recipe_local.get(i).getRecipeId());
            assertEquals(RECIPE_NAMES[i], list_recipe_local.get(i).getName());
            assertEquals((Integer)RECIPE_SERVINGS[i], list_recipe_local.get(i).getServings());
            assertEquals(RECIPE_IMAGES[i], list_recipe_local.get(i).getImage());
            assertEquals((int)MAX_INGREDIENTS, list_recipe_local.get(i).getIngredients().size());
            assertEquals((int)MAX_STEPS, list_recipe_local.get(i).getSteps().size());
        }
    }

    /**
     *
     */
    @Test
    public void convertRemoteToLocal_Recipe() {

        // create a remote (raw)
        RecipeEntity recipe_remote = new RecipeEntity(RECIPE_IDS[0], RECIPE_NAMES[0], RECIPE_SERVINGS[0], RECIPE_IMAGES[0], getListOfRecipeIngredientEntity(), getListOfRecipeStepEntity());

        TbRecipeEntity tbRecipeEntity = Converter.ToTbRecipeEntity(recipe_remote);
        Assert.assertNull(tbRecipeEntity.getId());
        assertEquals((Integer)RECIPE_IDS[0], tbRecipeEntity.getRecipeId());
        assertEquals(RECIPE_NAMES[0], tbRecipeEntity.getName());
        assertEquals((Integer)RECIPE_SERVINGS[0], tbRecipeEntity.getServings());
        assertEquals(RECIPE_IMAGES[0], tbRecipeEntity.getImage());
        assertEquals((int)MAX_INGREDIENTS, tbRecipeEntity.getIngredients().size());
        assertEquals((int)MAX_STEPS, tbRecipeEntity.getSteps().size());
    }

    /**
     *
     */
    @Test
    public void convertRemoteToLocal_ListOfStep() {

        // create a list of remote recipe data
        List<RecipeStepEntity> list_step_remote = getListOfRecipeStepEntity();

        // convert it to local (database)
        List<TbStepEntity> list_step_local = Converter.ToListOfTbStepEntity(list_step_remote,STEP_RECIPE_ID);
        Assert.assertNotNull(list_step_local);

        // check the result
        for (int i=0; i<MAX_STEPS; i++) {
            // check the local data
            assertEquals((Integer)STEP_RECIPE_ID, list_step_local.get(i).getRecipeId());
            assertEquals((Integer)STEP_IDS[i], list_step_local.get(i).getStepId());
            assertEquals(STEP_SHORT_DESCRIPTIONS[i], list_step_local.get(i).getShortDescription());
            assertEquals(STEP_DESCRIPTIONS[i], list_step_local.get(i).getDescription());
            assertEquals(STEP_VIDEO_URLS[i], list_step_local.get(i).getVideoURL());
            assertEquals(STEP_THUMBNAIL_URLS[i], list_step_local.get(i).getThumbnailURL());
        }
    }

    /**
     *
     */
    @Test
    public void convertRemoteToLocal_Step() {
        // create a remote (raw)
        RecipeStepEntity step_remote = new RecipeStepEntity(STEP_IDS[0],STEP_SHORT_DESCRIPTIONS[0],STEP_DESCRIPTIONS[0],STEP_VIDEO_URLS[0],STEP_THUMBNAIL_URLS[0]);

        // convert it to local (database)
        TbStepEntity step_local = Converter.ToTbStepEntity(step_remote,STEP_RECIPE_ID);

        // check the local data
        Assert.assertNull(step_local.getId());
        assertEquals((Integer)STEP_IDS[0], step_local.getStepId());
        assertEquals((Integer)STEP_RECIPE_ID, step_local.getRecipeId());
        assertEquals(STEP_SHORT_DESCRIPTIONS[0], step_local.getShortDescription());
        assertEquals(STEP_DESCRIPTIONS[0], step_local.getDescription());
        assertEquals(STEP_VIDEO_URLS[0], step_local.getVideoURL());
        assertEquals(STEP_THUMBNAIL_URLS[0], step_local.getThumbnailURL());
    }

    /**
     *
     */
    @Test
    public void convertRemoteToLocal_ListOfIngredient() {
        // create a list of remote recipe data
        List<RecipeIngredientEntity> list_ingredient_remote = getListOfRecipeIngredientEntity();

        // convert it to local (database)
        List<TbIngredientEntity> list_ingredient_local = Converter.ToListOfTbIngredientEntity(list_ingredient_remote,INGREDIENT_RECIPE_ID);
        Assert.assertNotNull(list_ingredient_local);

        // check the result
        for (int i=0; i<MAX_INGREDIENTS; i++) {
            // check the local data
            assertEquals((Integer)INGREDIENT_RECIPE_ID, list_ingredient_local.get(i).getRecipeId());
            assertEquals(INGREDIENT_QUANTITIES[i], list_ingredient_local.get(i).getQuantity());
            assertEquals(INGREDIENT_MEASURES[i], list_ingredient_local.get(i).getMeasure());
            assertEquals(INGREDIENT_NAMES[i], list_ingredient_local.get(i).getIngredient());
        }
    }

    /**
     *
     */
    @Test
    public void convertRemoteToLocal_Ingredient() {
        // create a remote (raw)
        RecipeIngredientEntity ingredient_remote = new RecipeIngredientEntity(INGREDIENT_QUANTITIES[0], INGREDIENT_MEASURES[0], INGREDIENT_NAMES[0]);

        // convert it to local (database)
        TbIngredientEntity ingredient_local = Converter.ToTbIngredientEntity(ingredient_remote,INGREDIENT_RECIPE_ID);

        // check the local data
        assertEquals((Integer)INGREDIENT_RECIPE_ID, ingredient_local.getRecipeId());
        assertEquals(INGREDIENT_QUANTITIES[0], ingredient_local.getQuantity());
        assertEquals(INGREDIENT_MEASURES[0], ingredient_local.getMeasure());
        assertEquals(INGREDIENT_NAMES[0], ingredient_local.getIngredient());
    }

    /**
     *
     */
    @Test
    public  void convertLocalToRemote_Ingredient() {
        // create a local (database)
        TbIngredientEntity ingredient_local = new TbIngredientEntity(INGREDIENT_IDS[0], INGREDIENT_RECIPE_ID, INGREDIENT_QUANTITIES[0], INGREDIENT_MEASURES[0], INGREDIENT_NAMES[0]);

        // convert it to remote (raw)
        RecipeIngredientEntity ingredient_remote = Converter.ToRecipeIngredientEntity(ingredient_local);

        assertEquals(INGREDIENT_QUANTITIES[0] , ingredient_remote.getQuantity());
        assertEquals(INGREDIENT_MEASURES[0] , ingredient_remote.getMeasure());
        assertEquals(INGREDIENT_NAMES[0] , ingredient_remote.getIngredient());
    }

    /**
     *
     */
    @Test
    public  void convertLocalToRemote_ListOfIngredient() {

        // create a list of local recipe data
        List<TbIngredientEntity> list_ingredient_local = new ArrayList<>();
        for (int i=0; i<MAX_INGREDIENTS; i++) {
            TbIngredientEntity ingredient_local = new TbIngredientEntity(INGREDIENT_IDS[i],INGREDIENT_RECIPE_ID,INGREDIENT_QUANTITIES[i],INGREDIENT_MEASURES[i],INGREDIENT_NAMES[i]);
            list_ingredient_local.add(ingredient_local);
        }

        // convert it to remote (raw)
        List<RecipeIngredientEntity> list_ingredient_remote = Converter.ToListOfRecipeIngredientEntity(list_ingredient_local);

        // check the result
        for (int i=0; i<MAX_INGREDIENTS; i++) {
            // check the local data
            assertEquals(INGREDIENT_QUANTITIES[i], list_ingredient_remote.get(i).getQuantity());
            assertEquals(INGREDIENT_MEASURES[i], list_ingredient_remote.get(i).getMeasure());
            assertEquals(INGREDIENT_NAMES[i], list_ingredient_remote.get(i).getIngredient());
        }
    }
}
