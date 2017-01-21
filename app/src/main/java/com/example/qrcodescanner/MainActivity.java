package com.example.qrcodescanner;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.zxing.Result;

import java.util.HashMap;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static com.example.qrcodescanner.R.layout.detail_page;

/**
 * Created by bhavyashah on 2017-01-21.
 */

public class MainActivity extends Activity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    private Firebase mRef;
//    private SliderLayout imageSlider;

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
        System.out.println(result.getText());
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
                        String address = snapshot.child("address").getValue().toString();
                        String price = snapshot.child("price").getValue().toString();
                        String type = snapshot.child("type").getValue().toString();
                        String area = snapshot.child("size").getValue().toString();
                        String parking_type = snapshot.child("parking_type").getValue().toString();
                        String num_baths = snapshot.child("num_baths").getValue().toString();
                        String num_beds = snapshot.child("num_beds").getValue().toString();
                        String age  = snapshot.child("age").getValue().toString();
                        String basement = snapshot.child("basement").getValue().toString();
//                        String buy_or_sell = snapshot.child("buy_or_sell").getValue().toString();
                        HashMap<String,String> images = new HashMap<String, String>();
                        int index =0;
                        for (DataSnapshot image: snapshot.child("images").getChildren()) {
                            images.put(index+"",image.getValue().toString());
                            index++;
                        }
                        HashMap<String,String> info = new HashMap<String, String>();
                        info.put("address",address);
                        info.put("price",price);
                        info.put("type",type);
                        info.put("area",area);
                        info.put("parking_type",parking_type);
                        info.put("num_baths",num_baths);
                        info.put("num_beds",num_beds);
                        info.put("age",age);
                        info.put("basement",basement);
                        launchActivity(info,images);
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
//    @Override
//    public void onSliderClick(BaseSliderView slider) {
//        Toast.makeText(this,slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
//
//    @Override
//    public void onPageSelected(int position) {
//        Log.e("Slider Demo", "Page Changed: " + position);
//    }
//
//    @Override
//    public void onPageScrollStateChanged(int state) {}

    private void launchActivity(HashMap<String,String> info, HashMap<String,String> images)
    {
        Intent intent=new Intent(this,DetailActivity.class);
        intent.putExtra("info",info);
        intent.putExtra("images",images);
        startActivity(intent);
    }

}
