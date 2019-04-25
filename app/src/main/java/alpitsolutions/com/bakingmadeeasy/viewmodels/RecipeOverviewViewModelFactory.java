package alpitsolutions.com.bakingmadeeasy.viewmodels;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import alpitsolutions.com.bakingmadeeasy.utility.Constants;

public class RecipeOverviewViewModelFactory implements ViewModelProvider.Factory{

    private static final String TAG = Constants.TAG_FILTER + RecipeOverviewViewModelFactory.class.getSimpleName();

    private Application application;
    private int recipeId;

    /**
     *
     * @param application
     * @param recipeId
     */
    public RecipeOverviewViewModelFactory(@NonNull Application application, @NonNull int recipeId)
    {
        this.application = application;
        this.recipeId = recipeId;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new RecipeOverviewViewModel(application, recipeId);
    }


}
