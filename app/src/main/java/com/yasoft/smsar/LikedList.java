package com.yasoft.smsar;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LikedList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LikedList extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView signUpAction;
    Button mLogin;

    public LikedList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LikedList.
     */
    // TODO: Rename and change types and number of parameters
    public static LikedList newInstance(String param1, String param2) {
        LikedList fragment = new LikedList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root= inflater.inflate(R.layout.fragment_liked_list, container, false);

        signUpAction=root.findViewById(R.id.signUpAction);
        mLogin=root.findViewById(R.id.login);

        String text = getString(R.string.signUp_description)+" <font color='blue'>" + String.format(getString(R.string.sign_up)) + "</font>.";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            signUpAction.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY), TextView.BufferType.SPANNABLE);
        } else {
            signUpAction.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
        }

        signUpAction.setOnClickListener(v -> startActivity(new Intent(getActivity(), Signup.class)));
        mLogin.setOnClickListener(v ->
                {
               Intent intent=  new Intent(getActivity(), MainActivity.class);
                 //   ((MainActivity) Objects.requireNonNull(getContext())).closeMainInterface();
                    startActivity(intent);
                }
        );

    return root;
    }

}
