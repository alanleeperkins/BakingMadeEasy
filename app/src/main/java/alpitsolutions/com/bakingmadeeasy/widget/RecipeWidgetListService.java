package alpitsolutions.com.bakingmadeeasy.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

import alpitsolutions.com.bakingmadeeasy.utility.Constants;

public class RecipeWidgetListService extends RemoteViewsService {

    private static final String sTAG = Constants.sTAG_FILTER + RecipeWidgetListService.class.getSimpleName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RecipeRemoteViewsFactory(this.getApplicationContext());
    }
}
