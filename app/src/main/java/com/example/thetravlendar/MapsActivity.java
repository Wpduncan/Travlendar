package com.example.thetravlendar;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.toolbox.HttpResponse;
import com.example.thetravlendar.Utils.getData;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    String TAG = "placeautocomplete";
    TextView txtView;
    EditText eventView;
    LatLng locationEntered;
    private GoogleMap mMap;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;
    private Marker mCurrentMarker;
    String streetNum;
    String city;
    String state;
    String zip;
    String name;
    String MOD;

    Double lat = 0.0;
    Double longt = 0.0;

    private String traveltime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Intent intent = getIntent();
        MOD = intent.getStringExtra("MOD");
        MOD = MOD.toLowerCase();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);



        txtView = findViewById(R.id.txtView);
        eventView = findViewById(R.id.event_location);

        // Initialize Places.
        Places.initialize(getApplicationContext(), "AIzaSyDAhzX0Vvqd5Xnv7eyUHr5drHWdQwZgeq8");
        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,
                Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.LAT_LNG));

        // Set up a PlaceSelectionListener to handle the response.

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                //For Testing
                //txtView.setText(place.getName() + "," + place.getAddress());
                //
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getAddress());


                StringTokenizer address = new StringTokenizer(place.getAddress(), ",");
                streetNum = address.nextToken();  // Street name and number
                city = address.nextToken(); // City name
                StringTokenizer stateZip = new StringTokenizer(address.nextToken(), " ");
                state = stateZip.nextToken(); // State Name
                zip = stateZip.nextToken(); // Zip code
                name = place.getName();
                locationEntered = place.getLatLng();


                {
                    mMap.clear();
                }
                mCurrentMarker = mMap.addMarker(new MarkerOptions()
                        .position(locationEntered)
                        .title(place.getName()));
                mMap.moveCamera(CameraUpdateFactory
                        .newLatLngZoom(locationEntered, DEFAULT_ZOOM));





                /*https://maps.googleapis.com/maps/api/distancematrix/json?origins=Vancouver+BC|Seattle&destinations=San+Francisco|Victoria+BC&mode=bicycling&language=fr-FR&key=AIzaSyDAhzX0Vvqd5Xnv7eyUHr5drHWdQwZgeq8
                 */
                String lat2 = Double.toString(lat);
                String longt2 = Double.toString(longt);
                String stringUrl = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + lat2 + "," + longt2 + "&destinations=" + place.getAddress() + "&mode=" + MOD + "&language=fr-FR&avoid=tolls&key=AIzaSyDAhzX0Vvqd5Xnv7eyUHr5drHWdQwZgeq8";
                /*String stringUrl = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=Vancouver+BC|Seattle&destinations=San+Francisco|Victoria+BC&mode=bicycling&language=fr-FR&key=AIzaSyDAhzX0Vvqd5Xnv7eyUHr5drHWdQwZgeq8";
                 */
                new getData(MapsActivity.this).execute(stringUrl);
                /*
                String time = txtView.getText().toString();
                displayToast(time); */



                /* try {
                    URL url = new URL("https://maps.googleapis.com/maps/api/distancematrix/json?origins=Vancouver+BC|Seattle&destinations=San+Francisco|Victoria+BC&mode=bicycling&language=fr-FR&key=AIzaSyDAhzX0Vvqd5Xnv7eyUHr5drHWdQwZgeq8");
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.disconnect();
                }
                catch (MalformedURLException ex) {
                    Log.e("httptest",Log.getStackTraceString(ex));
                }
                catch (IOException ex) {
                    Log.e("httptest",Log.getStackTraceString(ex));
                } */




                String test = "test " + traveltime;

                //displayToast(traveltime);




            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });




    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();



        getLocationPermission();
        updateLocationUI();
        getDeviceLocation();

        LatLng USA = new LatLng(lat, longt);
        float zoom = 3.5f;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(USA, zoom));


        /*mMap.addMarker(new MarkerOptions()
                .position(new LatLng(10, 10))
                .title("Hello world"));*/


        /*LatLng current = new LatLng(mLastKnownLocation.getLatitude(),
                mLastKnownLocation.getLongitude());*/




    }
    public void setDouble(String result) {

        String res[]=result.split(",");
        Double min=Double.parseDouble(res[0])/60;
        int minuets = (int) (min % 60);
        int hours = (int) (min / 60);
        traveltime = String.format("%02d", hours) + ":" + String.format("%02d", minuets);
        txtView.setText(streetNum);
        Intent i = new Intent(MapsActivity.this, AddEventActivity.class);
        i.putExtra("street", streetNum);
        i.putExtra("city", city);
        i.putExtra("state", state);
        i.putExtra("zip", zip);
        i.putExtra("name", name);
        i.putExtra("time", traveltime);
        setResult(RESULT_OK, i);
        finish();

        txtView.setText(traveltime);
        /*txtView.setText(traveltime);
         * */


    }

    private void getDeviceLocation() {
        try {

            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            lat = mLastKnownLocation.getLatitude();
                            longt = mLastKnownLocation.getLongitude();
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {

            Log.e("Exception: %s", e.getMessage());
        }
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {

                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);

            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_SHORT).show();
    }



    /** @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.map_options, menu);
    return true;
    }
     @Override
     public boolean onOptionsItemSelected(MenuItem item) {
     // Change the map type based on the user's selection.
     switch (item.getItemId()) {
     case R.id.normal_map:
     mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
     return true;
     case R.id.hybrid_map:
     mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
     return true;
     case R.id.satellite_map:
     mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
     return true;
     case R.id.terrain_map:
     mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
     return true;
     default:
     return super.onOptionsItemSelected(item);
     }
     }*/




}