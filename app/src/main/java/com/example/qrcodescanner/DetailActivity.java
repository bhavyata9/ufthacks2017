package com.example.qrcodescanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.util.HashMap;

public class DetailActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener {

    private SliderLayout imageSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        HashMap<String, String> info = (HashMap<String, String>)intent.getSerializableExtra("info");
        HashMap<String, String> image_maps = (HashMap<String, String>)intent.getSerializableExtra("images");
        String area = info.get("area") + " sqft";
        String price = "$ " +info.get("price");
        String type = info.get("type");
        String address = info.get("address");
        String num_baths = info.get("num_baths");
        String num_beds = info.get("num_beds");
        String parking_type = info.get("parking_type");
        String basement = info.get("basement");
        if(basement.equals("true")){
            basement = "Finsihed";
        }else{
            basement = "Not Finished";
        }
        String age = info.get("age")+ " years";

        setContentView(R.layout.activity_detail);
        imageSlider = (SliderLayout)findViewById(R.id.slider);
        // creating HashMap

//        image_maps.put("Main","https://scontent-yyz1-1.xx.fbcdn.net/v/t31.0-8/13442625_10154209253674840_4944598537700216261_o.jpg?oh=04d94b335fc2ab59b3671984bb415941&oe=591905E2");
//        image_maps.put("One","https://scontent-yyz1-1.xx.fbcdn.net/v/t31.0-8/13418516_10154209253684840_2151047237362795438_o.jpg?oh=b1b53d3fd5c60ffe84bbc8b4ccc59634&oe=5907D58C");
//        image_maps.put("Two","https://scontent-yyz1-1.xx.fbcdn.net/v/t31.0-8/13433169_10154209253679840_3684504477296386838_o.jpg?oh=cf0a73528f8c4e9095751ba9287c2b81&oe=591E9B73");
//        image_maps.put("Three","https://scontent-yyz1-1.xx.fbcdn.net/v/t31.0-8/13433129_10154209254629840_3115777736956481051_o.jpg?oh=d6fcdaf52c9e30d96386302d5b690a7a&oe=5921D665");

        for(String name : image_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(image_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            imageSlider.addSlider(textSliderView);
        }

        imageSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        imageSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        imageSlider.setCustomAnimation(new DescriptionAnimation());
        imageSlider.setDuration(4000);
        imageSlider.addOnPageChangeListener(this);

    }
    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this,slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        Log.e("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {}
}
