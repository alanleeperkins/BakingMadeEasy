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

    private static final String sTAG = Constants.sTAG_FILTER + RecipeOverviewViewModel.class.getSimpleName();

    private BakingMadeEasyRepository mBakingMadeEasyRepositoryRepository;
    private @Nullable LiveData<TbRecipeEntity> mFavoritesEntityLiveData;

    /**
     *
     * @param application
     * @param recipeId
     */
    public RecipeOverviewViewModel(@NonNull Application application,@NonNull int recipeId) {
        super(application);

        mBakingMadeEasyRepositoryRepository = BakingMadeEasyRepository.getInstance(this.getApplication());
        mFavoritesEntityLiveData = mBakingMadeEasyRepositoryRepository.mLocalRepository.mRecipesDao.getRecipeByRecipeId(recipeId);

        Log.d(sTAG, "RecipeOverviewViewModel init");
    }

    public LiveData<TbRecipeEntity> getFavorite() {
        return mFavoritesEntityLiveData;
    }

    public BakingMadeEasyRepository getRepository() { return mBakingMadeEasyRepositoryRepository; }
}
