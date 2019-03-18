package com.yasoft.smsar;


import android.os.Bundle;
import android.app.Fragment;
import android.support.design.chip.Chip;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pchmn.materialchips.ChipsInput;

import java.util.ArrayList;
import java.util.List;


public class ShowFilter extends Fragment {



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
            final Chip mChip;
            mChip=(Chip)root.findViewById(R.id.chip1);
            mChip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mChip.performCloseIconClick();
                }
            });
        // get ChipsInput view



        return root;
    }


}
