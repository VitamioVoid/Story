package com.qf.story.entities;

/**
 * 地理信息
 * Created by Administrator on 2016/10/28.
 */
public class GeoData {
    private double lat;
    private double lng;
    private String city;

    public GeoData() {
    }

    public GeoData(String city, double lat, double lng) {
        this.city = city;
        this.lat = lat;
        this.lng = lng;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
