package com.goldsand.outdoor;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;

public class HomeActivity extends Activity implements OnClickListener{
    
    
    private static final int EXIT_TIME = 2000;
    private final float MAX_ROATE_DEGREE = 1.0f;
    
    private SensorManager mSensorManager;
    private Sensor mOrientationSensor;
    
    private float mDirection;
    private float mTargetDirection;
    private AccelerateDecelerateInterpolator mInterpolator;
    protected final Handler mHandler = new Handler();
    
    private boolean mStopDrawing;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.outdoor);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        
    }
    
    private void initServices(){
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mOrientationSensor = mSensorManager.getSensorList(Sensor.TYPE_ORIENTATION).get(0);

    }
    private void initResources(){
        
    }
    
    private void updateDirection(){
        
        
        
    }
    private void updateLocation(){
        
    }
}
