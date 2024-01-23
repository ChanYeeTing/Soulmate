package com.example.soulmate;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminMedicalHistoryFragment extends Fragment {

    private ListView medicalHistoryListView;
    private DatabaseReference medicalHistoryReference;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_medical_history, container, false);

        medicalHistoryListView = view.findViewById(R.id.medicalHistoryListView);
        medicalHistoryReference = FirebaseDatabase.getInstance().getReference().child("Users");

        // Add a ValueEventListener to fetch medical history information
        medicalHistoryReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                updateListView(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });

        return view;
    }

    private void updateListView(DataSnapshot dataSnapshot) {
        // Create a list to store medical history information
        List<String> medicalHistoryList = new ArrayList<>();

        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
            String uid = userSnapshot.getKey();

            // Check if the "Medical History" node exists for the current user
            if (userSnapshot.hasChild("Medical History")) {
                DataSnapshot medicalHistorySnapshot = userSnapshot.child("Medical History");

                // Check if the "medicalHistory" node exists
                if (medicalHistorySnapshot.hasChild("medicalHistory")) {
                    DataSnapshot medicalDataSnapshot = medicalHistorySnapshot.child("medicalHistory");

                    // Retrieve "Name" directly from medicalHistorySnapshot
                    String name = String.valueOf(medicalHistorySnapshot.child("Name").getValue());

                    // Retrieve medical history data
                    String allergic = String.valueOf(medicalDataSnapshot.child("allergic").getValue());
                    String duration = String.valueOf(medicalDataSnapshot.child("duration").getValue());
                    String medicationName = String.valueOf(medicalDataSnapshot.child("medicationName").getValue());
                    String dosage = String.valueOf(medicalDataSnapshot.child("dosage").getValue());
                    String instruction = String.valueOf(medicalDataSnapshot.child("instruction").getValue());
                    String allergicDetails = String.valueOf(medicalDataSnapshot.child("allergicDetails").getValue());
                    String surgeryDetails = String.valueOf(medicalDataSnapshot.child("surgeryDetails").getValue());
                    String frequency = String.valueOf(medicalDataSnapshot.child("frequency").getValue());
                    String surgery = String.valueOf(medicalDataSnapshot.child("surgery").getValue());

                    // Build a string with formatted medical history information
                    String medicalHistoryInfo = "\nUser ID:\n" + uid +
                            "\nName: " + name +
                            "\nMedication Name: \n" + medicationName +
                            "\nDosage: \n" + dosage +
                            "\nFrequency: \n" + frequency +
                            "\nDuration: \n" + duration +
                            "\nInstruction: \n" + instruction +
                            "\nAllergic: " + allergic +
                            "\nAllergic Details: " + allergicDetails +
                            "\nSurgery: " + surgery +
                            "\nSurgery Details: " + surgeryDetails + "\n";

                    medicalHistoryList.add(medicalHistoryInfo);
                }
            }
        }

        // Create an adapter to populate the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_list_item_1, medicalHistoryList);

        medicalHistoryListView.setAdapter(adapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button backButton = view.findViewById(R.id.backButton4);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_adminMedicalHistoryFragment_to_adminMainPageFragment);
            }
        });
    }
}
