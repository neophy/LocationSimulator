package com.locus.simulator.domain;

import com.google.maps.model.LatLng;

import java.util.List;

/**
 * @author Neophy
 */
public class LocationsResponse {

    private List<LatLng> locations;
    private String statusMessage;
    private int statusCode;

    public List<LatLng> getLocations() {
        return locations;
    }

    public void setLocations(List<LatLng> locations) {
        this.locations = locations;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
