package com.ergasia.omada4.myapplicationmap.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by USER on 20/1/2017.
 */

public class Poi  implements Serializable{

        public String userId;

    // 01-20 23:06:16.056 1122-1122/com.ergasia.omada4.myapplicationmap E/UncaughtException:
    // com.google.firebase.database.DatabaseException: Class com.google.android.gms.maps.model.LatLng is missing a constructor with no arguments
    // ετσι ξεφωρτωθηκα την LatLng
        public double lat;
        public double lon;
        public String catId;
        public String catDescr;


    @Override
    public String toString() {
        return "Poi{" +
                "userId='" + userId + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", catId='" + catId + '\'' +
                ", catDescr='" + catDescr + '\'' +
                '}';
    }
}
