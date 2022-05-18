package com.example.weather;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import java.util.List;

public class MainActivity2 extends FragmentActivity {
    public static List<City> citiesList;
    DatabaseAdapter adapter;
    ImageButton menuBTN;
    ViewPager2 viewPager;
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
            popupMenu.show();
        });

        viewPager = (ViewPager2)findViewById(R.id.viewpager);
        viewPager.setPageTransformer((view, position) -> {
            final float MIN_SCALE = 0.85f;
            final float MIN_ALPHA = 0.5f;
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0f);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0f);
            }
        });
        sfadapter = new SimpleFragmentPageAdapter(this);
        viewPager.setAdapter(sfadapter);

        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume(){
        adapter.open();

        citiesList = adapter.getCities();
        System.out.println(citiesList.toString().toString());

        sfadapter = new SimpleFragmentPageAdapter(this);
        viewPager.setAdapter(sfadapter);

        adapter.close();
        super.onResume();
    }
}