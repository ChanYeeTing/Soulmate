//package com.example.soulmate;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//
//import androidx.annotation.Nullable;
//import androidx.fragment.app.DialogFragment;
//import androidx.navigation.NavController;
//import androidx.navigation.fragment.NavHostFragment;
//
//import com.example.soulmate.ui.reminder.ReminderFragment;
//
//
//public class PopUpReminderFragment extends DialogFragment {
//
//    private EditText MedicineName;
//    private EditText EditTextPillDose;
//    private EditText DateMedicine;
//    private EditText EditTextInput;
//
//    public PopUpReminderFragment() {
//        // Required empty public constructor
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_pop_up_reminder, container, false);
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        // Initialize UI elements
//        MedicineName = requireView().findViewById(R.id.medicationName);
//        EditTextPillDose = requireView().findViewById(R.id.pill_dose);
//        DateMedicine = requireView().findViewById(R.id.reminderDate);
//        EditTextInput = requireView().findViewById(R.id.timeReminder);
//
//        Button add = requireView().findViewById(R.id.addButton);
//
//        // Use requireView() to get the View associated with the Fragment
//        View fragmentView = requireView();
//
//        // Use NavHostFragment.findNavController to get the correct NavController
//        NavController navController = NavHostFragment.findNavController(this);
//
//        add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Save the data to Firebase in ReminderFragment style
//                saveDataToFirebase();
//
//                // Dismiss the popup
//                dismiss();
//            }
//        });
//    }
//
//    private void saveDataToFirebase() {
//        // Retrieve data from UI elements
//        String medicineName = MedicineName.getText().toString().trim();
//        String pillOrDose = pillOrDoseValidation(EditTextPillDose.getText().toString().trim());
//        String date = DateMedicine.getText().toString().trim();
//        String time = EditTextInput.getText().toString().trim();
//
//        // Validate input
//        if (medicineName.isEmpty() || pillOrDose.isEmpty() || date.isEmpty() || time.isEmpty()) {
//            // Handle invalid input
//            // You might want to show a message to the user
//            return;
//        }
//
//        // Find the existing instance of ReminderFragment
//        ReminderFragment reminderFragment = (ReminderFragment) getParentFragmentManager().findFragmentById(R.id.nav_reminder);
//
//        // Check if the fragment is not null before calling the method
//        if (reminderFragment != null) {
//            // Save data to Firebase using the instance of ReminderFragment
//            ReminderFragment.MedicineData medicineData = new ReminderFragment.MedicineData(medicineName, pillOrDose, date, time);
//            reminderFragment.saveDataToFirebase(medicineData);
//        }
//    }
//
//    private String pillOrDoseValidation(String input) {
//        // Validate and normalize input for pill/dose
//        if (input.equalsIgnoreCase("pill") || input.equalsIgnoreCase("dose")) {
//            return input.toLowerCase(); // Normalize to lowercase
//        } else {
//            // Handle invalid input
//            // You might want to show a message to the user
//            return ""; // Or handle it in a way that fits your application
//        }
//    }
//}
