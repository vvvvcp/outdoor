package com.goldsand.outdoor;

import android.R.integer;
import android.app.Application;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.GetChars;
import android.util.Log;

public class LocationApplication extends Application{
    private static LocationApplication instance;
    //public  LocationC
    private double latitude;
    private double longitude;
    
    private LocationManager mLocationManager;
    
    private String mLocationProvider;
    private TelephonyManager mTelephonyManager;
    private int mcc;
    private int mnc;
    private int lac;
    private int cid;

  
    public static LocationApplication getInstance() {

        return instance;
    }
    @Override
    public void onCreate() {
        instance=this;
        getLocation();
        super.onCreate();
        //initCellinfo();


    }
    private LocationListener mlocationListener=new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
             updateLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            if (status != LocationProvider.OUT_OF_SERVICE) {
                updateLocation(mLocationManager
                     .getLastKnownLocation(mLocationProvider));
              } else {
                Log.i("huxiao","cant location");
              }
        }

        @Override
        public void onProviderEnabled(String provider) {
            //Location location=locationManager.getLastKnownLocation(provider);
           // updateLoaction(location);

        }

        @Override
        public void onProviderDisabled(String provider) {
            updateLocation(null);

        }
    };
    private void initCellinfo() {
        mTelephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        GsmCellLocation location = (GsmCellLocation) mTelephonyManager.getCellLocation();
        String operator = mTelephonyManager.getNetworkOperator();
        mcc = Integer.parseInt(operator.substring(0, 3));
        mnc = Integer.parseInt(operator.substring(3));
        lac = location.getLac();
        cid = location.getCid();
    }
    private void getLocation() {
        //LocationManager locationManager;
        String serviceName = Context.LOCATION_SERVICE;
        mLocationManager = (LocationManager) this.getSystemService(serviceName);

        Criteria criteria=new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        mLocationProvider=mLocationManager.getBestProvider(criteria,true);
        Log.i("huxiao","mLocationProvider "+mLocationProvider);

    }
    public void addLocation(){
        if(mLocationProvider!=null) {
           Location location=mLocationManager.getLastKnownLocation(mLocationProvider);
           updateLocation(location);
           mLocationManager.requestLocationUpdates(mLocationProvider,2000,10,mlocationListener);
           Log.i("huxiao","requestLocationUpdates");
           
        }
        
    }
    public void removeLocation(){
        if (mLocationProvider != null) {
           mLocationManager.removeUpdates(mlocationListener);
        }
        
    }
    private void updateLocation(Location location) {
        if(location != null) {
            latitude=location.getLatitude();
            Log.i("huxiao", "huxiao add"+latitude);
            longitude=location.getLongitude();
        }
    }
    public double getLatitude(){
        
        return latitude;
    }
    public double getLongitude(){
        return longitude;
    }
    public int getMcc(){
        return mcc;
    }
    public int getMnc(){
        return mnc;
    }
    public int getLac(){
        return lac;
    }
    public int getCid(){
        return cid;
    }

}
