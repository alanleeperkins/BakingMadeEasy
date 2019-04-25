package alpitsolutions.com.bakingmadeeasy.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import alpitsolutions.com.bakingmadeeasy.repositories.RecipesRemoteRepository;
import alpitsolutions.com.bakingmadeeasy.utility.Constants;

public class RecipeStepViewModel extends AndroidViewModel {

    private static final String sTAG = Constants.sTAG_FILTER + RecipeStepViewModel.class.getSimpleName();

    private RecipesRemoteRepository mRecipesRepository;

    /***
     *
     * @param application
     * @param recipeId
     * @param recipeStepId
     */
    public RecipeStepViewModel(@NonNull Application application,@NonNull Integer recipeId, @NonNull Integer recipeStepId) {
        super(application);

        mRecipesRepository = RecipesRemoteRepository.getInstance();
        Log.d(sTAG, "RecipeStepViewModel init");
    }

    public RecipesRemoteRepository getRecipesRepository() { return mRecipesRepository; }
}
