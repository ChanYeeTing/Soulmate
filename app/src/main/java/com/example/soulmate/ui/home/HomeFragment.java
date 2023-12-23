package com.example.soulmate.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.soulmate.PopUpCallFragment;
import com.example.soulmate.R;
import com.example.soulmate.databinding.FragmentHomeBinding;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView ( @NonNull LayoutInflater inflater,
                               ViewGroup container, Bundle savedInstanceState ) {
        HomeViewModel homeViewModel =
                new ViewModelProvider ( this ).get ( HomeViewModel.class );

        binding = FragmentHomeBinding.inflate ( inflater, container, false );
        View root = binding.getRoot ();

        return root;
    }

    @Override
    public void onDestroyView () {
        super.onDestroyView ();
        binding = null;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton emergencyCall = view.findViewById(R.id.emergencyCall2);

        emergencyCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopUpCallFragment showPopUp = new PopUpCallFragment();
                showPopUp.show(((AppCompatActivity) requireActivity()).getSupportFragmentManager(), "showpopup");
            }
        });
    }
}