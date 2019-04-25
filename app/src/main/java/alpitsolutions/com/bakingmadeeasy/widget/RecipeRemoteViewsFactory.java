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

    private static final String sTAG = Constants.sTAG_FILTER + RecipeRemoteViewsFactory.class.getSimpleName();

    private Context mContext;
    private BakingMadeEasyRepository mRepository = null;
    private List<TbRecipeEntity> mRecipes = null;

    /**
     *
     * @param applicationContext
     */
    public RecipeRemoteViewsFactory(Context applicationContext) {
        mContext = applicationContext;
        mRepository = BakingMadeEasyRepository.getInstance(applicationContext);
    }

    @Override
    public void onCreate() {
        Log.d(sTAG,"onCreate");
    }

    @Override
    public void onDataSetChanged() {
        Log.d(sTAG,"onDataSetChanged");
        updateRecipeList();
    }

    /**
     *
     */
    private void updateRecipeList() {
        Log.d(sTAG,"updateRecipeList");
        if (mRepository ==null)
            return;

        mRecipes = mRepository.mLocalRepository.mRecipesDao.getListOfAllRecipes();
    }

    @Override
    public void onDestroy() {
        Log.d(sTAG,"onDestroy");
    }

    @Override
    public int getCount() {

        if(mRecipes==null) {
            Log.d(sTAG,"getCount = NULL");
            return 0;
        }

        int count = mRecipes.size();
        Log.d(sTAG,"getCount = "+count);

        return count;
    }

    /**
     *
     * @param position
     * @return
     */
    @Override
    public RemoteViews getViewAt(int position) {
        Log.d(sTAG,"getViewAt pos "+position);

        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.favorite_recipes_widget_list_item);
        remoteViews.setTextViewText(R.id.txtRecipeName,mRecipes.get(position).getName());

        Intent fillInIntent = new Intent();
        fillInIntent.putExtra(Constants.sKEY_RECIPE_ID, mRecipes.get(position).getRecipeId());

        remoteViews.setOnClickFillInIntent(R.id.recipe_widget_item_container, fillInIntent);

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        Log.d(sTAG,"getLoadingView");
        return null;
    }

    @Override
    public int getViewTypeCount() {
        Log.d(sTAG,"getViewTypeCount");
        return 1;
    }

    @Override
    public long getItemId(int position) {
        Log.d(sTAG,"getItemId");
        return position;
    }

    @Override
    public boolean hasStableIds() {
        Log.d(sTAG,"hasStableIds");
        return true;
    }
}
