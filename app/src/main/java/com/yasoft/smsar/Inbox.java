package com.yasoft.smsar;



import android.os.Bundle;
import android.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quinny898.library.persistentsearch.SearchBox;


public class Inbox extends Fragment {


    public Inbox() {
        // Required empty public constructor
    }



    private SearchBox search;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    View root;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root= inflater.inflate(R.layout.fragment_inbox, container, false);
        search = (SearchBox) root.findViewById(R.id.searchbox);
        search.enableVoiceRecognition(this);
        ((SmsarMainActivity)getActivity()).navPointer(R.id.navigation_inbox);

    return root;
    }



}
