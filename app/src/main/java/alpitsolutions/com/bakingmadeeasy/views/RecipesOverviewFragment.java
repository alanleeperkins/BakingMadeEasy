package alpitsolutions.com.bakingmadeeasy.views;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.solver.widgets.Helper;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.mbms.MbmsErrors;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import alpitsolutions.com.bakingmadeeasy.R;
import alpitsolutions.com.bakingmadeeasy.adapters.RecipeOverviewAdapter;
import alpitsolutions.com.bakingmadeeasy.interfaces.OnGetRecipesCallback;
import alpitsolutions.com.bakingmadeeasy.models.RecipeEntity;
import alpitsolutions.com.bakingmadeeasy.utility.Constants;
import alpitsolutions.com.bakingmadeeasy.utility.Globals;
import alpitsolutions.com.bakingmadeeasy.utility.Helpers;
import alpitsolutions.com.bakingmadeeasy.viewmodels.RecipesOverviewViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesOverviewFragment extends Fragment {

    private static final String TAG = Constants.TAG_FILTER + RecipesOverviewFragment.class.getSimpleName();

    private static final int MAX_COLUMNS_PHONE_VIEW = 1;
    private static final int MAX_COLUMNS_TABLET_VIEW = 2;

    private RecipeOverviewAdapter recipeOverviewAdapter;

    @BindView(R.id.coord_layout_recipes) CoordinatorLayout coord_layout_recipes;
    @BindView(R.id.rvwRecipesOverview) RecyclerView rvwRecipeOverview;
    @BindView(R.id.pbarRecipesLoading) ProgressBar pbarRecipesLoading;
    @BindView(R.id.txtConnectionError) TextView txtConnectionError;

    private OnRecipeClickCallback recipeClickCallback;
    public interface OnRecipeClickCallback {
        void onRecipeItemClick(RecipeEntity recipe);
    }

    private OnRecipesOverviewBuildUICallback onRecipesOverviewBuildUICallback;
    public interface OnRecipesOverviewBuildUICallback {
        void onRecipesOverviewBuildUI(Boolean buildSuccessful);
    }

    private RecipesOverviewViewModel viewModel;

    /**
     *
     */
    public RecipesOverviewFragment() {
    }

    /***
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG,"RecipesOverviewFragment onAttach");
        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            recipeClickCallback = (OnRecipeClickCallback) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnRecipeClickCallback");
        }
        try {
            onRecipesOverviewBuildUICallback = (OnRecipesOverviewBuildUICallback) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnRecipesOverviewBuildUICallback");
        }
    }

    /**
     *
     */
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG,"RecipesOverviewFragment onResume");
    }

    /**
     *
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"RecipesOverviewFragment onDestroy");
    }

    /**
     *
     */
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG,"RecipesOverviewFragment onStart");
    }

    /**
     *
     */
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG,"RecipesOverviewFragment onPause");
    }

    /**
     *
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG,"RecipesOverviewFragment onDestroyView");
    }

    /**
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG,"RecipesOverviewFragment onViewCreated");
    }

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "RecipesOverviewFragment onCreateView");

        final View rootView = inflater.inflate(R.layout.fragment_recipes_overview, container, false);

        ButterKnife.bind(this, rootView);

        if(Helpers.isTabletScreenActive(getActivity())) {
            rvwRecipeOverview.setLayoutManager(new GridLayoutManager(getActivity(),MAX_COLUMNS_TABLET_VIEW));
        }else {
            rvwRecipeOverview.setLayoutManager(new GridLayoutManager(getActivity(),MAX_COLUMNS_PHONE_VIEW));
        }

        // Return the root view
        return rootView;
    }

    /***
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupViewModel();

        reloadActiveData();
    }

    /***
     *
     */
    private void setupViewModel()
    {
        viewModel = ViewModelProviders.of(this).get(RecipesOverviewViewModel.class);
    }

    /***
     *
     * @return
     */
    private Boolean reloadActiveData()
    {
        Log.d(TAG,"reloadActiveData");

        getAllRecipes();

        return true;
    }


    /***
     *
     */
    private void getAllRecipes()
    {
        if (Helpers.checkConnection(getActivity()) ==false) {

            txtConnectionError.setVisibility(View.VISIBLE);
            pbarRecipesLoading.setVisibility(View.INVISIBLE);

            Snackbar snackbar = Snackbar.make(coord_layout_recipes,
                    R.string.error_no_internet_connection, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.retry, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getAllRecipes();
                        }
                    });
            snackbar.show();

            return;
        }

        viewModel.getRepository().getAllRecipes(new OnGetRecipesCallback() {
            @Override
            public void onStarted()
            {
                Log.d(TAG,"onStarted fetching recipes");
                pbarRecipesLoading.setVisibility(View.VISIBLE);
                txtConnectionError.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onSuccess(List<RecipeEntity> recipes) {
                Log.d(TAG,"onSuccess recipes="+recipes.size());

                if (recipeOverviewAdapter == null)
                    recipeOverviewAdapter = new RecipeOverviewAdapter(recipes, clickOnRecipeCallback);

                rvwRecipeOverview.setAdapter(recipeOverviewAdapter);
                onRecipesOverviewBuildUICallback.onRecipesOverviewBuildUI(true);

                //check for arguments (loading a recipe)
                if (Globals.getInstance().isActiveAutoLoadRecipe) {
                    Log.d(TAG,"AUTOLOAD RECIPE #"+Globals.getInstance().idAutoLoadRecipe);

                    // simulate the 'click' on an recipe
                    RecipeEntity dummy_recipe = new RecipeEntity(Globals.getInstance().idAutoLoadRecipe,"",0,"",null,null);
                    recipeClickCallback.onRecipeItemClick(dummy_recipe);
                }

                pbarRecipesLoading.setVisibility(View.INVISIBLE);
                txtConnectionError.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError() {
                onRecipesOverviewBuildUICallback.onRecipesOverviewBuildUI(false);
                Log.d(TAG,"onError fetching recipes");
                pbarRecipesLoading.setVisibility(View.INVISIBLE);
                txtConnectionError.setVisibility(View.INVISIBLE);
            }
        });
    }

    /***
     * the callback when the user clicks on the movie image
     */
    OnRecipeClickCallback clickOnRecipeCallback = new OnRecipeClickCallback()
    {
        @Override
        public void onRecipeItemClick(RecipeEntity recipe) {
        Log.v(TAG,"clickOnRecipeCallback name='" + recipe.getName() + "' clicked.");
        recipeClickCallback.onRecipeItemClick(recipe);
        }
    };
}
