package alpitsolutions.com.bakingmadeeasy.viewmodels;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import alpitsolutions.com.bakingmadeeasy.utility.Constants;

public class RecipeStepViewModelFactory implements ViewModelProvider.Factory{

    private static final String sTAG = Constants.sTAG_FILTER + RecipeStepViewModelFactory.class.getSimpleName();

    private Application mApplication;
    private int mRecipeId;
    private int mRecipeStepId;

    /**
     *
     * @param application
     * @param recipeStepId
     */
    public RecipeStepViewModelFactory(@NonNull Application application,@NonNull int recipeId, @NonNull int recipeStepId)
    {
        this.mApplication = application;
        this.mRecipeId = recipeId;
        this.mRecipeStepId = recipeStepId;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new RecipeStepViewModel(mApplication, mRecipeId, mRecipeStepId);
    }
}
