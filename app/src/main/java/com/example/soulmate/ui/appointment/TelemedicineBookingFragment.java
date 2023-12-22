package com.example.soulmate.ui.appointment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.soulmate.DatePickerFragment;
import com.example.soulmate.R;
import com.example.soulmate.main_page;
import com.example.soulmate.timeslot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TelemedicineBookingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TelemedicineBookingFragment extends Fragment implements android.app.DatePickerDialog.OnDateSetListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DatePickerDialog.OnDateSetListener DateSetListener;
    TextView Selectdate;
    TextView name;
    TextView number;
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    Button button5;
    Button button6;
    Button button7;
    Button button8;
    Button button9;
    String choice ;

    public TelemedicineBookingFragment () {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TelemedicineBookingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TelemedicineBookingFragment newInstance ( String param1, String param2 ) {
        TelemedicineBookingFragment fragment = new TelemedicineBookingFragment ();
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
        View view = inflater.inflate ( R.layout.fragment_telemedicine_booking, container, false );

        if (getActivity() instanceof main_page) {
            ((main_page) getActivity()).getSupportActionBar().setTitle("Telemedicine Booking");
        }
        
        return view;
    }


    @Override
    public void onActivityCreated ( @Nullable Bundle savedInstanceState ) {
        super.onActivityCreated(savedInstanceState);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser(); // Check for null here
        if (currentUser != null) {
            String uid = currentUser.getUid();

            //set username
            name = getView().findViewById(R.id.name);
            number = getView().findViewById(R.id.number);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult().exists()) {
                            DataSnapshot dataSnapshot = task.getResult();
                            String user = String.valueOf(dataSnapshot.child("name").getValue());
                            name.setText(user);
                            String contact = String.valueOf(dataSnapshot.child("Mobile Number").getValue());
                            number.setText(contact);
                        }
                    }
                }
            });

        }

        Selectdate= getView().findViewById(R.id.date);
        button1= getView().findViewById(R.id.button1);
        button2= getView().findViewById(R.id.button2);
        button3= getView().findViewById(R.id.button3);
        button4= getView().findViewById(R.id.button4);
        button5= getView().findViewById(R.id.button5);
        button6= getView().findViewById(R.id.button6);
        button7= getView().findViewById(R.id.button7);
        button8= getView().findViewById(R.id.button8);
        button9= getView().findViewById(R.id.button9);

        enableButton();



        Selectdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getActivity().getSupportFragmentManager(),"datePicker");

            }
        });



        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                enableButton();
            }
        };
        Selectdate.addTextChangedListener(textWatcher);

        final Bundle bundle = this.getArguments();
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeslot showPopUp = new timeslot();
                showPopUp.show(((AppCompatActivity) requireActivity()).getSupportFragmentManager(), "showpopup");
                if (bundle != null) {
                    choice  = bundle.getString("choice");

                }
            }
        });


    }

    private void enableButton() {
        String check = Selectdate.getText().toString().trim();
        button1.setEnabled(!check.equals("Select Date"));
        button2.setEnabled(!check.equals("Select Date"));
        button3.setEnabled(!check.equals("Select Date"));
        button4.setEnabled(!check.equals("Select Date"));
        button5.setEnabled(!check.equals("Select Date"));
        button6.setEnabled(!check.equals("Select Date"));
        button7.setEnabled(!check.equals("Select Date"));
        button8.setEnabled(!check.equals("Select Date"));
        button9.setEnabled(!check.equals("Select Date"));
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDataString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        Selectdate.setText(currentDataString);
    }
}