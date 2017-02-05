package com.ergasia.omada5.WhiteRabbit.Services;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by nekta on 22/01/2017.
 */

public class GeoService {


    public static String getAddress(Context context, double lat, double lon) {
        //Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        Locale aLocale = new Locale.Builder().setLanguage("el").setRegion("EL").build();
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());

        String address = null;
        try {
            List<Address> addressList = geocoder.getFromLocation(lat, lon, 1);
            address = addressList.iterator().next().getAddressLine(0);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }



}
