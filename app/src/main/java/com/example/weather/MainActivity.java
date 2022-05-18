package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    String api_key = "0e4b76af20c376f01b357e23eee67be0", weather_url1 = "";

    TextView textView;
    Button button;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},1);
        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.INTERNET},1);
        button = findViewById(R.id.btVar1);
        textView = findViewById(R.id.textView);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        button.setOnClickListener(view -> {
            // function to find the coordinates
            // of the last location
            System.out.println("Eh");
            obtainLocation();
        });
    }

    @SuppressLint("MissingPermission")
    private void obtainLocation() {
        // get the last location
        fusedLocationProviderClient.getLastLocation()
            .addOnSuccessListener(location -> {
                // get the latitude and longitude
                // and create the http URL
                weather_url1 = "https://api.openweathermap.org/data/2.5/weather?" + "lat=" + location.getLatitude() + "&lon=" + location.getLongitude() + "&appid=" + api_key;
                // this function will
                // fetch data from URL
                System.out.println(weather_url1);
                getTemp();
            });
    }

    void getTemp() {
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
                // get the Array from obj of name - "data"

                JSONObject main = obj.getJSONObject("main");
                // get the JSON object from the
                // array at index position 0
                //JSONObject obj2 = main.getJSONObject("temp");

                // set the temperature and the city
                // name using getString() function
                textView.setText(main.getString("temp") + " deg Fahrenheit");
            } catch (JSONException ignored) {
                // In case of any error
            }
        }, error -> textView.setText("That didn't work!"));
        queue.add(stringReq);
    }
}