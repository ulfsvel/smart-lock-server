package ro.cheiafermecata.smartlock.server.Data;


import javax.persistence.Entity;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
public class Event {

    private Long id;

    private Long userId;

    private Long deviceId;

    private String event;

    private String description;

    private String eventTime;

    public Event(Long id, Long userId, Long deviceId, String event, String description, String time) {
        this.id = id;
        this.userId = userId;
        this.deviceId = deviceId;
        this.event = event;
        this.description = description;
        this.eventTime = time;
    }

    public Event(Long userId, Long deviceId, String event, String description, String time) {
        this.userId = userId;
        this.deviceId = deviceId;
        this.event = event;
        this.description = description;
        this.eventTime = time;
    }

    public Event(Long userId, Long deviceId, String event, String description) {
        this.userId = userId;
        this.deviceId = deviceId;
        this.event = event;
        this.description = description;
        this.eventTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS").format(new Date());
    }

    public Event(){
        this.eventTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS").format(new Date());
    }

    public Long getId() {
        return id;
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


    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String time) {
        this.eventTime = time;
    }
}
