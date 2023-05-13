package com.asmt.civiladvocacy.data;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.asmt.civiladvocacy.data.entity.Address;
import com.asmt.civiladvocacy.data.entity.Office;
import com.asmt.civiladvocacy.data.entity.Official;
import com.asmt.civiladvocacy.data.entity.State;
import com.asmt.civiladvocacy.data.response.OfficialListResponse;
import com.asmt.civiladvocacy.ui.OfficialListState;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Networks {
    private static final String TAG = "CA.Networks";
    private static volatile Networks networks;

    private Application context;
    private RequestQueue queue;
    public static Networks get() {
        if (networks == null) {
            networks = new Networks();
        }
        return networks;
    }

    public void init(Application context) {
        this.context = context;
        queue = Volley.newRequestQueue(context);
    }

    public void getCivicInformation(String input, Consumer<State> consumer) {
        if (!isNetworkConnected(context)) {
            consumer.accept(new State.Failure("No Network Connection",
                    "Data cannot be accessed/loaded without an internet connection."));
            return;
        }
        String endpoint = "civicinfo/v2/representatives";
        String queryParameters = "?key=" + Constants.API_KEY + "&address=" + input;
        String url = Constants.BASE_URL + endpoint + queryParameters;
        Log.d(TAG, "getCivicInformation: " + url);
        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            try {
                JSONObject joResponse = new JSONObject(response);
                JSONObject joError = joResponse.optJSONObject("error");
                if (joError != null) {
                    String message = joError.getString("message");
                    consumer.accept(new State.Failure("Request Failed", message));
                } else {
                    JSONObject joAddress = joResponse.getJSONObject("normalizedInput");
                    Address address = Address.fromJson(joAddress);

                    List<Official> officialsCache = new ArrayList<>();
                    JSONArray jaOfficial = joResponse.getJSONArray("officials");
                    for (int i = 0; i < jaOfficial.length(); i++) {
                        JSONObject joOfficial = jaOfficial.getJSONObject(i);
                        officialsCache.add(Official.fromJson(joOfficial));
                    }

                    List<Office> offices = new ArrayList<>();
                    JSONArray jaOffices = joResponse.getJSONArray("offices");
                    for (int i = 0; i < jaOffices.length(); i++) {
                        JSONObject joOffice = jaOffices.getJSONObject(i);
                        offices.add(Office.fromJson(joOffice, officialsCache));
                    }

                    consumer.accept(new OfficialListState(
                            new OfficialListResponse(address, offices)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                consumer.accept(new State.Failure("Json parse failed",
                        "Data cannot be accessed/loaded."));
            }
        }, error -> {
            error.printStackTrace();
            consumer.accept(new State.Failure("Request failed",
                    "Data cannot be accessed/loaded."));
        });
        queue.add(request);
    }

    public static boolean isNetworkConnected(Context context) {
        context = context.getApplicationContext();
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Network network = cm.getActiveNetwork();
        if (network != null) {
            NetworkInfo networkInfo = cm.getNetworkInfo(network);
            return networkInfo.isConnected();
        }
        return false;
    }
}
