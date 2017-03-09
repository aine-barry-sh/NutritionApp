package ie.ul.csis.nutrition.user_interface.Dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

import ie.ul.csis.nutrition.utilities.StorageObject;

/**
 * Created by aineb100 on 08/03/2017.
 */

public class DialogManager {
    private Context context;
    private StorageObject storageObject;
    private AlertDialog mealPickerDialog;
    private AlertDialog extraTextDialog;
    public DialogManager(Context context, StorageObject storageObject) {
        this.context = context;
        this.storageObject = storageObject;
        buildMealPicker();
        buildExtraText();
        showMealPicker();

    }


    private void buildMealPicker() {
        final String [] mealTypes = {"Breakfast", "Lunch", "Dinner", "Snack", "Pre-workout", "Post-workout", "Other"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("What type of meal was this?");
        builder.setItems(mealTypes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                storageObject.setMealType(mealTypes[item]);
                showExtraText();
            }
        });
        mealPickerDialog = builder.create();
    }
    private void showMealPicker() {
        mealPickerDialog.show();
    }

    private void buildExtraText() {

        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        alert.setMessage("Any extra information?");

        // Seting an EditText view to get user input
        final EditText input = new EditText(context);
        alert.setView(input);

        alert.setPositiveButton("Send with this text", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                storageObject.setExtraInfo(input.getText().toString());
                storageObject.toFile();
            }
        });

        alert.setNegativeButton("Send without text", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                storageObject.toFile();
            }
        });

        extraTextDialog = alert.create();
    }
    private void showExtraText() {
        extraTextDialog.show();
    }
}
