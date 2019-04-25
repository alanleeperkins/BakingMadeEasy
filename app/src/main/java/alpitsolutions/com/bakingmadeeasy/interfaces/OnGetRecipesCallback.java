package alpitsolutions.com.bakingmadeeasy.interfaces;

import java.util.List;

import alpitsolutions.com.bakingmadeeasy.models.RecipeEntity;

public interface OnGetRecipesCallback {

    void onStarted();
    void onSuccess(List<RecipeEntity> recipes);
    void onError();

}
