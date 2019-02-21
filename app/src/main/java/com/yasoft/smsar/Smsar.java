package com.yasoft.smsar;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;



/**
 * A simple {@link Fragment} subclass.
 */
public class Smsar extends Fragment {


    public Smsar() {
        // Required empty public constructor
    }

    View root;
   Button mSmsar,mFinder;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        root= inflater.inflate(R.layout.fragment_smsar, container, false);

        mSmsar=(Button)root.findViewById(R.id.smsar);
        mFinder=(Button)root.findViewById(R.id.finder);
        mSmsar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
            getActivity().onBackPressed();

            }
        });

        mFinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MainActivity)getActivity()).closeSmsar();
                //Open UserMainActivity.

            }
        });



        return root;
    }



}
