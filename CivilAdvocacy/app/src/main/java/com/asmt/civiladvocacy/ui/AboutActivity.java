package com.asmt.civiladvocacy.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;

import com.asmt.civiladvocacy.databinding.AboutActivityBinding;

public class AboutActivity extends AppCompatActivity {

    AboutActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = AboutActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.tvHeadline.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        binding.tvHeadline.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://developers.google.com/civic-information/"));
            startActivity(intent);
        });

        binding.tvVersion.setText(String.format("Version: %s", getVersionCode(this)));
    }

    public static String getVersionCode(Context context) {
        String versionName = "";
        try {
            versionName = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }
}