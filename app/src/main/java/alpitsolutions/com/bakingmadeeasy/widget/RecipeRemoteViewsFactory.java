package alpitsolutions.com.bakingmadeeasy.widget;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.List;

import alpitsolutions.com.bakingmadeeasy.R;
import alpitsolutions.com.bakingmadeeasy.database.TbRecipeEntity;
import alpitsolutions.com.bakingmadeeasy.repositories.BakingMadeEasyRepository;
import alpitsolutions.com.bakingmadeeasy.utility.Constants;

public class RecipeRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String TAG = Constants.TAG_FILTER + RecipeRemoteViewsFactory.class.getSimpleName();
    private Context context;
    private BakingMadeEasyRepository repository=null;

    private List<TbRecipeEntity> recipes = null;
    /**
     *
     * @param applicationContext
     */
    public RecipeRemoteViewsFactory(Context applicationContext) {
        context = applicationContext;
        repository = BakingMadeEasyRepository.getInstance(applicationContext);
    }

    @Override
    public void onCreate() {
        Log.d(TAG,"onCreate");
    }

    @Override
    public void onDataSetChanged() {
        Log.d(TAG,"onDataSetChanged");
        updateRecipeList();
    }

    /**
     *
     */
    private void updateRecipeList() {
        Log.d(TAG,"updateRecipeList");
        if (repository==null)
            return;

        recipes = repository.localRepository.recipesDao.getListOfAllRecipes();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG,"onDestroy");
    }

    @Override
    public int getCount() {

        if(recipes==null) {
            Log.d(TAG,"getCount = NULL");
            return 0;
        }

        int count = recipes.size();
        Log.d(TAG,"getCount = "+count);

        return count;
    }

    /**
     *
     * @param position
     * @return
     */
    @Override
    public RemoteViews getViewAt(int position) {
        Log.d(TAG,"getViewAt pos "+position);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.favorite_recipes_widget_list_item);
        remoteViews.setTextViewText(R.id.txtRecipeName,recipes.get(position).getName());

        Intent fillInIntent = new Intent();
        fillInIntent.putExtra(Constants.KEY_RECIPE_ID, recipes.get(position).getRecipeId());

        remoteViews.setOnClickFillInIntent(R.id.recipe_widget_item_container, fillInIntent);

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        Log.d(TAG,"getLoadingView");
        return null;
    }

    @Override
    public int getViewTypeCount() {
        Log.d(TAG,"getViewTypeCount");
        return 1;
    }

    @Override
    public long getItemId(int position) {
        Log.d(TAG,"getItemId");
        return position;
    }

    @Override
    public boolean hasStableIds() {
        Log.d(TAG,"hasStableIds");
        return true;
    }
}
