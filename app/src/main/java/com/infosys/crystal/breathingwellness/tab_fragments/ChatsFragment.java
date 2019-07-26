package com.infosys.crystal.breathingwellness.tab_fragments;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.github.library.bubbleview.BubbleTextView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.infosys.crystal.breathingwellness.R;
import com.infosys.crystal.breathingwellness.adapter.Chatfragment_holder;
import com.infosys.crystal.breathingwellness.adapter.DisplayGroupAdapter;
import com.infosys.crystal.breathingwellness.model.ChatMessage;
import com.infosys.crystal.breathingwellness.model.Groupdatabasepush;
import com.infosys.crystal.breathingwellness.model.ProfileModelClass;
import com.infosys.crystal.breathingwellness.progress_bar.Play_progressbar;
import com.scottyab.aescrypt.AESCrypt;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment {

    private View chatView;
    RecyclerView recyclerView;
    List<ProfileModelClass>profileModelClasses;
    List<ChatMessage>chatMessages;
    FirebaseFirestore db,db2;
    Chatfragment_holder chatfragment_holder;






    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        chatView = inflater.inflate(R.layout.fragment_chats, container, false);

        //recyclerView = chatView.findViewById(R.id.recycleViewChat);
        db = FirebaseFirestore.getInstance();
        db2 = FirebaseFirestore.getInstance();

        displayChat();


        return chatView;
    }

    private void displayChat() {

//        db.collection("Messages").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//
//                if (! queryDocumentSnapshots.isEmpty()){
//                    //Toast.makeText(getActivity(), "am here", Toast.LENGTH_SHORT).show();
//                    chatMessages = new ArrayList<>();
//                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
//
//                    for (DocumentSnapshot documentSnapshot : list){
//
//                        ChatMessage u  = documentSnapshot.toObject(ChatMessage.class);
//                        chatMessages.add(u);
//
//                    }
//                    recyclerView = chatView.findViewById(R.id.recycleViewChat);
//                    recyclerView.setHasFixedSize(true);
//                    recyclerView.setLayoutManager( new LinearLayoutManager(getActivity()));
//                    chatfragment_holder = new Chatfragment_holder(getActivity(),chatMessages,profileModelClasses);
//                    recyclerView.setAdapter(chatfragment_holder);
//                    chatfragment_holder.notifyDataSetChanged();
//
//                }
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
//
//            }
//        });


        // for user name


        db2.collection("userDetail").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if (! queryDocumentSnapshots.isEmpty()){
                    //Toast.makeText(getActivity(), "am here", Toast.LENGTH_SHORT).show();
                    profileModelClasses = new ArrayList<>();
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                    for (DocumentSnapshot documentSnapshot : list){

                        ProfileModelClass u  = documentSnapshot.toObject(ProfileModelClass.class);
                        profileModelClasses.add(u);

                    }
                    recyclerView = chatView.findViewById(R.id.recycleViewChat);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager( new LinearLayoutManager(getActivity()));
                    chatfragment_holder = new Chatfragment_holder(getActivity() ,/*chatMessages,*/ profileModelClasses);
                    recyclerView.setAdapter(chatfragment_holder);
                    chatfragment_holder.notifyDataSetChanged();

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }


}
