package com.example.soulmate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

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

import java.util.ArrayList;
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
            Map<String, Object> userData = (Map<String, Object>) userSnapshot.child("User Info").getValue();

            if (userData != null) {
                String username = String.valueOf(userData.get("name"));
                String dob = String.valueOf(userData.get("Date Of Birth"));
                String gender = String.valueOf(userData.get("Gender"));
                String phone = String.valueOf(userData.get("Mobile Number"));
                String email = String.valueOf(userData.get("email"));
                String address = String.valueOf(userData.get("Address"));

                    // Build a string with user information
                String userInfo = "\nName: " + username + "\nGender: " + gender + "\nDate of Birth: " + dob
                        + "\nMobile Number: " + phone + "\nEmail: " + email + "\nAddress: " + address + "\n";

                userList.add(userInfo);
            }
        }

        // Create an adapter to populate the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_list_item_1, userList);

        userListView.setAdapter(adapter);

//        // Handle item click in the ListView
//        userListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                // Handle the item click, e.g., display more details
//                String name = userList.get(position);
//                Toast.makeText(requireContext(),name, Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public void onViewCreated( @NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button backButton = getView().findViewById(R.id.backButton2);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_adminUserInfoFragment_to_adminMainPageFragment);
            }
        });
    }
}
