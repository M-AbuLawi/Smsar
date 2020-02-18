package com.yasoft.aqarkom.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import androidx.fragment.app.FragmentStatePagerAdapter;

import com.yasoft.aqarkom.ApartmentType;
import com.yasoft.aqarkom.HousesType;
import com.yasoft.aqarkom.MainDiscoverFragment;
import com.yasoft.aqarkom.VillaType;


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
