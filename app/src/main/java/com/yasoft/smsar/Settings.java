package com.yasoft.smsar;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class Settings extends Fragment {

    /**
     * A simple {@link Fragment} subclass.
     */

    public Settings() {
        // Required empty public constructor
    }

//    EndSessions endSessions=new EndSessions();
    DBHelper mDBHelper;
    ListView li;
    ImageView image;
View root;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         root=inflater.inflate(R.layout.fragment_settings, container, false);

         //Toolbar toolbar=root.findViewById(R.layout.fragment_titlebar);


        String username = getArguments().getString("username");
        TextView usernameS=(TextView)root.findViewById(R.id.usernameSetting);
        image = (ImageView) root.findViewById(R.id.profile_image);
    image.setMaxHeight(52);
    image.setMaxWidth(52);
        usernameS.setText(username);



        li=(ListView)root.findViewById(R.id.listSetting);
    li.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (id==0)
            {
                ((SmsarMainActivity)getActivity()).logout();
            }
            if(id==1){
                mDBHelper=new DBHelper(root.getContext());
                String username = getArguments().getString("username");
                mDBHelper.deleteSmsar(username);
                ((SmsarMainActivity)getActivity()).logout();

                //need to create class to manage the sessions
            }
        }
    });
        ((SmsarMainActivity)getActivity()).navPointer(R.id.navigation_account);
         return root;
    }



}
