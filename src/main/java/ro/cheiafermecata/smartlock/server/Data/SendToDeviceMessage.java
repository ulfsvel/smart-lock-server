package ro.cheiafermecata.smartlock.server.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SendToDeviceMessage {

    private Long deviceId;

    private String action;

    private String time;

    public SendToDeviceMessage(){
        this.time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public SendToDeviceMessage(Long userId, Long deviceId, String action) {
        this.deviceId = deviceId;
        this.action = action;
        this.time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public String getTime() {
        return time;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
