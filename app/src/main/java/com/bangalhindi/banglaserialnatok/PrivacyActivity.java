package com.bangalhindi.banglaserialnatok;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class PrivacyActivity extends AppCompatActivity {

    TextView tvPrivacy;

    String htmlText = "<h2>What is Android?</h2>" + "<p> lorem ipsum dollar</p>";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);

        tvPrivacy = findViewById(R.id.tvPrivacy);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Privacy Policy");
        }

        tvPrivacy.setText(HtmlCompat.fromHtml(htmlText, 0));

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