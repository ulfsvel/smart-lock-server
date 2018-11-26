package ro.cheiafermecata.smartlock.server.Data;

public enum Events {
    OPEN, CLOSE, ERROR, DISCONNECT, CONNECT, OPEN_REQUEST,CLOSE_REQUEST;

    public static String getIcon(Events event) {
        switch (event) {
            case OPEN:
                return "lock_open";
            case CLOSE:
                return "lock";
            case ERROR:
            default:
                return "error";
            case DISCONNECT:
                return "mobile_off";
            case CONNECT:
                return "mobile_friendly";
            case OPEN_REQUEST:
            case CLOSE_REQUEST:
                return "autorenew";
        }
    }
}
