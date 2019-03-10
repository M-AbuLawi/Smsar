package com.yasoft.smsar;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import cn.pedant.SweetAlert.SweetAlertDialog;

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

                new SweetAlertDialog(root.getContext(), SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Are you sure?")
                        .setContentText("All of your data will be gone!")
                        .setConfirmText("Yes,delete it!")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                mDBHelper=new DBHelper(root.getContext());
                                String username = getArguments().getString("username");
                                mDBHelper.deleteSmsar(username);
                                ((SmsarMainActivity)getActivity()).logout();
                            }
                        })
                        .setCancelButton("Cancel", new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                            }
                        })
                        .show();
                //need to create class to manage the sessions
            }
        }
    });
        ((SmsarMainActivity)getActivity()).navPointer(R.id.navigation_account);
         return root;
    }


/*
*
*
* */
}
