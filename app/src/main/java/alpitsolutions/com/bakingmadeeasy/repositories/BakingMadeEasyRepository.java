package alpitsolutions.com.bakingmadeeasy.repositories;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import alpitsolutions.com.bakingmadeeasy.database.TbRecipeEntity;
import alpitsolutions.com.bakingmadeeasy.interfaces.OnGetFavoriteEntryUpdateCallback;
import alpitsolutions.com.bakingmadeeasy.interfaces.OnGetFavoritesCallback;
import alpitsolutions.com.bakingmadeeasy.interfaces.OnGetRecipeCallback;
import alpitsolutions.com.bakingmadeeasy.interfaces.OnGetRecipeStepCallback;
import alpitsolutions.com.bakingmadeeasy.interfaces.OnGetRecipesCallback;
import alpitsolutions.com.bakingmadeeasy.utility.Constants;

public class BakingMadeEasyRepository {

    private static final String sTAG = Constants.sTAG_FILTER + BakingMadeEasyRepository.class.getSimpleName();

    public static final String sFILTER_TYPE_NONE = "filter_none";
    public static final String sFILTER_TYPE_GLOBAL = "filter_global";
    public static final String sFILTER_TYPE_LOCAL = "filter_local";

    public static BakingMadeEasyRepository sInstance;

    public RecipesRemoteRepository mRemoteRepository;
    public RecipesLocalRepository mLocalRepository;

    /**
     * returns a new object of our BakingMadeEasy Repository for remote and local data access
     *
     * @param context
     */
    private BakingMadeEasyRepository(Context context) {
        mRemoteRepository = RecipesRemoteRepository.getInstance();
        mLocalRepository = new RecipesLocalRepository(context);
    }

    /**
     * returns a new object of our BakingMadeEasy Repository for remote and local data access
     *
     * @param context
     * @return
     */
    public static synchronized BakingMadeEasyRepository getInstance(Context context) {
        if (sInstance == null) {
            Log.d(sTAG, "NEW INSTANCE OF BakingMadeEasyRepository");
            sInstance = new BakingMadeEasyRepository(context);
        } else {
            Log.d(sTAG, "GRAB EXISTENT INSTANCE OF BakingMadeEasyRepository");
        }
        return sInstance;
    }

    public void getRecipeData(int recipeId, final OnGetRecipeCallback recipeCallback) {
        mRemoteRepository.getRecipeData(recipeId, recipeCallback);
    }

    public void getRecipeStep(@NonNull Integer recipeId, @NonNull Integer recipeStepId, final OnGetRecipeStepCallback recipeStepCallback) {
        mRemoteRepository.getRecipeStep(recipeId,recipeStepId, recipeStepCallback );
    }

    public void getAllRecipes(final OnGetRecipesCallback recipeCallback) {
        mRemoteRepository.getAllRecipes(recipeCallback);
    }

    /**
     *
     * @param favorite
     * @param listener
     */
    public void addAsFavorite(TbRecipeEntity favorite, OnGetFavoriteEntryUpdateCallback listener) {
        mLocalRepository.insert(favorite, listener);
    }

    /**
     *
     * @param recipeId
     * @param listener
     */
    public void removeAsFavoriteByRecipeId(Integer recipeId, OnGetFavoriteEntryUpdateCallback listener) {
        mLocalRepository.deleteByRecipeId(recipeId, listener);
    }

    /**
     *
     * @param listener
     */
    public void getAllFavorites(final OnGetFavoritesCallback listener) {
        mLocalRepository.getListOfAllFavorites(listener);
    }

}
