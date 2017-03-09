package ie.ul.csis.nutrition.utilities;

import android.content.Context;

import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import ie.ul.csis.nutrition.R;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by Jonathan on 13/09/2015.
 */
public  class Tools {

    public static void log(final String className, final String methodName, final String message){
        Log.d("MyApp", "   Class: " + className +
                       "   Method: " + methodName +
                       "   Message: " + message);
    }

    public static void log( final String message){
        Log.d("MyApp", "   Message: " + message);
    }

    public static void toast( final Context context, final String message){

        Handler handler = new Handler(Looper.getMainLooper());

        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        });
    }

    public static boolean hasInternetConnection(Context context)
    {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if ( activeNetworkInfo != null
//                && activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI
                && activeNetworkInfo.isConnected())
        {
            return true;
        }

        toast(context, "No connection");
        return false;

    }

    public static boolean isConnectedToInternet(Context context, int preference) {
        //if preference == 0, only wifi. If preference == 1, wifi or mobile connection

        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPerfs), MODE_PRIVATE);
        preference = sharedPreferences.getInt("wifiOnly",1);
        ArrayList<Integer> acceptableNetworkTypes = new ArrayList<Integer>();
        acceptableNetworkTypes.add(ConnectivityManager.TYPE_WIFI);
        if (preference == 1) {
            acceptableNetworkTypes.add(ConnectivityManager.TYPE_MOBILE);
        }

        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
        for (int networkType : acceptableNetworkTypes) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (activeNetwork != null && activeNetwork.getType() == networkType) {

                return true;
            }
        }
        return false;
    }


}
