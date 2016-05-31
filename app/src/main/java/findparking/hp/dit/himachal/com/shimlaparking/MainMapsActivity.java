package findparking.hp.dit.himachal.com.shimlaparking;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
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

public class MainMapsActivity extends AppCompatActivity implements

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

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    LatLng latLng;
    Marker currLocationMarker;
    private GoogleMap mMap;
    private ArrayList<My_Marker> mMyMarkersArray = null;
    private  HashMap<Marker, My_Marker> mMarkersHashMap = null;
    List<Get_Parking_Details> tasks;
    ProgressBar pb;
    URL url_;
    HttpURLConnection conn_;
    StringBuilder sb = new StringBuilder();


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


    }



    private void plotMarkers(ArrayList<My_Marker> markers) {
        if (markers.size() > 0) {
            for (My_Marker myMarker : markers) {

                System.out.println(markers.size() );

                // Create user marker with custom icon and other options
                MarkerOptions markerOption = new MarkerOptions().position(new LatLng(myMarker.getLatitude(), myMarker.getLongitude()));


                System.out.println(myMarker.getParkingFullTag() + "###" +myMarker.getParkingFullTag().toUpperCase() +"###"+ myMarker.getParkingFullTag().length() );
                 if(myMarker.getParkingFullTag().length()==5){
                     markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.pavailable));
                 }else{
                     markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.pnotavailable));
                 }/*else{
                    //Toast.makeText(getApplicationContext(),myMarker.getParkingFullTag().toString(),Toast.LENGTH_LONG).show();
                    markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.pp));
                }*/


                Marker currentMarker = mMap.addMarker(markerOption);
                mMarkersHashMap.put(currentMarker, myMarker);

                mMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter());
            }
        }
    }

   /* private int manageMarkerIcon(String markerIcon) {
        if (markerIcon.equals("ic_launcher"))
            return R.drawable.ic_launcher;
        else if (markerIcon.equals("ic_launcher"))
            return R.drawable.ic_launcher;
        else if (markerIcon.equals("ic_launcher"))
            return R.drawable.ic_launcher;
        else if (markerIcon.equals("ic_launcher"))
            return R.drawable.ic_launcher;
        else if (markerIcon.equals("ic_launcher"))
            return R.drawable.ic_launcher;
        else if (markerIcon.equals("ic_launcher"))
            return R.drawable.ic_launcher;
        else if (markerIcon.equals("ic_launcher"))
            return R.drawable.ic_launcher;
        else
            return R.drawable.icondefault;
    }*/




    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnInfoWindowLongClickListener(this);
try {
    mMap.setMyLocationEnabled(true);
}catch(SecurityException s){
    Toast.makeText(getApplicationContext(),"Not Good",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this, "Fetching your location.", Toast.LENGTH_SHORT).show();
                // Return false so that we don't consume the event and the default behavior still occurs
                // (the camera animates to the user's current position).
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
             Toast.makeText(getApplicationContext(),"There is a problem with the GPS device.",Toast.LENGTH_SHORT).show();
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
    Toast.makeText(getApplicationContext(),"Something's not Good.",Toast.LENGTH_LONG).show();
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

        //zoom to current position:
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng).zoom(13).build();  //default was 14

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
         Sending_Object_All_details SOAD = null;




        //  int id_ = Integer.parseInt(marker.getId())-1;
         // Toast.makeText(getApplicationContext(),mMyMarkersArray.size()+"$$$$$$"+marker.getId().toString(),Toast.LENGTH_LONG).show();
        String id_GetData = removeFirstCharacter(marker.getId().toString());
        int id_ = Integer.parseInt(id_GetData)-1;

        if (mMyMarkersArray.size() > 0) {
try {
    SOAD = new Sending_Object_All_details();

    SOAD.setParkingPlace(mMyMarkersArray.get(id_).getParkingPlace());
    SOAD.setParkingArea(mMyMarkersArray.get(id_).getParkingArea());
    SOAD.setParkingFullTag(mMyMarkersArray.get(id_).getParkingFullTag());
    SOAD.setRemarks(mMyMarkersArray.get(id_).getRemarks());
    SOAD.setSutedFor(mMyMarkersArray.get(id_).getSutedFor());
    SOAD.setThrashholdValue(mMyMarkersArray.get(id_).getThrashholdValue());
    SOAD.setMinimumParkingFeeSmallCar(mMyMarkersArray.get(id_).getMinimumParkingFeeSmallCar());
    SOAD.setMinimumParkingFeebigCar(mMyMarkersArray.get(id_).getMinimumParkingFeebigCar());
    SOAD.setMinimumParkingTime(mMyMarkersArray.get(id_).getMinimumParkingTime());
    SOAD.setCapacity(mMyMarkersArray.get(id_).getCapacity());
    SOAD.setContactNumber1(mMyMarkersArray.get(id_).getContactNumber1());
    SOAD.setContactNumber2(mMyMarkersArray.get(id_).getContactNumber2());
    SOAD.setContactNumber3(mMyMarkersArray.get(id_).getContactNumber3());
    SOAD.setContactPerson1(mMyMarkersArray.get(id_).getContactPerson1());
    SOAD.setContactPerson2(mMyMarkersArray.get(id_).getContactPerson2());
    SOAD.setContactPerson3(mMyMarkersArray.get(id_).getContactPerson3());
    SOAD.setIdentifier(mMyMarkersArray.get(id_).getIdentifier());
    SOAD.setImage(mMyMarkersArray.get(id_).getImage());
    SOAD.setImage1(mMyMarkersArray.get(id_).getImage1());
    SOAD.setImage2(mMyMarkersArray.get(id_).getImage2());
    SOAD.setLatitude(mMyMarkersArray.get(id_).getLatitude());
    SOAD.setLongitude(mMyMarkersArray.get(id_).getLongitude());
    SOAD.setLatitude_my_Location(latLng.latitude);
    SOAD.setLongitude_my_Location(latLng.longitude);
    SOAD.setParkingId(mMyMarkersArray.get(id_).getParkingID());
}catch(Exception e){
    Log.d("Error",e.getLocalizedMessage().toString());
}

            //Now Pass the Object
            Intent userSearch = new Intent();
            userSearch.putExtra("DETAILS_ALL", SOAD);
            userSearch.setClass(MainMapsActivity.this, Details_Parking.class);

            startActivity(userSearch);


            }
        else{
            Toast.makeText(getApplicationContext(),"Something Went Wrong. Please restart the application.", Toast.LENGTH_LONG).show();
        }





    }



    public String removeFirstCharacter(String s){
        return s.substring(1);
    }
    @Override
    public void onInfoWindowLongClick(Marker marker) {
        Toast.makeText(getApplicationContext(),"Please Work Long Press", Toast.LENGTH_LONG).show();
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

            final My_Marker myMarker = mMarkersHashMap.get(marker);

            ImageView markerIcon = (ImageView)v.findViewById(R.id.marker_icon);

            TextView parking_place = (TextView) v.findViewById(R.id.parking_place);
            TextView identifier= (TextView) v.findViewById(R.id.identifier);
            TextView capacity= (TextView) v.findViewById(R.id.capacity);
            TextView available= (TextView) v.findViewById(R.id.available);
            TextView minparkingtime= (TextView) v.findViewById(R.id.minparkingtime);
            TextView smallcarfare= (TextView) v.findViewById(R.id.smallcarfare);
            TextView bigcarfare= (TextView) v.findViewById(R.id.bigcarfare);
            TextView more= (TextView) v.findViewById(R.id.more);


           // markerIcon.setImageResource(manageMarkerIcon(myMarker.getmIcon()));

            parking_place.setText(myMarker.getParkingPlace());
            identifier.setText(myMarker.getIdentifier());
            capacity.setText(myMarker.getCapacity()+ " vehicles");



            System.out.println(myMarker.getParkingFullTag());
            System.out.println(myMarker.getParkingFullTag().toUpperCase());
            if(myMarker.getParkingFullTag().length()==5){
                available.setText("Parking Available");
            } else{
                available.setText("Parking Full");
            }
            minparkingtime.setText(myMarker.getMinimumParkingTime());
            smallcarfare.setText(myMarker.getMinimumParkingFeeSmallCar()+".00/-");
            bigcarfare.setText(myMarker.getMinimumParkingFeebigCar()+".00/-");

            return v;
        }
    }


    public class Get_Parking_Details extends AsyncTask<String,String,String>{

      //  ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

               // dialog.show();
               // dialog.setMessage("Please wait as we are fetching the near by Parking places.");
              //  dialog.setCancelable(false);

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

           // Log.d("PARAMS[0] result is:- ",s);

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
                    // Initialize the HashMap for Markers and MyMarker object
                    mMarkersHashMap = new HashMap<>();

                    mMyMarkersArray.add(new My_Marker(obj.getString("Capacity"),
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
                                                      obj.getString("ParkingId")));

                    System.out.println(obj.getString("ParkingId"));

                }

            } catch (JSONException e) {
                e.printStackTrace();

            }

            if(mMyMarkersArray.size() > 0) {
                plotMarkers(mMyMarkersArray);
            }else{
                Log.d("List is","Empty");
                Toast.makeText(getApplicationContext(),"List Empty",Toast.LENGTH_LONG).show();
            }
           // dialog.dismiss();

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       // MArkerDetails = null;
        mMarkersHashMap = null;
        mMyMarkersArray = null;
       // My_Marker = null;
        MainMapsActivity.this.finish();
    }
}
