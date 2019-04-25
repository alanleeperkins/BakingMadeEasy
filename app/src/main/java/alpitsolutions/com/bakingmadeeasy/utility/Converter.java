package alpitsolutions.com.bakingmadeeasy.utility;

import java.util.ArrayList;
import java.util.List;

import alpitsolutions.com.bakingmadeeasy.database.TbIngredientEntity;
import alpitsolutions.com.bakingmadeeasy.database.TbRecipeEntity;
import alpitsolutions.com.bakingmadeeasy.database.TbStepEntity;
import alpitsolutions.com.bakingmadeeasy.models.RecipeEntity;
import alpitsolutions.com.bakingmadeeasy.models.RecipeIngredientEntity;
import alpitsolutions.com.bakingmadeeasy.models.RecipeStepEntity;

public class Converter {

    /**
     *
     * @param recipeEntityList
     * @return
     */
    public static List<TbRecipeEntity> ToListOfTbRecipeEntity(List<RecipeEntity> recipeEntityList) {
        List<TbRecipeEntity> conv = new ArrayList<>();

        for (RecipeEntity single: recipeEntityList) {
            conv.add(ToTbRecipeEntity(single));
        }
        return conv;
    }

    /**
     * single local recipe
     * @param recipeEntity
     * @return
     */
    public static TbRecipeEntity ToTbRecipeEntity(RecipeEntity recipeEntity) {
        TbRecipeEntity conv = new TbRecipeEntity(recipeEntity.getId(),
                                                 recipeEntity.getName(),
                                                 recipeEntity.getServings(),
                                                 recipeEntity.getImage());

        // convert the ingredients
        List<TbIngredientEntity> ingredients = ToListOfTbIngredientEntity(recipeEntity.getIngredients(),recipeEntity.getId());
        conv.setIngredients(ingredients);

        // convert the steps
        List<TbStepEntity> steps = ToListOfTbStepEntity(recipeEntity.getSteps(),recipeEntity.getId());
        conv.setSteps(steps);

        return conv;
    }

    /**
     * list of local recipe ingredients
     * @param recipeIngredientEntityList
     * @param recipeId
     * @return
     */
    public static List<TbIngredientEntity> ToListOfTbIngredientEntity(List<RecipeIngredientEntity> recipeIngredientEntityList, Integer recipeId) {

        List<TbIngredientEntity> conv = new ArrayList<>();

        for (RecipeIngredientEntity ingr: recipeIngredientEntityList) {
            conv.add(ToTbIngredientEntity(ingr,recipeId ));
        }

        return conv;
    }

    /**
     * single local recipe ingredient
     * @param recipeIngredientEntity
     * @param recipeId
     * @return
     */
    public static TbIngredientEntity ToTbIngredientEntity(RecipeIngredientEntity recipeIngredientEntity, Integer recipeId) {

        TbIngredientEntity tbIngr = new TbIngredientEntity(recipeIngredientEntity.getIngredient());
        tbIngr.setMeasure(recipeIngredientEntity.getMeasure());
        tbIngr.setQuantity(recipeIngredientEntity.getQuantity());
        tbIngr.setRecipeId(recipeId);

        return tbIngr;
    }

    /**
     * list of local recipe steps
     * @param recipeStepEntityList
     * @param recipeId
     * @return
     */
    public static List<TbStepEntity> ToListOfTbStepEntity(List<RecipeStepEntity> recipeStepEntityList, Integer recipeId) {

        List<TbStepEntity> conv = new ArrayList<>();

        for (RecipeStepEntity step : recipeStepEntityList) {
            conv.add(ToTbStepEntity(step, recipeId));
        }

        return conv;
    }

    /**
     * single local recipe step
     * @param recipeStepEntity
     * @param recipeId
     * @return
     */
    public static TbStepEntity ToTbStepEntity(RecipeStepEntity recipeStepEntity, Integer recipeId) {
        TbStepEntity tbStepEntity = new TbStepEntity(recipeStepEntity.getId(),
                                                     recipeId,
                                                     recipeStepEntity.getShortDescription(),
                                                     recipeStepEntity.getDescription(),
                                                     recipeStepEntity.getVideoUrl(),
                                                     recipeStepEntity.getThumbnailUrl());
        return tbStepEntity;
    }



    /**
     * list of remote recipe ingredient
     * @param tbIngredientEntityList
     * @return
     */
    public static List<RecipeIngredientEntity> ToListOfRecipeIngredientEntity(List<TbIngredientEntity> tbIngredientEntityList) {

        List<RecipeIngredientEntity> conv = new ArrayList<>();

        for (TbIngredientEntity ingr: tbIngredientEntityList) {
            conv.add(ToRecipeIngredientEntity(ingr));
        }

        return conv;
    }
    /**
     * single remote recipe ingredient
     * @param tbIngredientEntity
     * @return
     */
    public static RecipeIngredientEntity ToRecipeIngredientEntity(TbIngredientEntity  tbIngredientEntity) {

        RecipeIngredientEntity ingr = new RecipeIngredientEntity(tbIngredientEntity.getQuantity(),tbIngredientEntity.getMeasure(),tbIngredientEntity.getIngredient());
        return ingr;
    }
}
