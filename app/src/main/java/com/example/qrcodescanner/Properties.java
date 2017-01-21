package com.example.qrcodescanner;

import android.app.Application;

import com.firebase.client.Firebase;

import static android.R.attr.handle;

/**
 * Created by bhavyashah on 2017-01-21.
 */

public class Properties extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        Firebase.setAndroidContext(this);
    }





}
