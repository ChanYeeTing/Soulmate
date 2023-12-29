package com.example.soulmate.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.soulmate.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class UpdateProfileFragment extends Fragment {

    private EditText editName, editDOB, editGender, editEmail, editMobile, editAddress, editPassword, editNameEmergency, editContactEmergency;
    private Button submit;

    private DatabaseReference userReference;
    private FirebaseUser currentUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_profile, container, false);

        // Initialize your EditText fields
        editName = view.findViewById(R.id.editName);
        editDOB = view.findViewById(R.id.editDOB);
        editGender = view.findViewById(R.id.editGender);
        editEmail = view.findViewById(R.id.editEmail);
        editMobile = view.findViewById(R.id.editPhone);
        editAddress = view.findViewById(R.id.editAddress);
        editPassword = view.findViewById(R.id.editPassword);
        editNameEmergency = view.findViewById(R.id.nameEmergency);
        editContactEmergency = view.findViewById(R.id.contactEmergency);

        submit = view.findViewById(R.id.submitButton);

        // Fetch the current user data and set the initial values
        fetchCurrentUserData();

        // Set onClickListener for the submit button
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update user information in the database
                updateUserInfo();
            }
        });

        return view;
    }


    private void fetchCurrentUserData() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            userReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

            userReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                    // Assuming your data structure is a Map<String, Object>
                    User currentUserData = dataSnapshot.getValue(User.class);

                    // Set the initial values in the EditText fields
                    if (currentUserData != null) {
                        editName.setText(currentUserData.getName());
                        editDOB.setText(currentUserData.getDateOfBirth());
                        editGender.setText(currentUserData.getGender());
                        editEmail.setText(currentUserData.getEmail());
                        editMobile.setText(currentUserData.getMobile());
                        editAddress.setText(currentUserData.getAddress());
                        editPassword.setText(currentUserData.getPassword());
                        editNameEmergency.setText(currentUserData.getNameEmergency());
                        editContactEmergency.setText(currentUserData.getContactEmergency());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error
                }
            });
        }
    }

    private void updateUserInfo() {
        // Get the new values from the EditText fields
        String newName = editName.getText().toString();
        String newDOB = editDOB.getText().toString();
        String newGender = editGender.getText().toString();
        String newEmail = editEmail.getText().toString();
        String newMobile = editMobile.getText().toString();
        String newAddress = editAddress.getText().toString();
        String newPassword = editPassword.getText().toString();
        String newNameEmergency = editNameEmergency.getText().toString();
        String newContactEmergency = editContactEmergency.getText().toString();

        // Create a User object with the new values
        User updatedUser = new User(newName, newDOB, newGender, newEmail, newMobile, newAddress, newPassword, newNameEmergency, newContactEmergency);

        // Update the user information in the database using userReference
        userReference.setValue(updatedUser.toMap())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Show a toast message
                            Toast.makeText(getContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            // Handle the error
                            Toast.makeText(getContext(), "Failed to update profile", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public static class User {

        private String name;
        private String dateOfBirth;
        private String gender;
        private String email;
        private String mobile;
        private String address;
        private String password;
        private String nameEmergency;
        private String contactEmergency;

        public User() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public User(String name, String dateOfBirth, String gender, String email, String mobile, String address, String password, String nameEmergency, String contactEmergency) {
            this.name = name;
            this.dateOfBirth = dateOfBirth;
            this.gender = gender;
            this.email = email;
            this.mobile = mobile;
            this.address = address;
            this.password = password;
            this.nameEmergency = nameEmergency;
            this.contactEmergency = contactEmergency;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getNameEmergency() {
            return nameEmergency;
        }

        public void setNameEmergency(String nameEmergency) {
            this.nameEmergency = nameEmergency;
        }

        public String getContactEmergency() {
            return contactEmergency;
        }

        public void setContactEmergency(String contactEmergency) {
            this.contactEmergency = contactEmergency;
        }

        public Map<String, Object> toMap() {
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("name", name);
            userMap.put("Date Of Birth", dateOfBirth);
            userMap.put("Gender", gender);
            userMap.put("email", email);
            userMap.put("Mobile Number", mobile);
            userMap.put("Address", address);
            userMap.put("Password", password);
            userMap.put("nameEmergency", nameEmergency);
            userMap.put("contactEmergency", contactEmergency);
            return userMap;
        }
    }
}