package com.example.soulmate;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.soulmate.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegistrationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrationFragment extends Fragment {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegistrationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistrationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegistrationFragment newInstance(String param1, String param2) {
        RegistrationFragment fragment = new RegistrationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration, container, false);
    }

    @Override
    public void onActivityCreated ( @Nullable Bundle savedInstanceState ) {
        super.onActivityCreated ( savedInstanceState );
        Button login, register2 ;
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference ref = firebaseDatabase.getReference();

        login = getView () .findViewById ( R.id.loginButton );
        login.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick ( View v ) {
                NavController controller = Navigation.findNavController ( v );
                controller.navigate ( R.id.action_registrationFragment_to_login_fragment );
            }
        } );

        register2 = getView ().findViewById ( R.id.registerButton );
        EditText name = getView().findViewById(R.id.username);
        EditText email = getView().findViewById(R.id.emailAddress);
        EditText number = getView().findViewById(R.id.userPhone);
        EditText DOB = getView().findViewById(R.id.dateOfBirth);
//        RadioButton gender = getView().findViewById(R.id.gender);


        register2.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick ( View v ) {
                String getName = name.getText().toString();
                String getEmail = email.getText().toString();
                String getNumber = number.getText().toString();
                String getDOB = DOB.getText().toString();
//                String getGender = gender.getText().toString();

                DatabaseReference userRef = ref.child("Users").child(getName);
                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("name",getName);
                hashMap.put("email",getEmail);
                hashMap.put("Mobile Number",getNumber);
                hashMap.put("Date of Birth",getDOB);
//                hashMap.put("Gender",getGender);

                userRef.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getActivity(), "Register Successful", Toast.LENGTH_SHORT).show();
                    }
                });

//                FirebaseDatabase.getInstance().clone("User")
//                        .document("UserData")
//                        .set(hashMap);
////                        .addOnSuccessListener(new OnSuccessListener<Void>() {
////                            @Override
////                            public void onSuccess(Void unused) {
////                                Toast.makeText(RegistrationFragment.this, "Data", Toast.LENGTH_SHORT).show();
////                            }
////                        }
////                        );

                NavController controller = Navigation.findNavController ( v );
                controller.navigate ( R.id.action_registrationFragment_to_emailVerificationFragment );
            }
        } );
    }





}