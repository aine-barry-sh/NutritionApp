package ie.ul.csis.nutrition.threading.networking;

import android.content.Context;
import android.os.AsyncTask;

import api.dto.meals.MealsSaveImagesDto;
import ie.ul.csis.nutrition.Request.UploadImageMeal;
import ie.ul.csis.nutrition.utilities.Tools;

import android.util.Log;

import java.io.File;

/**
 * Created by aineb100 on 03/01/2017.
 */

public class AutomaticImageRequest extends AsyncTask<MealsSaveImagesDto, Void, UploadImageMeal> {

    private Context context;

    private String token;
    private File imgFile;

    public AutomaticImageRequest(Context context, String token, File imgFile) {
        this.context = context;
        this.token = token;
        this.imgFile = imgFile;
    }

    @Override
    protected UploadImageMeal doInBackground(MealsSaveImagesDto... dto) {
        //todo add getting wifi preference
        if(dto.length ==0 || !Tools.isConnectedToInternet(context, 0)) {
            Log.d("AutomaticImageRequest", "Upload failed due to no object being sent or lack of connection");
            return null;
        }

        try {
            UploadImageMeal meal = new UploadImageMeal();
            meal.setToken(token);
            meal.request(dto[0]);
            return meal;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onPostExecute(UploadImageMeal meal) {
        if(meal == null) {
            Log.d("AutomaticImageRequest", "Upload failed: no response" );
            return;
        }

        if (meal.getResponseCode() == meal.getExpectedResponceCode()) {
            Log.d("AutomaticImageRequest", "Image uploaded");
            boolean deleteSuccess = imgFile.delete();
            Log.d("AutomaticImageRequest", "Picture deleted: " + deleteSuccess);
        } else {
            Log.d("AutomaticImageRequest", "Upload failed: " + meal.getResponseMessage()    );
        }
    }
}
