package com.goldsand.outdoor;

import android.app.Application;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.util.Log;

public class LocationApplication extends Application{
    private static LocationApplication instance;
    //public  LocationC
    private double latitude;
    private double longitude;
    
    private LocationManager mLocationManager;
    
    private String mLocationProvider;


  
    public static LocationApplication getInstance() {

        return instance;
    }
    @Override
    public void onCreate() {
        instance=this;
        getLocation();
        super.onCreate();


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

}
