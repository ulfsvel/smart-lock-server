package ro.cheiafermecata.smartlock.server.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SendToUsersMessage {

    private Long userId;

    private Long deviceId;

    private String actionType;

    private String actionContent;

    private String time;

    public SendToUsersMessage(){
        this.time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public SendToUsersMessage(Long userId, Long deviceId, String actionType, String actionContent) {
        this.userId = userId;
        this.deviceId = deviceId;
        this.actionType = actionType;
        this.actionContent = actionContent;
        this.time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
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
}
