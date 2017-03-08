package ie.ul.csis.nutrition.user_interface.Uploaders;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Observer;
import java.util.Observable;

import api.dto.meals.FoodItemDto;
import api.dto.meals.MealsSaveImagesDto;
import ie.ul.csis.nutrition.R;
import ie.ul.csis.nutrition.threading.networking.AutomaticImageRequest;
import ie.ul.csis.nutrition.threading.networking.SaveImagesRequest;
import ie.ul.csis.nutrition.utilities.Tools;

import static ie.ul.csis.nutrition.utilities.Tools.isConnectedToInternet;

/**
 * Created by ruppe on 08/07/2016.
 * Modified by Aine
 */
public class ConnectivityObserver extends Activity implements Observer {

    private Context context;
    private String token;
    private int preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.offline_mode_layout);

        //Register for network status updates

        SharedPreferences sharedPreferences = getSharedPreferences(context.getString(R.string.sharedPerfs), MODE_PRIVATE);
        preference = sharedPreferences.getInt("wifiOnly",-1);

    }

    public ConnectivityObserver(Context context, String token) {

        this.context = context;
        this.token = token;
    }

    @Override
    public void update(Observable o, Object arg) {

        //Log.d("ConnectivityObserver", "Connection Established, Uploading Points!");
        Log.d("ConnectivityObserver", "File found, checking connection");

        if (checkConnection()) {
            handlePictures();
        }

    }

    private boolean checkConnection() {

        boolean connection = isConnectedToInternet(context, preference);
        if (connection) {
            Log.d("ConnectivityObserver", "Connetion Detected");
        } else {
            Log.d("ConnectivityObserver", "No connection. Restarting process");
        }

        return connection;
    }

//    private void handlePictures() {
//
//        //returns absolute paths to where the pictures can be stored
//        File file = (File) context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//
//        boolean success = false;
//        File tempFile = new File("dummy file");
//
//        if (file.isDirectory()) {
//            String [] files = file.list();
//            for (String filePath : files) {
//                tempFile = new File(filePath);
//                sendPicture(tempFile);
//                if (tempFile.exists() || tempFile.isDirectory()) {
//                    //if this condition is met, the deletion of the file was not successful
//                    //so the request itself was not successful.
//                    //break cycle and continue later.
//                    break;
//                }
//
//
//            }
//        }
//    }

    private void handlePictures() {
        Log.d("ConnectivityObserver", "Attempting upload of picture");
        File file = (File) context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (file.isDirectory()) {
            String [] files = file.list();
            File fileOne = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath() + "/" + files[0]);
            Log.d("ConnectivityObserver", "Image loaded from cache");
            sendPicture(fileOne);
        }
    }

    private void handleFiles() {
        Log.d("ConnectivityObserver", "Attempting upload of picture");
        File directory = (File) context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        Boolean keepGoing = true;
        if (directory.isDirectory()) {
            String [] files = directory.list();
            for (int i =0; keepGoing; i++) {
                File myFile = new File(directory.getPath() + "/" + files[i]);
                if (isJPG(myFile)) {
                    String infoVersion = getTextFile(files[i]);
                    if (Arrays.asList(files).contains(infoVersion)) {
                        sendPicture(myFile, directory.getPath() + "/" + infoVersion);
                    }
                    keepGoing = false;
                }
            }
        }
    }

    private boolean sendPicture(File imgFile, String infoFilePath) {

        String infoString = "";
        try {
            infoString = getInfoFromInfoFile(infoFilePath);
        } catch(IOException e) {
            infoString = "";
        }


        //MealsSaveImagesDto dto = new MealsSaveImagesDto(null, null, new File[]{imgFile});
        FoodItemDto foodItemDto = new FoodItemDto(infoString);
        MealsSaveImagesDto dto = new MealsSaveImagesDto(null, new FoodItemDto[] {foodItemDto}, new File[]{imgFile});
        AutomaticImageRequest request = new AutomaticImageRequest(context, token, imgFile);
        request.execute(dto);
        return false;
    }

    private String getInfoFromInfoFile(String infoFilePath) throws IOException {
        String results = "";
        try {
            FileReader fileReader = new FileReader(infoFilePath);
            BufferedReader reader = new BufferedReader(fileReader);

            String line = "";

            while ((line = reader.readLine()) != null) {
                results += " " + line;
            }

            reader.close();
        } catch (Exception e) {
            results = "";
        }



        return results;
    }

    private boolean sendPicture(File imgFile) {
        MealsSaveImagesDto dto = new MealsSaveImagesDto(null, null, new File[]{imgFile});
        AutomaticImageRequest request = new AutomaticImageRequest(context, token, imgFile);
        request.execute(dto);
        return false;
    }


//    private String getExtraString(imgFile) {
//
//    }

    private boolean isJPG(File testFile) {
        String filename = testFile.getName();
        String filenameArray[] = filename.split("\\.");
        String extension = filenameArray[filenameArray.length-1];
        if (extension.equalsIgnoreCase("jpg")) {
            return true;
        }
        return false;
    }
    private boolean isTXT(File testFile) {
        String filename = testFile.getName();
        String filenameArray[] = filename.split("\\.");
        String extension = filenameArray[filenameArray.length-1];
        if (extension.equalsIgnoreCase("txt")) {
            return true;
        }
        return false;
    }

    private String getTextFile(String imageFileName) {
        String filenameArray[] = imageFileName.split("\\.");
        //by all conventions to date, the first part should be the file name and path,
        //the second part should be the extension
        //so just replace that with txt
        //happy days

        return filenameArray[0] + ".txt";
    }



}
