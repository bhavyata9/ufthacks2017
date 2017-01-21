package com.example.qrcodescanner;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by bhavyashah on 2017-01-21.
 */

public class MainActivity extends Activity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    private Firebase mRef;
    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void onClick(View v){
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }
    @Override
    protected void onPause(){
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result result){
        // Do anything with result here :D

       Log.w("handleResult",result.getText());
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Scan result");
//       // System.out.println(result.getText());
//        builder.setMessage(result.getText());
//        AlertDialog alertDialog = builder.create();
//        alertDialog.show();
        checkDataBase(result.getText());
        //Resume scanning
        mScannerView.resumeCameraPreview(this);


    }

    public void  checkDataBase(final String qrID){
        mRef = new Firebase("https://qrcodescanner-8506c.firebaseio.com/properties");
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog.Builder notFound = new AlertDialog.Builder(this);
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshots) {
                boolean found = false;
                for (DataSnapshot snapshot: snapshots.getChildren()) {
                    if(qrID.equals(snapshot.getKey())){
                        found = true;
                        builder.setTitle("Match found");
                        builder.setMessage(qrID);
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                }
                if(!found){
                    notFound.setTitle("no match found");
                    notFound.setMessage(qrID);
                    AlertDialog notDialog = notFound.create();
                    notDialog.show();

                }


            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

}
