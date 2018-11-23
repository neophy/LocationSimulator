package com.locus.simulator.calculations;

import com.google.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Neophy
 */
public class IntervalCalculator {
    private static final double INTERVAL = 50;
    private static final double AZIMUTH = 279.91;
    private static final double RADIUS = 6371000;

    /**
     * We need to get the locations on the route such that they are separated by INTERVAL = 50m
     * @param locations
     * @return
     * @source https://gis.stackexchange.com/questions/157693/getting-all-vertex-lat-long-coordinates-every-1-meter-between-two-known-points
     */
    public List<LatLng> getDesiredLocations(List<LatLng> locations) {
        List<LatLng> desiredLocations = new ArrayList<LatLng>();
        desiredLocations.add(locations.get(0));

        for (int i=0; i < locations.size()-1; i++) {
            double lat1 = locations.get(0).lat;
            double lat2 = locations.get(1).lat;
            double lng1 = locations.get(0).lng;
            double lng2 = locations.get(1).lng;

            double d = getPathLength(lat1, lng1, lat2, lng2);
            double dist = Math.floorMod((long) d, (long)INTERVAL);
            double counter = 1.0;

            for (int j=1; j< (int)dist; j++) {
                LatLng c = getDestinationLatLong(lat1,lng1, counter);
                counter += 1.0;
                desiredLocations.add(c);
            }
        }
        return desiredLocations;
    }

    // Returns the lat an long of destination point
    // given the start lat, long, azimuth, and distance
    private LatLng getDestinationLatLong(double lat, double lng, double distance) {
        // Radius of the Earth in km
        double R = 6378.1 ;

        //Bearing is degrees converted to radians
        double brng = Math.toRadians(AZIMUTH);

        // Distance m converted to km
        double d = distance/1000;

        double lat1 = Math.toRadians(lat);
        double lng1 = Math.toRadians(lng);

        double lat2 = Math.asin(Math.sin(lat1) * Math.cos(d / R) + Math.cos(lat1) * Math.sin(d / R) * Math.cos(brng));
        double lng2 = lng1 + Math.atan2(Math.sin(brng) * Math.sin(d / R) * Math.cos(lat1),
                Math.cos(d / R) - Math.sin(lat1) * Math.sin(lat2));

        LatLng destination = new LatLng();
        destination.lat = lat2;
        destination.lng = lng2;

        return destination;
    }

    private double getPathLength(double lat1, double lng1, double lat2, double lng2) {
        double lat1rads = Math.toRadians(lat1);
        double lat2rads = Math.toRadians(lat2);
        double deltaLat = Math.toRadians((lat2 - lat1));
        double deltaLng = Math.toRadians((lng2 - lng1));
        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) + Math.sin(lat1rads) *
                Math.sin(lat2rads) * Math.sin(deltaLng / 2) * Math.sin(deltaLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = RADIUS * c;

        return d;
    }
}
