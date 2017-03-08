package ie.ul.csis.nutrition.utilities;

import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by aineb100 on 02/03/2017.
 */

public class StorageObject {
    private String imageName;
    private String mealType;
    private String extraInfo;
    private File directory;

    public StorageObject(String imageName, String mealType, String extraInfo) {
        this.imageName = imageName;
        this.mealType = mealType;
        this.extraInfo = extraInfo;
    }
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
    public void setMealType(String mealType) {
        this.mealType = mealType;
    }
    public void setExtraInfo(String extraInfo) {
        this.extraInfo = extraInfo;
    }
    public void setFilePath (File directory) {
        this.directory = directory;
    }

    public StorageObject() {
        //
    }

    public String toString()
    {
        return new Gson().toJson(this).toString();
    }

    public void toFile() {
        try {
            File infoFile = File.createTempFile(
                    imageName,  /* prefix */
                    ".txt",         /* suffix */
                    directory      /* directory */
            );
            PrintWriter printWriter = new PrintWriter(new FileOutputStream(
                    infoFile,
                    true /* append = true */));
            printWriter.write(toString());
            printWriter.close();
        } catch (IOException e) {
            Log.d("SelectionFragment", e.toString());
        }
    }


}
