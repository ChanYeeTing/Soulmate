package com.example.soulmate.ui.telemedicine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.soulmate.databinding.FragmentTelemedicineBinding;

public class TelemedicineFragment extends Fragment {

    private FragmentTelemedicineBinding binding;

    public View onCreateView ( @NonNull LayoutInflater inflater,
                               ViewGroup container, Bundle savedInstanceState ) {
        TelemedicineViewModel telemedicineViewModel =
                new ViewModelProvider ( this ).get ( TelemedicineViewModel.class );

        binding = FragmentTelemedicineBinding.inflate ( inflater, container, false );
        View root = binding.getRoot ();

//        final TextView textView = binding.textTelemedicine;
//        telemedicineViewModel.getText ().observe ( getViewLifecycleOwner (), textView::setText );
        return root;
    }

    @Override
    public void onDestroyView () {
        super.onDestroyView ();
        binding = null;
    }



}