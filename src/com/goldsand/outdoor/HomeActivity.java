package com.goldsand.outdoor;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity implements OnClickListener{
    
    
    private static final int EXIT_TIME = 2000;
    private final float MAX_ROATE_DEGREE = 1.0f;
    
    private SensorManager mSensorManager;
    private Sensor mOrientationSensor;
    
    private float mDirection;
    private float mTargetDirection;
    private AccelerateInterpolator mInterpolator;
    protected final Handler mHandler = new Handler();
    
   // private boolean mStopDrawing;

//    private TextView latitude;
 //   private TextView longitude;

    private LocationApplication outdoor;
    
    private View backgroundView;
    private HomeView homeView;
    
    private TextView locationTextView;
    private TextView base_stationTextView;
    private long firstExitTime = 0L;
    
    private LinearLayout mAngleLayout;
    
    private boolean mStopDrawing = true;
    protected Runnable mCompassViewUpdater = new Runnable() {
        @Override
        public void run() {
                if (homeView != null && !mStopDrawing) {
                    if(mDirection != mTargetDirection) {

                        float to = mTargetDirection;
                        if(to-mDirection > 180) {
                             to -= 360;
                        }else if (to - mDirection < -180){
                             to += 360;
                        }


                        float distance = to - mDirection;
                        if(Math.abs(distance) > MAX_ROATE_DEGREE) {
                            distance = distance>0 ? MAX_ROATE_DEGREE:(-1.0f*MAX_ROATE_DEGREE);
                        }
                        mDirection = normalizeDegree(mDirection + ((to-mDirection)*mInterpolator.getInterpolation(Math.abs(distance)>MAX_ROATE_DEGREE?0.4f:0.3f)));
                        homeView.updateDirection(mDirection);
                    }
                }
                updateLocation();
                mHandler.postDelayed(mCompassViewUpdater,20);
        }
    };
    private SensorEventListener mOrientationSensorEventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            float direction = event.values[mSensorManager.DATA_X] * -1.0f;
            mTargetDirection = normalizeDegree(direction);
           
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        openGPS();
        setContentView(R.layout.outdoor);
       // latitude=(TextView)findViewById(R.id.latitude);
       // longitude=(TextView)findViewById(R.id.longitude);
       // outdoor=LocationApplication.getInstance();
        //latitude.setText(Double.toString(outdoor.getLatitude()));
       //longitude.setText(Double.toString(outdoor.getLongitude()));
        initServices();
        initResources();
    }
    @Override
    protected void onResume() {
        super.onResume();

        if(mOrientationSensor != null){
            mSensorManager.registerListener(mOrientationSensorEventListener,
                    mOrientationSensor, SensorManager.SENSOR_DELAY_GAME);
        } else {

        }
        mStopDrawing = false;
        mHandler.postDelayed(mCompassViewUpdater,20);
        outdoor.addLocation();
    }
    @Override
    protected void onPause() {
        super.onPause();
        mStopDrawing = true;
        if (mOrientationSensor !=null) {
            mSensorManager.unregisterListener(mOrientationSensorEventListener);
        }
        outdoor.removeLocation();
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        
    }
    private void openGPS() {
        LocationManager   locationManager=(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this,"GPS Open",Toast.LENGTH_SHORT);
            return;
        }
        Toast.makeText(this,"Please open GPS",Toast.LENGTH_SHORT);
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent,0);

    }
    
    private void initServices(){
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mOrientationSensor = mSensorManager.getSensorList(Sensor.TYPE_ORIENTATION).get(0);

    }
    private void initResources(){
        //mViewGuide = findViewById();
        mDirection = 0.01f;
        mTargetDirection = 0.01f;
        mInterpolator = new AccelerateInterpolator();
        mStopDrawing = true;
        backgroundView = findViewById(R.id.background);
        homeView =(HomeView) findViewById(R.id.HomeView);
        locationTextView =(TextView) findViewById(R.id.textview_location);
        base_stationTextView = (TextView) findViewById(R.id.textview_base_station);
        mAngleLayout = (LinearLayout) findViewById(R.id.layout_angle);
        outdoor = LocationApplication.getInstance();
        
    }
    
    private void updateDirection(){
      
        
        
    }
    private ImageView getNumberImage(int number){
        ImageView image = new ImageView(this);
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        switch (number) {
            case 0:
                image.setImageResource(R.drawable.number_0);
                break;
            case 1:
                image.setImageResource(R.drawable.number_1);
                break;
            case 2:
                image.setImageResource(R.drawable.number_2);
                break;
            case 3:
                image.setImageResource(R.drawable.number_3);
                break;
            case 4:
                image.setImageResource(R.drawable.number_4);
                break;
            case 5:
                image.setImageResource(R.drawable.number_5);
                break;
            case 6:
                image.setImageResource(R.drawable.number_6);
                break;
            case 7:
                image.setImageResource(R.drawable.number_7);
                break;
            case 8:
                image.setImageResource(R.drawable.number_8);
                break;
            case 9:
                image.setImageResource(R.drawable.number_9);
                break;
        }
        image.setLayoutParams(lp);
        return image;
    }
    private void updateLocation(){
        locationTextView.setText("latitude:"+Double.toString(outdoor.getLatitude())+"  "+"Longitude:"+Double.toString(outdoor.getLongitude()));
            
    }
    @Override
    public void onBackPressed() {
       long curTime = System.currentTimeMillis();
       if (curTime - firstExitTime < EXIT_TIME) {
           finish();
       } else {

           Toast.makeText(this,"Once more EXIT",Toast.LENGTH_SHORT).show();
           firstExitTime = curTime;
       }
    }
    private float normalizeDegree(float degree) {
        return (degree + 720) % 360;
    }
}