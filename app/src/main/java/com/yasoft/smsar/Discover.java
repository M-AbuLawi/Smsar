package com.yasoft.smsar;



import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.Toast;


import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.yasoft.smsar.adapters.DiscoverAdapter;
import com.yasoft.smsar.models.Property;


/**
 * A simple {@link Fragment} subclass.
 */
public class Discover extends Fragment {


    DBHelper mDBHelper;
    View root;
    Context mContext;
    boolean isLoading = false, empty = false;
    DiscoverAdapter mAdapter;
    FirebaseFirestore db;
    private CollectionReference propertyRef;
    public Discover() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        root = inflater.inflate(R.layout.fragment_discover, container, false);
        String context = root.getContext().toString();

        mContext = root.getContext();
        FirebaseApp.initializeApp(mContext);
        db = FirebaseFirestore.getInstance();
        propertyRef = db.collection("Property");
    //Too load Nav bar for each Activity;
        loadNavBar();
        //mUser.getSupportActionBar().setTitle(R.string.title_Discover);
        setUpRecyclerView();
        final Toolbar toolbar = root.findViewById(R.id.discoverToolBar);
        toolbar.inflateMenu(R.menu.discover_titlebar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                if (menuItem.toString().contains("load")) {
                    Toast.makeText(root.getContext(), "Not me ", Toast.LENGTH_LONG).show();

                    if (!empty) {

                    }

                    Bundle argument;
                    argument = getArguments();

                    if (argument != null)
                        getArguments().clear();


                }

                return false;

            }
        });


        Button mFilter = (Button) root.findViewById(R.id.filter);
        mFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowFilter mFlter = new ShowFilter();
                parseActivity(mFilter);

            }

        });




//        _propertList=(ListView)root.findViewById(R.id.discoverLV);

    /*    int images[]={R.drawable.house1,R.drawable.house2, R.drawable.house3};

        v_flipper=root.findViewById(R.id.v_flipper);

    for(int image:images)
        flipperImages(image);*/
        //  listData();

        Bundle argument;
        argument = getArguments();
        if (argument != null) {
            if (argument.containsKey("NORooms"));


        } else


        if (!empty) {

        }


        return root;

    }
    private static  int LOADIND_LIMIT=5;
    private void setUpRecyclerView() {
        Query query = propertyRef.orderBy("mPrice", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Property> options = new FirestoreRecyclerOptions.Builder<Property>()
                .setQuery(query, Property.class)
                .build();

        mAdapter = new DiscoverAdapter(options);

        RecyclerView recyclerView = root.findViewById(R.id.discoverRV);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(mAdapter);


        mAdapter.setOnItemClickListener((documentSnapshot, position) -> {
            Property property= documentSnapshot.toObject(Property.class);
            String id = documentSnapshot.getId();
            Bundle mBundle= new Bundle();
            mBundle.putString("id",id);
            ShowProperty showProperty=new ShowProperty();
            showProperty.setArguments(mBundle);

            parseActivity(showProperty);


        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.re_load:
                Toast.makeText(root.getContext(), "hit me again", Toast.LENGTH_LONG).show();
                return true;
            case R.id.action_contact:
                Toast.makeText(root.getContext(), "hit me again", Toast.LENGTH_LONG).show();
                return true;
            case R.id.action_setting:
                Toast.makeText(root.getContext(), "hit me again", Toast.LENGTH_LONG).show();
                return true;
            case R.id.action_about:
                Toast.makeText(root.getContext(), "hit me again", Toast.LENGTH_LONG).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }



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

        fragmentTransaction.addToBackStack(null);
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
/*



    public void listData() {



        //  String username = getArguments().getString("username");
        mDBHelper = new DBHelper(root.getContext());
        listItem = mDBHelper.getAllProperty();

        final RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.discoverLV);
        LinearLayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(root.getContext());
        recyclerView.setLayoutManager(mLayoutManager);


        mAdapter = new DiscoverAdapter(listItem);


        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged();

    }

 */

/*    public void flipperImages(int image){
        ImageView imageView=new ImageView(root.getContext());
        imageView.setBackgroundResource(image);
    v_flipper.addView(imageView);
    v_flipper.setFlipInterval(4000);
    v_flipper.setAutoStart(true);


    v_flipper.setInAnimation(root.getContext(),android.R.anim.slide_in_left);
    v_flipper.setOutAnimation(root.getContext(),android.R.anim.slide_out_right);
    }*/
