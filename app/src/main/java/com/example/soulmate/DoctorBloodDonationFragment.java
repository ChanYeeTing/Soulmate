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
import androidx.lifecycle.ViewModelProvider;
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

public class DoctorBloodDonationFragment extends Fragment {

    // Rename and change types of parameters
    private static final String ARG_HOSPITAL_NAME = "hospitalName";
    private String hospitalName;
    TextView hospitalViewText;
    private SharedViewModel viewModel;
    public DoctorBloodDonationFragment() {
        // Required empty public constructor
    }

    public static DoctorBloodDonationFragment newInstance(String hospitalName) {
        DoctorBloodDonationFragment fragment = new DoctorBloodDonationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_HOSPITAL_NAME, hospitalName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_doctor_blood_donation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedViewModel viewModel1 = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        hospitalViewText = view.findViewById(R.id.hospitalTextView1);

        String data = viewModel.getData();
        // Update UI with the shared data
        if(data!=null){
            hospitalViewText.setText(data);
            hospitalName = data;
        }
        viewModel1.setSharedData(hospitalName);



        if (hospitalName != null) {
            DatabaseReference donationRef = FirebaseDatabase.getInstance().getReference().child("Appointment");

            // Query blood donation records for the specific hospital
            donationRef.child("Blood Donation").child(hospitalName).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<String> donationDetailsList = new ArrayList<>();
                    Date currentDate = Calendar.getInstance().getTime();

                    for (DataSnapshot dateSnapshot : dataSnapshot.getChildren()) {
                        String date = dateSnapshot.getKey();

                        for (DataSnapshot timeSnapshot : dateSnapshot.getChildren()) {
                            String time = timeSnapshot.getKey();

                            for (DataSnapshot uidSnapshot : timeSnapshot.getChildren()) {
                                String uid = uidSnapshot.getKey();

                                Map<String, Object> appointmentData = (Map<String, Object>) timeSnapshot.getValue();

                                if (appointmentData != null && uid != null) {
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
                                                String donationDetails = "\nDate: " + dateRecords + "\nTime: " + timeRecords +
                                                        "\nName: " + name + "\nPhone Number: " + number + "\n";
                                                donationDetailsList.add(donationDetails);
                                            }
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Collections.sort(donationDetailsList, new Comparator<String>() {
                        @Override
                        public int compare(String s1, String s2) {
                            String dateTime1 = s1.substring(s1.indexOf("Donation Date: ") + 15);
                            String dateTime2 = s2.substring(s2.indexOf("Donation Date: ") + 15);

                            // Assuming dateTime is in "dd-MM-yyyy" format, adjust the parsing logic
                            SimpleDateFormat sdfDateTime = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
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
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, donationDetailsList);
                    ListView donationListView = view.findViewById(R.id.blooddonationListView);
                    donationListView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors if needed
                }
            });
            }
            // Adding buttons for navigation
            Button clinicHospitalButton = view.findViewById(R.id.appointmentRecord8);
            Button vaccinationButton = view.findViewById(R.id.appointmentRecord10);
            Button logoutButton = view.findViewById(R.id.logoutButton2);
            clinicHospitalButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavController controller = Navigation.findNavController(v);
                    controller.navigate(R.id.action_doctorBloodDonationFragment_to_doctorMainPageFragment);
                }
            });

            vaccinationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavController controller = Navigation.findNavController(v);
                    controller.navigate(R.id.action_doctorBloodDonationFragment_to_doctorVaccinationFragment);
                }
            });

            logoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Sign out the user from Firebase Authentication
                    FirebaseAuth.getInstance().signOut();

                    // Navigate to the login fragment
                    NavController controller = Navigation.findNavController(v);
                    controller.navigate(R.id.action_doctorBloodDonationFragment_to_doctorLoginFragment);
                }
            });

    }
}
