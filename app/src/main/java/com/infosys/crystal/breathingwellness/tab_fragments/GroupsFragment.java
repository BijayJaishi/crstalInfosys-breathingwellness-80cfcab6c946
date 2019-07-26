package com.infosys.crystal.breathingwellness.tab_fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.infosys.crystal.breathingwellness.R;
import com.infosys.crystal.breathingwellness.adapter.DisplayGroupAdapter;
import com.infosys.crystal.breathingwellness.model.Groupdatabasepush;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupsFragment extends Fragment{

    private View groupView;
    List<Groupdatabasepush>getGroups;
    //private ListView listView;
    //private ArrayList<String>arrayList = new ArrayList<>();
    //private ArrayAdapter<String>arrayAdapter;
    DisplayGroupAdapter displayGroupAdapter;
    RecyclerView recyclerView;
    String name;
    Groupdatabasepush groupdatabasepush;
    private FirebaseFirestore db;



    public GroupsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        groupView= inflater.inflate(R.layout.fragment_groups, container, false);



        db = FirebaseFirestore.getInstance();




        InitialieFields();

        DisplayGroup();

        return groupView;

    }



    private void InitialieFields() {

        //listView = groupView.findViewById(R.id.groups);
        //arrayAdapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,arrayList);
        //listView.setAdapter(arrayAdapter);


    }

    @Override
    public void onResume() {
        super.onResume();
        DisplayGroup();
    }

    private void DisplayGroup() {

      //name = groupdatabasepush.getGroup_name();



//        DocumentReference docRef = db.collection("Groups").document("GroupName");
//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document.exists()) {
//
//                        Toast.makeText(getActivity(), "Documents : "+document.getData(), Toast.LENGTH_SHORT).show();
//                    }
//
//                } else {
//                    Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        db.collection("Groups").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//
////                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
////                Toast.makeText(getActivity(), "Documents : "+list, Toast.LENGTH_SHORT).show();
////                List<String> list = new ArrayList<>();
////                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
////                    list.add(document.getId());
////                }
////                Toast.makeText(getActivity(), "Documents : "+list, Toast.LENGTH_SHORT).show();
////
////                for (DocumentSnapshot documentSnapshot : list){
////
////                    Toast.makeText(getActivity(), "Data : "+documentSnapshot.getData(), Toast.LENGTH_SHORT).show();
////                }
//
                if (! queryDocumentSnapshots.isEmpty()){
                    //Toast.makeText(getActivity(), "am here", Toast.LENGTH_SHORT).show();
                    getGroups = new ArrayList<>();
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                    for (DocumentSnapshot documentSnapshot : list){

                        Groupdatabasepush u  = documentSnapshot.toObject(Groupdatabasepush.class);
                        getGroups.add(u);

                    }
                    recyclerView = groupView.findViewById(R.id.groupRecyclerView);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager( new LinearLayoutManager(getActivity()));


                    displayGroupAdapter = new DisplayGroupAdapter(getActivity(),getGroups);
                    recyclerView.setAdapter(displayGroupAdapter);
                    displayGroupAdapter.notifyDataSetChanged();

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
