package com.infosys.crystal.breathingwellness.tab_fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.infosys.crystal.breathingwellness.R;
import com.infosys.crystal.breathingwellness.adapter.ChatRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 */
public class CallsFragment extends Fragment {


   View view;
   ChatRecyclerViewAdapter chatRecyclerViewAdapter;
   RecyclerView recyclerView;
   ArrayList<String> arrayList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_calls, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        arrayList = new ArrayList<>();
        String[] item ={"roshan","posakya","saroj","maharjan","milan","thapa","akash","khanal","manoj","dhungana","hemanga","gautam","roshan","posakya","saroj","maharjan","milan","thapa","akash","khanal","manoj","dhungana","hemanga","gautam","roshan","posakya","saroj","maharjan","milan","thapa","akash","khanal","manoj","dhungana","hemanga","gautam"};

        arrayList.addAll(Arrays.asList(item));


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        chatRecyclerViewAdapter = new ChatRecyclerViewAdapter(getActivity(),arrayList);
        recyclerView.setAdapter(chatRecyclerViewAdapter);

        return view;
    }

}
