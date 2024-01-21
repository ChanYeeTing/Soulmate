package com.example.soulmate;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class AdminUserInfoFragment extends Fragment {

    private ListView userListView;
    private DatabaseReference usersReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_user_info, container, false);

        userListView = view.findViewById(R.id.userInfoListView);
        usersReference = FirebaseDatabase.getInstance().getReference().child("Users");

        // Add a ValueEventListener to fetch user information
        usersReference.addValueEventListener(new ValueEventListener() {
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
        // Create a list to store user information
        List<String> userList = new ArrayList<>();

        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
            String uid = userSnapshot.getKey();
            Map<String, Object> userData = (Map<String, Object>) userSnapshot.child("User Info").getValue();

            if (userData != null) {
                String username = String.valueOf(userData.get("name"));
                String dob = String.valueOf(userData.get("Date Of Birth"));
                String gender = String.valueOf(userData.get("Gender"));
                String phone = String.valueOf(userData.get("Mobile Number"));
                String email = String.valueOf(userData.get("email"));
                String address = String.valueOf(userData.get("Address"));

                // Build a string with user information
                String userInfo = "\nUser ID:\n" + uid + "\nName: " + username + "\nGender: " + gender + "\nDate of Birth: " + dob
                        + "\nMobile Number: " + phone + "\nEmail: " + email + "\nAddress: " + address + "\n";

                userList.add(userInfo);
            }
        }

        // Sort the list based on usernames
        Collections.sort(userList, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                // Extract usernames from the strings and compare
                String username1 = extractValue(s1, "Name: ", "\nGender: ");
                String username2 = extractValue(s2, "Name: ", "\nGender: ");
                return username1.compareTo(username2);
            }
        });

        // Create an adapter to populate the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_list_item_1, userList);

        userListView.setAdapter(adapter);

        // Add long click listener to the ListView items
        userListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected user information
                String selectedUserInfo = userList.get(position);

                // Extract the user ID from the selected information
                String uid = extractValue(selectedUserInfo, "User ID:\n", "\nName:");

                // Prompt the admin for confirmation to delete the user account
                showDeleteConfirmationDialog(uid);

                return true; // Consume the long click event
            }
        });
    }

    // Helper method to extract a value from a string
    private String extractValue(String source, String start, String end) {
        int startIndex = source.indexOf(start) + start.length();
        int endIndex = source.indexOf(end, startIndex);
        return source.substring(startIndex, endIndex);
    }

    // Add a method to show a confirmation dialog
    private void showDeleteConfirmationDialog(final String userId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Delete User Account");
        builder.setMessage("Are you sure you want to delete this user account?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Delete the user account
                deleteUserAccount(userId);
                deleteAuthenticationAccount(userId);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    // Delete user data in firebase
    private void deleteUserAccount(String userId) {
        if (userId != null) {
            DatabaseReference userRef = usersReference.child(userId);
            userRef.removeValue()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(requireContext(), "Please wait for a while", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(requireContext(), "Failed to delete user account", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(requireContext(), "Invalid user ID", Toast.LENGTH_SHORT).show();
        }
    }

    //Delete user account in authentication
    private void deleteAuthenticationAccount(String userId) {
        // Get the authenticated user using the UID
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            // Delete the user's authentication account
            user.delete()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(requireContext(), "User account deleted", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(requireContext(), "Failed to delete user authentication account", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(requireContext(), "User is not authenticated", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button backButton = view.findViewById(R.id.backButton2);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_adminUserInfoFragment_to_adminMainPageFragment);
            }
        });
    }
}
