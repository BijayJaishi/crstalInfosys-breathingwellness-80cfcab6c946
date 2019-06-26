package com.infosys.crystal.breathingwellness;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.infosys.crystal.breathingwellness.adapter.Useradd_adapter;
import com.infosys.crystal.breathingwellness.model.GroupModel;
import com.infosys.crystal.breathingwellness.model.ProfileModelClass;
import com.infosys.crystal.breathingwellness.tab_fragments.GroupsFragment;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Groupcreate extends AppCompatActivity {


    Toolbar toolbar;
    MaterialEditText groupnamee;



    Useradd_adapter useradd_adapter;
    GroupuserRecyclerAdapter groupuserRecyclerAdapter;
    List<ProfileModelClass>userModels;

    FirebaseFirestore db;
    FloatingActionButton done;
    RecyclerView recyclerView,recyclerView3;



    List<GroupModel> usermodelList;
    List<GroupModel> groupModelList;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupcreate);

        toolbar  = findViewById(R.id.tool_bar);
        recyclerView3 = findViewById(R.id.recyclerView3);

        groupnamee = findViewById(R.id.newgroup);
        done = findViewById(R.id.done);


        recyclerView= findViewById(R.id.recyclerView2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userModels = new ArrayList<>();
        groupuserRecyclerAdapter = new GroupuserRecyclerAdapter(this,userModels);
        recyclerView.setAdapter(groupuserRecyclerAdapter);
        done = findViewById(R.id.done);

        usermodelList = new ArrayList<>();
//        groupModelList = new ArrayList<>();

        db = FirebaseFirestore.getInstance();


        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Add Group");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getdetails();
        setdetails();

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectusers();
            }
        });


    }

    private void selectusers() {

        String groupname = groupnamee.getText().toString().trim();

        String user_name =  getIntent().getExtras().getString("user_name");
        String id = getIntent().getExtras().getString("user_id");
        String phone = getIntent().getExtras().getString("user_phone");
        String about_us = getIntent().getExtras().getString("user_about");
        String image = getIntent().getExtras().getString("image_url");


        if(groupname.isEmpty()){
           groupnamee.setError("Please enter Group name first");
           groupnamee.requestFocus();


            /*CollectionReference grouplist = db.collection("Groups");
            GroupModel groupModel = new GroupModel(groupname,user_name,id,phone,about_us,image);

            grouplist.add(groupModel).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {

                    Toast.makeText(Groupcreate.this,"Group Created",Toast.LENGTH_LONG).show();
                    //Intent intent =new Intent(Groupcreate.this,GroupsFragment.class);
                    //startActivity(intent);




                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    Toast.makeText(Groupcreate.this,e.getMessage(),Toast.LENGTH_LONG).show();



                }
            });*/









        }
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
            ArrayList <String> array;
            myViewHolder.username.setText(profileModelClass.getUser_name());
            myViewHolder.about.setText(profileModelClass.getAbout_us());
            Picasso.get().load(profileModelClass.getImage()).placeholder(R.drawable.image_placeholder).error(R.drawable.image_placeholder).into(myViewHolder.image);


            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    try {


//
                    groupModelList = new ArrayList<>();



//                    if (groupModelList.size() != 0){


//
//                    for (int i=0; i<usermodelList.size();i++){
//                        Toast.makeText(Groupcreate.this,"Size : "+usermodelList.size(), Toast.LENGTH_SHORT).show();
//                    }

                        if (usermodelList.size() !=0 ){
                            for (GroupModel groupModels : usermodelList){

                                if (groupModels.getUser_name().equals(profileModelClass.getUser_name())){
                                    Toast.makeText(context, "Name : "+groupModels.getUser_name(), Toast.LENGTH_SHORT).show();
                                    groupModelList.remove(groupModels.getImage());
                                    groupModelList.remove(groupModels.getUser_name());
                                    groupModelList.remove(groupModels.getAbout_us());
                                    groupModelList.remove(groupModels.getId());
                                    groupModelList.remove(groupModels.getPhone());

                                    usermodelList.remove(groupModels.getImage());
                                    usermodelList.remove(groupModels.getPhone());
                                    usermodelList.remove(groupModels.getId());
                                    usermodelList.remove(groupModels.getUser_name());
                                    usermodelList.remove(groupModels.getAbout_us());

                                    getUserList();
                                    Toast.makeText(context, "Already Added : "+groupModels.getUser_name(), Toast.LENGTH_SHORT).show();

                                }else {



                                    groupModelList.add(new GroupModel(profileModelClass.getImage(),profileModelClass.getUser_name(),profileModelClass.getAbout_us(),profileModelClass.getPhone(),profileModelClass.getId()));
                                    getUserList();
                                }

                            }




                    }else {



                        groupModelList.add(new GroupModel(profileModelClass.getImage(),profileModelClass.getUser_name(),profileModelClass.getAbout_us(),profileModelClass.getPhone(),profileModelClass.getId()));
                            getUserList();



                    }


                    }catch (Exception e){
                        e.printStackTrace();
                    }

//                useradd_adapter = new Useradd_adapter(context,)

                    String name = profileModelClass.getUser_name();
                    String image_url = profileModelClass.getImage();
                    String phone_no = profileModelClass.getPhone();
                    String about = profileModelClass.getAbout_us();
                    String id = profileModelClass.getId();





//                GroupModel groupModel = new GroupModel(name,image_url,phone_no,about,id);
//                array = new ArrayList<>();
//                String [] strings = {name , image_url ,phone_no,about,id};
//                array.addAll(Arrays.asList(strings));


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

        }
        recyclerView3.setHasFixedSize(true);
        LinearLayoutManager layoutManager= new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView3.setLayoutManager(layoutManager);

        useradd_adapter = new Useradd_adapter(this,usermodelList);
        recyclerView3.setAdapter(useradd_adapter);
        useradd_adapter.notifyDataSetChanged();

    }



}
