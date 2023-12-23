package com.example.soulmate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.soulmate.ui.appointment.TelemedicineBookingFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PopUpCallFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class timeslot extends DialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public timeslot () {
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
    public static PopUpCallFragment newInstance ( String param1, String param2 ) {
        PopUpCallFragment fragment = new PopUpCallFragment ();
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
        return inflater.inflate ( R.layout.fragment_timeslot, container, false );
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button yes = getView().findViewById(R.id.yesButton2);
        Button cancel = getView().findViewById(R.id.cancelButton2);

        // Use requireView() to get the View associated with the Fragment
        View fragmentView = requireView();

        // Use NavHostFragment.findNavController to get the correct NavController
        NavController navController = NavHostFragment.findNavController(this);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*              Fragment fragment = new TelemedicineBookingFragment();
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.timeslot, fragment);
                ft.commit();

                String choice = "1";
                Bundle bundle = new Bundle();
                bundle.putString("choice",choice);

                fragment.setArguments(bundle);
*/
                // Use the NavController obtained from NavHostFragment
                navController.navigate(R.id.action_popUpTimeSlot_to_nav_date_tracking);
                dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Use the NavController obtained from NavHostFragment
                navController.navigate(R.id.action_popUpTimeSlot_to_telemedicineBooking);
                dismiss ();
            }
        });
    }

}