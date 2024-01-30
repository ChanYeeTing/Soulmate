package com.example.soulmate.ui.healthTracking;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.soulmate.R;
import com.example.soulmate.databinding.FragmentHealthTrackingBinding;

public class HealthTrackingFragment extends Fragment {

    private FragmentHealthTrackingBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HealthTrackingViewModel healthTrackingViewModel =
                new ViewModelProvider(this).get(HealthTrackingViewModel.class);

        binding = FragmentHealthTrackingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button submit = getView().findViewById(R.id.submitButton2);
        // Initially disable the submit button
        submit.setEnabled(false);

        // Set a TextWatcher on each input field to dynamically enable/disable the submit button
        addTextChangedListener(R.id.age);
        addTextChangedListener(R.id.weight);
        addTextChangedListener(R.id.height);
        addTextChangedListener(R.id.blood_sugar);
        addTextChangedListener(R.id.blood_pressure);
        addTextChangedListener(R.id.temperature);
        addTextChangedListener(R.id.blood_oxygen);
        addTextChangedListener(R.id.respiration_rate);
        addTextChangedListener(R.id.pulse_rate);

        RadioGroup smokingHabitRadioGroup = getView().findViewById(R.id.smokingHabit);

        smokingHabitRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Enable the submit button when the smoking habit is selected
                submit.setEnabled(checkedId != -1 && isInputValid());
                // Update the validation message
                showInputValidationMessages();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Log.d("HealthTrackingFragment", "Before creating HealthData");

                if (isInputValid()) {

                    HealthData healthData = new HealthData(
                            getEditTextValue(R.id.age),
                            getEditTextValue(R.id.weight),
                            getEditTextValue(R.id.height),
                            getEditTextValue(R.id.blood_sugar),
                            getEditTextValue(R.id.blood_pressure),
                            getEditTextValue(R.id.temperature),
                            getEditTextValue(R.id.blood_oxygen),
                            getEditTextValue(R.id.respiration_rate),
                            getEditTextValue(R.id.pulse_rate),
                            getRadioButtonValue(R.id.smokingHabit)
                    );
                    Log.d("HealthTrackingFragment", "HealthData created: " + healthData.toString());

                    // Display a success message
                    Toast.makeText(requireContext(), "Submit Successful!", Toast.LENGTH_SHORT).show();

                    Bundle bundle = new Bundle();
                    bundle.putParcelable("healthData", healthData);

                    NavController controller = Navigation.findNavController(v);
                    controller.navigate(R.id.action_nav_health_tracking_to_analyzeResultFragment, bundle);
                } else {
                    Log.d("HealthTrackingFragment", "Input is not valid");
                    // Display the detailed error message
                    showInputValidationMessages();
                }
            }
        });
    }

    private boolean isInputValid() {
        // Check if all EditText fields are not empty and contain valid numbers
        boolean isAgeValid = isEditTextValid(R.id.age);
        boolean isWeightValid = isEditTextValid(R.id.weight);
        boolean isHeightValid = isEditTextValid(R.id.height);
        boolean isBloodSugarValid = isEditTextValid(R.id.blood_sugar);
        boolean isBloodPressureValid = isBloodPressureValid(R.id.blood_pressure);
        boolean isTemperatureValid = isEditTextValid(R.id.temperature);
        boolean isBloodOxygenValid = isEditTextValid(R.id.blood_oxygen);
        boolean isRespirationRateValid = isEditTextValid(R.id.respiration_rate);
        boolean isPulseRateValid = isEditTextValid(R.id.pulse_rate);
        boolean isSmokingHabitSelected = isSmokingHabitSelected();

        // Log the values for debugging
        Log.d("Validation", "isAgeValid: " + isAgeValid);
        Log.d("Validation", "isWeightValid: " + isWeightValid);
        Log.d("Validation", "isHeightValid: " + isHeightValid);
        Log.d("Validation", "isBloodSugarValid: " + isBloodSugarValid);
        Log.d("Validation", "isBloodPressureValid: " + isBloodPressureValid);
        Log.d("Validation", "isTemperatureValid: " + isTemperatureValid);
        Log.d("Validation", "isBloodOxygenValid: " + isBloodOxygenValid);
        Log.d("Validation", "isRespirationRateValid: " + isRespirationRateValid);
        Log.d("Validation", "isPulseRateValid: " + isPulseRateValid);
        Log.d("Validation", "isSmokingHabitSelected: " + isSmokingHabitSelected);

        return isAgeValid &&
                isWeightValid &&
                isHeightValid &&
                isBloodSugarValid &&
                isBloodPressureValid &&
                isTemperatureValid &&
                isBloodOxygenValid &&
                isRespirationRateValid &&
                isPulseRateValid &&
                isSmokingHabitSelected;
    }


    private boolean isEditTextValid(int editTextId) {
        EditText editText = getView().findViewById(editTextId);
        String input = editText.getText().toString().trim();

        if (!TextUtils.isEmpty(input)) {
            if (editTextId == R.id.weight ) {
                return isDecimalNumber(input)  && Double.parseDouble(input) > 0;
            } else if (editTextId == R.id.temperature){
                return isDecimalNumber(input) && Double.parseDouble(input) > 0 && Double.parseDouble(input) <= 46.5;
            } else if (editTextId == R.id.age){
                return TextUtils.isDigitsOnly(input) && Integer.parseInt(input) > 0 && Integer.parseInt(input) <= 200;
            } else if(editTextId == R.id.height){
                return isDecimalNumber(input) && Double.parseDouble(input) > 0 && Double.parseDouble(input) <= 300;
            } else if (editTextId == R.id.blood_oxygen) {
                // Validate blood oxygen level (1 to 100)
                return isDecimalNumber(input) && Double.parseDouble(input) >= 1 && Double.parseDouble(input) <= 100;
            } else if (editTextId == R.id.respiration_rate || editTextId == R.id.pulse_rate
                    || editTextId == R.id.blood_pressure || editTextId == R.id.blood_sugar) {
                // Validate to avoid input digit 0 only
                return TextUtils.isDigitsOnly(input) && Integer.parseInt(input) > 0;
            } else {
                return TextUtils.isDigitsOnly(input);
            }
        }
        return false;
    }

    private boolean isDecimalNumber(String input) {
        try {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    private boolean isBloodPressureValid(int editTextId) {
        EditText editText = getView().findViewById(editTextId);
        String input = editText.getText().toString().trim();
        
        // Check if the input matches the format "systolic/diastolic" (e.g., "120/70")
        if (!TextUtils.isEmpty(input) && input.matches("\\d+/\\d+")) {
            // Split the input into systolic and diastolic parts
            String[] parts = input.split("/");

            // Check that both parts are not equal to 0
            return !parts[0].equals("0") && !parts[1].equals("0");
        }
        return false;
    }

    private boolean isSmokingHabitSelected() {
        RadioGroup smokingHabitRadioGroup = getView().findViewById(R.id.smokingHabit);
        return smokingHabitRadioGroup.getCheckedRadioButtonId() != -1;
    }

    private String getEditTextValue(int editTextId) {
        EditText editText = getView().findViewById(editTextId);
        return editText.getText().toString().trim();
    }

    private String getRadioButtonValue(int radioGroupId) {
        RadioGroup radioGroup = getView().findViewById(radioGroupId);

        // Get the ID of the selected radio button
        int selectedRadioButtonId = radioGroup.getCheckedRadioButtonId();

        // If no radio button is selected, return an empty string (or handle accordingly)
        if (selectedRadioButtonId == -1) {
            return "";
        }

        // Find the selected radio button
        RadioButton selectedRadioButton = getView().findViewById(selectedRadioButtonId);

        // Return the text of the selected radio button
        return selectedRadioButton.getText().toString().trim();
    }

    private void showInputValidationMessages() {
        StringBuilder errorMessage = new StringBuilder("");

        if (!isEditTextValid(R.id.age)) {
            errorMessage.append("- Age(Greater than 0, Less than 200)\n");
        }
        if (!isEditTextValid(R.id.weight)) {
            errorMessage.append("- Weight(Greater than 0)\n");
        }
        if (!isEditTextValid(R.id.height)) {
            errorMessage.append("- Height(Greater than 0, Less than 301)\n");
        }
        if (!isEditTextValid(R.id.blood_sugar)) {
            errorMessage.append("- Blood Sugar(Greater than 0)\n");
        }
        if (!isBloodPressureValid(R.id.blood_pressure)) {
            errorMessage.append("- Blood Pressure(Must systolic/diastolic, Greater than 0)\n");
        }
        if (!isEditTextValid(R.id.temperature)) {
            errorMessage.append("- Temperature(Greater than 0, Less than 46.6)\n");
        }
        if (!isEditTextValid(R.id.blood_oxygen)) {
            errorMessage.append("- Blood Oxygen(1-100 only)\n");
        }
        if (!isEditTextValid(R.id.respiration_rate)) {
            errorMessage.append("- Respiration Rate(Greater than 0)\n");
        }
        if (!isEditTextValid(R.id.pulse_rate)) {
            errorMessage.append("- Pulse Rate(Greater than 0)\n");
        }

        // Check if smoking habit is selected
        RadioGroup smokingHabitRadioGroup = getView().findViewById(R.id.smokingHabit);
        if (smokingHabitRadioGroup.getCheckedRadioButtonId() == -1) {
            errorMessage.append("- Select Smoking Habit");
        }

        // Show the detailed error message
        TextView validationMessageTextView = getView().findViewById(R.id.validationMessage);
        validationMessageTextView.setText(errorMessage.toString());
    }

    public static class HealthData implements Parcelable {
        public String age;
        public String weight;
        public String height;
        public String blood_sugar;
        public String blood_pressure;
        public String temperature;
        public String blood_oxygen;
        public String respiration_rate;
        public String pulse_rate;
        public String smokingHabit;

        public HealthData(String age, String weight, String height, String blood_sugar, String blood_pressure,
                          String temperature, String blood_oxygen, String respiration_rate,
                          String pulse_rate, String smokingHabit) {
            this.age = age;
            this.weight = weight;
            this.height = height;
            this.blood_sugar = blood_sugar;
            this.blood_pressure = blood_pressure;
            this.temperature = temperature;
            this.blood_oxygen = blood_oxygen;
            this.respiration_rate = respiration_rate;
            this.pulse_rate = pulse_rate;
            this.smokingHabit = smokingHabit;
        }
        protected HealthData(Parcel in) {
            age = in.readString();
            weight = in.readString();
            height = in.readString();
            blood_sugar = in.readString();
            blood_pressure = in.readString();
            temperature = in.readString();
            blood_oxygen = in.readString();
            respiration_rate = in.readString();
            pulse_rate = in.readString();
            smokingHabit = in.readString();
        }
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(age);
            dest.writeString(weight);
            dest.writeString(height);
            dest.writeString(blood_sugar);
            dest.writeString(blood_pressure);
            dest.writeString(temperature);
            dest.writeString(blood_oxygen);
            dest.writeString(respiration_rate);
            dest.writeString(pulse_rate);
            dest.writeString(smokingHabit);
        }

        public static final Creator<HealthData> CREATOR = new Creator<HealthData>() {
            @Override
            public HealthData createFromParcel(Parcel in) {
                return new HealthData(in);
            }

            @Override
            public HealthData[] newArray(int size) {
                return new HealthData[size];
            }
        };
    }
    private void addTextChangedListener(int editTextId) {
        EditText editText = getView().findViewById(editTextId);
        TextView validationMessageTextView = getView().findViewById(R.id.validationMessage);
        Button submitButton = getView().findViewById(R.id.submitButton2);

        // Array to hold the flag value
        final boolean[] validationMessageShown = {false};
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
                // Clear the validation message when the user starts editing
                if (validationMessageShown[0]) {
                    validationMessageTextView.setText("");
                    validationMessageShown[0] = false;
                } else {
                    // Enable the submit button when all input is valid
                    submitButton.setEnabled(isInputValid());
                }
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Check if the input is valid, and show the validation message if not
                if (!isEditTextValid(editTextId)) {
                    showInputValidationMessages();
                    validationMessageShown[0] = true;
                    submitButton.setEnabled(false);
                } else {
                    // Enable the submit button when all input is valid
                    submitButton.setEnabled(isInputValid());
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
                if (!validationMessageShown[0]) {
                    showInputValidationMessages();
                    validationMessageShown[0] = true;
                    submitButton.setEnabled(false);
                } else {
                    // Enable the submit button when all input is valid
                    submitButton.setEnabled(isInputValid());
                }
            }
        });
    }
}
