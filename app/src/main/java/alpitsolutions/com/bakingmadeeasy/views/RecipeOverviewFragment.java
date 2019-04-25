package alpitsolutions.com.bakingmadeeasy.views;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import alpitsolutions.com.bakingmadeeasy.R;
import alpitsolutions.com.bakingmadeeasy.database.TbRecipeEntity;
import alpitsolutions.com.bakingmadeeasy.interfaces.OnGetFavoriteEntryUpdateCallback;
import alpitsolutions.com.bakingmadeeasy.interfaces.OnGetRecipeCallback;
import alpitsolutions.com.bakingmadeeasy.models.RecipeEntity;
import alpitsolutions.com.bakingmadeeasy.models.RecipeIngredientEntity;
import alpitsolutions.com.bakingmadeeasy.models.RecipeStepEntity;
import alpitsolutions.com.bakingmadeeasy.utility.Constants;
import alpitsolutions.com.bakingmadeeasy.utility.Converter;
import alpitsolutions.com.bakingmadeeasy.utility.Helpers;
import alpitsolutions.com.bakingmadeeasy.viewmodels.RecipeOverviewViewModel;
import alpitsolutions.com.bakingmadeeasy.viewmodels.RecipeOverviewViewModelFactory;
import alpitsolutions.com.bakingmadeeasy.widget.RecipeWidgetProvider;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeOverviewFragment extends Fragment implements OnGetFavoriteEntryUpdateCallback {

    private static final String sTAG = Constants.sTAG_FILTER + RecipeOverviewFragment.class.getSimpleName();

    private RecipeEntity mRecipeData =null;
    private Integer mRecipeId = -1;
    private boolean mIsFavorite;
    private RecipeOverviewViewModel mViewModel;

    @BindView(R.id.lilIngredients) TableLayout mLilIngredients;
    @BindView(R.id.tlyRecipeSteps) TableLayout mTlyRecipeSteps;
    @BindView(R.id.txtRecipeTitle) TextView mTxtRecipeTitle;
    @BindView(R.id.imgRecipeToggleFavorite) ImageView mIvwRecipeToggleFavorite;

    // Define a new interface OnRecipeStepClickCallback that triggers a callback in the host activity
    OnRecipeStepClickCallback mOnRecipeStepClickCallback;

    /***
     * OnRecipeStepClickCallback interface, calls a method in the host activity named onRecipeStepSelected
     */
    public interface OnRecipeStepClickCallback {
        void onRecipeStepItemClicked(Integer recipeId, Integer recipeStepId);
    }


    /***
     *
     */
    public RecipeOverviewFragment() {

    }

    /**
     *
     * @param recipeId
     */
    public void setRecipeId(Integer recipeId) {
        this.mRecipeId = recipeId;
    }

    /***
     * Override onAttach to make sure that the container activity has implemented the callback
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(sTAG, "RecipeOverviewFragment onAttach");

        try {
            mOnRecipeStepClickCallback = (OnRecipeStepClickCallback) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnRecipeStepClickCallback");
        }
    }

    /***
     *
     */
    @Override
    public void onStart() {
        super.onStart();
        Log.d(sTAG, "RecipeOverviewFragment onStart");
    }

    /***
     *
     */
    @Override
    public void onResume() {
        super.onResume();
        Log.d(sTAG, "RecipeOverviewFragment onResume");
    }


    /***
     *
     */
    @Override
    public void onPause() {
        super.onPause();
        Log.d(sTAG, "RecipeOverviewFragment onPause");
    }

    /***
     *
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(sTAG, "RecipeOverviewFragment onDestroy");
    }

    /**
     * Save the current state of this fragment
     */
    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putInt(Constants.sKEY_RECIPE_ID, mRecipeId);
        currentState.putBoolean(Constants.sKEY_IS_FAVORITE, mIsFavorite);
    }

    /***
     *
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(sTAG, "RecipeOverviewFragment onDestroyView");
    }

    /***
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(sTAG, "RecipeOverviewFragment onViewCreated");
    }

    /***
     * Inflates the recipe steps-menu and the ingredients
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(sTAG, "RecipeOverviewFragment onCreateView");

        final View rootView = inflater.inflate(R.layout.fragment_recipe_overview, container, false);

        ButterKnife.bind(this, rootView);

        mIsFavorite = false;
        if (savedInstanceState != null) {
            mIsFavorite = savedInstanceState.getBoolean(Constants.sKEY_IS_FAVORITE);
            mRecipeId = savedInstanceState.getInt(Constants.sKEY_RECIPE_ID);
        }else {

        }

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

        mIvwRecipeToggleFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleRecipeFavoriteStatus();
            }
        });

        reloadRecipeData();
    }

    /***
     *
     */
    private void setupViewModel()
    {
        RecipeOverviewViewModelFactory factory = new RecipeOverviewViewModelFactory(getActivity().getApplication(), mRecipeId);
        mViewModel = ViewModelProviders.of(this, factory).get(RecipeOverviewViewModel.class);

        mViewModel.getFavorite().observe(this, new Observer<TbRecipeEntity>() {
            @Override
            public void onChanged(@Nullable TbRecipeEntity tbRecipeEntity) {
                Log.d(sTAG, "receiving database update from LiveData");
                updateIsFavorite((tbRecipeEntity!=null));
            }
        });
    }

    /***
     *
     * @return
     */
    private Boolean reloadRecipeData()
    {
        Log.d(sTAG,"reloadRecipeData");

        mViewModel.getRepository().mRemoteRepository.getRecipeData(mRecipeId, new OnGetRecipeCallback() {

            @Override
            public void onStarted() {
                Log.d(sTAG,"reloadRecipeData onStarted");
            }

            @Override
            public void onSuccess(RecipeEntity recipe) {
                Log.d(sTAG,"reloadData onSuccess");
                Log.d(sTAG,recipe.toString());
                mRecipeData = recipe;

                getActivity().setTitle("Recipe");

                mTxtRecipeTitle.setText(recipe.getName());

                updateIngredients(recipe.getIngredients());
                updateSteps(recipe.getId(),recipe.getSteps());
            }

            @Override
            public void onError() {
                Log.d(sTAG,"reloadRecipeData onError");
            }
        });

        return true;
    }

    /**
     *
     * @param ingredients
     */
    private void updateIngredients(List<RecipeIngredientEntity> ingredients)
    {
        if (ingredients == null || ingredients.size()==0)
            return;

        LayoutInflater inflater=(LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (RecipeIngredientEntity ingredient: ingredients) {

            View tr = inflater.inflate(R.layout.include_recipe_ingredient_row, null);
            TextView txtQuantityIngredient = tr.findViewById(R.id.txtQuantityIngredient);
            TextView txtNameIngredient = tr.findViewById(R.id.txtNameIngredient);

            txtQuantityIngredient.setText(Helpers.getQuantityMeasureString(ingredient.getQuantity(), Helpers.getMeasureUnit(getActivity(),ingredient.getMeasure())));

            txtNameIngredient.setText(ingredient.getIngredient());

            mLilIngredients.addView(tr);
        }
    }

    /**
     *
     * @param recipeId
     * @param steps
     */
    private void updateSteps(final int recipeId, final List<RecipeStepEntity> steps)
    {
        if (steps == null || steps.size()==0)
            return;

        for (final RecipeStepEntity step: steps) {
            String buttonText = String.format("%d. %s",step.getId(), step.getShortDescription());
            Button yourButton = new Button(this.getActivity());
            yourButton.setGravity(Gravity.LEFT| Gravity.CENTER_VERTICAL);
            yourButton.setTag(step.getId());
            yourButton.setText(buttonText);
            mTlyRecipeSteps.addView(yourButton);
            yourButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnRecipeStepClickCallback.onRecipeStepItemClicked(recipeId, step.getId());
                }
            });
        }
    }

    /***
     *
     * @param isFavorite
     */
    private void updateIsFavorite(Boolean isFavorite)
    {
        this.mIsFavorite = isFavorite;
        Log.v(sTAG,"updateIsFavorite="+this.mIsFavorite);
        showRecipeFavoriteStateOnUI(this.mIsFavorite);
    }

    /**
     *
     */
    @Override
    public void deletingFavoriteSuccessful() {
        showRecipeFavoriteStateOnUI(false);
        Toast.makeText(getActivity(),"Removed Favorite",Toast.LENGTH_SHORT).show();
    }

    /**
     *
     */
    @Override
    public void addingFavoriteSuccessful() {
        showRecipeFavoriteStateOnUI(true);
        Toast.makeText(getActivity(),"Added Favorite",Toast.LENGTH_SHORT).show();
    }

    /**
     * adds/removes a recipe from the favorite table
     */
    private void toggleRecipeFavoriteStatus()
    {
        if (mIsFavorite)
        {
            mViewModel.getRepository().removeAsFavoriteByRecipeId(mRecipeId,this);
        }
        else
        {
            if (mRecipeData ==null)
                return;

            TbRecipeEntity new_fav_recipe = Converter.ToTbRecipeEntity(mRecipeData);
            mViewModel.getRepository().addAsFavorite(new_fav_recipe,this);
        }
    }


    /***
     * shows the favorite status with a state specific image
     * @param favoriteState
     */
    public void showRecipeFavoriteStateOnUI(boolean favoriteState)
    {
        if (favoriteState)
        {
            mIvwRecipeToggleFavorite.setBackgroundResource(R.drawable.ic_star_golden_light);
        }
        else
        {
            mIvwRecipeToggleFavorite.setBackgroundResource(R.drawable.ic_star_grey_light);
        }

        RecipeWidgetProvider.sendRefreshBroadcast(this.getActivity());
    }
}