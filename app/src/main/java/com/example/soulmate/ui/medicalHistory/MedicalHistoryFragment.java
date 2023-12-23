package com.example.soulmate.ui.medicalHistory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.soulmate.databinding.FragmentMedicalHistoryBinding;
import com.example.soulmate.main_page;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MedicalHistoryFragment extends Fragment {

    private FragmentMedicalHistoryBinding binding;

    private LinearLayout viewModeLayout;
    private LinearLayout editModeLayout;
    private TextView textViewMedicalHistory;
    private EditText editTextMedicalHistory;
    private Button buttonEdit;
    private Button buttonSave;

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMedicalHistoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        if (getActivity() instanceof main_page) {
            ((main_page) getActivity()).getSupportActionBar().setTitle("Medical History");
        }

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModeLayout = binding.viewModeLayout;
        editModeLayout = binding.editModeLayout;
        textViewMedicalHistory = binding.textViewMedicalHistory;
        editTextMedicalHistory = binding.editTextMedicalHistory;
        buttonEdit = binding.buttonEdit;
        buttonSave = binding.buttonSave;

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference().child("MedicalHistory").child(userId);
        }

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToEditMode();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMedicalHistory();
            }
        });

        // Load and display medical history in view mode
        loadMedicalHistory();
    }

    private void switchToEditMode() {
        viewModeLayout.setVisibility(View.GONE);
        editModeLayout.setVisibility(View.VISIBLE);

        // Set the current medical history text to the edit text
        editTextMedicalHistory.setText(textViewMedicalHistory.getText());
    }

    private void loadMedicalHistory() {
        // Load medical history from Firebase and display in view mode
        if (databaseReference != null) {
            databaseReference.get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult() != null) {
                    String loadedMedicalHistory = String.valueOf(task.getResult().getValue());
                    textViewMedicalHistory.setText(loadedMedicalHistory);
                }
            });
        }
    }

    private void saveMedicalHistory() {
        String medicalHistory = editTextMedicalHistory.getText().toString().trim();

        if (!medicalHistory.isEmpty()) {
            // Save the data to Firebase
            if (databaseReference != null) {
                databaseReference.setValue(medicalHistory);
            }

            // Display the saved medical history in view mode
            textViewMedicalHistory.setText(medicalHistory);

            // Switch back to view mode
            viewModeLayout.setVisibility(View.VISIBLE);
            editModeLayout.setVisibility(View.GONE);

            Toast.makeText(getContext(), "Medical history saved successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Please enter medical history", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
