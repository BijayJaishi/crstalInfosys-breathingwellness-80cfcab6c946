package com.infosys.crystal.breathingwellness.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.github.library.bubbleview.BubbleTextView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.infosys.crystal.breathingwellness.R;
import com.infosys.crystal.breathingwellness.model.ChatMessage;
import com.infosys.crystal.breathingwellness.model.ProfileModelClass;
import com.infosys.crystal.breathingwellness.progress_bar.Play_progressbar;
import com.scottyab.aescrypt.AESCrypt;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.UUID;

import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class Chating_activity extends AppCompatActivity {

    private static int SIGN_IN_REQUEST_CODE = 1;
    //    private FirebaseRecyclerAdapter adapter;
    //private FirebaseListAdapter<ChatMessage> adapter;
    RelativeLayout activity_main;
    Play_progressbar progress;
    private FirestoreRecyclerAdapter<ChatMessage, ChatHolderAdapter> adapter;

    FirebaseStorage storage;
    StorageReference storageReference;

    //Add Emojicon
    EmojiconEditText emojiconEditText;
    ImageView emojiButton, submitButton, load_image;
    EmojIconActions emojIconActions;
    private Uri filepath, uri;

    private int PICK_IMAGE_REQUEST = 71;
    String title;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chating_activity);

        ProfileModelClass profileModelClass = new ProfileModelClass();
        title = profileModelClass.getUser_name();

        activity_main = (RelativeLayout) findViewById(R.id.activity_main);

        progress = new Play_progressbar(Chating_activity.this);

        //Firebase Storage Initilization

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        //Add Emoji
        emojiButton = findViewById(R.id.emoji_button);
        submitButton = findViewById(R.id.submit_button);
        emojiconEditText = findViewById(R.id.emojicon_edit_text);
        load_image = findViewById(R.id.load_image);
        emojIconActions = new EmojIconActions(getApplicationContext(), activity_main, emojiButton, emojiconEditText);
        emojIconActions.ShowEmojicon();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                upload();



            }
        });

        displayChatMessage();

        load_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Chating_activity.this, "Work need to be done!!!!!", Toast.LENGTH_SHORT).show();
                chooseImage();
            }
        });
    }

    private void chooseImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select your Sketch: "), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == SIGN_IN_REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//                Snackbar.make(activity_main, "Successfully signed in.Welcome!", Snackbar.LENGTH_SHORT).show();
//                displayChatMessage();
//            } else {
//                Snackbar.make(activity_main, "We couldn't sign you in.Please try again later", Snackbar.LENGTH_SHORT).show();
//                finish();
//            }
//        }

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filepath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filepath);
//                imagepreview.setImageBitmap(bitmap);
//                uploadbtn.setEnabled(true);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void upload() {

        if (filepath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child(new StringBuilder("chat_images/").append(UUID.randomUUID().toString())
                    .toString());


            ref.putFile(filepath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    progressDialog.dismiss();

                                    try {
                                        String password = String.valueOf(R.string.app_name);
                                        String encryptedMsg = AESCrypt.encrypt(password, emojiconEditText.getText().toString());
                                        if (emojiconEditText.getText().toString().equals("")) {

                                            FirebaseFirestore.getInstance().collection("Messages").add(new ChatMessage("image", "0",
                                                    FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), uri.toString(),title));


                                            FirebaseDatabase.getInstance().getReference().push().setValue(new ChatMessage("image", "0",
                                                    FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), uri.toString(),title));

                                        }

                                        emojiconEditText.setText("");
                                        filepath = null;
                                        emojiconEditText.requestFocus();
                                        adapter.notifyDataSetChanged();
                                    } catch (GeneralSecurityException e) {
                                        //handle error
                                    }
                                }
                            });
                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Chating_activity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0) * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("Upload Complete..." + (int) progress + "%");
                        }
                    });
        } else {
            try {
                String password = String.valueOf(R.string.app_name);
                if (emojiconEditText.getText().toString().equals("")){
                    Toast.makeText(this, "Enter message", Toast.LENGTH_SHORT).show();
                }else{


                    String encryptedMsg = AESCrypt.encrypt(password, emojiconEditText.getText().toString());


                    FirebaseFirestore.getInstance().collection("Messages").add(new ChatMessage("text", encryptedMsg,
                            FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), "0",title));


                    FirebaseDatabase.getInstance().getReference().push().setValue(new ChatMessage("text", encryptedMsg,
                            FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), "0",title));


                    emojiconEditText.setText("");
                    emojiconEditText.requestFocus();



                    adapter.notifyDataSetChanged();
                }

            } catch (GeneralSecurityException e) {
                //handle error
            }
        }
    }


    private void displayChatMessage() {
        //progress.showProgress();

        final RecyclerView listOfMessage = findViewById(R.id.list_of_message);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        listOfMessage.setLayoutManager(linearLayoutManager);

        //listOfMessage.setLayoutManager(new LinearLayoutManager(this));


        /// using query method ///

        //final Query query = FirebaseDatabase.getInstance().getReference();



        //final Query query = collectionReference.whereEqualTo("userDetail",FirebaseAuth.getInstance().getCurrentUser().getUid());

        final Query query = FirebaseFirestore.getInstance().collection("Messages")
                .orderBy("messageTime", Query.Direction.DESCENDING);

        System.out.println("Query : "+query);

//        FirebaseApp.initializeApp(this);

//        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot snapshot,
//                                @Nullable FirebaseFirestoreException e) {
//                if (e != null) {
//                    // Handle error
//                    //...
//                    return;
//                }
//
//                // Convert query snapshot to a list of chats
//                List<ChatMessage> chats = snapshot.toObjects(ChatMessage.class);
//
//                // Update UI
//                // ...
//            }
//        });


//        db.collection("Texts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//
//              List<ChatMessage> mMissionsList ;
//
//                if (task.isSuccessful()) {
//
//                    for (QueryDocumentSnapshot queryDocumentSnapshots : task.getResult()) {
//
//
//
//                        mMissionsList = new ArrayList<>();
//                        ChatMessage chatMessage = queryDocumentSnapshots.toObject(ChatMessage.class);
//                        mMissionsList.add(chatMessage);
//
//                    }
//                    progress.hideProgress();
//
//
//                } else {
//                    Toast.makeText(Chating_activity.this, "Some error sorry !!", Toast.LENGTH_SHORT).show();
//                }
//
//
//            }
//        });

        FirestoreRecyclerOptions<ChatMessage> options = new FirestoreRecyclerOptions.Builder<ChatMessage>()
                .setQuery(query, ChatMessage.class)
                .build();

        System.out.println("Options : "+options);


//        adapter = new FirestoreRecyclerAdapter<ChatMessage, ChatHolderAdapter>(options) {
//            @Override
//            public void onBindViewHolder( @NonNull ChatMessage model,@NonNull ChatHolderAdapter v, int position) {
//                // Bind the Chat object to the ChatHolder
//
//
//
//
//                listOfMessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
//
//
//                        AlertDialog alertDialog = new AlertDialog.Builder(Chating_activity.this, AlertDialog.THEME_HOLO_LIGHT).create();
//
//                        alertDialog.setTitle(R.string.app_name);
//                        alertDialog.setMessage(Html.fromHtml("Would you like to delete?"));
//
//                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "No",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.dismiss();
//                                    }
//                                });
//
//                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        adapter.getRef(i).removeValue();
//                                        adapter.notifyDataSetChanged();
//                                        // Snackbar.make(activity_main,"We couldn't sign you in.Please try again later", Snackbar.LENGTH_SHORT).show();
//                                        Snackbar.make(activity_main, " Deleted", Snackbar.LENGTH_SHORT).show();
//                                        dialog.dismiss();
//                                    }
//                                });
//
//                        alertDialog.show();
//                        final Button neutralButton = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
//                        final Button positveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
//                        neutralButton.setTextColor(getResources().getColor(R.color.colorAccent));
//                        positveButton.setTextColor(getResources().getColor(R.color.colorAccent));
//
//
//                    }
//                });
//
//            }
//
//            @NonNull
//            @Override
//            public ChatHolderAdapter onCreateViewHolder(ViewGroup group, int i) {
//                // Create a new instance of the ViewHolder, in this case we are using a custom
//                // layout called R.layout.message for each item
//                View view = LayoutInflater.from(group.getContext())
//                        .inflate(R.layout.chatlist_item, group, false);
//
//                return new ChatHolderAdapter(view);
//            }
//        };

        adapter = new FirestoreRecyclerAdapter<ChatMessage, ChatHolderAdapter>(options) {
            @Override
            public void onBindViewHolder(ChatHolderAdapter holder, int position, ChatMessage model) {

                //Get references to the views of list_item.xml
                //progress.hideProgress();

                holder.messageUser.setText(model.getMessageUser());
                holder.messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:aa)", model.getMessageTime()));
                holder.message_user_other.setText(model.getMessageUser());

                String author = model.getMessageUser();
                System.out.println("Current User : " + FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                System.out.println("User : " + author);

                System.out.println("Image : " + model.getImage());


                String messageAfterDecrypt = "";

                if (author != null && author.equals(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())) {


                    try {

                        if (model.getMessageType().equals("text")) {
                            String password = String.valueOf(R.string.app_name);
                            messageAfterDecrypt = AESCrypt.decrypt(password, model.getMessageText());
                            holder.message_text_other.setText(messageAfterDecrypt);
                            holder.message_text_other.setVisibility(View.VISIBLE);

                            holder.message_text_other_image.setVisibility(View.GONE);

                            holder.messageText_image.setVisibility(View.GONE);
                            holder.left_progress_bar.setVisibility(View.GONE);
                            holder.right_progress_bar.setVisibility(View.GONE);
                        } else {
                            if (model.getImage() != null) holder.message_text_other_image.setVisibility(View.VISIBLE);

                            RequestOptions requestOptions = new RequestOptions();
                            requestOptions.placeholder(R.drawable.ic_image_black_24dp);
                            requestOptions.error(R.drawable.ic_image_black_24dp);
                            requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
                            Glide.with(getApplicationContext())
                                    .load(model.getImage())
                                    .apply(requestOptions)
                                    .into(holder.message_text_other_image);
                            holder.message_text_other.setVisibility(View.GONE);
                            holder.messageText_image.setVisibility(View.GONE);

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        //handle error - could be due to incorrect password or tampered encryptedMsg
                    }
                    holder.messageText.setVisibility(View.GONE);
                    holder.messageUser.setVisibility(View.GONE);
                    holder.message_user_other.setVisibility(View.VISIBLE);

                } else {

                    try{
                        holder.message_text_other.setVisibility(View.GONE);
                        holder.message_user_other.setVisibility(View.GONE);
                        holder.messageUser.setVisibility(View.VISIBLE);

                        if (model.getMessageType().equals("text")) {
                            String password = String.valueOf(R.string.app_name);
                            try {
                                messageAfterDecrypt = AESCrypt.decrypt(password, model.getMessageText());
                            } catch (GeneralSecurityException e) {
                                e.printStackTrace();
                            }
                            System.out.println("Message : "+messageAfterDecrypt);
                            holder.messageText.setVisibility(View.VISIBLE);
                            holder.messageText.setText(messageAfterDecrypt);


                            holder.message_text_other_image.setVisibility(View.GONE);

                            holder.messageText_image.setVisibility(View.GONE);
                            holder.left_progress_bar.setVisibility(View.GONE);
                            holder.right_progress_bar.setVisibility(View.GONE);

                        } else {
                            if (model.getImage() != null) holder.messageText_image.setVisibility(View.VISIBLE);

                            RequestOptions requestOptions = new RequestOptions();
                            requestOptions.placeholder(R.drawable.ic_image_black_24dp);
                            requestOptions.error(R.drawable.ic_image_black_24dp);
                            requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
                            Glide.with(getApplicationContext()).load(model.getImage()).apply(requestOptions).into(holder.messageText_image);
                            holder.messageText.setVisibility(View.GONE);
                            holder.message_text_other_image.setVisibility(View.GONE);

                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }




                listOfMessage.smoothScrollToPosition(listOfMessage.getAdapter().getItemCount()-2100000000);

                //listOfMessage.smoothScrollToPosition(position);
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);



            }

            @Override
            public ChatHolderAdapter onCreateViewHolder(ViewGroup group, int i) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.chatlist_item, group, false);

                return new ChatHolderAdapter(view);
            }
        };
        adapter.notifyDataSetChanged();
       listOfMessage.setAdapter(adapter);

        //final int i = listOfMessage.getAdapter().getItemCount() - 1;



        //////Tnis is For Firebasedatabase!!!!//////


//        FirebaseApp.initializeApp(this);
//
//        FirebaseListOptions<ChatMessage> options = new FirebaseListOptions.Builder<ChatMessage>()
//                .setQuery(query, ChatMessage.class)
//                .setLayout(R.layout.chatlist_item)
//                .build();


//        adapter = new FirebaseListAdapter<ChatMessage>(options) {
//            @Override
//            protected void populateView(View v, final ChatMessage model, final int position) {
//                progress.hideProgress();
//
//                //Get references to the views of list_item.xml
//                TextView messageText, messageUser, messageTime, message_text_other, message_user_other;
//                final ProgressBar right_progress_bar, left_progress_bar;
//                ImageView messageText_image, message_text_other_image;
//
//                left_progress_bar = v.findViewById(R.id.left_progress_bar);
//                right_progress_bar = v.findViewById(R.id.right_progress_bar);
//                messageText = (BubbleTextView) v.findViewById(R.id.message_text);
//                message_text_other = (BubbleTextView) v.findViewById(R.id.message_text_other);
//                messageUser = (TextView) v.findViewById(R.id.message_user);
//                messageTime = (TextView) v.findViewById(R.id.message_time);
//                message_user_other = v.findViewById(R.id.message_user_other);
//
//                messageText_image = v.findViewById(R.id.message_text_image);
//                message_text_other_image = v.findViewById(R.id.message_text_other_image);
//
//                messageUser.setText(model.getMessageUser());
//                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:aa)", model.getMessageTime()));
//                message_user_other.setText(model.getMessageUser());
//
//                String author = model.getMessageUser();
//                System.out.println("Current User : " + FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
//                System.out.println("User : " + author);
//
//                System.out.println("Image : " + model.getImage());
//
//
//                String messageAfterDecrypt = "";
//
//                if (author != null && author.equals(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())) {
//
//
//                    try {
//
//                        if (model.getMessageType().equals("text")) {
//                            String password = String.valueOf(R.string.app_name);
//                            messageAfterDecrypt = AESCrypt.decrypt(password, model.getMessageText());
//                            message_text_other.setText(messageAfterDecrypt);
//                            message_text_other.setVisibility(View.VISIBLE);
//
//                            message_text_other_image.setVisibility(View.GONE);
//
//                            messageText_image.setVisibility(View.GONE);
//                            left_progress_bar.setVisibility(View.GONE);
//                            right_progress_bar.setVisibility(View.GONE);
//                        } else {
//                            if (model.getImage() != null) message_text_other_image.setVisibility(View.VISIBLE);
//
//                            RequestOptions requestOptions = new RequestOptions();
//                            requestOptions.placeholder(R.drawable.ic_image_black_24dp);
//                            requestOptions.error(R.drawable.ic_image_black_24dp);
//                            requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
//                            Glide.with(getApplicationContext())
//                                    .load(model.getImage())
//                                    .apply(requestOptions)
//                                    .into(message_text_other_image);
//                            message_text_other.setVisibility(View.GONE);
//                            messageText_image.setVisibility(View.GONE);
//
//
//                        }
//
//
//
//
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        //handle error - could be due to incorrect password or tampered encryptedMsg
//                    }
//                    messageText.setVisibility(View.GONE);
//                    messageUser.setVisibility(View.GONE);
//                    message_user_other.setVisibility(View.VISIBLE);
//
//                } else {
//
//                    try{
//                        message_text_other.setVisibility(View.GONE);
//                        message_user_other.setVisibility(View.GONE);
//                        messageUser.setVisibility(View.VISIBLE);
//
//                        if (model.getMessageType().equals("text")) {
//                            String password = String.valueOf(R.string.app_name);
//                            try {
//                                messageAfterDecrypt = AESCrypt.decrypt(password, model.getMessageText());
//                            } catch (GeneralSecurityException e) {
//                                e.printStackTrace();
//                            }
//                            System.out.println("Message : "+messageAfterDecrypt);
//                            messageText.setVisibility(View.VISIBLE);
//                            messageText.setText(messageAfterDecrypt);
//
//                            message_text_other_image.setVisibility(View.GONE);
//
//                            messageText_image.setVisibility(View.GONE);
//                            left_progress_bar.setVisibility(View.GONE);
//                            right_progress_bar.setVisibility(View.GONE);
//                        } else {
//                            if (model.getImage() != null) messageText_image.setVisibility(View.VISIBLE);
//
//                            RequestOptions requestOptions = new RequestOptions();
//                            requestOptions.placeholder(R.drawable.ic_image_black_24dp);
//                            requestOptions.error(R.drawable.ic_image_black_24dp);
//                            requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
//                            Glide.with(getApplicationContext()).load(model.getImage()).apply(requestOptions).into(messageText_image);
//                            messageText.setVisibility(View.GONE);
//                            message_text_other_image.setVisibility(View.GONE);
//
//                        }
//                    }catch (Exception e){
//                        e.printStackTrace();
//                    }
//
//
//
//                }
//
//
//                listOfMessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
//
//
//                        AlertDialog alertDialog = new AlertDialog.Builder(Chating_activity.this, AlertDialog.THEME_HOLO_LIGHT).create();
//
//                        alertDialog.setTitle(R.string.app_name);
//                        alertDialog.setMessage(Html.fromHtml("Would you like to delete?"));
//
//                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "No",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.dismiss();
//                                    }
//                                });
//
//                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        adapter.getRef(i).removeValue();
//                                        adapter.notifyDataSetChanged();
//                                        // Snackbar.make(activity_main,"We couldn't sign you in.Please try again later", Snackbar.LENGTH_SHORT).show();
//                                        Snackbar.make(activity_main, " Deleted", Snackbar.LENGTH_SHORT).show();
//                                        dialog.dismiss();
//                                    }
//                                });
//
//                        alertDialog.show();
//                        final Button neutralButton = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
//                        final Button positveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
//                        neutralButton.setTextColor(getResources().getColor(R.color.colorAccent));
//                        positveButton.setTextColor(getResources().getColor(R.color.colorAccent));
//
//
//                    }
//                });
//
//            }
//        };
//
//        if (adapter.getCount() == 0) {
//            progress.hideProgress();
//            //Snackbar.make(activity_main, " Chat is empty...", Snackbar.LENGTH_SHORT).show();
//
//        }
//        listOfMessage.setAdapter(adapter);
//
//
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            adapter.startListening();
        }catch (Exception e){

        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        try{
            adapter.stopListening();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public class ChatHolderAdapter  extends RecyclerView.ViewHolder {
        //    TextView messageText,messageUser,messageTime;
        TextView messageText, messageUser, messageTime,message_text_other,message_user_other;
        ProgressBar right_progress_bar,left_progress_bar;
        ImageView messageText_image,message_text_other_image;

        public ChatHolderAdapter(@NonNull View itemView) {
            super(itemView);

            left_progress_bar = itemView.findViewById(R.id.left_progress_bar);
            right_progress_bar = itemView.findViewById(R.id.right_progress_bar);
            messageText = (BubbleTextView) itemView.findViewById(R.id.message_text);
            message_text_other = (BubbleTextView) itemView.findViewById(R.id.message_text_other);
            messageUser = (TextView) itemView.findViewById(R.id.message_user);
            messageTime = (TextView) itemView.findViewById(R.id.message_time);
            message_user_other = itemView.findViewById(R.id.message_user_other);

            messageText_image = itemView.findViewById(R.id.message_text_image);
            message_text_other_image = itemView.findViewById(R.id.message_text_other_image);
        }
    }

}
