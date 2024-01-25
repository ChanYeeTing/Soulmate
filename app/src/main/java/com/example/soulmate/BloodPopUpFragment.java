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
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class BloodPopUpFragment extends DialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    String name, number,hospital, date, time;
    TextView description;

    public BloodPopUpFragment () {
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
    public static BloodPopUpFragment newInstance ( String param1, String param2 ) {
        BloodPopUpFragment fragment = new BloodPopUpFragment ();
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
            hospital = bundle.getString("hospital");
            date = bundle.getString("date");
            time = bundle.getString("time");

        }


    }

    @Override
    public View onCreateView ( LayoutInflater inflater, ViewGroup container,
                               Bundle savedInstanceState ) {


        // Inflate the layout for this fragment
        return inflater.inflate ( R.layout.fragment_blood_pop_up, container, false );
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button yes = getView().findViewById(R.id.yesButton10);
        Button cancel = getView().findViewById(R.id.cancelButton10);
        description = getView().findViewById(R.id.textView10);
        String question = "Please confirm you intent to schedule your appointment for \n"+ date + " "+time;
        description.setText(question);



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
                Toast.makeText(getActivity(), "You may check the your appointment in the date tracking", Toast.LENGTH_LONG).show();
                String user_id = auth.getCurrentUser().getUid();
                String dateTime=date+" "+time;
                DatabaseReference userRef = ref.child("Activity").child(user_id).child(dateTime);
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("Appointment", "Blood Donation");
                hashMap.put("date", date);
                hashMap.put("hospital", hospital);
                hashMap.put("name", name);
                hashMap.put("number", number);
                hashMap.put("time", time);
                hashMap.put("z","-");
                userRef.setValue(hashMap);

                DatabaseReference check = ref.child("Appointment").child("Blood Donation")
                        .child(hospital).child(date).child(time).child(user_id);
                HashMap<String, Object> hash = new HashMap<>();
                hash.put("Appointment", "Blood Donation");
                hash.put("date", date);
                hash.put("hospital", hospital);
                hash.put("name", name);
                hash.put("number", number);
                hash.put("time", time);
                hash.put("z","-");
                check.setValue(hash);

                // Use the NavController obtained from NavHostFragment
                navController.navigate(R.id.action_popUpBlood_to_bloodDonationBooking);
                dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Use the NavController obtained from NavHostFragment
                navController.navigate(R.id.action_popUpBlood_to_bloodDonationBooking);
                dismiss ();
            }
        });

    }

}