package com.yasoft.smsar;


import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.SQLException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.fragment.app.Fragment;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.orhanobut.dialogplus.DialogPlus;
import com.squareup.picasso.Picasso;
import com.suke.widget.SwitchButton;
import com.yasoft.smsar.adapters.ImageAdapter;
import com.yasoft.smsar.models.Images;
import com.yasoft.smsar.models.Property;

import org.imperiumlabs.geofirestore.GeoFirestore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Random;


import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewProperty extends Fragment {

    public static final int IMAGE_GALLERY_REQUEST = 20;
    public static final int CAMERA_REQUEST_CODE = 228;
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 4192;
    int PICK_IMAGE_MULTIPLE = 1;
    ImageButton dec, inc, inc1, dec1, exit;
    EditText DESC, PRICE, mAddress;
    ImageButton mUpload;
    Button city;
    ImageButton ib;
    DBHelper mDBHelper;
    ArrayList<Bitmap> images;
    FloatingActionButton fab;
    View root;
    View imageLayout;
    TextView mError, mArea;
    Context context;
    final SmsarMainActivity smsar = (SmsarMainActivity) context;
    TextView _mNumberOfRooms, _mNumberOfBathRooms;
    double latitude, longitude;
    SwitchButton mParking;
    RecyclerView mBrowse;
    String date;
    int mID;
    List<String> likedList;
    ImageAdapter mAdapter;
    ImageView imageView;
    String username;
    Images image;
    Bitmap bitmap;
    RadioGroup typeRG, catgoryRG;
    Uri mImageUri;
    Bundle argument;
    CollectionReference geoFirestoreRef = FirebaseFirestore.getInstance().collection("Property");
    GeoFirestore geoFirestore = new GeoFirestore(geoFirestoreRef);
    boolean propertyUploadedFlag;
    Bitmap mImage;
    private TextView _screenRooms;
    private TextView _screenBaths;
    private int numberOfRooms = 1, numberOfBathrooms = 1;
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
    ;
    private StorageTask mUploadTask;

    //  DocumentReference userRef;
    public NewProperty() {
        // Required empty public constructor


    }

    public static Bitmap addWaterMark(Bitmap src, int color, int alpha, Context mContext) {
        int w = src.getWidth();
        int h = src.getHeight();
        Bitmap result = Bitmap.createBitmap(w, h, src.getConfig());

        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(src, 0, 0, null);
        Bitmap waterMark = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.watermark);
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setAlpha(alpha);
        paint.setAntiAlias(true);
        canvas.drawBitmap(waterMark, 0, 0, paint);

        return result;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Get user Location


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_addproperty, container, false);


        context = root.getContext();
        imageLayout = root.findViewById(R.id.imageLayout);
        DESC = root.findViewById(R.id.description);
        PRICE = root.findViewById(R.id.price);
        ib = root.findViewById(R.id.addImage);

        city = root.findViewById(R.id.cities);
        fab = root.findViewById(R.id.next);
        mError = root.findViewById(R.id.errorLabel);

        catgoryRG = root.findViewById(R.id.catgoryRadioGroup);
        typeRG = root.findViewById(R.id.typeRadioGroub);


        inc = root.findViewById(R.id.increaseR);
        dec = root.findViewById(R.id.decreaseR);

        inc1 = root.findViewById(R.id.increaseB);
        dec1 = root.findViewById(R.id.decreaseB);
        // mBrowse = root.findViewById(R.id.browseImages);

        final ArrayAdapter<String> citisList = new ArrayAdapter<>(context, R.layout.fragment_testing,
                getResources().getStringArray(R.array.cities));
        //   mBrowse.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        //     mBrowse.setAdapter(new ImageAdapter(new int[]{R.drawable.home,R.drawable.house1,R.drawable.house2,R.drawable.house3,R.drawable.house1}));

        imageView = root.findViewById(R.id.propertyPicture);
        getLocation();
        FirebaseApp.initializeApp(root.getContext());

        db = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference("Property");
        //   userRef = db.document("ID/id");
        //   ScrollView scrollView=root.findViewById(R.id.mainScroll);
        //scrollView.onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed);
        if (checkArgumentsForId())
            fillFields();


        Bundle argument;
        argument = getArguments();
        if (argument != null) {
            username = getArguments().getString("username");

        }
        city.setOnClickListener(v -> showCitiesDialog(citisList));

        mUpload = root.findViewById(R.id.upload);
//open image Gallery
        mUpload.setOnClickListener(v -> openImageGallery());


        //confirm data
        fab.setOnClickListener(v -> {
            if (mImage != null)
                uploadFile();
            else
                addProperty();

        });

        //use the camera to capture image
        ib.setOnClickListener(v -> takePhoto());


        _mNumberOfRooms = root.findViewById(R.id.numOfRooms);
        _mNumberOfBathRooms = root.findViewById(R.id.numOfBaths);
        mArea = root.findViewById(R.id.area);
        mParking = root.findViewById(R.id.parking);
        mAddress = root.findViewById(R.id.address);


        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        date = sdf.format(new Date());

        //   Toast.makeText(root.getContext(),date,Toast.LENGTH_LONG).show(); //   FOE TESTING PURPOSE


        ((SmsarMainActivity) getActivity()).navPointer(R.id.navigation_newApartment);

        inc.setOnClickListener(v -> {
            ++numberOfRooms;
            updateScreen();
        });

        dec.setOnClickListener(v -> {
            --numberOfRooms;
            updateScreen();
        });


        inc1.setOnClickListener(v -> {
            ++numberOfBathrooms;
            updateScreen();
        });

        dec1.setOnClickListener(v -> {
            --numberOfBathrooms;
            updateScreen();
        });


        return root;
        //  return inflater.inflate(R.layout.fragment_addproperty, container, false);

    }

    private void updateScreen() {

        check();

        _mNumberOfRooms.setText(numberOfRooms + "");
        _mNumberOfBathRooms.setText(numberOfBathrooms + "");
    }

    void check() {
        if (numberOfRooms < 1)
            numberOfRooms = 1;
        if (numberOfBathrooms < 1)
            numberOfBathrooms = 1;
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = context.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {
        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            mImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            mUploadTask = fileReference.putBytes(data)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
         boolean flag = false;

          @Override
               public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                   fileReference.getDownloadUrl().addOnSuccessListener(uri ->
                           image = new Images(mID + "", Objects.requireNonNull(uri.toString())))
                        .addOnFailureListener(e ->
                                Toast.makeText(context, "Upload Failed", Toast.LENGTH_LONG).show())
                    .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                  @Override
                      public void onComplete(@NonNull Task<Uri> task) {
                          Toast.makeText(context, task.isComplete() + "", Toast.LENGTH_LONG).show();
                         addProperty();  }
                });
             }
         }
            )
            .addOnFailureListener(e ->
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show())
                    .addOnProgressListener(taskSnapshot ->
                            Toast.makeText(context, "Uploading...", Toast.LENGTH_LONG).show());
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


            if (!TextUtils.isEmpty(image.getmImageUrl()))
                mProperty = new Property(mID, username, mCity, mDesc, mPrice, noRooms, noBathrooms, address, date, area,
                        parking, Objects.requireNonNull(image.getmImageUrl()), type, category, longitude, latitude,likedList);
            else
                mProperty = new Property(mID, username, mCity, mDesc, mPrice, noRooms, noBathrooms, address, date, area, parking, type, category, longitude, latitude,likedList);

            db.collection("Property").document(mProperty.getmID() + "").set(mProperty)

                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            propertyUploadedFlag = true;
                            Toast.makeText(context, "done",
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

                            if (propertyUploadedFlag)
                                Toast.makeText(context, "Upload successful", Toast.LENGTH_LONG).show();
                            geoFirestore.setLocation(mProperty.getmID() + "", new GeoPoint(latitude, longitude), new GeoFirestore.CompletionListener() {

                                @Override
                                public void onComplete(Exception exception) {
                                    if (exception != null) {
                                        System.out.println("Location saved on server successfully!");
                                    }
                                }
                            });
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
                .setOnItemClickListener((dialog1, item, view, position) -> {
                    mCity = item.toString();
                    city.setText(item.toString());
                    dialog1.dismiss();
                })
                .setExpanded(false)
                .create();

        dialog.show();
    }

    public boolean validationVariable() {

        return !TextUtils.isEmpty(mCity) && !TextUtils.isEmpty(latitude + "") && !TextUtils.isEmpty(longitude + "");

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
                    mImage = BitmapFactory.decodeStream(inputStream);
//mBrowse , mAdapter

                    mImage = addWaterMark(mImage, Color.WHITE, 40, context);
                  /*  Images mImage=new Images(image);
                    mAdapter=new ImageAdapter(mImage);
                    mBrowse.setAdapter(mAdapter);*/
                    // show the image to the user
                    imageLayout.setVisibility(View.VISIBLE);
                    //   imageView.setImageBitmap(mImage);
                    Picasso.get().load(mImageUri).fit().into(imageView);


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


    private void getLocation() {

        UserLocation userLocation = new UserLocation(getActivity(), context);
        longitude = userLocation.getLongitude();
        latitude = userLocation.getLatitude();

    }


    public void prepareForInsert() {
        //  mCity = spin.getSelectedItem().toString();

        mDesc = DESC.getText().toString();
        mPrice = PRICE.getText().toString().trim();
        area = mArea.getText().toString().trim();
        noRooms = Integer.parseInt(_mNumberOfRooms.getText().toString());
        noBathrooms = Integer.parseInt(_mNumberOfBathRooms.getText().toString());
        address = mAddress.getText().toString();

        int selectedTypeId = typeRG.getCheckedRadioButtonId();
        int selectedCategoryId = catgoryRG.getCheckedRadioButtonId();
        RadioButton typeRB, catgoryRB;
        if (selectedCategoryId != 0) {
            catgoryRB = root.findViewById(selectedCategoryId);
            category = catgoryRB.getText().toString();

        }
        if (selectedTypeId != 0) {
            typeRB = root.findViewById(selectedTypeId);
            type = typeRB.getText().toString();
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
                            city.setText(documentSnapshot.getString("mCity") + " |");
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
                DESC.setHint("Write about your description here");
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


