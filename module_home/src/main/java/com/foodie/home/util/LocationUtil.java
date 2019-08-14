package com.foodie.home.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;

import java.util.List;

@SuppressLint("MissingPermission")
public class LocationUtil {

//    public  static void getLocation(Context context,LocationListener locationListener){
//        LocationManager locationManager;
//        String serviceName = Context.LOCATION_SERVICE;
//        locationManager = (LocationManager) context.getSystemService(serviceName);
////                String provider = LocationManager.GPS_PROVIDER;
////                String provider = "gps";
//
//        Criteria criteria = new Criteria();
//        criteria.setAccuracy(Criteria.ACCURACY_FINE);
//        criteria.setAltitudeRequired(false);
//        criteria.setBearingRequired(false);
//        criteria.setCostAllowed(true);
//        criteria.setPowerRequirement(Criteria.POWER_LOW);
//        String provider = locationManager.getBestProvider(criteria, true);
//        if (provider == null) {
//            return ;
//        }
////        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
////            // TODO: Consider calling
////            //    ActivityCompat#requestPermissions
////            // here to request the missing permissions, and then overriding
////            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
////            //                                          int[] grantResults)
////            // to handle the case where the user grants the permission. See the documentation
////            // for ActivityCompat#requestPermissions for more details.
////            return null;
////        }
//        locationManager.requestLocationUpdates(provider, 2000, 10, locationListener);
//
//    }

    public static void getLocation(Context context,LocationListener locationListener) {
        Location location=null;
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {  //从gps获取经纬度
             location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location == null) { //当GPS信号弱没获取到位置的时候又从网络获取
                getLngAndLatWithNetwork( context,locationListener);
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);

        } else {    //从网络获取经纬度
             location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 10, locationListener);
        }


    }

    //从网络获取经纬度
    public static void getLngAndLatWithNetwork(Context context,LocationListener locationListener) {
        double latitude = 0.0;
        double longitude = 0.0;
        LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);

        //Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 10, locationListener);

    }

    public static String getCity(Context context,Location location){
        Geocoder geocoder=new Geocoder(context);
        List places = null;

        try {
            places = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 5);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String placename = "";
        if (places != null && places.size() > 0) {
            // placename=((Address)places.get(0)).getLocality();
            //一下的信息将会具体到某条街
            //其中getAddressLine(0)表示国家，getAddressLine(1)表示精确到某个区，getAddressLine(2)表示精确到具体的街
            placename = ((Address) places.get(0)).getAddressLine(0) + ", " + System.getProperty("line.separator")
                    + ((Address) places.get(0)).getAddressLine(1) ;
        }
        return placename;

    }
    public static String getLocationDetail(Context context,Location location){
        Geocoder geocoder=new Geocoder(context);
        List places = null;

        try {
            places = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 5);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String placename = "";
        if (places != null && places.size() > 0) {
            // placename=((Address)places.get(0)).getLocality();
            //一下的信息将会具体到某条街
            //其中getAddressLine(0)表示国家，getAddressLine(1)表示精确到某个区，getAddressLine(2)表示精确到具体的街
            placename = ((Address) places.get(0)).getAddressLine(0) + ", " + System.getProperty("line.separator")
                    + ((Address) places.get(0)).getAddressLine(1) + ", "
                    + ((Address) places.get(0)).getAddressLine(2);
        }
        return placename;
    }
}
