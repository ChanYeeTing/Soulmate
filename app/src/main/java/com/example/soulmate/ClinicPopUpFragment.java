package com.example.soulmate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Random;

public class ClinicPopUpFragment extends DialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    String name, number, date, time;
    TextView description;

    public ClinicPopUpFragment () {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PopUpCallFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ClinicPopUpFragment newInstance ( String param1, String param2 ) {
        ClinicPopUpFragment fragment = new ClinicPopUpFragment ();
        Bundle args = new Bundle ();
        args.putString ( ARG_PARAM1, param1 );
        args.putString ( ARG_PARAM2, param2 );
        fragment.setArguments ( args );
        return fragment;
    }

    @Override
    public void onCreate ( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        //receive data from booking fragment
        Bundle bundle = getArguments();
        if (bundle != null) {
            name = bundle.getString("name");
            number = bundle.getString("number");
            date = bundle.getString("date");
            time = bundle.getString("time");

        }


    }

    @Override
    public View onCreateView ( LayoutInflater inflater, ViewGroup container,
                               Bundle savedInstanceState ) {


        // Inflate the layout for this fragment
        return inflater.inflate ( R.layout.fragment_clinic_pop_up, container, false );
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button yes = getView().findViewById(R.id.yesButton8);
        Button cancel = getView().findViewById(R.id.cancelButton8);
        description = getView().findViewById(R.id.textView4);
        String question = "Please confirm you intent to schedule your appointment for \n"+ date + " "+time;
        description.setText(question);

//        String roomId = generateRandomId();

        // Use requireView() to get the View associated with the Fragment
        View fragmentView = requireView();

        // Use NavHostFragment.findNavController to get the correct NavController
        NavController navController = NavHostFragment.findNavController(this);

        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference ref = firebaseDatabase.getReference();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //save into database
                Toast.makeText(getActivity(), "Booking Successful", Toast.LENGTH_SHORT).show();
                String user_id = auth.getCurrentUser().getUid();
                String dateTime=date+" "+time;
                DatabaseReference userRef = ref.child("Activity").child(user_id).child("Clinic/Hospital").child(dateTime);
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("name", name);
                hashMap.put("number", number);
                hashMap.put("date", date);
                hashMap.put("time", time);
//                hashMap.put("RoomId", roomId);
                userRef.setValue(hashMap);


                // Use the NavController obtained from NavHostFragment
                navController.navigate(R.id.action_popUpFragment_to_nav_date_tracking);
                dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Use the NavController obtained from NavHostFragment
                navController.navigate(R.id.action_popUpClinic_to_clinicBooking);
                dismiss ();
            }
        });

    }
/*    private String generateRandomId() {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for (int i = 0; i < 10; i++) {
            int randomIndex = random.nextInt(allowedChars.length());
            char randomChar = allowedChars.charAt(randomIndex);
            stringBuilder.append(randomChar);
        }
        return stringBuilder.toString();
    }*/
}