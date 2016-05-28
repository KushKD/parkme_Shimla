package findparking.hp.dit.himachal.com.shimlaparking;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

              /*  try {
                  //  check_FileSystem FileCheck = new check_FileSystem();
                 //   FileCheck.execute(Folder_name);
                }catch(Exception ex){
                    Log.e("Error","While Executing ASYNC Task");
                }*/

                //SharedPreferences settings = getSharedPreferences(EConstants.PREFS_NAME, 0);
                //Get "hasLoggedIn" value. If the value doesn't exist yet false is returned
              //  boolean hasLoggedIn = settings.getBoolean("hasLoggedIn", false);

              //  if(hasLoggedIn)
              //  {
                    Intent mainIntent = new Intent(SplashScreen.this, MainMapsActivity.class);
                    SplashScreen.this.startActivity(mainIntent);
                    SplashScreen.this.finish();
               // }else{
              //      Intent loginIntent = new Intent(SplashScreen.this, LogInActivity.class);
               //     SplashScreen.this.startActivity(loginIntent);
               //     SplashScreen.this.finish();

               // }




            }
        }, 3000);
    }
    }

