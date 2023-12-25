package com.example.soulmate.ui.appointment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.soulmate.DatePickerFragment;
import com.example.soulmate.R;
import com.example.soulmate.VaccinationPopUpFragment;
import com.example.soulmate.main_page;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VaccinationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VaccinationFragment extends Fragment {

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
    String getName,getNumber;
    public static String getDate ;
    public static String check;
    String getTime;

    public VaccinationFragment () {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VaccinationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VaccinationFragment newInstance ( String param1, String param2 ) {
        VaccinationFragment fragment = new VaccinationFragment ();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vaccination, container, false);

        if (getActivity() instanceof main_page) {
            ((main_page) getActivity()).getSupportActionBar().setTitle("Vaccination Booking");
        }

        String[] hospitalNames = {"Island Hospital", "Clinic Medicris", "Hospital Lam Wah Ee", "Clinic Dr. Dashindar Singh",
                "Medivici Clinic & Surgery", "Clinic Putra Simpang Ampat", "Clinic Lim", "House Call Doctor", "Clinic Medilife"};

        Spinner hospitalSpinner = view.findViewById(R.id.hospital_spinner);

        ArrayAdapter<String> hospitalAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, hospitalNames);
        hospitalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        hospitalSpinner.setAdapter(hospitalAdapter);

        String[] vaccineNames = {"Chickenpox", "Dengue", "Diphtheria", "Flu(Influenza)", "Hepatitis A", "Hepatitis B"};

        Spinner vaccineSpinner = view.findViewById(R.id.vaccine_spinner);

        ArrayAdapter<String> vaccineAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, vaccineNames);
        vaccineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        vaccineSpinner.setAdapter(vaccineAdapter);

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
            reference.child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot> () {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult().exists()) {
                            DataSnapshot dataSnapshot = task.getResult();
                            String user = String.valueOf(dataSnapshot.child("name").getValue());
                            name.setText(user);
                            getName = user;
                            String contact = String.valueOf(dataSnapshot.child("Mobile Number").getValue());
                            number.setText(contact);
                            getNumber=contact;
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
        check = Selectdate.getText().toString().trim();
        enableButton();


        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                enableButton();
            }
            @Override
            public void afterTextChanged( Editable s) {
                check = Selectdate.getText().toString().trim();
                enableButton();
                getDate = Selectdate.getText().toString().trim();

            }
        };
        Selectdate.addTextChangedListener(textWatcher);
        Selectdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment ();
                datePicker.show(getActivity().getSupportFragmentManager(),"datePicker");

            }
        });



        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTime = button1.getText().toString().trim();
                VaccinationPopUpFragment showPopUp = new VaccinationPopUpFragment ();
                Bundle bundle = new Bundle();
                bundle.putString("name", getName);
                bundle.putString("number",getNumber);
                bundle.putString("date",getDate);
                bundle.putString("time",getTime);

                showPopUp.setArguments(bundle);
                showPopUp.show(((AppCompatActivity) requireActivity()).getSupportFragmentManager(), "showpopup");


            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTime = button2.getText().toString().trim();
                VaccinationPopUpFragment showPopUp = new VaccinationPopUpFragment ();
                Bundle bundle = new Bundle();
                bundle.putString("name", getName);
                bundle.putString("number",getNumber);
                bundle.putString("date",getDate);
                bundle.putString("time",getTime);

                showPopUp.setArguments(bundle);
                showPopUp.show(((AppCompatActivity) requireActivity()).getSupportFragmentManager(), "showpopup");
            }
        });
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTime = button3.getText().toString().trim();
                VaccinationPopUpFragment showPopUp = new VaccinationPopUpFragment ();
                Bundle bundle = new Bundle();
                bundle.putString("name", getName);
                bundle.putString("number",getNumber);
                bundle.putString("date",getDate);
                bundle.putString("time",getTime);

                showPopUp.setArguments(bundle);
                showPopUp.show(((AppCompatActivity) requireActivity()).getSupportFragmentManager(), "showpopup");
            }
        });
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTime = button4.getText().toString().trim();
                VaccinationPopUpFragment showPopUp = new VaccinationPopUpFragment ();
                Bundle bundle = new Bundle();
                bundle.putString("name", getName);
                bundle.putString("number",getNumber);
                bundle.putString("date",getDate);
                bundle.putString("time",getTime);

                showPopUp.setArguments(bundle);
                showPopUp.show(((AppCompatActivity) requireActivity()).getSupportFragmentManager(), "showpopup");
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTime = button5.getText().toString().trim();
                VaccinationPopUpFragment showPopUp = new VaccinationPopUpFragment ();
                Bundle bundle = new Bundle();
                bundle.putString("name", getName);
                bundle.putString("number",getNumber);
                bundle.putString("date",getDate);
                bundle.putString("time",getTime);

                showPopUp.setArguments(bundle);
                showPopUp.show(((AppCompatActivity) requireActivity()).getSupportFragmentManager(), "showpopup");
            }
        });
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTime = button6.getText().toString().trim();
                VaccinationPopUpFragment showPopUp = new VaccinationPopUpFragment ();
                Bundle bundle = new Bundle();
                bundle.putString("name", getName);
                bundle.putString("number",getNumber);
                bundle.putString("date",getDate);
                bundle.putString("time",getTime);

                showPopUp.setArguments(bundle);
                showPopUp.show(((AppCompatActivity) requireActivity()).getSupportFragmentManager(), "showpopup");
            }
        });
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTime = button7.getText().toString().trim();
                VaccinationPopUpFragment showPopUp = new VaccinationPopUpFragment ();
                Bundle bundle = new Bundle();
                bundle.putString("name", getName);
                bundle.putString("number",getNumber);
                bundle.putString("date",getDate);
                bundle.putString("time",getTime);

                showPopUp.setArguments(bundle);
                showPopUp.show(((AppCompatActivity) requireActivity()).getSupportFragmentManager(), "showpopup");
            }
        });
        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTime = button8.getText().toString().trim();
                VaccinationPopUpFragment showPopUp = new VaccinationPopUpFragment ();
                Bundle bundle = new Bundle();
                bundle.putString("name", getName);
                bundle.putString("number",getNumber);
                bundle.putString("date",getDate);
                bundle.putString("time",getTime);

                showPopUp.setArguments(bundle);
                showPopUp.show(((AppCompatActivity) requireActivity()).getSupportFragmentManager(), "showpopup");
            }
        });
        button9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getTime = button9.getText().toString().trim();
                VaccinationPopUpFragment showPopUp = new VaccinationPopUpFragment ();
                Bundle bundle = new Bundle();
                bundle.putString("name", getName);
                bundle.putString("number",getNumber);
                bundle.putString("date",getDate);
                bundle.putString("time",getTime);

                showPopUp.setArguments(bundle);
                showPopUp.show(((AppCompatActivity) requireActivity()).getSupportFragmentManager(), "showpopup");

            }
        });


    }
    private void enableButton() {

        Calendar calendar = Calendar.getInstance(
                TimeZone.getTimeZone("Asia/Kuala_Lumpur"));;
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        String todayDateMediumFormat = dateFormat.format(calendar.getTime());
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY); // 24-hour format

        if (check.equals(todayDateMediumFormat.trim()))
        {
            button1.setEnabled(!check.equals("Select Date")&&(9>currentHour));
            button2.setEnabled(!check.equals("Select Date")&&(10>currentHour));
            button3.setEnabled(!check.equals("Select Date")&&(11>currentHour));
            button4.setEnabled(!check.equals("Select Date")&&(12>currentHour));
            button5.setEnabled(!check.equals("Select Date")&&(14>currentHour));
            button6.setEnabled(!check.equals("Select Date")&&(15>currentHour));
            button7.setEnabled(!check.equals("Select Date")&&(16>currentHour));
            button8.setEnabled(!check.equals("Select Date")&&(17>currentHour));
            button9.setEnabled(!check.equals("Select Date")&&(18>currentHour));
        }
        else {
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

    }
}