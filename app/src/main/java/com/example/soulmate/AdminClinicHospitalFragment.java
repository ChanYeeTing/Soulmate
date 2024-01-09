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

public class AdminClinicHospitalFragment extends Fragment {

    private ListView clinicHospitalListView;
    private DatabaseReference clinicHospitalReference;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_clinic_hospital, container, false);

        clinicHospitalListView = view.findViewById(R.id.clinicHospitalListView);
        clinicHospitalReference = FirebaseDatabase.getInstance().getReference().child("Activity");

        // Add a ValueEventListener to fetch clinicHospital information
        clinicHospitalReference.addValueEventListener(new ValueEventListener() {
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
        // Create a list to store clinicHospital information
        List<String> clinicHospitalList = new ArrayList<>();

        Date currentDate = Calendar.getInstance().getTime();

        for (DataSnapshot clinicHospitalSnapshot : dataSnapshot.getChildren()) {
            String uid = clinicHospitalSnapshot.getKey();

            for (DataSnapshot timeSnapshot : clinicHospitalSnapshot.getChildren()) {
                String timeAppointment = timeSnapshot.getKey();
                Map<String, Object> clinicHospitalData = (Map<String, Object>) timeSnapshot.getValue();

                if (clinicHospitalData != null) {
                    String appointment = String.valueOf(clinicHospitalData.get("Appointment"));

                    if ("Clinic and Hospital".equals(appointment)) {
                        String name = String.valueOf(clinicHospitalData.get("name"));
                        String number = String.valueOf(clinicHospitalData.get("number"));
                        String hospital = String.valueOf(clinicHospitalData.get("hospital"));
                        String date = String.valueOf(clinicHospitalData.get("date"));
                        String time = String.valueOf(clinicHospitalData.get("time"));

                        // Build a string with clinicHospital information
                        String clinicHospitalInfo = "\nTime: " + date + " (" + time + ") " + "\nVenue: " + hospital
                                + "\nUser ID: " + uid + "\nName: " + name + "\nPhone Number: " + number + "\n";

                        try {
                            // Parse clinicHospital date and time
                            Date clinicHospitalDateTime = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
                                    .parse(date + " " + time);

                            // Check if the clinicHospital is in the future
                            if (clinicHospitalDateTime != null && clinicHospitalDateTime.after(currentDate)) {
                                clinicHospitalList.add(clinicHospitalInfo);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        Collections.sort(clinicHospitalList, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                String dateTime1 = s1.substring(s1.indexOf("Time: ") + 6, s1.indexOf("\nVenue: "));
                String dateTime2 = s2.substring(s2.indexOf("Time: ") + 6, s2.indexOf("\nVenue: "));

                // Assuming dateTime is in "dd-MM-yyyy HH:mm" format, adjust the parsing logic
                SimpleDateFormat sdfDateTime = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
                try {
                    Date date1 = sdfDateTime.parse(dateTime1);
                    Date date2 = sdfDateTime.parse(dateTime2);
                    return date1.compareTo(date2);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                return 0; // Default return if parsing fails
            }
        });

        // Create an adapter to populate the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_list_item_1, clinicHospitalList);

        clinicHospitalListView.setAdapter(adapter);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button backButton = getView().findViewById(R.id.backButton5);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_adminClinicHospitalFragment_to_adminAppointmentFragment);
            }
        });
    }
}
