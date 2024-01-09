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

public class AdminTelemedicineFragment extends Fragment {

    private ListView telemedicineListView;
    private DatabaseReference telemedicineReference;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_telemedicine, container, false);

        telemedicineListView = view.findViewById(R.id.telemedicineListView);
        telemedicineReference = FirebaseDatabase.getInstance().getReference().child("Appointment").child("Telemedicine");

        // Add a ValueEventListener to fetch telemedicine information
        telemedicineReference.addValueEventListener(new ValueEventListener() {
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
        // Create a list to store telemedicine information
        List<String> telemedicineList = new ArrayList<>();

        for (DataSnapshot telemedicineSnapshot : dataSnapshot.getChildren()) {
            String date = telemedicineSnapshot.getKey();

            for (DataSnapshot timeSnapshot : telemedicineSnapshot.getChildren()) {
                String time = timeSnapshot.getKey();
                Map<String, Object> telemedicineData = (Map<String, Object>) timeSnapshot.getValue();

                if (telemedicineData != null) {
                    String name = String.valueOf(telemedicineData.get("name"));
                    String number = String.valueOf(telemedicineData.get("number"));

                    // Build a string with telemedicine information
                    String telemedicineInfo = "\nDate: " + date + "\nTime: " + time + "\nName: " + name + "\nPhone Number: " + number  + "\n";

                    telemedicineList.add(telemedicineInfo);
                }
            }
        }

        // Create an adapter to populate the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_list_item_1, telemedicineList);

        telemedicineListView.setAdapter(adapter);
    }

    @Override
    public void onViewCreated( @NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated ( view, savedInstanceState );

        Button backButton = getView ().findViewById ( R.id.backButton6);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_adminTelemedicineFragment_to_adminAppointmentFragment);
            }
        } );
    }
}
