package com.example.soulmate.ui.medicalHistory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.soulmate.databinding.FragmentMedicalHistoryBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MedicalHistoryFragment extends Fragment {

    private FragmentMedicalHistoryBinding binding;

    private LinearLayout viewModeLayout;
    private LinearLayout editModeLayout;

    private Button buttonEdit;
    private Button buttonSave;

    private EditText editTextMedicationName;
    private EditText editTextDosage;
    private EditText editTextFrequency;
    private EditText editTextDuration;
    private EditText editTextLastDate;
    private EditText editTextInstructions;

    private CheckBox checkBoxAllergic;
    private CheckBox checkBoxSurgery;
    private EditText editTextAllergicDetails;
    private EditText editTextSurgeryDetails;

    private TextView textViewMedicationName;
    private TextView textViewDosage;
    private TextView textViewFrequency;

    private TextView textViewDuration;
    private TextView textViewLastDate;
    private TextView textViewInstructions;
    private TextView textViewAllergicStatus;
    private  TextView textViewAllergicStatusValue;
    private TextView textViewSurgeryStatus;
    private TextView textViewSurgeryStatusValue;

    private boolean isInEditMode = false;

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMedicalHistoryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModeLayout = binding.viewModeLayout;
        editModeLayout = binding.editModeLayout;

        editTextMedicationName = binding.editTextMedicationName;
        editTextDosage = binding.editTextDosage;
        editTextFrequency = binding.editTextFrequency;
        editTextDuration = binding.editTextDuration;
        editTextLastDate = binding.editTextLastDate;
        editTextInstructions = binding.editTextInstructions;
        editTextAllergicDetails = binding.editTextAllergicDetails;
        editTextSurgeryDetails = binding.editTextSurgeryDetails;
        checkBoxAllergic = binding.checkBoxAllergic;
        checkBoxSurgery = binding.checkBoxSurgery;
        buttonEdit = binding.buttonEdit;
        buttonSave = binding.buttonSave;

        textViewMedicationName = binding.TextViewMedicationName;
        textViewDosage = binding.TextDosage;
        textViewFrequency = binding.TextFrequency;
        textViewDuration = binding.TextDuration;
        textViewLastDate = binding.TextLastDate;
        textViewInstructions = binding.TextInstructions;
        textViewAllergicStatusValue = binding.textViewAllergicStatusValue;
        textViewAllergicStatus = binding.textViewAllergicStatus;
        textViewSurgeryStatusValue = binding.textViewSurgeryStatusValue;
        textViewSurgeryStatus = binding.textViewSurgeryStatus;

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Medical History").child("medicalHistory");
        }

        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isInEditMode) {
                    switchToEditMode();
                }
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMedicalHistory();
                switchToViewMode();
            }
        });

        // Load and display medical history in view mode
        loadMedicalHistory();
    }

    private void switchToEditMode() {
        isInEditMode = true;
        viewModeLayout.setVisibility(View.GONE);
        editModeLayout.setVisibility(View.VISIBLE);

        // Enable editing for medication details
        editTextMedicationName.setEnabled(true);
        editTextDosage.setEnabled(true);
        editTextFrequency.setEnabled(true);
        editTextDuration.setEnabled(true);
        editTextLastDate.setEnabled(true);
        editTextInstructions.setEnabled(true);
        editTextAllergicDetails.setEnabled(true);
        editTextSurgeryDetails.setEnabled(true);

        // Enable editing for allergic and surgery details
        checkBoxAllergic.setEnabled(true);
        checkBoxSurgery.setEnabled(true);

    }

    private void switchToViewMode() {
        isInEditMode = false;
        viewModeLayout.setVisibility(View.VISIBLE);
        editModeLayout.setVisibility(View.GONE);

        // Disable editing for medication details
        editTextMedicationName.setEnabled(false);
        editTextDosage.setEnabled(false);
        editTextFrequency.setEnabled(false);
        editTextDuration.setEnabled(false);
        editTextLastDate.setEnabled(false);
        editTextInstructions.setEnabled(false);
        editTextAllergicDetails.setEnabled(false);
        editTextSurgeryDetails.setEnabled(false);

//        // Disable editing for allergic and surgery details
//        checkBoxAllergic.setEnabled(false);
//        checkBoxSurgery.setEnabled(false);

    }


    private void loadMedicalHistory() {
        // Load medical history from Firebase and display in view mode
        if (databaseReference != null) {
            databaseReference.get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult() != null) {
                    MedicationDetails medicationDetails = task.getResult().getValue(MedicationDetails.class);
                    if (medicationDetails != null) {
                        textViewMedicationName.setText(medicationDetails.getMedicationName());
                        textViewDosage.setText(medicationDetails.getDosage());
                        textViewFrequency.setText(medicationDetails.getFrequency());
                        textViewDuration.setText(medicationDetails.getDuration());
                        textViewLastDate.setText(medicationDetails.getLastDate());
                        textViewInstructions.setText(medicationDetails.getInstructions());
                        textViewAllergicStatusValue.setText(medicationDetails.isAllergic() ? "Yes" : "No");
                        textViewAllergicStatus.setText(medicationDetails.getAllergicDetails());
                        textViewSurgeryStatusValue.setText(medicationDetails.isSurgery() ? "Yes" : "No");
                        textViewSurgeryStatus.setText(medicationDetails.getSurgeryDetails());
                    }
                }
            });
        }
    }

    private void saveMedicalHistory() {
        String medicationName = editTextMedicationName.getText().toString().trim();
        String dosage = editTextDosage.getText().toString().trim();
        String frequency = editTextFrequency.getText().toString().trim();
        String duration = editTextDuration.getText().toString().trim();
        String lastDate = editTextLastDate.getText().toString().trim();
        String instructions = editTextInstructions.getText().toString().trim();
        String allergicDetails = editTextAllergicDetails.getText().toString().trim();
        String surgeryDetails = editTextSurgeryDetails.getText().toString().trim();
        boolean isAllergic = checkBoxAllergic.isChecked();
        boolean hasSurgery = checkBoxSurgery.isChecked();

        // Validate your data here before saving

        if (!medicationName.isEmpty() && !dosage.isEmpty() && !frequency.isEmpty() && !duration.isEmpty() && !lastDate.isEmpty()) {
            // Save the data to Firebase
            if (firebaseAuth != null) {
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();

                if (currentUser != null) {
                    String userId = currentUser.getUid();

                    // Reference to the user's specific "MedicalHistory" node
                    DatabaseReference userMedicalHistoryReference = FirebaseDatabase.getInstance().getReference()
                            .child("Users").child(userId).child("Medical History");

                    // Retrieve the user's name from the "Users" node
                    DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("User Info");
                    usersRef.child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                String name = dataSnapshot.getValue(String.class);

                                // Create a MedicationDetails object
                                MedicationDetails medicalHistory = new MedicationDetails(
                                        medicationName,
                                        dosage,
                                        frequency,
                                        duration,
                                        lastDate,
                                        instructions,
                                        isAllergic,
                                        allergicDetails,
                                        hasSurgery,
                                        surgeryDetails
                                );

                                // Save the user's name under "Name"
                                userMedicalHistoryReference.child("Name").setValue(name);

                                // Save the medical history under "medicalHistory" under the user's UID
                                userMedicalHistoryReference.child("medicalHistory").setValue(medicalHistory);

                                loadMedicalHistory();

                                // Switch back to view mode
                                switchToViewMode();

                                Toast.makeText(getContext(), "Medical history saved successfully", Toast.LENGTH_SHORT).show();
                            }
                        }

                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle error
                            Toast.makeText(getContext(), "Error retrieving user's name", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        } else {
            Toast.makeText(getContext(), "Please enter all required field.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
