package com.company;

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
        print(mainDevice, new Device[2]);
    }

    public void setMainDevice(Device mainDevice) {
        this.mainDevice = mainDevice;
    }

    public Device[] getDevices() {
        return devices;
    }

    private void print(Device device, Device[] selectedDevices) {
        Route[] routes = device.getLinkedRoutes();

        System.out.println("-------------------------------------------");
        System.out.println("Device " + device.getId().toString());
        System.out.println("Connesso a:");

        for(int i=0; i<device.getLinkedRoutes().length; i++) {
            System.out.println("[" + (i+1) + "] Device " + routes[i].getNextDevice(device).getId().toString() + " tramite la rotta " + routes[i].getId().toString() + ".");
        }

        System.out.print("Digitare l'indice di un dispositivo per mostrare i dispositivi ai quali è connesso oppure digitare l'indice preceduto da un meno per selezionare il dispositivo come estremità della rotta:\n-> ");
        int input = Read.getInt(-routes.length, routes.length);
        if (input != 0) {
            if (input < 0) {
                if (selectedDevices[0] == null) {
                    selectedDevices[0] = routes[input*-1 - 1].getNextDevice(device);
                    print(routes[input*-1 - 1].getNextDevice(device), selectedDevices);
                } else {
                    selectedDevices[1] = routes[input*-1 - 1].getNextDevice(device);
                    estimateRoute(selectedDevices[0], selectedDevices[1]).print();
                }
            }
            else {
                print(routes[input - 1].getNextDevice(device), selectedDevices);
            }
        }
        else {
            System.out.println("-------------------------------------------");
        }
    }

    public RoutesFound estimateRoute(Device sendingDevice, Device receivingDevice) {
        final int totalEstimatedRoute = 100;
        RoutesFound routesFound = new RoutesFound(totalEstimatedRoute);

        do {
            routesFound.addFoundRoute(new EstimatedRoute(sendingDevice, receivingDevice, this, routesFound.getRoutesToAvoid()));
        } while(routesFound.getLast().validRoute());

        return routesFound;
    }
}
