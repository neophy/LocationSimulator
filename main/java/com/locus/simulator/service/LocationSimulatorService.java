package com.locus.simulator.service;

import com.google.maps.model.LatLng;
import com.locus.simulator.domain.LocationsResponse;

/**
 * @author Neophy
 */
public interface LocationSimulatorService {

     LocationsResponse getLocations(LatLng src, LatLng dest);
}
