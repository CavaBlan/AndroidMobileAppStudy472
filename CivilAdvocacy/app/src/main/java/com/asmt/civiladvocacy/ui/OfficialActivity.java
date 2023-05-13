package com.asmt.civiladvocacy.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.asmt.civiladvocacy.R;
import com.asmt.civiladvocacy.data.entity.Office;
import com.asmt.civiladvocacy.data.entity.Official;
import com.asmt.civiladvocacy.databinding.OfficialActivityBinding;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.function.Consumer;

public class OfficialActivity extends AppCompatActivity {

    OfficialActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = OfficialActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String address = getIntent().getStringExtra("address");
        binding.tvLocation.setText(address);

        Office office = (Office) getIntent().getSerializableExtra("office");
        binding.tvName.setText(office.getOfficial().name);
        binding.tvOffice.setText(office.name);
        binding.tvParty.setText(String.format("(%s)", office.getOfficial().party));
        binding.tvAddress.setText(office.getOfficial().getAddress());
        binding.tvPhone.setText(office.getOfficial().getPhone());
        binding.tvEmail.setText(office.getOfficial().getEmail());
        binding.tvWebsite.setText(office.getOfficial().getUrl());
        String photoUrl = office.getOfficial().photoUrl;
        if (!photoUrl.isEmpty()) {
            Picasso.get().load(office.getOfficial().photoUrl)
                    .error(R.drawable.brokenimage)
                    .into(binding.ivPhoto);
        }

        String party = office.getOfficial().party;
        if (party.startsWith("Democratic")) {
            binding.ivPartyLogo.setVisibility(View.VISIBLE);
            binding.ivPartyLogo.setImageResource(R.drawable.dem_logo);
            binding.getRoot().setBackgroundColor(Color.BLUE);
        } else if (party.startsWith("Republican")) {
            binding.ivPartyLogo.setVisibility(View.VISIBLE);
            binding.ivPartyLogo.setImageResource(R.drawable.rep_logo);
            binding.getRoot().setBackgroundColor(Color.RED);
        } else {
            binding.ivPartyLogo.setVisibility(View.GONE);
            binding.getRoot().setBackgroundColor(Color.BLACK);
        }

        binding.flFacebook.setVisibility(View.GONE);
        binding.flTwitter.setVisibility(View.GONE);
        binding.flYoutube.setVisibility(View.GONE);
        office.getOfficial().channels.forEach(channel -> {
            if ("Facebook".equals(channel.type)) {
                binding.flFacebook.setVisibility(View.VISIBLE);
                binding.ivFacebook.setOnClickListener(v -> {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setPackage("com.facebook.katana");
                        intent.setData(Uri.parse("fb://page/" + channel.id));
                        startActivity(intent);
                    } catch (Exception e) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://www.facebook.com/" + channel.id)));
                    }
                });
            } else if ("Twitter".equals(channel.type)) {
                binding.flTwitter.setVisibility(View.VISIBLE);
                binding.ivTwitter.setOnClickListener(v -> {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setPackage("com.twitter.android");
                        intent.setData(Uri.parse("twitter://user?screen_name=" + channel.id));
                        startActivity(intent);
                    } catch (Exception e) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://twitter.com/" + channel.id)));
                    }
                });
            } else if ("Youtube".equals(channel.type)) {
                binding.flYoutube.setVisibility(View.VISIBLE);
                binding.ivYoutube.setOnClickListener(v -> {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setPackage("com.google.android.youtube");
                        intent.setData(Uri.parse("https://www.youtube.com/" + channel.id));
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW,
                                Uri.parse("https://www.youtube.com/" + channel.id)));
                    }
                });
            }
        });

        binding.ivPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(this, PhotoDetailActivity.class);
            intent.putExtra("address", address);
            intent.putExtra("name", office.getOfficial().name);
            intent.putExtra("office", office.name);
            intent.putExtra("photoUrl", photoUrl);
            intent.putExtra("party", party);
            startActivity(intent);
        });
    }
}