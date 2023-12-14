package com.example.soulmate.ui.dateTracking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.soulmate.databinding.FragmentDateTrackingBinding;

public class DateTrackingFragment extends Fragment {

    private FragmentDateTrackingBinding binding;

    public View onCreateView ( @NonNull LayoutInflater inflater,
                               ViewGroup container, Bundle savedInstanceState ) {
        DateTrackingViewModel dateTrackingViewModelViewModel =
                new ViewModelProvider ( this ).get ( DateTrackingViewModel.class );

        binding = FragmentDateTrackingBinding.inflate ( inflater, container, false );
        View root = binding.getRoot ();

        return root;
    }

    @Override
    public void onDestroyView () {
        super.onDestroyView ();
        binding = null;
    }
}