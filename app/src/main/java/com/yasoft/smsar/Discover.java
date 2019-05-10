package com.yasoft.smsar;



import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.yasoft.smsar.adapters.DiscoverAdapter;
import com.yasoft.smsar.adapters.SectionsPagerAdapter;
import com.yasoft.smsar.models.Property;

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
    private SectionsPagerAdapter mSectionsPagerAdapter;
   @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.re_load:
                Toast.makeText(root.getContext(), "hit me again", Toast.LENGTH_LONG).show();
                return true;
            case R.id.action_contact:
                Toast.makeText(root.getContext(), "hit me again", Toast.LENGTH_LONG).show();
                return true;
            case R.id.action_setting:
                Toast.makeText(root.getContext(), "hit me again", Toast.LENGTH_LONG).show();
                return true;
            case R.id.action_about:
                Toast.makeText(root.getContext(), "hit me again", Toast.LENGTH_LONG).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }



    }

    View root;
   Context mContext;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        root = inflater.inflate(R.layout.fragment_discover, container, false);
    //  String context = root.getContext().toString();
         mContext=root.getContext();

  //      setUpAdapter(context);
             SmsarMainActivity  mSmsar  = (SmsarMainActivity) mContext;
        Toolbar toolbar = root.findViewById(R.id.discoverToolBar);
        mSmsar.setSupportActionBar(toolbar);
          mSectionsPagerAdapter = new SectionsPagerAdapter(mSmsar.getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = root.findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = root.findViewById(R.id.tabLayout);
        tabLayout.setupWithViewPager(mViewPager);


        return root;

    }


    private void setUpAdapter(String activity){
        SmsarMainActivity mSmsar;
        UserMainActivity mUser;
        if(activity.contains("Smsar")){
             mSmsar  = (SmsarMainActivity) mContext;
            mSectionsPagerAdapter = new SectionsPagerAdapter(mSmsar.getSupportFragmentManager());
        }
        else if(activity.contains("User")) {
            mUser = (UserMainActivity) mContext;
            mSectionsPagerAdapter = new SectionsPagerAdapter(mUser.getSupportFragmentManager());
        }

    }
}