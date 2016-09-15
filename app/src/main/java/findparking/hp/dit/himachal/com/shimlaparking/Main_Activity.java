package findparking.hp.dit.himachal.com.shimlaparking;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Abstract.PermissionUtils;
import Generic.Custom_Dialog;
import Utilities.helper_Functions;
import Model.My_Marker_Pojo;
import Model.Sending_Object_All_details_Pojo;
import Utilities.Econstants;

public class Main_Activity extends AppCompatActivity implements

        GoogleMap.OnMyLocationButtonClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        LocationListener,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnInfoWindowLongClickListener

{


    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private boolean mPermissionDenied = false;
    private int Zoom_Value_Camera = 13;
    private float Straight_Distance = 0;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    LatLng latLng;
    LatLng Shimla_latLng;
    Marker currLocationMarker;
    private GoogleMap mMap;
    private ArrayList<My_Marker_Pojo> mMyMarkersArray = null;
    private  HashMap<Marker, My_Marker_Pojo> mMarkersHashMap = null;
    List<Get_Parking_Details> tasks;
    ProgressBar pb;
    URL url_;
    HttpURLConnection conn_;
    StringBuilder sb = new StringBuilder();
    float distance =0;

    Button bt_greenfee , bt_parkinglist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.my_location_demo);
        tasks = new ArrayList<>();

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        bt_greenfee = (Button)findViewById(R.id.green_fee);
        bt_parkinglist = (Button)findViewById(R.id.parking_list);

        bt_greenfee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    final Dialog dialog = new Dialog(Main_Activity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setCancelable(true);
                    dialog.setContentView(R.layout.dialog_custom);

                    TextView text = (TextView) dialog.findViewById(R.id.dialog_result);
                    text.setText("You will be redirected to Green Fee website. Press OK to continue and press back button to cancel. ");

                    Button dialog_ok = (Button)dialog.findViewById(R.id.dialog_ok);

                    dialog_ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // activity.finish();
                            dialog.dismiss();
                            Uri uri = Uri.parse("http://hpparking.hp.gov.in/web/Green_Fee_Form.aspx"); // missing 'http://' will cause crashed
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        }
                    });

                    dialog.show();

                }

        });

        bt_parkinglist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





                        if((latLng!=null)) {
                            Intent i_List = new Intent(Main_Activity.this,List_Parking.class);
                            i_List.putExtra("Latitude",Double.toString(latLng.latitude));
                            i_List.putExtra("Longitude",Double.toString(latLng.longitude));
                            startActivity(i_List);

                        }else{
                           Custom_Dialog CD = new Custom_Dialog();
                            CD.showDialog(Main_Activity.this,"Please go to the settings and  enable your GPS Location.");
                        }



                    }
                });
    }



    private void plotMarkers(ArrayList<My_Marker_Pojo> markers) {
        if (markers.size() > 0) {
            for (My_Marker_Pojo myMarker : markers) {


               // System.out.println(markers.size() );
               // System.out.println(myMarker.getParkingID() );

                // Create user marker with custom icon and other options
                MarkerOptions markerOption = new MarkerOptions().position(new LatLng(myMarker.getLatitude(), myMarker.getLongitude()));


              /*  System.out.println(myMarker.getParkingFullTag() + "###" +myMarker.getParkingFullTag().toUpperCase() +"###"+ myMarker.getParkingFullTag().length() );
                 if(myMarker.getParkingFullTag().equalsIgnoreCase("1")){
                     markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.pavailable));
                 }else if(myMarker.getParkingFullTag().equalsIgnoreCase("0")){
                     markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.pnotavailable));
                 }else{
                    //Toast.makeText(getApplicationContext(),myMarker.getParkingFullTag().toString(),Toast.LENGTH_LONG).show();
                    markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.pp));
                }*/

                if(myMarker.getAvailability().equalsIgnoreCase("Not Known")){
                    markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.pp));
                }else{
                    //Toast.makeText(getApplicationContext(),myMarker.getParkingFullTag().toString(),Toast.LENGTH_LONG).show();
                   // available.setText(myMarker.getAvailability()+"("+myMarker.getPercentage()+"%)");  //
                    // parking_availability.setTextColor(Color.parseColor("#ffa500")); //orange
                    if(myMarker.getPercentage().equalsIgnoreCase("0")){
                        markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.pnotavailable));
                    }
                    else{
                    markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.pavailable));
                    }
                }


                Marker currentMarker = mMap.addMarker(markerOption);
                mMarkersHashMap.put(currentMarker, myMarker);
                System.out.println("Current MarkerID:-  "+currentMarker.getId().toString()+"    ########    "+"MyMarker_Pojo:-  "+myMarker.getParkingID() );

                mMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter());
            }
        }
    }





    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
       // mMap.setOnInfoWindowLongClickListener(this);
try {
    mMap.setMyLocationEnabled(true);
}catch(SecurityException s){
   // Toast.makeText(getApplicationContext(),"Not Good",Toast.LENGTH_SHORT).show();
}

                buildGoogleApiClient();

                mGoogleApiClient.connect();

                enableMyLocation();

        if (isOnline()) {
            try {
                Get_Parking_Details GPD = new Get_Parking_Details();
                GPD.execute(Econstants.URL_GENERIC);
            }catch(Exception e){
                Log.e("CAUGHT",e.getMessage().toString());
            }

        } else {
            Toast.makeText(this,"Unable to Connect to Internet. Please check your Network connection.", Toast.LENGTH_LONG).show();
        }
        // Async Task Starts Here


       /* if(mMyMarkersArray.size() > 0) {
            plotMarkers(mMyMarkersArray);
        }else{
            Log.d("List is","Empty");
        }*/

                

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


    protected synchronized void buildGoogleApiClient() {
       // Toast.makeText(this,"buildGoogleApiClient",Toast.LENGTH_SHORT).show();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    /**
             * Enables the My Location layer if the fine location permission has been granted.
             */
            private void enableMyLocation() {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission to access the location is missing.
                    PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                            Manifest.permission.ACCESS_FINE_LOCATION, true);
                } else if (mMap != null) {
                    // Access to the location has been granted to the app.
                    mMap.setMyLocationEnabled(true);
                }
            }

            @Override
            public boolean onMyLocationButtonClick() {
                if((latLng==null)) {
                    Custom_Dialog CD = new Custom_Dialog();
                    CD.showDialog(Main_Activity.this,"Please go to the settings and  enable your GPS Location.");

                }
                return false;
            }


            @Override
            public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                                   @NonNull int[] grantResults) {
                if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
                    return;
                }

                if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
                    // Enable the my location layer if the permission has been granted.
                    enableMyLocation();
                } else {
                    // Display the missing permission error dialog when the fragments resume.
                    mPermissionDenied = true;
                }
            }

            @Override
            protected void onResumeFragments() {
                super.onResumeFragments();
                if (mPermissionDenied) {
                    // Permission was not granted, display error dialog.
                    showMissingPermissionError();
                    mPermissionDenied = false;
                }
            }

            /**
             * Displays a dialog with error message explaining that the location permission is missing.
             */
            private void showMissingPermissionError() {
                PermissionUtils.PermissionDeniedDialog
                        .newInstance(true).show(getSupportFragmentManager(), "dialog");
            }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Toast.makeText(this,"onConnected",Toast.LENGTH_SHORT).show();
        Location mLastLocation = null;
        try {
             mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }catch(SecurityException r){
           //
            //  Toast.makeText(getApplicationContext(),"There is a problem with the GPS device.",Toast.LENGTH_SHORT).show();
        }
        if (mLastLocation != null) {
            //place marker at current position
            //mGoogleMap.clear();
            latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
           // markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            currLocationMarker = mMap.addMarker(markerOptions);
        }
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(150000); //5 seconds
            mLocationRequest.setFastestInterval(100000); //3 seconds
            mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
            //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter
try {
    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
}catch(SecurityException s){
  //  Toast.makeText(getApplicationContext(),"Something's not Good.",Toast.LENGTH_LONG).show();
}
        }


    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(this,"onConnectionSuspended",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {

        //place marker at current position
        //mGoogleMap.clear();
        if (currLocationMarker != null) {
            currLocationMarker.remove();
        }
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
       // markerOptions.title("Current Position");
       // markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
      //  currLocationMarker = mMap.addMarker(markerOptions);

       // Toast.makeText(this,"Location Changed",Toast.LENGTH_SHORT).show();

        //Get Distance Grom Delhi
        float Distance_From_Delhi = helper_Functions.distFromDelhi((float)28.6457559,(float)76.8105573,(float)31.0699863,(float)77.1867547);
        Log.e("Distance from Delhi",Float.toString(Distance_From_Delhi/40));
        //Get Distance from Changigarh
        float Distance_From_Changigarh = helper_Functions.distFromChandigarh((float)30.726525,(float)76.6963736,(float)31.0699863,(float)77.1867547);
        Log.e("Distance from Chandi",Float.toString(Distance_From_Changigarh/40));




        //Straight_Distance
        Straight_Distance = helper_Functions.distGeneric((float)31.099992,(float)71.174456,(float)latLng.latitude,(float)latLng.longitude);
        Log.e("Distance from ChotaS",Float.toString(Straight_Distance));

      //  Zoom_Value_Camera = Math.round((600+Straight_Distance)/Straight_Distance);
      //  Log.e("Zoom Value",Integer.toString(Zoom_Value_Camera));

        Location CurrentLocation = new Location("Current Location");
        CurrentLocation.setLatitude(latLng.latitude);
        CurrentLocation.setLongitude(latLng.longitude);

        Location Shimla_Location = new Location("Shimla Location");
        Shimla_Location.setLatitude(31.0782882);
        Shimla_Location.setLongitude(77.1240016);

        distance = CurrentLocation.distanceTo(Shimla_Location) / 1000; // in km

        Log.e("DistanceKM",Float.toString(distance));

        if(distance<=15){
            Zoom_Value_Camera = 12;
        }else if(distance >15 && distance <=60){
            Zoom_Value_Camera =9;
        }else if(distance>60 && distance<=300){
            Zoom_Value_Camera = 5;
        }else{
            Zoom_Value_Camera=1;
        }


        //zoom to current position:
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng).zoom(Zoom_Value_Camera).build();  //default was 14

        mMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

        //If you only need one location, unregister the listener
        //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this,"onConnectionFailed",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {



        //Declare Object
         Sending_Object_All_details_Pojo SOAD = null;



        if (mMyMarkersArray.size() > 0) {

try {
    SOAD = new Sending_Object_All_details_Pojo();



    SOAD.setParkingPlace(mMarkersHashMap.get(marker).getParkingPlace());
    SOAD.setParkingArea(mMarkersHashMap.get(marker).getParkingArea());
    SOAD.setRemarks(mMarkersHashMap.get(marker).getRemarks());
    SOAD.setSutedFor(mMarkersHashMap.get(marker).getSutedFor());
    SOAD.setThrashholdValue(mMarkersHashMap.get(marker).getThrashholdValue());
    SOAD.setMinimumParkingFeeSmallCar(mMarkersHashMap.get(marker).getMinimumParkingFeeSmallCar());
    SOAD.setMinimumParkingFeebigCar(mMarkersHashMap.get(marker).getMinimumParkingFeebigCar());
    SOAD.setMinimumParkingTime(mMarkersHashMap.get(marker).getMinimumParkingTime());
    SOAD.setCapacity(mMarkersHashMap.get(marker).getCapacity());
    SOAD.setContactNumber1(mMarkersHashMap.get(marker).getContactNumber1());
    SOAD.setContactNumber2(mMarkersHashMap.get(marker).getContactNumber2());
    SOAD.setContactNumber3(mMarkersHashMap.get(marker).getContactNumber3());
    SOAD.setContactPerson1(mMarkersHashMap.get(marker).getContactPerson1());
    SOAD.setContactPerson2(mMarkersHashMap.get(marker).getContactPerson2());
    SOAD.setContactPerson3(mMarkersHashMap.get(marker).getContactPerson3());
    SOAD.setIdentifier(mMarkersHashMap.get(marker).getIdentifier());
    SOAD.setImage(mMarkersHashMap.get(marker).getImage());
    SOAD.setImage1(mMarkersHashMap.get(marker).getImage1());
    SOAD.setImage2(mMarkersHashMap.get(marker).getImage2());
    SOAD.setLatitude(mMarkersHashMap.get(marker).getLatitude());
    SOAD.setLongitude(mMarkersHashMap.get(marker).getLongitude());
    SOAD.setParkingId(mMarkersHashMap.get(marker).getParkingID());
    SOAD.setPercentage(mMarkersHashMap.get(marker).getPercentage());
    SOAD.setAvailability(mMarkersHashMap.get(marker).getAvailability());
    SOAD.setParkingFullTag(mMarkersHashMap.get(marker).getParkingFullTag());
    SOAD.setRating(mMarkersHashMap.get(marker).getRating());

    if(latLng.latitude!=0) {
        SOAD.setLatitude_my_Location(latLng.latitude);
    }else{
        SOAD.setLatitude_my_Location(000.000);
    }
    if(latLng.longitude!=0) {
        SOAD.setLongitude_my_Location(latLng.longitude);
    }else{
        SOAD.setLongitude_my_Location(000.000);
    }


}catch(Exception e){
    Log.d("Error",e.getLocalizedMessage().toString());
}



            //Now Pass the Object
            Intent userSearch = new Intent();
            userSearch.putExtra("FLAG", "MARKER");
            userSearch.putExtra("DETAILS_ALL", SOAD);
            userSearch.setClass(Main_Activity.this, ParkingDetails_Activity.class);

            startActivity(userSearch);


            }
        else{
            Toast.makeText(getApplicationContext(),"Something Went Wrong. Please restart the application.", Toast.LENGTH_LONG).show();
        }






    }


    @Override
    public void onInfoWindowLongClick(Marker marker) {
       // Toast.makeText(getApplicationContext(),"Please Work Long Press", Toast.LENGTH_LONG).show();
    }




    /* Custom Marker */
    public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter
    {
        public MarkerInfoWindowAdapter()
        {
        }

        @Override
        public View getInfoWindow(Marker marker)
        {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker)
        {
            View v  = getLayoutInflater().inflate(R.layout.infowindow_layout, null);

            final My_Marker_Pojo myMarker = mMarkersHashMap.get(marker);

            ImageView markerIcon = (ImageView)v.findViewById(R.id.marker_icon);
            RatingBar parking_Rating = (RatingBar)v.findViewById(R.id.myRatingBar);
            TextView parking_place = (TextView) v.findViewById(R.id.parking_place);
            TextView identifier= (TextView) v.findViewById(R.id.identifier);
            TextView capacity= (TextView) v.findViewById(R.id.capacity);
            TextView available= (TextView) v.findViewById(R.id.available);
            TextView minparkingtime= (TextView) v.findViewById(R.id.minparkingtime);
            TextView smallcarfare= (TextView) v.findViewById(R.id.smallcarfare);
            TextView bigcarfare= (TextView) v.findViewById(R.id.bigcarfare);
            TextView more= (TextView) v.findViewById(R.id.more);
            TextView markerid = (TextView)v.findViewById(R.id.markerid);
            TextView parkingid = (TextView)v.findViewById(R.id.parkingid);
            // markerIcon.setImageResource(manageMarkerIcon(myMarker.getmIcon()));
            parking_place.setText(myMarker.getParkingPlace());
            identifier.setText(myMarker.getIdentifier());
            capacity.setText(myMarker.getCapacity()+ " vehicles");
            markerid.setText(marker.getId());
            parkingid.setText(myMarker.getParkingID());

            parking_Rating.setRating(Float.valueOf(myMarker.getRating()));

          //  available.setText(myMarker.getAvailability()+"("+myMarker.getPercentage()+"%)");
            if(myMarker.getAvailability().equalsIgnoreCase("Not Known")){
                available.setText(myMarker.getAvailability());
                available.setTextColor(Color.parseColor("#ffa500")); //orange
            }else{
                //Toast.makeText(getApplicationContext(),myMarker.getParkingFullTag().toString(),Toast.LENGTH_LONG).show();
                available.setText(myMarker.getAvailability()+"("+myMarker.getPercentage()+"%)");  //
                // parking_availability.setTextColor(Color.parseColor("#ffa500")); //orange
                if(myMarker.getPercentage().equalsIgnoreCase("0")){
                    available.setTextColor(Color.parseColor("#990000")); //red
                }else if(Integer.parseInt(myMarker.getPercentage())>0 && Integer.parseInt(myMarker.getPercentage())<=25){
                    available.setTextColor(Color.parseColor("#ec7046"));  //wheat
                }else if(Integer.parseInt(myMarker.getPercentage())>25 && Integer.parseInt(myMarker.getPercentage())<=50){
                    available.setTextColor(Color.parseColor("#0000b2")); //blue
                }else if(Integer.parseInt(myMarker.getPercentage())>50 && Integer.parseInt(myMarker.getPercentage())<=75){
                    available.setTextColor(Color.parseColor("#006600")); //Light green
                }else{
                    available.setTextColor(Color.parseColor("#004c00")); //Dark green
                }
            }
            minparkingtime.setText(myMarker.getMinimumParkingTime());
            smallcarfare.setText(myMarker.getMinimumParkingFeeSmallCar()+".00/-");
            bigcarfare.setText(myMarker.getMinimumParkingFeebigCar()+".00/-");

            return v;
        }
    }


    public class Get_Parking_Details extends AsyncTask<String,String,String>{

      //  ProgressDialog dialog;
      private ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            dialog = new ProgressDialog(Main_Activity.this);
            this.dialog.setMessage("Please wait...");
            this.dialog.show();
            this.dialog.setCancelable(false);

        }

        @Override
        protected String doInBackground(String... params) {
            try {
            url_ =new URL(params[0]);
            conn_ = (HttpURLConnection)url_.openConnection();
            conn_.setRequestMethod("GET");
            conn_.setUseCaches(false);
            conn_.setConnectTimeout(20000);
            conn_.setReadTimeout(20000);
            conn_.connect();

                try{
            int HttpResult =conn_.getResponseCode();
            if(HttpResult ==HttpURLConnection.HTTP_OK){
                BufferedReader br = new BufferedReader(new InputStreamReader(conn_.getInputStream(),"utf-8"));
                String line = null;
                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
                }
                br.close();
                System.out.print(sb.toString());

            }else{
                return "Server Connection failed.";
            }}catch(Exception e){
                    return "Server Connection failed.";
                }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            if(conn_!=null)
                conn_.disconnect();
        }
        return sb.toString();
        }



        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(s.equalsIgnoreCase("Server Connection failed.")){
                dialog.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();

            }else{
                if(s.length()>200)
                {
                    //Send String JSON FOr Parsing
                    try {
                        String g_Table = null;
                        Object json = new JSONTokener(s).nextValue();
                        if (json instanceof JSONObject){
                            JSONObject obj = new JSONObject(s);
                            g_Table = obj.optString("getParking_JSONResult");
                        }
                        else if (json instanceof JSONArray){
                        }
                        JSONArray ar = new JSONArray(g_Table);

                        mMyMarkersArray = new ArrayList<>();

                        for (int i = 0; i < ar.length(); i++) {
                            JSONObject obj = ar.getJSONObject(i);
                            // Initialize the HashMap for Markers and MyMarker_Pojo object
                            mMarkersHashMap = new HashMap<>();

                            mMyMarkersArray.add(new My_Marker_Pojo(obj.getString("Capacity"),
                                    obj.getString("ContactNumber1"),
                                    obj.getString("ContactNumber2"),
                                    obj.getString("ContactNumber3"),
                                    obj.getString("ContactPerson1"),
                                    obj.getString("ContactPerson2"),
                                    obj.getString("ContactPerson3"),
                                    obj.getString("Identifier"),
                                    obj.getString("Image"),
                                    obj.getString("Image1"),
                                    obj.getString("Image2"),
                                    obj.getDouble("Latitude"),
                                    obj.getDouble("Longitude"),
                                    obj.getString("ParkingArea"),
                                    obj.getString("ParkingFullTag").trim(),
                                    obj.getString("ParkingPlace"),
                                    obj.getString("Remarks"),
                                    obj.getString("SutedFor"),
                                    obj.getString("ThrashholdValue"),
                                    obj.getString("MinimumParkingFeeSmallCar"),
                                    obj.getString("MinimumParkingFeebigCar"),
                                    obj.getString("MinimumParkingTime"),
                                    obj.getString("percentage"),
                                    obj.getString("Availability"),
                                    obj.getString("ParkingId"),
                                    obj.optString("Rating")));

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }
                }else{

                    Toast.makeText(Main_Activity.this, "Connection to server failed.Please try again.", Toast.LENGTH_SHORT).show();
                }

                if(mMyMarkersArray.size() > 0) {
                    plotMarkers(mMyMarkersArray);
                }else{
                    Log.d("List is","Empty");
                    Toast.makeText(getApplicationContext(),"List Empty",Toast.LENGTH_LONG).show();
                }
                dialog.dismiss();
            }








        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       // MArkerDetails = null;
       // mMarkersHashMap = null;
       // mMyMarkersArray = null;
       // My_Marker_Pojo = null;
        Main_Activity.this.finish();
    }
}
