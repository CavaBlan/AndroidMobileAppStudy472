package com.asmt.civiladvocacy.data.entity;

import android.util.Log;

public interface State {
    String TAG = State.class.getSimpleName();
    class Success<T> implements State {
        public final T data;
        public Success(T data) {
            Log.d(TAG, "Success: " + data);
            this.data = data;
        }
    }

    class Loading implements State {
        public Loading() {
            Log.d(TAG, "Loading: ");
        }
    }

    class Failure implements State {
        public final int icon;
        public final String title;
        public final String message;

        public Failure(String title, String message) {
            this(0, title, message);
        }

        public Failure(int icon, String title, String message) {
            Log.d(TAG, "Failure: " + title);
            this.icon = icon;
            this.title = title;
            this.message = message;
        }
    }
}

