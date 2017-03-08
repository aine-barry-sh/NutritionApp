package ie.ul.csis.nutrition.user_interface.Dialogs;

import android.app.AlertDialog;
import android.content.Context;


/**
 * Created by aineb100 on 08/03/2017.
 */

public class NutritionDialog extends AlertDialog {

    protected Context context;
    protected AlertDialog dialog;

    protected NutritionDialog(Context context) {
        super(context);

        this.context = context;
        buildDialog();
    }

    public void showDialog() {
        dialog.show();
    }

    public void buildDialog() {
        //to be overridden
    }


}
