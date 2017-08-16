package com.example.dell.carrace;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.wearable.view.DismissOverlayView;
import android.support.wearable.view.SwipeDismissFrameLayout;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by DELL on 8/16/2017.
 */

public class StartActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        //final DismissOverlayView stub = (DismissOverlayView) findViewById(R.id.watch_view_stub);
        Button change = (Button) findViewById(R.id.test_butt);

        change.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClassName("com.example.dell.carrace","com.example.dell.carrace.MainActivity");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                //Toast.makeText(getApplicationContext(), "check", Toast.LENGTH_SHORT).show();
            }
        });
        //frm.addView(R.layout.activity_main);
        //   frm.addView(parallaxView);

        //SwipeDismissFrameLayout testLayout = (SwipeDismissFrameLayout) findViewById(R.id.watch_view_stub);


    }



    // If the Activity is paused make sure to pause our thread
    @Override
    protected void onPause() {
        super.onPause();
    }

    // If the Activity is resumed make sure to resume our thread
    @Override
    protected void onResume() {
        super.onResume();
    }




}
