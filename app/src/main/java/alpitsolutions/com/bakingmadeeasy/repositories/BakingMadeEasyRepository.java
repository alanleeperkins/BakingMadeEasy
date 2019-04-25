package alpitsolutions.com.bakingmadeeasy.repositories;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import alpitsolutions.com.bakingmadeeasy.database.TbRecipeEntity;
import alpitsolutions.com.bakingmadeeasy.interfaces.OnGetFavoriteEntryUpdateCallback;
import alpitsolutions.com.bakingmadeeasy.interfaces.OnGetFavoritesCallback;
import alpitsolutions.com.bakingmadeeasy.interfaces.OnGetRecipeCallback;
import alpitsolutions.com.bakingmadeeasy.interfaces.OnGetRecipeStepCallback;
import alpitsolutions.com.bakingmadeeasy.interfaces.OnGetRecipesCallback;
import alpitsolutions.com.bakingmadeeasy.utility.Constants;

public class BakingMadeEasyRepository {

    private static final String TAG = Constants.TAG_FILTER + BakingMadeEasyRepository.class.getSimpleName();

    public static final String FILTER_TYPE_NONE = "filter_none";
    public static final String FILTER_TYPE_GLOBAL = "filter_global";
    public static final String FILTER_TYPE_LOCAL = "filter_local";

    public static BakingMadeEasyRepository sInstance;

    public RecipesRemoteRepository remoteRepository;
    public RecipesLocalRepository localRepository;

    /**
     * returns a new object of our BakingMadeEasy Repository for remote and local data access
     *
     * @param context
     */
    private BakingMadeEasyRepository(Context context) {
        remoteRepository = RecipesRemoteRepository.getInstance();
        localRepository = new RecipesLocalRepository(context);
    }

    /**
     * returns a new object of our BakingMadeEasy Repository for remote and local data access
     *
     * @param context
     * @return
     */
    public static synchronized BakingMadeEasyRepository getInstance(Context context) {
        if (sInstance == null) {
            Log.d(TAG, "NEW INSTANCE OF BakingMadeEasyRepository");
            sInstance = new BakingMadeEasyRepository(context);
        } else {
            Log.d(TAG, "GRAB EXISTENT INSTANCE OF BakingMadeEasyRepository");
        }
        return sInstance;
    }

    public void getRecipeData(int recipeId, final OnGetRecipeCallback recipeCallback) {
        remoteRepository.getRecipeData(recipeId, recipeCallback);
    }

    public void getRecipeStep(@NonNull Integer recipeId, @NonNull Integer recipeStepId, final OnGetRecipeStepCallback recipeStepCallback) {
        remoteRepository.getRecipeStep(recipeId,recipeStepId, recipeStepCallback );
    }

    public void getAllRecipes(final OnGetRecipesCallback recipeCallback) {
        remoteRepository.getAllRecipes(recipeCallback);
    }

    /**
     *
     * @param favorite
     * @param listener
     */
    public void addAsFavorite(TbRecipeEntity favorite, OnGetFavoriteEntryUpdateCallback listener)
    {
        localRepository.insert(favorite, listener);
    }

    /**
     *
     * @param recipeId
     * @param listener
     */
    public void removeAsFavoriteByRecipeId(Integer recipeId, OnGetFavoriteEntryUpdateCallback listener)
    {
        localRepository.deleteByRecipeId(recipeId, listener);
    }

    /**
     *
     * @param listener
     */
    public void getAllFavorites(final OnGetFavoritesCallback listener)
    {
        localRepository.getListOfAllFavorites(listener);
    }

}
