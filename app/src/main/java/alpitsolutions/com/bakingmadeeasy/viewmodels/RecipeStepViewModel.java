package alpitsolutions.com.bakingmadeeasy.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import alpitsolutions.com.bakingmadeeasy.repositories.RecipesRemoteRepository;
import alpitsolutions.com.bakingmadeeasy.utility.Constants;

public class RecipeStepViewModel extends AndroidViewModel {

    private static final String TAG = Constants.TAG_FILTER + RecipeStepViewModel.class.getSimpleName();

    private RecipesRemoteRepository recipesRepository;

    /***
     *
     * @param application
     * @param recipeId
     * @param recipeStepId
     */
    public RecipeStepViewModel(@NonNull Application application,@NonNull Integer recipeId, @NonNull Integer recipeStepId) {
        super(application);

        recipesRepository = RecipesRemoteRepository.getInstance();
        Log.d(TAG, "RecipeStepViewModel init");
    }

    public RecipesRemoteRepository getRecipesRepository() { return recipesRepository; }
}
