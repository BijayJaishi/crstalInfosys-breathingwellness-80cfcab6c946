package com.infosys.crystal.breathingwellness;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.infosys.crystal.breathingwellness.activities.AddDetailActivity;
import com.infosys.crystal.breathingwellness.activities.ProfileActivity;
import com.infosys.crystal.breathingwellness.adapter.TabAdapter;
import com.infosys.crystal.breathingwellness.model.ProfileModelClass;
import com.infosys.crystal.breathingwellness.model.UserSignInCheck;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {



    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;
    CoordinatorLayout activity_main;
    TabAdapter tabAdapter;
    FloatingActionButton fab;
    private DatabaseReference mdataBase;

     Groupcreate.GroupuserRecyclerAdapter groupuserRecyclerAdapter;

    List<ProfileModelClass>userModels;
    FirebaseFirestore db;



     /*
        defining icon and background color for floating icon
     */
    int[] colorIntArray = {R.color.green,R.color.colorAccent,R.color.colorPrimary};
    int[] iconIntArray = {R.drawable.ic_message_black_24dp,R.drawable.ic_group_add_black_24dp,R.drawable.ic_phone_in_talk_black_24dp};


    private static int SIGN_IN_REQUEST_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.tool_bar);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tab);
        fab = findViewById(R.id.fabButton);
        activity_main = findViewById(R.id.activity_main);


        /*
        getting refrence for firebase database
         */
        mdataBase = FirebaseDatabase.getInstance().getReference();



        // Choose authentication providers
//        List<AuthUI.IdpConfig> providers = Arrays.asList(
//                new AuthUI.IdpConfig.EmailBuilder().build(),
//                new AuthUI.IdpConfig.PhoneBuilder().build(),
//                new AuthUI.IdpConfig.GoogleBuilder().build());

        final List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build());

          /*

        Check if not sign-in then navigate Signing page

        */



        if (FirebaseAuth.getInstance().getCurrentUser() == null) {

            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    SIGN_IN_REQUEST_CODE);




//            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), SIGN_IN_REQUEST_CODE);


        } else {

            writeUserSignStatus(FirebaseAuth.getInstance().getCurrentUser().getUid(),FirebaseAuth.getInstance().getCurrentUser().getEmail(),"1");

            Snackbar.make(activity_main,"Welcome " + FirebaseAuth.getInstance().getCurrentUser().getEmail(), Snackbar.LENGTH_SHORT).show();
              //Load content
             //displayChatMessage();
        }


        /*
        setting toolbar in actionbar
        */
        setSupportActionBar(toolbar);

        /*
        setting title for toolbar
         */
        getSupportActionBar().setTitle(R.string.app_name);

        /*
        calling tabAdapter class and setting tabAdapter on it
         */

        tabAdapter = new TabAdapter(getSupportFragmentManager());
        viewPager.setAdapter(tabAdapter);

        /*
        setting tablayout with viewpager
         */
        tabLayout.setupWithViewPager(viewPager);


        /*
        change fab icon based on tab selected
         */
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                animateFab(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


         /*
            fab icon on click listener
         */

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = tabLayout.getSelectedTabPosition();

                switch(position) {
                    case 0:
                        Toast.makeText(MainActivity.this, "You are in chat fragment", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(MainActivity.this, "You are in groups fragment", Toast.LENGTH_SHORT).show();
                        //addgroup();
                        Intent intent =new Intent(MainActivity.this,Groupcreate.class);
                        startActivity(intent);
                        break;
                    case 2:
                        Toast.makeText(MainActivity.this, "You are in calls fragment", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });


    }

    /*private void addgroup() {




        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Add group");
        LayoutInflater inflater = LayoutInflater.from(this);
        View maddgroup = inflater.inflate(R.layout.add_group,null);

        final Button adduser = maddgroup.findViewById(R.id.adduser);
        final RecyclerView recyclerView = maddgroup.findViewById(R.id.recyclerView2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userModels = new ArrayList<>();
        groupuserRecyclerAdapter = new GroupuserRecyclerAdapter(this,userModels);
        recyclerView.setAdapter(groupuserRecyclerAdapter);

        db = FirebaseFirestore.getInstance();


        adduser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

            }
        });




        dialog.setView(maddgroup);

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        dialog.show();


    }*/


    /*
        defining menu item in toolbar

     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    /*
       action on menu item in toolbar

    */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_sign_out)
        {

            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            final DatabaseReference assignedCustomerRef = FirebaseDatabase.getInstance().getReference().child("userSignInStatus").child(userId);
            assignedCustomerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()) {
                        Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                        map.put("signInStatus","0");
                        assignedCustomerRef.updateChildren(map);

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

            AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {


                    Snackbar.make(activity_main,"You have been signed out.", Snackbar.LENGTH_SHORT).show();

                    finish();
                }
            });
        }else if (item.getItemId()==R.id.menu_setting){

//            Snackbar.make(activity_main,"You have clicked settings.", Snackbar.LENGTH_SHORT).show();

            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));

        }else if (item.getItemId() == R.id.menu_delete_account){
            AuthUI.getInstance()
                    .delete(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Snackbar.make(activity_main,FirebaseAuth.getInstance().getCurrentUser().getDisplayName() +" is deleted", Snackbar.LENGTH_SHORT).show();
                            finish();
                        }
                    });
        }
        return true;
    }

    /*
    function to check whether the user is signed in or not to save from multiple login with same account
     */

    private void writeUserSignStatus(String userId, String email, String status) {
        UserSignInCheck user = new UserSignInCheck(email, status);

        mdataBase.child("userSignInStatus").child(userId).setValue(user);

    }

    /*
    intent code
     */

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SIGN_IN_REQUEST_CODE) {

            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {

                final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                final DatabaseReference assignedCustomerRef = FirebaseDatabase.getInstance().getReference().child("userSignInStatus").child(userId).child("signInStatus");
                assignedCustomerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {

                            System.out.println("Status : "+dataSnapshot.getValue().toString());

                            if (dataSnapshot.getValue().toString().equals("1")){



                                Toast.makeText(MainActivity.this, "Already Exist", Toast.LENGTH_SHORT).show();

                            }else{
                                // Create and launch sign-in intent
                                startActivity(new Intent(getApplicationContext(), AddDetailActivity.class));

                                writeUserSignStatus(FirebaseAuth.getInstance().getCurrentUser().getUid(),FirebaseAuth.getInstance().getCurrentUser().getEmail(),"1");

                            }

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });


            } else {

            }

        }
    }


    /*
    animate fab icon
     */

    protected void animateFab(final int position) {
        fab.clearAnimation();
        // Scale down animation
        ScaleAnimation shrink =  new ScaleAnimation(1f, 0.2f, 1f, 0.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        shrink.setDuration(150);     // animation duration in milliseconds
        shrink.setInterpolator(new DecelerateInterpolator());
        shrink.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onAnimationEnd(Animation animation) {
                // Change FAB color and icon
                fab.setBackgroundTintList(getResources().getColorStateList(colorIntArray[position]));
                fab.setImageDrawable(getResources().getDrawable(iconIntArray[position], null));


                // Scale up animation
                ScaleAnimation expand =  new ScaleAnimation(0.2f, 1f, 0.2f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                expand.setDuration(100);     // animation duration in milliseconds
                expand.setInterpolator(new AccelerateInterpolator());
                fab.startAnimation(expand);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        fab.startAnimation(shrink);
    }

    public void alreadySignIn(final String uid){


//        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals())


    }


}
