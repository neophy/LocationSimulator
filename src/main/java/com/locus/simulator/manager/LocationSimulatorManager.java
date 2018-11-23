package com.locus.simulator.manager;

import com.google.maps.model.LatLng;
import com.locus.simulator.exception.LocationSimulatorException;

import java.util.List;

/**
 * @author Neophy
 */
public interface LocationSimulatorManager {

    List<LatLng> getLocations(LatLng src, LatLng destination) throws LocationSimulatorException;
}
