package com.yasoft.aqarkom;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class ResetPassword extends AppCompatActivity {

    RelativeLayout usernameView,pinView,passwordView;
    String username,pin,newPassword,repeatPassword;
    EditText mUsername,mPIN,mNewPassword,mRePassword;
    FirebaseFirestore db;
    DocumentReference userRef;
    CollectionReference smsarRef;
    ArrayList<RelativeLayout> mView;
    short pagination=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = FirebaseFirestore.getInstance();
        smsarRef=db.collection("aqarkom");
        usernameView=findViewById(R.id.userNameView);
        pinView=findViewById(R.id.pinVerifyView);
        passwordView=findViewById(R.id.passwordResetView);
        mUsername=findViewById(R.id.userNameEntry);
        mPIN=findViewById(R.id.pinEntry);
        mNewPassword=findViewById(R.id.newPasswordEntry);
        mRePassword=findViewById(R.id.repeatPasswordEntry);

        replaceView(pagination);
/*        mView=new ArrayList<>();
        mView.add(usernameView);
        mView.add(pinView);
        mView.add(passwordView);*/



        FloatingActionButton fab = findViewById(R.id.next);

        fab.setOnClickListener(view -> {
           //Validate input
            if(flag) {
                ++pagination;
                replaceView(pagination);
            }
            });
    }

  Boolean flag=false;
    private void replaceView(short  page) {
        switch (page)
        {
            case 0:
                username=mUsername.getText().toString();
                flag=checkUsername(username.trim().toLowerCase());
                usernameView.setVisibility(View.VISIBLE);
                pinView.setVisibility(View.GONE);
                passwordView.setVisibility(View.GONE);
                break;

            case 1:
                usernameView.setVisibility(View.GONE);
                pinView.setVisibility(View.VISIBLE);
                passwordView.setVisibility(View.GONE);
                break;
            case 2:
                usernameView.setVisibility(View.GONE);
                pinView.setVisibility(View.GONE);
                passwordView.setVisibility(View.VISIBLE);
                break;

        }

    }
        static int PIN;
    private Boolean checkUsername(String username) {
//            userRef=smsarRef.document(username);
            userRef.get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful())
                        {
                            DocumentSnapshot snapshot =task.getResult();
                            if(Objects.requireNonNull(snapshot).exists()) {
                                Toast.makeText(this, "username is exists", Toast.LENGTH_LONG).show();
                                flag = true;
                                String userEmail=snapshot.getString("mEmail");
                              PIN= generatePIN();
                                if (PIN!=0) {
                                    userRef.set(PIN).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            sendPIN(userEmail);
                                        }
                                    });
                                }
                            }
                        }
                    });
        return flag;
    }

    private int generatePIN() {
       int pin;
            Random generate = new Random();
        pin = generate.nextInt(1000000);
            if (pin < 0)
                pin *= -1;

        return pin;
    }

    private void sendPIN(String userEmail) {

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{userEmail});
        i.putExtra(Intent.EXTRA_SUBJECT, "Your Reset PIN ");
        i.putExtra(Intent.EXTRA_TEXT   , "Your Reset PIN is "+PIN+"Please Enter this PIN in application to set You new Password");
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ResetPassword.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }

    }


}
