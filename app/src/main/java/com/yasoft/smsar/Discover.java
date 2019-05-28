package com.yasoft.smsar;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.Toast;


import com.yasoft.smsar.adapters.PropertySectionsPagerAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class Discover extends Fragment {


    public Discover() {
        // Required empty public constructor
    }


    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private PropertySectionsPagerAdapter mSectionsPagerAdapter;
    private ImageButton filter;


    View root;
    Context mContext;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        root = inflater.inflate(R.layout.fragment_discover, container, false);
        String context = root.getContext().toString();
        mContext = root.getContext();


        setUpAdapter(context);


        // Set up the ViewPager with the sections adapter.
        mViewPager = root.findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = root.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(mViewPager);

        filter = root.findViewById(R.id.filter);

        filter.setOnClickListener(v -> runFilter());

        return root;

    }


    private void setUpAdapter(String activity) {
        SmsarMainActivity mSmsar;
        UserMainActivity mUser;
        if (activity.contains("Smsar")) {
            mSmsar = (SmsarMainActivity) mContext;
            mSectionsPagerAdapter = new PropertySectionsPagerAdapter(mSmsar.getSupportFragmentManager());
        } else if (activity.contains("User")) {
            mUser = (UserMainActivity) mContext;
            mSectionsPagerAdapter = new PropertySectionsPagerAdapter(mUser.getSupportFragmentManager());
        }

    }

    private void runFilter() {

        ShowFilter showFilter = new ShowFilter();
        Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainView, showFilter, "filter")
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void onResume() {
        super.onResume();
        setUpAdapter(Objects.requireNonNull(getContext()).toString());

    }
}