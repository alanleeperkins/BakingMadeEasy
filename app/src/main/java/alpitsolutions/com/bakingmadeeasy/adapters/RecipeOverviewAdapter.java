package alpitsolutions.com.bakingmadeeasy.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import alpitsolutions.com.bakingmadeeasy.R;
import alpitsolutions.com.bakingmadeeasy.models.RecipeEntity;
import alpitsolutions.com.bakingmadeeasy.utility.Constants;
import alpitsolutions.com.bakingmadeeasy.views.RecipesOverviewFragment;

public class RecipeOverviewAdapter extends RecyclerView.Adapter<RecipeOverviewAdapter.RecipeOverviewViewHolder> {

    private static final String TAG = Constants.TAG_FILTER + RecipeOverviewAdapter.class.getSimpleName();

    private List<RecipeEntity> recipes;
    final private RecipesOverviewFragment.OnRecipeClickCallback onRecipeClickCallback;

    /***
     *
     * @param recipes
     * @param onRecipeClickCallback
     */
    public RecipeOverviewAdapter(List<RecipeEntity> recipes, RecipesOverviewFragment.OnRecipeClickCallback onRecipeClickCallback) {
        this.recipes = recipes;
        this.onRecipeClickCallback = onRecipeClickCallback;
    }

    /***
     *
     * @param recipes
     */
    public void setRecipeEntryList(List<RecipeEntity> recipes)
    {
        this.recipes = recipes;
        notifyDataSetChanged();
    }

    /***
     * create a new RecyclerView
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public RecipeOverviewAdapter.RecipeOverviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.include_recipes_single_overview_card, parent, false);
        return new RecipeOverviewAdapter.RecipeOverviewViewHolder(view);
    }

    /***
     *
     * @param recipeOverviewViewHolderViewHolder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull RecipeOverviewAdapter.RecipeOverviewViewHolder recipeOverviewViewHolderViewHolder, int position) {
        Log.d(TAG,"onBindViewHolder position: "+position);
        recipeOverviewViewHolderViewHolder.bind(recipes.get(position));
    }

    /***
     * returns the number of items we have in our list
     * @return
     */
    @Override
    public int getItemCount() {
        return recipes.size();
    }


    /***
     *
     */
    class RecipeOverviewViewHolder extends RecyclerView.ViewHolder {
        TextView txtRecipeName;
        RecipeEntity recipeEntity;

        public RecipeOverviewViewHolder(@NonNull View viewItem) {
          super(viewItem);
          txtRecipeName = viewItem.findViewById(R.id.txtRecipeTitle);
          itemView.setOnClickListener(new View.OnClickListener(){
              @Override
              public void onClick(View v) {
                  onRecipeClickCallback.onRecipeItemClick(recipeEntity);
              }
        });
      }

    /***
     *
     * @param recipeEntity
     */
      public void bind(RecipeEntity recipeEntity) {
          this.recipeEntity = recipeEntity;

          txtRecipeName.setText(recipeEntity.getName());
      }
    }
}
