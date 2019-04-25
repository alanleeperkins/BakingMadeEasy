package alpitsolutions.com.bakingmadeeasy.repositories;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import alpitsolutions.com.bakingmadeeasy.database.IngredientsDao;
import alpitsolutions.com.bakingmadeeasy.database.RecipeDatabase;
import alpitsolutions.com.bakingmadeeasy.database.RecipesDao;
import alpitsolutions.com.bakingmadeeasy.database.StepsDao;
import alpitsolutions.com.bakingmadeeasy.database.TbIngredientEntity;
import alpitsolutions.com.bakingmadeeasy.database.TbRecipeEntity;
import alpitsolutions.com.bakingmadeeasy.database.TbStepEntity;
import alpitsolutions.com.bakingmadeeasy.interfaces.OnGetFavoriteEntryUpdateCallback;
import alpitsolutions.com.bakingmadeeasy.interfaces.OnGetFavoritesCallback;
import alpitsolutions.com.bakingmadeeasy.utility.Constants;

public class RecipesLocalRepository {
    private static final String TAG = Constants.TAG_FILTER + RecipesLocalRepository.class.getSimpleName();

    private static RecipesLocalRepository repository;

    public RecipesDao recipesDao;
    public IngredientsDao ingredientsDao;
    public StepsDao stepsDao;

    /**
     *
     * @param context
     */
    public RecipesLocalRepository(Context context) {
        RecipeDatabase database = RecipeDatabase.getInstance(context);
        recipesDao = database.getRecipesDao();
        ingredientsDao = database.getIngredientsDao();
        stepsDao = database.getStepsDao();
    }

    public void insert(TbRecipeEntity favorite, OnGetFavoriteEntryUpdateCallback listener) {
        new InsertLocalAsyncTask(recipesDao, ingredientsDao, stepsDao, listener).execute(favorite);
    }

    public void deleteByRecipeId(Integer recipeId, OnGetFavoriteEntryUpdateCallback listener) {
        new DeleteLocalAsyncTask(recipesDao,listener).execute(recipeId);
    }

    public void getListOfAllFavorites(OnGetFavoritesCallback listener) {
        new GetAllLocalAsyncTask(recipesDao, listener).execute();
    }

    /**
     *
     */
    private static class GetAllLocalAsyncTask extends AsyncTask<Void, Void, List<TbRecipeEntity>> {
        private RecipesDao recipesDao;

        OnGetFavoritesCallback listener;

        private GetAllLocalAsyncTask(RecipesDao recipesDao, OnGetFavoritesCallback listener) {
            this.recipesDao = recipesDao;
            this.listener = listener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected List<TbRecipeEntity> doInBackground(Void... voids) {

            List<TbRecipeEntity> locals = recipesDao.getListOfAllRecipes();

            return locals;
        }

        @Override
        protected void onPostExecute(List<TbRecipeEntity> tbRecipeEntities) {
            listener.onSuccess(tbRecipeEntities);
        }
    }

    /**
     *
     */
    private static class InsertLocalAsyncTask extends AsyncTask<TbRecipeEntity, Void, Void> {
        private RecipesDao recipesDao;
        private IngredientsDao ingredientsDao;
        private StepsDao stepsDao;

        OnGetFavoriteEntryUpdateCallback listener;

        private InsertLocalAsyncTask(RecipesDao recipesDao, IngredientsDao ingredientsDao, StepsDao stepsDao, OnGetFavoriteEntryUpdateCallback listener) {
            this.recipesDao = recipesDao;
            this.ingredientsDao = ingredientsDao;
            this.stepsDao = stepsDao;
            this.listener = listener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(TbRecipeEntity... notes) {

            recipesDao.insert(notes[0]);

            // ingredients
            for (TbIngredientEntity ingredientEntity : notes[0].getIngredients()) {
                ingredientsDao.insert(ingredientEntity);
            }

            // steps
            for (TbStepEntity stepEntity : notes[0].getSteps()) {
                stepsDao.insert(stepEntity);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            listener.addingFavoriteSuccessful();
        }
    }

    /**
     *
     */
    private static class DeleteLocalAsyncTask extends AsyncTask<Integer, Void, Void> {
        private RecipesDao recipesDao;

        OnGetFavoriteEntryUpdateCallback listener;

        private DeleteLocalAsyncTask(RecipesDao recipesDao, OnGetFavoriteEntryUpdateCallback listener) {
            this.recipesDao = recipesDao;
            this.listener = listener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Integer... recipeIds) {
            recipesDao.deleteRecipeByRecipeId(recipeIds[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            listener.deletingFavoriteSuccessful();
        }
    }

}
