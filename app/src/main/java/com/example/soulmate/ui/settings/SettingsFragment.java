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
import androidx.navigation.fragment.NavHostFragment;

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
    private FirebaseUser currentUser;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SettingsViewModel settingsViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);

        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
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
                displayUserInfo(userData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                displayError();
            }
        });
    }

    private void displayUserInfo(Map<String, Object> userData) {
        if (userData != null) {
            setTextOrPlaceholder(R.id.nameProfile, "Name : ", userData.get("name"));
            setTextOrPlaceholder(R.id.birthProfile, "Date of Birth : ", userData.get("Date of Birth"));
            setTextOrPlaceholder(R.id.gender3, "Gender : ", userData.get("Gender"));
            setTextOrPlaceholder(R.id.email, "Email Address : ", userData.get("email"));
            setTextOrPlaceholder(R.id.birthProfile4, "Mobile No : ", userData.get("Mobile Number"));
            setTextOrPlaceholder(R.id.birthProfile5, "Address : ", userData.get("Address"));
            setTextOrPlaceholder(R.id.birthProfile8, "Name : ", userData.get("emergencyContactName"));
            setTextOrPlaceholder(R.id.birthProfile9, "Phone No : ", userData.get("emergencyContactPhone"));
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

