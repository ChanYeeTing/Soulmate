//package com.example.soulmate;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.lifecycle.ViewModelProvider;
//import androidx.navigation.NavController;
//import androidx.navigation.Navigation;
//
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.Date;
//import java.util.List;
//import java.util.Locale;
//import java.util.Map;
//
//public class DoctorMainPageFragment extends Fragment {
//
//    private static final String ARG_HOSPITAL_NAME = "hospitalName";
//    private static String hospitalName;
//    private SharedViewModel viewModel;
//    public DoctorMainPageFragment() {
//        // Required empty public constructor
//    }
//
//    public static DoctorMainPageFragment newInstance(String hospitalName) {
//        DoctorMainPageFragment fragment = new DoctorMainPageFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_HOSPITAL_NAME, hospitalName);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_doctor_main_page, container, false);
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        SharedViewModel viewModel1 = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
//        TextView hospitalViewText = view.findViewById(R.id.hospitalTextView);
//
//        // Observe the LiveData
//        String data = viewModel1.getData();
//        // Update UI with the shared data
//        hospitalViewText.setText(data);
//        hospitalName = data;
//        viewModel1.setSharedData(hospitalName);
//
//        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("Users");
//        DatabaseReference appointmentRef = FirebaseDatabase.getInstance().getReference().child("Appointment");
//
//        // Retrieve the user ID from the Users node
//        userRef.child(userId).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot userSnapshot) {
//                for (DataSnapshot userUidSnapshot : userSnapshot.getChildren()) {
//                    String userId = userUidSnapshot.getKey();
//
//                    // Query appointments for the specific user ID
//                    appointmentRef.child("ClinicHospital").child(hospitalName).addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot appointmentSnapshot) {
//                            List<String> appointmentDetailsList = new ArrayList<>();
//                            Date currentDate = Calendar.getInstance().getTime();
//
//                            for (DataSnapshot dateSnapshot : appointmentSnapshot.getChildren()) {
//                                String date = dateSnapshot.getKey();
//
//                                for (DataSnapshot timeSnapshot : dateSnapshot.getChildren()) {
//                                    for (DataSnapshot uidSnapshot : timeSnapshot.getChildren()) {
//                                        String uid = uidSnapshot.getKey();
//
//                                        Map<String, Object> appointmentData = (Map<String, Object>) uidSnapshot.getValue();
//
//                                        if (appointmentData != null && uid.equals(userId)) {
//                                            String name = String.valueOf(appointmentData.get("name"));
//                                            String number = String.valueOf(appointmentData.get("number"));
//                                            String dateRecords = String.valueOf(appointmentData.get("date"));
//                                            String timeRecords = String.valueOf(appointmentData.get("time"));
//
//                                            // Check if any of the required fields are null
//                                            if (name != null && number != null && dateRecords != null && timeRecords != null) {
//                                                try {
//                                                    // Parse appointment date and time
//                                                    Date appointmentDateTime = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
//                                                            .parse(dateRecords + " " + timeRecords);
//
//                                                    // Check if the appointment is in the future
//                                                    if (appointmentDateTime != null && appointmentDateTime.after(currentDate)) {
//                                                        // Build a string with appointment information
//                                                        String appointmentDetails = "\nDate: " + dateRecords + "\nTime: " + timeRecords +
//                                                                "\nName: " + name + "\nPhone Number: " + number + "\n";
//                                                        appointmentDetailsList.add(appointmentDetails);
//                                                    }
//                                                } catch (ParseException e) {
//                                                    e.printStackTrace();
//                                                }
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//
//                            Collections.sort(appointmentDetailsList, new Comparator<String>() {
//                                @Override
//                                public int compare(String s1, String s2) {
//                                    String dateTime1 = s1.substring(s1.indexOf("Date: ") + 6, s1.indexOf("\nTime: "));
//                                    String dateTime2 = s2.substring(s2.indexOf("Date: ") + 6, s2.indexOf("\nTime: "));
//
//                                    // Assuming dateTime is in "dd-MM-yyyy HH:mm" format, adjust the parsing logic
//                                    SimpleDateFormat sdfDateTime = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
//                                    try {
//                                        Date date1 = sdfDateTime.parse(dateTime1);
//                                        Date date2 = sdfDateTime.parse(dateTime2);
//                                        return date1.compareTo(date2);
//                                    } catch (ParseException e) {
//                                        e.printStackTrace();
//                                    }
//
//                                    return 0; // Default return if parsing fails
//                                }
//                            });
//
//                            // Create an adapter to populate the ListView
//                            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, appointmentDetailsList);
//                            ListView appointmentListView = view.findViewById(R.id.appointmentListView);
//                            appointmentListView.setAdapter(adapter);
//                        }
//
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//                            // Handle errors if needed
//                        }
//                    });
//                }
//
//                // Adding buttons for navigation
//                Button vaccinationButton = view.findViewById(R.id.appointmentRecord7);
//                Button bloodDonationButton = view.findViewById(R.id.appointmentRecord6);
//                Button logoutButton = view.findViewById(R.id.logoutButton);
//                vaccinationButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        NavController controller = Navigation.findNavController(v);
//                        controller.navigate(R.id.action_doctorMainPageFragment_to_doctorVaccinationFragment);
//                    }
//                });
//
//                bloodDonationButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        NavController controller = Navigation.findNavController(v);
//                        controller.navigate(R.id.action_doctorMainPageFragment_to_doctorBloodDonationFragment);
//                    }
//                });
//
//                logoutButton.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // Sign out the user from Firebase Authentication
//                        FirebaseAuth.getInstance().signOut();
//
//                        // Navigate to the login fragment
//                        NavController controller = Navigation.findNavController(v);
//                        controller.navigate(R.id.action_doctorMainPageFragment_to_doctorLoginFragment);
//                    }
//                });
//
//    });
//        }
//    }
//}

package com.example.soulmate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class DoctorMainPageFragment extends Fragment {

    private static final String ARG_HOSPITAL_NAME = "hospitalName";
    private static String hospitalName;
    private SharedViewModel viewModel;
    public DoctorMainPageFragment() {
        // Required empty public constructor
    }

    public static DoctorMainPageFragment newInstance(String hospitalName) {
        DoctorMainPageFragment fragment = new DoctorMainPageFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_doctor_main_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedViewModel viewModel1 = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        TextView hospitalViewText = view.findViewById(R.id.hospitalTextView);



        // Observe the LiveData
        String data = viewModel1.getData();
        // Update UI with the shared data
        hospitalViewText.setText(data);
        hospitalName = data;
        viewModel1.setSharedData(hospitalName);



        if (hospitalName != null) {
            DatabaseReference appointmentRef = FirebaseDatabase.getInstance().getReference().child("Appointment");

            // Query appointments for the specific hospital
            appointmentRef.child("ClinicHospital").child(hospitalName).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<String> appointmentDetailsList = new ArrayList<>();
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
                                            appointmentDetailsList.add(appointmentDetails);
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }

                    Collections.sort(appointmentDetailsList, new Comparator<String>() {
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
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, appointmentDetailsList);
                    ListView appointmentListView = view.findViewById(R.id.appointmentListView);
                    appointmentListView.setAdapter(adapter);
                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors if needed
                }
            });
        }

        else {
            Toast.makeText(getActivity(), "Something wrong", Toast.LENGTH_SHORT).show();
        }


        // Adding buttons for navigation
        Button vaccinationButton = view.findViewById(R.id.appointmentRecord7);
        Button bloodDonationButton = view.findViewById(R.id.appointmentRecord6);
        Button logoutButton = view.findViewById(R.id.logoutButton);
        vaccinationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_doctorMainPageFragment_to_doctorVaccinationFragment);
            }
        });

        bloodDonationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_doctorMainPageFragment_to_doctorBloodDonationFragment);
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sign out the user from Firebase Authentication
                FirebaseAuth.getInstance().signOut();

                // Navigate to the login fragment
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_doctorMainPageFragment_to_doctorLoginFragment);
            }
        });

    }
}