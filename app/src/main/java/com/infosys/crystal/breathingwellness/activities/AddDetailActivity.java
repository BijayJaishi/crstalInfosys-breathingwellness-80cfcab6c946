package com.infosys.crystal.breathingwellness.activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.infosys.crystal.breathingwellness.MainActivity;
import com.infosys.crystal.breathingwellness.R;
import com.infosys.crystal.breathingwellness.model.ProfileModelClass;
import com.infosys.crystal.breathingwellness.model.UserSignInCheck;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class AddDetailActivity extends AppCompatActivity {
    Toolbar toolbar;
    private DatabaseReference userDatabase;
    TextInputEditText txtName,txtPhone,txtAbout;
    CircleImageView userImage;
    final int RQS_IMAGE1 = 1;
    Uri source1;
    Bitmap bm1;
    Uri resultUri;
    File file1;
    StorageReference storageReference;
    FirebaseStorage storage;
    FirebaseFirestore db;


    private static final int MY_PERMISSIONS_REQUEST_STORAGE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_detail);
        toolbar  = findViewById(R.id.tool_bar);

        txtAbout = findViewById(R.id.txtAbout);
        txtName = findViewById(R.id.txt_userName);
        txtPhone = findViewById(R.id.txtPhone);
        userImage = findViewById(R.id.profile_image);

        //Firebase Storage Initilization

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        // Firestore initialization

        db = FirebaseFirestore.getInstance();
        String id;


        /*
        getting refrence for firebase database
         */
        userDatabase = FirebaseDatabase.getInstance().getReference();

        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Add Detail");

        //// asking for permission to read the gallery

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_STORAGE);
            }
        }

        txtName.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());



    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            }

        }
    }



    /*

        select user image

     */
    public void ChangeAvatar(View view) {

        Intent intent = CropImage.activity(source1)
                .getIntent(getApplicationContext());
        startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);

    }


     /*
    function to update user detail
     */

    private void writeUserDetail(String UserId, String name, String image, String about, String phone) {
        ProfileModelClass user = new ProfileModelClass(image, name, about, phone);

        userDatabase.child("userDetail").child(UserId).setValue(user);



       CollectionReference dbusers = db.collection("userDetail");
        //DocumentReference dbu = dbusers.document(userId);

        dbusers.add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(AddDetailActivity.this,"Saved",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(AddDetailActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        });


    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode == RESULT_OK){
//            switch (requestCode){
//                case RQS_IMAGE1:
//                    source1 = data.getData();
//
//                    try {
//                        System.out.println("Bitmap path = "+source1.getPath());
//                        bm1 = BitmapFactory.decodeStream(
//                                getContentResolver().openInputStream(source1));
//                        ByteArrayOutputStream out = new ByteArrayOutputStream();
//                        // bm1.compress(Bitmap.CompressFormat.PNG,100,out);
//                        bm1.compress(Bitmap.CompressFormat.JPEG,70,out);
//                        userImage.setImageBitmap(bm1);
//
//                        String[] filePathColumn = { MediaStore.Images.Media.DATA };
//                        Cursor cursor = getContentResolver().query(source1, filePathColumn, null, null, null);
//                        cursor.moveToFirst();
//                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                        String filePath = cursor.getString(columnIndex);
//                        cursor.close();
//
//                        file1 = new Compressor(this).compressToFile(new File(filePath));
//
//
//                        //System.out.println("Image :"+bm1);
//                        System.out.println("Image :"+file1.length());
//
//                    } catch (NullPointerException e) {
//                        e.printStackTrace();
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                    break;
//            }
//        }
//    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resultUri = result.getUri();
                try {
                    bm1 = BitmapFactory.decodeStream(
                                    getContentResolver().openInputStream(resultUri));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                        // bm1.compress(Bitmap.CompressFormat.PNG,100,out);
                        bm1.compress(Bitmap.CompressFormat.JPEG,70,out);
                        userImage.setImageBitmap(bm1);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    /*

    save image in firebase store

     */

    private void upload() {

        if (resultUri != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child(new StringBuilder("images/").append(UUID.randomUUID().toString())
                    .toString());


            ref.putFile(resultUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    progressDialog.dismiss();

                                    String username = txtName.getText().toString().trim();
                                    String about = txtAbout.getText().toString().trim();
                                    String phone = txtPhone.getText().toString().trim();


                                    if (username.equals("")){
                                        Toast.makeText(AddDetailActivity.this, "Please enter name", Toast.LENGTH_SHORT).show();
                                    }else if (phone.equals("")){
                                        Toast.makeText(AddDetailActivity.this, "Please enter phone", Toast.LENGTH_SHORT).show();
                                    }else if (about.equals("")){
                                        Toast.makeText(AddDetailActivity.this, "Please post your detail", Toast.LENGTH_SHORT).show();
                                    }else{
                                        writeUserDetail(FirebaseAuth.getInstance().getCurrentUser().getUid(),username,uri.toString(),about,phone);
                                    }


//                                        String password = String.valueOf(R.string.app_name);
//                                        String encryptedMsg = AESCrypt.encrypt(password, emojiconEditText.getText().toString());
//                                        if (emojiconEditText.getText().toString().equals("")) {
//
//                                            FirebaseDatabase.getInstance().getReference().push().setValue(new ChatMessage("image", "0",
//                                                    FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), uri.toString()));
////
//                                        }


                                }
                            });
                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddDetailActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0) * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Upload Complete..." + (int) progress + "%");
                            if (progress == 100){
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            }

                        }
                    });
        } else {
            Toast.makeText(this, "Please insert image!!!", Toast.LENGTH_SHORT).show();
            //                String password = String.valueOf(R.string.app_name);
//                String encryptedMsg = AESCrypt.encrypt(password, emojiconEditText.getText().toString());


//                FirebaseDatabase.getInstance().getReference().push().setValue(new ChatMessage("text", encryptedMsg,
//                        FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), "0"));
//
//
//                emojiconEditText.setText("");
//                emojiconEditText.requestFocus();
//                adapter.notifyDataSetChanged();
        }
    }

    /*

    save user detail

     */

    public void save(View view) {

       upload();
    }
}
