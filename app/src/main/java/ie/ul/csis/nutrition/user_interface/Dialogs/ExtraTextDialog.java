package ie.ul.csis.nutrition.user_interface.Dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

import ie.ul.csis.nutrition.utilities.StorageObject;

/**
 * Created by aineb100 on 08/03/2017.
 */

public class ExtraTextDialog extends PhotoInformationDialog {
    private StorageObject storageObject;

    public ExtraTextDialog(Context context) {
        super(context);
    }

    public void buildDialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        alert.setMessage("Any extra information?");

        // Seting an EditText view to get user input
        final EditText input = new EditText(super.context);
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

        super.dialog = alert.create();
    }

    public void setStorageObject(StorageObject storageObject) {
        this.storageObject = storageObject;
    }
}
