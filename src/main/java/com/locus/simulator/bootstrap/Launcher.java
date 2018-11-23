package com.locus.simulator.bootstrap;


import com.google.maps.model.LatLng;
import com.locus.simulator.domain.LocationsResponse;
import com.locus.simulator.service.LocationSimulatorService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.util.List;


/**
 * @author Neophy
 */
public class Launcher {

    public static void main(String[] args) {

        // Load all beans
        loadBeans();

        // Get Application context
        ApplicationContext context = loadBeans();

        // Get LocationSimulatorService bean from context
        LocationSimulatorService locationSimulatorService = (LocationSimulatorService) context.getBean("locationSimulatorService");

        //TODO: Give the values here
        LatLng src = new LatLng(1, 2);
        LatLng dest = new LatLng(3, 5);

        // Call LocationSimulatorService to get all the locations present between source and destination
        LocationsResponse locations = locationSimulatorService.getLocations(src, dest);
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
            System.out.println(location);
        }
    }

}
