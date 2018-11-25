package ro.cheiafermecata.smartlock.server.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SendToUsersMessage {

    private Long deviceId;

    private String actionType;

    private String actionContent;

    private String time;

    private String deviceName;

    public SendToUsersMessage(){
        this.time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public SendToUsersMessage(String actionType, String actionContent) {
        this.actionType = actionType;
        this.actionContent = actionContent;
        this.time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public SendToUsersMessage(String actionType, String actionContent, Long deviceId, String deviceName) {
        this.actionType = actionType;
        this.actionContent = actionContent;
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getActionContent() {
        return actionContent;
    }

    public void setActionContent(String actionContent) {
        this.actionContent = actionContent;
    }

    public String getTime() {
        return time;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}
