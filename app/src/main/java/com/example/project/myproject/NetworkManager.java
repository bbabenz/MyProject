package com.example.project.myproject;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.Callback;

/**
 * Created by B on 20/9/2558.
 */

//TODO Add new sensors type
enum SensorType {
    GYRO,ACCEL,GPS, NONE
}

public class NetworkManager {
    //TODO Add new sensors type
    String ip = MainActivity.getIP();
    private final int allSensor = 3;
    private boolean isComplete[] = new boolean[allSensor];
    private HashMap<String,Double> allData[] = new HashMap[allSensor];
    private String IP = ip;
    private OkHttpClient okHttpClient = new OkHttpClient();
    private Request.Builder builder = new Request.Builder();
    private Context mContext;
    String android_id = MainActivity.getdeviceid();
    private String ANDROIDid = android_id;


    private static class Holder {
        static final NetworkManager INSTANCE = new NetworkManager();
    }

    public static NetworkManager getInstance() {
        return Holder.INSTANCE;
    }

    public void putData(SensorType type,  HashMap<String,Double> valueMap){

        //save data to array
        switch (type){
            case GYRO :{
                allData[getIndex(SensorType.GYRO)] = new HashMap<String, Double>(valueMap);
                isComplete[getIndex(SensorType.GYRO)] = true;
                break;
            }
            case ACCEL :{
                allData[getIndex(SensorType.ACCEL)] = new HashMap<String, Double>(valueMap);

                isComplete[getIndex(SensorType.ACCEL)] = true;
                break;
            }
            case GPS :{
                allData[getIndex(SensorType.GPS)] = new HashMap<String, Double>(valueMap);
                isComplete[getIndex(SensorType.GPS)] = true;
                break;
           }
        }

        //check if all sensor come
        boolean didAllSensorCome = true;
        for(int i = 0;i < allSensor-1; i++){
            if(isComplete[i] == false)
               didAllSensorCome = false;
       }


        //if all sensor sent data then post to server
        if(didAllSensorCome)
            postData();


    }

    private boolean postData(){
//        Long tsLong = System.currentTimeMillis()/1000;
//        String ts = tsLong.toString();
        SimpleDateFormat s = new SimpleDateFormat("ddMMyyyyhhmmss");
        String format = s.format(new Date());

        String URL = "http://"+IP+"/senior_project/main/insert_log?deviceID="+ANDROIDid+"&";
        //TODO send data
        if(isComplete[getIndex(SensorType.GPS)] == true){
        for(int index = 0 ;index < allSensor ; index++){
            Iterator it = allData[index].entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();

                URL += pair.getKey() + "=" + pair.getValue() + "&";
                it.remove(); // avoids a ConcurrentModificationException
            }

        }}
        if(isComplete[getIndex(SensorType.GPS)] == false){
            for(int index = 0 ;index < allSensor-1 ; index++){
                Iterator it = allData[index].entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry)it.next();

                    URL += pair.getKey() + "=" + pair.getValue() + "&";
                    it.remove(); // avoids a ConcurrentModificationException
                }

            }}

        //cut "&" at the final position out of String
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

        //clear all boolean
        for(int i = 0;i < allSensor; i++){
            isComplete[i] = false;
        }
        return true;
    }

    //TODO Add new sensors type
    private int getIndex(SensorType type){
        switch (type){
            case GYRO :{
                return 0;
            }
            case ACCEL :{
                return 1;
            }
            case GPS :{
                return 2;
            }
            default: return -1;
        }
    }

    //TODO Add new sensors type
    private SensorType getType(int index){
        switch (index) {
            case 0: {
                return SensorType.GYRO;
            }
            case 1: {
                return SensorType.ACCEL;
            }
            case 2: {
                return SensorType.GPS;
            }
            default: return SensorType.NONE;
        }
    }
    public void setContext(Context context) {
        mContext = context;
    }


}

