package findparking.hp.dit.himachal.com.shimlaparking;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class Details_Parking extends AppCompatActivity {

    private TextView tv_label,tv_icon, tv_lat, tv_long;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details__parking);
        Intent intent = getIntent();
        String mLabel = intent.getExtras().getString("mLabel");
        String mIcon = intent.getExtras().getString("mIcon");
        double mLatitude = intent.getExtras().getDouble("mLatitude");
        double mLongitude = intent.getExtras().getDouble("mLongitude");

        tv_icon = (TextView)findViewById(R.id.icon);
        tv_label = (TextView)findViewById(R.id.label);
        tv_lat = (TextView)findViewById(R.id.lat);
        tv_long = (TextView)findViewById(R.id.ong);

        tv_icon.setText(mIcon);
        tv_label.setText(mLabel);
        tv_lat.setText(Double.toString(mLatitude));
        tv_long.setText(Double.toString(mLongitude));




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Details_Parking.this.finish();
    }
}
