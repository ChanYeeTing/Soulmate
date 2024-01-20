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

    private static FragmentSettingsBinding binding;
    private DatabaseReference userReference;
    private static Map<String, Object> userData;
    private static String name, DOB, gender, email, number, address, nameE, numberE;
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SettingsViewModel settingsViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);


        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        TextView textView1 = binding.nameProfile;
        TextView textView2 = binding.birthProfile;
        TextView textView3 = binding.genderProfile;
        TextView textView4 = binding.emailProfile;
        TextView textView5 = binding.phoneProfile;
        TextView textView6 = binding.addressProfile;
        TextView textView7 = binding.emergencyProfile;
        TextView textView8 = binding.phoneEmergency;

        if (currentUser != null) {
            String userId = currentUser.getUid();
            userReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("User Info");

            userReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Assuming your data structure is a Map<String, Object>
                    userData = (Map<String, Object>) dataSnapshot.getValue();
                    if (userData != null) {

                        DOB = String.valueOf(dataSnapshot.child("Date of Birth").getValue());
                        name = String.valueOf(dataSnapshot.child("name").getValue());
                        gender = String.valueOf(dataSnapshot.child("Gender").getValue());
                        email = String.valueOf(dataSnapshot.child("email").getValue());
                        number = String.valueOf(dataSnapshot.child("Mobile Number").getValue());
                        address = String.valueOf(dataSnapshot.child("Address").getValue());
                        nameE = String.valueOf(dataSnapshot.child("nameEmergency").getValue());
                        numberE = String.valueOf(dataSnapshot.child("contactEmergency").getValue());


                        if(name!="null"&&name!="")
                        {
                            textView1.setText("Name: \t" + name.toString());
                        }
                        else {
                            textView1.setText("Name: \t" +  " - ");
                        }
                        if(DOB!="null"&&DOB!="")
                        {
                            textView2.setText("Date of Birth: \t" + DOB.toString());
                        }
                        else {
                            textView2.setText("Date of Birth: \t" +  " - ");
                        }
                        if(gender!="null"&&gender!="")
                        {
                            textView3.setText("Gender: \t" + gender.toString());
                        }
                        else {
                            textView3.setText("Gender: \t" +  " - ");
                        }
                        if(email!="null"&&email!="")
                        {
                            textView4.setText("Email: \t" + email.toString());
                        }
                        else {
                            textView4.setText("Email: \t" +  " - ");
                        }
                        if(number!="null"&&number!="")
                        {
                            textView5.setText("Phone: \t" + number.toString());
                        }
                        else {
                            textView5.setText("Phone: \t" +  " - ");
                        }
                        if(address!="null"&& address!="")
                        {
                            textView6.setText("Address: \t" + address.toString());
                        }
                        else {
                            textView6.setText("Address: \t" +  " - ");
                        }
                        if(nameE!="null"&&nameE!="")
                        {
                            textView7.setText("Name: \t" + nameE.toString());
                        }
                        else {
                            textView7.setText("Name: \t" +  " - ");
                        }
                        if(numberE!="null"&&numberE!="")
                        {
                            textView8.setText("Contact: \t" + numberE.toString());
                        }
                        else {
                            textView8.setText("Contact: \t" +  " - ");
                        }

                    } else {
                        // Data is not available, display placeholders
                        displayError();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle error
                    displayError();
                }
            });
        }



        return root;
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
