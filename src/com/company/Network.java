package com.company;

import java.util.UUID;

public class Network {
    private Device mainDevice;
    private Device[] devices;

    public Network(Device[] devices) {
        this.devices = devices;
        if (devices.length > 0) {
            Device mainDevice = devices[0];
            for(Device device: devices) {
                if (device.getLinkedRoutes().length > mainDevice.getLinkedRoutes().length) {
                    mainDevice = device;
                }
            }
            this.mainDevice = mainDevice;
        }
    }

    public void print() {
        System.out.println("Selezionare il metodo d'input:");
        System.out.println("[1] Esplora rete");
        System.out.println("[2] Inserimento UUID manuale");
        switch (Read.getInt(1, 2)) {
            case 1:
                exploreNetwork(mainDevice, new Device[2]);
                break;
            default:
                userInputByUUID();
                break;
        }
    }

    public void setMainDevice(Device mainDevice) {
        this.mainDevice = mainDevice;
    }

    public Device[] getDevices() {
        return devices;
    }

    public Device getDevice(UUID uuid) {
        for(Device device:getDevices()) {
            if (device.getId().toString().equals(uuid.toString())) {
                return device;
            }
        }
        return null;
    }

    private void userInputByUUID() {
        Device one = null;
        System.out.println("Digitare l'UUID del primo dispositivo:");
        do {
            one = getDevice(Read.getUUID());
            if (one == null) {
                System.out.println("\uDBC0\uDD84 Il dispositivo non è stato trovato.");
            }
        } while (one == null);

        Device two = null;
        System.out.println("Digitare l'UUID del secondo dispositivo:");
        do {
            two = getDevice(Read.getUUID());
            if (two == null) {
                System.out.println("\uDBC0\uDD84 Il dispositivo non è stato trovato.");
            }
        } while (two == null);

        estimateRoute(one, two).print();
    }

    private void exploreNetwork(Device device, Device[] selectedDevices) {
        Route[] routes = device.getLinkedRoutes();

        System.out.println("-------------------------------------------");
        System.out.println("\uDBC1\uDE57 " + device.getId().toString());
        System.out.println("Connesso a:");

        for(int i=0; i<device.getLinkedRoutes().length; i++) {
            System.out.println("[" + (i+1) + "] \uDBC1\uDE57 " + routes[i].getNextDevice(device).getId().toString() + " \uDBC3\uDC11 \uDBC0\uDE64 " + routes[i].getId().toString());
        }

        System.out.println("Digitare l'indice di un dispositivo per mostrare i dispositivi ai quali è connesso oppure digitare l'indice preceduto da un meno per selezionare il dispositivo come estremità del percorso:");
        int input = Read.getInt(-routes.length, routes.length);
        if (input != 0) {
            if (input < 0) {
                if (selectedDevices[0] == null) {
                    selectedDevices[0] = routes[input*-1 - 1].getNextDevice(device);
                    exploreNetwork(routes[input*-1 - 1].getNextDevice(device), selectedDevices);
                } else {
                    selectedDevices[1] = routes[input*-1 - 1].getNextDevice(device);
                    estimateRoute(selectedDevices[0], selectedDevices[1]).print();
                }
            }
            else {
                exploreNetwork(routes[input - 1].getNextDevice(device), selectedDevices);
            }
        }
        else {
            System.out.println("-------------------------------------------");
        }
    }

    public RoutesFound estimateRoute(Device sendingDevice, Device receivingDevice) {
        final int totalEstimatedRoute = 100;
        RoutesFound routesFound = new RoutesFound(totalEstimatedRoute);

        if (sendingDevice.sameAs(receivingDevice)) {
            System.out.println("\uDBC0\uDD84 I dispositivi di partenza e di arrivo combaciano.");
        }
        else {
            do {
                routesFound.addFoundRoute(new EstimatedRoute(sendingDevice, receivingDevice, this, routesFound.getRoutesToAvoid()));
            } while(routesFound.getLast().validRoute());
        }

        return routesFound;
    }
}
