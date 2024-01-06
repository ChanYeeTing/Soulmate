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

public class AdminClinicHospitalFragment extends Fragment {

    private ListView clinicHospitalListView;
    private DatabaseReference clinicHospitalReference;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_clinic_hospital, container, false);

        clinicHospitalListView = view.findViewById(R.id.clinicHospitalListView);
        clinicHospitalReference = FirebaseDatabase.getInstance().getReference().child("Appointment").child("ClinicHospital");

        // Add a ValueEventListener to fetch clinicHospital information
        clinicHospitalReference.addValueEventListener(new ValueEventListener() {
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
        // Create a list to store clinicHospital information
        List<String> clinicHospitalList = new ArrayList<>();

        for (DataSnapshot clinicHospitalSnapshot : dataSnapshot.getChildren()) {
            String date = clinicHospitalSnapshot.getKey();

            for (DataSnapshot timeSnapshot : clinicHospitalSnapshot.getChildren()) {
                String time = timeSnapshot.getKey();
                Map<String, Object> clinicHospitalData = (Map<String, Object>) timeSnapshot.getValue();

                if (clinicHospitalData != null) {
                    String name = String.valueOf(clinicHospitalData.get("name"));
                    String number = String.valueOf(clinicHospitalData.get("number"));

                    // Build a string with clinicHospital information
                    String clinicHospitalInfo = "\nAppointment Number: " + number + "\nDate: " + date + "\nTime: " + time + "\nName: " + name + "\n";

                    clinicHospitalList.add(clinicHospitalInfo);
                }
            }
        }

        // Create an adapter to populate the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_list_item_1, clinicHospitalList);

        clinicHospitalListView.setAdapter(adapter);
    }

    @Override
    public void onViewCreated( @NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated ( view, savedInstanceState );

        Button backButton = getView ().findViewById ( R.id.backButton5);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_adminClinicHospitalFragment_to_adminAppointmentFragment);
            }
        } );
    }
}
