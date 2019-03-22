package com.yasoft.smsar;



import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;

import android.os.Bundle;
import android.app.Fragment;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;


import com.yasoft.smsar.models.Property;
import com.yasoft.smsar.adapters.DiscoverAdapter;
import com.yasoft.smsar.models.SmsarModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


/**
 * A simple {@link Fragment} subclass.
 */
public class Discover extends Fragment {


    public Discover() {
        // Required empty public constructor
    }

    DBHelper mDBHelper;


    List<Property> listItem;ArrayList<Property>rowsList ;
    DiscoverAdapter mAdapter;
    RecyclerView recyclerView;
    View root;
    Context mContext;
    boolean isLoading = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        root = inflater.inflate(R.layout.fragment_discover, container, false);
        String context = root.getContext().toString();


        mContext = root.getContext();

        if (context.contains("Smsar")) {
            ((SmsarMainActivity) getActivity()).navPointer(R.id.navigation_discover);
            //       UserMainActivity mUser=(UserMainActivity)mContext;
        } else if (context.contains("User")) {
            ((UserMainActivity) getActivity()).navPointer(R.id.navigation_discover);
        }

        //mUser.getSupportActionBar().setTitle(R.string.title_Discover);

        final Toolbar toolbar1 = (Toolbar) root.findViewById(R.id.discoverToolBar);
        toolbar1.inflateMenu(R.menu.menu_dashboard_titlebar);
        toolbar1.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


        Button mFilter = (Button) root.findViewById(R.id.filter);
        mFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parseActivity();
            }
        });


//        _propertList=(ListView)root.findViewById(R.id.discoverLV);

    /*    int images[]={R.drawable.house1,R.drawable.house2, R.drawable.house3};

        v_flipper=root.findViewById(R.id.v_flipper);

    for(int image:images)
        flipperImages(image);*/
      //  listData();

        populateData();
        initAdapter();
        initScrollListener();

        return root;
    }

/*    public void flipperImages(int image){
        ImageView imageView=new ImageView(root.getContext());
        imageView.setBackgroundResource(image);
    v_flipper.addView(imageView);
    v_flipper.setFlipInterval(4000);
    v_flipper.setAutoStart(true);


    v_flipper.setInAnimation(root.getContext(),android.R.anim.slide_in_left);
    v_flipper.setOutAnimation(root.getContext(),android.R.anim.slide_out_right);
    }*/


    public void listData() {



        //  String username = getArguments().getString("username");
        mDBHelper = new DBHelper(root.getContext());
        listItem = mDBHelper.getAllProperty();

        final RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.discoverLV);
        LinearLayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(root.getContext());
        recyclerView.setLayoutManager(mLayoutManager);


        mAdapter = new DiscoverAdapter(listItem);


        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged();

    }



    private void populateData() {
        rowsList = new ArrayList<>();

        mDBHelper = new DBHelper(root.getContext());
        listItem = mDBHelper.getAllProperty();
        if(listItem.size()>0) {
            int i = 0;
            while (i < 5) {
                if(listItem.get(i)!=null) {
                    rowsList.add(listItem.get(i));
                    i++;
                }
                else
                     Toast.makeText(root.getContext(),"No more Property",Toast.LENGTH_LONG).show();
            }
        }else Toast.makeText(root.getContext(),"No more Property",Toast.LENGTH_LONG).show();

    }

    private void initAdapter() {

        recyclerView = (RecyclerView) root.findViewById(R.id.discoverLV);

        LinearLayoutManager mLayoutManager;
        mLayoutManager = new LinearLayoutManager(root.getContext());
        recyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new DiscoverAdapter(rowsList);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

    }

    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == rowsList.size() - 1) {
                        //bottom of list!

                            loadMore();
                            isLoading = true;
                 //       mAdapter.notifyDataSetChanged();
                        }


                    }
                }

        });


    }

    private void loadMore() {
        rowsList.add(null);
        mAdapter.notifyItemInserted(rowsList.size() - 1);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                rowsList.remove(rowsList.size() - 1);
                int scrollPosition = rowsList.size();
                mAdapter.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                int nextLimit = currentSize + 1;
                if(nextLimit!=listItem.size())
                {

             while (currentSize - 1 < nextLimit) {
                  rowsList.add(listItem.get(currentSize));
                       currentSize++;
                 mAdapter.notifyDataSetChanged();
                }

                isLoading = false;
            }
            else
                Toast.makeText(root.getContext(),"No more Property",Toast.LENGTH_LONG).show();
            }

        }, 1000);

    }


    public void parseActivity() {
        FragmentManager fragmentManager = null;
        FragmentTransaction fragmentTransaction = null;
        ShowFilter mFlter = new ShowFilter();
        Bundle mBundle = new Bundle();
        if (mContext.toString().contains("User")) {
            final UserMainActivity user = (UserMainActivity) mContext;
            fragmentManager = user.getFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out,
                    android.R.animator.fade_in, android.R.animator.fade_out);

            fragmentTransaction.replace(R.id.discoverView, mFlter, "ShowFilter");


        } else if (mContext.toString().contains("Smsar")) {
            final SmsarMainActivity mSmsar = (SmsarMainActivity) mContext;
            fragmentManager = mSmsar.getFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
            fragmentTransaction.replace(R.id.mainView, mFlter, "ShowFilter");

        }

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

}
