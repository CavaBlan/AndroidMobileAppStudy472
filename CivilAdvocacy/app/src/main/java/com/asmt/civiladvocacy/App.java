package com.asmt.civiladvocacy;

import android.app.Application;

import com.asmt.civiladvocacy.data.Locations;
import com.asmt.civiladvocacy.data.Networks;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Networks.get().init(this);
        Locations.get().init(this);
    }
}
