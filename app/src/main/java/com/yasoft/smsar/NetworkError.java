package com.yasoft.aqarkom;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class NetworkError extends Fragment {

    Button mTry;
    View root;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.network_error, container, false);
            mTry=root.findViewById(R.id.tryAgain);
            mTry.setOnClickListener(v -> replace());

        return root;


    }

    public void replace(){
        ((MainActivity)getActivity()).replaceInterface();

    }


}
