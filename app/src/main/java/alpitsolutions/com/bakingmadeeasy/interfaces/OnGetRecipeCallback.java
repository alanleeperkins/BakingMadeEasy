package alpitsolutions.com.bakingmadeeasy.interfaces;
import alpitsolutions.com.bakingmadeeasy.models.RecipeEntity;

public interface OnGetRecipeCallback {
    void onStarted();
    void onSuccess(RecipeEntity recipe);
    void onError();
}
