package alpitsolutions.com.bakingmadeeasy;

import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import alpitsolutions.com.bakingmadeeasy.utility.Constants;
import alpitsolutions.com.bakingmadeeasy.utility.Helpers;
import alpitsolutions.com.bakingmadeeasy.viewmodels.RecipeStepViewModel;
import alpitsolutions.com.bakingmadeeasy.viewmodels.RecipeStepViewModelFactory;
import alpitsolutions.com.bakingmadeeasy.views.RecipeStepFragment;

public class RecipeStepActivity extends AppCompatActivity {

    private static final String sTAG = Constants.sTAG_FILTER + RecipeStepActivity.class.getSimpleName();

    private RecipeStepViewModel mViewModel;
    private Integer mRecipeId = -1;
    private Integer mRecipeStepId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(sTAG, "onCreate");

        setContentView(R.layout.activity_recipe_step);

        Helpers.setScreenOrientation(this);

        if (savedInstanceState == null) {
            int recipeId = getIntent().getIntExtra(Constants.sKEY_RECIPE_ID, 0);
            int recipeStepId = getIntent().getIntExtra(Constants.sKEY_RECIPE_STEP_ID, 0);
            if (recipeId <= 0) {
                Log.d(sTAG, "ERROR: illegal mRecipeId:" + recipeId);
                finish();
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            RecipeStepFragment recipeStepFragment = new RecipeStepFragment();
            recipeStepFragment.setRecipeStep(recipeId, recipeStepId);
            fragmentManager.beginTransaction()
                    .add(R.id.recipe_step_container, recipeStepFragment)
                    .commit();
        }
    }

    /***
     *
     */
    private void setupViewModel()
    {
        RecipeStepViewModelFactory factory = new RecipeStepViewModelFactory(this.getApplication(), mRecipeId, mRecipeStepId);
        mViewModel = ViewModelProviders.of(this, factory).get(RecipeStepViewModel.class);
    }




}
