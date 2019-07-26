package com.infosys.crystal.breathingwellness.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.infosys.crystal.breathingwellness.MainActivity;
import com.infosys.crystal.breathingwellness.R;
import com.infosys.crystal.breathingwellness.adapter.Useradd_adapter;
import com.infosys.crystal.breathingwellness.model.GroupModel;
import com.infosys.crystal.breathingwellness.model.Groupdatabasepush;
import com.infosys.crystal.breathingwellness.model.ProfileModelClass;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;


public class Groupcreate extends AppCompatActivity {

    Toolbar toolbar;
    MaterialEditText groupnamee;
    ProgressDialog progressDialog;

    String image_address;


    Useradd_adapter useradd_adapter;
    GroupuserRecyclerAdapter groupuserRecyclerAdapter;
    List<ProfileModelClass>userModels;
    ArrayList <String> array;


    FirebaseFirestore db;
    FloatingActionButton done,group_imagebtn;
    CircleImageView group_imageView;
    RecyclerView recyclerView,recyclerView3;

    String name,image_url,phone_no,id,about;
    String groupname;

    List<GroupModel> usermodelList;
    List<GroupModel> groupModelList;


    final int RQS_IMAGE1 = 1;
    Uri source1;
    Bitmap bm1;
    Uri resultUri;
    File file1;
    StorageReference storageReference;
    FirebaseStorage storage;

    RelativeLayout image_layout;
    LinearLayout linearLayout;
    Button save_photo;



    private static final int MY_PERMISSIONS_REQUEST_STORAGE = 0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupcreate);

        toolbar  = findViewById(R.id.tool_bar);
        recyclerView3 = findViewById(R.id.recyclerView3);
        groupnamee = findViewById(R.id.newgroup);
        progressDialog = new ProgressDialog(this);
        image_layout = findViewById(R.id.image_layout);
        save_photo = findViewById(R.id.save_photo);
        linearLayout = findViewById(R.id.linearlayout);

        recyclerView= findViewById(R.id.recyclerView2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userModels = new ArrayList<>();
        groupuserRecyclerAdapter = new GroupuserRecyclerAdapter(this,userModels);
        recyclerView.setAdapter(groupuserRecyclerAdapter);
        done = findViewById(R.id.done);
        group_imagebtn = findViewById(R.id.group_image);
        group_imageView = findViewById(R.id.profile_image);

        usermodelList = new ArrayList<>();
//        groupModelList = new ArrayList<>();

        //Firebase Storage Initilization

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();



        db = FirebaseFirestore.getInstance();


        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Add Group");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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

        getdetails();
        setdetails();
        save_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectusers();
            }
        });


        group_imagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_image();
            }
        });

        group_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_image();
            }
        });

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


    @SuppressLint("RestrictedApi")
    private void add_image() {

        //select user image

        image_layout.setVisibility(View.VISIBLE);
        done.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        group_imagebtn.setVisibility(View.INVISIBLE);
        linearLayout.setVisibility(View.INVISIBLE);


        Intent intent = CropImage.activity(source1)
                .getIntent(getApplicationContext());
        startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);


    }

        /*
    function to update user detail
     */




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
                group_imageView.setImageBitmap(bm1);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public void upload(){
        if (resultUri != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child(new StringBuilder("Group_propic/").append(UUID.randomUUID().toString())
                    .toString());


            ref.putFile(resultUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    progressDialog.dismiss();


                                    image_address = uri.toString();
                                    selectusers();




                                }
                            });
                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Groupcreate.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @SuppressLint("RestrictedApi")
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0) * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Upload Complete..." + (int) progress + "%");
                            if (progress == 100){
                                Toast.makeText(Groupcreate.this, "Group Image Selected", Toast.LENGTH_SHORT).show();
                                image_layout.setVisibility(View.INVISIBLE);
                                done.setVisibility(View.VISIBLE);
                                linearLayout.setVisibility(View.VISIBLE);
                                group_imagebtn.setVisibility(View.VISIBLE);
                                group_imagebtn.setClickable(false);
                                recyclerView.setVisibility(View.VISIBLE);

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



    private void selectusers() {

        groupname = groupnamee.getText().toString().trim();



        if(groupname.isEmpty()) {
                //recyclerView3.setVisibility(View.GONE);
                groupnamee.setError("Please enter Group name first");
                groupnamee.requestFocus();
                return;
        }

        if(resultUri == null){
            groupnamee.setError("Please upload Image");
            groupnamee.requestFocus();
            return;
        }


             Groupdatabasepush groupdatabasepush = new Groupdatabasepush(groupname,usermodelList,image_address);
             CollectionReference grouplist = db.collection("Groups");
             progressDialog.setMessage(" Creating please Wait...");
             progressDialog.show();
             //progressDialog.setCancelable(false);


                                    grouplist.add(groupdatabasepush).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {

                                            progressDialog.dismiss();

                                            Toast.makeText(Groupcreate.this,groupname+ "  Created",Toast.LENGTH_LONG).show();
                                            finish();
                                            //Intent intent =new Intent(Groupcreate.this,GroupsFragment.class);
                                            // startActivity(intent);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            Toast.makeText(Groupcreate.this,e.getMessage(),Toast.LENGTH_LONG).show();
                                        }
                                    });



    }

    private void setdetails() {


        db.collection("userDetail").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if(!queryDocumentSnapshots.isEmpty()){
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                    for (DocumentSnapshot documentSnapshot : list){

                        ProfileModelClass u = documentSnapshot.toObject(ProfileModelClass.class);
//                        getUserModels.add(u);


                    }
//                    useradd_adapter.notifyDataSetChanged();

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getdetails() {


        db.collection("userDetail").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if(!queryDocumentSnapshots.isEmpty()){
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                    for (DocumentSnapshot documentSnapshot : list){

                        ProfileModelClass u = documentSnapshot.toObject(ProfileModelClass.class);
                        userModels.add(u);


                    }

                    groupuserRecyclerAdapter.notifyDataSetChanged();
//                    useradd_adapter.notifyDataSetChanged();

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });



    }

    public class GroupuserRecyclerAdapter extends RecyclerView.Adapter<GroupuserRecyclerAdapter.MyViewHolder> {


        Context context;
        List<ProfileModelClass>profileModelClasses;

        public GroupuserRecyclerAdapter(Context context, List<ProfileModelClass> profileModelClasses) {
            this.context = context;
            this.profileModelClasses = profileModelClasses;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_item2,viewGroup,false);

            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {

//            groupModel = new GroupModel();

            final ProfileModelClass profileModelClass = profileModelClasses.get(i);

            myViewHolder.username.setText(profileModelClass.getUser_name());
            myViewHolder.about.setText(profileModelClass.getAbout_us());
            Picasso.get().load(profileModelClass.getImage()).placeholder(R.drawable.image_placeholder).error(R.drawable.image_placeholder).into(myViewHolder.image);


            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    recyclerView3.setVisibility(View.VISIBLE);
                    groupnamee.clearValidators();


                    try {


//
                    groupModelList = new ArrayList<>();




//                    if (groupModelList.size() != 0){


//
//                    for (int i=0; i<usermodelList.size();i++){
//                        Toast.makeText(Groupcreate.this,"Size : "+usermodelList.size(), Toast.LENGTH_SHORT).show();
//                    }

                        if (usermodelList.size() !=0 ) {
                            for (GroupModel groupModels : usermodelList) {





                                if (groupModels.getUser_name().equals(profileModelClass.getUser_name())){
                                    Toast.makeText(context, "Name : "+groupModels.getUser_name(), Toast.LENGTH_SHORT).show();
//                                    groupModelList.remove(groupModels.getImage());
//                                    groupModelList.remove(groupModels.getUser_name());
//                                    groupModelList.remove(groupModels.getAbout_us());
//                                    groupModelList.remove(groupModels.getId());
//                                    groupModelList.remove(groupModels.getPhone());
//
//                                    usermodelList.remove(groupModels.getImage());
//                                    usermodelList.remove(groupModels.getPhone());
//                                    usermodelList.remove(groupModels.getId());
//                                    usermodelList.remove(groupModels.getUser_name());
//                                    usermodelList.remove(groupModels.getAbout_us());

                                    //usermodelList.remove(groupModels);


                                    usermodelList.remove(groupModels);

                                    getUserList();
                                    Toast.makeText(context, "Removed: "+groupModels.getUser_name(), Toast.LENGTH_SHORT).show();


                                }else {





                                    usermodelList.add(new GroupModel(profileModelClass.getImage(),profileModelClass.getUser_name(),profileModelClass.getAbout_us(),profileModelClass.getPhone(),profileModelClass.getId()));
                                    Toast.makeText(context," Added :"+profileModelClass.getUser_name(),Toast.LENGTH_SHORT).show();
                                    myViewHolder.itemView.setClickable(false);
                                    //Toast.makeText(context,"Already Added :"+profileModelClass.getUser_name(),Toast.LENGTH_SHORT).show();

                                    name = profileModelClass.getUser_name();
                                    image_url = profileModelClass.getImage();
                                    phone_no = profileModelClass.getPhone();
                                    about = profileModelClass.getAbout_us();
                                    id = profileModelClass.getId();
                                    getUserList();

                                }

                            }




                    }else {



                        usermodelList.add(new GroupModel(profileModelClass.getImage(),profileModelClass.getUser_name(),profileModelClass.getAbout_us(),profileModelClass.getPhone(),profileModelClass.getId()));
                            Toast.makeText(context," Added :"+profileModelClass.getUser_name(),Toast.LENGTH_SHORT).show();
                            myViewHolder.itemView.setClickable(false);
                            //Toast.makeText(context,"Already Added :"+profileModelClass.getUser_name(),Toast.LENGTH_SHORT).show();

                            getUserList();










                        }


                    }catch (Exception e){
                        e.printStackTrace();
                    }

//                useradd_adapter = new Useradd_adapter(context,)







//                GroupModel groupModel = new GroupModel(name,image_url,phone_no,about,id);
//                 array = new ArrayList<>();
//                 String [] strings = {name , image_url ,phone_no,about,id};
//                 array.addAll(Arrays.asList(strings));


//                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();




                }
            });

        }

        @Override
        public int getItemCount() {
            return profileModelClasses.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder{

            TextView username,about;
            CircleImageView image;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);

                username = itemView.findViewById(R.id.uname);
                //check = itemView.findViewById(R.id.check);
                image = itemView.findViewById(R.id.proimage);
                about = itemView.findViewById(R.id.about);
            }
        }
    }

    public void getUserList(){

        for (GroupModel groupModel : groupModelList){

            usermodelList.add(groupModel);
            usermodelList.addAll(Arrays.asList(groupModel));




        }
        recyclerView3.setHasFixedSize(true);
        LinearLayoutManager layoutManager= new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView3.setLayoutManager(layoutManager);

        useradd_adapter = new Useradd_adapter(this,usermodelList);
        recyclerView3.setAdapter(useradd_adapter);
        useradd_adapter.notifyDataSetChanged();

    }



}
