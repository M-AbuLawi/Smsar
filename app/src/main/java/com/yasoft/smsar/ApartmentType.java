package com.yasoft.smsar;


import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.github.florent37.rxgps.RxGps;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.yasoft.smsar.adapters.DiscoverAdapter;
import com.yasoft.smsar.models.Property;

import java.util.Objects;

import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ApartmentType#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ApartmentType extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    Context mContext;
    TextView mReset;
    DiscoverAdapter mAdapter;
    EditText searchBar;
    ImageButton deleteTextButton;

    public ApartmentType() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ApartmentType.
     */
    // TODO: Rename and change types and number of parameters
    public static ApartmentType newInstance(String param1, String param2) {
        ApartmentType fragment = new ApartmentType();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    FirebaseFirestore db;
    private CollectionReference propertyRef;
    View root;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root= inflater.inflate(R.layout.fragment_apartment_type, container, false);
        root=inflater.inflate(R.layout.fragment_type_houses, container, false);
        mReset=root.findViewById(R.id.resetButton);
        mContext = root.getContext();

        FirebaseApp.initializeApp(mContext);
        db = FirebaseFirestore.getInstance();
        propertyRef = db.collection("Property");
        getLocation();

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
    @SuppressLint("CheckResult")
    private void getLocation(){

        new RxGps(Objects.requireNonNull(getActivity())).locationLowPower()

                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.single())

                .subscribe(location -> Log.i( "getLocation: ",location.getLatitude()+""), throwable -> {
                    if (throwable instanceof RxGps.PermissionException) {
                        //the user does not allow the permission
                    } else if (throwable instanceof RxGps.PlayServicesNotAvailableException) {
                        //the user do not have play services
                    }
                });


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
        Query queryLocation = propertyRef.orderBy("mPrice", Query.Direction.ASCENDING).whereEqualTo("mCity","Amman");
        dataFetch(queryLocation,R.id.discoverRV);

    }

    private void recentAdded(){
        Query fireStoreSearchQuery = propertyRef.orderBy("date").whereEqualTo("category","House").limit(LOADIND_LIMIT);
        dataFetch(fireStoreSearchQuery,R.id.recentRV);

    }

    private void rentAdded(){
        Query fireStoreSearchQuery = propertyRef.orderBy("date").whereEqualTo("type","Rent").whereEqualTo("category","House").limit(LOADIND_LIMIT);
        dataFetch(fireStoreSearchQuery,R.id.rentRV);

    }
    private void sellAdded(){
        Query fireStoreSearchQuery = propertyRef.orderBy("date").whereEqualTo("type","Sell").whereEqualTo("category","House").limit(LOADIND_LIMIT);
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
    public void loadNavBar(){
        // String context=mContext.toString();
        if (getContext().toString().contains("Smsar")) {
            ((SmsarMainActivity) getActivity()).navPointer(R.id.MainDiscover);
            //       UserMainActivity mUser=(UserMainActivity)mContext;
        } else if (getContext().toString().contains("User")) {
            ((UserMainActivity) getActivity()).navPointer(R.id.MainDiscover);
        }

    }

}
