package alpitsolutions.com.bakingmadeeasy;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import alpitsolutions.com.bakingmadeeasy.utility.Constants;
import alpitsolutions.com.bakingmadeeasy.utility.Helpers;
import alpitsolutions.com.bakingmadeeasy.views.RecipeOverviewFragment;
import alpitsolutions.com.bakingmadeeasy.views.RecipeStepFragment;

public class RecipeOverviewActivity extends AppCompatActivity implements RecipeOverviewFragment.OnRecipeStepClickCallback {

    private static final String sTAG = Constants.sTAG_FILTER + RecipeOverviewActivity.class.getSimpleName();

    // Track whether to display a two-pane or single-pane UI
    // A single-pane display refers to phone screens, and two-pane to larger tablet screens
    private Boolean mIsSetTwoPaneMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(sTAG, "onCreate");

        setContentView(R.layout.activity_recipe_overview);
        Helpers.setScreenOrientation(this);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // with the fragment_divider active we know whe have the two pane mode set
        mIsSetTwoPaneMode = (findViewById(R.id.fragment_divider) != null);
        Log.d(sTAG,"mIsSetTwoPaneMode = " + mIsSetTwoPaneMode.toString());

        if (savedInstanceState == null) {

            int recipeId = getIntent().getIntExtra(Constants.sKEY_RECIPE_ID, 0);
            int recipeStepId = getIntent().getIntExtra(Constants.sKEY_RECIPE_STEP_ID, 0);
            if (recipeId<=0) {
                Log.d(sTAG, "ERROR: illegal recipeId:" + recipeId);
                finish();
            }

            // always create the left menu -> recipe overview fragment
            RecipeOverviewFragment recipeOverviewFragment = new RecipeOverviewFragment();
            recipeOverviewFragment.setRecipeId(recipeId);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.recipe_overview_container, recipeOverviewFragment)
                    .commit();


            if (mIsSetTwoPaneMode) {
                replaceRecipeStepFragment(recipeId, recipeStepId);
            }
        }
    }

    /**
     * replace the current recipe step fragment
     * @param recipeId
     * @param recipeStepId
     */
    private void replaceRecipeStepFragment(Integer recipeId, Integer recipeStepId) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        // create the right recipe step -> recipe step fragment
        RecipeStepFragment recipeStepFragment = new RecipeStepFragment();
        recipeStepFragment.setRecipeStep(recipeId, recipeStepId);
        fragmentManager.beginTransaction()
                .replace(R.id.recipe_step_container, recipeStepFragment)
                .commit();
    }

    /**
     *
     * @param recipeId
     * @param recipeStepId
     */
    @Override
    public void onRecipeStepItemClicked(Integer recipeId, Integer recipeStepId) {
        Log.d(sTAG,"onRecipeStepItemClicked rc=" + recipeId + " stp="+recipeStepId);

        if (mIsSetTwoPaneMode) {
            Log.d(sTAG,"onRecipeStepItemClicked mIsSetTwoPaneMode");
            replaceRecipeStepFragment(recipeId, recipeStepId);
        }
        else {
            Log.d(sTAG,"onRecipeStepItemClicked singlePaneMode");

            Bundle b = new Bundle();
            b.putInt(Constants.sKEY_RECIPE_ID, recipeId);
            b.putInt(Constants.sKEY_RECIPE_STEP_ID, recipeStepId);

            final Intent intent = new Intent(this, RecipeStepActivity.class);
            intent.putExtras(b);
            startActivity(intent);
        }
    }
}
