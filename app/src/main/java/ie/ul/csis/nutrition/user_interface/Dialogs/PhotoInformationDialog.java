package ie.ul.csis.nutrition.user_interface.Dialogs;

import android.content.Context;

import ie.ul.csis.nutrition.utilities.StorageObject;

/**
 * Created by aineb100 on 08/03/2017.
 */

public class PhotoInformationDialog extends NutritionDialog {

    protected StorageObject storageObject;

    protected PhotoInformationDialog(Context context) {
        super(context);
    }

    public void setStorageObject(StorageObject storageObject) {
        this.storageObject = storageObject;
    }
}
