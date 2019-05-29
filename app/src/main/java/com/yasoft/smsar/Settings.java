package com.yasoft.smsar;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.yasoft.smsar.models.Images;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
    ImageView profileImage;
    View root;
    Images image;
    String username;
    private Context context;
    private ImageButton mImageButton;
    StorageReference mStorageRef;
    private StorageTask mUploadTask;
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         root=inflater.inflate(R.layout.fragment_settings, container, false);
        context=root.getContext();
         //Toolbar toolbar=root.findViewById(R.layout.fragment_titlebar);
        FirebaseApp.initializeApp(root.getContext());
        firestore=FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("ProfilePictures");


         username = getArguments().getString("username");
        TextView usernameS=root.findViewById(R.id.usernameSetting);
        mImageButton=root.findViewById(R.id.changeImage);
        profileImage =  root.findViewById(R.id.profile_image);
         profileImage.setMaxHeight(52);
         profileImage.setMaxWidth(52);
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
        setProfileImage();

         return root;
    }


    private void logout(){
        ((SmsarMainActivity)getActivity()).logout();

    }

    private int propertyID;
    private void deleteSmsar(){

        firestore.collection("Smsar").document(username).delete();
        firestore.collection("Property").whereEqualTo("mUsername",username).get()
                .addOnSuccessListener(queryDocumentSnapshots -> firestore.collection("Property")
                        .document(queryDocumentSnapshots.getDocuments().toString()).delete());

              }
    Uri mImageUri;
    Bitmap mImage;
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
                   // Picasso.get().load(mImageUri).fit().into(image);
                    mImage = BitmapFactory.decodeStream(inputStream);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    // show a message to the user indictating that the image is unavailable.
                    Toast.makeText(context, "Unable to open image", Toast.LENGTH_LONG).show();
                }
                uploadFile();

            }
        }
    }

    private void uploadFile() {
        /* */
        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            mImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            mUploadTask = fileReference.putBytes(data)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                              @Override
                                              public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                                  fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                      @Override
                                                      public void onSuccess(Uri uri) {

                                                          image = new Images( username+ "", Objects.requireNonNull(uri.toString()));
                                                      }
                                                  })
                                                          .addOnFailureListener(new OnFailureListener() {
                                                              @Override
                                                              public void onFailure(@NonNull Exception e) {
                                                                  Toast.makeText(context, "Upload Failed", Toast.LENGTH_LONG).show();
                                                              }
                                                          })

                                                          .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                                              @Override
                                                              public void onComplete(@NonNull Task<Uri> task) {
                                                                  Toast.makeText(context, task.isComplete() + "", Toast.LENGTH_LONG).show();
                                                                  changeImage();


                                                              }
                                                          })
                                                  ;


                                              }
                                          }
                    )

                    .addOnFailureListener(e ->
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show())

                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(context, "Uploading...", Toast.LENGTH_LONG).show();
                        }
                    });

        } else {
            Toast.makeText(context, "No file selected", Toast.LENGTH_SHORT).show();

        }


    }
    private String getFileExtension(Uri uri) {
        ContentResolver cR = context.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void changeImage(){
        Map<String, Object> data = new HashMap<>();
        data.put("imageUrl", image.getmImageUrl());
        firestore.collection("ProfilePictures").document(username).set(data)
        .addOnSuccessListener(aVoid -> Toast.makeText(context,"Profile Picture Changed ",Toast. LENGTH_LONG).show())
        .addOnCompleteListener(task -> Picasso.get().load(image.getmImageUrl()).fit().into(profileImage))
        ;

    }

    private void setProfileImage(){

        DocumentReference docRef = firestore.collection("ProfilePictures").document(username);
        docRef.get()
                .addOnCompleteListener(task -> {
                    DocumentSnapshot snapshot=task.getResult();
                    if(Objects.requireNonNull(snapshot).exists()){
                        Picasso.get().load(String.valueOf(snapshot.get("imageUrl"))).fit().placeholder(R.drawable.logo_c_144).into(profileImage);

                    }


                });



    }
}
