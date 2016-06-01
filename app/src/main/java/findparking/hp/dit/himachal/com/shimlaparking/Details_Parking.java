package findparking.hp.dit.himachal.com.shimlaparking;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
            contactphone3,
            parking_id;


    private LinearLayout contactperson1_layout,contactperson2_layout,contactperson3_layout;
    private Button call1 , call2,call3,get_directions , rates , issues;
    final Context context = this;
    private static final int PERMISSION_REQUEST_CODE = 1;
    Sending_Object_All_details MArkerDetails = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details__parking);

        try{

        Intent getRoomDetailsIntent = getIntent();
         MArkerDetails =  (Sending_Object_All_details) getRoomDetailsIntent.getSerializableExtra("DETAILS_ALL");

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
        call1 = (Button)findViewById(R.id.call1);
        call2 = (Button)findViewById(R.id.call2);
        call3 = (Button)findViewById(R.id.call3);
        parking_id = (TextView)findViewById(R.id.parkingid);
        get_directions = (Button)findViewById(R.id.get_directions);
        rates = (Button)findViewById(R.id.rates);
        issues = (Button)findViewById(R.id.issues);



            if(MArkerDetails.getContactPerson1().length()==0){
                contactperson1_layout.setVisibility(View.GONE);
            }

            if(MArkerDetails.getContactPerson2().length()==0){
                contactperson2_layout.setVisibility(View.GONE);
            }

            if(MArkerDetails.getContactPerson3().length()==0){
                contactperson3_layout.setVisibility(View.GONE);
            }
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

            contactphone1.setText("+91"+MArkerDetails.getContactNumber1());
            contactphone2.setText("+91"+MArkerDetails.getContactNumber2());
            contactphone3.setText("+91"+MArkerDetails.getContactNumber3());
            parking_id.setText(MArkerDetails.getParkingId());



        issues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             Intent i = new Intent(Details_Parking.this, Issues_Feedback.class);
                i.putExtra("ID",MArkerDetails.getParkingId());
                startActivity(i);


            }
        });


        get_directions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri = "http://maps.google.com/maps?f=d&hl=en&saddr="+Double.toString(MArkerDetails.getLatitude_my_Location())+","+Double.toString(MArkerDetails.getLongitude_my_Location())+"&daddr="+Double.toString(MArkerDetails.getLatitude())+","+Double.toString(MArkerDetails.getLongitude());
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(Intent.createChooser(intent, "com.Google.Android.apps.map"));
            }
        });







         rates.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 Intent Rates_Intent = new Intent(Details_Parking.this,Rates.class);
                 Rates_Intent.putExtra("ID",MArkerDetails.getParkingId());
                 startActivity(Rates_Intent);

             }
         });



        call1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MArkerDetails.getContactNumber1()!=null && MArkerDetails.getContactNumber1().length()==10){

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    // set title
                    alertDialogBuilder.setTitle("Make a Call:");

                    // set dialog message
                    alertDialogBuilder
                            .setMessage("You are about to call"+ MArkerDetails.getContactPerson1()+ "for assistance.Do you want to continue?")
                            .setCancelable(false)
                            .setPositiveButton("Call",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    if (checkPermission()) {

                                        Toast.makeText(getApplicationContext(),"Permission already granted.",Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+91"+MArkerDetails.getContactNumber1().toString().trim()));
                                        try{
                                            startActivity(intent);}catch(SecurityException e){ Toast.makeText(getApplicationContext(),e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();}

                                    } else {

                                        Toast.makeText(getApplicationContext(),"Permission Not granted.",Toast.LENGTH_LONG).show();

                                        //Get The Permission   Need to work on this Solution
                                      Boolean flagpermission =   requestPermission();

                                        if(flagpermission){
                                            Toast.makeText(context,"GPS permission allows us to access location data. Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "9459619235"));
                                            try{
                                                startActivity(intent);}catch(SecurityException e){ Toast.makeText(getApplicationContext(),e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();}
                                        }else{
                                            Toast.makeText(context,"You need to manually set the permission to the Application."+"####"+ flagpermission.toString(),Toast.LENGTH_LONG).show();
                                        }
                                    }








                                }
                            })
                            .setNegativeButton("Don't Call", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing
                                    dialog.cancel();
                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();
                }
            }
        });

        call2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if(contactphone2!=null && contactphone2.length()==10){
                        //Start the call Subroutines

                }
            }
        });

        call3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    //Start the call Subroutine
                    if(contactphone3!=null && contactphone3.length()==10){
                        //Start the call Subroutines
                    }

            }
        });





        parking_place.setText(MArkerDetails.getParkingPlace());
        parking_area.setText(MArkerDetails.getParkingArea());
        if(MArkerDetails.getParkingFullTag().length()==5) {
            parking_availability.setText("Available");
        }else{
            parking_availability.setText("Not Available");
        }





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

        }catch(Exception e){
            Toast.makeText(getApplicationContext(),e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
        }
    }

    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;

        }
    }

    private Boolean requestPermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(Details_Parking.this,Manifest.permission.ACCESS_FINE_LOCATION)){

            return true;

        } else {

            ActivityCompat.requestPermissions(Details_Parking.this,new String[]{Manifest.permission.CALL_PHONE},PERMISSION_REQUEST_CODE);
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(getApplicationContext(),"Permission Granted, Now you can make a call.",Toast.LENGTH_LONG).show();
                  //  Toast.makeText(context,"GPS permission allows us to access location data. Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();
                  //  Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "9459619235"));
                   // try{
                   //     startActivity(intent);}catch(SecurityException e){ Toast.makeText(getApplicationContext(),e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();}
                } else {

                    Toast.makeText(getApplicationContext(),"Permission Denied, You cannot give a call",Toast.LENGTH_LONG).show();

                }
                break;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MArkerDetails = null;
        Details_Parking.this.finish();
    }



}
