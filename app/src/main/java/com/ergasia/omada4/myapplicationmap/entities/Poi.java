package com.ergasia.omada4.myapplicationmap.entities;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

/**
 * Created by USER on 20/1/2017.
 */

public class Poi  implements Serializable{

        public String userId;
        public LatLng location;
        public String catId;
        public String catDescr;


    /**
     * @param userId
     * @param location
     * @param catId
     * @param catDescr
     */
        public Poi(String userId, LatLng location, String catId, String catDescr) {
            this.userId = userId;
            this.location = location;
            this.catId = catId;
            this.catDescr = catDescr;
        }

        public Poi () {

        }


    }
