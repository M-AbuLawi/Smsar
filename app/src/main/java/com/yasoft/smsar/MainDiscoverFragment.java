package com.yasoft.smsar;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.yasoft.smsar.adapters.DiscoverAdapter;
import com.yasoft.smsar.models.Property;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainDiscoverFragment extends Fragment {


    public MainDiscoverFragment() {
        // Required empty public constructor
    }


    View root;
    Context mContext;

    DiscoverAdapter mAdapter;
    FirebaseFirestore db;
    private CollectionReference propertyRef;
    EditText searchBar;
    ImageButton searchBttn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root= inflater.inflate(R.layout.fragment_main_discover, container, false);

        mContext = root.getContext();
        FirebaseApp.initializeApp(mContext);
        db = FirebaseFirestore.getInstance();
        propertyRef = db.collection("Property");

        searchBar=root.findViewById(R.id.search_field);
        searchBttn=root.findViewById(R.id.search_btn);
        //Too load Nav bar for each Activity;
        loadNavBar();

        //mUser.getSupportActionBar().setTitle(R.string.title_Discover);
        if(searchBar.getText().toString().equals(""))
            setUpRecyclerView();


        searchBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText=searchBar.getText().toString();
                Toast.makeText(mContext, searchText, Toast.LENGTH_LONG).show();
                if(searchText.equals(""))
                    setUpRecyclerView();
                else
                    fireStoreUserSearch(searchText);

            }
        });
        return root;
    }
    private static  int LOADIND_LIMIT=5;
    private void setUpRecyclerView() {
        Query query = propertyRef.orderBy("mPrice", Query.Direction.ASCENDING).whereEqualTo("mCity","Amman");
        FirestoreRecyclerOptions<Property> options = new FirestoreRecyclerOptions.Builder<Property>()
                .setQuery(query, Property.class)
                .build();

        mAdapter = new DiscoverAdapter(options);

        RecyclerView recyclerView = root.findViewById(R.id.discoverRV);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(mAdapter);
        mAdapter.startListening();

        mAdapter.setOnItemClickListener((documentSnapshot, position) -> {
            Property property = documentSnapshot.toObject(Property.class);
            String id = documentSnapshot.getId();
            Bundle mBundle = new Bundle();
            mBundle.putString("id", id);
            ShowProperty showProperty = new ShowProperty();
            showProperty.setArguments(mBundle);

            MainDiscoverFragment.this.parseActivity(showProperty);


        });

    }
    private void fireStoreUserSearch(String searchText){
        Query fireStoreSearchQuery = propertyRef.orderBy("mDesc").startAt(searchText).endAt(searchText + "\uf8ff");
        FirestoreRecyclerOptions<Property> options = new FirestoreRecyclerOptions.Builder<Property>()
                .setQuery(fireStoreSearchQuery, Property.class)
                .build();

        mAdapter = new DiscoverAdapter(options);

        RecyclerView recyclerView = root.findViewById(R.id.discoverRV);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext,LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setAdapter(mAdapter);
        mAdapter.startListening();
        mAdapter.setOnItemClickListener((documentSnapshot, position) -> {
            Property property = documentSnapshot.toObject(Property.class);
            String id = documentSnapshot.getId();
            Bundle mBundle = new Bundle();
            mBundle.putString("id", id);
            ShowProperty showProperty = new ShowProperty();
            showProperty.setArguments(mBundle);

            MainDiscoverFragment.this.parseActivity(showProperty);


        });
    }



    public void loadNavBar(){
        String context=mContext.toString();

        if (context.contains("Smsar")) {
            ((SmsarMainActivity) getActivity()).navPointer(R.id.navigation_discover);
            //       UserMainActivity mUser=(UserMainActivity)mContext;
        } else if (context.contains("User")) {
            ((UserMainActivity) getActivity()).navPointer(R.id.navigation_discover);
        }

    }


    public <T> void parseActivity(T fragment) {
        FragmentManager fragmentManager = null;
        FragmentTransaction fragmentTransaction = null;

        //  Bundle mBundle = new Bundle();
        if (mContext.toString().contains("User")) {
            final UserMainActivity user = (UserMainActivity) mContext;
            fragmentManager = user.getFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
            fragmentTransaction.replace(R.id.discoverView, (android.app.Fragment) fragment);


        } else if (mContext.toString().contains("Smsar")) {
            final SmsarMainActivity mSmsar = (SmsarMainActivity) mContext;
            fragmentManager = mSmsar.getFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
            fragmentTransaction.replace(R.id.mainView, (android.app.Fragment) fragment);

        }

        Objects.requireNonNull(fragmentTransaction).addToBackStack(null);
        fragmentTransaction.commit();

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
