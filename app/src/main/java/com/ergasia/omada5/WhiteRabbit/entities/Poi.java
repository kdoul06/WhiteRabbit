package com.ergasia.omada5.WhiteRabbit.entities;

import java.io.Serializable;

/**
 * Created by USER on 20/1/2017.
 */

public class Poi  implements Serializable{

    // 01-20 23:06:16.056 1122-1122/com.ergasia.omada4.myapplicationmap E/UncaughtException:
    // com.google.firebase.database.DatabaseException: Class com.google.android.gms.maps.model.LatLng is missing a constructor with no arguments
    // ετσι ξεφωρτωθηκα την LatLng
        public double lat;
        public double lon;
        public String catId;
        public String category;
        public String uid;



}
