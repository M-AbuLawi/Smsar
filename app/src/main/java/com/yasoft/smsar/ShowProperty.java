package com.yasoft.smsar;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.FeatureGroupInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;


public class ShowProperty extends Fragment implements OnMapReadyCallback {

    View root;
    private Context mContext;
    ViewGroup actionBarLayout;
    private static String USERNAME = "", PHONENUMBER = "";
    private FirebaseFirestore db;
    private DocumentReference detailsRef;
    private ProgressBar pg;

    LikeButton likeButton;
    private TextView mPrice,mDescription,mCity,mDate ,mBaths,mRooms,mArea,decLabel;
    private ImageView mParking,propertyImage;
    private boolean parking;
    private double lat,lon;
    String username;
    public static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";



    public static ShowProperty newInstance(String param1) {
        ShowProperty fragment = new ShowProperty();
        Bundle args = new Bundle();
        args.putString("username", param1);

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeFireStore();
        if (getArguments() != null) {
         username = getArguments().getString("username");
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                Toast.makeText(root.getContext(), "hello", Toast.LENGTH_LONG).show();
                return true;

            case R.id.action_setting:
                Toast.makeText(root.getContext(), "hello", Toast.LENGTH_LONG).show();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    MapView map;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_show_property, container, false);
        mContext=root.getContext();



        String park = "false";
        //TextView Declaration
        //START
          mPrice = root.findViewById(R.id.price);
          mDescription = root.findViewById(R.id.description);
          mCity = root.findViewById(R.id.address);
          mDate = root.findViewById(R.id.date);
          mBaths = root.findViewById(R.id.bathroomNO);
          mRooms = root.findViewById(R.id.bedroomNO);
          mParking = root.findViewById(R.id.parking);
          mArea = root.findViewById(R.id.area);
          pg=root.findViewById(R.id.loadingItems);
          propertyImage=root.findViewById(R.id.imageP);
        ImageButton mCall = root.findViewById(R.id.call);
        ImageButton mChat = root.findViewById(R.id.chat);
        ImageButton mClose = root.findViewById(R.id.close);
          decLabel=root.findViewById(R.id.descLabel);

            map=root.findViewById(R.id.propertyLocation);

            initGoogleMap(savedInstanceState);
          mDescription.setMovementMethod(new ScrollingMovementMethod());

        //END


        hideNavigationBar();

        setUpDetails();

        mClose.setOnClickListener(v->getActivity().onBackPressed());
        mChat.setOnClickListener(v->sendMessage());
        mCall.setOnClickListener(v->callSmsar());


         likeButton=root.findViewById(R.id.fav_button);
        likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                addLikedMember();
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                removeLikedMember();
            }
        });


        //Show Parking Option


    return root;
    }


    private void addLikedMember() {
        detailsRef.update("likedList", FieldValue.arrayUnion(username));
    }
    private void removeLikedMember() {

        detailsRef.update("likedList", FieldValue.arrayRemove(username));

    }

    private void initGoogleMap(Bundle savedInstanceState){
        // *** IMPORTANT ***
        // MapView requires that the Bundle you pass contain _ONLY_ MapView SDK
        // objects or sub-Bundles.
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        map.onCreate(mapViewBundle);

        map.getMapAsync(this);
    }



    private void initializeFireStore(){

        db = FirebaseFirestore.getInstance();
        detailsRef=db.collection("Property").document(getPropertyID());
    }

    private void hideNavigationBar() {

        //Hide navigation bar
        //START
        if (root.getContext().toString().contains("User")) {
            ((UserMainActivity) getActivity()).hideBottomNavigationView();

        } else if (root.getContext().toString().contains("Smsar")) {
            ((SmsarMainActivity) getActivity()).hideBottomNavigationView();
        }
        //END

    }

        private void showNavigationBar() {


            if (root.getContext().toString().contains("User")) {
                ((UserMainActivity) getActivity()).showBottomNavigationView();

            } else if (root.getContext().toString().contains("Smsar")) {
                ((SmsarMainActivity) getActivity()).showBottomNavigationView();
            }
        }


        private void callSmsar(){
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + PHONENUMBER));
            root.getContext().startActivity(intent);

        }
        public void sendMessage(){
                startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("sms:" +PHONENUMBER)));

        }

        private String getPropertyID(){
            String id="";
            Bundle argument;
            argument = getArguments();
            if (argument != null) {
                id=getArguments().getString("id");
            }
            return id;
        }
    List<String> group;
        private void setUpDetails(){
                detailsRef.get()
                        .addOnSuccessListener(documentSnapshot -> {
                            if(documentSnapshot.exists()){
                                mDescription.setText(documentSnapshot.getString("mDesc"));
                                mPrice.setText(documentSnapshot.getString("mPrice")+" JD");
                                mCity.setText(documentSnapshot.getString("mCity")+" |");
                                mCity.append(documentSnapshot.getString("address"));
                                mBaths.setText(Objects.requireNonNull(documentSnapshot.get("noBathrooms")).toString());
                                mRooms.setText(Objects.requireNonNull(documentSnapshot.get("noRooms")).toString());
                                mDate.setText(documentSnapshot.getString("date"));
                             group = (List<String>) documentSnapshot.get("likedList");
                                mArea.setText(Objects.requireNonNull(documentSnapshot.get("area")).toString());
                                parking=Objects.requireNonNull(documentSnapshot.getBoolean("parking"));
                                lat=documentSnapshot.getDouble("latitude");
                                lon=documentSnapshot.getDouble("longitude");
                                Picasso.get().load(documentSnapshot.getString("mImageUrl")).fit().into(propertyImage);
                               USERNAME = documentSnapshot.getString("mUsername");
                               getSmsarPhoneNumber(USERNAME);
                            }
                            else
                                Toast.makeText(mContext,"Not exists",Toast.LENGTH_LONG).show();
                        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(mContext,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        Toast.makeText(mContext,"None",Toast.LENGTH_LONG).show();

                        viewVisible();
                        if (parking)
                            mParking.setVisibility(View.VISIBLE);
                        else
                            mParking.setVisibility(View.INVISIBLE);

                        if(group.contains(username))
                            likeButton.setLiked(true);
                    }
                });



        }


    private void getSmsarPhoneNumber(String username) {
       db.collection("Smsar").document(username).get()
               .addOnSuccessListener(snapshot -> PHONENUMBER=snapshot.getString("mPhoneNumber"));

    }

    private void checkUserLike(){

    }

    private void viewVisible(){

        RelativeLayout propertyView=root.findViewById(R.id.propertyRV);

        propertyView.setVisibility(View.VISIBLE);

            pg.setVisibility(View.GONE);


        }


        private void setLocationOnMap(){



        }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        map.onSaveInstanceState(mapViewBundle);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @SuppressLint("MissingPermission")
            @Override
            public void run() {
                Log.i("location",lat+" "+lon);
                LatLng latLng = new LatLng(lat, lon);
                //     LatLngBounds latLngBounds=new LatLngBounds(latLng,latLng);
                googleMap.setMyLocationEnabled(true);
                googleMap.addMarker(new MarkerOptions().position(latLng)
                        .title(USERNAME)
                );
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                // googleMap.ani


                     }
}, 3000);
        CameraUpdate center=
                CameraUpdateFactory.newLatLng(new LatLng(lat,
                        lon));
        CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);
        googleMap.animateCamera(zoom);


    }
    @Override
    public void onStart() {
        super.onStart();
        map.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        map.onStop();
    }

    @Override
    public void onPause() {
        map.onPause();
        super.onPause();
    }

    @Override
    public void onResume() {
        map.onResume();
        super.onResume();
    /*    mCall.setTextColor(getResources().getColor(R.color.ms_black));
        mCall.setBackgroundColor(getResources().getColor(R.color.float_transparent));*/

    }
    @Override
    public void onDetach() {
        super.onDetach();
        showNavigationBar();

    }
    @Override
    public void onDestroy() {
     map.onDestroy();
        super.onDestroy();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        map.onLowMemory();
    }
}

