package com.bangalhindi.banglaserialnatok;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkConfiguration;
import com.facebook.ads.AudienceNetworkAds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class ServerActivity extends AppCompatActivity {

    String url = decryptUrl(Config.BASE_URL);
    GridView listView;

    ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
    HashMap<String,String> hashMap;
    ProgressBar proBar;

    LinearLayout fbBanLayout;


    private String decryptUrl(String encryptedUrl) {
        byte[] decodedBytes = Base64.decode(encryptedUrl, Base64.DEFAULT);
        return new String(decodedBytes);
    }

    private String encryptUrl(String url) {
        return Base64.encodeToString(url.getBytes(), Base64.DEFAULT);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        proBar = findViewById(R.id.proBar);
        listView = findViewById(R.id.listView);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("All Natok");
        }

        proBar.setVisibility(View.VISIBLE);

        fbBanLayout = findViewById(R.id.fbBanLayout);


        //------------- Ads Init Activity ---------------

        try {
            ApplicationInfo applicationInfoApplovin = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            applicationInfoApplovin.metaData.putString("applovin.sdk.key", Config.ApplovinSdkKey);
        } catch (Exception e) {
            e.printStackTrace();
        }

        AudienceNetworkAds.initialize(ServerActivity.this);

        AppLovinSdk.getInstance( ServerActivity.this ).setMediationProvider( "max" );
        AppLovinSdk.initializeSdk( ServerActivity.this, new AppLovinSdk.SdkInitializationListener() {
            @Override
            public void onSdkInitialized(final AppLovinSdkConfiguration configuration)
            {
            }
        } );

        if(Config.isAdsOpen){
            FacebookActivity.loadFbBannerAds(ServerActivity.this, fbBanLayout);
            FacebookActivity.loadFbIntAds(ServerActivity.this);
            ApplovinActivity.loadApplovinInterstitial(ServerActivity.this);
        }


        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo!=null && networkInfo.isConnected()){
            retrieveSpreadsheetData();
        }else {
            new AlertDialog.Builder(ServerActivity.this)
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


    }

    //------------- On Create End Here --------------

    private void retrieveSpreadsheetData() {
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                proBar.setVisibility(View.GONE);

                try {
                    JSONArray itemsArray = response.getJSONArray("values");

                    for (int x = 0; x < itemsArray.length(); x++) {
                        JSONObject jsonObject = itemsArray.getJSONObject(x);

                        String vdo_title = jsonObject.getString("vdo_title");
                        String vdo_url = jsonObject.getString("vdo_url");
                        String date = jsonObject.getString("date");


                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("vdo_title", vdo_title);
                        hashMap.put("vdo_url", vdo_url);
                        hashMap.put("date", date);

                        arrayList.add(hashMap);
                    }

                    ChannelAdaptar adapter = new ChannelAdaptar();
                    listView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                    proBar.setVisibility(View.GONE);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(ServerActivity.this);
        requestQueue.add(objectRequest);


    }



    public class ChannelAdaptar extends BaseAdapter {
        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View myView = layoutInflater.inflate(R.layout.list_item_natok,parent,false);

            LinearLayout playVideoBtn = myView.findViewById(R.id.playVideoBtn);
            TextView all_channel_titel = myView.findViewById(R.id.textViewNatokTitle);
            TextView epsdDate = myView.findViewById(R.id.epsdDate);

            HashMap<String,String> hashMap = arrayList.get(position);

            String vdo_title = hashMap.get("vdo_title");
            String vdo_url = hashMap.get("vdo_url");
            String date = hashMap.get("date");

            all_channel_titel.setText(vdo_title);
            epsdDate.setText(date);

            playVideoBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    showAllIntAds();

                    Intent intent = new Intent(ServerActivity.this, PlayVideo.class);
                    intent.putExtra("dataTitle", vdo_title + " | " + date);
                    intent.putExtra("dataUrl", vdo_url);
                    ServerActivity.this.startActivity(intent);
                }
            });

            return myView;
        }
    }

    //----------------- All Ads Show Code here --------------
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}