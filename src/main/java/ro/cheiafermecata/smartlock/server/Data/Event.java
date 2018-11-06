package ro.cheiafermecata.smartlock.server.Data;


import javax.persistence.Entity;

@Entity
public class Event {

    private Long id;

    private Long userId;

    private Long deviceId;

    private String event;

    private String description;

    private String time;

    public Event(Long id, Long userId, Long deviceId, String event, String description, String time) {
        this.id = id;
        this.userId = userId;
        this.deviceId = deviceId;
        this.event = event;
        this.description = description;
        this.time = time;
    }

    public Event(Long userId, Long deviceId, String event, String description, String time) {
        this.userId = userId;
        this.deviceId = deviceId;
        this.event = event;
        this.description = description;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }


    public String getDate() {
        return time;
    }

    public void setDate(String time) {
        this.time = time;
    }
}
