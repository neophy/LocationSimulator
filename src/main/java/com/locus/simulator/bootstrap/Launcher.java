package com.locus.simulator.bootstrap;


import com.google.maps.model.LatLng;
import com.locus.simulator.domain.LocationsResponse;
import com.locus.simulator.service.LocationSimulatorService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author Neophy
 */
public class Launcher {

    private static final Logger LOGGER = Logger.getLogger(Launcher.class.getName());

    public static void main(String[] args) {

        // Load all beans
        loadBeans();

        // Get Application context
        ApplicationContext context = loadBeans();

        // Get LocationSimulatorService bean from context
        LocationSimulatorService locationSimulatorService = (LocationSimulatorService) context.getBean("locationSimulatorService");

        //TODO: Give the values here in degrees
        LatLng src = new LatLng(49, 50);
        LatLng dest = new LatLng(68, 99);

        // Call LocationSimulatorService to get all the locations present between source and destination
        LocationsResponse locations = locationSimulatorService.getLocations(src, dest);
        if (locations.getLocations() == null) {
            LOGGER.log(Level.SEVERE, "No locations found for src: " + src.toString() + " and destination: "
                    + dest.toString() + ". Error: " + locations.getStatusMessage());
            System.exit(0);
        }
        printLocations(locations.getLocations());
    }

    private static ApplicationContext loadBeans() {
        ApplicationConfigReader config = null;
        ApplicationContext context = null;
        try {
            config = new ApplicationConfigReader("conf/application.properties");
        } catch (IOException e) {
            System.exit(0);
        }
        String springFilesStr = config.getProperty("spring.configs");
        if (springFilesStr != null) {
            String[] springFilesArr = springFilesStr.split(",");
            if (springFilesArr.length > 0) {
                context = new ClassPathXmlApplicationContext(springFilesArr);
            }
        }
        return context;
    }

    private static void printLocations(List<LatLng> locations) {
        for (LatLng location:locations) {
            LOGGER.log(Level.INFO, location.toString());
        }
    }

}
