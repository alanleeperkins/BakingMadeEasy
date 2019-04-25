package alpitsolutions.com.bakingmadeeasy.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import alpitsolutions.com.bakingmadeeasy.database.TbRecipeEntity;
import alpitsolutions.com.bakingmadeeasy.repositories.BakingMadeEasyRepository;
import alpitsolutions.com.bakingmadeeasy.utility.Constants;

public class RecipeOverviewViewModel extends AndroidViewModel {

    private static final String TAG = Constants.TAG_FILTER + RecipeOverviewViewModel.class.getSimpleName();

    private BakingMadeEasyRepository bakingMadeEasyRepositoryRepository;
    private @Nullable LiveData<TbRecipeEntity> favoritesEntityLiveData;

    /**
     *
     * @param application
     * @param recipeId
     */
    public RecipeOverviewViewModel(@NonNull Application application,@NonNull int recipeId) {
        super(application);

        bakingMadeEasyRepositoryRepository = BakingMadeEasyRepository.getInstance(this.getApplication());
        favoritesEntityLiveData = bakingMadeEasyRepositoryRepository.localRepository.recipesDao.getRecipeByRecipeId(recipeId);

        Log.d(TAG, "RecipeOverviewViewModel init");
    }

    public LiveData<TbRecipeEntity> getFavorite() {
        return favoritesEntityLiveData;
    }

    public BakingMadeEasyRepository getRepository() { return bakingMadeEasyRepositoryRepository; }
}
