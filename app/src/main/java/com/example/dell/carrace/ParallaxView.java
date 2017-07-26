package com.example.dell.carrace;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Picture;
import android.graphics.drawable.Drawable;
import android.support.wearable.view.SwipeDismissFrameLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import static android.view.Gravity.RIGHT;

/**
 * Created by DELL on 7/17/2017.
 */

public class ParallaxView extends SurfaceView implements Runnable{

    ArrayList<Carobj> carobj;

    private volatile boolean running;
    private Thread gameThread = null;

    // For drawing
    private Paint paint;
    private Canvas canvas;
    private SurfaceHolder ourHolder;
    Drawable car;

    // Holds a reference to the Activity
    Context context;

    // Control the fps
    long fps =60;

    // Screen resolution
    int screenWidth;
    int screenHeight;

    //car attributes
    float speed = (float)0.7;
    float[] x1;
    float[] x2;

    //swipe variables
    private float cord1,cord2;
    static final int MIN_DISTANCE = 0;
    int curr_pos; // 0 for left and 1 for right

    //make grass
    Bitmap bm;
    Bitmap revBm;
    float bmStart=0;
    float bmRevStart=0;
    float bmLeft=0;
    float bmRight;
    boolean bmfirst =true;
    float Yjoin;

    //AlertDialog.Builder alertDialogBuilder;


    public ParallaxView(Context context, int screenWidth, int screenHeight) {
        super(context);
        this.context = context;

        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        // Initialize our drawing objects
        ourHolder = getHolder();
        paint = new Paint();
        // height is h
        // making 10 strips so beginning of strips
        x1= new float[]{0, (float) (2*0.053*screenHeight),(float) (4*0.053*screenHeight),(float) (6*0.053*screenHeight),(float) (8*0.053*screenHeight),(float) (10*0.053*screenHeight),(float) (12*0.053*screenHeight),(float) (14*0.053*screenHeight),(float) (16*0.053*screenHeight),(float) (18*0.053*screenHeight)} ;
        x2= new float[]{(float) (1*0.053*screenHeight),(float) (3*0.053*screenHeight),(float) (5*0.053*screenHeight),(float) (7*0.053*screenHeight),(float) (9*0.053*screenHeight),(float) (11*0.053*screenHeight),(float) (13*0.053*screenHeight),(float) (15*0.053*screenHeight),(float) (17*0.053*screenHeight), (float)(19*0.053*screenHeight)} ;

        //car
        curr_pos=0;
        carobj = new ArrayList<Carobj>();
        car = getResources().getDrawable(R.drawable.car);

        bm= BitmapFactory.decodeResource(getResources(),R.drawable.left);
        Matrix matrix = new Matrix();
        matrix.setScale(1,-1);
        revBm = Bitmap.createBitmap(bm,0,0,bm.getWidth(),bm.getHeight(),matrix,true);

        bmStart=screenHeight- bm.getHeight();
        bmRevStart = bmStart - revBm.getHeight();

        bmLeft = (float)(0.25 * screenWidth) - bm.getWidth();
        bmRight= (float) (0.75) * screenWidth;
        //alertDialogBuilder = new AlertDialog.Builder(context);

    }


    @Override
    public void run() {
        while (running) {
            long startFrameTime = System.currentTimeMillis();

            update();

            draw();
            if(!carobj.isEmpty()) {
                if (carobj.get(0).top > (0.75 * screenHeight -70)){
                    if(carobj.get(0).left == curr_pos)
                    {
                        pause();
                    }
                }
            }
            // Calculate the fps this frame
            long timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame >= 1) {
                fps = 1000 / timeThisFrame;
            }
        }
    }
    private void add_cars(){
        float rand_dist=0;
        int dist=0;
        speed +=0.1;
        Random rand = new Random();
        for(int i= 0; i<10 ;i++ ){
            dist = dist + rand.nextInt(3) +2;
            carobj.add(new Carobj(-(float)(dist * 85 ) , rand.nextInt(2)));
        }

    }
    private void update() {
        // Update all the background positions
        for(int i=0; i<10; i++) {
            x1[i] = (float) (x1[i] + speed);
            x2[i] = (float) (x2[i] + speed);

            if (x1[i] > screenHeight) {
                x1[i] = -(float)(0.053*screenHeight);
                x2[i] = 0;
            }
        }
        bmStart +=(speed);
        bmRevStart +=speed;
        if(bmStart >screenHeight){

            bmStart=bmRevStart - bm.getHeight();
        } else if(bmRevStart >screenHeight){

            bmRevStart= bmStart - bm.getHeight();
        }



        if(carobj.isEmpty()){
           add_cars();
        } else {
            if(carobj.get(0).top >screenHeight){
                carobj.remove(0);
            }
            for(int i=0; i< carobj.size();i++){
                 carobj.get(i).top += (speed-0.2);
            }
        }

    }

    private void draw() {

        if (ourHolder.getSurface().isValid()) {
            //First we lock the area of memory we will be drawing to
            canvas = ourHolder.lockCanvas();

            //draw a background color
            canvas.drawColor(Color.argb(255, 0, 3, 70));

            // Draw the background parallax

            // Draw the rest of the game
            paint.setColor(Color.GREEN);
            canvas.drawRect(0,0, (float) (0.25*screenWidth),screenHeight,paint);


            paint.setColor(Color.BLACK);
            canvas.drawRect((float)(0.25*screenWidth),0,(float)(0.75*screenWidth),screenHeight,paint);
            paint.setColor(Color.GREEN);
            canvas.drawRect((float) (0.75*screenWidth),0,screenWidth,screenHeight,paint);

            paint.setColor(Color.WHITE);
            canvas.drawRect((float)(0.25*screenWidth),0,(float) (0.27 * screenWidth), screenHeight, paint);
            //if(bmfirst == true) {
                canvas.drawBitmap(bm, bmLeft, bmStart, paint);
                canvas.drawBitmap(revBm, bmLeft , bmRevStart, paint);
            //} else{
                canvas.drawBitmap(bm, bmRight, bmRevStart, paint);
                canvas.drawBitmap(revBm, bmRight, bmStart, paint);
            //}



            // Draw the foreground parallax
            paint.setColor(Color.WHITE);
            for(int i=0;i<10;i++) {
                canvas.drawRect((float) (0.49 * screenWidth), x1[i], (float) (0.51 * screenWidth), x2[i], paint);
            }

            //car
            if(curr_pos==0) {
                car.setBounds((int) (0.30 * screenWidth), (int) (0.75 * screenHeight), (int) (0.44 * screenWidth), (int) (0.75 * screenHeight) +70);
            }
            else {
                car.setBounds((int) (0.56 * screenWidth), (int) (0.75 * screenHeight), (int) (0.70 * screenWidth), (int) (0.75 * screenHeight) + 70);
            }
            car.draw(canvas);

            //case of crash


            for(int i=0; i< carobj.size();i++){
               if(carobj.get(i).left==0){
                    car.setBounds((int) (0.30 * screenWidth), (int) carobj.get(i).top, (int) (0.44 * screenWidth), (int)carobj.get(i).top + 70);
                    car.draw(canvas);
               } else {
                   car.setBounds((int) (0.56 * screenWidth), (int) carobj.get(i).top, (int) (0.70 * screenWidth), (int)carobj.get(i).top + 70);
                   car.draw(canvas);
               }

            }

            // Unlock and draw the scene
            ourHolder.unlockCanvasAndPost(canvas);
        }
    }

    // Clean up our thread if the game is stopped
    public void pause() {

        running = false;
        try {
            gameThread.join();
        } catch (InterruptedException e) {
            // Error
        }
    }

    // Make a new thread and start it
    // Execution moves to our run method
    public void resume() {
        running = true;
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        //Toast.makeText(this, "hi", Toast.LENGTH_SHORT).show();
        //        curr_pos=1;
        switch(event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                cord1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                cord2 = event.getX();

                float diff = cord2-cord1;

                    if (curr_pos == 0) {
                        //right swipe
                        curr_pos=1;

                    } else if (curr_pos == 1) {
                        //left swipe
                        curr_pos=0;

                    }

                //invalidate();
                break;

        }
        return true;
    }




}
