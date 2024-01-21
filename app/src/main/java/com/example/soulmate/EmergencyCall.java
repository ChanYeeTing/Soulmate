package com.example.soulmate;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class EmergencyCall extends Fragment implements OnMapReadyCallback {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public static boolean isCall;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private GoogleMap googleMap;
    private MapView mapView;

    private Timer locationUpdateTimer;
    private TimerTask locationUpdateTask;

    private long lastNotificationTime = 0;

    public EmergencyCall() {
        // Required empty public constructor
    }

    public static EmergencyCall newInstance(String param1, String param2) {
        EmergencyCall fragment = new EmergencyCall();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_emergency_call, container, false);

        if (getActivity() instanceof main_page) {
            ((main_page) getActivity()).getSupportActionBar().setTitle("Emergency Call");
        }

        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        enableMyLocation();
        startLocationUpdates();
        startLocationUpdateTimer();
    }

    private void startLocationUpdateTimer() {
        locationUpdateTimer = new Timer();
        locationUpdateTask = new TimerTask() {
            @Override
            public void run() {
                // Call the method to update location
                updateLocation();
            }
        };

        // Schedule the timer to run every 30 seconds (adjust the interval as needed)
        locationUpdateTimer.schedule(locationUpdateTask, 0, 30000); // 30 seconds
    }

    private void stopLocationUpdateTimer() {
        if (locationUpdateTimer != null) {
            locationUpdateTimer.cancel();
            locationUpdateTimer = null;
        }
        if (locationUpdateTask != null) {
            locationUpdateTask.cancel();
            locationUpdateTask = null;
        }
    }

    private void updateLocation() {
        if (googleMap != null) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                updateLocationUI(location);
                            }
                        }
                    });
        }
    }

    private void enableMyLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                googleMap.setMyLocationEnabled(true);
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            }
        } else {
            googleMap.setMyLocationEnabled(true);
        }
    }

    private void startLocationUpdates() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    updateLocationUI(location);
                }
            }
        };

        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000); // 10 seconds
        locationRequest.setFastestInterval(5000); // 5 seconds
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
        }
    }


    private void updateLocationUI(Location location) {
        if (location != null) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

            // Use Geocoder to get the address from the location
            Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (addresses != null && !addresses.isEmpty()) {
                    Address address = addresses.get(0);

                    // Display the formatted address in the TextView
                    TextView addressTextView = getView().findViewById(R.id.textView5);
                    addressTextView.setText("Address: " + address.getAddressLine(0));

                    // Check if five minutes have passed since the last notification
                    long currentTime = System.currentTimeMillis();
                    if (currentTime - lastNotificationTime >= 5 * 60 * 1000) { // 5 minutes in milliseconds
                        saveAddressToFirebase(address.getAddressLine(0));
                        Toast.makeText(getContext(), "Notified admin and will call for help!", Toast.LENGTH_SHORT).show();

                        // Update the time of the last notification
                        lastNotificationTime = currentTime;
                    }

                    isCall = false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveAddressToFirebase(String address) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();

            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

            // Generate a unique key using push
            String newLocationKey = usersRef.child("Location").push().getKey();

            // Retrieve user information from "UserInfo" node
            usersRef.child("User Info").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String name = dataSnapshot.child("name").getValue(String.class);
                        String nameEmergency = dataSnapshot.child("nameEmergency").getValue(String.class);
                        String contactEmergency = dataSnapshot.child("contactEmergency").getValue(String.class);

                        // Save user information along with current location using the generated key
                        usersRef.child("Location").child(newLocationKey).child("currentLocation").setValue(address);
                        usersRef.child("Location").child(newLocationKey).child("name").setValue(name);
                        usersRef.child("Location").child(newLocationKey).child("nameEmergency").setValue(nameEmergency);
                        usersRef.child("Location").child(newLocationKey).child("contactEmergency").setValue(contactEmergency);

                        isCall = false;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Firebase", "Error saving data to Firebase: " + databaseError.getMessage());
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdateTimer();
        if (fusedLocationClient != null && locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
        }
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

}