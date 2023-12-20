package com.example.soulmate.ui.settings;

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
import com.example.soulmate.databinding.FragmentSettingsBinding;

public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;

    public View onCreateView ( @NonNull LayoutInflater inflater,
                               ViewGroup container, Bundle savedInstanceState ) {
        SettingsViewModel settingsViewModel =
                new ViewModelProvider ( this ).get ( SettingsViewModel.class );

        binding = FragmentSettingsBinding.inflate ( inflater, container, false );
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

        Button updateProfile = getView ().findViewById ( R.id.updateProfileButton );

        updateProfile.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick ( View v ) {
                NavController controller = Navigation.findNavController ( v );
                controller.navigate ( R.id.action_nav_settings_to_updateProfile );
            }
        } );
    }
}