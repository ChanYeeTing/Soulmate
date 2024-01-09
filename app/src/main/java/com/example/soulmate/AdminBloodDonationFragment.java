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
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminBloodDonationFragment extends Fragment {

    private ListView bloodDonationListView;
    private DatabaseReference bloodDonationReference;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_blood_donation, container, false);

        bloodDonationListView = view.findViewById(R.id.bloodDonationListView);
        bloodDonationReference = FirebaseDatabase.getInstance().getReference().child("Appointment").child("Blood Donation");

        // Add a ValueEventListener to fetch blood donation information
        bloodDonationReference.addValueEventListener(new ValueEventListener() {
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
        // Create a list to store blood donation information
        List<String> bloodDonationList = new ArrayList<>();

        for (DataSnapshot donationSnapshot : dataSnapshot.getChildren()) {
            String date = donationSnapshot.getKey();

            for (DataSnapshot timeSnapshot : donationSnapshot.getChildren()) {
                String time = timeSnapshot.getKey();
                Map<String, Object> donorData = (Map<String, Object>) timeSnapshot.getValue();

                if (donorData != null) {
                    String name = String.valueOf(donorData.get("name"));
                    String number = String.valueOf(donorData.get("number"));

                    // Build a string with blood donation information
                    String donationInfo = "\nDate: " + date + "\nTime: " + time + "\nName: " + name + "\nPhone Number: " + number + "\n";

                    bloodDonationList.add(donationInfo);
                }
            }
        }

        // Create an adapter to populate the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_list_item_1, bloodDonationList);

        bloodDonationListView.setAdapter(adapter);
    }

    @Override
    public void onViewCreated( @NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated ( view, savedInstanceState );

        Button backButton = getView ().findViewById ( R.id.backButton3);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_adminBloodDonationFragment_to_adminAppointmentFragment);
            }
        } );
    }
}
