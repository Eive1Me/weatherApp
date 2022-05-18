package com.example.weather;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeatherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeatherFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String CITY = "";

    // TODO: Rename and change types of parameters
    private String city;

    String api_key = "", weather_url1 = "";

    TextView temp_tv,humid_tv,press_tv,location_tv;
    ImageView imageView;

    public WeatherFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static WeatherFragment newInstance(String city) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putString(CITY, city);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            api_key = Util.getProperty("api",this.requireContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (getArguments() != null) {
            city = getArguments().getString(CITY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        location_tv = (TextView)  getView().findViewById(R.id.location_tv3);
        temp_tv = (TextView)  getView().findViewById(R.id.temp_tv3);
        humid_tv = (TextView)  getView().findViewById(R.id.humid_tv3);
        press_tv = (TextView)  getView().findViewById(R.id.press_tv3);
        imageView = (ImageView)  getView().findViewById(R.id.imageView3);
        setLocation();
    }

    @SuppressLint("MissingPermission")
    private void setLocation() {
        weather_url1 = "http://api.openweathermap.org/geo/1.0/direct?q=" + city + "&appid=" + api_key + "&lang=ru&units=metric";
        System.out.println(weather_url1);
        setCoords();
    }

    String lat ="",lon = "";
    RequestQueue queue;

    void setCoords(){
        queue = Volley.newRequestQueue(this.requireContext());
        String url = weather_url1;
        System.out.println("hellll");
        StringRequest stringReq = new StringRequest(url, (Response.Listener<String>) response1 -> {
            // get the JSON object--
            try {
                System.out.println("Goodbye1");
                //System.out.println(response1);
                JSONObject obj = new JSONArray(response1).getJSONObject(0);
                lat = obj.getString("lat");
                lon = obj.getString("lon");
                System.out.println(lat + " " + lon);

            } catch (JSONException ignored) {
                // In case of any error
                System.out.println("help");
            }
        }, error -> System.out.println("aaa"));
        queue.add(stringReq);
        queue.addRequestFinishedListener(request -> setInfo());
    }

    void setInfo() {
        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(this.requireContext());
        String url = "https://api.openweathermap.org/data/2.5/weather?" + "lat=" + lat + "&lon=" + lon + "&appid=" + api_key + "&lang=ru&units=metric";
        // Request a string response
        // from the provided URL.
        System.out.println("hel");
        @SuppressLint("SetTextI18n") StringRequest stringReq1 = new StringRequest(url, (Response.Listener<String>) response -> {
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
        }, error -> System.out.println("That didn't work!"));
        queue.add(stringReq1);
    }

    public static class StringFormatter {
        public static String reverseString(String str){
            StringBuilder sb=new StringBuilder(str);
            sb.reverse();
            return sb.toString();
        }
    }
}