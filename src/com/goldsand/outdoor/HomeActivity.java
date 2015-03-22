package com.goldsand.outdoor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    private TextView latitude;
    private TextView longitude;

    private LocationApplication outdoor;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        openGPS();
        setContentView(R.layout.outdoor);
        latitude=(TextView)findViewById(R.id.latitude);
        longitude=(TextView)findViewById(R.id.longitude);
        outdoor=LocationApplication.getInstance();
        latitude.setText(Double.toString(outdoor.getLatitude()));
        longitude.setText(Double.toString(outdoor.getLongitude()));
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
        mViewGuide = findViewById();

        
    }
    
    private void updateDirection(){
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        // 先移除layout中所有的view
        mDirectionLayout.removeAllViews();
        mAngleLayout.removeAllViews();

        // 下面是根据mTargetDirection，作方向名称图片的处理
        ImageView east = null;
        ImageView west = null;
        ImageView south = null;
        ImageView north = null;
        float direction = normalizeDegree(mTargetDirection * -1.0f);
        if (direction > 22.5f && direction < 157.5f) {
            // east
            east = new ImageView(this);
            east.setImageResource(mChinease ? R.drawable.e_cn : R.drawable.e);
            east.setLayoutParams(lp);
        } else if (direction > 202.5f && direction < 337.5f) {
            // west
            west = new ImageView(this);
            west.setImageResource(mChinease ? R.drawable.w_cn : R.drawable.w);
            west.setLayoutParams(lp);
        }

        if (direction > 112.5f && direction < 247.5f) {
            // south
            south = new ImageView(this);
            south.setImageResource(mChinease ? R.drawable.s_cn : R.drawable.s);
            south.setLayoutParams(lp);
        } else if (direction < 67.5 || direction > 292.5f) {
            // north
            north = new ImageView(this);
            north.setImageResource(mChinease ? R.drawable.n_cn : R.drawable.n);
            north.setLayoutParams(lp);
        }
        // 下面是根据系统使用语言，更换对应的语言图片资源
        if (mChinease) {
            // east/west should be before north/south
            if (east != null) {
                mDirectionLayout.addView(east);
            }
            if (west != null) {
                mDirectionLayout.addView(west);
            }
            if (south != null) {
                mDirectionLayout.addView(south);
            }
            if (north != null) {
                mDirectionLayout.addView(north);
            }
        } else {
            // north/south should be before east/west
            if (south != null) {
                mDirectionLayout.addView(south);
            }
            if (north != null) {
                mDirectionLayout.addView(north);
            }
            if (east != null) {
                mDirectionLayout.addView(east);
            }
            if (west != null) {
                mDirectionLayout.addView(west);
            }
        }
        // 下面是根据方向度数显示度数图片数字
        int direction2 = (int) direction;
        boolean show = false;
        if (direction2 >= 100) {
            mAngleLayout.addView(getNumberImage(direction2 / 100));
            direction2 %= 100;
            show = true;
        }
        if (direction2 >= 10 || show) {
            mAngleLayout.addView(getNumberImage(direction2 / 10));
            direction2 %= 10;
        }
        mAngleLayout.addView(getNumberImage(direction2));
        // 下面是增加一个°的图片
        ImageView degreeImageView = new ImageView(this);
        degreeImageView.setImageResource(R.drawable.degree);
        degreeImageView.setLayoutParams(lp);
        mAngleLayout.addView(degreeImageView);
        
        
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
    private void updateLocation(Location location){

        if (location == null) {
            // mLocationTextView.setText(R.string.getting_location);
            return;
        } else {
            // StringBuilder sb = new StringBuilder();
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            String latitudeStr;
            String longitudeStr;
            if (latitude >= 0.0f) {
                latitudeStr = getString(R.string.location_north,
                        getLocationString(latitude));
            } else {
                latitudeStr = getString(R.string.location_south,
                        getLocationString(-1.0 * latitude));
            }

            // sb.append("    ");

            if (longitude >= 0.0f) {
                longitudeStr = getString(R.string.location_east,
                        getLocationString(longitude));
            } else {
                longitudeStr = getString(R.string.location_west,
                        getLocationString(-1.0 * longitude));
            }
            mLatitudeTV.setText(latitudeStr);
            mLongitudeTV.setText(longitudeStr);
            // mLocationTextView.setText(sb.toString());//
            // 显示经纬度，其实还可以作反向编译，显示具体地址
        }
    }
    View mViewGuide;
    ImageView mGuideAnimation;
    HomeView mPointer;
    private boolean mStopDrawing;
    protected Handler invisiableHandler = new Handler() {

        public void handleMessage(Message msg){
             mViewGuide.setVisibility(View.GONE);
        }
    }
    public void onWindowFocusChanged(boolean hasFocus) {
        AnimationDrawable anim = (AnimationDrawable) mGuideAnimation.getDrawable();
        anim.start();
    }
    protected Runnable mCompassViewUpdater = new Runnable() {
        @Override
        public void run() {
                if (mPointer != null && !mStopDrawing) {
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
                        mPointer.updateDirection(mDirection;
                    }
                }
                updateDirection();
                mHandler.postDelayed(mCompassViewUpdater,20);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        outdoor = LocationApplication.getInstance();
        initResources();
        initServices();
        //outdoor

        if (outdoor)


    }
    @Override
    public void onBackPressed() {
       long curTime = System.currentTimeMillis();
       if (curTime - firstExitTime < EXIT_TIME) {
           finish();
       } else {

           Toast.makeText(this,"Once more EXIT",Toast.LENGTH_SHORT).show();
           fristExitTime = curTime;
       }
    }
    @Override
    protected void onResume() {
        super.onResume();

        if(mOrientationSensor != null){
            mSensorManager.registerListener(mOrientationSensor);
        } else {

        }
        mStopDrawing = false;
        mHandler.postDelayed(mCompassViewUpdater,20);
    }
    @Override
    protected void onPause() {
        super.onPause();
        mStopDrawing = true;
        if (mOrientationSensor !=null) {
            mSensorManager.unregisterListener(mOrientationSensor);
        }

    }
    private SensorEventListener mOrientationSensorEventListener = new SensorEventListener() {

        @Override
        public void onSensorChanged(SensorEvent event) {
            float direction = event.values[mSensorManager.DATA_X] * -1.0f;
            mTargetDirection = normalizeDegree(direction);// 赋值给全局变量，让指南针旋转
            // Log.i("way", event.values[mSensorManager.DATA_Y] + "");
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
    private float normalizeDegree(float degree) {
        return (degree + 720) % 360;
    }
}