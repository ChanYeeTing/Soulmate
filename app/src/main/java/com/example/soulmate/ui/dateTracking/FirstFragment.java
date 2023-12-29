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
import com.example.soulmate.databinding.FragmentFirstBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

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
        TextView emptyView = binding.textviewFirst;
        Telemedicine = new ArrayList<>();

        Calendar calendar = Calendar.getInstance(
                TimeZone.getTimeZone("Asia/Kuala_Lumpur"));;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String todayDateFormat = dateFormat.format(calendar.getTime());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm a");
        String currentHour = timeFormat.format(calendar.get(Calendar.HOUR_OF_DAY));






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
                        // Current date
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                            LocalDate currentDate = LocalDate.now();
                            LocalDate dateFromString = LocalDate.parse(value[1], formatter);

                            // Date from the string "22-12-2023"
                            if (dateFromString.isAfter(currentDate)) {
                                DataModel dataModel = new DataModel(value[0], value[1],value[5],value[2],value[6]);
                                Telemedicine.add(dataModel);
                                Log.d("DataModel", "Added DataModel: " + dataModel.toString()); // Log for debugging
                            }
                            else if(value[1].equals(todayDateFormat))
                            {
                                try {
                                    if(timeFormat.parse(value[5]).after(timeFormat.parse(currentHour)))
                                    {
                                        DataModel dataModel = new DataModel(value[0], value[1],value[5],value[2],value[6]);
                                        Telemedicine.add(dataModel);
                                        Log.d("DataModel", "Added DataModel: " + dataModel.toString()); // Log for debugging
                                    }
                                } catch (ParseException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                    else
                        Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();

                }
                arrayAdapter.notifyDataSetChanged();
                if (updated.getAdapter() == null || updated.getAdapter().getCount() == 0) {
                    updated.setVisibility(View.GONE);
                    emptyView.setVisibility(View.VISIBLE);
                } else {
                    updated.setVisibility(View.VISIBLE);
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

        binding.buttonFirst.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick ( View view ) {
                NavHostFragment.findNavController ( FirstFragment.this )
                        .navigate ( R.id.action_FirstFragment_to_SecondFragment );
            }
        } );
    }


    @Override
    public void onDestroyView () {
        super.onDestroyView ();
        binding = null;
    }

}