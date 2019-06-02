package com.yasoft.smsar;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class UserLocation {
   private double longitude;
    private double latitude;
    private String street;
   private String city="";
    private LocationManager lm;
   private Location location;
    UserLocation(Activity activity,Context context){
         lm = (LocationManager) Objects.requireNonNull(activity).getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
             location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            longitude = location.getLongitude();
            latitude = location.getLatitude();



         // Toast.makeText(context,dis+"",Toast.LENGTH_LONG).show();
            LocationListener locationListener = new LocationListener() {
                public void onLocationChanged(Location location) {
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            };
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);

        } else {
            ActivityCompat.requestPermissions(activity, new String[] {
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION },
                    122);
        }
    }


    public void defineAddress(Activity activity) {

        List<Address> addresses= null;
        Geocoder geocoder = new Geocoder(activity);
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

            Address address = addresses != null ? addresses.get(0) : null;
            String addressline = address != null ? address.getAddressLine(0) : null;
            street= address != null ? address.getThoroughfare() : null;
            city= address != null ? address.getLocality() : null;


    }

    public void getNerestLocation(){
        float dis= location.distanceTo(location);
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }


    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}
