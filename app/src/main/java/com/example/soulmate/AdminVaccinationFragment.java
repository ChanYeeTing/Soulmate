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

public class AdminVaccinationFragment extends Fragment {

    private ListView vaccinationListView;
    private DatabaseReference vaccinationReference;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_vaccination, container, false);

        vaccinationListView = view.findViewById(R.id.vaccinationListView);
        vaccinationReference = FirebaseDatabase.getInstance().getReference().child("Appointment").child("Vaccination");

        // Add a ValueEventListener to fetch vaccination information
        vaccinationReference.addValueEventListener(new ValueEventListener() {
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
        // Create a list to store vaccination information
        List<String> vaccinationList = new ArrayList<>();

        for (DataSnapshot vaccinationSnapshot : dataSnapshot.getChildren()) {
            String date = vaccinationSnapshot.getKey();

            for (DataSnapshot timeSnapshot : vaccinationSnapshot.getChildren()) {
                String time = timeSnapshot.getKey();
                Map<String, Object> vaccinationData = (Map<String, Object>) timeSnapshot.getValue();

                if (vaccinationData != null) {
                    String name = String.valueOf(vaccinationData.get("name"));
                    String number = String.valueOf(vaccinationData.get("number"));

                    // Build a string with vaccination information
                    String vaccinationInfo = "\nAppointment Number: " + number + "\nDate: " + date + "\nTime: " + time + "\nName: " + name + "\n";

                    vaccinationList.add(vaccinationInfo);
                }
            }
        }

        // Create an adapter to populate the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_list_item_1, vaccinationList);

        vaccinationListView.setAdapter(adapter);
    }

    @Override
    public void onViewCreated( @NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated ( view, savedInstanceState );

        Button backButton = getView ().findViewById ( R.id.backButton7);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_adminVaccinationFragment_to_adminAppointmentFragment);
            }
        } );
    }
}
