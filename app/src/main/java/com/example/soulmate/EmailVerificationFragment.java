package com.example.soulmate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EmailVerificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmailVerificationFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public EmailVerificationFragment () {
        // Required empty public constructor
    }

    public static EmailVerificationFragment newInstance ( String param1, String param2 ) {
        EmailVerificationFragment fragment = new EmailVerificationFragment ();
        Bundle args = new Bundle ();
        args.putString ( ARG_PARAM1, param1 );
        args.putString ( ARG_PARAM2, param2 );
        fragment.setArguments ( args );
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
    public View onCreateView ( LayoutInflater inflater, ViewGroup container,
                               Bundle savedInstanceState ) {
        // Inflate the layout for this fragment
        return inflater.inflate ( R.layout.fragment_email_verification, container, false );
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button resend = getView().findViewById(R.id.resendButton);
        Button main = getView().findViewById(R.id.continue_main);
        TextView message = getView().findViewById(R.id.message);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        FirebaseAuth.AuthStateListener listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                auth.getCurrentUser().reload();

                if (!user.isEmailVerified()) {
                    resend.setVisibility(View.VISIBLE);
                    message.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getActivity(), "Successful Registered", Toast.LENGTH_SHORT).show();
                    NavController controller = Navigation.findNavController(getView());
                    controller.navigate(R.id.action_emailVerificationFragment_to_login_fragment);
                }
            }
        };

        // Register the AuthStateListener
        auth.addAuthStateListener(listener);

        main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().getCurrentUser().reload();

                if (!user.isEmailVerified()) {
                    Toast.makeText(getActivity(), "Email Not Verified", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Successful Registered", Toast.LENGTH_SHORT).show();
                    NavController controller = Navigation.findNavController(v);
                    controller.navigate(R.id.action_emailVerificationFragment_to_login_fragment);
                }
            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
                Toast.makeText(getActivity(), "Verification Link Sent", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
