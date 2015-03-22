package com.goldsand.outdoor;

import android.app.Application;
import android.content.Context;
import android.location.Criteria;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.widget.EditText;

public class LocationApplication extends Application{
    private static LocationApplication instance;
    //public  LocationC
    private double latitude;
    private double longitude;



  //  LocationManager locationManager=(LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
    public static LocationApplication getInstance() {

        return instance;
    }
    @Override
    public void onCreate() {
        instance=this;
        getLocation();
        super.onCreate();


    }
    private LocationListener locationListener=new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            updateLoaction(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status){
                case LocationProvider.AVAILABLE:
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    break;
            }

        }

        @Override
        public void onProviderEnabled(String provider) {
            //Location location=locationManager.getLastKnownLocation(provider);
           // updateLoaction(location);

        }

        @Override
        public void onProviderDisabled(String provider) {
            updateLoaction(null);

        }
    };
    //private GpsStatus.Listener listener=new GpsStatus.Listener();

    private void getLocation() {
        LocationManager locationManager;
        String serviceName = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) this.getSystemService(serviceName);

        Criteria criteria=new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);

        String provider=locationManager.getBestProvider(criteria,true);
        Location location=locationManager.getLastKnownLocation(provider);
        updateLoaction(location);
        locationManager.requestLocationUpdates(provider,100*1000,500,locationListener);

    }
    private void updateLoaction(Location location) {
        if(location != null) {
            latitude=location.getLatitude();
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
