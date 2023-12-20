package com.example.soulmate.ui.appointment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.soulmate.R;
import com.example.soulmate.databinding.FragmentAppointmentBinding;

public class appointmentFragment extends Fragment {

    private FragmentAppointmentBinding binding;

    public View onCreateView ( @NonNull LayoutInflater inflater,
                               ViewGroup container, Bundle savedInstanceState ) {
        AppointmentViewModel telemedicineViewModel =
                new ViewModelProvider ( this ).get ( AppointmentViewModel.class );

        binding = FragmentAppointmentBinding.inflate ( inflater, container, false );
        View root = binding.getRoot ();
        return root;
    }

    @Override
    public void onDestroyView () {
        super.onDestroyView ();
        binding = null;
    }

    @Override
    public void onViewCreated ( @NonNull View view, @Nullable Bundle savedInstanceState ) {
        super.onViewCreated ( view, savedInstanceState );

        Button telemedicine = getView ().findViewById ( R.id.telemedicineButton );
        Button clinic = getView ().findViewById ( R.id.clinicButton );
        Button vaccination = getView ().findViewById ( R.id.vaccinationButton );
        Button blood = getView ().findViewById ( R.id.bloodButton );

        telemedicine.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick ( View v ) {
                NavController controller = Navigation.findNavController ( v );
                controller.navigate ( R.id.action_nav_appointment_to_telemedicineBooking );
            }
        } );

        clinic.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick ( View v ) {
                NavController controller = Navigation.findNavController ( v );
                controller.navigate ( R.id.action_nav_appointment_to_clinicHospitalBooking );
            }
        } );

        vaccination.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick ( View v ) {
                NavController controller = Navigation.findNavController ( v );
                controller.navigate ( R.id.action_nav_appointment_to_vaccinationBooking );
            }
        } );

        blood.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick ( View v ) {
                NavController controller = Navigation.findNavController ( v );
                controller.navigate ( R.id.action_nav_appointment_to_bloodDonationBooking );
            }
        } );
    }
}