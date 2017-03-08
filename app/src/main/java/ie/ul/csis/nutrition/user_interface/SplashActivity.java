package ie.ul.csis.nutrition.user_interface;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import ie.ul.csis.nutrition.R;
import ie.ul.csis.nutrition.utilities.Tools;

/**
 * 
 */
public class SplashActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        context = this;

        preferences = getSharedPreferences(context.getString(R.string.sharedPerfs), MODE_PRIVATE);

        if(preferences.getBoolean(context.getString(R.string.rememberMeKey), false))  {
            Log.d("SplashActivity", preferences.getString("token", "no token found"));


            Long expiryTime = preferences.getLong(context.getString(R.string.tokenExpiryTimeKey), 0);

            if(System.currentTimeMillis()<expiryTime) {
                context.startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
                return;
            } else {
                context.startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                Tools.toast(context, "Token out of date. Please re login");
                finish();
                return;
            }
        } else {
            context.startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            finish();
            return;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
