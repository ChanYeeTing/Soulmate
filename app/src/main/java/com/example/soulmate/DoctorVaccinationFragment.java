package com.example.soulmate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

public class DoctorVaccinationFragment extends Fragment {

    // Rename and change types of parameters
    private static final String ARG_HOSPITAL_NAME = "hospitalName";
    private String hospitalName;

    public DoctorVaccinationFragment() {
        // Required empty public constructor
    }

    public static DoctorVaccinationFragment newInstance(String hospitalName) {
        DoctorVaccinationFragment fragment = new DoctorVaccinationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_HOSPITAL_NAME, hospitalName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            hospitalName = getArguments().getString(ARG_HOSPITAL_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_doctor_vaccination, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (hospitalName != null) {
            TextView hospitalViewText = view.findViewById(R.id.hospitalTextView2);
            hospitalViewText.setText(hospitalName);

            DatabaseReference appointmentRef = FirebaseDatabase.getInstance().getReference().child("Appointment");

            // Query appointments for the specific hospital
            appointmentRef.child("Vaccination").child(hospitalName).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<String> vaccinationDetailsList = new ArrayList<>();
                    Date currentDate = Calendar.getInstance().getTime();

                    for (DataSnapshot dateSnapshot : dataSnapshot.getChildren()) {
                        String date = dateSnapshot.getKey();

                        for (DataSnapshot timeSnapshot : dateSnapshot.getChildren()) {
                            String time = timeSnapshot.getKey();

                            Map<String, Object> appointmentData = (Map<String, Object>) timeSnapshot.getValue();

                            if (appointmentData != null) {
                                String name = String.valueOf(appointmentData.get("name"));
                                String number = String.valueOf(appointmentData.get("number"));
                                String dateRecords = String.valueOf(appointmentData.get("date"));
                                String timeRecords = String.valueOf(appointmentData.get("time"));

                                // Check if any of the required fields are null
                                if (name != null && number != null && dateRecords != null && timeRecords != null) {
                                    try {
                                        // Parse appointment date and time
                                        Date appointmentDateTime = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
                                                .parse(dateRecords + " " + timeRecords);

                                        // Check if the appointment is in the future
                                        if (appointmentDateTime != null && appointmentDateTime.after(currentDate)) {
                                            // Build a string with appointment information
                                            String appointmentDetails = "\nDate: " + dateRecords + "\nTime: " + timeRecords +
                                                    "\nName: " + name + "\nPhone Number: " + number + "\n";
                                            vaccinationDetailsList.add(appointmentDetails);
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }

                    Collections.sort(vaccinationDetailsList, new Comparator<String>() {
                        @Override
                        public int compare(String s1, String s2) {
                            String dateTime1 = s1.substring(s1.indexOf("Date: ") + 6, s1.indexOf("\nTime: "));
                            String dateTime2 = s2.substring(s2.indexOf("Date: ") + 6, s2.indexOf("\nTime: "));

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
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, vaccinationDetailsList);
                    ListView vaccinationListView = view.findViewById(R.id.vaccinationListView);
                    vaccinationListView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors if needed
                }
            });

            // Adding buttons for navigation
            Button clinicHospitalButton = view.findViewById(R.id.appointmentRecord2);
            Button bloodDonationButton = view.findViewById(R.id.appointmentRecord3);
            Button logoutButton = view.findViewById(R.id.logoutButton3);
            clinicHospitalButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavController controller = Navigation.findNavController(v);
                    controller.navigate(R.id.action_doctorVaccinationFragment_to_doctorMainPageFragment);
                }
            });

            bloodDonationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavController controller = Navigation.findNavController(v);
                    controller.navigate(R.id.action_doctorVaccinationFragment_to_doctorBloodDonationFragment);
                }
            });

            logoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Sign out the user from Firebase Authentication
                    FirebaseAuth.getInstance().signOut();

                    // Navigate to the login fragment
                    NavController controller = Navigation.findNavController(v);
                    controller.navigate(R.id.action_doctorVaccinationFragment_to_doctorLoginFragment);
                }
            });
        }
    }
}
