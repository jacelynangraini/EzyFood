package com.jacelyn.ezyfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.ToolbarWidgetWrapper;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static androidx.constraintlayout.motion.widget.Debug.getLocation;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, AdapterView.OnItemSelectedListener {
    // Google Maps
    com.google.android.gms.location.FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleMap mMap;
    LatLng rest_1 = new LatLng(-6.176873, 106.790807);
    LatLng rest_2 = new LatLng(-6.1090330225994895, 106.74014991064062);
    LatLng rest_3 = new LatLng(-6.194924107127545, 106.82045192598413);

    // Initialize variables
    public static final String EXTRA_REST_ID="restID";
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Initialize views and variables
        final Spinner spinner = (Spinner) findViewById(R.id.spinner2);
        int restID = 0;
        TextView locPicked = (TextView) findViewById(R.id.textLocation);
        mSwipeRefreshLayout = findViewById(R.id.swiperefresh_items);


        // Refresh
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Intent intent = new Intent(MapsActivity.this, MapsActivity.class);
                startActivity(intent);

//                final Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        if(mSwipeRefreshLayout.isRefreshing()) {
//                            mSwipeRefreshLayout.setRefreshing(false);
//                        }
//                    }
//                }, 1000);
            }
        });

        // Spinner
        spinner.setOnItemSelectedListener(this);

        // Add spinner data
        List<String> labels = new ArrayList<String>();
        labels.add("Central Park");
        labels.add("PIK Avenue");
        labels.add("Grand Indonesia");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            restID = (Integer)getIntent().getExtras().get(EXTRA_REST_ID);
            spinner.setSelection(restID);
            if(restID == 0){
                locPicked.setText("Central Park");
            }
            else if(restID == 1){
                locPicked.setText("PIK Avenue");
            }
            else if(restID == 2){
                locPicked.setText("Grand Indonesia");
            }
        }

        // Spinner listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String text = spinner.getSelectedItem().toString();
                int restId = 0;
                TextView locPicked = (TextView) findViewById(R.id.textLocation);


                if (text == "Central Park") {
                    float zoomLevel = 16.0f;
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(rest_1, zoomLevel));
                    restId = 1;
                    locPicked.setText("Central Park");
                } else if (text == "PIK Avenue") {
                    float zoomLevel = 16.0f;
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(rest_2, zoomLevel));
                    restId = 2;
                    locPicked.setText("PIK Avenue");
                } else if (text == "Grand Indonesia") {
                    float zoomLevel = 16.0f;
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(rest_3, zoomLevel));
                    restId = 3;
                    locPicked.setText("Grand Indonesia");
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        Toolbar t = (Toolbar) findViewById(R.id.toolbar);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setActionBar(toolbar);
//        setSupportActionBar(t);
//        setActionBar(toolbar);
//
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //WHEN GRANTED
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    //initialize location
                    Location location = task.getResult();
                    if(location!=null){
                        try {
                            //initialize geocoder
                            Geocoder geo = new Geocoder(MapsActivity.this, Locale.getDefault());
                            //initialize address list
                            List<Address> addresses = geo.getFromLocation(
                                    location.getLatitude(), location.getLongitude(), 1);

                            double latitude = addresses.get(0).getLatitude();
                            double longitude = addresses.get(0).getLongitude();

                            String address = addresses.get(0).getAddressLine(0);
                            TextView tv3 = (TextView) findViewById(R.id.textView16);
                            tv3.setText(Html.fromHtml(
                                    address
                            ));
                            tv3.setMovementMethod(new ScrollingMovementMethod());

                            LatLng userLoc = new LatLng(latitude, longitude);
                            mMap.addMarker(new MarkerOptions().position(userLoc).title("You are here"));

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            });
        } else {
            //WHEN DENIED
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

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

        mMap.addMarker(new MarkerOptions().position(rest_1).title("Central Park"));
        mMap.addMarker(new MarkerOptions().position(rest_2).title("PIK Avenue"));
        mMap.addMarker(new MarkerOptions().position(rest_3).title("Grand Indonesia"));
        float zoomLevel = 16.0f;
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(rest_1, zoomLevel));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void onMapClicked(View view) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        final Spinner spinner = (Spinner) findViewById(R.id.spinner2);

        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //WHEN GRANTED
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    //initialize location
                    Location location = task.getResult();
                    if(location!=null){
                        try {
                            //initialize geocoder
                            Geocoder geo = new Geocoder(MapsActivity.this, Locale.getDefault());
                            //initialize address list
                            List<Address> addresses = geo.getFromLocation(
                                    location.getLatitude(), location.getLongitude(), 1);

                            double latitude = addresses.get(0).getLatitude();
                            double longitude = addresses.get(0).getLongitude();

                            float[] distanceBetw = new float[3];

                            location.distanceBetween(-6.176873, 106.790807,latitude, longitude,  distanceBetw);
                            location.distanceBetween(-6.1090330225994895, 106.74014991064062,latitude, longitude,  distanceBetw);
                            location.distanceBetween(-6.194924107127545, 106.82045192598413,latitude, longitude,  distanceBetw);

                            int pos = 0;
                            float minValue = distanceBetw[0];
                            if(distanceBetw[1] < minValue){
                                minValue = distanceBetw[1];
                                pos = 1;
                            }
                            if(distanceBetw[2] < minValue){
                                minValue = distanceBetw[2];
                                pos = 2;
                            }

                            TextView locPicked = (TextView) findViewById(R.id.textLocation);
                            spinner.setSelection(pos);
                            if(pos == 0){
                                locPicked.setText("Central Park");

                            }
                            else if(pos == 1){
                                locPicked.setText("PIK Avenue");
                            }
                            else if(pos == 2){
                                locPicked.setText("Grand Indonesia");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            });
        } else {
            //WHEN DENIED
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }

    }



    public void onConfirmLocClicked(android.view.View view){
        Spinner spinner = (Spinner)findViewById(R.id.spinner2);
        String text = spinner.getSelectedItem().toString();
        int restId = 0;

        if(text=="Central Park"){
            Toast toast = Toast.makeText(this, "Selected: Central Park", Toast.LENGTH_SHORT);
            toast.show();
            restId = 0;
        }
        else if(text=="PIK Avenue"){
            Toast toast = Toast.makeText(this, "Selected: PIK Avenue", Toast.LENGTH_SHORT);
            toast.show();
            restId = 1;
        }
        else if(text=="Grand Indonesia"){
            Toast toast = Toast.makeText(this, "Selected: Grand Indonesia", Toast.LENGTH_SHORT);
            toast.show();
            restId = 2;
        }

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.EXTRA_REST_ID, restId);
        startActivity(intent);
    }
}