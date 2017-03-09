package ie.ul.csis.nutrition.user_interface;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import ie.ul.csis.nutrition.R;
import ie.ul.csis.nutrition.threading.networking.LogoutRequest;
import ie.ul.csis.nutrition.user_interface.Dialogs.DialogManager;
import ie.ul.csis.nutrition.user_interface.Dialogs.InternetInformationDialog;
import ie.ul.csis.nutrition.user_interface.Dialogs.NutritionDialog;
import ie.ul.csis.nutrition.user_interface.Fragments.ConfirmationFragment;
import ie.ul.csis.nutrition.user_interface.Fragments.SelectionFragment;
import ie.ul.csis.nutrition.user_interface.Uploaders.ConnectivityObserver;
import ie.ul.csis.nutrition.user_interface.Uploaders.FileStatus;
import ie.ul.csis.nutrition.utilities.StorageObject;
import ie.ul.csis.nutrition.utilities.Tools;
import java.io.File;


public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private File imageFile;
    private Context context;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        context = this;
        imageView = (ImageView) findViewById(R.id.displayImage);
        imageFile = null;
        pDialog = new ProgressDialog(context);
        pDialog.setTitle("Uploading");
        pDialog.setMessage("Please Wait...");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);

        getPermissions();
        updateFragment();
        uploadPreferences();
    }

    @Override
    public void onResume() {
        super.onResume();
        initializeBackgroundProgress();
    }

    private void uploadPreferences() {

        SharedPreferences preferences = getSharedPreferences(context.getString(R.string.sharedPerfs), MODE_PRIVATE);
        if (preferences.getInt("wifiOnly", -1) == -1) {
            NutritionDialog internetInformationDialog = new InternetInformationDialog(context);
            internetInformationDialog.showDialog();
        }
    }
    private void initializeBackgroundProgress() {
        FileStatus fileChecker = new FileStatus(this);
        Thread fileCheckerThread = new Thread(fileChecker);

        ConnectivityObserver fileObserver = new ConnectivityObserver(this, getToken());
        SharedPreferences sharedPreferences = getSharedPreferences(context.getString(R.string.sharedPerfs), MODE_PRIVATE);
        int preference = sharedPreferences.getInt("wifiOnly",-1);

        fileChecker.addObserver(fileObserver);
        fileCheckerThread.start();
    }


    private void getPermissions() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
                    &&
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.ACCESS_FINE_LOCATION},10);

                return;
            }
        }
    }

    public void updateFragment() {

        Fragment confirmFrag = new ConfirmationFragment();
        Fragment selectFrag = new SelectionFragment();

        FragmentManager fm = getSupportFragmentManager();

        if (imageView.getDrawable() == null) {
            fm.beginTransaction()
                    .replace(R.id.contentFragment, selectFrag)
                    .commit();
        } else {
            fm.beginTransaction()
                    .replace(R.id.contentFragment, confirmFrag)
                    .commit();
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if(requestCode == 10) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                //yay
            }
        }
    }

    public void SendImage() {

        getExtraInfo();
        imageView.setImageResource(0);
        updateFragment();
    }

    private void getExtraInfo() {
        StorageObject storageObject = new StorageObject();
        storageObject.setFilePath(getExternalFilesDir(Environment.DIRECTORY_PICTURES));
        storageObject.setImageName(imageFile.getName());
        DialogManager dialogManager = new DialogManager(context, storageObject);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id)
        {
            case R.id.logout:
                logout();
                context.startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                break;
            default:
                Tools.toast(context, "unknown menu Item");
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    public void rejectPhoto() {
        if (imageFile != null) {
            imageFile.delete();
        }
        imageView.setImageResource(0);
        updateFragment();

    }

    private void logout(){

        SharedPreferences preferences =
                getSharedPreferences(context.getString(R.string.sharedPerfs), MODE_PRIVATE);

        LogoutRequest logout = new LogoutRequest(this,
                preferences.getString(context.getString(R.string.tokenKey), "") );
        logout.execute();


        SharedPreferences.Editor  editor = preferences.edit();
        editor.remove(context.getString(R.string.tokenKey));
        editor.remove(context.getString(R.string.tokenExpiryTimeKey));
        editor.remove(context.getString(R.string.rememberMeKey));
        editor.remove("wifiOnly");
        editor.apply();
    }


    public File getImageFile(){return  imageFile;}

    public ProgressDialog getProgressDialog(){
        return pDialog;
    }

    public ImageView getImageView(){return imageView;}


    private String getToken() {
        SharedPreferences preferences =
                getSharedPreferences(getString(R.string.sharedPerfs), MODE_PRIVATE);
        return preferences.getString(getString(R.string.tokenKey), "");
    }

    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }

}