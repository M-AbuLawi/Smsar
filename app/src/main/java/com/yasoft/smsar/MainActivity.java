package com.yasoft.aqarkom;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.yasoft.aqarkom.models.Seeker;
import com.yasoft.aqarkom.models.Smsar;

import java.io.IOException;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {


    private static final String TAG = "MainActivity" ;
    //
    private Handler mHandler;
    //

    EditText userNameEdit, passwordEdit;
    TextView _signUp,resetPassword;
    Button _login;
    Intent intent;
    ImageView imageView;
    TextView mError;

    SharedPreferences pref;


    EncryptString mEncrypt;

    String email, name, username, password, pn;
    private static String KEY_USERNAME = "username";
    private static String KEY_PASSWORD = "password";

    FirebaseFirestore db;
    DocumentReference userRef;
    ProgressBar progressBar ;
    private RadioGroup typeRG;
    private RadioButton typeRB;
    private String userType;

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
        TextView guestLogin= findViewById(R.id.guestLogin);
        typeRG = findViewById(R.id.userTypeRG);

        guestLogin.setOnClickListener(v->startActivity(new Intent(this,UserMainActivity.class)));
        mHandler = new Handler();
        String text = "New ? Start Now By  <font color='blue'>" + String.format(getString(R.string.sign_up)) + "</font>.";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            _signUp.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY), TextView.BufferType.SPANNABLE);
        } else {
            _signUp.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
        }

//        resetPassword.setOnClickListener(v->startActivity(new Intent(this,ResetPassword.class)));
        //User session
        pref = getSharedPreferences("user_details", MODE_PRIVATE);
        userType = pref.getString("userType","none");
        if (pref.contains("username") && pref.contains("password"))
            success();

        replaceInterface();
        imageView = findViewById(R.id.logo);

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
        int id=typeRG.getCheckedRadioButtonId();
        userType="";
        if(id!=0) {
            typeRB = findViewById(id);
            userType=typeRB.getText().toString();
        }
     //   progressBar.setVisibility(View.VISIBLE);
        startLoading();
        showProgressBar();

        pref = getSharedPreferences("user_details", MODE_PRIVATE);
        prepareToInsert();
        if (validationVariable()) {
            Log.d(TAG, "logIN: "+ userType);
        if(userType.contains("Smsar"))
            LogInSmsar();
        else
            LogInSeeker();

         //   Map<String, Object> userDate = new HashMap<>()

          //  hideProgressBar();
        } else {
            mError.setText("Empty Fields");
            mError.setVisibility(View.VISIBLE);
            hideProgressBar();
        }


    }
public void  LogInSmsar(){
    System.out.println(userType);
    Log.d(TAG, "LogInSmsar: "+ userType);
    db.collection(userType).whereEqualTo("mUsername", username).get()
            .addOnSuccessListener(queryDocumentSnapshots ->  {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Smsar smsar = documentSnapshot.toObject(Smsar.class);
                    cUsername = smsar.getmUsername();
                    cPassword = smsar.getmPassword();

                    progressBar.setProgress(50);
                    if (isCorrect(cUsername,cPassword)) {
                        success();
                        saveSession(cUsername, cPassword, userType);
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


}
public void LogInSeeker(){
    System.out.println(userType);
    db.collection(userType).whereEqualTo("mUsername", username).get()
            .addOnSuccessListener(queryDocumentSnapshots ->  {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Seeker seeker = documentSnapshot.toObject(Seeker.class);
                    cUsername = seeker.getmUsername();
                    cPassword = seeker.getmPassword();

                    progressBar.setProgress(50);
                    if (isCorrect(cUsername,cPassword)) {

                        saveSession(cUsername, cPassword, userType);
                        success();
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

}

    public boolean validationVariable() {
        return !username.isEmpty() && !password.isEmpty() && !userType.isEmpty();

    }
    public static Boolean SESSION;

    private void success() {
        progressBar.setProgress(100);
        hideProgressBar();
        SESSION = true;
        if(userType!=null)
        if(userType.contains("Smsar"))
        intent = new Intent(MainActivity.this, SmsarMainActivity.class);
        else if(userType.contains("Seeker"))
            intent = new Intent(MainActivity.this, UserMainActivity.class);

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

    private void saveSession(String user, String pass, String userType) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("username", user);
        editor.putString("password", pass);
        editor.putString("userType",userType);
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
                mError.setVisibility(View.GONE);
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
                mError.setVisibility(View.GONE);
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
        RelativeLayout login,network_error;
        login=findViewById(R.id.loginView);
        network_error=findViewById(R.id.network_connView);

        if(isOnline()) {
            login.setVisibility(View.VISIBLE);
            network_error.setVisibility(View.GONE);
        }
        else {
            network_error.setVisibility(View.VISIBLE);
                login.setVisibility(View.GONE);

        }


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
