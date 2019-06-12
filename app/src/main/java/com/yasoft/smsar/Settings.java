package com.yasoft.smsar;


import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.os.Environment;
import android.text.InputType;
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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.kongzue.dialog.listener.InputDialogOkButtonClickListener;
import com.kongzue.dialog.util.InputInfo;
import com.kongzue.dialog.v2.InputDialog;
import com.kongzue.dialog.v2.Notification;
import com.kongzue.dialog.v2.TipDialog;
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
    CollectionReference cr,smsarRef;
    StorageReference mStorageRef;
    FirebaseStorage storage;
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
         storage = FirebaseStorage.getInstance();
        cr=firestore.collection("Property");
        smsarRef=firestore.collection("Smsar");
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

        li=root.findViewById(R.id.listSetting);
    li.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                case 0:
                 showInputDialog("Set your Fee","Please Set your fee \nOnly from 0 to 9 percent is what allowed you to set\nYou can update it and delete it anytime","Your Fee is : "," %",InputType.TYPE_CLASS_NUMBER,1,"fee");
                    break;
                case 1:
                  showInputDialog("Change your PhoneNumber","Please Change your PhoneNumber, you can update it anytime","Your new PhoneNumber is : ","",InputType.TYPE_CLASS_PHONE,14,"phone");
                    break;
                case 2:
                showInputDialog("Change your Email","Please Change your Email, you can update it anytime.\nemail is very important to be valid and real.","Your new Email is : ","",InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS,50,"email");
                    break;
                case 3:
               showInputDialog("Change your Password","Please Change your Password, No one will know your password except you.\nyou can update it anytime.","Your new password is : ","",InputType.TYPE_NUMBER_VARIATION_PASSWORD,50,"password");
                    break;
                case 4:
                    ((SmsarMainActivity) getActivity()).logout();
                    break;
                case 5:
                    new SweetAlertDialog(root.getContext(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Are you sure?")
                            .setContentText("All of your data will be gone!")
                            .setConfirmText("Yes, delete it!")
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
                    break;

            }


        }
    });
        ((SmsarMainActivity)getActivity()).navPointer(R.id.navigation_account);
        setProfileImage();

         return root;
    }

    private void setFee(String newFee){
       // int fee=Integer.parseInt(newFee);
        smsarRef.document(username).update("fee",newFee);

    }
    private void updatePassword(String newPassword){
       newPassword=EncryptString.encryptString(newPassword);
        smsarRef.document(username).update("mPassword",newPassword);

    }
    private void updateEmail(String newEmail) {
        smsarRef.document(username).update("mEmail",newEmail);

    }


    private void updatePhoneNumber(String newPhoneNumber) {
        smsarRef.document(username).update("mPhoneNumber",newPhoneNumber);
    }



    private void showInputDialog(String TITLE, String MESSAGE, String OUTPUT_MESSAGE , String SIGN,int INPUT_TYPE,int MAX_LENGTH,String TYPE){
      InputDialog.show(context, TITLE, MESSAGE, "Confirm", new InputDialogOkButtonClickListener() {
            @Override
            public void onClick(Dialog dialog, String inputText) {
               Toast.makeText(context, OUTPUT_MESSAGE + inputText+SIGN, Toast.LENGTH_SHORT).show();
                String input=inputText;
                updateData(input,TYPE);
               dialog.dismiss();
            }
        }, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setInputInfo(new InputInfo()
              .setMAX_LENGTH(MAX_LENGTH)
                .setInputType(INPUT_TYPE));



    }

    private void updateData(String input,String Type) {
        if(Type.contains("fee"))
            setFee(input);
        if(Type.contains("email"))
            updateEmail(input);
        if(Type.contains("password"))
            updatePassword(input);
        if(Type.contains("phone"))
            updatePhoneNumber(input);
    }

    private void logout(){
        ((SmsarMainActivity)getActivity()).logout();

    }







    private void deleteSmsar(){
        WriteBatch batch = firestore.batch();
        firestore.collection("Smsar").document(username).delete();
        firestore.collection("Property").whereEqualTo("mUsername",username).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                       //     queryDocumentSnapshots.forEach(v-> cr.document(queryDocumentSnapshots.getDocuments().toString()).delete());
                          for (int i=0 ; i<queryDocumentSnapshots.size();i++)
                           batch.delete(cr.document(queryDocumentSnapshots.getDocuments().get(i).getId()));
                          batch.commit();

                        }
                        else {
                            int count=0;
                            while (queryDocumentSnapshots.size() > count){
                                cr.document(queryDocumentSnapshots.getDocuments().toString()).delete();
                            count++;
                            }
                        }
                    }
                });


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
                    .addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            image = new Images( username+ "", Objects.requireNonNull(uri.toString()));
                        }
                    })
                            .addOnFailureListener(e -> Toast.makeText(context, "Upload Failed", Toast.LENGTH_LONG).show())

                            .addOnCompleteListener(task -> changeImage())
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
        storage.getReferenceFromUrl(imageUrl).delete();
        Map<String, Object> data = new HashMap<>();
        data.put("imageUrl", image.getmImageUrl());
        firestore.collection("ProfilePictures").document(username).set(data)
        .addOnSuccessListener(aVoid -> Toast.makeText(context,"Profile Picture Changed ",Toast. LENGTH_LONG).show())
        .addOnCompleteListener(task -> Picasso.get().load(image.getmImageUrl()).fit().into(profileImage))
        ;

    }

    String imageUrl;
    private void setProfileImage(){

        DocumentReference docRef = firestore.collection("ProfilePictures").document(username);
        docRef.get()
                .addOnCompleteListener(task -> {
                    DocumentSnapshot snapshot=task.getResult();
                    if(Objects.requireNonNull(snapshot).exists()){
                        imageUrl=snapshot.getString("imageUrl");
                        Picasso.get().load(String.valueOf(snapshot.get("imageUrl"))).fit().placeholder(R.drawable.logo_c_144).into(profileImage);

                    }


                });



    }
}
