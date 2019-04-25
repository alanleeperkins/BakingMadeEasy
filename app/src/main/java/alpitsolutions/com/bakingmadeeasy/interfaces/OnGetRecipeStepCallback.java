package alpitsolutions.com.bakingmadeeasy.interfaces;

import alpitsolutions.com.bakingmadeeasy.models.RecipeStepEntity;

public interface OnGetRecipeStepCallback {
    void onStarted();
    void onSuccess(RecipeStepEntity recipeStep);
    void onError();
}
