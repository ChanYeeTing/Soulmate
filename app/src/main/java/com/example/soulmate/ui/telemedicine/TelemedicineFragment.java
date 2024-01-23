package com.example.soulmate.ui.telemedicine;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.soulmate.R;
import com.example.soulmate.databinding.FragmentTelemedicineBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;


public class TelemedicineFragment extends Fragment {

    private FragmentTelemedicineBinding binding;
    private DatabaseReference userReference;
    static String value ="";
    Button button;
    EditText input;
    int REQUEST_CODE=123;

    public View onCreateView ( @NonNull LayoutInflater inflater,
                               ViewGroup container, Bundle savedInstanceState ) {
        TelemedicineViewModel telemedicineViewModel =
                new ViewModelProvider ( this ).get ( TelemedicineViewModel.class );

        binding = FragmentTelemedicineBinding.inflate ( inflater, container, false );
        View root = binding.getRoot ();

        return root;
    }

    @Override
    public void onDestroyView () {
        super.onDestroyView ();
        binding = null;
    }

    @Override
    public void onActivityCreated ( @Nullable Bundle savedInstanceState ) {
        super.onActivityCreated(savedInstanceState);
        URL serverUrl;
        input = getView().findViewById(R.id.roomId);
        button = getView().findViewById(R.id.button);
        String text = input.getText().toString().trim();
        button.setEnabled(text.length()==10);
        TextWatcher submitTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String text = input.getText().toString().trim();
                button.setEnabled(text.length()==10);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = input.getText().toString().trim();
                if(text.length()>10)
                {
                    input.setError("Room code must be 10 digit");
                    input.requestFocus();
                    button.setEnabled(text.length()==10);
                }
                else{
                    button.setEnabled(text.length()==10);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        input.addTextChangedListener(submitTextWatcher);

        try {
            serverUrl = new URL("https://meet.jit.si");
            JitsiMeetConferenceOptions defaultOptions
                    = new JitsiMeetConferenceOptions.Builder()
                    .setServerURL(serverUrl)
                    .build();

            JitsiMeet.setDefaultConferenceOptions(defaultOptions);
;        }catch(MalformedURLException e)
        {
            e.printStackTrace();
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = input.getText().toString().trim();

                // Check if the permission is granted
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.FOREGROUND_SERVICE_MEDIA_PROJECTION)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted, request it
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.FOREGROUND_SERVICE_MEDIA_PROJECTION},
                            REQUEST_CODE); // Replace REQUEST_CODE with your own code
                }
                else {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Appointment");
                    databaseReference.child("Telemedicine").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            outerLoop:
                            for(DataSnapshot snapshot1: snapshot.getChildren()) {
                                String childKey = snapshot1.getKey();

                                DataSnapshot snapshotDate = snapshot.child(childKey);

                                for (DataSnapshot snapshotTime : snapshotDate.getChildren()) {
                                    String Key = snapshotTime.getKey();
                                    DataSnapshot snapshotData = snapshot1.child(Key);

                                    for (DataSnapshot snapshotUid : snapshotData.getChildren()) {
                                        String uidKey = snapshotUid.getKey();

                                        DataSnapshot snapshotDetail = snapshotTime.child(uidKey);

                                        if (snapshotDetail.exists()) {

                                            String text = input.getText().toString().trim();
                                            value = String.valueOf(snapshotDetail.child("zId").getValue());
                                        if (value.equals(text)) {
                                            JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                                                    .setRoom(text)
                                                    .setFeatureFlag("invite.enabled", false)
                                                    .setAudioMuted(true)
                                                    .setVideoMuted(true)
                                                    .build();
                                            JitsiMeetActivity.launch(getActivity(), options);
                                            break outerLoop;
                                        }
                                    }}}}
                            if(!value.equals(text))
                            {
                                Toast.makeText(getContext(), "Invalid Room Code", Toast.LENGTH_SHORT).show();
                            }
                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }


            }

        });


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            // Check if the permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted, proceed with service initialization or usage
            } else {
                // Permission denied, handle accordingly (possibly show a message to the user)
            }
        }
    }
}