package com.distance.optimizer.model.entity;

import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @author FarazAhmed
 */
public class Location {
    private double lng;
    private double lat;

    public Location() {
    }

    public Location(double lng, double lat) {
        this.lng = lng;
        this.lat = lat;
    }

    @Field("lng")
    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Field("lat")
    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getLocation(){
        return this.lat + ", " + this.lng;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        if (Double.compare(location.lng, lng) != 0) return false;
        return Double.compare(location.lat, lat) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(lng);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(lat);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Location{" +
                "lng=" + lng +
                ", lat=" + lat +
                '}';
    }
}
