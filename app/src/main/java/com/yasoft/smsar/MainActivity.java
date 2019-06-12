package com.yasoft.smsar;


import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.Build;
import android.os.Bundle;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.yasoft.smsar.models.Smsar;

import java.io.IOException;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {



    //
    private Handler mHandler;
    //

    EditText userNameEdit, passwordEdit;
    TextView _signUp,resetPassword;
    Button _login;
    Intent intent;
    ImageView imageView;
    TextView mError;
    Fragment mFrag = new StartInterface();
    Fragment mNWError = new NetworkError();
    SharedPreferences pref;
    private DBHelper mDBHelper;

    EncryptString mEncrypt;

    String email, name, username, password, pn;
    private static String KEY_USERNAME = "username";
    private static String KEY_PASSWORD = "password";

    FirebaseFirestore db;
    DocumentReference userRef;
    ProgressBar progressBar ;
    @TargetApi(Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_GuidelinesCompat);
        setContentView(R.layout.activity_main);

        _login = findViewById(R.id.logIn);
        _signUp = findViewById(R.id.sginUp);
        progressBar =findViewById(R.id.loading);
        resetPassword=findViewById(R.id.forgot_password);

        mHandler = new Handler();
        String text = "New ? Start Now By  <font color='blue'>" + String.format(getString(R.string.sign_up)) + "</font>.";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            _signUp.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY), TextView.BufferType.SPANNABLE);
        } else {
            _signUp.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
        }

        resetPassword.setOnClickListener(v->startActivity(new Intent(this,ResetPassword.class)));
        //User session
        pref = getSharedPreferences("user_details", MODE_PRIVATE);
        if (pref.contains("username") && pref.contains("password"))
            success();

        replaceInterface();
        imageView = findViewById(R.id.logo);

        mDBHelper = new DBHelper(this);
        FirebaseApp.initializeApp(this);
        db = FirebaseFirestore.getInstance();
        userRef = db.document("ID/id");

        mError = findViewById(R.id.mError);
        //Define the variables
        userNameEdit =findViewById(R.id.userName);
        passwordEdit =findViewById(R.id.password);


        userNameEdit.setFocusableInTouchMode(true);
        passwordEdit.setFocusableInTouchMode(true);
        //End


        _signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent = new Intent(MainActivity.this, Signup.class);
                startActivity(intent);

            }
        });



        //On Type Listener for Text view so we can know the user is typing or retyping in them ;
        checkInputs();




    }

   public void  closeMainInterface(){
       getSupportFragmentManager().beginTransaction().
               remove(Objects.requireNonNull(getSupportFragmentManager().findFragmentById(R.id.MainFragment))).commit();
   }
    int counter;
    String cUsername="";
    String cPassword="";
    public void logIN(View view) throws SQLException {

     //   progressBar.setVisibility(View.VISIBLE);
        startLoading();
        showProgressBar();

        pref = getSharedPreferences("user_details", MODE_PRIVATE);
        prepareToInsert();
        if (validationVariable()) {

         //   Map<String, Object> userDate = new HashMap<>();
            db.collection("Smsar").whereEqualTo("mUsername", username).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Smsar smsar = documentSnapshot.toObject(Smsar.class);
                         cUsername = smsar.getmUsername();
                         cPassword = smsar.getmPassword();
                        progressBar.setProgress(50);
                        if (isCorrect(cUsername,cPassword)) {

                            success();
                            saveSession(cUsername, cPassword);
                        } else{
                            Toast.makeText(MainActivity.this,
                                    "Wrong username or password\nTry again please", Toast.LENGTH_LONG).show();

                    }}

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MainActivity.this,
                            "Failed", Toast.LENGTH_LONG).show();
                    hideProgressBar();
                })
            .addOnCompleteListener(task -> {
                if(cUsername.equals("")&&cPassword.equals("")){
                    Toast.makeText(MainActivity.this,
                            "Wrong username or password \nTry again please", Toast.LENGTH_LONG).show();
                hideProgressBar();
                }

            });

          //  hideProgressBar();
        } else {
            mError.setText("Empty Fields");
            mError.setVisibility(View.VISIBLE);
            hideProgressBar();
        }

    }

    public boolean validationVariable() {
        return !username.isEmpty() && !password.isEmpty();

    }

    private void success() {
        progressBar.setProgress(100);
        hideProgressBar();
        intent = new Intent(MainActivity.this, SmsarMainActivity.class);
        startActivity(intent);
        finish();
        this.finish();
    }

    public void closeSmsar() {
        Intent intent = new Intent(this, UserMainActivity.class);
        startActivity(intent);
    }
    int x;
    public  String string(int x){
        this.x=x;

        return "string";

    }

    public void prepareToInsert() {

        username = userNameEdit.getText().toString();
        if (!username.isEmpty()) {
            password = passwordEdit.getText().toString();
            password =EncryptString.encryptString(password);
    //   String space = username.charAt(username.length() - 1) + "";
            username = username.toLowerCase();
            username = username.trim();
        }


    }


    private boolean isCorrect(String user,String pass) {
        return password.equals(pass)&&username.equals(user);
    }

    private void saveSession(String user, String pass) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("username", user);
        editor.putString("password", pass);
        editor.apply();

    }
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    // Hide progress
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    private void startLoading() {
        //Simulate Heavy task in background thread
     new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <= 30; i++) {
                    final int currentProgressCount = i;
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                    //Post updates to the User Interface
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(currentProgressCount);
                        }
                    });
                }

            }

        }).start();



    }

    public void checkInputs(){
        userNameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mError.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        passwordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mError.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void replaceInterface(){

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.addToBackStack(null);

        if(isOnline())
            ft.replace(R.id.mainView, mFrag);
            else {
            ft.addToBackStack(null);
            ft.replace(R.id.mainView, mNWError);
        }

        ft.commit();


    }

    public boolean isOnline()  {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }


    @Override
    protected void onResume() {
        super.onResume();
        replaceInterface();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        replaceInterface();
    }
}
