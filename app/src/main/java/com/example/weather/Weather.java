package com.example.weather;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class Weather extends AppCompatActivity {

    public void goMain(View view) {
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }

    String api_key = "0e4b76af20c376f01b357e23eee67be0", weather_url1 = "", location_url1 = "";

    TextView temp_tv,humid_tv,press_tv,location_tv;
    ImageButton menuBtn;
    ImageView imageView;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        ActivityCompat.requestPermissions(Weather.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);
        ActivityCompat.requestPermissions(Weather.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        ActivityCompat.requestPermissions(Weather.this,new String[]{Manifest.permission.INTERNET},1);
        menuBtn = findViewById(R.id.menuBtn);
        location_tv = findViewById(R.id.location_tv);
        temp_tv = findViewById(R.id.temp_tv);
        humid_tv = findViewById(R.id.humid_tv);
        press_tv = findViewById(R.id.press_tv);
        imageView = findViewById(R.id.imageView);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        obtainLocation();
    }

    @SuppressLint("MissingPermission")
    private void obtainLocation() {
        // get the last location
        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    // get the latitude and longitude
                    // and create the http URL
                    weather_url1 = "https://api.openweathermap.org/data/2.5/weather?" + "lat=" + location.getLatitude() + "&lon=" + location.getLongitude() + "&appid=" + api_key + "&lang=ru&units=metric";
                    // this function will
                    // fetch data from URL
                    System.out.println(weather_url1);
                    setInfo();
                });
    }

    void setInfo() {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = weather_url1;
        // Request a string response
        // from the provided URL.
        @SuppressLint("SetTextI18n") StringRequest stringReq = new StringRequest(url, (Response.Listener<String>) response -> {
            // get the JSON object--
            try {
                System.out.println("Goodbye");
                JSONObject obj = new JSONObject(response);
                System.out.println(obj.toString());
                // get the Array from obj of name - "main"
                JSONObject main = obj.getJSONObject("main");
                // set the temperature and the city
                // name using getString() function
                String icon = StringFormatter.reverseString(obj.getJSONArray("weather").getJSONObject(0).getString("icon"));
                System.out.println(icon);
                Context context = imageView.getContext();
                imageView.setImageResource(context.getResources().getIdentifier(icon, "drawable", context.getPackageName()));
                location_tv.setText(obj.getString("name"));
                temp_tv.setText(main.getString("temp") + "℃");
                humid_tv.setText(main.getString("humidity") + "%");
                press_tv.setText(main.getString("pressure") + "мм. рт. ст.");
            } catch (JSONException ignored) {
                // In case of any error
            }
        }, error -> temp_tv.setText("That didn't work!"));
        queue.add(stringReq);
    }

    public static class StringFormatter {
        public static String reverseString(String str){
            StringBuilder sb=new StringBuilder(str);
            sb.reverse();
            return sb.toString();
        }
    }
}