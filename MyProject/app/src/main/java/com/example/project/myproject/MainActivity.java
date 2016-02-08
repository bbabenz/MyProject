package com.example.project.myproject;

import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
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
    private  EditText inputip;
    private Request.Builder builder = new Request.Builder();
    private OkHttpClient okHttpClient = new OkHttpClient();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final String android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        Button button = (Button)findViewById(R.id.btn);



        WifiManager wifiMgr = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        final String ipAddress = Formatter.formatIpAddress(ip);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                String URL = "http://192.168.137.1/senior_project/main/insert_device?deviceID="+android_id;
                URL = URL.substring(0,URL.length() - 1);
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

                Intent intent = new Intent(MainActivity.this, Accelerometer.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }

        });

    }
    public static String getIP()
    {

        return ipAddress;
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
