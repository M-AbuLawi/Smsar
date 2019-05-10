package com.yasoft.smsar;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.app.Activity.RESULT_OK;

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
    public static final int IMAGE_GALLERY_REQUEST = 20;
//    EndSessions endSessions=new EndSessions();
    FirebaseFirestore firestore;

    ListView li;
    ImageView image;
    View root;
    String username;
    private Context context;
    private ImageButton mImageButton;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         root=inflater.inflate(R.layout.fragment_settings, container, false);
        context=root.getContext();
         //Toolbar toolbar=root.findViewById(R.layout.fragment_titlebar);
        FirebaseApp.initializeApp(root.getContext());
        firestore=FirebaseFirestore.getInstance();



         username = getArguments().getString("username");
        TextView usernameS=(TextView)root.findViewById(R.id.usernameSetting);
        mImageButton=root.findViewById(R.id.changeImage);
        image =  root.findViewById(R.id.profile_image);
         image.setMaxHeight(52);
         image.setMaxWidth(52);
        usernameS.setText(username);


        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageGallery();
            }
        });

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
                               deleteSmsar();
                                logout();

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


    private void logout(){
        ((SmsarMainActivity)getActivity()).logout();

    }

    private int propertyID;
    private void deleteSmsar(){
        firestore.collection("Smsar").document(username).delete();
        firestore.collection("Property").whereEqualTo("mUsername",username).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    }
                });

              }
    Uri mImageUri;
    public void openImageGallery() {
        // invoke the image gallery using an implict intent.
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);

        // where do we want to find the data?
        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pictureDirectoryPath = pictureDirectory.getPath();
        // finally, get a URI representation
        Uri data = Uri.parse(pictureDirectoryPath);

        // set the data and type.  Get all image types.
        photoPickerIntent.setDataAndType(data, "image/*");

        // we will invoke this activity, and get something back from it.
        startActivityForResult(photoPickerIntent, IMAGE_GALLERY_REQUEST);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            // if we are here, everything processed successfully.
            if (requestCode == IMAGE_GALLERY_REQUEST) {
                // if we are here, we are hearing back from the image gallery.

                // the address of the image on the device.
                mImageUri = data.getData();

                // declare a stream to read the image data from the device.
                InputStream inputStream;

                // we are getting an input stream, based on the URI of the image.
                try {

                    inputStream = context.getContentResolver().openInputStream(mImageUri);
                    Picasso.get().load(mImageUri).fit().into(image);


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    // show a message to the user indictating that the image is unavailable.
                    Toast.makeText(context, "Unable to open image", Toast.LENGTH_LONG).show();
                }

            }
        }
    }
}
