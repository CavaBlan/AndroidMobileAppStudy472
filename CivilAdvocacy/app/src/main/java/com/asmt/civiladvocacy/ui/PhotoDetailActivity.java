package com.asmt.civiladvocacy.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.asmt.civiladvocacy.R;
import com.asmt.civiladvocacy.databinding.PhotoDetailActivityBinding;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class PhotoDetailActivity extends AppCompatActivity {

    PhotoDetailActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = PhotoDetailActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String address = getIntent().getStringExtra("address");
        binding.tvLocation.setText(address);

        String name = getIntent().getStringExtra("name");
        binding.tvName.setText(name);

        String office = getIntent().getStringExtra("office");
        binding.tvOffice.setText(office);

        String photoUrl = getIntent().getStringExtra("photoUrl");
        if (!photoUrl.isEmpty()) {
            Picasso.get().load(photoUrl)
                    .error(R.drawable.brokenimage)
                    .into(binding.ivPhoto);
        }

        String party = getIntent().getStringExtra("party");
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
    }
}