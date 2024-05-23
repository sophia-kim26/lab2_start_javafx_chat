package sockets;

public class MessageCtoS_Private extends Message {
    String recipientUserName;
    String msg;
    public MessageCtoS_Private(String r, String m) {
        this.recipientUserName = r;
        this.msg = m;
    }
}
