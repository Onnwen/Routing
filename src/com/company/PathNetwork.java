package com.company;

import java.util.Arrays;

public class PathNetwork {
    Network network;
    Device[] devicesToAvoid;
    int totalDevicesToAvoid;

    public PathNetwork(Network network) {
        this.network = network;
        this.devicesToAvoid = new Device[FileManagment.getDevicesCount()];
        this.totalDevicesToAvoid = 0;
    }

    public Device[] getDevicesToAvoid() {
        return Arrays.copyOf(devicesToAvoid, totalDevicesToAvoid);
    }

    public void setMainDevice(Device mainDevice) {
        this.network.setMainDevice(mainDevice);
    }

    public boolean needToAvoid(Device device) {
        for(Device currentDevice:getDevicesToAvoid()) {
            if (currentDevice.sameAs(device)) {
                return true;
            }
        }
        return false;
    }

    public void addDeviceToAvoid(Device device) {
        devicesToAvoid[totalDevicesToAvoid] = device;
        totalDevicesToAvoid++;
    }

    public Device getNetworkDevice(Device device) {
        for(Device currentDevice: network.getDevices()) {
            if (currentDevice.sameAs(device)) {
                return currentDevice;
            }
        }
        return null;
    }
}
