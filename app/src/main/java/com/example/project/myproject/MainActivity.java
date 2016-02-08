package com.example.project.myproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.OkHttpClient;

import com.squareup.okhttp.Response;
import com.squareup.okhttp.Callback;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static String ip;
    private static String ipAddress;
    private static String androidID;
    private  EditText inputip;
    public static int index = 1;
    private Request.Builder builder = new Request.Builder();
    private OkHttpClient okHttpClient = new OkHttpClient();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final String android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Button button = (Button)findViewById(R.id.btn);


        LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        if(!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //Ask the user to enable GPS
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Location Manager");
            builder.setMessage("Would you like to enable GPS?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Launch settings, allowing user to make a change
                    Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(i);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //No location service, no Activity
                    finish();
                }
            });
            builder.create().show();
        }

        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (!wifi.isConnected()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (!isFinishing()){
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle("Can not connect server")
                                .setMessage("Turn on wi-fi")
                                .setCancelable(false)
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                        System.exit(0);
                                    }
                                }).create().show();
                    }
                }
            });
        }
        else {
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    final String ipaddress = IP.getInstance.getIP(getApplicationContext());
                    ip = ipaddress;
                    final String androidid = AndroidID.getInstance.getandroidid(getApplicationContext());
                    androidID = androidid;
                    String androidname = android.os.Build.MODEL;

                    String URL = "http://" + ipaddress + "/senior_project/main/insert_device?deviceID=" + android_id+"&device_name="+ androidname;
                    URL = URL.substring(0, URL.length() - 1);
                    Log.d("URL", URL);
                    Request request = builder.url(URL).build();
                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Request request, IOException e) {
                            Log.d("onFailure", "Error - " + e.getMessage());
                        }

                        @Override
                        public void onResponse(Response response) {
                            if (response.isSuccessful()) {
                                try {
                                    Log.d("onResponse", response.body().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Log.d("onResponse", "Error - " + e.getMessage());
                                }
                            } else {
                                Log.d("onResponse", "Not Success - code : " + response.code());
                            }
                        }

                    });
                    Intent i= new Intent(MainActivity.this,AndroidVideo.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(i);
//                    Intent intent = new Intent(MainActivity.this, Accelerometer.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);

                }

            });
        }
    }
    public static String getIP()
    {

        return ip;
    }

    public static String getdeviceid()
    {

        return androidID;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



}
