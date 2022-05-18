package com.example.weather;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class SimpleFragmentPageAdapter extends FragmentPagerAdapter {
    int count;

    public SimpleFragmentPageAdapter(
            FragmentManager fm, int count)
    {
        super(fm);
        this.count = count;
    }

    @Override
    public Fragment getItem(int position)
    {
        count = MainActivity2.citiesList.size();
        return WeatherFragment.newInstance(MainActivity2.citiesList.get(position).getName());
    }

    @Override
    public int getCount()
    {
        return count;
    }
}
