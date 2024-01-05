package com.example.soulmate.ui.settings;

import static androidx.fragment.app.FragmentManager.TAG;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.soulmate.R;
import com.example.soulmate.main_page;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class UpdateProfileFragment extends Fragment {

    private EditText editName;
    private EditText editMobile;
    private EditText editAddress;
    private static String Genderchoice;
    private EditText editNameEmergency;
    private EditText editContactEmergency;
    private TextView editDOB;
    private TextView emailView;
    private Button submit;
    private  RadioButton gender;
    private String newGender,newName ,newDOB, newEmail,newMobile,newAddress,newNameEmergency ,newContactEmergency;
    private static String getName, getDOB,getGender,getMobile,getAddress;
    private DatabaseReference userReference;
    private FirebaseUser currentUser;
    private DatePickerDialog.OnDateSetListener DateSetListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_profile, container, false);

        if (getActivity() instanceof main_page) {
            ((main_page) getActivity()).getSupportActionBar().setTitle("Update Profile");
        }

        // Initialize your EditText fields
        editName = view.findViewById(R.id.editName);
        editDOB = view.findViewById(R.id.editDOB);
        emailView = view.findViewById(R.id.editEmail);
        editMobile = view.findViewById(R.id.editPhone);
        editAddress = view.findViewById(R.id.editAddress);
        editNameEmergency = view.findViewById(R.id.nameEmergency);
        editContactEmergency = view.findViewById(R.id.contactEmergency);
        RadioGroup rg = view.findViewById(R.id.editGender);



        submit = view.findViewById(R.id.submitButton);

        // Fetch the current user data and set the initial values
        fetchCurrentUserData();

        int option1Id = R.id.radioButton1;
        int option2Id = R.id.radioButton2;

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String userId = currentUser.getUid();
        userReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("User Info");

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Genderchoice = String.valueOf(snapshot.child("Gender").getValue());
                if(Genderchoice.equals("Male"))
                {
                    rg.check(option1Id);
                }
                else if(Genderchoice.equals("Female"))
                    rg.check(option2Id);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





        TextWatcher submitTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                int genid = rg.getCheckedRadioButtonId();

                String name = editName.getText().toString().trim();
                String DOB = editDOB.getText().toString().trim();
                String email = emailView.getText().toString().trim();
                String mobile= editMobile.getText().toString().trim();
                String address = editAddress.getText().toString().trim();
                String E = editNameEmergency.getText().toString().trim();
                String numE = editContactEmergency.getText().toString().trim();

                rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        submit.setEnabled(!name.isEmpty() && !DOB.isEmpty() && !email.isEmpty() && !mobile.isEmpty() && !address.isEmpty()
                                && !E.isEmpty() && !numE.isEmpty() && (genid!=-1));
                    }
                });

                submit.setEnabled(!name.isEmpty() && !DOB.isEmpty() && !email.isEmpty() && !mobile.isEmpty() && !address.isEmpty()
                && !E.isEmpty() && !numE.isEmpty() && (genid!=-1));
            }

            @Override
            public void afterTextChanged(Editable s) {
                rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {

                        int genid = rg.getCheckedRadioButtonId();

                        String name = editName.getText().toString().trim();
                        String DOB = editDOB.getText().toString().trim();
                        String email = emailView.getText().toString().trim();
                        String mobile = editMobile.getText().toString().trim();
                        String address = editAddress.getText().toString().trim();
                        String E = editNameEmergency.getText().toString().trim();
                        String numE = editContactEmergency.getText().toString().trim();
                        submit.setEnabled(!name.isEmpty() && !DOB.isEmpty() && !email.isEmpty() && !mobile.isEmpty() && !address.isEmpty()
                                && !E.isEmpty() && !numE.isEmpty() && (genid != -1));
                    }
                });
            }
        };
        editName.addTextChangedListener(submitTextWatcher);
        editDOB.addTextChangedListener(submitTextWatcher);
        emailView.addTextChangedListener(submitTextWatcher);
        editMobile.addTextChangedListener(submitTextWatcher);
        editAddress.addTextChangedListener(submitTextWatcher);
        editNameEmergency.addTextChangedListener(submitTextWatcher);
        editContactEmergency.addTextChangedListener(submitTextWatcher);

        editDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        DateSetListener,
                        year, month, day
                );
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();

            }
        });
        DateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: dd/mm/yyyy: " + day + "/" + month + "/" + year);
                String date = day + "/" + month + "/" + year;
                editDOB.setText(date);
            }

        };


        // Set onClickListener for the submit button
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Update user information in the database
                updateUserInfo();
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_updateProfile_to_nav_settings);

            }
        });

        return view;
    }


    private void fetchCurrentUserData() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            userReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("User Info");

            userReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                    // Assuming your data structure is a Map<String, Object>
                    User currentUserData = dataSnapshot.getValue(User.class);

                    // Set the initial values in the EditText fields
                    if (currentUserData != null) {
                        editName.setText(currentUserData.getName());
                        editDOB.setText(String.valueOf(dataSnapshot.child("Date Of Birth").getValue()));
                        Genderchoice = String.valueOf(dataSnapshot.child("Gender").getValue());
                        emailView.setText(currentUserData.getEmail());
                        editMobile.setText(String.valueOf(dataSnapshot.child("Mobile Number").getValue()));
                        if(String.valueOf(dataSnapshot.child("Address").getValue())!="null") {
                            editAddress.setText(String.valueOf(dataSnapshot.child("Address").getValue()));
                        }
                        else {
                            editAddress.setText("");
                        }
                        editNameEmergency.setText(currentUserData.getNameEmergency());
                        editContactEmergency.setText(currentUserData.getContactEmergency());
                        getGender = String.valueOf(dataSnapshot.child("gender").getValue());
                        User user = new User();
                        user.setPassword(currentUserData.getPassword());
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
        RadioGroup rg = getView().findViewById(R.id.editGender);
        int genid = rg.getCheckedRadioButtonId();
        gender = getView().findViewById(genid);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            userReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("User Info");
            userReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull com.google.firebase.database.DataSnapshot dataSnapshot) {
                    // Assuming your data structure is a Map<String, Object>
                    getGender = String.valueOf(dataSnapshot.child("gender").getValue());
                    getName = String.valueOf(dataSnapshot.child("name").getValue());
                    getDOB = String.valueOf(dataSnapshot.child("Date Of Birth").getValue());
                    getMobile = String.valueOf(dataSnapshot.child("Mobile Number").getValue());
                    getAddress = String.valueOf(dataSnapshot.child("Address").getValue());

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error
                 }
            });



                            newGender = gender.getText().toString();

                            newName = editName.getText().toString();

                            newDOB = editDOB.getText().toString();

                        newEmail = emailView.getText().toString();

                            newMobile = editMobile.getText().toString();

                            newAddress = editAddress.getText().toString();

                        newNameEmergency = editNameEmergency.getText().toString();
                        newContactEmergency = editContactEmergency.getText().toString();

                        // Create a User object with the new values
                        User updatedUser = new User(newName, newDOB, newGender, newEmail, newMobile, newAddress, newNameEmergency, newContactEmergency);

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
            this.name = "";
        }

        public User(String name, String dateOfBirth, String gender, String email, String mobile, String address, String nameEmergency, String contactEmergency) {
            this.name = name;
            this.dateOfBirth = dateOfBirth;
            this.gender = gender;
            this.email = email;
            this.mobile = mobile;
            this.address = address;
//            this.password = password;
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