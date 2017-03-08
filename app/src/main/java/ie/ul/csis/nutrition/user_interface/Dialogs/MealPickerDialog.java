package ie.ul.csis.nutrition.user_interface.Dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.app.AlertDialog;

/**
 * Created by aineb100 on 23/02/2017.
 */

public class MealPickerDialog extends PhotoInformationDialog{

    private String mealType;

    public MealPickerDialog(Context context) {
        super(context);
    }

    public void buildDialog() {
        final String [] mealTypes = {"Breakfast", "Lunch", "Dinner", "Snack", "Pre-workout", "Post-workout", "Other"};
        PhotoInformationDialog newDialog = new ExtraTextDialog(super.context);
        AlertDialog.Builder builder = new AlertDialog.Builder(super.context);
        builder.setTitle("What type of meal was this?");
        builder.setItems(mealTypes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                mealType = mealTypes[item];
                storageObject.setMealType(mealType);
                newDialog.show();
            }
        });
        super.dialog = builder.create();

    }

    public String getMealType() {

        return this.mealType;
    }
}
