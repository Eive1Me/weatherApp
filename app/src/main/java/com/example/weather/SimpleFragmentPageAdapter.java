package com.example.weather;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;


public class SimpleFragmentPageAdapter extends FragmentStateAdapter {

    public SimpleFragmentPageAdapter(FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return WeatherFragment.newInstance(MainActivity2.citiesList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return MainActivity2.citiesList.size();
    }
}
