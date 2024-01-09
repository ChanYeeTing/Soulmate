package com.example.soulmate.ui.telemedicine;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.soulmate.R;
import com.example.soulmate.databinding.FragmentTelemedicineBinding;

import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;

import java.net.MalformedURLException;
import java.net.URL;


public class TelemedicineFragment extends Fragment {

    private FragmentTelemedicineBinding binding;
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

                // Check if the permission is granted
                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.FOREGROUND_SERVICE_MEDIA_PROJECTION)
                        != PackageManager.PERMISSION_GRANTED) {
                    // Permission is not granted, request it
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.FOREGROUND_SERVICE_MEDIA_PROJECTION},
                            REQUEST_CODE); // Replace REQUEST_CODE with your own code
                }
                else {
                    String text = input.getText().toString().trim();
                    if(text.length()>0)
                    {
                        JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
                                .setRoom(text)
                                .setFeatureFlag("invite.enabled", false)
                                .setAudioMuted(true)
                                .setVideoMuted(true)
                                .build();
                        JitsiMeetActivity.launch(getActivity(), options);
                    }
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