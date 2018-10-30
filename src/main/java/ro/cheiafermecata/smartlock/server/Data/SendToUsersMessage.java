package ro.cheiafermecata.smartlock.server.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SendToUsersMessage {

    private String email;

    private String deviceName;

    private String actionType;

    private String actionContent;

    private String time;

    public SendToUsersMessage(){
        this.time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public SendToUsersMessage(String email, String deviceName, String actionType, String actionContent) {
        this.email = email;
        this.deviceName = deviceName;
        this.actionType = actionType;
        this.actionContent = actionContent;
        this.time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
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
