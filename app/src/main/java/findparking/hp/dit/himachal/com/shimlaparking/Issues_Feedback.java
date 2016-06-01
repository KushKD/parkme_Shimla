package findparking.hp.dit.himachal.com.shimlaparking;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class Issues_Feedback extends Activity {

   public String Parking_ID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issues__feedback);

        Bundle bundle = getIntent().getExtras();
        Parking_ID = bundle.getString("ID");

        Toast.makeText(getApplicationContext(),Parking_ID,Toast.LENGTH_LONG).show();

    }
}
