//package com.example.soulmate.ui.settings;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.lifecycle.ViewModelProvider;
//import androidx.navigation.NavController;
//import androidx.navigation.Navigation;
//
//import com.example.soulmate.R;
//import com.example.soulmate.databinding.FragmentSettingsBinding;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.Map;
//
//public class SettingsFragment extends Fragment {
//
//    private FragmentSettingsBinding binding;
//    private DatabaseReference userReference;
//    private FirebaseUser currentUser;
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//        SettingsViewModel settingsViewModel =
//                new ViewModelProvider(this).get(SettingsViewModel.class);
//
//        binding = FragmentSettingsBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();
//
//        currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        if (currentUser != null) {
//            String userId = currentUser.getUid();
//            userReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
//            fetchUserData();
//        }
//
//        return root;
//    }
//
//    private void fetchUserData() {
//        userReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                // Assuming your data structure is a Map<String, Object>
//                Map<String, Object> userData = (Map<String, Object>) dataSnapshot.getValue();
//
//                // Display user information
//                if (userData != null) {
//                    TextView usernameTextView = getView().findViewById(R.id.nameProfile);
//                    TextView emailTextView = getView().findViewById(R.id.email);
//
//                    String username = (String) userData.get("name");
//                    String email = (String) userData.get("email");
//
//                    usernameTextView.setText("Username: " + username);
//                    emailTextView.setText("Email: " + email);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Handle error
//            }
//        });
//    }
//
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        binding = null;
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        Button updateProfile = getView().findViewById(R.id.updateProfileButton);
//
//        updateProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                NavController controller = Navigation.findNavController(v);
//                controller.navigate(R.id.action_nav_settings_to_updateProfile);
//            }
//        });
//    }
//}

package com.example.soulmate.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.soulmate.R;
import com.example.soulmate.databinding.FragmentSettingsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private DatabaseReference userReference;
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SettingsViewModel settingsViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            userReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
            fetchUserData();
        }

        return root;
    }

    private void fetchUserData() {
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Assuming your data structure is a Map<String, Object>
                Map<String, Object> userData = (Map<String, Object>) dataSnapshot.getValue();

                // Display user information
                showAllUserData(userData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                displayError();
            }
        });
    }

    public void showAllUserData(Map<String, Object> userData) {
        if (userData != null) {
            setTextOrPlaceholder(R.id.nameProfile, "Name: ", (String) userData.get("name"));
            setTextOrPlaceholder(R.id.birthProfile, "Date Of Birth: ", (String) userData.get("Date Of Birth"));
            setTextOrPlaceholder(R.id.genderProfile, "Gender: ", (String) userData.get("Gender"));
            setTextOrPlaceholder(R.id.emailProfile, "Email: ", (String) userData.get("email"));
            setTextOrPlaceholder(R.id.phoneProfile, "Phone: ", (String) userData.get("Mobile Number"));
            setTextOrPlaceholder(R.id.addressProfile, "Address: ", (String) userData.get("Address"));
            setTextOrPlaceholder(R.id.emergencyProfile, "Name: ", (String) userData.get("nameEmergency"));
            setTextOrPlaceholder(R.id.phoneEmergency, "Contact: ", (String) userData.get("contactEmergency"));
        } else {
            // Data is not available, display placeholders
            displayError();
        }
    }

    private void setTextOrPlaceholder(int textViewId, String prefix, Object value) {
        TextView textView = getView().findViewById(textViewId);
        if (value != null) {
            textView.setText(prefix + value.toString());
        } else {
            textView.setText(prefix + " - ");
        }
    }

    private void displayError() {
        // Handle error, for example, display "Error fetching data"
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button updateProfile = getView().findViewById(R.id.updateProfileButton);

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_nav_settings_to_updateProfile);
            }
        });
    }
}
