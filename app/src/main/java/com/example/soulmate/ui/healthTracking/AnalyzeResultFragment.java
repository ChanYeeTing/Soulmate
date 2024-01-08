package com.example.soulmate.ui.healthTracking;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.soulmate.R;
import com.example.soulmate.main_page;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AnalyzeResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnalyzeResultFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView textViewAge;
    private TextView textViewHeight;
    private TextView textViewWeight;
    private TextView textViewBMI;
    private TextView textViewBloodPressure;
    private TextView textViewBodyTemperature;
    private TextView textViewPulseRate;
    private TextView textViewRespiratoryRate;
    private TextView textViewBloodOxygen;
    private TextView textViewBloodSugar;

    private TextView textViewSmokingHabit;

    private HealthTrackingFragment.HealthData healthData;
    public AnalyzeResultFragment () {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AnalyzeResultFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AnalyzeResultFragment newInstance(String param1, String param2) {
        AnalyzeResultFragment fragment = new AnalyzeResultFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        if (getArguments () != null) {
            mParam1 = getArguments ().getString ( ARG_PARAM1 );
            mParam2 = getArguments ().getString ( ARG_PARAM2 );
        }
    }

    @Override
    public View onCreateView ( @NonNull LayoutInflater inflater, ViewGroup container,
                               Bundle savedInstanceState ) {
        // Inflate the layout for this fragment
        View view = inflater.inflate ( R.layout.fragment_analyze_result, container, false );

        if (getActivity() instanceof main_page) {
            ((main_page) getActivity()).getSupportActionBar().setTitle("Analyze Result");
        }
        return view;
    }
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d("AnalyzeResultFragment", "onViewCreated called");

        // Initialize TextViews
        textViewAge = view.findViewById(R.id.textViewAge);
        textViewHeight = view.findViewById(R.id.textViewHeight);
        textViewWeight = view.findViewById(R.id.textViewWeight);
        textViewBMI = view.findViewById(R.id.textViewBMI);
        textViewBloodPressure = view.findViewById(R.id.textViewBloodPressure);
        textViewBodyTemperature = view.findViewById(R.id.textViewBodyTemperature);
        textViewPulseRate = view.findViewById(R.id.textViewPulseRate);
        textViewRespiratoryRate = view.findViewById(R.id.textViewRespiratoryRate);
        textViewBloodOxygen = view.findViewById(R.id.textViewBloodOxygen);
        textViewBloodSugar = view.findViewById(R.id.textViewBloodSugar);
        textViewSmokingHabit = view.findViewById(R.id.textViewSmokingHabit);

        // Perform a null check on getArguments()
        Bundle arguments = getArguments();
        if (arguments != null) {
            healthData = arguments.getParcelable("healthData");
            Log.d("AnalyzeResultFragment", "Received healthData: " + healthData);

            // Retrieve and analyze user's health data
            analyzeHealthData(healthData);
        } else {
            Log.e("AnalyzeResultFragment", "Arguments are null");
        }
    }

    private void analyzeHealthData(HealthTrackingFragment.HealthData healthData) {
        if (healthData != null) {
            double bmi = calculateBMI(
                    Double.parseDouble(healthData.weight),
                    Double.parseDouble(healthData.height)
            );
            textViewAge.setText("Age: "+ healthData.age + " years old");
            textViewHeight.setText("Height: " + healthData.height + " cm");
            textViewWeight.setText("Weight: " + healthData.weight + " kg");

            updateBMI(bmi);
            updateBloodPressureStatus(healthData.blood_pressure);
            updateBodyTemperatureStatus(healthData.temperature);
            updatePulseRateStatus(healthData.pulse_rate);
            updateRespiratoryRateStatus(healthData.respiration_rate);
            updateBloodOxygenStatus(healthData.blood_oxygen);
            updateBloodSugarStatus(healthData.blood_sugar);
            updateSmokingHabitStatus(healthData.smokingHabit);
        }
    }

    private double calculateBMI(double weight, double height) {
        //Assume users input height in cm
        // BMI formula: BMI = weight (kg) / (height (m) * height (m))
        return weight / (height/100 * height/100);
    }

    private void updateBMI(double bmi) {
        // Update the analysis result based on the calculated BMI
        String status;
        if (bmi < 18.5) {
            status = "Underweight";
            textViewBMI.setText("BMI:"+ String.format("%.2f", bmi) + " (" + status + ") \n" +
                    "- Advice: Maintain a healthy weight: Eat a well-balanced diet rich in fruits and vegetables, whole grains, and lean meats. Participate in frequent physical exercise.");
        } else if (bmi >= 18.5 && bmi <= 24.9) {
            status = "Normal Range";
            textViewBMI.setText("BMI: " + String.format("%.2f", bmi) + " (" + status + ") \n" +
                    "- Advice: Keep it normal: Maintain a balanced diet and regular exercise routine.");
        } else if (bmi >= 25 && bmi <= 29.9) {
            status = "Overweight";
            textViewBMI.setText("BMI: " + String.format("%.2f", bmi) + " (" + status + ") \n" +
                    "- Advice: Manage weight: Focus on a healthy diet and exercise routine. Consult with a healthcare professional for personalized advice.");
        } else {
            status = "Obese";
            textViewBMI.setText("BMI: " + String.format("%.2f", bmi) + " (" + status + ") \n" +
                    "- Advice: Address obesity: Seek guidance from a healthcare professional for weight management strategies.");
        }
    }

    private void updateBloodPressureStatus(String bloodPressure) {
        // Assume bloodPressure is in the format "systolic/diastolic"
        String[] pressureValues = bloodPressure.split("/");
        int systolic = Integer.parseInt(pressureValues[0]);
        int diastolic = Integer.parseInt(pressureValues[1]);

        if (systolic < 120 && diastolic < 80) {
            textViewBloodPressure.setText("Blood Pressure Level: "+ healthData.blood_pressure + " mmHg" + "\n# (Normal Range)");
        } else if (systolic >= 120 && systolic <= 129 && diastolic < 80) {
            textViewBloodPressure.setText("Blood Pressure Level: "+ healthData.blood_pressure + " mmHg" + "\n# (Elevated) \n- Advice: Monitor your blood pressure regularly and adopt a healthy lifestyle.");
        } else if ((systolic >= 130 && systolic <= 139) || (diastolic >= 80 && diastolic <= 89)) {
            textViewBloodPressure.setText("Blood Pressure Level: "+ healthData.blood_pressure + " mmHg" + "\n# (Hypertension Stage 1) \n- Advice: Consult with a healthcare professional for further evaluation and lifestyle modifications.");
        } else {
            textViewBloodPressure.setText("Blood Pressure Level: "+ healthData.blood_pressure + " mmHg" + "\n# (Hypertension Stage 2) \n- Advice: Seek prompt medical attention and follow prescribed treatments.");
        }
    }

    private void updateBodyTemperatureStatus(String temperature) {
        // Assume temperature is in Celsius
        double tempCelsius = Double.parseDouble(temperature);

        if (tempCelsius < 38.0) {
            textViewBodyTemperature.setText("Body Temperature: " + healthData.temperature + " °C" + "\n# (Normal Range) \n- Advice: Keep it normal: Maintain good hygiene practices and a healthy lifestyle.");
        } else {
            textViewBodyTemperature.setText("Body Temperature: " + healthData.temperature + " °C" + "\n# (Fever) \n- Advice: Take appropriate measures to manage fever. "
                    + "Stay hydrated, rest, and use fever-reducing medications (such as acetaminophen or ibuprofen) under medical guidance. "
                    + "Consult with a healthcare professional if necessary.");
        }
    }

    private void updatePulseRateStatus(String pulseRate) {
        int rate = Integer.parseInt(pulseRate);

        if (rate >= 60 && rate <= 100) {
            textViewPulseRate.setText("Pulse Rate: " + healthData.pulse_rate + " bpm" + "\n# (Normal Range) \n- Advice: Maintain cardiovascular fitness through regular aerobic exercise and a healthy lifestyle.");
        } else if (rate < 60) {
            textViewPulseRate.setText("Pulse Rate: " + healthData.pulse_rate + " bpm" + "\n# (Bradycardia) \n- Advice: Consult with a healthcare professional to determine the cause and appropriate management.");
        } else {
            textViewPulseRate.setText("Pulse Rate: " + healthData.pulse_rate + " bpm" + "\n# (Tachycardia) \n- Advice: Seek medical evaluation for underlying causes and follow recommended treatments.");
        }
    }

    private void updateRespiratoryRateStatus(String respiratoryRate) {
        int rate = Integer.parseInt(respiratoryRate);

        if (rate >= 12 && rate <= 20) {
            textViewRespiratoryRate.setText("Respiratory Rate: " + healthData.respiration_rate +" breaths/min\n# (Normal Range) \n- Advice: Maintain good respiratory health through regular exercise and avoiding exposure to respiratory irritants.");
        } else {
            textViewRespiratoryRate.setText("Respiratory Rate: " + healthData.respiration_rate +" breaths/min\n# (Abnormal) \n- Advice: Consult with a healthcare professional for further evaluation and management of respiratory issues.");
        }
    }

    private void updateBloodOxygenStatus(String bloodOxygen) {
        int oxygenLevel = Integer.parseInt(bloodOxygen);

        if (oxygenLevel >= 95 && oxygenLevel <= 100) {
            textViewBloodOxygen.setText("Blood Oxygen Level: " + healthData.blood_oxygen +"%\n# (Normal Range) \n- Advice: Continue healthy habits, and consult with a healthcare professional if respiratory symptoms arise.");
        } else {
            textViewBloodOxygen.setText("Blood Oxygen Level: " + healthData.blood_oxygen +"%\n# (Hypoxemia) \n- Advice: Seek immediate medical attention for evaluation and management of low blood oxygen levels.");
        }
    }

    private void updateBloodSugarStatus(String bloodSugar) {
        int glucoseLevel = Integer.parseInt(bloodSugar);

        if (glucoseLevel >= 70 && glucoseLevel <= 100) {
            textViewBloodSugar.setText("Blood Sugar Level: " + healthData.blood_sugar +" mg/dL\n# (Normal Range) \n- Advice: Maintain a balanced diet, regular physical activity, and monitor blood sugar levels as advised by healthcare professionals.");
        } else if (glucoseLevel >= 101 && glucoseLevel <= 125) {
            textViewBloodSugar.setText("Blood Sugar Level: " + healthData.blood_sugar +" mg/dL\n# (Pre-diabetes) \n- Advice: Implement lifestyle changes, including diet and exercise, to prevent progression to diabetes. Consult with healthcare professionals.");
        } else {
            textViewBloodSugar.setText("Blood Sugar Level: " + healthData.blood_sugar +" mg/dL\n# (Diabetes) \n- Advice: Seek guidance from healthcare professionals for comprehensive diabetes management, including medication, diet, and lifestyle adjustments.");
        }
    }

    private void updateSmokingHabitStatus(String smokingHabit) {
        if ("Yes".equalsIgnoreCase(smokingHabit)) {
            textViewSmokingHabit.setText("Smoking Habit: Yes \n- Advice: Quit smoking for better health. Seek support from healthcare professionals, friends, and family.");
        } else {
            textViewSmokingHabit.setText("Smoking Habit: No \n- Advice: Keep it smoke-free for a healthier lifestyle.");
        }
    }

}