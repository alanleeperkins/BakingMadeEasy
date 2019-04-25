package alpitsolutions.com.bakingmadeeasy.utility;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import alpitsolutions.com.bakingmadeeasy.R;
import static android.content.Context.WINDOW_SERVICE;

public class Helpers {

    private static final Map<String,Integer> sMeasureTypes;

    static {
        Map<String,Integer> map = new HashMap();
        map.put("CUP",R.string.measure_unit_cup);
        map.put("TSP",R.string.measure_unit_tsp);
        map.put("TBLSP",R.string.measure_unit_tbsp);
        map.put("G",R.string.measure_unit_g);
        map.put("K",R.string.measure_unit_k);
        map.put("UNIT",R.string.measure_unit_unit);

        sMeasureTypes = Collections.unmodifiableMap(map);
    }

    /**
     * set the requested screen orientation
     * @param context
     */
    public static void setScreenOrientation(Activity context) {
        // make sure we have the right screen orientation set based on the screen width
        if (context.getResources().getBoolean(R.bool.is_portrait_only)) {
            context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else  if (context.getResources().getBoolean(R.bool.is_landscape_only)) {
            context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    /**
     * detects the tablet screen based on the screen width
     * @param context
     * @return
     */
    public static boolean isTabletScreenActive(Activity context)
    {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        int widthInDP = Math.round(dm.widthPixels / dm.density);
        if (widthInDP>=600)
            return true;
        else
            return false;
    }


    /**
     *
     * @param quantity
     * @param unit
     * @return
     */
    public static String getQuantityMeasureString(Double quantity, String unit)
    {

        String output = String.format("%.2f %s",quantity,unit );

        return output;
    }


    /**
     *
     * @param context
     * @param measureKey
     * @return
     */
    public static String getMeasureUnit(Activity context, String measureKey)
    {
        String measure;
        if (sMeasureTypes.containsKey(measureKey))
            measure = context.getText(sMeasureTypes.get(measureKey)).toString();
        else
            measure = measureKey;

        return measure;
    }

    /**
     *
     * @param videoUrl
     * @return
     */
    public static boolean isLegalVideoUrl(String videoUrl) {

        if (videoUrl.endsWith(".mp4"))
            return true;
        if (videoUrl.endsWith(".mkv"))
            return true;

        return false;
    }


    /**
     * Method that checks if there is a valid network connection
     * @param context
     * @return
     */
    public static boolean checkConnection(Context context){
        final ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm != null) {
            if (Build.VERSION.SDK_INT < 23) {
                final NetworkInfo ni = cm.getActiveNetworkInfo();

                if (ni != null) {
                    return (ni.isConnected() && (ni.getType() == ConnectivityManager.TYPE_WIFI || ni.getType() == ConnectivityManager.TYPE_MOBILE));
                }
            } else {
                final Network n = cm.getActiveNetwork();

                if (n != null) {
                    final NetworkCapabilities nc = cm.getNetworkCapabilities(n);

                    return (nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI));
                }
            }
        }

        return false;
    }

}
