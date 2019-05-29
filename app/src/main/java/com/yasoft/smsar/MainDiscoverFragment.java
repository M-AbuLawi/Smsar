package com.yasoft.smsar;


import android.annotation.SuppressLint;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.yasoft.smsar.adapters.DiscoverAdapter;
import com.yasoft.smsar.models.Property;

import org.imperiumlabs.geofirestore.GeoFirestore;
import org.imperiumlabs.geofirestore.GeoQuery;

import java.util.HashMap;
import java.util.Map;
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
    TextView mReset;
    DiscoverAdapter mAdapter;
    FirebaseFirestore db;
    private CollectionReference propertyRef;
    EditText searchBar;
    double latitude, longitude;
    CollectionReference geoFirestoreRef = FirebaseFirestore.getInstance().collection("Property");
    GeoFirestore geoFirestore = new GeoFirestore(geoFirestoreRef);
    ImageButton deleteTextButton;
    // TODO: Rename and change types and number of parameters
    HashMap<String,Object> list;

    boolean flag=false;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=new Bundle();
        bundle=getArguments();
        if (bundle != null) {
            list= (HashMap<String, Object>) bundle.getSerializable("hashmap");

        }
        loadNavBar();
       // Toast.makeText(getContext(), "hii", Toast.LENGTH_SHORT).show();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_main_discover, container, false);
        mReset=root.findViewById(R.id.resetButton);
        mContext = root.getContext();

        FirebaseApp.initializeApp(mContext);
        db = FirebaseFirestore.getInstance();
        propertyRef = db.collection("Property");
        getLocation();
        if(list!=null)
            filterData();

  /*      if(filterQuery!=null) {
            dataFetch(filterQuery, R.id.rentRV);
            mReset.setVisibility(View.VISIBLE);
        }*/

        searchBar = root.findViewById(R.id.search_field);
        deleteTextButton = root.findViewById(R.id.deleteButton);
        //Too load Nav bar for each Activity;
        loadNavBar();
        mReset.setOnClickListener(v->{
            mReset.setVisibility(View.INVISIBLE);
            setUpRecyclerView();
            recentAdded();
            rentAdded();
            sellAdded();
         list=null;
        });
        //mUser.getSupportActionBar().setTitle(R.string.title_Discover);
        if (searchBar.getText().toString().equals(""))
        {
            setUpRecyclerView();
            recentAdded();
            rentAdded();
            sellAdded();
//            if(list!=null)
//                filterData();
    }
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                searchBarListener();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchBarListener();
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchBarListener();
            }
        });

        deleteTextButton.setOnClickListener(v -> searchBar.setText(""));

        return root;
    }



    private void getLocation(){
        UserLocation userLocation=new UserLocation(getActivity(),mContext);
        longitude=userLocation.getLongitude();
        latitude=userLocation.getLatitude();

    }

        private void searchBarListener(){
            String searchText=searchBar.getText().toString();
            if(searchText.equals("")) {
                setUpRecyclerView();
                recentAdded();
                rentAdded();
                sellAdded();
                deleteTextButton.setVisibility(View.INVISIBLE);
            }
            else{
                fireStoreUserSearch(searchText);
                deleteTextButton.setVisibility(View.VISIBLE);

            }

        }

    private static  int LOADIND_LIMIT=10;
    private void setUpRecyclerView() {
        GeoQuery geoQuery = geoFirestore.queryAtLocation(new GeoPoint(37.7832, -122.4056), 0.6);
        Query queryLocation = propertyRef.orderBy("mPrice", Query.Direction.ASCENDING).whereEqualTo("mCity","Amman");
        dataFetch(queryLocation,R.id.discoverRV);

    }

    private void recentAdded(){
        Query fireStoreSearchQuery = propertyRef.orderBy("date").limit(LOADIND_LIMIT);
        dataFetch(fireStoreSearchQuery,R.id.recentRV);

    }

    private void rentAdded(){
        Query fireStoreSearchQuery = propertyRef.orderBy("date").whereEqualTo("type","Rent").limit(LOADIND_LIMIT);
        dataFetch(fireStoreSearchQuery,R.id.rentRV);

    }
    private void sellAdded(){
        Query fireStoreSearchQuery = propertyRef.orderBy("date").whereEqualTo("type","Sell").limit(LOADIND_LIMIT);
        dataFetch(fireStoreSearchQuery,R.id.sellRV);

    }
    private void fireStoreUserSearch(String searchText){
        Query fireStoreSearchQuery = propertyRef.orderBy("mDesc").startAt(searchText).endAt(searchText + "\uf8ff");
        dataFetch(fireStoreSearchQuery,R.id.discoverRV);

    }

private void dataFetch(Query query,int rvView){

        Log.i("Query",query.toString());
    FirestoreRecyclerOptions<Property> options = new FirestoreRecyclerOptions.Builder<Property>()
            .setQuery(query, Property.class)
            .build();

    mAdapter = new DiscoverAdapter(options);
    RecyclerView recyclerView = root.findViewById(rvView);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
    recyclerView.setAdapter(mAdapter);
    mAdapter.startListening();
    mAdapter.setOnItemClickListener((documentSnapshot, position) -> {
        Property property = documentSnapshot.toObject(Property.class);
        String id = documentSnapshot.getId();
        Bundle mBundle = new Bundle();
        mBundle.putString("id", id);
        Toast.makeText(getContext(), id, Toast.LENGTH_SHORT).show();
        ShowProperty showProperty = new ShowProperty();
        showProperty.setArguments(mBundle);
        parseActivity(showProperty);


    });

}

    public void loadNavBar(){
       // String context=mContext.toString();
        if (Objects.requireNonNull(getContext()).toString().contains("Smsar")) {
            ((SmsarMainActivity) Objects.requireNonNull(getActivity())).navPointer(R.id.MainDiscover);
            //       UserMainActivity mUser=(UserMainActivity)mContext;
        } else if (getContext().toString().contains("User")) {
            ((UserMainActivity) Objects.requireNonNull(getActivity())).navPointer(R.id.MainDiscover);
        }

    }

    public void filterData(){

        Query filterQuery =null;
        for(Map.Entry<String, Object> entry : list.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().toString();
            Log.i(key,value);
            filterQuery=propertyRef.whereEqualTo(key,value);
        }
        dataFetch(filterQuery,R.id.rentRV);
    }


    public void parseActivity(Fragment fragment) {
        FragmentManager fragmentManager = null;
        FragmentTransaction fragmentTransaction = null;

        //  Bundle mBundle = new Bundle();
        if (mContext.toString().contains("User")) {
            final UserMainActivity user = (UserMainActivity) mContext;
            fragmentManager = user.getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
            fragmentTransaction.replace(R.id.discoverView,  fragment);


        } else if (mContext.toString().contains("Smsar")) {
            final SmsarMainActivity mSmsar = (SmsarMainActivity) mContext;
            fragmentManager = mSmsar.getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
            fragmentTransaction.replace(R.id.mainView,  fragment);

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
