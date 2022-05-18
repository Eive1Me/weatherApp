package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListPopupWindow;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.List;

public class MainActivity2 extends AppCompatActivity {
    public static List<City> citiesList;
    DatabaseAdapter adapter;
    ImageButton menuBTN;
    ViewPager viewPager;
    SimpleFragmentPageAdapter sfadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main2);

        adapter = new DatabaseAdapter(this);
        adapter.open();

        citiesList = adapter.getCities();
        System.out.println(citiesList.size());

        adapter.close();

        menuBTN = findViewById(R.id.menuBtn2);
        menuBTN.setOnClickListener(view -> {
            // Initializing the popup menu and giving the reference as current context
            PopupMenu popupMenu = new PopupMenu(MainActivity2.this, menuBTN);
            for (int i=0; i<citiesList.size();i++) {
                popupMenu.getMenu().add(citiesList.get(i).getName());
            }

            // Inflating popup menu from popup_menu.xml file
            popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                // Toast message on menu item clicked
                Toast.makeText(MainActivity2.this, "You Clicked " + menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                long extra = Long.valueOf("0");
                for (int i=0; i<citiesList.size();i++) {
                    if (citiesList.get(i).getName().equals(menuItem.getTitle()))
                        extra = citiesList.get(i).getId();
                }
                if (menuItem.getItemId() == R.id.addNew)
                    extra = 0;
                Intent intent = new Intent(getApplicationContext(), Edit.class);
                intent.putExtra("id", extra);
                startActivity(intent);
                return true;
            });
            // Showing the popup menu
            popupMenu.show();
        });

        viewPager = (ViewPager)findViewById(R.id.viewpager);
        viewPager.clearFocus();
        sfadapter = new SimpleFragmentPageAdapter(getSupportFragmentManager(),citiesList.size());
        viewPager.setAdapter(sfadapter);
        viewPager.setSaveFromParentEnabled(false);

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume(){
        adapter.open();

        citiesList = adapter.getCities();
        System.out.println(citiesList.toString().toString());

        sfadapter = new SimpleFragmentPageAdapter(getSupportFragmentManager(),citiesList.size());
        viewPager.setAdapter(sfadapter);

        adapter.close();
        super.onResume();
    }
}