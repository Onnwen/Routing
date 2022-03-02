package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.UUID;

public class FileManagment {
    public static File routesFile = new File("routes4.csv");

    public static void createFiles() {
        createRoutesFile();
    }

    public static void createRoutesFile() {
        try {
            routesFile.createNewFile();
        } catch (IOException e) {}
    }

    public static Network loadNetwork() {
        try {
            createFiles();
            Device[] devicesList = getDevicesArray();
            Scanner fileScanner = new Scanner(routesFile);
            Route[] routesList = new Route[getFileLines(routesFile)];

            for(int routeIndex=0; routeIndex<routesList.length; routeIndex++) {
                String[] routeData = fileScanner.nextLine().split(";");
                Device one = findDevice(UUID.fromString(routeData[2]), devicesList);
                Device two = findDevice(UUID.fromString(routeData[3]), devicesList);
                if (one != null && two != null) {
                    routesList[routeIndex] = new Route(UUID.fromString(routeData[0]), Integer.parseInt(routeData[1]), one, two);
                }
            }

            for (Device device:devicesList) {
                for(Route route:routesList) {
                    if (route != null && route.linkedTo(device)) {
                        device.addRoute(route);
                    }
                }
            }

            return new Network(devicesList);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    private static Device findDevice(UUID id, Device[] devices) {
        for (Device device:devices) {
            if (device.getId().equals(id)) {
                return device;
            }
        }
        return null;
    }

    private static int getFileLines(File file) {
        try {
            Scanner fileScanner = new Scanner(file);
            int lines = 0;
            while(fileScanner.hasNext()) {
                fileScanner.nextLine();
                lines++;
            }
            return lines;
        } catch (FileNotFoundException e) {
            return 0;
        }
    }

    public static int getDevicesCount() {
        return getDevicesArray().length;
    }

    private static Device[] getDevicesArray() {
        try {
            Scanner fileScanner = new Scanner(routesFile);
            Device[] devicesList = new Device[getFileLines(routesFile)];
            int totalDevices = 0;
            for(int routeIndex=0; routeIndex<devicesList.length; routeIndex++) {
                String[] deviceData = fileScanner.nextLine().split(";");
                for(int i=2; i<=3; i++) {
                    Device newDevice = new Device(UUID.fromString(deviceData[i]));
                    if (!arrayContains(newDevice, devicesList, totalDevices)) {
                        devicesList[totalDevices] = newDevice;
                        totalDevices++;
                    }
                }
            }
            return Arrays.copyOf(devicesList, totalDevices);
        } catch (FileNotFoundException e) {
            return new Device[0];
        }
    }

    private static boolean arrayContains(Device device, Device[] devices, int length) {
        for(int i=0; i<length; i++) {
            if (devices[i].sameAs(device)) {
                return true;
            }
        }
        return false;
    }
}
