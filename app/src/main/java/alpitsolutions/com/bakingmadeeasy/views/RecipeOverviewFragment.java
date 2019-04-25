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

    private static final String TAG = Constants.TAG_FILTER + RecipeOverviewFragment.class.getSimpleName();

    private RecipeEntity recipeData=null;
    private Integer recipeId = -1;
    private boolean isFavorite;

    private RecipeOverviewViewModel viewModel;

    @BindView(R.id.lilIngredients) TableLayout lilIngredients;
    @BindView(R.id.tlyRecipeSteps) TableLayout tlyRecipeSteps;
    @BindView(R.id.txtRecipeTitle) TextView txtRecipeTitle;
    @BindView(R.id.imgRecipeToggleFavorite) ImageView ivRecipeToggleFavorite;

    // Define a new interface OnRecipeStepClickCallback that triggers a callback in the host activity
    OnRecipeStepClickCallback recipeStepClickCallback;

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
        this.recipeId = recipeId;
    }

    /***
     * Override onAttach to make sure that the container activity has implemented the callback
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "RecipeOverviewFragment onAttach");

        try {
            recipeStepClickCallback = (OnRecipeStepClickCallback) context;
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
        Log.d(TAG, "RecipeOverviewFragment onStart");
    }

    /***
     *
     */
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "RecipeOverviewFragment onResume");
    }


    /***
     *
     */
    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "RecipeOverviewFragment onPause");
    }

    /***
     *
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "RecipeOverviewFragment onDestroy");
    }

    /**
     * Save the current state of this fragment
     */
    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putInt(Constants.KEY_RECIPE_ID, recipeId);
        currentState.putBoolean(Constants.KEY_IS_FAVORITE, isFavorite);
    }

    /***
     *
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "RecipeOverviewFragment onDestroyView");
    }

    /***
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "RecipeOverviewFragment onViewCreated");
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
        Log.d(TAG, "RecipeOverviewFragment onCreateView");

        final View rootView = inflater.inflate(R.layout.fragment_recipe_overview, container, false);

        ButterKnife.bind(this, rootView);

        isFavorite = false;
        if (savedInstanceState != null) {
            isFavorite = savedInstanceState.getBoolean(Constants.KEY_IS_FAVORITE);
            recipeId = savedInstanceState.getInt(Constants.KEY_RECIPE_ID);
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

        ivRecipeToggleFavorite.setOnClickListener(new View.OnClickListener() {
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
        RecipeOverviewViewModelFactory factory = new RecipeOverviewViewModelFactory(getActivity().getApplication(), recipeId);
        viewModel = ViewModelProviders.of(this, factory).get(RecipeOverviewViewModel.class);

        viewModel.getFavorite().observe(this, new Observer<TbRecipeEntity>() {
            @Override
            public void onChanged(@Nullable TbRecipeEntity tbRecipeEntity) {
                Log.d(TAG, "receiving database update from LiveData");
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
        Log.d(TAG,"reloadRecipeData");

        viewModel.getRepository().remoteRepository.getRecipeData(recipeId, new OnGetRecipeCallback() {

            @Override
            public void onStarted() {
                Log.d(TAG,"reloadRecipeData onStarted");
            }

            @Override
            public void onSuccess(RecipeEntity recipe) {
                Log.d(TAG,"reloadData onSuccess");
                Log.d(TAG,recipe.toString());
                recipeData = recipe;

                getActivity().setTitle("Recipe");

                txtRecipeTitle.setText(recipe.getName());

                updateIngredients(recipe.getIngredients());
                updateSteps(recipe.getId(),recipe.getSteps());
            }

            @Override
            public void onError() {
                Log.d(TAG,"reloadRecipeData onError");
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

            lilIngredients.addView(tr);
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
            tlyRecipeSteps.addView(yourButton);
            yourButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recipeStepClickCallback.onRecipeStepItemClicked(recipeId, step.getId());
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
        this.isFavorite = isFavorite;
        Log.v(TAG,"updateIsFavorite="+this.isFavorite);
        showRecipeFavoriteStateOnUI(this.isFavorite);
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
        if (isFavorite)
        {
            viewModel.getRepository().removeAsFavoriteByRecipeId(recipeId,this);
        }
        else
        {
            if (recipeData==null)
                return;

            TbRecipeEntity new_fav_recipe = Converter.ToTbRecipeEntity(recipeData);
            viewModel.getRepository().addAsFavorite(new_fav_recipe,this);
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
            ivRecipeToggleFavorite.setBackgroundResource(R.drawable.ic_star_golden_light);
        }
        else
        {
            ivRecipeToggleFavorite.setBackgroundResource(R.drawable.ic_star_grey_light);
        }

        RecipeWidgetProvider.sendRefreshBroadcast(this.getActivity());
    }
}