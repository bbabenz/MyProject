package com.example.project.myproject;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;
import android.widget.VideoView;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class Accelerometer extends Activity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    GoogleApiClient googleApiClient;
  //  TextView textView;
  // TextView textX1, textY1, textZ1,textX2, textY2, textZ2;
    SensorManager sensorManager;
    Sensor sensor1,sensor2;
    double x1,y1,z1,x2,y2,z2;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer);

//start video
        Intent i= new Intent(Accelerometer.this,AndroidVideo.class);
        startActivity(i);
//connect google api for gps
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor1 = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensor2 = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

//        textX1 = (TextView) findViewById(R.id.textX1);
//        textY1 = (TextView) findViewById(R.id.textY1);
//        textZ1 = (TextView) findViewById(R.id.textZ1);
//        textX2 = (TextView) findViewById(R.id.textX2);
//        textY2 = (TextView) findViewById(R.id.textY2);
//        textZ2 = (TextView) findViewById(R.id.textZ2);
//        textView = (TextView) findViewById(R.id.text_view);

    }

    @Override
    public void onStart() {
        super.onStart();

        // Connect to Google API Client
        googleApiClient.connect();
    }



    @Override
    public void onConnected(Bundle bundle) {
        // Do something when connected with Google API Client
        LocationAvailability locationAvailability = LocationServices.FusedLocationApi.getLocationAvailability(googleApiClient);
        if (locationAvailability.isLocationAvailable()) {
            // Call Location Services
            LocationRequest locationRequest = new LocationRequest()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(1000);
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        } else {
            // Do something when Location Provider not available
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        // Do something when Google API Client connection was suspended

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Do something when Google API Client connection failed

    }

    @Override
    public void onLocationChanged(Location location) {
        // Do something when got new current location
        double latitude = (double) location.getLatitude();
        double longitude = (double) location.getLongitude();

       // textView.setText("Latitude : " + location.getLatitude() + "\n" + "Longitude : " + location.getLongitude());
        HashMap<String,Double> data = new HashMap<String, Double>();
        data.put("latitude",latitude);
        data.put("longitude",longitude);


        NetworkManager.getInstance().putData(SensorType.GPS, data);
    }


    public void onResume() {
        super.onResume();

        sensorManager.registerListener(accelListener, sensor1, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(gyroListener, sensor2, SensorManager.SENSOR_DELAY_NORMAL);

    }



    public void onStop() {
        super.onStop();
        //if (googleApiClient != null && googleApiClient.isConnected()) {
            // Disconnect Google API Client if available and connected
           // googleApiClient.disconnect();
       // }
//        sensorManager.unregisterListener(accelListener);
//        sensorManager.unregisterListener(gyroListener);


    }

    SensorEventListener accelListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor1, int acc) { }

        public void onSensorChanged(SensorEvent event) {
            x1 = event.values[0];
            y1 = event.values[1];
            z1 = event.values[2];

            //textX1.setText("X : " + (int)x1);
            //textY1.setText("Y : " + (int) y1);
            //textZ1.setText("Z : " + (int) z1);

            HashMap<String,Double> data = new HashMap<String, Double>();
            data.put("x_axis",x1);
            data.put("y_axis",y1);
            data.put("z_axis",z1);

            NetworkManager.getInstance().putData(SensorType.ACCEL, data);
        }
    };
    SensorEventListener gyroListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor2, int acc) { }

        public void onSensorChanged(SensorEvent event) {
            x2 = event.values[0];
            y2 = event.values[1];
            z2 = event.values[2];

            //textX2.setText("X : " + (int)x2+ " rad/s");
            //textY2.setText("Y : " + (int)y2+ " rad/s");
            //textZ2.setText("Z : " + (int)z2+ " rad/s");

            HashMap<String,Double> data = new HashMap<String, Double>();
            data.put("x_gyro",x2);
            data.put("y_gyro",y2);
            data.put("z_gyro",z2);

            NetworkManager.getInstance().putData(SensorType.GYRO, data);
        }
    };




}