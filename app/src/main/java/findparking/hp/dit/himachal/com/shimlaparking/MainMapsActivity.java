package findparking.hp.dit.himachal.com.shimlaparking;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;

public class MainMapsActivity extends AppCompatActivity implements

        GoogleMap.OnMyLocationButtonClickListener,
        OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback,
        LocationListener,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks

{


    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private boolean mPermissionDenied = false;

    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;

    LatLng latLng;
    Marker currLocationMarker;


    private GoogleMap mMap;

    private ArrayList<MyMarker> mMyMarkersArray = new ArrayList<MyMarker>();
    private HashMap<Marker, MyMarker> mMarkersHashMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main_maps);
        setContentView(R.layout.my_location_demo);

        // Initialize the HashMap for Markers and MyMarker object
        mMarkersHashMap = new HashMap<Marker, MyMarker>();

        mMyMarkersArray.add(new MyMarker("Khalini", "ic_launcher", Double.parseDouble("31.089749"), Double.parseDouble("77.17067")));
        mMyMarkersArray.add(new MyMarker("Bemloe", "ic_launcher", Double.parseDouble("31.09776"), Double.parseDouble("77.17455")));
        mMyMarkersArray.add(new MyMarker("HHH", "ic_launcher", Double.parseDouble("31.099992"), Double.parseDouble("77.17446")));
        mMyMarkersArray.add(new MyMarker("High Court", "ic_launcher", Double.parseDouble("31.099369"), Double.parseDouble("77.17575")));
        mMyMarkersArray.add(new MyMarker("Sabji Mandi", "ic_launcher", Double.parseDouble("31.102719"), Double.parseDouble("77.17467")));
        mMyMarkersArray.add(new MyMarker("Below Metro Poll", "ic_launcher", Double.parseDouble("31.100736"), Double.parseDouble("77.17529")));
        mMyMarkersArray.add(new MyMarker("Olr Railway Staytion", "ic_launcher", Double.parseDouble("31.104103"), Double.parseDouble("77.16817")));
        mMyMarkersArray.add(new MyMarker("Old Bus Stand", "ic_launcher", Double.parseDouble("31.101348"), Double.parseDouble("77.17098")));


        // setUpMap();
        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    private void plotMarkers(ArrayList<MyMarker> markers) {
        if (markers.size() > 0) {
            for (MyMarker myMarker : markers) {

                // Create user marker with custom icon and other options
                MarkerOptions markerOption = new MarkerOptions().position(new LatLng(myMarker.getmLatitude(), myMarker.getmLongitude()));
                markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.currentlocation_icon));

                Marker currentMarker = mMap.addMarker(markerOption);
                mMarkersHashMap.put(currentMarker, myMarker);

                mMap.setInfoWindowAdapter(new MarkerInfoWindowAdapter());
            }
        }
    }

    private int manageMarkerIcon(String markerIcon) {
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
    }


    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        mMap.setOnMyLocationButtonClickListener(this);
try {
    mMap.setMyLocationEnabled(true);
}catch(SecurityException s){
    Toast.makeText(getApplicationContext(),"Not Good",Toast.LENGTH_SHORT).show();
}

                buildGoogleApiClient();

                mGoogleApiClient.connect();

                enableMyLocation();
                plotMarkers(mMyMarkersArray);
                

            }

    protected synchronized void buildGoogleApiClient() {
        Toast.makeText(this,"buildGoogleApiClient",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
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
            mLocationRequest.setInterval(50000); //5 seconds
            mLocationRequest.setFastestInterval(50000); //3 seconds
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
                .target(latLng).zoom(14).build();

        mMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

        //If you only need one location, unregister the listener
        //LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(this,"onConnectionFailed",Toast.LENGTH_SHORT).show();
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

            final MyMarker myMarker = mMarkersHashMap.get(marker);

            ImageView markerIcon = (ImageView)v.findViewById(R.id.marker_icon);

            TextView markerLabel = (TextView)v.findViewById(R.id.marker_label);

            TextView anotherLabel = (TextView)v.findViewById(R.id.another_label);

            Button b = (Button)v.findViewById(R.id.call);


            markerIcon.setImageResource(manageMarkerIcon(myMarker.getmIcon()));

            markerLabel.setText(myMarker.getmLabel());
            anotherLabel.setText("A custom text");

            return v;
        }
    }
}