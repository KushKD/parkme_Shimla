package findparking.hp.dit.himachal.com.shimlaparking;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONStringer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import Generic.Custom_Dialog;
import HTTP.DateTime;
import Model.Sending_Object_All_details_Pojo;
import Parse.Manager_Json;
import Parse.Parse_Google_API_Json;
import Utilities.AppStatus;
import Utilities.Econstants;
import HTTP.HttpManager;

public class ParkingDetails_Activity extends AppCompatActivity  {

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
            parking_id,
            distance,
            duration;

    String [] Distance_Time = null;

    private double _Distance = 0;
    float[] result;
      Sending_Object_All_details_Pojo MArkerDetails;


    private LinearLayout contactperson1_layout,contactperson2_layout,contactperson3_layout;
    private Button call1 , call2,call3,get_directions , rates , issues,parkme_bt,parkout_bt,rating_bt;
    final Context context = this;
    private static final int PERMISSION_REQUEST_CODE = 1;
   // MArkerDetails = null;

    Boolean FLAG_UI = false;
    StringBuilder SB = null;


    private static final int PERMISSION_REQUEST_CODE_LOCATION = 1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details__parking);



        FLAG_UI = Initialize_View();

        if (FLAG_UI) {
            try {

                Intent getRoomDetailsIntent = getIntent();
                 MArkerDetails = (Sending_Object_All_details_Pojo) getRoomDetailsIntent.getSerializableExtra("DETAILS_ALL");


                if (MArkerDetails.getContactPerson1().length() == 0) {
                    contactperson1_layout.setVisibility(View.GONE);
                }

                if (MArkerDetails.getContactPerson2().length() == 0) {
                    contactperson2_layout.setVisibility(View.GONE);
                }

                if (MArkerDetails.getContactPerson3().length() == 0) {
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
                contactperson1.setText(MArkerDetails.getContactPerson1());
                contactperson2.setText(MArkerDetails.getContactPerson2());
                contactperson3.setText(MArkerDetails.getContactPerson3());
                contactphone1.setText("+91" + MArkerDetails.getContactNumber1());
                contactphone2.setText("+91" + MArkerDetails.getContactNumber2());
                contactphone3.setText("+91" + MArkerDetails.getContactNumber3());
              //  parking_id.setText(MArkerDetails.getParkingId());
                parking_place.setText(MArkerDetails.getParkingPlace());
                parking_area.setText(MArkerDetails.getParkingArea());

               /* if (MArkerDetails.getParkingFullTag().length() == 5) {
                    parking_availability.setText("Available");
                } else {
                    parking_availability.setText("Not Available");
                }*/
                if(MArkerDetails.getAvailability().equalsIgnoreCase("Not Known")){
                    parking_availability.setText(MArkerDetails.getAvailability());
                    parking_availability.setTextColor(Color.parseColor("#ffa500")); //orange
                }else{
                    //Toast.makeText(getApplicationContext(),myMarker.getParkingFullTag().toString(),Toast.LENGTH_LONG).show();
                    parking_availability.setText(MArkerDetails.getAvailability()+"("+MArkerDetails.getPercentage()+"%)");  //
                   // parking_availability.setTextColor(Color.parseColor("#e3ff0")); //parrot green
                    if(Integer.parseInt(MArkerDetails.getPercentage())<=0){
                        parking_availability.setTextColor(Color.parseColor("#990000")); //red
                    }else if(Integer.parseInt(MArkerDetails.getPercentage())>0 && Integer.parseInt(MArkerDetails.getPercentage())<=25){
                        parking_availability.setTextColor(Color.parseColor("#ec7046"));  //wheat
                    }else if(Integer.parseInt(MArkerDetails.getPercentage())>25 && Integer.parseInt(MArkerDetails.getPercentage())<=50){
                        parking_availability.setTextColor(Color.parseColor("#0000b2")); //blue
                    }else if(Integer.parseInt(MArkerDetails.getPercentage())>50 && Integer.parseInt(MArkerDetails.getPercentage())<=75){
                        parking_availability.setTextColor(Color.parseColor("#006600")); //Light green
                    }else{
                        parking_availability.setTextColor(Color.parseColor("#004c00")); //Dark green
                    }
                }





                if(MArkerDetails.getLongitude_my_Location()!= null && MArkerDetails.getLatitude_my_Location()!=null){
                    latitude_person.setText(Double.toString(MArkerDetails.getLatitude_my_Location()));
                    longitude_person.setText(Double.toString(MArkerDetails.getLongitude_my_Location()));
                }else{
                  //  Toast.makeText(ParkingDetails_Activity.this, "We don't have your precise Location to start the navigation.", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(ParkingDetails_Activity.this);

                    // Setting Dialog Title
                    alertDialog.setTitle("Alert");

                    // Setting Dialog Message
                    alertDialog.setMessage("We don't have your precise Location to start the navigation. Click Yes to get your Location and No to exit.");

                    // Setting Icon to Dialog
                  //  alertDialog.setIcon(R.drawable.delete);

                    // Setting Positive "Yes" Button
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int which) {

                            // Write your code here to invoke YES event
                            //Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                            // instantiate the location manager, note you will need to request permissions in your manifest
                            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                            // get the last know location from your location manager.
                            try{
                            Location location= locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                                latitude_person.setText(Double.toString(location.getLatitude()));
                                longitude_person.setText(Double.toString(location.getLongitude()));


                            }catch (SecurityException e){
                                Log.e("ERROR",e.getLocalizedMessage().toString());
                            }
                            // now get the lat/lon from the location and do something with it.
                           // nowDoSomethingWith(location.getLatitude(), location.getLongitude());
                        }
                    });



                    // Setting Negative "NO" Button
                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Write your code here to invoke NO event
                            Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                        }
                    });

                    // Showing Alert Message
                    alertDialog.show();
                }




                issues.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if(latitude_person.getText().toString()!= null && longitude_person.getText().toString()!=null){
                            Intent i = new Intent(ParkingDetails_Activity.this, IssuesFeedback_Activity.class);
                            i.putExtra("ID", MArkerDetails.getParkingId());
                            i.putExtra("LATITUDE",latitude_person.getText().toString());
                            i.putExtra("LONGITUDE",longitude_person.getText().toString());
                            startActivity(i);
                        }else{
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ParkingDetails_Activity.this);

                            // Setting Dialog Title
                            alertDialog.setTitle("Alert");

                            // Setting Dialog Message
                            alertDialog.setMessage("We don't have your precise Location to start the navigation. Click Yes to get your Location and No to exit.");

                            // Setting Icon to Dialog
                            //  alertDialog.setIcon(R.drawable.delete);

                            // Setting Positive "Yes" Button
                            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int which) {

                                    // Write your code here to invoke YES event
                                    //Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                                    // instantiate the location manager, note you will need to request permissions in your manifest
                                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                                    // get the last know location from your location manager.
                                    try{
                                        Location location= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                        latitude_person.setText(Double.toString(location.getLatitude()));
                                        longitude_person.setText(Double.toString(location.getLongitude()));
                                    }catch (SecurityException e){
                                        Log.e("ERROR",e.getLocalizedMessage().toString());
                                    }
                                    // now get the lat/lon from the location and do something with it.
                                    // nowDoSomethingWith(location.getLatitude(), location.getLongitude());
                                }
                            });

                            // Setting Negative "NO" Button
                            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to invoke NO event
                                    Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }
                            });

                            // Showing Alert Message
                            alertDialog.show();
                        }


                    }
                });


                get_directions.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(latitude_person.getText().toString()!= null && longitude_person.getText().toString()!=null){
                            String uri = "http://maps.google.com/maps?f=d&hl=en&saddr=" + latitude_person.getText().toString() + "," + longitude_person.getText().toString() + "&daddr=" + Double.toString(MArkerDetails.getLatitude()) + "," + Double.toString(MArkerDetails.getLongitude());
                            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                            startActivity(Intent.createChooser(intent, "com.Google.Android.apps.map"));
                        }else{
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ParkingDetails_Activity.this);

                            // Setting Dialog Title
                            alertDialog.setTitle("Alert");

                            // Setting Dialog Message
                            alertDialog.setMessage("We don't have your precise Location to start the navigation. Click Yes to get your Location and No to exit.");

                            // Setting Icon to Dialog
                            //  alertDialog.setIcon(R.drawable.delete);

                            // Setting Positive "Yes" Button
                            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int which) {

                                    // Write your code here to invoke YES event
                                    //Toast.makeText(getApplicationContext(), "You clicked on YES", Toast.LENGTH_SHORT).show();
                                    // instantiate the location manager, note you will need to request permissions in your manifest
                                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                                    // get the last know location from your location manager.
                                    try{
                                        Location location= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                        latitude_person.setText(Double.toString(location.getLatitude()));
                                        longitude_person.setText(Double.toString(location.getLongitude()));
                                    }catch (SecurityException e){
                                        Log.e("ERROR",e.getLocalizedMessage().toString());
                                    }
                                    // now get the lat/lon from the location and do something with it.
                                    // nowDoSomethingWith(location.getLatitude(), location.getLongitude());
                                }
                            });

                            // Setting Negative "NO" Button
                            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Write your code here to invoke NO event
                                    Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                                    dialog.cancel();
                                }
                            });

                            // Showing Alert Message
                            alertDialog.show();
                        }

                    }
                });


                rates.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent Rates_Intent = new Intent(ParkingDetails_Activity.this, Rates_Activity.class);
                        Rates_Intent.putExtra("ID", MArkerDetails.getParkingId());
                        startActivity(Rates_Intent);

                    }
                });


                call1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (MArkerDetails.getContactNumber1() != null && MArkerDetails.getContactNumber1().length() == 10) {

                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                            // set title
                            alertDialogBuilder.setTitle("Make a Call:");

                            // set dialog message
                            alertDialogBuilder
                                    .setMessage("You are about to call" + MArkerDetails.getContactPerson1() + "for assistance.Do you want to continue?")
                                    .setCancelable(false)
                                    .setPositiveButton("Call", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            if (checkPermission()) {

                                              //  Toast.makeText(getApplicationContext(), "Permission already granted.", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "+91" + MArkerDetails.getContactNumber1().toString().trim()));
                                                try {
                                                    startActivity(intent);
                                                } catch (SecurityException e) {
                                                    Toast.makeText(getApplicationContext(), e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                                                }

                                            } else {

                                                Toast.makeText(getApplicationContext(), "Permission Not granted.", Toast.LENGTH_LONG).show();

                                                //Get The Permission   Need to work on this Solution
                                                Boolean flagpermission = requestPermission();

                                                if (flagpermission) {
                                                 //   Toast.makeText(context, "GPS permission allows us to access location data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "9459619235"));
                                                    try {
                                                        startActivity(intent);
                                                    } catch (SecurityException e) {
                                                        Toast.makeText(getApplicationContext(), e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                                                    }
                                                } else {
                                                   // Toast.makeText(context, "You need to manually set the permission to the Application." + "####" + flagpermission.toString(), Toast.LENGTH_LONG).show();
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

                        if (contactphone2 != null && contactphone2.length() == 10) {
                            //Start the call Subroutines

                        }
                    }
                });

                call3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Start the call Subroutine
                        if (contactphone3 != null && contactphone3.length() == 10) {
                            //Start the call Subroutines
                        }

                    }
                });

                /**
                 * Park Out
                 */

            parkout_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  //  ShowAlertParkOut("Check Out");
                    SharedPreferences prfs = getSharedPreferences(Econstants.PREFRANCE_NAME, Context.MODE_PRIVATE);
                    final String ParkingId_Server  = MArkerDetails.getParkingId();
                    String PhoneNumber_Server = prfs.getString("phonenumber","");
                    String VehicleNo_Server = prfs.getString("VehicleNumber","");
                    Log.e("Phone",PhoneNumber_Server);
                    Log.e("Vehicle No",VehicleNo_Server);

                    Custom_Dialog DC = new Custom_Dialog();
                    DC.Show_Park_OUT(ParkingDetails_Activity.this,"Park Out",PhoneNumber_Server,VehicleNo_Server,ParkingId_Server);
                }
            });


                /**
                 * Rating
                 */
            rating_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SharedPreferences x = getSharedPreferences(Econstants.PREFRANCE_NAME, Context.MODE_PRIVATE);

                    final String ParkingId_Server  = MArkerDetails.getParkingId();
                    String PhoneNumber_Server = x.getString("phonenumber","");

                    Log.e("Phone",PhoneNumber_Server);
                    Log.e("Parking ID",ParkingId_Server);

    Custom_Dialog RM = new Custom_Dialog();
    Log.e("Error","We are Here");
                  //  RM.showDialog(ParkingDetails_Activity.this,"HI");
      RM.show_Rating(ParkingDetails_Activity.this, "Rate Me", PhoneNumber_Server, ParkingId_Server);

}
            });


                /**
                 * Park Me
                 */
                parkme_bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if (Integer.parseInt(MArkerDetails.getParkingId()) == 19) {
                            SharedPreferences prfs = getSharedPreferences(Econstants.PREFRANCE_NAME, Context.MODE_PRIVATE);

                            final String ParkingId_Server  = MArkerDetails.getParkingId();
                            String PhoneNumber_Server = prfs.getString("phonenumber","");
                            String VehicleNo_Server = prfs.getString("VehicleNumber","");

                            Log.e("Phone",PhoneNumber_Server);
                            Log.e("Vehicle No",VehicleNo_Server);
                            if(AppStatus.getInstance(ParkingDetails_Activity.this).isOnline()) {
                                Custom_Dialog DC = new Custom_Dialog();
                                DC.Show_Park_ME(ParkingDetails_Activity.this, "Park Me", PhoneNumber_Server, VehicleNo_Server, ParkingId_Server);
                            }else{
                                Toast.makeText(getApplicationContext(),"Please connect to Internet.",Toast.LENGTH_LONG).show();
                            }
                        } else {


                            //Check the Distance
                            //If the distance is more than 5000 metres The user is not allowed to park in
                            try {
                                float distance_for_car_parking = 0;
                                Location CurrentLocation = null;


                                //Get Location
                                try {
                                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                                    CurrentLocation = new Location("Current Location");
                                    CurrentLocation.setLatitude(location.getLatitude());
                                    CurrentLocation.setLongitude(location.getLongitude());


                                } catch (SecurityException e) {
                                    Log.e("ERROR", e.getLocalizedMessage().toString());
                                }


                                Location Parking_Location = new Location("Selected Parking");
                                Parking_Location.setLatitude(MArkerDetails.getLatitude());
                                Parking_Location.setLongitude(MArkerDetails.getLongitude());

                                distance_for_car_parking = CurrentLocation.distanceTo(Parking_Location); // in meters

                                Log.e("Distance", Float.toString(distance_for_car_parking));

                                if (distance_for_car_parking <= 500) {
                                    SharedPreferences prfs = getSharedPreferences(Econstants.PREFRANCE_NAME, Context.MODE_PRIVATE);
                                    final String ParkingId_Server  = MArkerDetails.getParkingId();
                                    String PhoneNumber_Server = prfs.getString("phonenumber","");
                                    String VehicleNo_Server = prfs.getString("VehicleNumber","");
                                    Log.e("Phone",PhoneNumber_Server);
                                    Log.e("Vehicle No",VehicleNo_Server);

                                    Custom_Dialog DC = new Custom_Dialog();
                                    DC.Show_Park_ME(ParkingDetails_Activity.this,"Park Me",PhoneNumber_Server,VehicleNo_Server,ParkingId_Server);
                                } else {
                                    String Message_NO_PARK = "You are currently " + Math.round(distance_for_car_parking) + " meters away from selected parking. Please reach closer (within 500 meters) and try again.";
                                  Custom_Dialog D_C = new Custom_Dialog();
                                    D_C.showDialog(ParkingDetails_Activity.this,Message_NO_PARK);
                                }


                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), "Don't have the precise location, please enable your GPS.", Toast.LENGTH_LONG).show();
                            }

                        }
                    }
                });


                if(MArkerDetails.getLongitude_my_Location()!= null && MArkerDetails.getLatitude_my_Location()!=null){

                    distance.setText("Getting distance please wait..");
                    duration.setText("Getting Duration please wait..");
                     if(isOnline()){

                         SB = new StringBuilder();
                         SB.append("http://maps.googleapis.com/maps/api/directions/json?origin=");
                         SB.append(MArkerDetails.getLatitude_my_Location());
                         SB.append(",");
                         SB.append(MArkerDetails.getLongitude_my_Location());
                         SB.append("&destination=");
                         SB.append(MArkerDetails.getLatitude());
                         SB.append(",");
                         SB.append(MArkerDetails.getLongitude());
                         SB.append("&mode=driving&sensor=false");
                         GetDistance get_Distance = new GetDistance();
                         get_Distance.execute(SB.toString());
                     }else{
                        distance.setText("Unable to get the distance.Please connect to Internet.");
                    }

                }else{
                    Toast.makeText(getApplicationContext(),"User Location not known ",Toast.LENGTH_SHORT).show();
                }





            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Unable to Load the elements properly", Toast.LENGTH_LONG).show();
        }
    }

    private Boolean Initialize_View() {

        try{

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
          //  parking_id = (TextView)findViewById(R.id.parkingid);
            get_directions = (Button)findViewById(R.id.get_directions);
            rates = (Button)findViewById(R.id.rates);
            issues = (Button)findViewById(R.id.issues);
            distance = (TextView)findViewById(R.id.distance);
            parkme_bt = (Button)findViewById(R.id.parkme);
            parkout_bt = (Button)findViewById(R.id.parkout);
            rating_bt = (Button)findViewById(R.id.rating);
            duration = (TextView)findViewById(R.id.duration);


            return true;
        }catch(Exception e){
            return false;
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

        if (ActivityCompat.shouldShowRequestPermissionRationale(ParkingDetails_Activity.this,Manifest.permission.ACCESS_FINE_LOCATION)){

            return true;

        } else {

            ActivityCompat.requestPermissions(ParkingDetails_Activity.this,new String[]{Manifest.permission.CALL_PHONE},PERMISSION_REQUEST_CODE);
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
        ParkingDetails_Activity.this.finish();
    }


    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }








class GetDistance extends AsyncTask<String,String,String>{


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        String content = HttpManager.get_Data(params[0]);
        return content;

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.e("GOOGLE Direction API",s);

          //Google
        try{
        Distance_Time = new String[2];
        Distance_Time = Parse_Google_API_Json.parseGoogleJSON(s);
        distance.setText(Distance_Time[0]);
        duration.setText(Distance_Time[1]);
        }catch(Exception e){

            distance.setText("Unable to get distance.");
            duration.setText("Unable to get duration");

        }

    }
}

}
