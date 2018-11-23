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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Neophy
 */
public class LocationSimulatorManagerImpl implements LocationSimulatorManager {
    private static final Logger LOGGER = Logger.getLogger(LocationSimulatorManagerImpl.class.getName());

    private static final String API_KEY = "AIzaSyARB94yftG3b4MFQeEKpqMB6mqywkeYVdw";
    private IntervalCalculator calculator;

    public List<LatLng> getLocations(LatLng src, LatLng destination) throws LocationSimulatorException {

        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(API_KEY)
                .build();

        DirectionsResult results ;
        try {
            results = DirectionsApi.newRequest(context)
                        .origin(src)
                        .destination(destination)
                        .await();
        } catch (ApiException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
            throw new LocationSimulatorException(e.getMessage(), LocationSimulatorErrorCodes.GOOGLE_API_ERROR);
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
            throw new LocationSimulatorException(e.getMessage(), LocationSimulatorErrorCodes.GOOGLE_API_ERROR);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, e.getMessage());
            throw new LocationSimulatorException(e.getMessage(), LocationSimulatorErrorCodes.GOOGLE_API_ERROR);
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

}

