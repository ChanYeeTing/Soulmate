////save admin data to firebase
//
//package com.example.soulmate;
//
//import android.os.Bundle;
//import android.text.TextUtils;
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
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//
//public class AdminLoginFragment extends Fragment {
//
//    private EditText emailAdminEditText, passAdminEditText, nameAdminEditText;
//    private Button loginBtn;
//    private DatabaseReference adminDatabase;
//
//    public AdminLoginFragment() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        adminDatabase = FirebaseDatabase.getInstance().getReference().child("Admin");
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_admin_login, container, false);
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        emailAdminEditText = getView().findViewById(R.id.editTextTextEmailAddress);
//        passAdminEditText = getView().findViewById(R.id.editTextPassword);
//        nameAdminEditText = getView().findViewById(R.id.editTextname);
//        loginBtn = getView().findViewById(R.id.loginButton20);
//
//        loginBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String email = emailAdminEditText.getText().toString().trim();
//                String password = passAdminEditText.getText().toString().trim();
//                String name = nameAdminEditText.getText().toString().trim();
//
//                if (validateInputs(email, password)) {
//                    // Save admin data to Firebase
//                    saveAdminData(email, password, name);
//
//                    // Navigate to the admin main page
//                    NavController controller = Navigation.findNavController(v);
//                    controller.navigate(R.id.action_adminLoginFragment_to_adminMainPage);
//                }
//            }
//        });
//    }
//
//    private boolean validateInputs(String email, String password) {
//        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
//            Toast.makeText(getActivity(), "Please fill in all fields.", Toast.LENGTH_SHORT).show();
//            return false;
//        } else if (password.length() < 6) {
//            Toast.makeText(getActivity(), "Password must be at least 6 characters.", Toast.LENGTH_SHORT).show();
//            return false;
//        } else {
//            return true;
//        }
//    }
//
//    private void saveAdminData(String email, String password, String nameAdmin) {
//        // Generate a random UID for the new admin
//        //String adminUid = adminDatabase.push().getKey();
//
//
//        // Create a reference to the new admin node
//        com.google.firebase.database.DatabaseReference adminRef = adminDatabase.child(nameAdmin);
//
//        // Set the email and password for the new admin
//        adminRef.child("emailAdmin").setValue(email);
//        adminRef.child("passwordAdmin").setValue(password);
//    }
//
//}

package com.example.soulmate;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminLoginFragment extends Fragment {

    private EditText nameEditText, emailAdminEditText, passAdminEditText;
    private Button loginBtn;
    private DatabaseReference adminDatabase;

    public AdminLoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adminDatabase = FirebaseDatabase.getInstance().getReference().child("Admin");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_login, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        nameEditText = getView().findViewById(R.id.editTextname);
        emailAdminEditText = getView().findViewById(R.id.editTextTextEmailAddress);
        passAdminEditText = getView().findViewById(R.id.editTextPassword);
        loginBtn = getView().findViewById(R.id.loginButton20);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString().trim();
                String email = emailAdminEditText.getText().toString().trim();
                String password = passAdminEditText.getText().toString().trim();

                if (validateInputs(name, email, password)) {
                    // Check admin credentials in the Firebase Realtime Database
                    checkAdminCredentials(name, email, password, v);
                }
            }
        });
    }

    private boolean validateInputs(String name, String email, String password) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity(), "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.length() < 6) {
            Toast.makeText(getActivity(), "Password must be at least 6 characters.", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private void checkAdminCredentials(String name, String email, String password, View v) {
        adminDatabase.child(name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String storedEmail = dataSnapshot.child("emailAdmin").getValue(String.class);
                    String storedPassword = dataSnapshot.child("passwordAdmin").getValue(String.class);
                    if (email.equals(storedEmail) && password.equals(storedPassword)) {
                        // Admin login successful
                        Toast.makeText(getActivity(), "Admin login successful.", Toast.LENGTH_SHORT).show();
                        // Add your navigation logic here
                        NavController controller = Navigation.findNavController(v);
                        controller.navigate(R.id.action_adminLoginFragment_to_adminMainPageFragment);
                    } else {
                        Toast.makeText(getActivity(), "Incorrect email or password.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Admin not found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Error checking admin credentials.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

