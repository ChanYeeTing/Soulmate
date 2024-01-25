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

public class AdminTelemedicineFragment extends Fragment {

    private ListView telemedicineListView;
    private DatabaseReference telemedicineReference;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_telemedicine, container, false);

        telemedicineListView = view.findViewById(R.id.telemedicineListView);
        telemedicineReference = FirebaseDatabase.getInstance().getReference().child("Activity");

        // Add a ValueEventListener to fetch telemedicine information
        telemedicineReference.addValueEventListener(new ValueEventListener() {
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
        // Create a list to store telemedicine information
        List<String> telemedicineList = new ArrayList<>();

        Date currentDate = Calendar.getInstance().getTime();

        for (DataSnapshot telemedicineSnapshot : dataSnapshot.getChildren()) {
            String uid = telemedicineSnapshot.getKey();

            for (DataSnapshot timeSnapshot : telemedicineSnapshot.getChildren()) {
                String timeAppointment = timeSnapshot.getKey();
                Map<String, Object> telemedicineData = (Map<String, Object>) timeSnapshot.getValue();

                if (telemedicineData != null && uid != null) {
                    String appointment = String.valueOf(telemedicineData.get("Appointment"));

                    if ("Telemedicine".equals(appointment)) {
                        String name = String.valueOf(telemedicineData.get("name"));
                        String number = String.valueOf(telemedicineData.get("number"));
                        String zid = String.valueOf(telemedicineData.get("zId"));
                        String date = String.valueOf(telemedicineData.get("date"));
                        String time = String.valueOf(telemedicineData.get("time"));

                        // Build a string with telemedicine information
                        String telemedicineInfo = "\nTime: " + date + " (" + time + ") " + "\nMeeting ID: " + zid
                                + "\nUser ID: " + uid + "\nName: " + name + "\nPhone Number: " + number + "\n";

                        try {
                            // Parse telemedicine date and time
                            Date telemedicineDateTime = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
                                    .parse(date + " " + time);

                            // Check if the telemedicine is in the future
                            if (telemedicineDateTime != null && telemedicineDateTime.after(currentDate)) {
                                telemedicineList.add(telemedicineInfo);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        Collections.sort(telemedicineList, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                // Extract date and time from the strings and compare
                String dateTime1 = s1.substring(s1.indexOf("Time: ") + 6, s1.indexOf("\nMeeting ID:"));
                String dateTime2 = s2.substring(s2.indexOf("Time: ") + 6, s2.indexOf("\nMeeting ID:"));

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
                android.R.layout.simple_list_item_1, telemedicineList);

        telemedicineListView.setAdapter(adapter);
    }


    @Override
    public void onViewCreated( @NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated ( view, savedInstanceState );

        Button backButton = getView ().findViewById ( R.id.backButton6);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_adminTelemedicineFragment_to_adminAppointmentFragment);
            }
        } );
    }
}