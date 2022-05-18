package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import java.util.List;

public class MainActivity2 extends AppCompatActivity {
    public static List<City> citiesList;
    DatabaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        adapter = new DatabaseAdapter(this);
        adapter.open();

        citiesList = adapter.getCities();
        System.out.println(citiesList);

        adapter.close();
        ViewPager viewPager
                = (ViewPager)findViewById(
                R.id.viewpager);
        SimpleFragmentPageAdapter
                adapter
                = new SimpleFragmentPageAdapter(
                getSupportFragmentManager(),citiesList.size());

        // Set the adapter onto
        // the view pager
        viewPager.setAdapter(adapter);
    }

    @Override
    protected void onResume(){
        super.onResume();

        adapter.open();

        citiesList = adapter.getCities();

        adapter.close();
    }
}