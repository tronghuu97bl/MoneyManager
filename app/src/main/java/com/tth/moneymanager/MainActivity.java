package com.tth.moneymanager;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tth.moneymanager.Security.AES;
import com.tth.moneymanager.Security.RSAUtil;

import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {
    //private Util util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_stats, R.id.navigation_transaction,
                R.id.navigation_home, R.id.navigation_loan, R.id.navigation_investment)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        try {
            Bundle bundle = getIntent().getExtras();
            int a = bundle.getInt("result");
            if (a == 1) {
                Toast.makeText(this, "Add shopping transaction success.", Toast.LENGTH_SHORT).show();
            }
            if (a == 2) {
                Toast.makeText(this, "Add investment transaction success.", Toast.LENGTH_SHORT).show();
            }
            if (a == 3) {
                Toast.makeText(this, "Add loan transaction success.", Toast.LENGTH_SHORT).show();
            }
            if (a == 4) {
                Toast.makeText(this, "Add send/receive transaction success.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //util = new Util(this);

    }
}