package com.infosys.crystal.breathingwellness.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.infosys.crystal.breathingwellness.R;
import com.infosys.crystal.breathingwellness.model.GroupModel;
import com.infosys.crystal.breathingwellness.model.ProfileModelClass;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Intent.getIntent;

/**
 * Created by Bijay on 6/22/2019.
 */

public class Useradd_adapter extends RecyclerView.Adapter<Useradd_adapter.MyViewHolder>{


    Context context;
    List<GroupModel> groupModelList;

    public Useradd_adapter(Context context, List<GroupModel> groupModelList) {
        this.context = context;
        this.groupModelList = groupModelList;
    }

    @NonNull
    @Override
    public Useradd_adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycler_item3,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Useradd_adapter.MyViewHolder myViewHolder, int i) {

        final GroupModel profileModelClass = groupModelList.get(i);
        Picasso.get().load(profileModelClass.getImage()).placeholder(R.drawable.image_placeholder).error(R.drawable.image_placeholder).into(myViewHolder.pic);


    }

    @Override
    public int getItemCount() {
        return groupModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView pic;
        //FloatingActionButton cancel;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            pic = itemView.findViewById(R.id.profileimage);
            //cancel = itemView.findViewById(R.id.crossbtn);
        }
    }
}

