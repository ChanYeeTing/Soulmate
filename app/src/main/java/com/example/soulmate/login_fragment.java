package com.example.soulmate;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login_fragment extends Fragment {

    EditText loginEmail, loginPassword;
    Button loginButton;
    TextView signupRedirectText;

    private FirebaseAuth firebaseAuth;
    public static String PREFS_NAME = "MyPrefsFile";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        checkBox();

        firebaseAuth = FirebaseAuth.getInstance();

        loginEmail = getView().findViewById(R.id.EmailAddress);
        loginPassword = getView().findViewById(R.id.Password);
        signupRedirectText = getView().findViewById(R.id.RegisterButton);
        loginButton = getView().findViewById(R.id.LoginButton);

        signupRedirectText.setOnClickListener(v -> {
            NavController controller = Navigation.findNavController(v);
            controller.navigate(R.id.action_login_fragment_to_registrationFragment);
        });

        loginButton.setOnClickListener(v -> {
            String email = loginEmail.getText().toString().trim();
            String password = loginPassword.getText().toString().trim();

            if (validateEmail(email) && validatePassword()) {
                loginUser(email, password);
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREFS_NAME,0);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.putBoolean("hasLoggedIn", true);
                editor.apply();
            }
        });

        Button admin = getView ().findViewById ( R.id.adminLoginButton );
        admin.setOnClickListener(v -> {
            NavController controller = Navigation.findNavController(v);
            controller.navigate(R.id.action_login_fragment_to_adminLoginFragment);
        });
    }

    private Boolean validateEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (!email.matches(emailPattern)) {
            loginEmail.setError("Invalid email address");
            return false;
        } else {
            loginEmail.setError(null);
            return true;
        }
    }

    private Boolean validatePassword() {
        String val = loginPassword.getText().toString();
        if (val.isEmpty()) {
            loginPassword.setError("Password cannot be empty");
            return false;
        } else {
            loginPassword.setError(null);
            return true;
        }
    }

    private void loginUser(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(requireActivity(), task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        if (user != null && user.isEmailVerified()) {
                            Toast.makeText(getActivity(), "Login successful", Toast.LENGTH_SHORT).show();

                            NavController controller = Navigation.findNavController(getView());
                            controller.navigate(R.id.action_login_fragment_to_main_page);
                        } else {
                            Toast.makeText(getActivity(), "Please verify your email", Toast.LENGTH_SHORT).show();
                            FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Login failed. Check your credentials", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    //Skip login if already login
    private void checkBox()
    {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREFS_NAME, 0);
        Boolean check = sharedPreferences.getBoolean("hasLoggedIn", false);
        if(check)
        {
            NavController controller = Navigation.findNavController(getView());
            controller.navigate(R.id.action_login_fragment_to_main_page);
        }
    }
}
