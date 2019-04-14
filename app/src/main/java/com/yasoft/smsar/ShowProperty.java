package com.yasoft.smsar;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;


public class ShowProperty extends Fragment {

    View root;
    Context mContext;
    ViewGroup actionBarLayout;
    String username = "", pn = "";
    Button mCall;
    private FirebaseFirestore db;
    private DocumentReference detailsRef;
    ProgressBar pg;


    TextView mPrice,mDescription,mCity,mDate ,mBaths,mRooms,mArea;
    ImageView mParking;
    boolean parking;




    public ShowProperty() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                onDetach();
                Toast.makeText(root.getContext(), "hello", Toast.LENGTH_LONG).show();
                return true;

            case R.id.action_setting:
                onDetach();
                Toast.makeText(root.getContext(), "hello", Toast.LENGTH_LONG).show();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    

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
        //END


        hideNavigationBar();
        initializeFireStore();
        setUpDetails();




        final Toolbar toolbar1 = (Toolbar) root.findViewById(R.id.discoverToolBar);
        toolbar1.inflateMenu(R.menu.menu_dashboard_titlebar);

        toolbar1.setNavigationOnClickListener(v -> {
            onDetach();
            if (root.getContext().toString().contains("User")) {
                ((UserMainActivity) getActivity()).onBackPressed();

            } else if (root.getContext().toString().contains("Smsar")) {
                ((SmsarMainActivity) getActivity()).onBackPressed();
            }


            Toast.makeText(mContext, getArguments().get("propertyID")+"",Toast.LENGTH_LONG).show();//For Testing Purpose

        });



        //Show Parking Option


    return root;
    }

    @Override
    public void onDetach() {
        super.onDetach();
            showNavigationBar();
    }

    @Override
    public void onResume() {
        super.onResume();
    /*    mCall.setTextColor(getResources().getColor(R.color.ms_black));
        mCall.setBackgroundColor(getResources().getColor(R.color.float_transparent));*/

    }

    
    
    private void initializeFireStore(){

        FirebaseApp.initializeApp(mContext);
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
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + pn));
            root.getContext().startActivity(intent);

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
        private void setUpDetails(){
                detailsRef.get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if(documentSnapshot.exists()){
                                    mDescription.setText(documentSnapshot.getString("mDesc"));
                                    mPrice.setText(documentSnapshot.getString("mPrice")+" JD");
                                    mCity.setText(documentSnapshot.getString("mCity")+" |");
                                    mCity.append(documentSnapshot.getString("address"));
                                    mBaths.setText(Objects.requireNonNull(documentSnapshot.get("noBathrooms")).toString());
                                    mRooms.setText(Objects.requireNonNull(documentSnapshot.get("noRooms")).toString());
                                    mDate.setText(documentSnapshot.getString("date"));
                                    mArea.setText(Objects.requireNonNull(documentSnapshot.get("area")).toString());
                                    parking=Objects.requireNonNull(documentSnapshot.getBoolean("parking"));
                                //    username = rs.getString(rs.getColumnIndex(DBHelper.PROPERTY_COLUMN_SMSARUSERNAME));
                                }
                                else
                                    Toast.makeText(mContext,"Not exists",Toast.LENGTH_LONG).show();
                            }
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
                    }
                });



        }


        private void viewVisible(){

            mDescription.setVisibility(View.VISIBLE);
            mPrice.setVisibility(View.VISIBLE);
            mCity.setVisibility(View.VISIBLE);
            mCity.setVisibility(View.VISIBLE);
            mBaths.setVisibility(View.VISIBLE);
            mRooms.setVisibility(View.VISIBLE);
            mDate.setVisibility(View.VISIBLE);
            mArea.setVisibility(View.VISIBLE);
          //  parking.setVisibility(View.VISIBLE);

            pg.setVisibility(View.GONE);


        }
}
