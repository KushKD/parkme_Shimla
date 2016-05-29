package findparking.hp.dit.himachal.com.shimlaparking;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
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
            capacity,
            latitude_person,
            longitude_person,
            contactperson1,
            contactperson2,
            contactperson3,
            contactphone1,
            contactphone2,
            contactphone3;


    private LinearLayout contactperson1_layout,contactperson2_layout,contactperson3_layout;

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
        latitude_person = (TextView)findViewById(R.id.latitude_person);
        longitude_person = (TextView)findViewById(R.id.longitude_person);
        contactperson1= (TextView)findViewById(R.id.contactperson1);
        contactperson2= (TextView)findViewById(R.id.contactperson2);
        contactperson3= (TextView)findViewById(R.id.contactperson3);
        contactphone1=(TextView)findViewById(R.id.contactphone1);
        contactphone2=(TextView)findViewById(R.id.contactphone2);
        contactphone3=(TextView)findViewById(R.id.contactphone3);
        contactperson1_layout = (LinearLayout)findViewById(R.id.contactperson1_layout);
        contactperson2_layout = (LinearLayout)findViewById(R.id.contactperson2_layout);
        contactperson3_layout = (LinearLayout)findViewById(R.id.contactperson3_layout);

        if(MArkerDetails.getContactPerson1().length()==0){
            contactperson1_layout.setVisibility(View.GONE);
        }

        if(MArkerDetails.getContactPerson2().length()==0){
            contactperson2_layout.setVisibility(View.GONE);
        }

        if(MArkerDetails.getContactPerson3().length()==0){
            contactperson3_layout.setVisibility(View.GONE);
        }


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
        latitude_person.setText(Double.toString(MArkerDetails.getLatitude_my_Location()));
        longitude_person.setText(Double.toString(MArkerDetails.getLongitude_my_Location()));

        contactperson1.setText(MArkerDetails.getContactPerson1());
        contactperson2.setText(MArkerDetails.getContactPerson2());
        contactperson3.setText(MArkerDetails.getContactPerson3());

        contactphone1.setText(MArkerDetails.getContactNumber1());
        contactphone2.setText(MArkerDetails.getContactNumber2());
        contactphone3.setText(MArkerDetails.getContactNumber3());





       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "http://maps.google.com/maps?f=d&hl=en&saddr="+Double.toString(MArkerDetails.getLatitude_my_Location())+","+Double.toString(MArkerDetails.getLongitude_my_Location())+"&daddr="+Double.toString(MArkerDetails.getLatitude())+","+Double.toString(MArkerDetails.getLongitude());
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(Intent.createChooser(intent, "com.Google.Android.apps.map"));
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Details_Parking.this.finish();
    }
}
