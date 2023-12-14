package com.example.soulmate.ui.medicalHistory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.soulmate.databinding.FragmentMedicalHistoryBinding;

public class MedicalHistoryFragment extends Fragment {

    private FragmentMedicalHistoryBinding binding;

    public View onCreateView ( @NonNull LayoutInflater inflater,
                               ViewGroup container, Bundle savedInstanceState ) {
        MedicalHistoryViewModel medicalHistoryViewModel =
                new ViewModelProvider ( this ).get ( MedicalHistoryViewModel.class );

        binding = FragmentMedicalHistoryBinding.inflate ( inflater, container, false );
        View root = binding.getRoot ();
        return root;
    }

    @Override
    public void onDestroyView () {
        super.onDestroyView ();
        binding = null;
    }
}