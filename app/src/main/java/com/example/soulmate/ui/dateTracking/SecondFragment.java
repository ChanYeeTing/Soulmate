package com.example.soulmate.ui.dateTracking;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.soulmate.R;
import com.example.soulmate.databinding.FragmentSecondBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    private ArrayList<DataModel> HistoryList;
    private CustomAdapter arrayAdapter;
    String uid;

    @Override
    public View onCreateView (
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate ( inflater, container, false );
        View view = binding.getRoot();


        ListView history= binding.activitySecond;
        TextView emptyView = binding.textviewSecond;
        HistoryList = new ArrayList<>();



        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser(); // Check for null here
        if (currentUser != null) {
            uid = currentUser.getUid();
        }
        arrayAdapter = new CustomAdapter(getActivity(), HistoryList);
        history.setAdapter(arrayAdapter);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Activity");

        //Telemedicine
        databaseReference.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HistoryList.clear();

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
                        HistoryList.add(dataModel);
                        Log.d("DataModel", "Added DataModel: " + dataModel.toString()); // Log for debugging

                    }
                    else
                        Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();

                }
                arrayAdapter.notifyDataSetChanged();
                if (history.getAdapter() == null || history.getAdapter().getCount() == 0) {
                    history.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                } else {
                    history.setVisibility(View.VISIBLE);
                    emptyView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });









        return view;

    }

    public void onViewCreated ( @NonNull View view, Bundle savedInstanceState ) {
        super.onViewCreated ( view, savedInstanceState );

        binding.buttonSecond.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick ( View view ) {
                NavHostFragment.findNavController ( SecondFragment.this )
                        .navigate ( R.id.action_SecondFragment_to_FirstFragment );
            }
        } );
    }

    @Override
    public void onDestroyView () {
        super.onDestroyView ();
        binding = null;
    }

}