//package com.example.soulmate;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.navigation.NavController;
//import androidx.navigation.Navigation;
//
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//public class DoctorLoginFragment extends Fragment {
//
//    private EditText nameEditText, emailDoctorEditText, passDoctorEditText;
//    private Button loginBtn;
//    private DatabaseReference doctorDatabase;
//
//    public DoctorLoginFragment() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        doctorDatabase = FirebaseDatabase.getInstance().getReference().child("Doctor");
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_doctor_login, container, false);
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        nameEditText = getView().findViewById(R.id.editTextname2);
//        emailDoctorEditText = getView().findViewById(R.id.editTextTextEmailAddress2);
//        passDoctorEditText = getView().findViewById(R.id.editTextPassword2);
//        loginBtn = getView().findViewById(R.id.loginButton2);
//
//        loginBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String name = nameEditText.getText().toString().trim();
//                String email = emailDoctorEditText.getText().toString().trim();
//                String password = passDoctorEditText.getText().toString().trim();
//
//                if (validateInputs(name, email, password)) {
//                    // Check doctor credentials in the Firebase Realtime Database
//                    checkDoctorCredentials(name, email, password, v);
//                }
//            }
//        });
//    }
//
//    private boolean validateInputs(String name, String email, String password) {
//        // Validation logic here
//        // Return true if inputs are valid, otherwise show a Toast and return false
//        return true;
//    }
//
//    private void checkDoctorCredentials(String name, String email, String password, View v) {
//        doctorDatabase.child(name).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    // Doctor already exists, check credentials
//                    String storedEmail = dataSnapshot.child("emailDoctor").getValue(String.class);
//                    String storedPassword = dataSnapshot.child("passwordDoctor").getValue(String.class);
//                    if (email.equals(storedEmail) && password.equals(storedPassword)) {
//                        // Doctor login successful
//                        Toast.makeText(getActivity(), "Doctor login successful.", Toast.LENGTH_SHORT).show();
//                        // Add your navigation logic here
//                        NavController controller = Navigation.findNavController(v);
//                        // Replace with the appropriate action for doctor's main page
//                        controller.navigate(R.id.action_doctorLoginFragment_to_doctorMainPageFragment);
//                    } else {
//                        Toast.makeText(getActivity(), "Incorrect email or password.", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//                    // Doctor not found, create a new entry in the database
//                    saveDoctorData(name, email, password, v);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Toast.makeText(getActivity(), "Error checking doctor credentials.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void saveDoctorData(String name, String email, String password, View v) {
//        // Save the new doctor's data to the database
//        DatabaseReference newDoctorRef = doctorDatabase.child(name);
//        newDoctorRef.child("emailDoctor").setValue(email);
//        newDoctorRef.child("passwordDoctor").setValue(password);
//
//        Toast.makeText(getActivity(), "Doctor registration successful.", Toast.LENGTH_SHORT).show();
//
//        // Add your navigation logic here
//        NavController controller = Navigation.findNavController(v);
//        // Replace with the appropriate action for doctor's main page
//        controller.navigate(R.id.action_doctorLoginFragment_to_doctorMainPageFragment);
//    }
//}


package com.example.soulmate;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DoctorLoginFragment extends Fragment {

    private EditText nameEditText, emailDoctorEditText, passDoctorEditText;
    private Button loginBtn, teleBtn;
    private Spinner hospitalSpinner;
    private SharedViewModel viewModel;
    private DatabaseReference doctorDatabase;

    public DoctorLoginFragment() {
        // Required empty public constructor
    }
    @Override
    public void onStart() {
        super.onStart();

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                NavController controller = Navigation.findNavController(requireView());
                controller.navigate(R.id.action_doctorLoginFragment_to_login_fragment);
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doctorDatabase = FirebaseDatabase.getInstance().getReference().child("Doctor");
        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_doctor_login, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        emailDoctorEditText = getView().findViewById(R.id.editTextTextEmailAddress2);
        passDoctorEditText = getView().findViewById(R.id.editTextPassword2);
        loginBtn = getView().findViewById(R.id.loginButton2);
        teleBtn = getView().findViewById(R.id.TeleDoctorLoginButton);
        hospitalSpinner = getView().findViewById(R.id.hospitalSpinner);

        String hospitalNames[] = {"Clinic Medicris", "Clinic Medilife", "Clinic Putra Simpang Ampat", "Island Hospital"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, hospitalNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        hospitalSpinner.setAdapter(adapter);

        teleBtn.setOnClickListener(v -> {
            NavController controller = Navigation.findNavController(v);
            controller.navigate(R.id.action_doctorLoginFragment_to_teleDoctorLoginFragment);
        });

        loginBtn.setOnClickListener(v -> {
            String hospitalName = hospitalSpinner.getSelectedItem().toString();
            String email = emailDoctorEditText.getText().toString().trim();
            String password = passDoctorEditText.getText().toString().trim();

            if (validateInputs(email, password)) {
                // Check doctor credentials in the Firebase Realtime Database
                checkDoctorCredentials(email, password, hospitalName, v);
            }
        });
    }

    private boolean validateInputs(String email, String password) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity(), "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.length() < 6) {
            Toast.makeText(getActivity(), "Password must be at least 6 characters.", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private void checkDoctorCredentials(String email, String password, String hospitalName, View v) {
        doctorDatabase.child(hospitalName).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String storedEmail = dataSnapshot.child("emailDoctor").getValue(String.class);
                    String storedPassword = dataSnapshot.child("passwordDoctor").getValue(String.class);
//                    SharedViewModel viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);


                    if (email.equals(storedEmail) && password.equals(storedPassword)) {
                        // Doctor login successful
                        Toast.makeText(getActivity(), "Doctor login successful.", Toast.LENGTH_SHORT).show();

                        // Pass hospitalName to DoctorMainPageFragment using Bundle
                        Bundle bundle = new Bundle();
                        bundle.putString("hospitalName", hospitalName);
                        viewModel.setSharedData(hospitalName);


                        NavController controller = Navigation.findNavController(v);
                        controller.navigate(R.id.action_doctorLoginFragment_to_doctorMainPageFragment, bundle);
                    } else {
                        Toast.makeText(getActivity(), "Incorrect email or password.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Doctor not found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Error checking doctor credentials.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button teledoctor = view.findViewById(R.id.TeleDoctorLoginButton);
        Button loginDoctor = view.findViewById(R.id.loginButton2);
        teledoctor.setOnClickListener(v -> {
            NavController controller = Navigation.findNavController(v);
            controller.navigate(R.id.action_doctorLoginFragment_to_teleDoctorLoginFragment);
        });
        loginDoctor.setOnClickListener(v -> {
            NavController controller = Navigation.findNavController(v);
            controller.navigate(R.id.action_doctorLoginFragment_to_doctorMainPageFragment);
        });
    }
}
