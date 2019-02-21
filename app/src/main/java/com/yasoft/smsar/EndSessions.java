package com.yasoft.smsar;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;

public class EndSessions extends Activity {


    final SharedPreferences pref = getSharedPreferences("user_details", MODE_PRIVATE);;

    EndSessions(){

    }


  public void logout(View v){

      SharedPreferences.Editor editor = pref.edit();
      editor.clear();
      editor.commit();

      Intent intent=new Intent(v.getContext(),MainActivity.class);
      startActivity(intent);



  }


}
