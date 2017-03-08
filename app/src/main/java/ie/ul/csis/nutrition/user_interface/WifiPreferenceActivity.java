package ie.ul.csis.nutrition.user_interface;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;

import ie.ul.csis.nutrition.R;
import ie.ul.csis.nutrition.utilities.Tools;

public class WifiPreferenceActivity extends AppCompatActivity {


    private RadioButton wifiOnlyButton;
    private RadioButton allUploadButton;
    private Button submitButton;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_preference);


        wifiOnlyButton = (RadioButton) findViewById(R.id.wifiOnlyRadioButton);
        allUploadButton = (RadioButton) findViewById(R.id.allUploadRadioButton);
        submitButton = (Button) findViewById(R.id.submitButton);

        configureButtons();

        sharedPreferences = getSharedPreferences("shared_prefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }

    private void configureButtons() {
        submitButton.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                if (wifiOnlyButton.isChecked()) {
                    editor.putInt("uploadPreference", 0);
                    editor.commit();
                }
                if (allUploadButton.isChecked()) {
                    editor.putInt("uploadPreference", 1);
                    editor.commit();
                }

                Tools.toast(getBaseContext(), "Updated");
            }
        });
    }
}
