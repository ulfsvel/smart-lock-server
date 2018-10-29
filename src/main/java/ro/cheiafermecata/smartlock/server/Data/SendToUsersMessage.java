package ro.cheiafermecata.smartlock.server.Data;

public class SendToUsersMessage {

    private String email;

    private String deviceName;

    private String actionType;

    private String actionContent;

    public SendToUsersMessage(){

    }

    public SendToUsersMessage(String email, String deviceName, String actionType, String actionContent) {
        this.email = email;
        this.deviceName = deviceName;
        this.actionType = actionType;
        this.actionContent = actionContent;
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

}
