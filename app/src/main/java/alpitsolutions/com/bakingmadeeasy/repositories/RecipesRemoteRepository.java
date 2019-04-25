package alpitsolutions.com.bakingmadeeasy.repositories;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import alpitsolutions.com.bakingmadeeasy.interfaces.OnGetRecipeCallback;
import alpitsolutions.com.bakingmadeeasy.interfaces.OnGetRecipeStepCallback;
import alpitsolutions.com.bakingmadeeasy.interfaces.OnGetRecipesCallback;
import alpitsolutions.com.bakingmadeeasy.interfaces.RecipeApi;
import alpitsolutions.com.bakingmadeeasy.models.RecipeEntity;
import alpitsolutions.com.bakingmadeeasy.models.RecipeStepEntity;
import alpitsolutions.com.bakingmadeeasy.utility.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipesRemoteRepository {

    private static final String TAG = Constants.TAG_FILTER + RecipesRemoteRepository.class.getSimpleName();

    public static final String RECIPES_CLOUDFRONT_BASE_URL = "https://d17h27t6h515a5.cloudfront.net/";

    private static RecipesRemoteRepository repository;

    private RecipeApi recipeApi;


    private List<RecipeEntity> cacheData=null;

    /***
     *
     * @param recipeApi
     */
    private RecipesRemoteRepository(RecipeApi recipeApi) {
        this.recipeApi = recipeApi;
    }

    /***
     * returns an instance of our RecipesRemoteRepository
     * @return
     */
    public static RecipesRemoteRepository getInstance() {
        if (repository == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(RECIPES_CLOUDFRONT_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            repository = new RecipesRemoteRepository(retrofit.create(RecipeApi.class));
        }

        return repository;
    }

    /***
     *
     * @param recipeId
     * @param recipeCallback
     */
    public void getRecipeData(int recipeId, final OnGetRecipeCallback recipeCallback) {
        Log.d(TAG, "getRecipeData recipeId:"+recipeId);
        recipeCallback.onStarted();

        if (cacheData == null || cacheData.size()==0) {
            Log.d(TAG, "ERROR: getRecipeData cache is empty!");
            recipeCallback.onError();
            return;
        }

        for (RecipeEntity recipe:cacheData) {
            if (recipe.getId().equals(recipeId)) {
                // SUCCESS -> recipe found
                Log.d(TAG, "SUCCESS: getRecipeData recipe found!");
                recipeCallback.onSuccess(recipe);
                return;
            }
        }

        recipeCallback.onError();
    }

    /**
     *
     * @param recipeId
     * @param recipeStepId
     * @param recipeStepCallback
     */
    public void getRecipeStep(@NonNull Integer recipeId, @NonNull Integer recipeStepId, final OnGetRecipeStepCallback recipeStepCallback)
    {
        Log.d(TAG, "getRecipeStep recipeId:"+recipeId+" recipeStepId:"+recipeStepId);
        recipeStepCallback.onStarted();

        if (cacheData == null || cacheData.size()==0) {
            Log.d(TAG, "ERROR: getRecipeStep cache is empty!");
            recipeStepCallback.onError();
            return;
        }

        for (RecipeEntity recipe:cacheData) {
            if (recipe.getId().equals(recipeId)) {
                if (recipe.getSteps()==null || recipe.getSteps().size()==0) {
                    recipeStepCallback.onError();
                    return;
                }

                for (RecipeStepEntity recipeStep:recipe.getSteps()) {
                    if (recipeStep.getId().equals(recipeStepId)) {
                        // SUCCESS -> step found
                        Log.d(TAG, "SUCCESS: getRecipeStep recipe-step found!");
                        recipeStepCallback.onSuccess(recipeStep);
                        return;
                    }
                }
            }
        }

        recipeStepCallback.onError();
    }

    /***
     *
     * @param recipeCallback
     */
    public void getAllRecipes(final OnGetRecipesCallback recipeCallback) {
        recipeCallback.onStarted();
        if (cacheData == null || cacheData.size()==0) {
            Log.d(TAG, "getAllRecipes (refresh)");
            // refresh the cache
            Callback<List<RecipeEntity>> call = new Callback<List<RecipeEntity>>() {
                @Override
                public void onResponse(Call<List<RecipeEntity>> call, Response<List<RecipeEntity>> response) {
                    if (response.isSuccessful()) {
                        // update the cache
                        cacheData = response.body();
                        if (cacheData !=null) {
                            // send the cache
                            recipeCallback.onSuccess(cacheData);
                        }
                        else {
                            recipeCallback.onError();
                        }
                    }
                    else {
                        recipeCallback.onError();
                    }
                }

                @Override
                public void onFailure(Call<List<RecipeEntity>> call, Throwable t) {
                    recipeCallback.onError();
                }
            };

            recipeApi.getAllRecipes().enqueue(call);
        }
        else
        {
            Log.d(TAG, "getAllRecipes (already cached)");
            if (cacheData !=null) {
                // send the cache
                recipeCallback.onSuccess(cacheData);
            }
            else {
                recipeCallback.onError();
            }
        }
    }
}
