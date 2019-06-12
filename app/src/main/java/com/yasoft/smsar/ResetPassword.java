package com.yasoft.smsar;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.RelativeLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ResetPassword extends AppCompatActivity {

    RelativeLayout usernameView,pinView,passwordView;
    String username,pin,newPassword,repeatPassword;
    ArrayList<RelativeLayout> mView;
    short pagination=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        usernameView=findViewById(R.id.userNameView);
        pinView=findViewById(R.id.pinVerifyView);
        passwordView=findViewById(R.id.passwordResetView);
        replaceView(pagination);
        mView=new ArrayList<>();
        mView.add(usernameView);
        mView.add(pinView);
        mView.add(passwordView);




        FloatingActionButton fab = findViewById(R.id.next);

        fab.setOnClickListener(view -> {
           //Validate input
                ++pagination;
                replaceView(pagination);

            });
    }

    private void replaceView(short  page) {
        switch (page)
        {
            case 0:
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


    /*
    *
    *  if(!username.equals("")) {
                    usernameView.setVisibility(View.GONE);
                    replaceView(mView.get(1));
                }
                if(!pin.equals("")){
                    usernameView.setVisibility(View.GONE);
                    pinView.setVisibility(View.GONE);
                    replaceView(mView.get(2));
                }     replaceView(mView.get(0));
                view.setVisibility(View.VISIBLE);
    * */
}
