package com.locus.simulator.service.impl;

import com.google.maps.model.LatLng;
import com.locus.simulator.domain.LocationsResponse;
import com.locus.simulator.exception.LocationSimulatorException;
import com.locus.simulator.manager.LocationSimulatorManager;
import com.locus.simulator.service.LocationSimulatorService;
import org.apache.http.HttpStatus;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Neophy
 */
public class LocationSimulatorServiceImpl implements LocationSimulatorService {
    private static final Logger LOGGER = Logger.getLogger(LocationSimulatorServiceImpl.class.getName());
    private LocationSimulatorManager locationSimulatorManager;

    public  LocationsResponse getLocations(LatLng src, LatLng dest) {
        List<LatLng> locations;
        try {
            locations = locationSimulatorManager.getLocations(src, dest);
            return buildSuccessResponse(locations);
        } catch (LocationSimulatorException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage());
            return buildErrorResponse(ex);
        }
    }

    private LocationsResponse buildSuccessResponse(List<LatLng> locations) {
        LocationsResponse resp = new LocationsResponse();
        resp.setLocations(locations);
        resp.setStatusCode(HttpStatus.SC_OK);
        return resp;
    }

    private LocationsResponse buildErrorResponse(LocationSimulatorException ex) {
        LocationsResponse resp = new LocationsResponse();
        resp.setStatusCode(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        resp.setStatusMessage(ex.getMessage());
        return resp;
    }

    public void setLocationSimulatorManager(LocationSimulatorManager locationSimulatorManager) {
        this.locationSimulatorManager = locationSimulatorManager;
    }
}
