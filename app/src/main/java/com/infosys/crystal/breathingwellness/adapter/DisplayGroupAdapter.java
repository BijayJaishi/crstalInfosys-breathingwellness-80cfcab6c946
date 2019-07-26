package com.infosys.crystal.breathingwellness.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.infosys.crystal.breathingwellness.activities.GroupChatRoom;
import com.infosys.crystal.breathingwellness.R;
import com.infosys.crystal.breathingwellness.model.Groupdatabasepush;

import java.util.List;

/**
 * Created by Bijay on 6/28/2019.
 */

public class DisplayGroupAdapter extends RecyclerView.Adapter<DisplayGroupAdapter.MyViewHolder>{

    Context context;
    List<Groupdatabasepush> getGroupsList;

    public DisplayGroupAdapter(Context context, List<Groupdatabasepush> getGroupsList) {
        this.context = context;
        this.getGroupsList = getGroupsList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grouplist_recycleview,viewGroup,false);
        return new  MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int position) {

        final Groupdatabasepush groupdatabasepush = getGroupsList.get(position);
        myViewHolder.gname.setText(groupdatabasepush.getGroup_name());


        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(context, "Clicked:"+position, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context,GroupChatRoom.class);
                intent.putExtra("title",groupdatabasepush.getGroup_name());
                context.startActivity(intent);


            }
        });





    }

    @Override
    public int getItemCount() {
        return getGroupsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView gname;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            gname = itemView.findViewById(R.id.gname);

        }


    }


}
