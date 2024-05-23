package com.bangalhindi.banglaserialnatok;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ServerActivity extends AppCompatActivity {

    String url = decryptUrl(Config.BASE_URL);
    GridView listView;

    ArrayList<HashMap<String,String>> arrayList = new ArrayList<>();
    HashMap<String,String> hashMap;

    ProgressBar proBar;


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

        proBar.setVisibility(View.VISIBLE);

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

                    Intent intent = new Intent(ServerActivity.this, PlayVideo.class);
                    intent.putExtra("dataTitle", vdo_title);
                    intent.putExtra("dataUrl", vdo_url);
                    ServerActivity.this.startActivity(intent);
                }
            });

            return myView;
        }
    }

}