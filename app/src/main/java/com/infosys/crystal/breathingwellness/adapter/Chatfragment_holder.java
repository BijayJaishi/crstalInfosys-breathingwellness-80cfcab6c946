package com.infosys.crystal.breathingwellness.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.infosys.crystal.breathingwellness.R;
import com.infosys.crystal.breathingwellness.model.ChatMessage;
import com.infosys.crystal.breathingwellness.model.ProfileModelClass;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Bijay on 7/5/2019.
 */

public class Chatfragment_holder extends RecyclerView.Adapter<Chatfragment_holder.MyViewHolder> {

    Context context;
    //List<ChatMessage>chatMessages;
    List<ProfileModelClass>profileModelClasses;


    public Chatfragment_holder(Context context, /*List<ChatMessage> chatMessages,*/ List<ProfileModelClass> profileModelClasses) {
        this.context = context;
        //this.chatMessages = chatMessages;
        this.profileModelClasses = profileModelClasses;
    }

    @NonNull
    @Override
    public Chatfragment_holder.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_itemforchat,viewGroup,false);
        return new  MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Chatfragment_holder.MyViewHolder myViewHolder, int i) {

        final ProfileModelClass profileModelClass = profileModelClasses.get(i);
        //final ChatMessage chatMessage = chatMessages.get(i);

        Picasso.get().load(profileModelClass.getImage()).placeholder(R.drawable.image_placeholder).error(R.drawable.image_placeholder).into(myViewHolder.imageView);
        myViewHolder.usernamee.setText(profileModelClass.getUser_name());
        //myViewHolder.messages.setText(chatMessage.getMessageType());


    }

    @Override
    public int getItemCount() {
        return profileModelClasses.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView usernamee,messages;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.pimage);
            usernamee = itemView.findViewById(R.id.usernamee);
            messages = itemView.findViewById(R.id.messages);
        }
    }
}
