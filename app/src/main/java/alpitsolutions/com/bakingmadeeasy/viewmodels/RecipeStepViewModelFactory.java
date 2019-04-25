package alpitsolutions.com.bakingmadeeasy.viewmodels;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import alpitsolutions.com.bakingmadeeasy.utility.Constants;

public class RecipeStepViewModelFactory implements ViewModelProvider.Factory{

    private static final String TAG = Constants.TAG_FILTER + RecipeStepViewModelFactory.class.getSimpleName();

    private Application application;
    private int recipeId;
    private int recipeStepId;

    /**
     *
     * @param application
     * @param recipeStepId
     */
    public RecipeStepViewModelFactory(@NonNull Application application,@NonNull int recipeId, @NonNull int recipeStepId)
    {
        this.application = application;
        this.recipeStepId = recipeStepId;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new RecipeStepViewModel(application, recipeId, recipeStepId);
    }
}
