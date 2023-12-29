package com.example.soulmate.ui.dateTracking;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.soulmate.R;
import com.example.soulmate.databinding.FragmentFirstBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;

    String uid;
    private ArrayList<DataModel> Telemedicine;
    private CustomAdapter arrayAdapter;


    @Override
    public View onCreateView (
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentFirstBinding.inflate ( inflater, container, false );
        View view = binding.getRoot();

        ListView updated = binding.activityFirst;

        Telemedicine = new ArrayList<>();



        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser(); // Check for null here
        if (currentUser != null) {
            uid = currentUser.getUid();
        }
        arrayAdapter = new CustomAdapter(getActivity(),Telemedicine);
        updated.setAdapter(arrayAdapter);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Activity");

        //Telemedicine
        databaseReference.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Telemedicine.clear();

                for(DataSnapshot snapshot1: snapshot.getChildren())
                {
                    String childKey = snapshot1.getKey();

                    DataSnapshot snapshotDate = snapshot.child(childKey);

                    if (snapshotDate.exists()) {
                        int i = 0;
                        String[] value = new String[7];
                        for (DataSnapshot snapshotTime : snapshotDate.getChildren()) {
                            value[i] = String.valueOf(snapshotTime.getValue());
                            i++;

                        }
//                        Toast.makeText(getActivity(), "testing", Toast.LENGTH_SHORT).show();
                        DataModel dataModel = new DataModel(value[0], value[1],value[5],value[2],value[6]);
                        Telemedicine.add(dataModel);
                        Log.d("DataModel", "Added DataModel: " + dataModel.toString()); // Log for debugging

                    }
                    else
                        Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();

                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return view;

    }

    public void onViewCreated ( @NonNull View view, Bundle savedInstanceState ) {
        super.onViewCreated ( view, savedInstanceState );

        binding.buttonFirst.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick ( View view ) {
                NavHostFragment.findNavController ( FirstFragment.this )
                        .navigate ( R.id.action_FirstFragment_to_SecondFragment );
            }
        } );
    }
    @Override
    public void onActivityCreated ( @Nullable Bundle savedInstanceState ) {
        super.onActivityCreated(savedInstanceState);

//        updated = getView().findViewById(R.id.activity);
//
//        Telemedicine = new ArrayList<>();
//        arrayAdapter = new CustomAdapter(getActivity(), Telemedicine);
//        updated.setAdapter(arrayAdapter);
//
//
//        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = firebaseAuth.getCurrentUser(); // Check for null here
//        if (currentUser != null) {
//            uid = currentUser.getUid();
//        }
//            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Activity").child(uid);
//            databaseReference.child("Telemedicine").addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    Telemedicine.clear();
//
//                    for(DataSnapshot snapshot1: snapshot.getChildren())
//                    {
//                        String data = String.valueOf(snapshot1.getValue(String.class));
//                        DataSnapshot snapshotDate = snapshot1.child(data);
//
//                        for(DataSnapshot snapshotTime: snapshotDate.getChildren())
//                        {
//                            DataModel dataModel = snapshot1.getValue(DataModel.class);
//                            Telemedicine.add(dataModel);
//                        }
//                        arrayAdapter = new CustomAdapter(getActivity(),Telemedicine);
//                        Telemedicine.setAdapter(arrayAdapter);
//
//                    }
//
//
//                    arrayAdapter.notifyDataSetChanged();
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });

    }

    @Override
    public void onDestroyView () {
        super.onDestroyView ();
        binding = null;
    }

}