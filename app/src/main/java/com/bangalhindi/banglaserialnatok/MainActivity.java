package com.bangalhindi.banglaserialnatok;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    CardView btn1, btn2, btn3, btn4;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dower_navigation);


        //------- Dower Navigation Code ----------------

        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FrameLayout container = findViewById(R.id.container);


        //------- Layout Inflater Code ----------------

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View activityMain = inflater.inflate(R.layout.activity_main, container, false);
        container.addView(activityMain);

        btn1 = activityMain.findViewById(R.id.btn1);
        btn2 = activityMain.findViewById(R.id.btn2);
        btn3 = activityMain.findViewById(R.id.btn3);
        btn4 = activityMain.findViewById(R.id.btn4);

        //------- All Action Button Code ----------------

        allBtn();
        navigationView();


    }

    //------- All CLick listener code ----------------

    private void allBtn(){
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ServerActivity.class));
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ServerActivity.class));
            }
        });


        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ServerActivity.class));
            }
        });


        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ServerActivity.class));
            }
        });
    }


    //------- Navigation view button code ----------------
    private void  navigationView(){
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle navigation view item clicks here.
                int id = item.getItemId();

                if (id == R.id.nav_home) {

                } else if (id == R.id.nav_feedback) {
                    openAppInPlayStore();

                }else if (id == R.id.nav_rating) {
                    openAppInPlayStore();

                }else if (id == R.id.nav_more) {
                    openPublisherPageInPlayStore();

                } else if (id == R.id.nav_update) {
                    openAppInPlayStore();

                } else if (id == R.id.nav_privacy) {
                    startActivity(new Intent(MainActivity.this, PrivacyActivity.class));
                }

                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    private void openAppInPlayStore() {
        String packageName = getPackageName(); // Get your app's package name

        try {
            // Try to open the app's page on the Play Store app
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // If the Play Store app is not installed, open the app's page on the Play Store website
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private void openPublisherPageInPlayStore() {
        // Specify the publisher name
        String publisherName = "DhakaDevCraft";

        try {
            // Construct the URI to search for apps published by the specified publisher
            Uri uri = Uri.parse("market://search?q=pub:" + publisherName);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // If the Play Store app is not installed, open the Play Store website
            Uri uri = Uri.parse("https://play.google.com/store/search?q=pub:" + publisherName);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }


    //------- Dower Select Item Button Code ----------------
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}