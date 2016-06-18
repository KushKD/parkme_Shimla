package findparking.hp.dit.himachal.com.shimlaparking;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;

import Utilities.Econstants;

public class SplashScreen_Activity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences settings = getSharedPreferences(Econstants.PREFRANCE_NAME, 0);
                //Get "hasLoggedIn" value. If the value doesn't exist yet false is returned
                boolean hasLoggedIn = settings.getBoolean("hasLoggedIn", false);

                if(hasLoggedIn)
                {
                    Intent mainIntent = new Intent(SplashScreen_Activity.this, Main_Activity.class);
                    SplashScreen_Activity.this.startActivity(mainIntent);
                    SplashScreen_Activity.this.finish();
                }else{
                    Intent loginIntent = new Intent(SplashScreen_Activity.this, Registration_Activity.class);
                    SplashScreen_Activity.this.startActivity(loginIntent);
                    SplashScreen_Activity.this.finish();

                }




            }
        }, 3000);
    }
    }

