package com.example.soulmate.ui.reminder;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

import com.example.soulmate.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class ReminderFragment extends Fragment {

    private DatabaseReference databaseReference;
    private EditText medicineNameEditText, pillOrDoseEditText;
    private Button addReminderButton, setAlarmButton, cancelAlarmButton;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    // Include the Medicine class here
    private static class Medicine {
        public String medicineName;
        public String pillOrDose;

        public Medicine() {
            // Default constructor required for Firebase
        }

        public Medicine(String medicineName, String pillOrDose) {
            this.medicineName = medicineName;
            this.pillOrDose = pillOrDose;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_reminder, container, false);

        // Initialize UI elements
        medicineNameEditText = root.findViewById(R.id.medicationName);
        pillOrDoseEditText = root.findViewById(R.id.pill_dose);
        addReminderButton = root.findViewById(R.id.addButton);
        setAlarmButton = root.findViewById(R.id.setAlarmBtn);
        cancelAlarmButton = root.findViewById(R.id.cancelAlarmBtn);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Medicines");

        // Set click listener for adding a reminder
        addReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Validate and save the data to Firebase
                saveDataToFirebase();
            }
        });

        // Set click listener for setting an alarm
        setAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        // Set click listener for canceling an alarm
        cancelAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarm();
            }
        });

        return root;
    }

    private void saveDataToFirebase() {
        // Get the medicine details
        String medicineName = medicineNameEditText.getText().toString().trim();
        String pillOrDose = pillOrDoseValidation(pillOrDoseEditText.getText().toString().trim());

        // Validate input
        if (medicineName.isEmpty() || pillOrDose.isEmpty()) {
            // Handle invalid input
            Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save data to Firebase
        Medicine medicineData = new Medicine(medicineName, pillOrDose);
        databaseReference.push().setValue(medicineData);

        // Optional: Provide user feedback
        Toast.makeText(getActivity(), "Medicine reminder added successfully", Toast.LENGTH_SHORT).show();

        // Clear the input fields after adding a reminder
        medicineNameEditText.setText("");
        pillOrDoseEditText.setText("");
    }

    private String pillOrDoseValidation(String input) {
        // Validate and normalize input for pill/dose
        try {
            int doseNumber = Integer.parseInt(input);

            // Check if the number is in the range of 1 to 9
            if (doseNumber >= 1 && doseNumber <= 9) {
                return String.valueOf(doseNumber); // Return the validated dose as a string
            } else {
                // Handle invalid input (outside the range 1-9)
                Toast.makeText(getActivity(), "Invalid input for pill/dose. Please enter a number between 1 and 9.", Toast.LENGTH_SHORT).show();
                return ""; // Or handle it in a way that fits your application
            }
        } catch (NumberFormatException e) {
            // Handle invalid input (not a number)
            Toast.makeText(getActivity(), "Invalid input for pill/dose. Please enter a number between 1 and 9.", Toast.LENGTH_SHORT).show();
            return ""; // Or handle it in a way that fits your application
        }
    }


    private void showTimePickerDialog() {
        // Use a TimePickerDialog to get the selected time from the user
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                requireContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(android.widget.TimePicker timePicker, int selectedHour, int selectedMinute) {
                        // Set the selected time to the calendar
                        Calendar selectedTime = Calendar.getInstance();
                        selectedTime.set(Calendar.HOUR_OF_DAY, selectedHour);
                        selectedTime.set(Calendar.MINUTE, selectedMinute);
                        selectedTime.set(Calendar.SECOND, 0);

                        // Set the alarm
                        setAlarm(selectedTime);
                    }
                },
                hour,
                minute,
                false
        );

        // Show the time picker dialog
        timePickerDialog.show();
    }


    private void setAlarm(Calendar calendar) {
        alarmManager = (AlarmManager) requireContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(requireContext(), AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(requireContext(), 0, intent, 0);

        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            Toast.makeText(requireContext(), "Alarm Set", Toast.LENGTH_SHORT).show();
        }
    }

    private void cancelAlarm() {
        if (alarmManager != null && pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
            Toast.makeText(requireContext(), "Alarm Canceled", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(requireContext(), "No alarm set to cancel", Toast.LENGTH_SHORT).show();
        }
    }

    // NotificationActivity class
    public static class NotificationActivity extends AppCompatActivity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            // Your notification activity code here
            // You might want to show some information or navigate to a specific fragment
        }
    }

    // AlarmReceiver class
    public static class AlarmReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Intent nextActivity = new Intent(context, NotificationActivity.class);
            nextActivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, nextActivity, 0);

            // Ensure that the notification channel is created
            createNotificationChannel(context);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "reminder_channel")
                    .setContentTitle("Medicine Reminder")
                    .setContentText("It's time for your medication")
                    .setAutoCancel(true)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent);

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            notificationManagerCompat.notify(123, builder.build());
        }

        private void createNotificationChannel(Context context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = "reminder_channel";
                String desc = "Channel for Medicine Reminders";
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel channel = new NotificationChannel("reminder_channel", name, importance);
                channel.setDescription(desc);

                NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}

