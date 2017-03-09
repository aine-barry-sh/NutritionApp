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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Observer;
import java.util.Observable;

import api.dto.meals.FoodItemDto;
import api.dto.meals.MealsSaveImagesDto;
import ie.ul.csis.nutrition.R;
import ie.ul.csis.nutrition.threading.networking.AutomaticImageRequest;

import static ie.ul.csis.nutrition.utilities.Tools.isConnectedToInternet;

/**
 * Created by ruppe on 08/07/2016.
 * Modified by Aine
 */
public class ConnectivityObserver extends Activity implements Observer {

    private Context context;
    private String token;
    private SharedPreferences sharedPreferences;
    private int preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        //Register for network status updates


    }

    public ConnectivityObserver(Context context, String token) {

        this.context = context;
        this.token = token;
    }

    @Override
    public void update(Observable o, Object arg) {

        Log.d("ConnectivityObserver", "File found, checking connection");

        if (checkConnection()) {
            handleFiles();
        }

    }

    private boolean checkConnection() {

        sharedPreferences = context.getSharedPreferences(context.getString(R.string.sharedPerfs), MODE_PRIVATE);
        preference = sharedPreferences.getInt("wifiOnly",1);
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

//                    break;
//                }
//
//
//            }
//        }
//    }


    private void handleFiles() {
        Log.d("ConnectivityObserver", "Attempting upload of picture");
        File directory = (File) context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        Boolean keepGoing = true;
        if (directory.isDirectory()) {
            String [] stringFiles = directory.list();
            ArrayList<String> files = new ArrayList<String>(Arrays.asList(stringFiles));

            for (int i =0; i<files.size(); i++) {
                File myFile = new File(directory.getPath() + "/" + files.get(i));
                if (isTXT(myFile)) {
                    //continue to next and do nothing
                    Log.d("ConnecvitiyObserver", "Is text file " + myFile.getName());
                } else if (isJPG(myFile)) {
                    String infoVersion = getTextFile(files.get(i));
                    if (files.contains(infoVersion)) {
                        try {
                            String info = getInfoFromInfoFile(infoVersion);
                            sendPicture(myFile, info);
                        } catch (IOException e) {
                            Log.d("ConnectivityObserver", "IOException while searching for " + infoVersion);
                        } catch(Exception e) {
                            Log.d("ConnectivityObserver","Exception for " + infoVersion);
                        }
                    } else {
                        sendPicture(myFile);
                    }
                }
                if (!myFile.exists()) {
                    break;
                }
            }
        }
    }
    private boolean sendPicture(File imgFile, String infoString) {

        FoodItemDto foodItemDto = new FoodItemDto(infoString);
        MealsSaveImagesDto dto = new MealsSaveImagesDto(null, new FoodItemDto[]{foodItemDto}, new File[]{imgFile});
        AutomaticImageRequest request = new AutomaticImageRequest(context, token, imgFile);
        request.execute(dto);
        return false;
    }

    private String getInfoFromInfoFile(String infoFilePath) throws IOException {
        String results = "";
        try {
            File myFile = new File(infoFilePath);
            FileReader fileReader = new FileReader(myFile);
            BufferedReader reader = new BufferedReader(fileReader);

            String line = "";

            while ((line = reader.readLine()) != null) {
                results += " " + line;
            }

            reader.close();
            fileReader.close();
            myFile.delete();

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

        String returnString = "";

        for (int i =0; i<filenameArray.length-1; i++) {
            returnString += filenameArray[i] + ".";
        }
        return returnString + "txt";
    }



}
