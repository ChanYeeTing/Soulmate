package com.example.soulmate;

import static androidx.fragment.app.FragmentManager.TAG;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegistrationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrationFragment extends Fragment {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

   private DatePickerDialog.OnDateSetListener DateSetListener;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegistrationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistrationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegistrationFragment newInstance(String param1, String param2) {
        RegistrationFragment fragment = new RegistrationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration, container, false);
    }

    @Override
    public void onActivityCreated ( @Nullable Bundle savedInstanceState ) {
        super.onActivityCreated ( savedInstanceState );
        Button login, register2 ;
        firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference ref = firebaseDatabase.getReference();

        login = getView () .findViewById ( R.id.loginButton );
        login.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick ( View v ) {
                NavController controller = Navigation.findNavController ( v );
                controller.navigate ( R.id.action_registrationFragment_to_login_fragment );
            }
        } );
               TextView DOB = getView().findViewById(R.id.dateOfBirth);
               DOB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar cal = Calendar.getInstance();
                        int year = cal.get(Calendar.YEAR);
                        int month = cal.get(Calendar.MONTH);
                        int day = cal.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog dialog = new DatePickerDialog(
                                getActivity(),
                                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                                DateSetListener,
                                year, month, day
                        );

                        dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.show();

                    }
       });
       DateSetListener = new DatePickerDialog.OnDateSetListener() {
           @Override
           public void onDateSet(DatePicker view, int year, int month, int day) {
               month = month + 1;
               Log.d(TAG, "onDateSet: dd/mm/yyyy: " + day + "/" + month + "/" + year);
               String date = day + "/" + month + "/" + year;
               DOB.setText(date);
           }

       };

        register2 = getView ().findViewById ( R.id.registerButton );


        register2.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick ( View v ) {



                EditText name = getView().findViewById(R.id.username);
                EditText email = getView().findViewById(R.id.emailAddress);
                EditText number = getView().findViewById(R.id.userPhone);


                RadioGroup rg = getView().findViewById(R.id.timeSelector );
                int genid = rg.getCheckedRadioButtonId();
                RadioButton gender = getView().findViewById(genid);
//        RadioButton gender = getView().findViewById(rg.getCheckedRadioButtonId())


                EditText password = getView().findViewById(R.id.password);
                EditText Cpassword = getView().findViewById(R.id.confirmPassword);
                String getName = name.getText().toString().trim();
                String getEmail = email.getText().toString().trim();
                String getNumber = number.getText().toString().trim();
                String getDOB = DOB.getText().toString();
                String getpassword = password.getText().toString().trim();
                String getCpassword = Cpassword.getText().toString().trim();




                if (!getName.isEmpty() && !getEmail.isEmpty() && !getNumber.isEmpty() && !getDOB.equals("Date of Birth") && genid!=-1
                        && !getpassword.isEmpty() && !getCpassword.isEmpty()) {
                    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                    if (getEmail.matches(emailPattern)) {



                        if (getpassword.length() >= 6 && getCpassword.length() >= 6) {

                            if (getpassword.equals(getCpassword)) {
                                FirebaseAuth auth = FirebaseAuth.getInstance();
                                auth.createUserWithEmailAndPassword(getEmail, getpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            String getGender = gender.getText().toString();
                                            FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
                                            Toast.makeText(getActivity(), "Verification Link Sent", Toast.LENGTH_SHORT).show();
                                            String user_id = auth.getCurrentUser().getUid();
                                            DatabaseReference userRef = ref.child("Users").child(user_id).child("User Info");
                                            HashMap<String, Object> hashMap = new HashMap<>();
                                            hashMap.put("name", getName);
                                            hashMap.put("email", getEmail);
                                            hashMap.put("Mobile Number", getNumber);
                                            hashMap.put("Date of Birth", getDOB);
                                            hashMap.put("Gender", getGender);
                                            hashMap.put("Password", getpassword);
                                            userRef.setValue(hashMap);
                                            NavController controller = Navigation.findNavController(v);
                                            controller.navigate(R.id.action_registrationFragment_to_emailVerificationFragment);

                                        } else {
                                            Toast.makeText(getActivity(), "Error! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        if (e instanceof FirebaseAuthUserCollisionException) {
                                            email.setError("Email Already Registered");
                                            email.requestFocus();
                                        }
                                    }
                                });


                            } else {
                                Cpassword.setError("Password not matched");
                                Cpassword.requestFocus();
                                Cpassword.setText("");
                                //                        Toast.makeText(getActivity(),"Password Not Same", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Password should be >= 6 characters", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else {
                        email.setError("Invalid email address");
                        email.requestFocus();
                    }

                    }
                else {
                    Toast.makeText(getActivity(), "Not Empty Field Allowed", Toast.LENGTH_SHORT).show();
                }

            }
        } );
    }

    private void sendEmailVerification() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null)
        {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(getActivity(), "Check your Email for verification", Toast.LENGTH_SHORT).show();
                        FirebaseAuth.getInstance().signOut();
                    }
                }
            });
        }
    }







}