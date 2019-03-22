package com.yasoft.smsar.adapters;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import com.like.LikeButton;
import com.like.OnLikeListener;
import com.yasoft.smsar.DBHelper;
import com.yasoft.smsar.ShowProperty;
import com.yasoft.smsar.SmsarMainActivity;
import com.yasoft.smsar.models.Property;
import com.yasoft.smsar.R;
import com.yasoft.smsar.UserMainActivity;
import com.yasoft.smsar.models.SmsarModel;

import java.util.ArrayList;
import java.util.List;
//  private List<Property> arrayList;



public class DiscoverAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    List<Property> arrayList;
    ArrayList<SmsarModel> smsarModels;
    Context context;
    DBHelper mDBHelper;
    Button mCall,mMessage;
    String pn;
    String textMessage="أود استأجار الشقة ";
    TextView mInfo;
     Property property;
    View convertView;
  //  public List<String> arrayList;


    public DiscoverAdapter(List<Property> itemList) {

        arrayList = itemList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.property_view, parent, false);
            return new ItemViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        TextView txtCity, txtPrice, txtUsername,txtDesc;
        if (viewHolder instanceof ItemViewHolder) {

            populateItemRows((ItemViewHolder) viewHolder, position);
        } else if (viewHolder instanceof LoadingViewHolder) {
            showLoadingView((LoadingViewHolder) viewHolder, position);
        }


        final LikeButton likeButton =(LikeButton)convertView.findViewById(R.id.star_button);
        final MediaPlayer mp=MediaPlayer.create(convertView.getContext(),R.raw.like);

        mDBHelper= new DBHelper(convertView.getContext());
       // smsarModels=mDBHelper.getAllSmsar(property.getmUsername());
        likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                mp.start();
            }

            @Override
            public void unLiked(LikeButton likeButton) {

            }
        });

        txtDesc=(TextView)convertView.findViewById(R.id.description);
        txtDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });




        final Property mProperty=arrayList.get(position);



        //mCall=(TextView) convertView.findViewById(R.id.eCall);
        //  mMessage=(Button)convertView.findViewById(R.id.eTextMessage);

        mInfo=(TextView)convertView.findViewById(R.id.description);
        ViewFlipper mImage=(ViewFlipper)convertView.findViewById(R.id.v_flipper);




        mInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { parseActivity(mProperty.getmID()); }
        });

        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parseActivity(mProperty.getmID());
            }
        });
        

    }

    @Override
    public int getItemCount() {
        return arrayList == null ? 0 : arrayList.size();
    }

    /**
     * The following method decides the type of ViewHolder to display in the RecyclerView
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return arrayList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }


    private class ItemViewHolder extends RecyclerView.ViewHolder {
        public   TextView txtCity, txtPrice, txtUsername,txtDesc;


        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            convertView=itemView;
            context=convertView.getContext();


            txtDesc=(TextView)itemView.findViewById(R.id.description);
            txtCity=(TextView)itemView.findViewById(R.id.city);
            txtPrice=(TextView)itemView.findViewById(R.id.price);
            txtUsername=(TextView)itemView.findViewById(R.id.usernameShow);

        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    private void showLoadingView(LoadingViewHolder viewHolder, int position) {
        //ProgressBar would be displayed

    }

    private void populateItemRows(ItemViewHolder viewHolder, int position) {

        Property item = arrayList.get(position);
        viewHolder.txtCity.setText(item.getmCity());
        viewHolder.txtDesc.setText(item.getmDesc());
        viewHolder.txtPrice.setText(item.getmPrice()+" JD");


        String text = "By <font color='blue'>"+item.getmUsername()+"</font>.";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            viewHolder.txtUsername.setText(Html.fromHtml(text,  Html.FROM_HTML_MODE_LEGACY), TextView.BufferType.SPANNABLE);
        } else {
            viewHolder.txtUsername.setText(Html.fromHtml(text), TextView.BufferType.SPANNABLE);
        }
    }




    public  void parseActivity (int id /*Property item ID*/)

    {
        FragmentManager fragmentManager =null;
        FragmentTransaction fragmentTransaction=null;
        ShowProperty mShowProperty = new ShowProperty();
        Bundle mBundle=new Bundle();
        if(context.toString().contains("User")) {
            final UserMainActivity user = (UserMainActivity) context;
            fragmentManager=user.getFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.discoverView ,mShowProperty, "ShowProperty");


        }

        else if(context.toString().contains("Smsar"))
        {
            final SmsarMainActivity mSmsar = (SmsarMainActivity) context;
            fragmentManager=mSmsar.getFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.mainView ,mShowProperty, "ShowProperty");

        }

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        mBundle.putInt("propertyID",id);
        mShowProperty.setArguments(mBundle);
    }


}
