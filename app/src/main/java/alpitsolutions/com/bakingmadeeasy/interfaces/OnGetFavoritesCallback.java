package alpitsolutions.com.bakingmadeeasy.interfaces;

import java.util.List;

import alpitsolutions.com.bakingmadeeasy.database.TbRecipeEntity;
import alpitsolutions.com.bakingmadeeasy.models.RecipeEntity;

public interface OnGetFavoritesCallback {
    void onStarted();
    void onSuccess(List<TbRecipeEntity> favorites);
    void onError();
}
