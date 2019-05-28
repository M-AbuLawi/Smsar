package com.yasoft.smsar.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.support.v4.app.FragmentStatePagerAdapter;

import com.yasoft.smsar.ApartmentType;
import com.yasoft.smsar.HousesType;
import com.yasoft.smsar.MainDiscoverFragment;
import com.yasoft.smsar.VillaType;


public class PropertySectionsPagerAdapter extends FragmentStatePagerAdapter {
    public PropertySectionsPagerAdapter(FragmentManager fm) {

        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
       switch (position) {
            case 0:
                fragment = new MainDiscoverFragment();
                break;
            case 1:
                fragment = new HousesType();
                break;
            case 2:
                fragment = new ApartmentType();
                break;
           case 3:
               fragment = new VillaType();
               break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 4;
    }

   @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "All";
            case 1:
                return "House";
            case 2:
                return "Apartment";
            case 3:
                return "Villa";
        }
        return null;
    }
}
