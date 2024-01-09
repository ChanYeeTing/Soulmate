package com.example.soulmate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

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

import java.util.Map;

public class AdminMainPageFragment extends Fragment {

    private LinearLayout checkboxContainer;
    private DatabaseReference usersReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_main_page, container, false);

        checkboxContainer = view.findViewById(R.id.checkboxContainer);

        usersReference = FirebaseDatabase.getInstance().getReference().child("Users");

        // Add a ValueEventListener to fetch user information
        usersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String uid = userSnapshot.getKey();

                    // Access the "Location" node
                    DataSnapshot locationSnapshot = userSnapshot.child("Location");

                    // Iterate over the child nodes of "Location"
                    for (DataSnapshot idSnapshot : locationSnapshot.getChildren()) {
                        String id = idSnapshot.getKey();

                        // Check if the value is a JSON object (a map-like structure)
                        if (idSnapshot.getValue() instanceof Map) {
                            Map<String, Object> userData = (Map<String, Object>) idSnapshot.getValue();
                            String username = String.valueOf(userData.get("name"));
                            String location = String.valueOf(userData.get("currentLocation"));
                            String nameEmergency = String.valueOf(userData.get("nameEmergency"));
                            String contactEmergency = String.valueOf(userData.get("contactEmergency"));

                            if (location != null) {
                                // Create a CheckBox for each user and add it to the checkboxContainer
                                CheckBox checkBox = new CheckBox(requireContext());
                                checkBox.setText("\n" + "User ID:" + uid + "\nName: " + username + "\n\nCurrent Location: " + location + "\n\nEmergency Contact: " + nameEmergency + " (" + contactEmergency + ") " + "\n");
                                checkBox.setTag(userSnapshot.getKey()); // Set a tag to identify the user
                                checkBox.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        handleCheckBoxClick((CheckBox) v);
                                    }
                                });

                                checkboxContainer.addView(checkBox);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
            }
        });

        return view;
    }

    private void handleCheckBoxClick(CheckBox checkBox) {
        // Handle the checkbox click, e.g., add userId to a list if checked
        Toast.makeText(requireContext(), "The emergency call has made", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button appointment = view.findViewById(R.id.appointmentRecord);
        Button userInfo = view.findViewById(R.id.userInfoButton);
        Button medicalHistory = view.findViewById(R.id.medicalHistoryRecord);

        appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_adminMainPageFragment_to_adminAppointmentFragment);
            }
        });

        userInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_adminMainPageFragment_to_adminUserInfoFragment);
            }
        });

        medicalHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_adminMainPageFragment_to_adminMedicalHistoryFragment);
            }
        });
    }
}