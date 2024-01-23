package com.example.soulmate;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TeleDoctorMainPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeleDoctorMainPageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<TeleDataModel> Telemedicine;
    private TeleCustomAdapter arrayAdapter;


    public TeleDoctorMainPageFragment () {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TeleDoctorMainPageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TeleDoctorMainPageFragment newInstance ( String param1, String param2 ) {
        TeleDoctorMainPageFragment fragment = new TeleDoctorMainPageFragment ();
        Bundle args = new Bundle ();
        args.putString ( ARG_PARAM1, param1 );
        args.putString ( ARG_PARAM2, param2 );
        fragment.setArguments ( args );
        return fragment;
    }

    @Override
    public void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        if (getArguments () != null) {
            mParam1 = getArguments ().getString ( ARG_PARAM1 );
            mParam2 = getArguments ().getString ( ARG_PARAM2 );
        }
    }

    @Override
    public View onCreateView ( LayoutInflater inflater, ViewGroup container,
                               Bundle savedInstanceState ) {
        // Inflate the layout for this fragment
        return inflater.inflate ( R.layout.fragment_tele_doctor_main_page, container, false );
    }

    @Override
    public void onViewCreated ( @NonNull View view, @Nullable Bundle savedInstanceState ) {
        super.onViewCreated ( view, savedInstanceState );

        Button logoutButton = view.findViewById(R.id.logoutButton4);
        Button meeting = view.findViewById(R.id.teleMeet);
        meeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_teleDoctorMainPageFragment_to_teleRoom2);
            }
        });
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sign out the user from Firebase Authentication
                FirebaseAuth.getInstance().signOut();

                // Navigate to the login fragment
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_teleDoctorMainPageFragment_to_teleDoctorLoginFragment);
            }
        });
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



        ListView updated = getView().findViewById(R.id.telemedicineList);
        Telemedicine = new ArrayList<>();

        TimeZone specificTimeZone = TimeZone.getTimeZone("Asia/Kuala_Lumpur");
        Calendar calendar = Calendar.getInstance(specificTimeZone);;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String todayDateFormat = dateFormat.format(calendar.getTime());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());

        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        timeFormat.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

        String currentHour = timeFormat.format(calendar.getTime());


        arrayAdapter = new TeleCustomAdapter(getActivity(),Telemedicine);
        updated.setAdapter(arrayAdapter);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Appointment");

        //Telemedicine
        databaseReference.child("Telemedicine").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Telemedicine.clear();

                for(DataSnapshot snapshot1: snapshot.getChildren()) {
                    String childKey = snapshot1.getKey();

                    DataSnapshot snapshotDate = snapshot.child(childKey);

                    for (DataSnapshot snapshotTime : snapshotDate.getChildren()) {
                        String Key = snapshotTime.getKey();
                        DataSnapshot snapshotData = snapshot1.child(Key);

                        for (DataSnapshot snapshotUid : snapshotData.getChildren()) {
                            String uidKey = snapshotUid.getKey();

                            DataSnapshot snapshotDetail = snapshotTime.child(uidKey);


                        if (snapshotDetail.exists()) {
                            int i = 0;
                            String[] value = new String[7];
                            for (DataSnapshot snapshot2 : snapshotDetail.getChildren()) {
                                value[i] = String.valueOf(snapshot2.getValue());
                                i++;

                            }
                            // Current date
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                                LocalDate currentDate = LocalDate.now();
                                LocalDate dateFromString = LocalDate.parse(value[1], formatter);

                                // Date from the string "22-12-2023"
                                if (dateFromString.isAfter(currentDate)) {
                                    TeleDataModel dataModel = new TeleDataModel(value[1], value[5], value[3], value[4], value[6]);
                                    Telemedicine.add(dataModel);
                                    Log.d("DataModel", "Added DataModel: " + dataModel.toString()); // Log for debugging
                                } else if (value[1].equals(todayDateFormat)) {
                                    try {
                                        if (timeFormat.parse(value[5]).before(timeFormat.parse(currentHour))) {

                                        } else {
                                            TeleDataModel dataModel = new TeleDataModel(value[1], value[5], value[3], value[4], value[6]);
                                            Telemedicine.add(dataModel);
                                        }
                                    } catch (ParseException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            }
                        } else
                            Toast.makeText(getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                    }
                    arrayAdapter.notifyDataSetChanged();
                    if (updated.getAdapter() == null || updated.getAdapter().getCount() == 0) {
                        updated.setVisibility(View.GONE);
                    } else {
                        updated.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });









    }

}