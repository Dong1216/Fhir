package com.example.fhirsimpleapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class PatientMap extends FragmentActivity implements OnMapReadyCallback {

    Location currentLoc;
    GoogleMap map;
    Intent intent;
    Button locationBtn;
    private FusedLocationProviderClient mFusedLocationClient;
    private static int REQUEST_CODE=101;
    boolean mlocationGranted=false;
    String myAddress;
    Geocoder geocoder;
    PractitionerSample practitioner;
    String senderId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);

        geocoder = new Geocoder(this, Locale.getDefault());
        locationBtn= findViewById(R.id.send_loc_btn);
        locationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAddress();

                intent=getIntent();
                senderId = intent.getStringExtra("sender");
                practitioner = (PractitionerSample) intent.getSerializableExtra("receiver");

                Intent i= new Intent(PatientMap.this, PatientChatting2.class);
                i.putExtra("address",myAddress);
                i.putExtra("receiver", practitioner);
                i.putExtra("sender", senderId);
                startActivity(i);
            }
        });
        getLocationPermission();
    }

    private void getAddress() {
        List<Address> addresses = new ArrayList<>();
        try {
            addresses = geocoder.getFromLocation(currentLoc.getLatitude(), currentLoc.getLongitude(), 1);
            String address = addresses.get(0).getAddressLine(0);
            myAddress=address;

        } catch (IOException e) {
            System.out.println("***********************************"+e);
            e.printStackTrace();
        }
    }

    private  void initMap(){
        SupportMapFragment mapFragment=(SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(PatientMap.this);

    }

    public void getLocationPermission()
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED&&ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Check Permissions Now
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE);
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_CODE);
        }
        else
        {
            mlocationGranted=true;
            initMap();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map=googleMap;
        if (mlocationGranted)
        {
            getDeviceLocation();
            map.setMyLocationEnabled(true);
        }


    }
    private void getDeviceLocation()
    {
        mFusedLocationClient= LocationServices.getFusedLocationProviderClient(this);
        try{
            if (mlocationGranted) {
                Task location=mFusedLocationClient.getLastLocation();
                location.addOnCompleteListener( new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()) {
                            currentLoc=(Location)task.getResult();

                            moveCamera(new LatLng(currentLoc.getLatitude(),currentLoc.getLongitude()));
                        }else{
                            Log.d("map", "Current location is null. Using defaults.");
                            Log.e("map", "Exception: %s", task.getException());

                        }

                    }
                });
            }

        } catch (SecurityException e) {
            Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show(); }
    }
    private  void moveCamera(LatLng latLng)
    {

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        mlocationGranted=false;
        if (requestCode==REQUEST_CODE)
        {
            if (grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                for (int i=0;i<grantResults.length;i++)
                {
                    if (grantResults[i]!=PackageManager.PERMISSION_GRANTED)
                    {
                        mlocationGranted=false;
                        return;
                    }
                }
                mlocationGranted=true;
                initMap();
            }
        }
    }
}
