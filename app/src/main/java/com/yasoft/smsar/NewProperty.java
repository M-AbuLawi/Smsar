package com.yasoft.smsar;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.rxgps.RxGps;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.suke.widget.SwitchButton;
import com.yasoft.smsar.adapters.ImageAdapter;
import com.yasoft.smsar.models.Images;
import com.yasoft.smsar.models.Property;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

import io.reactivex.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewProperty extends Fragment {

    public static final int IMAGE_GALLERY_REQUEST = 20;
    public static final int CAMERA_REQUEST_CODE = 228;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 4192;
    int PICK_IMAGE_MULTIPLE = 1;
    EditText DESC, PRICE, mAddress;
    ImageButton mUpload;
    Button city;
    ImageButton ib;
    DBHelper mDBHelper;
    ArrayList<Bitmap> images;
    FloatingActionButton fab;
    View root;
    TextView mError, mArea;
    Context context;
    final SmsarMainActivity smsar = (SmsarMainActivity) context;
    TextView _mNumberOfRooms, _mNumberOfBathRooms;
    double lat,lon;
    SwitchButton mParking;
    RecyclerView mBrowse;
    String date;
    int mID;
    ImageAdapter mAdapter;
    ImageView imageView;
    String username;
    Images image;
    Bitmap bitmap;
    RadioGroup typeRG,catgoryRG;
    Uri mImageUri;
    Bundle argument;
    private String mCity;
    private String mDesc;
    private String area;
    private String mPrice;
    private String type, category;
    private int noRooms;
    private int noBathrooms;
    private String address;
    private boolean parking;
    private FirebaseFirestore db;
    private StorageReference mStorageRef;
    private DocumentReference detailsRef;
    private StorageTask mUploadTask;

    //  DocumentReference userRef;
    public NewProperty() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_addproperty, container, false);
        context = root.getContext();

        DESC = root.findViewById(R.id.description);
     PRICE = root.findViewById(R.id.price);
      ib = root.findViewById(R.id.addImage);

       city = root.findViewById(R.id.cities);
        fab = root.findViewById(R.id.addProperty);
       mError = root.findViewById(R.id.errorLabel);

       catgoryRG=root.findViewById(R.id.catgoryRadioGroup);
       typeRG=root.findViewById(R.id.typeRadioGroub);
       // mBrowse = root.findViewById(R.id.browseImages);

        final ArrayAdapter<String> citisList = new ArrayAdapter<>(context, R.layout.fragment_testing,
                getResources().getStringArray(R.array.cities));
     //   mBrowse.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        //     mBrowse.setAdapter(new ImageAdapter(new int[]{R.drawable.home,R.drawable.house1,R.drawable.house2,R.drawable.house3,R.drawable.house1}));

      //  imageView = root.findViewById(R.id.testI);

        FirebaseApp.initializeApp(root.getContext());
        db = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("Property");
        //   userRef = db.document("ID/id");
     //   ScrollView scrollView=root.findViewById(R.id.mainScroll);
        //scrollView.onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed);
        if (checkArgumentsForId())
            fillFields();

        //Get user Location
        getLocation();


        Bundle argument;
        argument = getArguments();
        if (argument != null) {
            username = getArguments().getString("username");

        }
        city.setOnClickListener(v->showCitiesDialog(citisList));

        mUpload = root.findViewById(R.id.upload);
//open image Gallery
        mUpload.setOnClickListener(v -> openImageGallery());


        //confirm data
        fab.setOnClickListener(v -> {
            uploadFile();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    addProperty();
                }
            }, 9000);

        });

        //use the camera to capture image
      ib.setOnClickListener(v -> takePhoto());


       _mNumberOfRooms = root.findViewById(R.id.numOfRooms);
        _mNumberOfBathRooms = root.findViewById(R.id.numOfBaths);
        mArea =  root.findViewById(R.id.area);
        mParking = root.findViewById(R.id.parking);
        mAddress =  root.findViewById(R.id.address);


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        date = sdf.format(new Date());

        //   Toast.makeText(root.getContext(),date,Toast.LENGTH_LONG).show(); //   FOE TESTING PURPOSE


        ((SmsarMainActivity) getActivity()).navPointer(R.id.navigation_newApartment);

        return root;
        //  return inflater.inflate(R.layout.fragment_addproperty, container, false);

    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = context.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        /* */
        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                              @Override
                                              public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                  Toast.makeText(context, "Upload successful", Toast.LENGTH_LONG).show();
                                                  fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                      @Override
                                                      public void onSuccess(Uri uri) {
                                                          image = new Images(mID + "", Objects.requireNonNull(uri.toString()));
                                                      }
                                                  })
                                                          .addOnFailureListener(new OnFailureListener() {
                                                              @Override
                                                              public void onFailure(@NonNull Exception e) {
                                                                  Toast.makeText(context, "Upload Failed", Toast.LENGTH_LONG).show();
                                                              }
                                                          });

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

    public void addProperty() throws SQLException {


        if (validationVariable()) {

            boolean parking = false;
            if (mParking.isChecked())
                parking = true;

            prepareForInsert();
            Property mProperty;

       Log.i("lat",lat+"");
            if (!TextUtils.isEmpty(image.getmImageUrl()))
                mProperty = new Property(mID, username, mCity, mDesc, mPrice, noRooms, noBathrooms, address, date, area,
                        parking, Objects.requireNonNull(image.getmImageUrl()),type,category,lon,lat);
            else
                mProperty = new Property(mID, username, mCity, mDesc, mPrice, noRooms, noBathrooms, address, date, area, parking,type,category,lon,lat);
            db.collection("Property").document(mProperty.getmID() + "").set(mProperty)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(context, "done",
                                    Toast.LENGTH_SHORT).show();
                            Toast.makeText(context, lon+"",
                                    Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(context, "Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    })
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(context, "Complete",
                            Toast.LENGTH_SHORT).show();
                }
            })
            ;




        } else {
            mError.setText("Empty Fields");
            mError.setVisibility(View.VISIBLE);
        }
    }
    public void showCitiesDialog(ArrayAdapter adapter) {
        DialogPlus dialog = DialogPlus.newDialog(context)
                .setAdapter(adapter)
                .setOnItemClickListener((dialog1, item, view, position) -> { mCity=item.toString();city.setText(item.toString()); dialog1.dismiss(); })
                .setExpanded(false)
                .create();

        dialog.show();
    }
    public boolean validationVariable() {

        return !TextUtils.isEmpty(mCity)&& !TextUtils.isEmpty(lat+"")&& !TextUtils.isEmpty(lon+"");

    }

    public void takePhoto() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED && context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                invokeCamera();
            } else {
                // let's request permission.
                String[] permissionRequest = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissionRequest, CAMERA_PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_CODE) {
                Toast.makeText(context, "Image Saved.", Toast.LENGTH_LONG).show();
                //imageView.setImageBitmap();
            }
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

                    // get a bitmap from the stream.
                    Bitmap image = BitmapFactory.decodeStream(inputStream);
//mBrowse , mAdapter

                  /*  Images mImage=new Images(image);
                    mAdapter=new ImageAdapter(mImage);
                    mBrowse.setAdapter(mAdapter);*/
                    // show the image to the user
//                    imageView.setImageBitmap(image);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    // show a message to the user indictating that the image is unavailable.
                    Toast.makeText(context, "Unable to open image", Toast.LENGTH_LONG).show();
                }

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            // we have heard back from our request for camera and write external storage.
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                invokeCamera();
            } else {
                Toast.makeText(context, "Can't Open the camera", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void invokeCamera() {

        // get a file reference
        Uri pictureUri = FileProvider.getUriForFile(context, "com.yasoft.smsar" + ".provider", createImageFile());

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // tell the camera where to save the image.
        intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);

        // tell the camera to request WRITE permission.
        intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

        startActivityForResult(intent, CAMERA_REQUEST_CODE);

    }

    private File createImageFile() {
        // the public picture director
        File picturesDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        // timestamp makes unique name.
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
        String timestamp = sdf.format(new Date());

        // put together the directory and the timestamp to make a unique image location.

        return new File(picturesDirectory, username + timestamp + ".jpg");
    }

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

    public void generateId() {
        Random generate = new Random();
        mID = generate.nextInt(1000000);
        if (mID < 0)
            mID *= -1;

    }
    @SuppressLint("CheckResult")
    private void getLocation(){

        new RxGps(Objects.requireNonNull(getActivity())).locationLowPower()

                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.single())

                .subscribe(location -> {
                    lat = location.getLatitude();
                    lon=location.getLongitude();
                }, throwable -> {

                    if (throwable instanceof RxGps.PermissionException) {
                        //the user does not allow the permission
                    } else if (throwable instanceof RxGps.PlayServicesNotAvailableException) {
                        //the user do not have play services
                    }
                });


    }
    public void prepareForInsert() {
      //  mCity = spin.getSelectedItem().toString();

        mDesc = DESC.getText().toString();
        mPrice = PRICE.getText().toString().trim();
        area = mArea.getText().toString().trim();
        noRooms =  Integer.parseInt(_mNumberOfRooms.getText().toString());
        noBathrooms = Integer.parseInt(_mNumberOfBathRooms.getText().toString());
        address = mAddress.getText().toString();

        int selectedTypeId= typeRG.getCheckedRadioButtonId();
        int selectedCategoryId = catgoryRG.getCheckedRadioButtonId();
        RadioButton typeRB,catgoryRB;
        if(selectedCategoryId!=0) {
            catgoryRB = root.findViewById(selectedCategoryId);
            category=catgoryRB.getText().toString();

        }
        if(selectedTypeId!=0) {
            typeRB = root.findViewById(selectedTypeId);
            type=typeRB.getText().toString();
        }

        generateId();
    }

    public boolean checkArgumentsForId() {

        argument = getArguments();
        if (argument != null) {
            return argument.containsKey("id");

        }
        return false;
    }

    private void initializeReference() {

        detailsRef = db.collection("Property").document(argument.getInt("id") + "");
    }
    public void fillFields() {
        initializeReference();
        detailsRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            DESC.setText(documentSnapshot.getString("mDesc"));
                            PRICE.setText(documentSnapshot.getString("mPrice"));
                               city.setText(documentSnapshot.getString("mCity")+" |");
                            mAddress.setText(documentSnapshot.getString("address"));
                            _mNumberOfBathRooms.setText((Objects.requireNonNull(documentSnapshot.get("noBathrooms")).toString()));
                            _mNumberOfRooms.setText(Objects.requireNonNull(documentSnapshot.get("noRooms")).toString());
                            //     mDate.setText(documentSnapshot.getString("date"));
                            mArea.setText(Objects.requireNonNull(documentSnapshot.get("area")).toString());
                            parking = Objects.requireNonNull(documentSnapshot.getBoolean("parking"));
                             //username = rs.getString(rs.getColumnIndex(DBHelper.PROPERTY_COLUMN_SMSARUSERNAME));

                        }

                    }
                });
        if (parking)
            mParking.setChecked(true);

        //Get data from firebase
        //Load data into the  fields

    }




    private void clearFields() {

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                DESC.setText("Write about your description");
                PRICE.setHint("JD");
                mAddress.setHint("Street Name");
                _mNumberOfBathRooms.setHint("1");
                _mNumberOfRooms.setHint("1");
                mArea.setHint("m");
                mParking.setChecked(false);

            }
        }, 2000);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.getArguments().remove("id");
        argument = null;
   clearFields();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.getArguments().remove("id");
        argument = null;
       clearFields();

    }
}
