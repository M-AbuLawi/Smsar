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
import android.widget.ListView;

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

View root;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         root=inflater.inflate(R.layout.fragment_settings, container, false);
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

         return root;
    }



}
