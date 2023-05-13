package com.asmt.civiladvocacy.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.asmt.civiladvocacy.R;
import com.asmt.civiladvocacy.data.Constants;
import com.asmt.civiladvocacy.data.Locations;
import com.asmt.civiladvocacy.data.Networks;
import com.asmt.civiladvocacy.data.entity.State;
import com.asmt.civiladvocacy.data.response.OfficialListResponse;
import com.asmt.civiladvocacy.databinding.OfficialListActivityBinding;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class OfficeListActivity extends AppCompatActivity {
    private static final String TAG = "CA.OfficialList";

    State state;
    OfficeListAdapter listAdapter;
    OfficialListActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = OfficialListActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listAdapter = new OfficeListAdapter();
        listAdapter.setOnItemClickListener((view, position) -> {
            OfficialListResponse response = ((OfficialListState) state).data;
            Intent intent = new Intent(this, OfficialActivity.class);
            intent.putExtra("address", response.address.getAddress());
            intent.putExtra("office", listAdapter.getItem(position));
            startActivity(intent);
        });
        binding.recyclerView.setAdapter(listAdapter);

        requestPermission();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.official_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (state instanceof State.Success || state instanceof State.Failure) {
            menu.findItem(R.id.action_helper).setVisible(true);
            menu.findItem(R.id.action_search).setVisible(true);
        } else {
            menu.findItem(R.id.action_helper).setVisible(false);
            menu.findItem(R.id.action_search).setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (R.id.action_helper == item.getItemId()) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        } else if (R.id.action_search == item.getItemId()) {
            LinearLayout linearLayout = new LinearLayout(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            linearLayout.setPadding(64, 0, 64, 0);
            linearLayout.setLayoutParams(layoutParams);

            EditText editText = new EditText(this);
            editText.setLayoutParams(new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            linearLayout.addView(editText);
            new AlertDialog.Builder(this)
                    .setTitle("Enter Address")
                    .setView(linearLayout)
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("OK", (dialogInterface, i) -> {
                        String address = editText.getText().toString().trim();
                        Networks.get().getCivicInformation(address, this::showState);
                    }).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("MissingPermission")
    private void startLocation() {
        Locations.get().startLocation(
                LocationManager.GPS_PROVIDER, new Locations.LocationCallback() {
                    @Override
                    public void onSuccess(double latitude, double longitude) {
                        Locations.get().stopLocation();
                        onLocationSuccess(latitude, longitude);
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Locations.get().stopLocation();
                        showState(new State.Failure("", throwable.getMessage()));
                    }
                });
    }

    public void onLocationSuccess(double latitude, double longitude) {
        Log.d(TAG, "onLocationSuccess: " + Locale.getDefault());
        new Thread(() -> {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                if (addresses.size() > 0) {
                    Address address = addresses.get(0);
                    String result = String.format("%s,%s",
                            address.getLocality(), address.getPostalCode());
                    Log.d(TAG, "onLocationSuccess: " + result);
                    runOnUiThread(() -> Networks.get().getCivicInformation(result, this::showState));
                }
            } catch (IOException e) {
                Log.e(TAG, "onLocationSuccess: ", e);
                runOnUiThread(() -> showState(new State.Failure("Geocoder Failed",
                        e.getLocalizedMessage())));
            }
        }).start();
    }

    @SuppressLint("SetTextI18n")
    public void showState(State state) {
        boolean isLoading = state instanceof State.Loading;
        binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        if (state instanceof OfficialListState) {
            binding.recyclerView.setVisibility(View.VISIBLE);
            binding.llError.setVisibility(View.GONE);
            OfficialListState success = (OfficialListState) state;
            OfficialListResponse response = success.data;
            binding.tvLocation.setText(success.data.address.getAddress());
            listAdapter.submitList(response.offices);
        } else if (state instanceof State.Failure) {
            binding.recyclerView.setVisibility(View.GONE);
            binding.llError.setVisibility(View.VISIBLE);
            State.Failure failure = (State.Failure) state;
            binding.tvLocation.setText("No Data For Location");
            binding.tvErrorTitle.setText(failure.title);
            binding.tvErrorMessage.setText(failure.message);
        }
        this.state = state;
        invalidateOptionsMenu();
    }

    private void requestPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION },
                    Constants.REQUEST_CODE_LOCATION_PERMISSION);
        } else {
            startLocation();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == Constants.REQUEST_CODE_LOCATION_PERMISSION &&
                resultCode == RESULT_OK) {
            startLocation();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}