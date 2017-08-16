package com.example.dell.carrace;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.support.wearable.internal.view.SwipeDismissLayout;
import android.support.wearable.view.DismissOverlayView;
import android.support.wearable.view.SwipeDismissFrameLayout;
import android.support.wearable.view.WatchViewStub;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private TextView mTextView;

    // Our object to handle the View
    private ParallaxView parallaxView;
    private DismissOverlayView dol;
    private SwipeDismissFrameLayout frm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);
        //final DismissOverlayView stub = (DismissOverlayView) findViewById(R.id.watch_view_stub);

        // Get a Display object to access screen details
        Display display = getWindowManager().getDefaultDisplay();

        // Load the resolution into a Point object
        Point resolution = new Point();
        display.getSize(resolution);

        // And finally set the view for our game
        parallaxView = new ParallaxView(this, resolution.x, resolution.y);

        //FrameLayout frm = (FrameLayout)findViewById(R.id.root_container);
        //frm.setFocusable(true);
        frm = (SwipeDismissFrameLayout) findViewById(R.id.swipe_dismiss_root);
        //frm.canScrollHorizontally(1);
        //frm.setDismissEnabled(false);


        // Make our parallaxView the view for the Activity
        //setContentView(parallaxView);

       // frm.addCallback(new SwipeDismissFrameLayout.Callback() {
                    //               @Override
                     //             public void onDismissed(SwipeDismissFrameLayout layout) {
        //                             layout.setVisibility(View.VISIBLE);
        //                           }
                      //         }
        //);

        //frm.addView(R.layout.activity_main);
        frm.addView(parallaxView);

        //SwipeDismissFrameLayout testLayout = (SwipeDismissFrameLayout) findViewById(R.id.watch_view_stub);


    }

    public void change(){

        frm.addView(parallaxView);
    }


    // If the Activity is paused make sure to pause our thread
    @Override
    protected void onPause() {
        super.onPause();
        parallaxView.pause();
    }

    // If the Activity is resumed make sure to resume our thread
    @Override
    protected void onResume() {
        super.onResume();
        parallaxView.resume();
    }



}
