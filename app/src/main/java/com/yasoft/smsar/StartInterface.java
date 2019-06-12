package com.yasoft.smsar;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;



/**
 * A simple {@link Fragment} subclass.
 */
public class StartInterface extends Fragment {


    public StartInterface() {
        // Required empty public constructor
    }

    View root;
   Button mSmsar,mFinder;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        root= inflater.inflate(R.layout.fragment_smsar, container, false);

        mSmsar=root.findViewById(R.id.smsar);
        mFinder=root.findViewById(R.id.seeker);
        mSmsar.setOnClickListener(v -> getActivity().onBackPressed());

        mFinder.setOnClickListener(v -> {

            ((MainActivity)getActivity()).closeSmsar();
            //Open UserMainActivity.

        });



        return root;
    }

}
