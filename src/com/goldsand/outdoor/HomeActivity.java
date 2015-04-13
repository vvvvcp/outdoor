package com.goldsand.outdoor;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.goldsand.outservice.outdoorService;

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
    private static final int MENU_START =1;
    private static final int MENU_STOP  =2;
    private static final int WIFI_DISPLAY =3;
    private static final int MENU_TEST =4;
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
        base_stationTextView.setText("Mcc:"+outdoor.getMcc()+"Mnc:"+outdoor.getMnc()+"Lac:"+outdoor.getLac()+"Cid:"+outdoor.getCid());
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case MENU_START:
            startOutdoor();
            break;
        case MENU_STOP:
            stopOutdoor();
            break;
        case WIFI_DISPLAY:
            display();
            break;
        case MENU_TEST:
            cellQuery();
            break;
        default:
            break;
        }
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, MENU_START, 0, R.string.start_service);
        menu.add(0, MENU_STOP, 0, R.string.stop_service);
        menu.add(0, WIFI_DISPLAY, 0 ,"Search");
        menu.add(0, MENU_TEST, 0,"Test");
        return true;
    }
    private void startOutdoor() {
        this.startService(new Intent(this,outdoorService.class));
    }
    private void stopOutdoor() {
        this.stopService(new Intent(this,outdoorService.class));
    }
    private void cellQuery() {
        Uri insertUri = Uri.parse("content://com.goldsand.outdoor.cellinfo/cellid");
        Cursor cursor = getContentResolver().query(insertUri, null, null, null, null);
        if( cursor == null || cursor.getCount()==0) {
            return;
        }
        Log.i("huxiao","ddd "+cursor.getCount() );
        while (cursor.moveToNext()) {
            Log.i("huxiao","huxiao id "+cursor.getInt(0));
            Log.i("huxiao","huxiao"+cursor.getString(1));
            Log.i("huxiao","huxiao"+cursor.getString(2));
            Log.i("huxiao","huxiao"+cursor.getString(3));
            Log.i("huxiao","huxiao"+cursor.getString(4)); 
 
        }
    }
    private void display() {
        new Thread(){
            @Override  
            public void run() {  
                try {
                    
                    String json = getJsonCellPos(460, 0, 6338, 62451);  
                    Log.i("huxiao", "request = " + json);  

                    String url = "http://www.minigps.net/minigps/map/google/location";  
                    String result = httpPost(url, json);  
                    Log.i("huxiao", "result = " + result);  
                } catch (Exception e) {  
                    // TODO Auto-generated catch block  
                    e.printStackTrace();  
                }  
            }  
            
        }.start();
        
        
    }
    /**
     * 获取JSON形式的基站信息
     * @param mcc 移动国家代码（中国的为460）
     * @param mnc 移动网络号码（中国移动为0，中国联通为1，中国电信为2）； 
     * @param lac 位置区域码
     * @param cid 基站编号
     * @return json
     * @throws JSONException
     */
    private String getJsonCellPos(int mcc, int mnc, int lac, int cid) throws JSONException {
        JSONObject jsonCellPos = new JSONObject();
        jsonCellPos.put("version", "1.1.0");
        jsonCellPos.put("host", "maps.google.com");

        JSONArray array = new JSONArray();
        JSONObject json1 = new JSONObject();
        json1.put("location_area_code", "" + lac + "");
        json1.put("mobile_country_code", "" + mcc + "");
        json1.put("mobile_network_code", "" + mnc + "");
        json1.put("age", 0);
        json1.put("cell_id", "" + cid + "");
        array.put(json1);

        jsonCellPos.put("cell_towers", array);
        return jsonCellPos.toString();
    }
    /**
     * 调用第三方公开的API根据基站信息查找基站的经纬度值及地址信息
     */
    public String httpPost(String url, String jsonCellPos) throws IOException{
        byte[] data = jsonCellPos.toString().getBytes();
        URL realUrl = new URL(url);
        HttpURLConnection httpURLConnection = (HttpURLConnection) realUrl.openConnection();
        httpURLConnection.setConnectTimeout(6 * 1000);
        httpURLConnection.setDoOutput(true);
        httpURLConnection.setDoInput(true);
        httpURLConnection.setUseCaches(false);
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setRequestProperty("Accept", "application/json, text/javascript, */*; q=0.01");
        httpURLConnection.setRequestProperty("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");
        httpURLConnection.setRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
        httpURLConnection.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
        httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
        httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));
        httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

        httpURLConnection.setRequestProperty("Host", "www.minigps.net");
        httpURLConnection.setRequestProperty("Referer", "http://www.minigps.net/map.html");
        httpURLConnection.setRequestProperty("User-Agent",
                "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/537.4 (KHTML, like Gecko) Chrome/22.0.1229.94 Safari/537.4X-Requested-With:XMLHttpRequest");

        httpURLConnection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
        httpURLConnection.setRequestProperty("Host", "www.minigps.net");

        DataOutputStream outStream = new DataOutputStream(httpURLConnection.getOutputStream());
        outStream.write(data);
        outStream.flush();
        outStream.close();

        if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = httpURLConnection.getInputStream();
            return new String(read(inputStream));
        }
        return null;
    }
    /**
     * 读取IO流并以byte[]形式存储
     * @param inputSream InputStream
     * @return byte[]
     * @throws IOException
     */
    public byte[] read(InputStream inputSream) throws IOException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        int len = -1;
        byte[] buffer = new byte[1024];
        while ((len = inputSream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inputSream.close();

        return outStream.toByteArray();
    }
    

}
