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

public class AdminVaccinationFragment extends Fragment {

    private ListView vaccinationListView;
    private DatabaseReference vaccinationReference;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_vaccination, container, false);

        vaccinationListView = view.findViewById(R.id.vaccinationListView);
        vaccinationReference = FirebaseDatabase.getInstance().getReference().child("Activity");

        // Add a ValueEventListener to fetch vaccination information
        vaccinationReference.addValueEventListener(new ValueEventListener() {
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
        // Create a list to store vaccination information
        List<String> vaccinationList = new ArrayList<>();

        Date currentDate = Calendar.getInstance().getTime();

        for (DataSnapshot vaccinationSnapshot : dataSnapshot.getChildren()) {
            String uid = vaccinationSnapshot.getKey();

            for (DataSnapshot timeSnapshot : vaccinationSnapshot.getChildren()) {
                String timeAppointment = timeSnapshot.getKey();
                Map<String, Object> vaccinationData = (Map<String, Object>) timeSnapshot.getValue();

                if (vaccinationData != null && uid != null ) {
                    String appointment = String.valueOf(vaccinationData.get("Appointment"));

                    if ("Vaccination".equals(appointment)) {
                        String name = String.valueOf(vaccinationData.get("name"));
                        String number = String.valueOf(vaccinationData.get("number"));
                        String hospital = String.valueOf(vaccinationData.get("hospital"));
                        String date = String.valueOf(vaccinationData.get("date"));
                        String time = String.valueOf(vaccinationData.get("time"));
                        String vaccine = String.valueOf(vaccinationData.get("vaccine"));

                        // Build a string with clinicHospital information
                        String vaccinationInfo = "\nTime: " + date + " (" + time + ") " + "\nVenue: " + hospital + "\nVaccine Type: " + vaccine
                                + "\nUser ID: " + uid + "\nName: " + name + "\nPhone Number: " + number + "\n";

                        try {
                            // Parse appointment date and time
                            Date appointmentDateTime = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
                                    .parse(date + " " + time);

                            // Check if the appointment is in the future
                            if (appointmentDateTime != null && appointmentDateTime.after(currentDate)) {
                                vaccinationList.add(vaccinationInfo);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        Collections.sort(vaccinationList, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                String dateTime1 = "";
                String time1 = "";

                if (s1.contains("Date:") && s1.contains("Time:")) {
                    dateTime1 = s1.substring(s1.indexOf("Date: ") + 6, s1.indexOf("\nTime: "));
                    time1 = s1.substring(s1.indexOf("Time: ") + 6, s1.indexOf("\nName: "));
                }

                String dateTime2 = "";
                String time2 = "";

                if (s2.contains("Date:") && s2.contains("Time:")) {
                    dateTime2 = s2.substring(s2.indexOf("Date: ") + 6, s2.indexOf("\nTime: "));
                    time2 = s2.substring(s2.indexOf("Time: ") + 6, s2.indexOf("\nName: "));
                }


                // Assuming dateTime is in "yyyy-MM-dd" and time is in "HH:mm" format, adjust the parsing logic
                SimpleDateFormat sdfDateTime = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
                try {
                    Date date1 = sdfDateTime.parse(dateTime1 + " " + time1);
                    Date date2 = sdfDateTime.parse(dateTime2 + " " + time2);
                    return date1.compareTo(date2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                return 0; // Default return if parsing fails
            }
        });


        // Create an adapter to populate the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_list_item_1, vaccinationList);

        vaccinationListView.setAdapter(adapter);
    }

    @Override
    public void onViewCreated( @NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated ( view, savedInstanceState );

        Button backButton = getView ().findViewById ( R.id.backButton7);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_adminVaccinationFragment_to_adminAppointmentFragment);
            }
        } );
    }
}