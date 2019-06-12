package com.yasoft.smsar;


import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.yasoft.smsar.models.Seeker;
import com.yasoft.smsar.models.Smsar;

import java.util.Objects;
import java.util.regex.Pattern;

public class Signup extends AppCompatActivity {

    EditText Fullname, Email, Username, Password, phonenumber;
    int id;
    Intent intent;
    Button button;
    RadioGroup typeRG;
    RadioButton typeRB;
    TextView ErorrM;
    private DBHelper mDBHelper = new DBHelper(this);
    private SQLiteDatabase mDb;
    private static int CURRENT_ID;

    String email, name, username, password, pn;
    String userType;
    FirebaseFirestore db;
    DocumentReference userRef;
    private CollectionReference smsarRef,seekerRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();
        userRef = db.document("ID/id");
        smsarRef = db.collection("Smsar");
        seekerRef=db.collection("Seeker");

        Fullname = findViewById(R.id.fullName);
        Email = findViewById(R.id.email);
        Username = findViewById(R.id.username);
        Password = findViewById(R.id.Password);
        phonenumber = findViewById(R.id.phoneNumber);
        ErorrM = findViewById(R.id.eTextMessage);
        typeRG=findViewById(R.id.userTypeRG);


        button = findViewById(R.id._signUp);


        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)  {
                int id=typeRG.getCheckedRadioButtonId();
                 userType="";
                if(id!=0) {
                    typeRB = findViewById(id);
                    userType=typeRB.getText().toString();
                }


                if(userType.equals("Smsar"))
                    insertSmsar();
                insertSeeker();

            }

        });

        //On Type Listener for Text view so we can know the user is typing or retyping in them ;



    }

    private void insertSeeker() {
        if (validationVariable()) {


            //      mydb.getWritableDatabase();
            prepareToInsert();

            if (!validEmail(email)) {
                Toast.makeText(Signup.this,"Enter valid e-mail!",Toast.LENGTH_LONG).show();
            }

            if(password.length()<6)
                Toast.makeText(Signup.this,"Password must be at least 6 characters",Toast.LENGTH_LONG).show();
            else if(validEmail(email) && password.length()>=6 && !flag)
                try {
                    password= EncryptString.encryptString(password);

                    Seeker mSeeker = new Seeker(name, pn, username, email, password);

                    //    smsarRef.add(mSmsar);


                 seekerRef.document(username).set(mSeeker)
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


                    intent = new Intent(Signup.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
                }
        }
        else {
            ErorrM.setText("Empty Fields");
            ErorrM.setVisibility(View.VISIBLE);
        }
    }

    private void insertSmsar() {
        if (validationVariable()) {


            //      mydb.getWritableDatabase();
            prepareToInsert();
            if (!validEmail(email))
                Toast.makeText(Signup.this,"Enter valid e-mail!",Toast.LENGTH_LONG).show();

            if(password.length()<6)
                Toast.makeText(Signup.this,"Password must be at least 6 characters",Toast.LENGTH_LONG).show();
            else if(validEmail(email) && password.length()>=6 && !flag){
                try {
                    Smsar mSmsar = new Smsar(name, pn, username, email, password);
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



                    intent = new Intent(Signup.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }
        else {
            ErorrM.setText("Empty Fields");
            ErorrM.setVisibility(View.VISIBLE);
        }
    }
    //Validation

    public boolean validationVariable() {
        return !TextUtils.isEmpty(Fullname.getText().toString()) && !TextUtils.isEmpty(Email.getText().toString()) &&
                !TextUtils.isEmpty(Username.getText().toString())
                && !TextUtils.isEmpty(Password.getText().toString()) && !TextUtils.isEmpty(phonenumber.getText().toString());

    }

    EncryptString mEncrypt;
    public void prepareToInsert() {
        
        username = Username.getText().toString();
      
            
        name = Fullname.getText().toString();
        email = Email.getText().toString();
        

        password = Password.getText().toString();


        pn = phonenumber.getText().toString();
        username = username.toLowerCase().trim();

        isExist(username,userType);

    }

    private boolean validEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;

        int emailLength=email.length();
        String domain="";

        //a@e.com example@example.com , .org, .net
        if(emailLength>6) {
            domain = email.substring(emailLength - 3);
            if (domain.equals("com") || domain.equals("net") || domain.equals("org"))
                return pattern.matcher(email).matches();

            else return false;
        }
        else return false;
    }





//        return currentid;


    
    boolean flag =false;
    private void isExist(String username,String path){
        DocumentReference docRef = db.collection(path).document(username);
        docRef.get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful())
                    {
                        DocumentSnapshot snapshot =task.getResult();
                    if(Objects.requireNonNull(snapshot).exists()) {
                        Toast.makeText(this, "username is exists", Toast.LENGTH_LONG).show();
                        flag = true;
                    }
                    }
                });



    }
}





