package ie.ul.csis.nutrition.user_interface;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import ie.ul.csis.nutrition.R;

public class InternetInformationActivity extends AppCompatActivity {

    private RadioButton wifiAndDataButton;
    private Button submitButton;

    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet_information);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        wifiAndDataButton = (RadioButton) findViewById(R.id.radioButton4);

        submitButton = (Button) findViewById(R.id.internetSubmitButton);

        configureButtons();
    }

    @Override
    public void onResume() {

        preferences = getSharedPreferences(getString(R.string.sharedPerfs), MODE_PRIVATE);
        checkMemory();

    }

    private void configureButtons() {
        submitButton.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                submitButtonClicked();
            }
        });

    }

    private void submitButtonClicked() {

        int choice =0;

        if (wifiAndDataButton.isChecked()) {
            choice = 1;
        }
        SharedPreferences.Editor editor = preferences.edit();
        Log.d("InternetDialog", "preference: " + choice);
        editor.putInt("wifiOnly", choice);
        editor.commit();
        changeToMainActivity();

    }

    private void checkMemory() {
        ActivityManager.MemoryInfo memoryInfo = getAvailableMemory();

        if(memoryInfo.lowMemory) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.low_memory_message);
            builder.setTitle(R.string.low_memory_title);
            builder.setCancelable(false);
            builder.create().show();
        }
    }

    private ActivityManager.MemoryInfo getAvailableMemory() {

        ActivityManager activityManager = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo;
    }


    public void changeToMainActivity()
    {
        startActivity(new Intent(InternetInformationActivity.this, MainActivity.class));
        finish();
    }







}
