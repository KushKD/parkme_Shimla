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
import android.support.design.widget.FloatingActionButton;
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

import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import org.json.JSONException;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import Helper.helper_Functions;
import Http_Manager.Http_Manager;
import Http_Manager.date_Time;
import Parse.JSON_Manager;
import Parse.Parse_JSON_Google;

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
            parking_id,
            distance,
            duration;

    String [] Distance_Time = null;

    private double _Distance = 0;
    float[] result;
      Sending_Object_All_details   MArkerDetails;


    private LinearLayout contactperson1_layout,contactperson2_layout,contactperson3_layout;
    private Button call1 , call2,call3,get_directions , rates , issues,parkme_bt;
    final Context context = this;
    private static final int PERMISSION_REQUEST_CODE = 1;
   // MArkerDetails = null;

    Boolean FLAG_UI = false;
    StringBuilder SB = null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details__parking);

        FLAG_UI = Initialize_View();

        if (FLAG_UI) {
            try {

                Intent getRoomDetailsIntent = getIntent();
                 MArkerDetails = (Sending_Object_All_details) getRoomDetailsIntent.getSerializableExtra("DETAILS_ALL");


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
                  //  Toast.makeText(Details_Parking.this, "We don't have your precise Location to start the navigation.", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(Details_Parking.this);

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




                issues.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if(latitude_person.getText().toString()!= null && longitude_person.getText().toString()!=null){
                            Intent i = new Intent(Details_Parking.this, Issues_Feedback.class);
                            i.putExtra("ID", MArkerDetails.getParkingId());
                            i.putExtra("LATITUDE",latitude_person.getText().toString());
                            i.putExtra("LONGITUDE",longitude_person.getText().toString());
                            startActivity(i);
                        }else{
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(Details_Parking.this);

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
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(Details_Parking.this);

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

                        Intent Rates_Intent = new Intent(Details_Parking.this, Rates.class);
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

                                                Toast.makeText(getApplicationContext(), "Permission already granted.", Toast.LENGTH_LONG).show();
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
                                                    Toast.makeText(context, "GPS permission allows us to access location data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
                                                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "9459619235"));
                                                    try {
                                                        startActivity(intent);
                                                    } catch (SecurityException e) {
                                                        Toast.makeText(getApplicationContext(), e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                                                    }
                                                } else {
                                                    Toast.makeText(context, "You need to manually set the permission to the Application." + "####" + flagpermission.toString(), Toast.LENGTH_LONG).show();
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
                 * Park Me
                 */
                parkme_bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //// TODO: 6/9/2016

                         //Show Alert Box
                        ShowAlert("kush");

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
            duration = (TextView)findViewById(R.id.duration);

            return true;
        }catch(Exception e){
            return false;
        }
    }

    private void ShowAlert(String s) {
        final Dialog dialog = new Dialog(Details_Parking.this);
        dialog.setContentView(R.layout.dialog_parkme);
        dialog.setTitle("Park Me");
        dialog.setCancelable(false);
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE  | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();
       // TextView DialogInfo = (TextView)dialog.findViewById(R.id.dialog_info);
       // DialogInfo.setText(s);


        SharedPreferences prfs = getSharedPreferences(Econstants.PREFRANCE_NAME, Context.MODE_PRIVATE);

      final String ParkingId_Server  = MArkerDetails.getParkingId();
      String PhoneNumber_Server = prfs.getString("phonenumber","");
      String VehicleNo_Server = prfs.getString("VehicleNumber","");

        Log.e("Phone",PhoneNumber_Server);
        Log.e("Vehicle No",VehicleNo_Server);



        Button dialog_ok = (Button)dialog.findViewById(R.id.dialog_ok);
        Button exit = (Button)dialog.findViewById(R.id.dialog_exit);
       final  EditText carnumber_tv = (EditText)dialog.findViewById(R.id.carnumber);
        carnumber_tv.setText(VehicleNo_Server);
        final EditText phonenumber_tv  = (EditText)dialog.findViewById(R.id.phonenumber);
        phonenumber_tv.setText(PhoneNumber_Server);
       final Spinner typecar_sp = (Spinner)dialog.findViewById(R.id.typecar);
       final Spinner estimatedtime_sp = (Spinner)dialog.findViewById(R.id.estimatedtime);

        final String WSTS = Long.toString( estimatedtime_sp.getSelectedItemId());
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOnline()) {
                    String EstimatedTime_Server = null;
                     EstimatedTime_Server  = Long.toString(estimatedtime_sp.getSelectedItemId());

                    String VehicleType_Server = typecar_sp.getSelectedItem().toString().trim();
                   Log.e("EstimatedTime_Server",WSTS);
                    Log.e("VehicleType_Server",VehicleType_Server);
                    Log.e("ParkingId_Server",ParkingId_Server);
                    Log.e("Phone no",phonenumber_tv.getText().toString());
                    Log.e("Car Number",carnumber_tv.getText().toString());

                    Park_Me PM = new Park_Me();
                    PM.execute(EstimatedTime_Server,ParkingId_Server,phonenumber_tv.getText().toString().trim(),carnumber_tv.getText().toString().trim(),VehicleType_Server);
                     dialog.dismiss();
                }else{
                    Toast.makeText(getApplicationContext(), "Network not found" ,Toast.LENGTH_LONG).show();
                }
            }
        });
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
        Details_Parking.this.finish();
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




    public class Park_Me extends AsyncTask<String,String,String>{

        URL url_;
        HttpURLConnection conn_;
        StringBuilder sb_ = null;

        private String EstimatedTime = null;
        private String ParkingId = null;
        private String PhoneNumber = null;
        private String VehicleNo = null;
        private String VehicleType = null;

        JSONStringer userJson = null;

        private ProgressDialog dialog;
        String url = null;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(Details_Parking.this);
            this.dialog.setMessage("Please wait ..");
            this.dialog.show();
            this.dialog.setCancelable(false);
        }


        @Override
        protected String doInBackground(String... params) {
            EstimatedTime = params[0];
            ParkingId = params[1];
            PhoneNumber = params[2];
            VehicleNo = params[3];
            VehicleType = params[4];

            try {
                url_ =new URL(Econstants.URL_MAIN+"/getParkMeRequest_JSON");
                System.out.println(url_.toString());
                conn_ = (HttpURLConnection)url_.openConnection();
                conn_.setDoOutput(true);
                conn_.setRequestMethod("POST");
                conn_.setUseCaches(false);
                conn_.setConnectTimeout(10000);
                conn_.setReadTimeout(10000);
                conn_.setRequestProperty("Content-Type", "application/json");
                conn_.connect();

                userJson = new JSONStringer()
                        .object().key("ParkMeRequst")
                        .object()
                        .key("EstimatedTime").value(EstimatedTime)
                        .key("InTime").value(date_Time.GetDateAndTime())
                        .key("ParkingId").value(ParkingId)
                        .key("PhoneNumber").value(PhoneNumber)
                        .key("RegisterId").value("0")
                        .key("RequestStatus").value("Pending")
                        .key("RequestTime").value(date_Time.GetDateAndTime())
                        .key("VehicleNo").value(VehicleNo)
                        .key("VehicleType").value(VehicleType)
                        .endObject()
                        .endObject();


                System.out.println(userJson.toString());
                Log.e("Object",userJson.toString());
                OutputStreamWriter out = new OutputStreamWriter(conn_.getOutputStream());
                out.write(userJson.toString());
                out.close();

                try{
                    int HttpResult =conn_.getResponseCode();
                    if(HttpResult ==HttpURLConnection.HTTP_OK){
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn_.getInputStream(),"utf-8"));
                        sb_ = new StringBuilder();
                        String line = null;
                        while ((line = br.readLine()) != null) {
                            sb_.append(line + "\n");
                        }
                        br.close();
                        System.out.println(sb_.toString());

                    }else{
                        System.out.println("Server Connection failed.");
                    }

                } catch(Exception e){
                    return "Server Connection failed.";
                }

            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally{
                if(conn_!=null)
                    conn_.disconnect();
            }
            return sb_.toString();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                dialog.dismiss();
                String result_to_Show = JSON_Manager.Parse_PArkME(s);
                ShowAlertafter_ParkME(result_to_Show);


            }catch(Exception e){
                dialog.dismiss();
                String result_to_Show  = "Something went wrong. Please try again later.";
                ShowAlertafter_ParkME(result_to_Show);

            }



        }
    }


    private void ShowAlertafter_ParkME(String s) {
        final Dialog dialog = new Dialog(Details_Parking.this);
        dialog.setContentView(R.layout.dialog_parkme_result);
        dialog.setTitle("Alert");
        dialog.setCancelable(false);

        dialog.show();



        Button dialog_ok = (Button)dialog.findViewById(R.id.dialog_ok);

        TextView dialog_result_tv =  (TextView)dialog.findViewById(R.id.dialog_result);
        dialog_result_tv.setText(s);


        dialog_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });




    }


class GetDistance extends AsyncTask<String,String,String>{


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        String content = Http_Manager.get_Data(params[0]);
        return content;

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.e("GOOGLE Direction API",s);

          //Google
        try{
        Distance_Time = new String[2];
        Distance_Time = Parse_JSON_Google.parseGoogleJSON(s);
        distance.setText(Distance_Time[0]);
        duration.setText(Distance_Time[1]);
        }catch(Exception e){

            distance.setText("Unable to get distance.");
            duration.setText("Unable to get duration");

        }

       // Log.e("GOOGLE Direction API",RR);
    }
}

}
