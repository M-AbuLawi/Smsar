package com.yasoft.smsar;


import android.os.Bundle;
import android.app.Fragment;
import android.support.design.chip.Chip;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.pchmn.materialchips.ChipsInput;

import java.util.ArrayList;
import java.util.List;


public class ShowFilter extends Fragment {


    private TextView _screenRooms,_screenBaths,mApply,mReset;
    private int numberOfRooms=1,numberOfBathrooms=1;
    ImageButton dec,inc,inc1,dec1;
    CheckBox mBox;
    boolean parking,mFiltered=false;
    Discover mDiscover=new Discover();
    Bundle bundel;

    public ShowFilter() {

    }


    View root;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_filter, container, false);

        hideNavigationBar();

        _screenRooms= root.findViewById(R.id.numofRooms);
        _screenBaths=root.findViewById(R.id.numofbathroom);

        inc= root.findViewById(R.id.increaseR);
        dec=root.findViewById(R.id.decreaseR);

        inc1=root.findViewById(R.id.increaseB);
        dec1=root.findViewById(R.id.decreaseB);

        mApply=root.findViewById(R.id.apply);
        mReset=root.findViewById(R.id.reset);

        bundel =new Bundle();

        inc.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {
                ++numberOfRooms;
                updateScreen();
            }
        });

        dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                --numberOfRooms;
                updateScreen();
            }
        });


        inc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++numberOfBathrooms;
                updateScreen();
            }
        });

        dec1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                --numberOfBathrooms;
                updateScreen();
            }
        });


        mBox=root.findViewById(R.id.parkingAsk);
        //mSwitch;


        mApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parking=mBox.isChecked();
                ((UserMainActivity)getActivity()).parseDataFilter(numberOfRooms,numberOfBathrooms,parking);
                getActivity().getFragmentManager().popBackStack();
            }
        });

        bundel.putBoolean("Filtered",mFiltered);
        mReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFiltered=false;
                mBox.setChecked(false);
                numberOfBathrooms=1;
                numberOfRooms=1;
                updateScreen();

            }
        });

   //     ((UserMainActivity)getActivity()).parseDataFilter(numberOfRooms,numberOfBathrooms,parking);

        return root;
    }




    private void updateScreen(){

                check();

        _screenRooms.setText(numberOfRooms+"");
        _screenBaths.setText(numberOfBathrooms+"");
    }


    void check(){
        if(numberOfRooms<0)
            numberOfRooms=0;
        if(numberOfBathrooms<0)
            numberOfBathrooms=0;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mDiscover.setArguments(bundel);
        if(root.getContext().toString().contains("User")) {
            ((UserMainActivity)getActivity()).showBottomNavigationView();

        }
        else if(root.getContext().toString().contains("StartInterface"))
        {
            ((SmsarMainActivity)getActivity()).showBottomNavigationView();
        }
    }

            private void hideNavigationBar(){

                //Hide navigation bar
                //START
                if(root.getContext().toString().contains("User")) {
                    ((UserMainActivity)getActivity()).hideBottomNavigationView();

                }
                else if(root.getContext().toString().contains("Smsar"))
                {
                    ((SmsarMainActivity)getActivity()).hideBottomNavigationView();
                }
                //END

            }


}
