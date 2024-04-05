package com.alexandra.virtual_vogue_mobile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class landingPage extends AppCompatActivity {

    String TAG = "LandingActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        try {

            BottomNavigationView navigationView = findViewById(R.id.landing_nav_view);
            NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().
                    findFragmentById(R.id.nav_host_landing_fragment);

            if (navHostFragment != null) {
                NavController navController = navHostFragment.getNavController();

                AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                        R.id.navigation_myoutfits, R.id.navigation_createOutfit, R.id.navigation_settings).build();

                NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
                NavigationUI.setupWithNavController(navigationView, navController);
            }

            Log.d(TAG, "onCreate: NavBar Setup");
        } catch(Exception e){
            Log.e(TAG, "onCreate: Error on creation", e);
            onCreate(savedInstanceState);
        }

    }
}