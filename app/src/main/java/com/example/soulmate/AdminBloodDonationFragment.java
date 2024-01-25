package com.example.soulmate;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AdminBloodDonationFragment extends Fragment {

    private ListView bloodDonationListView;
    private DatabaseReference bloodDonationReference;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_blood_donation, container, false);

        bloodDonationListView = view.findViewById(R.id.bloodDonationListView);
        bloodDonationReference = FirebaseDatabase.getInstance().getReference().child("Activity");

        // Add a ValueEventListener to fetch blood donation information
        bloodDonationReference.addValueEventListener(new ValueEventListener() {
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
        // Create a list to store blood donation information
        List<String> bloodDonationList = new ArrayList<>();

        Date currentDate = Calendar.getInstance().getTime();

        for (DataSnapshot uidSnapshot : dataSnapshot.getChildren()) {
            String uid = uidSnapshot.getKey();

            for (DataSnapshot timeSnapshot : uidSnapshot.getChildren()) {
                String timeAppointment = timeSnapshot.getKey();
                Map<String, Object> donorData = (Map<String, Object>) timeSnapshot.getValue();

                if (donorData != null && uid != null) {
                    String appointment = String.valueOf(donorData.get("Appointment"));

                    if ("Blood Donation".equals(appointment)) {
                        String name = String.valueOf(donorData.get("name"));
                        String number = String.valueOf(donorData.get("number"));
                        String hospital = String.valueOf(donorData.get("hospital"));
                        String date = String.valueOf(donorData.get("date"));
                        String time = String.valueOf(donorData.get("time"));

                        // Build a string with blood donation information
                        String donationInfo = "\nTime: " + date + " (" + time + ") " + "\nVenue: " + hospital
                                + "\nUser ID: " + uid + "\nName: " + name + "\nPhone Number: " + number + "\n";

                        try {
                            // Parse donation date and time
                            Date donationDateTime = new SimpleDateFormat("dd-MM-yyyy (HH:mm)", Locale.getDefault())
                                    .parse(date + " (" + time + ")");

                            // Check if the donation is in the future
                            if (donationDateTime != null && donationDateTime.after(currentDate)) {
                                bloodDonationList.add(donationInfo);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        Collections.sort(bloodDonationList, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                // Extract date and time from the strings and compare
                // Assuming dateTime is in "dd-MM-yyyy (HH:mm)" format
                String dateTime1 = s1.substring(s1.indexOf("Time: ") + 6, s1.indexOf("\nVenue: "));
                String dateTime2 = s2.substring(s2.indexOf("Time: ") + 6, s2.indexOf("\nVenue: "));

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy (HH:mm)", Locale.getDefault());
                try {
                    Date date1 = sdf.parse(dateTime1);
                    Date date2 = sdf.parse(dateTime2);
                    return date1.compareTo(date2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                return 0; // Default return if parsing fails
            }
        });

        // Create an adapter to populate the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_list_item_1, bloodDonationList);

        bloodDonationListView.setAdapter(adapter);
    }


    @Override
    public void onViewCreated( @NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated ( view, savedInstanceState );

        Button backButton = getView ().findViewById ( R.id.backButton3);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_adminBloodDonationFragment_to_adminAppointmentFragment);
            }
        } );
    }
}