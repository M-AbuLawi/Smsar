package com.yasoft.smsar;


import android.os.Bundle;
import android.app.Fragment;
import android.support.design.chip.Chip;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.pchmn.materialchips.ChipsInput;

import java.util.ArrayList;
import java.util.List;


public class ShowFilter extends Fragment {


    private TextView _screenRooms,_screenBaths;
    private int numberOfRooms=1,numberOfBathrooms=1;
    ImageButton dec,inc,inc1,dec1;
    public ShowFilter() {

    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
View root;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_filter, container, false);

        _screenRooms= root.findViewById(R.id.numofRooms);
        _screenBaths=root.findViewById(R.id.numofbathroom);

        inc= root.findViewById(R.id.increaseR);
        dec=root.findViewById(R.id.decreaseR);

        inc1=root.findViewById(R.id.increaseB);
        dec1=root.findViewById(R.id.decreaseB);




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

        return root;
    }




    private void updateScreen(){
        _screenRooms.setText(numberOfRooms+"");
        _screenBaths.setText(numberOfBathrooms+"");
    }
}
