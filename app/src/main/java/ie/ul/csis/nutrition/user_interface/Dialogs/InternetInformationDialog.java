package ie.ul.csis.nutrition.user_interface.Dialogs;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;

import ie.ul.csis.nutrition.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by aineb100 on 08/03/2017.
 */

public class InternetInformationDialog extends NutritionDialog {


    private SharedPreferences preferences;

    public InternetInformationDialog(Context context) {
        super(context);
        preferences = context.getSharedPreferences(context.getString(R.string.sharedPerfs), MODE_PRIVATE);
    }

    public void buildDialog() {

        final String [] options = {"Wifi only", "Data and Wifi"};
        AlertDialog.Builder builder = new AlertDialog.Builder(super.context);
        builder.setTitle("Make your selection");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                setWifiPreference(item);
            }
        });
        builder.setCancelable(false);
        super.dialog = builder.create();


    }

    private void setWifiPreference(int choice) {
        SharedPreferences.Editor editor = preferences.edit();
        Log.d("InternetDialog", "preference: " + choice);
        editor.putInt("wifiOnly", choice);
        editor.commit();
    }




}
