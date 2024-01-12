package com.example.soulmate;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DoctorMainPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DoctorMainPageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DoctorMainPageFragment () {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DoctorMainPageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DoctorMainPageFragment newInstance ( String param1, String param2 ) {
        DoctorMainPageFragment fragment = new DoctorMainPageFragment ();
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
        return inflater.inflate ( R.layout.fragment_doctor_main_page, container, false );
    }

    @Override
    public void onViewCreated ( @NonNull View view, @Nullable Bundle savedInstanceState ) {
        super.onViewCreated ( view, savedInstanceState );

        Button vaccination = view.findViewById(R.id.appointmentRecord7);
        Button bloodDonation = view.findViewById ( R.id.appointmentRecord6 );
        Button logoutButton = view.findViewById(R.id.logoutButton);
        vaccination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.navigate(R.id.action_doctorMainPageFragment_to_doctorVaccinationFragment);
            }
        });
        bloodDonation.setOnClickListener(new View.OnClickListener() {
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