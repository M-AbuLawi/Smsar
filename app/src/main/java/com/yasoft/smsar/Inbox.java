package com.yasoft.aqarkom;



import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.quinny898.library.persistentsearch.SearchBox;


public class Inbox extends Fragment {

    TextView loginText;
    Button loginB;

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
        root= inflater.inflate(R.layout.fragment_inbox, container, false);
        // Inflate the layout for this fragment
        loginB = root.findViewById(R.id.loginB);
        loginText = root.findViewById(R.id.signUpAction);

    //    search = (SearchBox) root.findViewById(R.id.searchbox);
 //       search.enableVoiceRecognition(this);
        loginText.setVisibility(View.VISIBLE);
        loginB.setVisibility(View.VISIBLE);
    if(MainActivity.SESSION) {
        loginText.setVisibility(View.GONE);
        loginB.setVisibility(View.GONE);
    }

    return root;
    }



}
