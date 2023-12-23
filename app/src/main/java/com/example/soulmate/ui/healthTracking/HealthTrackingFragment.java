package com.example.soulmate.ui.healthTracking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.soulmate.R;
import com.example.soulmate.databinding.FragmentHealthTrackingBinding;
import com.example.soulmate.ui.settings.HealthTrackingViewModel;

public class HealthTrackingFragment extends Fragment {

    private FragmentHealthTrackingBinding binding;

    public View onCreateView ( @NonNull LayoutInflater inflater,
                               ViewGroup container, Bundle savedInstanceState ) {
        HealthTrackingViewModel HealthTrackingViewModel =
                new ViewModelProvider ( this ).get ( HealthTrackingViewModel.class );

        binding = FragmentHealthTrackingBinding.inflate ( inflater, container, false );
        View root = binding.getRoot ();

        return root;
    }

    @Override
    public void onDestroyView () {
        super.onDestroyView ();
        binding = null;
    }

    @Override
    public void onActivityCreated ( @Nullable Bundle savedInstanceState ) {
        super.onActivityCreated ( savedInstanceState );

        Button submit = getView ().findViewById ( R.id.submitButton2 );

        submit.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick ( View v ) {
                NavController controller = Navigation.findNavController ( v );
                controller.navigate ( R.id.action_nav_health_tracking_to_analyzeResultFragment );
            }
        } );
    }
}
