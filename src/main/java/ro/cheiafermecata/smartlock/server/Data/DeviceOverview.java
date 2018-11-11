package ro.cheiafermecata.smartlock.server.Data;

import java.util.List;

public class DeviceOverview {

    private Device device;

    private String status;

    private List<Event> events;

    public DeviceOverview(Device device, String status, List<Event> events) {
        this.device = device;
        this.status = status;
        this.events = events;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}
