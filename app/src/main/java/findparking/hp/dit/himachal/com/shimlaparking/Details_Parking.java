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

    private TextView
            parking_place,
            parking_area,
            parking_availability,
            remarks,
            suitedfor,
            thrash_hold_value,
            smallcarsfare,
            bigcarsfare,
            parking_time,
            identifier,
            latitude,
            longitude,
            capacity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details__parking);

        Intent getRoomDetailsIntent = getIntent();
        final Sending_Object_All_details MArkerDetails =  (Sending_Object_All_details) getRoomDetailsIntent.getSerializableExtra("DETAILS_ALL");

                parking_place = (TextView)findViewById(R.id.parking_place);
                parking_area = (TextView)findViewById(R.id.parking_area);
                parking_availability = (TextView)findViewById(R.id.parking_availability);
                remarks = (TextView)findViewById(R.id.remarks);
                suitedfor= (TextView)findViewById(R.id.suitedfor);
                thrash_hold_value = (TextView)findViewById(R.id.thrash_hold_value);
                smallcarsfare = (TextView)findViewById(R.id.smallcarsfare);
                bigcarsfare = (TextView)findViewById(R.id.bigcarsfare);
                parking_time = (TextView)findViewById(R.id.parking_time);
                identifier = (TextView)findViewById(R.id.identifier);
                latitude = (TextView)findViewById(R.id.latitude);
                longitude = (TextView)findViewById(R.id.longitude);
                capacity = (TextView)findViewById(R.id.capacity);



        parking_place.setText(MArkerDetails.getParkingPlace());
        parking_area.setText(MArkerDetails.getParkingArea());
        parking_availability.setText(MArkerDetails.getParkingFullTag());
        remarks.setText(MArkerDetails.getRemarks());
        suitedfor.setText(MArkerDetails.getSutedFor());
        thrash_hold_value.setText(MArkerDetails.getThrashholdValue());
        smallcarsfare.setText(MArkerDetails.getMinimumParkingFeeSmallCar());
        bigcarsfare.setText(MArkerDetails.getMinimumParkingFeebigCar());

        parking_time.setText(MArkerDetails.getMinimumParkingTime());
        identifier.setText(MArkerDetails.getIdentifier());
        latitude.setText(Double.toString(MArkerDetails.getLatitude()));
        longitude.setText(Double.toString(MArkerDetails.getLongitude()));
        capacity.setText(MArkerDetails.getCapacity());





       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

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
