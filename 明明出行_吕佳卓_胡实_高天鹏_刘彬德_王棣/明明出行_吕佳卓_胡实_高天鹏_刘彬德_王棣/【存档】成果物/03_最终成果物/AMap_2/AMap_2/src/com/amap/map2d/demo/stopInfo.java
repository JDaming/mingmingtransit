package com.amap.map2d.demo;



import java.io.Serializable;

/**
 * Created by ivan on 7/20/16.
 */
public class stopInfo implements Serializable {
    public String name;
    public Double lat;
    public Double lng;
    public stopInfo(String inputName, Double inputLat, Double inputLng){
        name = inputName;
        lat = inputLat;
        lng = inputLng;
    }
}