package com.yasoft.smsar;



import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Signup extends AppCompatActivity {

        TextView Fullname, Email, Username, Password, phonenumber, ErorrM;;
    int id;
    RadioGroup radioGroup;
    RadioButton radioButton;
    Intent intent;
    Button button;
    private DBHelper mDBHelper =new DBHelper(this);
    private SQLiteDatabase mDb;

    String email,name,username,password,pn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
     //   Toast.makeText(getApplicationContext(), String.valueOf(mydatabase.isOpen()), Toast.LENGTH_LONG).show();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        SQLiteDatabase mydatabase = openOrCreateDatabase("Smsar", MODE_PRIVATE, null);

        Fullname = (TextView) findViewById(R.id.fullName);
        Email = (TextView) findViewById(R.id.email);
        Username = (TextView) findViewById(R.id.username);
        Password = (TextView) findViewById(R.id.Password);
        phonenumber = (TextView) findViewById(R.id.phoneNumber);
        ErorrM = (TextView) findViewById(R.id.eTextMessage);
   /*     radioGroup = (RadioGroup) findViewById(R.id.rgType);
        id = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(id);*/

        button = (Button) findViewById(R.id._signUp);



        button.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v) throws SQLException {


                if (validationVariable()) {

                    boolean flag;
                    //      mydb.getWritableDatabase();
                    prepareToInsert();
                    try {
                        flag = mDBHelper.insertSmsar( username,name,
                                email, password,pn);
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
                        Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                    }
                } else {
                    ErorrM.setText("Empty Fields");
                    ErorrM.setVisibility(View.VISIBLE);
                }
            }

        });

    }
        //Validation

        public boolean validationVariable () {
            if (TextUtils.isEmpty(Fullname.getText().toString()) || TextUtils.isEmpty(Email.getText().toString()) ||
                    TextUtils.isEmpty(Username.getText().toString())
                    || TextUtils.isEmpty(Password.getText().toString()) || TextUtils.isEmpty(phonenumber.getText().toString()))
                return false;

            return true;

        }

        public void prepareToInsert(){
        username=Username.getText().toString();
        name=Fullname.getText().toString();
        email= Email.getText().toString();
        password= Password.getText().toString();
        pn= phonenumber.getText().toString();
        String space=name.charAt(name.length()-1)+"";
       username=username.toLowerCase();
       username=username.replaceAll(" ","");
    }
}










