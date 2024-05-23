package sockets;

public class MessageStoC_Private extends Message {
    public String fromUserName;
    public String msg;

    public MessageStoC_Private(String userName, String msg) {
        this.fromUserName = userName;
        this.msg = msg;
    }

    public String toString() {
        return fromUserName + ": " + msg;
    }
}
