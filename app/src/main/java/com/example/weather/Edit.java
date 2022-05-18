package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Edit extends AppCompatActivity {

    private DatabaseAdapter adapter;
    private long cityId =0;

    private EditText nameBox;
    private Button delButton;
    private ImageButton locateBTN;
    String weather_url1 = "",api_key = "";
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        try {
            api_key = Util.getProperty("api",this);
        } catch (IOException e) {
            e.printStackTrace();
        }

        nameBox = findViewById(R.id.name);
        delButton = findViewById(R.id.deleteButton);
        locateBTN = findViewById(R.id.locateBtn);

        adapter = new DatabaseAdapter(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            cityId = extras.getLong("id");
        }
        // если 0, то добавление
        if (cityId > 0) {
            // получаем элемент по id из бд
            adapter.open();
            City city = adapter.getCity(cityId);
            nameBox.setText(city.getName());
            adapter.close();
        } else {
            // скрываем кнопку удаления
            delButton.setVisibility(View.GONE);
        }
        locateBTN.setOnClickListener(view -> {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
            obtainLocation();
        });
    }

    public void save(View view){
        String name = nameBox.getText().toString();
        City user = new City(cityId, name);

        adapter.open();
        if (cityId > 0) {
            adapter.update(user);
        } else {
            adapter.insert(user);
        }
        adapter.close();
        goHome();
    }

    public void delete(View view){
        adapter.open();
        adapter.delete(cityId);
        adapter.close();
        goHome();
    }

    private void goHome(){
        // переход к главной activity
        Intent intent = new Intent(this, MainActivity2.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
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
                // set the temperature and the city
                // name using getString() function
                nameBox.setText(obj.getString("name"));
            } catch (JSONException ignored) {
                // In case of any error
            }
        }, error -> nameBox.setText("Err"));
        queue.add(stringReq);
    }
}