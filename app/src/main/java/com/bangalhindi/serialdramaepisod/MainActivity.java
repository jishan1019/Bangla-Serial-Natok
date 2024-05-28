package com.bangalhindi.serialdramaepisod;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.appupdate.AppUpdateOptions;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    String url = decryptUrl(Config.BASE_URL);

    CardView btn1, btn2, btn3, btn4;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    TextView noticeTitle;

    private InterstitialAd fbIntAds;
    private AdView fbadView;
    LinearLayout fbBanLayout,maxBanLayout;
    private static final int MY_REQUEST_CODE = 1001;
    private AppUpdateManager appUpdateManager;
    public ProgressDialog progressDialog;



    private String decryptUrl(String encryptedUrl) {
        byte[] decodedBytes = Base64.decode(encryptedUrl, Base64.DEFAULT);
        return new String(decodedBytes);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dower_navigation);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);



        //--------- App update manager ----------------
        appUpdateManager = AppUpdateManagerFactory.create(this);

        // Check for updates
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                // Request the update
                startUpdateFlow(appUpdateInfo);
            }
        });


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
        noticeTitle = activityMain.findViewById(R.id.noticeTitle);
        fbBanLayout = activityMain.findViewById(R.id.fbBanLayout);


        noticeTitle.setSelected(true);


        //-------------- Netowrk Check -----------------

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo!=null && networkInfo.isConnected()){
            fetchAdsData();
        }else {
            new AlertDialog.Builder(MainActivity.this)
                    .setIcon(getDrawable(R.drawable.ic_baseline_signal_wifi_connected_no_internet_4_24))
                    .setTitle("No internet Connection")
                    .setMessage("Dear user please connect internet!")
                    .setCancelable(false)
                    .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .show();
        }



        //------- All Action Button Code ----------------

        allBtn();
        navigationView();


    }

    //------- All CLick listener code ----------------

    private void allBtn(){
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllIntAds();
                startActivity(new Intent(MainActivity.this, ServerActivity.class));
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllIntAds();
                startActivity(new Intent(MainActivity.this, ServerActivity.class));
            }
        });


        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllIntAds();
                startActivity(new Intent(MainActivity.this, ServerActivity.class));
            }
        });


        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllIntAds();
                startActivity(new Intent(MainActivity.this, ServerActivity.class));
            }
        });
    }



    //------------- Apps Update Code -------------------
    private void startUpdateFlow(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    this,
                    AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build(),
                    MY_REQUEST_CODE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode != RESULT_OK) {
                // Handle update failure
                Log.e("MainActivity", "Update flow failed! Result code: " + resultCode);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                startUpdateFlow(appUpdateInfo);
            }
        });
    }

    //-------------- Fetch Data -------------------------

    private void fetchAdsData(){
        progressDialog.show();
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = response.getJSONObject("aInfo");

                    String applovinSdkKey = jsonObject.getString("asK");
                    String applovinMaxId = jsonObject.getString("aMI");
                    String facebookIntId = jsonObject.getString("fbIND");
                    String facebookBannerId = jsonObject.getString("fbBID");
                    String isAdsOpen = jsonObject.getString("aSts");

                    Config.ApplovinSdkKey = applovinSdkKey;
                    Config.ApplovinMaxId = applovinMaxId;
                    Config.FacebookIntId = facebookIntId;
                    Config.FacebookBanId = facebookBannerId;
                    Config.isAdsOpen = isAdsOpen.contains("yes");

                    progressDialog.dismiss();

                    try {
                        ApplicationInfo applicationInfoApplovin = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
                        applicationInfoApplovin.metaData.putString("applovin.sdk.key", applovinSdkKey);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    AudienceNetworkAds.initialize(MainActivity.this);

                    AppLovinSdk.getInstance( MainActivity.this ).setMediationProvider( "max" );
                    AppLovinSdk.initializeSdk( MainActivity.this, new AppLovinSdk.SdkInitializationListener() {
                        @Override
                        public void onSdkInitialized(final AppLovinSdkConfiguration configuration)
                        {
                        }
                    } );
                    

                    if(Config.isAdsOpen){
                        FacebookActivity.loadFbBannerAds(MainActivity.this, fbBanLayout);
                        FacebookActivity.loadFbIntAds(MainActivity.this);
                        ApplovinActivity.loadApplovinInterstitial(MainActivity.this);
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(objectRequest);

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

                } else if (id == R.id.nav_fbgroup) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/profile.php?id=61560263304357"));
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

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


    //----------- ADS Logic ------------------------------
    private void showAllIntAds(){
        Random random = new Random();
        int randomNumber = random.nextInt(100)+1;
        if(randomNumber >=2 && randomNumber <=20 || randomNumber >=61 && randomNumber <=75 ){
            ApplovinActivity.showApplivinAds();
        }if(randomNumber >=35 && randomNumber <=50 || randomNumber >=85 && randomNumber <=99 ){
            FacebookActivity.showFbIntAds();
        }
    }


    @Override
    protected void onDestroy() {
        if (fbadView != null) {
            fbadView.destroy();
        }
        if (fbIntAds != null) {
            fbIntAds.destroy();
        }
        super.onDestroy();
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