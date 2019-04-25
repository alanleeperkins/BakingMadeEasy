package alpitsolutions.com.bakingmadeeasy.widget;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.List;

import alpitsolutions.com.bakingmadeeasy.R;
import alpitsolutions.com.bakingmadeeasy.RecipeOverviewActivity;
import alpitsolutions.com.bakingmadeeasy.RecipesOverviewActivity;
import alpitsolutions.com.bakingmadeeasy.database.TbRecipeEntity;
import alpitsolutions.com.bakingmadeeasy.repositories.BakingMadeEasyRepository;
import alpitsolutions.com.bakingmadeeasy.repositories.RecipesLocalRepository;
import alpitsolutions.com.bakingmadeeasy.utility.Constants;

public class RecipeWidgetProvider extends AppWidgetProvider {

    private static final String TAG = Constants.TAG_FILTER + RecipeWidgetProvider.class.getSimpleName();

    /**
     *
     * @param context
     * @param appWidgetManager
     * @param appWidgetId
     * @param modelPosition
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, int modelPosition) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.favorite_recipes_widget);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.favorite_recipes_widget);
            Intent intent = new Intent(context, RecipeWidgetListService.class);
            views.setRemoteAdapter(R.id.lvwRecipeNameList, intent);

            Intent clickIntentTemplateSingleRecipe = new Intent(context, RecipesOverviewActivity.class);
            PendingIntent clickPendingIntentTemplateSingleRecipe = TaskStackBuilder.create(context)
                    .addNextIntentWithParentStack(clickIntentTemplateSingleRecipe)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.lvwRecipeNameList, clickPendingIntentTemplateSingleRecipe);
            views.setEmptyView(R.id.lvwRecipeNameList,R.id.txtWidgetInfoNoFavorites);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }

        Log.d(TAG, "onUpdate");
    }

    /**
     * sends a refresh broadcast, so the widgets always show the actual data
     * @param context
     */
    public static void sendRefreshBroadcast(Context context) {
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.setComponent(new ComponentName(context, RecipeWidgetProvider.class));
        context.sendBroadcast(intent);
        Log.d(TAG, "sendRefreshBroadcast AppWidgetManager.ACTION_APPWIDGET_UPDATE");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        Log.d(TAG, "onReceive (we received an callback [action='"+ action +"'])");

        if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            // refresh our widgets
            AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            ComponentName cn = new ComponentName(context, RecipeWidgetProvider.class);
            mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.lvwRecipeNameList);
        }

        super.onReceive(context, intent);
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
        Log.d(TAG, "onAppWidgetOptionsChanged");
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Log.d(TAG, "onDeleted (One Instance removed)");
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.d(TAG, "onEnabled (First Instance created)");
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Log.d(TAG, "onDisabled (Last Instance deleted)");
    }

    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        super.onRestored(context, oldWidgetIds, newWidgetIds);
        Log.d(TAG, "onRestored");
    }
}
