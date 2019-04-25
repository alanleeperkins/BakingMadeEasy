package alpitsolutions.com.bakingmadeeasy;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import alpitsolutions.com.bakingmadeeasy.models.RecipeEntity;
import alpitsolutions.com.bakingmadeeasy.utility.Constants;
import alpitsolutions.com.bakingmadeeasy.utility.Globals;
import alpitsolutions.com.bakingmadeeasy.utility.Helpers;
import alpitsolutions.com.bakingmadeeasy.utility.SimpleIdlingResource;
import alpitsolutions.com.bakingmadeeasy.viewmodels.RecipesOverviewViewModel;
import alpitsolutions.com.bakingmadeeasy.views.RecipesOverviewFragment;

public class RecipesOverviewActivity extends AppCompatActivity implements RecipesOverviewFragment.OnRecipeClickCallback,RecipesOverviewFragment.OnRecipesOverviewBuildUICallback {

    private static final String sTAG = Constants.sTAG_FILTER + RecipesOverviewActivity.class.getSimpleName();

    // The Idling Resource which will be null in production.
    @Nullable
    private SimpleIdlingResource mIdlingResourceUpdateUI;

    private RecipesOverviewViewModel mViewModel;

    /***
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_overview);

        Globals.getInstance().mIsActiveAutoLoadRecipe = false;
        Globals.getInstance().mIdAutoLoadRecipe = -1;

        if (savedInstanceState == null) {

            Globals.getInstance().mIdAutoLoadRecipe = getIntent().getIntExtra(Constants.sKEY_RECIPE_ID, -1);
            if (Globals.getInstance().mIdAutoLoadRecipe != -1) {
                Globals.getInstance().mIsActiveAutoLoadRecipe = true;
                Log.d(sTAG,"mIdAutoLoadRecipe = "+Globals.getInstance().mIdAutoLoadRecipe);
            }
        }

        // get the idling resource
        getIdlingResourceUpdateUI();

        /**
         * For testings purposes we need the IdlingResource, Espresso will use that variable
         * idleState=true means the Espresso can perform the next action
         */
        if (mIdlingResourceUpdateUI != null) {
            mIdlingResourceUpdateUI.setIdleState(false);
        }

        Helpers.setScreenOrientation(this);
        setupViewModel();
    }

    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResourceUpdateUI() {
        if (mIdlingResourceUpdateUI == null) {
            mIdlingResourceUpdateUI = new SimpleIdlingResource();
        }
        return mIdlingResourceUpdateUI;
    }

    /***
     *
     */
    private void setupViewModel() {
        mViewModel = ViewModelProviders.of(this).get(RecipesOverviewViewModel.class);
    }

    /***
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(sTAG,"onSaveInstanceState");
    }

    /**
     *
     */
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(sTAG,"onPause");
    }

    /***
     *
     */
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(sTAG,"onResume");
    }

    /**
     *
     * @param recipe
     */
    @Override
    public void onRecipeItemClick(RecipeEntity recipe) {
        Log.d(sTAG,"onRecipeItemClick " + recipe.toString());

        if (Helpers.isTabletScreenActive(this))
        {
            Log.d(sTAG,"Tablet Screen ");
        }
        else
        {
            Log.d(sTAG,"Phone Screen ");
        }
        Bundle b = new Bundle();
        b.putInt(Constants.sKEY_RECIPE_ID, recipe.getId());
        if(recipe.getSteps() !=null && recipe.getSteps().size()>0)
            b.putInt(Constants.sKEY_RECIPE_STEP_ID, 0);

        final Intent intent = new Intent(this, RecipeOverviewActivity.class);
        intent.putExtras(b);
        startActivity(intent);
    }

    /***
     *
     * @param buildSuccessful
     */
    @Override
    public void onRecipesOverviewBuildUI(Boolean buildSuccessful) {
        if (mIdlingResourceUpdateUI != null) {
            mIdlingResourceUpdateUI.setIdleState(true);
        }
    }
}
