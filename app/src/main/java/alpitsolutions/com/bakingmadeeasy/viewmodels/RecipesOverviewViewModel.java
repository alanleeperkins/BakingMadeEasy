package alpitsolutions.com.bakingmadeeasy.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;

import alpitsolutions.com.bakingmadeeasy.models.RecipeEntity;
import alpitsolutions.com.bakingmadeeasy.repositories.BakingMadeEasyRepository;
import alpitsolutions.com.bakingmadeeasy.utility.Constants;

public class RecipesOverviewViewModel extends AndroidViewModel {

    private static final String TAG = Constants.TAG_FILTER + RecipesOverviewViewModel.class.getSimpleName();

    private BakingMadeEasyRepository bakingMadeEasyRepositoryRepository;
    private @Nullable
    LiveData<List<RecipeEntity>> recipes;

    public RecipesOverviewViewModel(@NonNull Application application) {
        super(application);

        bakingMadeEasyRepositoryRepository = BakingMadeEasyRepository.getInstance(this.getApplication());
        Log.d(TAG, "RecipesOverviewViewModel init");
    }


    public BakingMadeEasyRepository getRepository() { return bakingMadeEasyRepositoryRepository; }
}
