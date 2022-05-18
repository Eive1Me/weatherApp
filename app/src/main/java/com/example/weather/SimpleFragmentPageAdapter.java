package com.example.weather;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

public class SimpleFragmentPageAdapter extends FragmentPagerAdapter {
    int count;
    FragmentManager fm;
    List<Fragment> mFragmentList = new ArrayList<>();
    List<Integer> TABLE = new ArrayList<>();

    public SimpleFragmentPageAdapter(
            FragmentManager fm, int count)
    {
        super(fm);
        this.fm = fm;
        this.count = count;
    }

    @Override
    public Fragment getItem(int position)
    {
        System.out.println("IT HAPPENED" + MainActivity2.citiesList.get(position).getName());
        count = MainActivity2.citiesList.size();
        Fragment my =WeatherFragment.newInstance(MainActivity2.citiesList.get(position).getName());
        mFragmentList.add(my);
        TABLE.add(position);
        return my;
    }

    @Override
    public int getCount()
    {
        return count;
    }

}
