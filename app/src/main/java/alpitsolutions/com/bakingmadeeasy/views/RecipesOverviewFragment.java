package alpitsolutions.com.bakingmadeeasy.views;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

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

    private static final String sTAG = Constants.sTAG_FILTER + RecipesOverviewFragment.class.getSimpleName();

    private static final int sMAX_COLUMNS_PHONE_VIEW = 1;
    private static final int sMAX_COLUMNS_TABLET_VIEW = 2;

    private RecipeOverviewAdapter sRecipeOverviewAdapter;

    @BindView(R.id.coord_layout_recipes) CoordinatorLayout mClyRecipes;
    @BindView(R.id.rvwRecipesOverview) RecyclerView mRvwRecipeOverview;
    @BindView(R.id.pbarRecipesLoading) ProgressBar mPbarRecipesLoading;
    @BindView(R.id.txtConnectionError) TextView mTxtConnectionError;

    private OnRecipeClickCallback mRecipeClickCallback;
    public interface OnRecipeClickCallback {
        void onRecipeItemClick(RecipeEntity recipe);
    }

    private OnRecipesOverviewBuildUICallback mOnRecipesOverviewBuildUICallback;
    public interface OnRecipesOverviewBuildUICallback {
        void onRecipesOverviewBuildUI(Boolean buildSuccessful);
    }

    private RecipesOverviewViewModel mViewModel;

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
        Log.d(sTAG,"RecipesOverviewFragment onAttach");
        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mRecipeClickCallback = (OnRecipeClickCallback) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnRecipeClickCallback");
        }
        try {
            mOnRecipesOverviewBuildUICallback = (OnRecipesOverviewBuildUICallback) context;
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
        Log.d(sTAG,"RecipesOverviewFragment onResume");
    }

    /**
     *
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(sTAG,"RecipesOverviewFragment onDestroy");
    }

    /**
     *
     */
    @Override
    public void onStart() {
        super.onStart();
        Log.d(sTAG,"RecipesOverviewFragment onStart");
    }

    /**
     *
     */
    @Override
    public void onPause() {
        super.onPause();
        Log.d(sTAG,"RecipesOverviewFragment onPause");
    }

    /**
     *
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(sTAG,"RecipesOverviewFragment onDestroyView");
    }

    /**
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(sTAG,"RecipesOverviewFragment onViewCreated");
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
        Log.d(sTAG, "RecipesOverviewFragment onCreateView");

        final View rootView = inflater.inflate(R.layout.fragment_recipes_overview, container, false);

        ButterKnife.bind(this, rootView);

        if(Helpers.isTabletScreenActive(getActivity())) {
            mRvwRecipeOverview.setLayoutManager(new GridLayoutManager(getActivity(), sMAX_COLUMNS_TABLET_VIEW));
        }else {
            mRvwRecipeOverview.setLayoutManager(new GridLayoutManager(getActivity(), sMAX_COLUMNS_PHONE_VIEW));
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
        mViewModel = ViewModelProviders.of(this).get(RecipesOverviewViewModel.class);
    }

    /***
     *
     * @return
     */
    private Boolean reloadActiveData()
    {
        Log.d(sTAG,"reloadActiveData");

        getAllRecipes();

        return true;
    }


    /***
     *
     */
    private void getAllRecipes()
    {
        if (Helpers.checkConnection(getActivity()) ==false) {

            mTxtConnectionError.setVisibility(View.VISIBLE);
            mPbarRecipesLoading.setVisibility(View.INVISIBLE);

            Snackbar snackbar = Snackbar.make(mClyRecipes,
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

        mViewModel.getRepository().getAllRecipes(new OnGetRecipesCallback() {
            @Override
            public void onStarted()
            {
                Log.d(sTAG,"onStarted fetching recipes");
                mPbarRecipesLoading.setVisibility(View.VISIBLE);
                mTxtConnectionError.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onSuccess(List<RecipeEntity> recipes) {
                Log.d(sTAG,"onSuccess recipes="+recipes.size());

                if (sRecipeOverviewAdapter == null)
                    sRecipeOverviewAdapter = new RecipeOverviewAdapter(recipes, clickOnRecipeCallback);

                mRvwRecipeOverview.setAdapter(sRecipeOverviewAdapter);
                mOnRecipesOverviewBuildUICallback.onRecipesOverviewBuildUI(true);

                //check for arguments (loading a recipe)
                if (Globals.getInstance().mIsActiveAutoLoadRecipe) {
                    Log.d(sTAG,"AUTOLOAD RECIPE #"+Globals.getInstance().mIdAutoLoadRecipe);

                    // simulate the 'click' on an recipe
                    RecipeEntity dummy_recipe = new RecipeEntity(Globals.getInstance().mIdAutoLoadRecipe,"",0,"",null,null);
                    mRecipeClickCallback.onRecipeItemClick(dummy_recipe);
                }

                mPbarRecipesLoading.setVisibility(View.INVISIBLE);
                mTxtConnectionError.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError() {
                mOnRecipesOverviewBuildUICallback.onRecipesOverviewBuildUI(false);
                Log.d(sTAG,"onError fetching recipes");
                mPbarRecipesLoading.setVisibility(View.INVISIBLE);
                mTxtConnectionError.setVisibility(View.INVISIBLE);
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
        Log.v(sTAG,"clickOnRecipeCallback name='" + recipe.getName() + "' clicked.");
        mRecipeClickCallback.onRecipeItemClick(recipe);
        }
    };
}
