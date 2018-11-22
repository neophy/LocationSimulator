package com.locus.simulator.manager.impl;

import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;
import com.google.maps.model.LatLng;
import com.locus.simulator.calculations.IntervalCalculator;
import com.locus.simulator.codes.LocationSimulatorErrorCodes;
import com.locus.simulator.exception.LocationSimulatorException;
import com.locus.simulator.manager.LocationSimulatorManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static java.lang.Math.acos;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * @author Neophy
 */
public class LocationSimulatorManagerImpl implements LocationSimulatorManager {
    private static final Logger LOGGER = Logger.getLogger(LocationSimulatorManagerImpl.class.getName());

    private static final String API_KEY = "AIzaSyARB94yftG3b4MFQeEKpqMB6mqywkeYVdw";
    private IntervalCalculator calculator;
    static double PI_RAD = Math.PI / 180.0;

    public List<LatLng> getLocations(LatLng src, LatLng destination) throws LocationSimulatorException {

        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(API_KEY)
                .build();

        DirectionsResult results = null;
        try {
            results = DirectionsApi.newRequest(context)
                        .origin(src)
                        .destination(destination)
                        .await();
        } catch (ApiException e) {
            System.out.println(e);
            throw new LocationSimulatorException("DirectionsApi failure", e.getCause(), LocationSimulatorErrorCodes.GOOGLE_API_ERROR);
        } catch (InterruptedException| IOException e) {
            throw new LocationSimulatorException("DirectionsApi failure", e.getCause(), LocationSimulatorErrorCodes.GOOGLE_API_ERROR);
        }

        List<LatLng> locations = getLocations(results);
        return calculator.getDesiredLocations(locations);

    }

    private List<LatLng> getLocations(DirectionsResult results) throws LocationSimulatorException {
        List<LatLng> locations = new ArrayList();
        DirectionsRoute routes[] = results.routes;
        if (routes.length != 1) {
            throw new LocationSimulatorException("Invalid Routes: " + routes, LocationSimulatorErrorCodes.INVALID_GEO_RESPONSE);
        }
        // As we don't have any alternative route
        DirectionsLeg legs[] = routes[0].legs;
        if (legs.length != 1) {
            throw new LocationSimulatorException("Invalid Legs: " + legs, LocationSimulatorErrorCodes.INVALID_GEO_RESPONSE);
        }

        // As we don't have any waypoints so only one leg is there
        DirectionsStep steps[] = legs[0].steps;
        if (steps.length <=1) {
            throw new LocationSimulatorException("Invalid steps: " + steps, LocationSimulatorErrorCodes.INVALID_GEO_RESPONSE);
        }

        for (int i=0; i<steps.length; i++) {
            EncodedPolyline polyLine = steps[i].polyline;
            List<LatLng> latLngs = polyLine.decodePath();
            locations.addAll(latLngs);
        }
        return locations;
    }

    public void setCalculator(IntervalCalculator calculator) {
        this.calculator = calculator;
    }

    private  double greatCircleInMeters(LatLng latLng1, LatLng latLng2) {
        return greatCircleInKilometers(latLng1.lat, latLng1.lng, latLng2.lng,
                latLng2.lng) * 1000;
    }

    /**
     * Use Great Circle distance formula to calculate distance between 2 coordinates in kilometers.
     * https://software.intel.com/en-us/blogs/2012/11/25/calculating-geographic-distances-in-location-aware-apps
     */
    private double greatCircleInKilometers(double lat1, double long1, double lat2, double long2) {
        double phi1 = lat1 * PI_RAD;
        double phi2 = lat2 * PI_RAD;
        double lam1 = long1 * PI_RAD;
        double lam2 = long2 * PI_RAD;

        return 6371.01 * acos(sin(phi1) * sin(phi2) + cos(phi1) * cos(phi2) * cos(lam2 - lam1));
    }
}

