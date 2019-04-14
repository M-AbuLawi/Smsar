package com.yasoft.smsar;


import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.yasoft.smsar.models.Property;
import com.yasoft.smsar.models.Smsar;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Signup extends AppCompatActivity {

    EditText Fullname, Email, Username, Password, phonenumber;
    int id;
    Intent intent;
    Button button;
    TextView ErorrM;
    private DBHelper mDBHelper = new DBHelper(this);
    private SQLiteDatabase mDb;
    private static int CURRENT_ID;

    String email, name, username, password, pn;

    FirebaseFirestore db;
    DocumentReference userRef;
    private CollectionReference smsarRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();
        userRef = db.document("ID/id");
        smsarRef = db.collection("Smsar");
        getIDs();
        Fullname = findViewById(R.id.fullName);
        Email = findViewById(R.id.email);
        Username = findViewById(R.id.username);
        Password = findViewById(R.id.Password);
        phonenumber = findViewById(R.id.phoneNumber);
        ErorrM = findViewById(R.id.eTextMessage);

        button = findViewById(R.id._signUp);


        button.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) throws SQLException {

                String userid = "" + (++CURRENT_ID);
                if (validationVariable()) {

                    boolean flag;
                    //      mydb.getWritableDatabase();
                    prepareToInsert();
                    try {
                        Smsar mSmsar = new Smsar(name, pn, username, email, password);

                        //    smsarRef.add(mSmsar);
                        db.collection("Smsar").document(username).set(mSmsar)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(Signup.this, "done",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        Toast.makeText(Signup.this, "Failed",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });


                        flag = mDBHelper.insertSmsar(username, name,
                                email, password, pn);
                        if (flag) {
                            Toast.makeText(getApplicationContext(), "done",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "not done",
                                    Toast.LENGTH_SHORT).show();

                        }
                        intent = new Intent(Signup.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    ErorrM.setText("Empty Fields");
                    ErorrM.setVisibility(View.VISIBLE);
                }
            }

        });

        //On Type Listener for Text view so we can know the user is typing or retyping in them ;



    }
    //Validation

    public boolean validationVariable() {
        return !TextUtils.isEmpty(Fullname.getText().toString()) && !TextUtils.isEmpty(Email.getText().toString()) &&
                !TextUtils.isEmpty(Username.getText().toString())
                && !TextUtils.isEmpty(Password.getText().toString()) && !TextUtils.isEmpty(phonenumber.getText().toString());

    }

    public void prepareToInsert() {
        username = Username.getText().toString();
        name = Fullname.getText().toString();
        email = Email.getText().toString();
        password = Password.getText().toString();
        pn = phonenumber.getText().toString();
        username = username.toLowerCase().trim();

    }


    public void getIDs() {
        //  Map<String,Object> data=new HashMap<>();

        userRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.exists()) {

                            String id = documentSnapshot.get("current_id_number").toString();
                            // Toast.makeText(MainActivity.this,id,Toast.LENGTH_LONG).show();// for Testing PURPOSE
                            CURRENT_ID = Integer.parseInt(id);

                        } else
                            Toast.makeText(Signup.this, "not exist ", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

//        return currentid;

    }
}










