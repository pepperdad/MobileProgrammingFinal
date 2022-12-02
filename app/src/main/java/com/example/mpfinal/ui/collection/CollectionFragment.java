package com.example.mpfinal.ui.collection;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mpfinal.Display_Fish;
import com.example.mpfinal.Fish;
import com.example.mpfinal.R;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CollectionFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private CollectionRecyclerViewAdapter collectionRecyclerViewAdapter;
    private ArrayList<Boolean> fish, display_fish;
    private FirebaseDatabase database;
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef,userRef,groupRef;
    private String TAG = "GroupFragmentTAG";
    private ProgressBar progressBar;
    String userId;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_collection, container, false);

        //firebase
        userId = mFirebaseAuth.getInstance().getCurrentUser().getUid();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("MadCampWeek4/UserAccount/"+userId);

        progressBar = view.findViewById(R.id.spin_kit_group);
        progressBar.setIndeterminateDrawable(new CubeGrid());

        recyclerView = view.findViewById(R.id.groupRecyclerView);
        recyclerView.setHasFixedSize(true);  //기존성능 강화
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fish = new ArrayList<>();  //객체 담을 arraylist
        display_fish = new ArrayList<>();

        adaptfish();
        return view;
    }

    private void adaptfish() {
        fish= Fish.getOwn();
        display_fish= Display_Fish.getOwn();
        collectionRecyclerViewAdapter = new CollectionRecyclerViewAdapter(getContext(), fish, display_fish);
        recyclerView.setAdapter(collectionRecyclerViewAdapter);

        database=FirebaseDatabase.getInstance(); //파이어베이스 데이터베이스 연동
        mDatabaseRef = database.getReference("MadCampWeek4/UserAccount");

    }

}