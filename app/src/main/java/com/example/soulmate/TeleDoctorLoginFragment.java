package com.example.soulmate;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TeleDoctorLoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeleDoctorLoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    EditText emailDoctorEditText,passDoctorEditText, nameEditText;
    Button loginTele;
    private DatabaseReference TeleDatabase;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TeleDoctorLoginFragment () {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TeleDoctorLoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TeleDoctorLoginFragment newInstance ( String param1, String param2 ) {
        TeleDoctorLoginFragment fragment = new TeleDoctorLoginFragment ();
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
        TeleDatabase = FirebaseDatabase.getInstance().getReference().child("Doctor");
    }

    @Override
    public View onCreateView ( LayoutInflater inflater, ViewGroup container,
                               Bundle savedInstanceState ) {
        // Inflate the layout for this fragment
        return inflater.inflate ( R.layout.fragment_tele_doctor_login, container, false );
    }

    @Override
    public void onViewCreated ( @NonNull View view, @Nullable Bundle savedInstanceState ) {
        super.onViewCreated ( view, savedInstanceState );

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        nameEditText = getView().findViewById(R.id.editTextname3);
        emailDoctorEditText = getView().findViewById(R.id.editTextTextEmailAddress3);
        passDoctorEditText = getView().findViewById(R.id.editTextPassword3);
        loginTele = getView().findViewById(R.id.loginButton3);

        loginTele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString().trim();
                String email = emailDoctorEditText.getText().toString().trim();
                String password = passDoctorEditText.getText().toString().trim();

                if (validateInputs(name, email, password)) {
                    // Check doctor credentials in the Firebase Realtime Database
                    checkDoctorCredentials(name, email, password, v);
                }
            }
        });
    }
    private boolean validateInputs(String name, String email, String password) {
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity(), "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.length() < 6) {
            Toast.makeText(getActivity(), "Password must be at least 6 characters.", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }

    private void checkDoctorCredentials(String name, String email, String password, View v) {
        TeleDatabase.child(name).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String storedEmail = dataSnapshot.child("email").getValue(String.class);
                    String storedPassword = dataSnapshot.child("password").getValue(String.class);
                    if (email.equals(storedEmail) && password.equals(storedPassword)) {
                        // Doctor login successful
                        Toast.makeText(getActivity(), "Telemedicine login successful.", Toast.LENGTH_SHORT).show();
                        // Add your navigation logic here
                        NavController controller = Navigation.findNavController(v);
                        controller.navigate(R.id.action_teleDoctorLoginFragment_to_teleDoctorMainPageFragment);

                    } else {
                        Toast.makeText(getActivity(), "Incorrect email or password.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Telemedicine not found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Error checking your credentials.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}