package com.yasoft.smsar;


import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.yasoft.smsar.adapters.CustomAdapter;
import com.yasoft.smsar.models.Property;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class Manage extends Fragment {


    public Manage() {
        // Required empty public constructor
    }

    View root;
    TextView userName ;
    Context mContext;
    CustomAdapter mAdapter;
    FirebaseFirestore db;
    private CollectionReference propertyRef;
    private DocumentReference userRef;
    HashMap<String,Object> list;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

//Need to get id from db and then delete record from the listview and the db;

      root = inflater.inflate(R.layout.fragment_manage, container, false);
        mContext = root.getContext();

        FirebaseApp.initializeApp(mContext);
        db = FirebaseFirestore.getInstance();

        propertyRef = db.collection("Property");



      //  userRef=db.document(Objects.requireNonNull(getUsername()));

 /*       _Fab.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                ((SmsarMainActivity)getActivity()).openAddNew();
            }
        });*/


        userName=root.findViewById(R.id.user) ;

        setUpRecyclerView();

        ((SmsarMainActivity)getActivity()).navPointer(R.id.navigation_dashboard);

        return root;
    }




    private void setUpRecyclerView() {
        Query query = propertyRef.whereEqualTo("mUsername",getUsername());

        FirestoreRecyclerOptions<Property> options = new FirestoreRecyclerOptions.Builder<Property>()
                .setQuery(query, Property.class)
                .build();
        mAdapter = new CustomAdapter(options);

        RecyclerView recyclerView = root.findViewById(R.id.propertyRV);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(mAdapter);

    }


    private String getUsername(){
        Bundle argument;
        argument = getArguments();
        String username="";

        if (argument != null)
          return getArguments().getString("username");

        return username;
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.startListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

    @Override
    public void onPause() {
        super.onPause();
        mAdapter.stopListening();
    }


}
