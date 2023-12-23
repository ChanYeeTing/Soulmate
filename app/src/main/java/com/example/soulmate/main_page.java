package com.example.soulmate;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.soulmate.databinding.ActivityMainPageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class main_page extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainPageBinding binding;
    TextView username;
    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );

        binding = ActivityMainPageBinding.inflate ( getLayoutInflater () );
        setContentView ( binding.getRoot () );

        setSupportActionBar ( binding.appBarMainPage.toolbar );
/*        binding.appBarMainPage.fab.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick ( View view ) {
                Snackbar.make ( view, "Replace with your own action", Snackbar.LENGTH_LONG )
                        .setAction ( "Action", null ).show ();
            }
        } );*/
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder (
                R.id.nav_home, R.id.nav_medical_history, R.id.nav_settings, R.id.nav_telemedicine, R.id.nav_date_tracking)
                .setOpenableLayout ( drawer )
                .build ();
        NavController navController = Navigation.findNavController ( this, R.id.nav_host_fragment_content_main_page );
        NavigationUI.setupActionBarWithNavController ( this, navController, mAppBarConfiguration );
        NavigationUI.setupWithNavController ( navigationView, navController );

        //Edit username
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser(); // Check for null here
        if (currentUser != null) {
            String uid = currentUser.getUid();
            NavigationView navigationView1 = binding.navView;
            View headerView = navigationView1.getHeaderView(0);

            //set username
            username = headerView.findViewById(R.id.username);
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.child(uid).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        if (task.getResult().exists()) {
                            DataSnapshot dataSnapshot = task.getResult();
                            String name = String.valueOf(dataSnapshot.child("name").getValue());
                            username.setText(name);
                        }
                    }
                }
            });
        }

    }

    @Override
    public boolean onCreateOptionsMenu ( Menu menu ) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater ().inflate ( R.menu.main_page, menu );
        return true;
    }

    @Override
    public boolean onSupportNavigateUp () {
        NavController navController = Navigation.findNavController ( this, R.id.nav_host_fragment_content_main_page );
        return NavigationUI.navigateUp ( navController, mAppBarConfiguration )
                || super.onSupportNavigateUp ();
    }

    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sign_out) {
            signOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        navigateToLoginFragment();
    }

    private void navigateToLoginFragment() {
        // Use the view parameter to find the NavController associated with the current view hierarchy
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main_page);

        // Navigate to the login destination
        navController.navigate(R.id.action_global_login);
    }
}